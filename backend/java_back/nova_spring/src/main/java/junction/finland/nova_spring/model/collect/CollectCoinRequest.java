package junction.finland.nova_spring.model.collect;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "The object that represent a request to collect a coin")
public class CollectCoinRequest {
    @Schema(name = "coin_id", description = "The id of coin")
    @NotNull(message = "coinId must not be null")
    @NotBlank(message = "coinId must not be blank")
    private Long coinId;
    @NotNull(message = "currentLat must not be null")
    @NotBlank(message = "currentLat must not be blank")
    @Schema(name = "current_lat", description = "the latitude that a request will be made of")
    private Double currentLat;
    @NotNull(message = "currentLon must not be null")
    @NotBlank(message = "currentLon must not be blank")
    @Schema(name = "current_lon", description = "the longitude that a request will be made of")
    private Double currentLon;
}
