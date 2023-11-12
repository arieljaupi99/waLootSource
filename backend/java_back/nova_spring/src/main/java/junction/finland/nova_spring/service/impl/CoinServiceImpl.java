package junction.finland.nova_spring.service.impl;

import junction.finland.nova_spring.entities.Coin;
import junction.finland.nova_spring.entities.User;
import junction.finland.nova_spring.exception.CoinAlreadyCompletedException;
import junction.finland.nova_spring.exception.ResourceNotFound;
import junction.finland.nova_spring.exception.UserNotFoundException;
import junction.finland.nova_spring.model.CoinsResponse;
import junction.finland.nova_spring.model.LocationRequest;
import junction.finland.nova_spring.model.UserAllCoinsResponse;
import junction.finland.nova_spring.model.collect.CollectCoinRequest;
import junction.finland.nova_spring.model.collect.CollectCoinResponse;
import junction.finland.nova_spring.model.nominative.ReverseResponse;
import junction.finland.nova_spring.repository.CoinRepo;
import junction.finland.nova_spring.repository.UserRepo;
import junction.finland.nova_spring.service.CoinService;
import junction.finland.nova_spring.utility.LocationUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junction.finland.nova_spring.component.NominativeApi.NOMINATIM_BASE_URL;


@Service
@Log4j2
public class CoinServiceImpl implements CoinService {
    private final RestTemplate nominativeRestTemplate;
    private final CoinRepo coinRepo;
    private final UserRepo userRepo;
    // Radius of the Earth in meters
    private static final double EARTH_RADIUS = 6371000;

    // Margin of error in meters
    private static final double MARGIN_OF_ERROR = 40;

    public CoinServiceImpl(RestTemplate nominativeRestTemplate, CoinRepo coinRepo, UserRepo userRepo) {
        this.nominativeRestTemplate = nominativeRestTemplate;
        this.coinRepo = coinRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<CoinsResponse> generateCoins(LocationRequest locationRequest) {
        User byId = this.userRepo.findById(Long.valueOf(locationRequest.getUserId())).orElse(null);
        if (byId == null) {
            throw new UserNotFoundException("The user with id " + locationRequest.getUserId() + " does not exists ");
        }
        double distanceMeters = (locationRequest.getDistance() == null || locationRequest.getDistance().isEmpty()) ? 1000 : Double.parseDouble(locationRequest.getDistance());
        String latitude = locationRequest.getLatitude();
        String longitude = locationRequest.getLongitude();
        Map<String, Map<String, Double>> stringMapMap = LocationUtils.calculateNewCoordinates(Double.parseDouble(latitude), Double.parseDouble(longitude), distanceMeters);
        //Bej thirrje tek nominative api percdo direction
        return this.generateCoinsForEachDirection(stringMapMap, locationRequest.getUserId(), locationRequest.getLatitude(), locationRequest.getLongitude());
    }

    @Override
    public CollectCoinResponse collectCoin(CollectCoinRequest collectCoinRequest) {
        //Gjej coin nga DB
        Coin coin = this.coinRepo.findById(collectCoinRequest.getCoinId()).orElse(null);
        if (coin == null) {
            throw new ResourceNotFound("Coin with id '" + collectCoinRequest.getCoinId() + "' was not found!");
        }
        this.validateCoin(coin);
        //Bej check nese kane te njejten lon lat (perfshi edhe nje marg gabimi)
        boolean shouldCollect = this.checkCoin(coin, collectCoinRequest);
        //Nese kalon check atehere bej status complete true dhe update coin
        if (shouldCollect) {
            coin.setCompleted(true);
            coin.setStopLatitude(collectCoinRequest.getCurrentLat());
            coin.setStopLongitude(collectCoinRequest.getCurrentLon());
            this.saveCoin(coin);
        }
        //Nese jo vazhdo
        //Gjenero response
        return CollectCoinResponse.loadFromCoin(coin);
    }

    @Override
    public CollectCoinResponse stopRequest(CollectCoinRequest collectCoinRequest) {
        //Gjej coin nga DB
        Coin coin = this.coinRepo.findById(collectCoinRequest.getCoinId()).orElse(null);
        if (coin == null) {
            throw new ResourceNotFound("Coin with id '" + collectCoinRequest.getCoinId() + "' was not found!");
        }
        if (coin.isStopped()) {
            throw new CoinAlreadyCompletedException("This coin has already a stop location");
        }
        coin.setStopLatitude(collectCoinRequest.getCurrentLat());
        coin.setStopLongitude(collectCoinRequest.getCurrentLon());
        this.saveCoin(coin);
        return CollectCoinResponse.loadFromCoin(coin);
    }

    @Override
    public UserAllCoinsResponse allCoinsForUser(String userId) {
        UserAllCoinsResponse response = new UserAllCoinsResponse();
        response.setCoins(this.coinRepo.findCoinsByUserIdIs(Long.valueOf(userId)));
        response.setUserId(userId);
        return response;
    }

    private void validateCoin(Coin coin) {

        if (Boolean.TRUE.equals(coin.getCompleted())) {
            throw new CoinAlreadyCompletedException("Coin with id '" + coin.getId() + "' is already completed");
        }

        if (coin.isStopped()) {
            throw new CoinAlreadyCompletedException("This coin has already a stop location");
        }
    }

    private boolean checkCoin(Coin coin, CollectCoinRequest collectCoinRequest) {
        double initialLatitude = Math.toRadians(coin.getLatitude());
        double initialLongitude = Math.toRadians(coin.getLongitude());

        double requestLatitude = Math.toRadians(collectCoinRequest.getCurrentLat());
        double requestLongitude = Math.toRadians(collectCoinRequest.getCurrentLon());

        // Haversine formula to calculate distance between two points on the Earth
        double dLat = initialLatitude - requestLatitude;
        double dLon = initialLongitude - requestLongitude;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(requestLatitude) * Math.cos(initialLatitude) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;

        return distance <= MARGIN_OF_ERROR;
    }

    private List<CoinsResponse> generateCoinsForEachDirection(Map<String, Map<String, Double>> stringMapMap, String userId, String startLatitude, String startLongitude) {
        List<CoinsResponse> coinsResponses = new ArrayList<>();
        for (Map.Entry<String, Map<String, Double>> entry : stringMapMap.entrySet()) {
            String direction = entry.getKey();
            Map<String, Double> coordinates = entry.getValue();
            CoinsResponse coin = new CoinsResponse();
            coin.setDirection(direction);
            // Create URL with parameters
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NOMINATIM_BASE_URL + "/reverse")
                    .queryParam("lat", coordinates.get("latitude"))
                    .queryParam("lon", coordinates.get("longitude"))
                    .queryParam("format", "jsonv2");
            ReverseResponse response = nominativeRestTemplate.getForEntity(builder.toUriString(), ReverseResponse.class).getBody();
            if (response != null) {
                coin.setResponse(response);
                Coin saved = this.coinRepo.save(Coin.loadFromResponse(response, userId, startLatitude, startLongitude));
                coin.setCoinId(saved.getId());
            }
            coinsResponses.add(coin);
        }
        return coinsResponses;
    }

    void saveCoin(Coin coin) {
        this.coinRepo.save(coin);
    }
}
