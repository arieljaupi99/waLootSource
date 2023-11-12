package junction.finland.nova_spring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import junction.finland.nova_spring.entities.Coin;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(name = "UserAllCoinsResponse" , description = "The object that represent a user wíth all its coins")
public class UserAllCoinsResponse {
    @Schema(name = "userId" , description = "The id of user")
    private String userId;
    @Schema(name = "coins" , description = "List of coins")
    private List<Coin> coins;
}
