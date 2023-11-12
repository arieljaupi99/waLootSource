package junction.finland.nova_spring.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@Getter
@Setter
@RequestScope
public class RequestData {

    private String lastClientDateTime;


}