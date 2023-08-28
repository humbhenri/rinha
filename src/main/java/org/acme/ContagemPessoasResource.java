package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("contagem-pessoas")
public class ContagemPessoasResource {
  
  @GET
  public Response contagem() {
    throw new UnsupportedOperationException();
  }
}
