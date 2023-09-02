package thedimas.aurora.database;

import lombok.extern.apachecommons.CommonsLog;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thedimas.aurora.database.gen.Tables;
import thedimas.aurora.database.gen.tables.daos.*;
import thedimas.aurora.database.gen.tables.pojos.Users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@CommonsLog
@SuppressWarnings("unused")
public class DatabaseService {
    private final ChatsDao chatsDao;
    private final MessagesDao messagesDao;
    private final TokensDao tokensDao;
    private final UsersDao usersDao;
    private final DSLContext context;

    @Autowired
    public DatabaseService(ChatsDao chatsDao, MessagesDao messagesDao, TokensDao tokensDao, UsersDao usersDao, DSLContext context) {
        this.chatsDao = chatsDao;
        this.messagesDao = messagesDao;
        this.tokensDao = tokensDao;
        this.usersDao = usersDao;
        this.context = context;
    }

    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username The username to check for existence.
     * @return {@code true} if a user with the specified username exists, otherwise {@code false}.
     */
    public boolean userExists(String username) {
        return context.fetchExists(Tables.USERS, Tables.USERS.USERNAME.eq(username));
    }

    /**
     * Creates a new user with the provided name, username, and password.
     * The password is hashed using the SHA-256 algorithm before storing it.
     *
     * @param name     The name of the user.
     * @param username The username of the user.
     * @param password The user's password in plaintext.
     * @return The newly created {@link Users} instance.
     */
    public Users createUser(String name, String username, String password) {
        Users user = new Users().setName(name)
                .setUsername(username)
                .setPassword(sha256(password));
        usersDao.insert(user);
        return user;
    }

    /**
     * Computes the SHA-256 hash of the given input string and returns it as a hexadecimal string.
     *
     * @param input The input string to hash.
     * @return A hexadecimal string representing the SHA-256 hash of the input, or {@code null} if an error occurs.
     */
    public String sha256(String input) {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = input.getBytes();
            byte[] hashBytes = sha256Digest.digest(inputBytes);
            StringBuilder builder = new StringBuilder();
            for (byte b : hashBytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to hashify", e);
            return null;
        }
    }
}
