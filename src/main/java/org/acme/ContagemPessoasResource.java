package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import io.smallrye.mutiny.Uni;

@Path("contagem-pessoas")
public class ContagemPessoasResource {
  
  @GET
  @Produces("text/plain")
  public Uni<Long> contagem() {
    return PessoaEntity.count();
  }
}
