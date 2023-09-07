package thedimas.aurora.database;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thedimas.aurora.database.gen.Tables;
import thedimas.aurora.database.gen.tables.daos.*;
import thedimas.aurora.database.gen.tables.pojos.Chats;
import thedimas.aurora.database.gen.tables.pojos.Messages;
import thedimas.aurora.database.gen.tables.pojos.Tokens;
import thedimas.aurora.database.gen.tables.pojos.Users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@CommonsLog
@Getter
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

    // region users
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
     * Creates a new user with the provided name, username, and password, and stores it in the database.
     * The password is securely hashed using the SHA-256 algorithm before storage.
     *
     * @param name     The name of the user.
     * @param username The username of the user, which should be unique.
     * @param password The user's password in plaintext.
     * @return The newly created {@link Users} instance representing the user.
     * @throws IllegalArgumentException If a user with the provided username already exists.
     */
    public Users createUser(String name, String username, String password) throws IllegalArgumentException {
        if (userExists(username)) {
            throw new IllegalArgumentException("A user with the provided username already exists.");
        }
        Users user = new Users().setName(name)
                .setUsername(username)
                .setPassword(sha256(password));
        usersDao.insert(user);
        return user;
    }


    /**
     * Retrieves a user from the database by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The {@link Users} instance representing the user, or {@code null} if the user is not found.
     */
    public Users getUser(String username) {
        return usersDao.fetchOneByUsername(username);
    }

    /**
     * Retrieves a user from the database by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The {@link Users} instance representing the user, or {@code null} if the user is not found.
     */
    public Users getUser(int userId) {
        return usersDao.fetchOneById(userId);
    }


    /**
     * Creates a new token for a user with the provided user ID and IP address, and stores it in the database.
     *
     * @param userId The ID of the user for whom the token is created.
     * @param ip     The IP address associated with the token.
     * @return The newly created {@link Tokens} instance representing the token.
     */
    public Tokens createToken(int userId, String ip) {
        Tokens token = new Tokens()
                .setUser(userId)
                .setToken(generateToken())
                .setIp(ip);
        tokensDao.insert(token);
        return token;
    }

    /**
     * Retrieves a token from the database based on the provided token string.
     *
     * @param token The token string used to look up the token.
     * @return The {@link Tokens} instance representing the token, or {@code null} if the token is not found.
     */
    public Tokens getToken(String token) {
        return tokensDao.fetchOneByToken(token);
    }
    // endregion

    // region chats and messages

    /**
     * Retrieves a chat from the database based on the provided chat ID.
     *
     * @param id The unique identifier of the chat to retrieve.
     * @return The {@link Chats} instance representing the chat, or {@code null} if the chat is not found.
     */
    public Chats getChat(int id) {
        return chatsDao.fetchOneById(id);
    }

    /**
     * Creates a new message in the database with the provided sender ID, chat ID, and message content.
     *
     * @param senderId The ID of the user who sent the message.
     * @param chatId   The ID of the chat where the message is sent.
     * @param content  The content of the message.
     * @return The newly created {@link Messages} instance representing the message.
     */
    public Messages createMessage(int senderId, int chatId, String content) {
        Messages message = new Messages()
                .setAuthor(senderId)
                .setChat(chatId)
                .setContent(content);
        messagesDao.insert(message);
        return message;
    }

    /**
     * Creates and sends a new message in the database with the provided sender ID, chat ID, and message content.
     * Alias for {@link #createMessage(int, int, String)}.
     *
     * @param senderId The ID of the user who sent the message.
     * @param chatId   The ID of the chat where the message is sent.
     * @param content  The content of the message.
     * @return The newly created {@link Messages} instance representing the message.
     */
    public Messages sendMessage(int senderId, int chatId, String content) {
        return createMessage(senderId, chatId, content);
    }
    // endregion


    // region util
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

    /**
     * Generates a random secure token.
     *
     * @return A random secure token as a String.
     */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
    // endregion
}
