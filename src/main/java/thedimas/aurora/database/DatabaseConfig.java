package thedimas.aurora.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import thedimas.aurora.JsonPropertySourceFactory;

@Configuration
@PropertySource(
        value = "file:database.json",
        factory = JsonPropertySourceFactory.class)
@ConfigurationProperties
@Data
class DatabaseConfig {
    private String host, schema, username, password;

    /**
     * Gets the connection URL for the database.
     *
     * @return The connection URL as a string.
     */
    public String getConnectionUrl() {
        return String.format("jdbc:mysql://%s:%d/%s", host, 3306, schema);
    }
}
