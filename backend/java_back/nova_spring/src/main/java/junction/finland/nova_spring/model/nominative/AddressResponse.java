package junction.finland.nova_spring.model.nominative;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Schema(name = "AddressResponse", description = "Describe the address response from reverse call of NOMINATIVE API")
@ToString
public class AddressResponse {
    private String road;
    private String city;
    private String state;
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
}
