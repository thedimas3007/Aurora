package thedimas.aurora.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thedimas.aurora.database.DatabaseService;
import thedimas.aurora.database.gen.tables.pojos.Users;
import thedimas.aurora.response.ApiResponse;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {
    @Autowired
    private DatabaseService database;

    @GetMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam(name = "user") String login,
            @RequestParam(name = "password") String password,
            HttpServletRequest request
    ) {
        Users user = database.getUser(login);
        if (user == null) {
            return ApiController.error(HttpStatus.NOT_FOUND, "User not found");
        }

        if (user.getPassword().equals(database.sha256(password))) {
            return ApiController.error(HttpStatus.FORBIDDEN, "Invalid password");
        }

        return ApiController.success("User logged in successfully", database.createToken(user.getId(), request.getRemoteAddr()));
    }

    @GetMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password
    ) {
        if (database.userExists(username)) {
            return ApiController.error(HttpStatus.CONFLICT, "User already exists");
        }

        return ApiController.success(HttpStatus.CREATED, "User successfully created", database.createUser(name, username, password));
    }
}
