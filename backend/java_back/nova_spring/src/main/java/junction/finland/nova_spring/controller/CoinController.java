package junction.finland.nova_spring.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import junction.finland.nova_spring.model.CoinsResponse;
import junction.finland.nova_spring.model.RequestData;
import junction.finland.nova_spring.model.UserAllCoinsResponse;
import junction.finland.nova_spring.model.collect.CollectCoinRequest;
import junction.finland.nova_spring.model.collect.CollectCoinResponse;
import junction.finland.nova_spring.model.LocationRequest;
import junction.finland.nova_spring.model.auth.NovaUser;
import junction.finland.nova_spring.service.CoinService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static junction.finland.nova_spring.utility.RequestDataUtility.getCurrentDateTime;

@RestController
@RequestMapping("coins")
@Tag(name = "CoinController")
@Log4j2
public class CoinController {
    private final CoinService coinService;
    @Autowired
    RequestData requestData;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @PostMapping("/reverse")
    public ResponseEntity<List<CoinsResponse>> generateCoins(
            @Valid @RequestBody LocationRequest locationRequest,
            @RequestAttribute("user") NovaUser novaUser

    ) {
        log.info("{} *** {} ->Generating coins for user {} ", new Date(), getClass().getSimpleName(), novaUser.getEmail());
        locationRequest.setUserId(String.valueOf(novaUser.getId()));
        log.info("Request is : {}" , locationRequest.toString());
        return ResponseEntity.ok(this.coinService.generateCoins(locationRequest));
    }

    @PostMapping("/collect")
    public ResponseEntity<CollectCoinResponse> collectCoin(
            @Valid @RequestBody CollectCoinRequest collectCoinRequest,
            @RequestAttribute("user") NovaUser novaUser

    ){
        this.requestData.setLastClientDateTime(getCurrentDateTime());
        log.info("{} *** {} Coin Collection request from user '{}'" , new Date() , getClass().getSimpleName() , novaUser.getEmail());
        return ResponseEntity.ok(this.coinService.collectCoin(collectCoinRequest));
    }

    @PostMapping("/stop")
    public ResponseEntity<CollectCoinResponse> stopCoin(
            @RequestBody CollectCoinRequest collectCoinRequest,
            @RequestAttribute("user") NovaUser novaUser
    ){
        log.info("{} *** {} Stop request from user '{}'" , new Date() , getClass().getSimpleName() , novaUser.getEmail());
        return ResponseEntity.ok(this.coinService.stopRequest(collectCoinRequest));
    }

    @GetMapping("/allCoins")
    public ResponseEntity<UserAllCoinsResponse> getAllUserWithAllCoins(
            @RequestParam("user_id") String userId,
            @RequestAttribute("user") NovaUser novaUser
    ) {
        log.info("Request to get all coins with user id :'"+userId+"' made by : " + novaUser.getEmail());
        return ResponseEntity.ok(this.coinService.allCoinsForUser(userId));
    }

}
