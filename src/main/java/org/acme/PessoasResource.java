package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.validation.Valid;
import org.jboss.logging.Logger;
import jakarta.transaction.*;
import org.postgresql.util.PSQLException;
import jakarta.persistence.*;
import java.util.*;

@Path("/pessoas")
@Produces("application/json")
@Consumes("application/json")
public class PessoasResource {

  Logger log = Logger.getLogger(PessoasResource.class);
  
  @POST
  @Transactional
  public Response cria(@Valid PessoaDTO pessoa) {
    try {
      var pessoaEntity = new PessoaEntity();
      pessoaEntity.nome = pessoa.getNome();
      pessoaEntity.apelido = pessoa.getApelido();
      pessoaEntity.nascimento = pessoa.getNascimento();
      pessoaEntity.persistAndFlush(); // force 
      return Response.noContent().build();
    } catch(PersistenceException e) {
      log.error(e.getMessage(), e);
      return Response.status(422).entity(e.getMessage()).build();
    }
  }

  @GET
  public Response lista() {
    throw new UnsupportedOperationException();
  }

  @GET
  @Path("/{id}")
  public Response consulta(@PathParam("id") String idPessoa) {
    log.info("uuid = " + idPessoa);
    var pessoa = PessoaEntity.findByIdOptional(UUID.fromString(idPessoa))
      .orElseThrow(() -> new NotFoundException());
    return Response.ok(pessoa).build();
  }

}
