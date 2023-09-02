package thedimas.aurora;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import thedimas.aurora.database.DatabaseService;
import thedimas.aurora.database.gen.tables.pojos.Users;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {
    @Autowired
    private DatabaseService database;

    @GetMapping("/login")
    public String login() {
        return "WIP";
    }

    @GetMapping("/register")
    public Users register(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password
    ) {
        return database.createUser(name, username, password);
    }
}
