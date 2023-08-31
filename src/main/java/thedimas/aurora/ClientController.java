package thedimas.aurora;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {
    @GetMapping("/login")
    public String login() {
        return "WIP";
    }

    @PostMapping("/register")
    public String register() {
        return "WIP";
    }
}
