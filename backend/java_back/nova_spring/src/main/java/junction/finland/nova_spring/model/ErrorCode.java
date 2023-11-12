package junction.finland.nova_spring.model;

import lombok.Getter;

@Getter
public enum ErrorCode {
    GENERIC_ERROR("01", "Generic error"),
    BAD_REQUEST("02", "Bad request"),
    CONNECTION_TIMEOUT("03", "Connection timeout"),
    INTERNAL_SERVER_ERROR("04", "Internal server error"),
    INVALID_INPUT("05", "Invalid input"),
    INPUT_SYNTAX_ERROR("06", " Input syntax error not valid"),
    NOT_FOUND_ERROR("07", " Resource not found"),
    NOMINATIVE_API_ERROR("08", "Internal API NOMINATIVE Error!");

    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getFullDescription() {
        return this.getCode() + " - " + this.getDescription();
    }
}