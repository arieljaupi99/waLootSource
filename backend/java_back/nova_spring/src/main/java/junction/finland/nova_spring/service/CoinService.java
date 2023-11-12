package junction.finland.nova_spring.service;

import junction.finland.nova_spring.model.CoinsResponse;
import junction.finland.nova_spring.model.LocationRequest;
import junction.finland.nova_spring.model.UserAllCoinsResponse;
import junction.finland.nova_spring.model.collect.CollectCoinRequest;
import junction.finland.nova_spring.model.collect.CollectCoinResponse;

import java.util.List;

public interface CoinService {
    List<CoinsResponse> generateCoins(LocationRequest locationRequest);

    CollectCoinResponse collectCoin(CollectCoinRequest collectCoinRequest);

    CollectCoinResponse stopRequest(CollectCoinRequest collectCoinRequest);

    UserAllCoinsResponse allCoinsForUser(String userId);
}
