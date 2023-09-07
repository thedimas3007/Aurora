package thedimas.aurora.database.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Configuration
public class JooqConfiguration {
    @Autowired
    private HikariDataSource dataSource;

    @Bean
    public org.jooq.Configuration getConfiguration() {
        return new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.MYSQL);
    }
}
