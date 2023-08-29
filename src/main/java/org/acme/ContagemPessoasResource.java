package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("contagem-pessoas")
public class ContagemPessoasResource {
  
  @GET
  @Produces("text/plain")
  public long contagem() {
    return PessoaEntity.count();
  }
}
