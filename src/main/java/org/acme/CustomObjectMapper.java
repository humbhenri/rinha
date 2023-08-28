package org.acme;

import com.fasterxml.jackson.databind.*;
import io.quarkus.arc.All;
import io.quarkus.jackson.*;
import java.util.List;
import jakarta.inject.*;
import jakarta.enterprise.inject.Produces;
import org.jboss.logging.Logger;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Singleton
public class CustomObjectMapper implements ObjectMapperCustomizer {

  Logger log = Logger.getLogger(CustomObjectMapper.class);

  @Override
  public void customize(ObjectMapper mapper) {
    log.info("passou ");
    // mapper.disable(MapperFeature.ALLOW_COERCION_OF_SCALARS);
    var module = new SimpleModule();
    module.addDeserializer(String.class, new CoercionLessStringDeserializer());
    mapper.registerModule(module);
  }
  
}
