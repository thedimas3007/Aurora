package thedimas.aurora.database;

import lombok.extern.apachecommons.CommonsLog;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thedimas.aurora.database.gen.Tables;
import thedimas.aurora.database.gen.tables.daos.*;
import thedimas.aurora.database.gen.tables.pojos.Users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@CommonsLog
@SuppressWarnings("unused")
public class DatabaseService {
    private final ChatsDao chatsDao;
    private final MessagesDao messagesDao;
    private final TokensDao tokensDao;
    private final UsersDao usersDao;
    private final Configuration jooqConfiguration;

    @Autowired
    public DatabaseService(
            ChatsDao chatsDao,
            MessagesDao messagesDao,
            TokensDao tokensDao,
            UsersDao usersDao,
            Configuration jooqConfiguration) {
        this.chatsDao = chatsDao;
        this.messagesDao = messagesDao;
        this.tokensDao = tokensDao;
        this.usersDao = usersDao;
        this.jooqConfiguration = jooqConfiguration;
    }

    // region raw
    /**
     * Retrieves the {@link DSLContext} for database queries.
     *
     * @return The DSL context.
     */
    public DSLContext getContext() {
        return DSL.using(jooqConfiguration);
    }
    // endregion

    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username The username to check for existence.
     * @return {@code true} if a user with the specified username exists, otherwise {@code false}.
     */
    public boolean userExists(String username) {
        return getContext().fetchExists(Tables.USERS, Tables.USERS.USERNAME.eq(username));
    }

    public Users createUser(String name, String username, String password) {
        Users user = new Users().setName(name)
                .setUsername(username)
                .setPassword(sha256(password));
        usersDao.insert(user);
        return user;
    }

    public List<Users> getUsers() {
        return usersDao.findAll();
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
