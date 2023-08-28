package org.acme;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.validation.*;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
  public Response toResponse(ConstraintViolationException e) {
    return Response.status(422).entity(e.getMessage()).build();
  }
}
