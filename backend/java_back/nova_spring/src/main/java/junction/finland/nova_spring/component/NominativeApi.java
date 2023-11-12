package junction.finland.nova_spring.component;

import junction.finland.nova_spring.model.nominative.CustomResponseErrorHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@Log4j2
public class NominativeApi {

    // Define the base URL of the Nominatim API
    public static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org";


    @Bean
    public RestTemplate nominativeRestTemplate() {
        // Set a custom user-agent header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("User-Agent", "Junction Nova");

        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        // Set the custom headers to the RestTemplate instance
        restTemplate.setInterceptors(Collections.singletonList(new CustomRequestInterceptor(httpHeaders)));

        // Configure a custom error handler
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());


        log.info("Creating a restTemplate for Nominative API");
        return restTemplate;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(10000);    // 10 seconds

        return factory;
    }
}
