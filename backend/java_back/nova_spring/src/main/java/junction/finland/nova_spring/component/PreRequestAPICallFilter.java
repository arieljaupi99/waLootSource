package junction.finland.nova_spring.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
public class PreRequestAPICallFilter implements HandlerInterceptor {


    private final LaravelApi laravelApi;

    public PreRequestAPICallFilter(LaravelApi laravelApi) {
        this.laravelApi = laravelApi;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // This method is called before the actual handler method is invoked.
        // You can log the request information here.
        String requestURI = request.getRequestURI();
        String remoteAddress = request.getRemoteAddr();
        log.info("Request received from " + remoteAddress + " to " + requestURI);
        //Bej auth
        laravelApi.authenticate(request);
        return true;
    }
}
