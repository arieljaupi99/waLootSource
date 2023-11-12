package junction.finland.nova_spring.component;

import jakarta.servlet.http.HttpServletRequest;
import junction.finland.nova_spring.model.auth.AuthenticationCustomResponseErrorHandle;
import junction.finland.nova_spring.model.auth.NovaUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@Log4j2
public class LaravelApi {

    // Define the base URL of the Nominatim API
    @Value("${laravel.base.url}")
    public String laravelBasePath;
    private final Environment env;

    public LaravelApi(Environment env) {
        this.env = env;
    }


    public void authenticate(HttpServletRequest request) {
        // Set a custom user-agent header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("User-Agent", "Spring Nova");
        httpHeaders.set("Authorization", request.getHeader("Authorization"));

        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        // Set the custom headers to the RestTemplate instance
        restTemplate.setInterceptors(Collections.singletonList(new CustomRequestInterceptor(httpHeaders)));
        // Configure a custom error handler
        restTemplate.setErrorHandler(new AuthenticationCustomResponseErrorHandle());
        NovaUser novaUser = new NovaUser();
        if (!containSwagger(request.getRequestURL().toString())) {
            novaUser = restTemplate.postForEntity(laravelBasePath + "/check/token", null, NovaUser.class).getBody();
        }
        request.setAttribute("user", novaUser);
        log.info("Successfully authenticate the JWT from Laravel API");
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(10000);    // 10 seconds

        return factory;
    }

    private boolean containSwagger(String url) {
        boolean contains = false;
        List<String> swaggerPaths = List.of("swagger-ui", "v3", "api-docs");
        for (String swaggerPath : swaggerPaths) {
            if (url.contains(swaggerPath)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

}
