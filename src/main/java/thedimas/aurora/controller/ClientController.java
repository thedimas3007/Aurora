package thedimas.aurora.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thedimas.aurora.database.DatabaseService;
import thedimas.aurora.database.gen.tables.pojos.Tokens;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {
    @Autowired
    private DatabaseService database;

    @GetMapping("/login")
    public Tokens login(
            @RequestParam(name = "user") int userId,
            HttpServletRequest request
    ) {
        return database.createToken(userId, request.getRemoteAddr());
    }

    @GetMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password
    ) {
        if (database.userExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username is already in use");
        }

        return ResponseEntity.ok(database.createUser(name, username, password));
    }
}
