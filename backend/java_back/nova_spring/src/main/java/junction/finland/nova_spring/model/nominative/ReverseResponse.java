package junction.finland.nova_spring.model.nominative;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@Schema(name = "ReverseResponse", description = "The response that we expect when we call /revers of Nominative API")
@ToString
public class ReverseResponse {
    @JsonProperty("place_id")
    private int placeId;
    @JsonProperty("osm_type")
    private String osmType;
    private String lat;
    private String lon;
    private String category;
    @JsonProperty("osm_id")
    private String osmId;
    private String type;
    @JsonProperty("addresstype")
    private String addressType;
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    private AddressResponse address;
    @JsonProperty("boundingbox")
    private ArrayList<String> boundingBox;
}
