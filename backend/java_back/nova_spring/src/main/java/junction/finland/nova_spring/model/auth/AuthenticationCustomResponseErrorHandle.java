package junction.finland.nova_spring.model.auth;

import junction.finland.nova_spring.exception.AuthenticationCustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@Log4j2
public class AuthenticationCustomResponseErrorHandle implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("An error is occured from Laravel API Authentication : -> {}", response.getStatusText());
        throw new AuthenticationCustomException("Laravel API error: " + response.getStatusCode());
    }
}
