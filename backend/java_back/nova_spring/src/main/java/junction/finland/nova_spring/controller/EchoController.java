package junction.finland.nova_spring.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class EchoController {


    @GetMapping("/echo")
    @ResponseStatus(HttpStatus.OK)
    public String echo(
    ) {
        return "echo";
    }
}
