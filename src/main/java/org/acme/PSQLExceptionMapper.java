package org.acme;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.validation.*;
import org.postgresql.util.PSQLException;

@Provider
public class PSQLExceptionMapper implements ExceptionMapper<PSQLException> {
  public Response toResponse(PSQLException e) {
    return Response.status(422).entity(e.getMessage()).build();
  }
}

