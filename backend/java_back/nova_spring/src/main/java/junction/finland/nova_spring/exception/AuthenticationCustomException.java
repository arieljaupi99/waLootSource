package junction.finland.nova_spring.exception;

public class AuthenticationCustomException extends RuntimeException {
    public AuthenticationCustomException(String message) {
        super(message);
    }
}
