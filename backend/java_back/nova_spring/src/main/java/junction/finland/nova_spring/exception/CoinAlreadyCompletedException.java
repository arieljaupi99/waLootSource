package junction.finland.nova_spring.exception;

public class CoinAlreadyCompletedException extends RuntimeException {
    public CoinAlreadyCompletedException(String message) {
        super(message);
    }
}
