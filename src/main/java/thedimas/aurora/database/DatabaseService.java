package thedimas.aurora.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.apachecommons.CommonsLog;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thedimas.aurora.database.gen.Tables;
import thedimas.aurora.database.gen.tables.daos.ChatsDao;
import thedimas.aurora.database.gen.tables.daos.MessagesDao;
import thedimas.aurora.database.gen.tables.daos.TokensDao;
import thedimas.aurora.database.gen.tables.daos.UsersDao;
import thedimas.aurora.database.gen.tables.pojos.Users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@CommonsLog
@SuppressWarnings("unused")
public class DatabaseService {
    @Autowired
    private DatabaseConfig config;
    private Configuration jooqConfiguration;
    private static HikariDataSource dataSource;
    private static HikariConfig hikariConfig;

    public DatabaseService() {
    }

    // region raw
    /**
     * Retrieves the database {@link HikariDataSource}.
     *
     * @return The database {@link HikariDataSource}.
     */
    public HikariDataSource getDataSource() {
        if (dataSource == null || dataSource.isClosed()) {
            hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(config.getConnectionUrl());
            hikariConfig.setUsername(config.getUsername());
            hikariConfig.setPassword(config.getPassword());

            dataSource = new HikariDataSource(hikariConfig);
        }
        return dataSource;
    }

    /**
     * Retrieves the {@link Configuration} for  {@link DSLContext}.
     *
     * @return The database {@link Configuration}.
     */
    public Configuration getConfiguration() {
        if (jooqConfiguration == null) {
            jooqConfiguration = new DefaultConfiguration()
                    .set(getDataSource())
                    .set(SQLDialect.MYSQL);
        }
        return jooqConfiguration;
    }

    /**
     * Retrieves the {@link DSLContext} for database queries.
     *
     * @return The DSL context.
     */
    public DSLContext getContext() {
        return DSL.using(getConfiguration());
    }
    // endregion

    // region daos
    /**
     * Returns a new instance of the {@link ChatsDao}.
     *
     * @return A {@link ChatsDao} instance.
     */
    public ChatsDao chatsDao() {
        return new ChatsDao(getConfiguration());
    }

    /**
     * Returns a new instance of the {@link MessagesDao}.
     *
     * @return A {@link MessagesDao} instance.
     */
    public MessagesDao messagesDao() {
        return new MessagesDao(getConfiguration());
    }

    /**
     * Returns a new instance of the {@link TokensDao}.
     *
     * @return A {@link TokensDao} instance.
     */
    public TokensDao tokensDao() {
        return new TokensDao(getConfiguration());
    }

    /**
     * Returns a new instance of the {@link UsersDao}.
     *
     * @return A {@link UsersDao} instance.
     */
    public UsersDao usersDao() {
        return new UsersDao(getConfiguration());
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
        usersDao().insert(user);
        return user;
    }

    public List<Users> getUsers() {
        return usersDao().findAll();
    }

    /**
     * Computes the SHA-256 hash of the given input string and returns it as a hexadecimal string.
     *
     * @param input The input string to hash.
     * @return A hexadecimal string representing the SHA-256 hash of the input, or {@code null} if an error occurs.
     */
    public static String sha256(String input) {
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
