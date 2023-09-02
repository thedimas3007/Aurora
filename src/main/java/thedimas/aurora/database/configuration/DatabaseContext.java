package thedimas.aurora.database.configuration;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseContext {
    @Autowired
    private org.jooq.Configuration configuration;

    public DSLContext getContext() {
        return DSL.using(configuration);
    }
}
