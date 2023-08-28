package org.acme;

import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import java.io.*;
import com.fasterxml.jackson.databind.deser.std.*;
import java.text.MessageFormat;
import com.fasterxml.jackson.databind.exc.*;

public class CoercionLessStringDeserializer extends StringDeserializer {

  @Override
  public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

    List<JsonToken> forbiddenTypes =
        Arrays.asList(
            JsonToken.VALUE_NUMBER_INT,
            JsonToken.VALUE_NUMBER_FLOAT,
            JsonToken.VALUE_TRUE,
            JsonToken.VALUE_FALSE);

    if (forbiddenTypes.contains(p.getCurrentToken())) {
      String message =
          MessageFormat.format("Cannot coerce {0} to String value", p.getCurrentToken());
      throw MismatchedInputException.from(p, String.class, message);
    }
    return super.deserialize(p, ctxt);
  }
}
