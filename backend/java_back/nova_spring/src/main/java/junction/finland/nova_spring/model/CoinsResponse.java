package junction.finland.nova_spring.model;


import io.swagger.v3.oas.annotations.media.Schema;
import junction.finland.nova_spring.model.nominative.ReverseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "CoinsResponse", description = "The object that represent a coin")
public class CoinsResponse {
    @Schema(name = "coinId" , description = "the coin id")
    private Long coinId;
    @Schema(name = "direction", description = "direction of coin", example = "WEST|EAST|NORTH|SOUTH")
    private String direction;
    @Schema(name = "response" , description = "the response that we get back from nominative API")
    private ReverseResponse response;
}
