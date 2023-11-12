package junction.finland.nova_spring.model.collect;

import io.swagger.v3.oas.annotations.media.Schema;
import junction.finland.nova_spring.entities.Coin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "CollectCoinResponse", description = "The object that will be returned when a collection request will be made")
public class CollectCoinResponse {
    @Schema(name = "coin_id", description = "the id of the coin")
    private Long coinId;
    @Schema(name = "collected" , description = "indicate if the coins is collected or not")
    private Boolean collected;
    @Schema(name = "stopped" , description = "indicate if the coins has a stopped location")
    private Boolean stopped;

    public static CollectCoinResponse loadFromCoin(Coin coin) {
        CollectCoinResponse collectCoinResponse = new CollectCoinResponse();
        collectCoinResponse.setCoinId(coin.getId());
        collectCoinResponse.setCollected(coin.getCompleted());
        collectCoinResponse.setStopped(coin.isStopped());
        return collectCoinResponse;
    }
}
