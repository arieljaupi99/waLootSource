package junction.finland.nova_spring.config;

import junction.finland.nova_spring.component.LaravelApi;
import junction.finland.nova_spring.component.PreRequestAPICallFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LaravelApi laravelApi;

    public WebConfig(LaravelApi laravelApi) {
        this.laravelApi = laravelApi;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PreRequestAPICallFilter(laravelApi));
    }
}
