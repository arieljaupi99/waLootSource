package junction.finland.nova_spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Schema(name = "LocationRequest", description = "Present the request that is made to generate coins")
@ToString
public class LocationRequest {
    @NotNull(message = "Latitude should not null")
    @NotBlank(message = "Latitude should not be empty")
    @Schema(name = "latitude", description = "location latitude")
    private String latitude;
    @NotNull(message = "Longitude should not null")
    @NotBlank(message = "Longitude should not be empty")
    @Schema(name = "longitude", description = "location longitude")
    private String longitude;
    @Schema(name = "distance", description = "tell how far from current location will coins be generated")
    private String distance;
    @Schema(name = "userId" , description = "user which the coins will be generated")
    @JsonProperty("user_id")
    private String userId;
}
