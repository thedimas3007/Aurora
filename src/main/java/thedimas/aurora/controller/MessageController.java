package thedimas.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thedimas.aurora.database.DatabaseService;
import thedimas.aurora.database.gen.tables.pojos.*;

@RestController
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    @Autowired
    private DatabaseService database;

    @PostMapping("/send") // TODO: not @RequestParams
    public ResponseEntity<?> send(
         @RequestParam(name = "message") String content,
         @RequestParam(name = "chat") int chatId,
         @RequestParam(name = "token") String tokenString
    ) {
        Tokens token = database.getToken(tokenString);
        if (token == null) {
            return ApiController.error(HttpStatus.FORBIDDEN, "Invalid token specified");
        } else if (!token.getActive()) {
            return ApiController.error(HttpStatus.FORBIDDEN, "Inactive token specified");
        }

        Chats chat = database.getChat(chatId);
        if (chat == null) {
            return ApiController.error(HttpStatus.NOT_FOUND, "Target chat not found");
        }

        Users user = database.getUser(token.getUser());
        return ApiController.success("Message has been successfully sent", database.createMessage(user.getId(), chatId, content));
    }
}
