package junction.finland.nova_spring.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ErrorMessage", description = "Object containing error information.")
public class ErrorResponse {

    @Schema(description = "the HTTP status code applicable to this problem", example = "400")
    private String status;

    @NotNull
    @Schema(description = "the error code for", allowableValues = {"01", "02", "03", "04", "05", "06"})
    private String code;

    @Schema(description = "explanation specific to this occurrence of the problem (could be the stack trace)", example = "Missing field: email")
    private String detail;

    @JsonIgnore
    private ErrorCode errorCode;

    @Schema(description = "the date time sent by client", example = "2022-11-10T15:54:25.681Z")
    private String clientDateTime;

    @Schema(description = "the current date time of the server", example = "2022-11-10T15:55:01.000Z")
    private String serverDateTime;

}