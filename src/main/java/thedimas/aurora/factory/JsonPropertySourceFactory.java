package thedimas.aurora.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Map;

public class JsonPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, @NotNull EncodedResource resource) throws IOException {
        Map readValue = new ObjectMapper().readValue(resource.getInputStream(), Map.class);
        return new MapPropertySource("json-property", readValue);
    }
}
