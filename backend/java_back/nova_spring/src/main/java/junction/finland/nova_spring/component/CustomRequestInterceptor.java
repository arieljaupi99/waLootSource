package junction.finland.nova_spring.component;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomRequestInterceptor implements ClientHttpRequestInterceptor {
    private HttpHeaders headers;

    public CustomRequestInterceptor(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // Add custom headers to the request
        request.getHeaders().addAll(headers);
        // Continue with the request execution
        return execution.execute(request, body);
    }
}
