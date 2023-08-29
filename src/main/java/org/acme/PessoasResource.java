package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.validation.Valid;
import org.jboss.logging.Logger;
import jakarta.transaction.*;
import jakarta.persistence.*;
import java.util.*;
import java.util.stream.*;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

@Path("/pessoas")
@Produces("application/json")
@Consumes("application/json")
public class PessoasResource {

  Logger log = Logger.getLogger(PessoasResource.class);
  
  @POST
  @Transactional
  public Response cria(@Valid PessoaDTO pessoa, @Context UriInfo uriInfo) {
    try {
      var pessoaEntity = new PessoaEntity();
      pessoaEntity.nome = pessoa.getNome();
      pessoaEntity.apelido = pessoa.getApelido();
      pessoaEntity.nascimento = pessoa.getNascimento();
      if (pessoa.getStack() != null) {
        pessoaEntity.stack = pessoa.getStack().stream().collect(Collectors.joining(","));
      }
      pessoaEntity.persistAndFlush(); // force exceptions
      UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
      uriBuilder.path(pessoaEntity.id.toString());
      return Response.created(uriBuilder.build()).build();
    } catch(PersistenceException e) {
      log.error(e.getMessage(), e);
      return Response.status(422).entity(e.getMessage()).build();
    }
  }

  @GET
  @Path("/{id}")
  public Response consulta(@PathParam("id") String idPessoa) {
    var pessoa = PessoaEntity.findByIdOptional(UUID.fromString(idPessoa))
      .orElseThrow(() -> new NotFoundException());
    return Response.ok(new PessoaDTO((PessoaEntity) pessoa)).build();
  }

  @GET
  public Response pesquisa(@QueryParam("t") String termoBusca) {
    if (termoBusca != null && termoBusca.length() == 0) {
      return Response.status(404).build();
    }
    var predicates = new ArrayList<String>();
    var params = new HashMap<String, Object>();
    if (termoBusca != null) {
      predicates.add("apelido ilike :termoBusca");
      predicates.add("nome ilike :termoBusca");
      predicates.add("stack ilike :termoBusca");
      params.put("termoBusca", "%"+termoBusca+"%");
    }
    var whereClauses = predicates.stream().collect(Collectors.joining(" or "));
    log.info(whereClauses);
    var pessoas = predicates.isEmpty() ? PessoaEntity.listAll() :
      PessoaEntity.find(whereClauses, params).page(Page.ofSize(50)).list();
    var dtos = pessoas
      .stream()
      .map(p -> (PessoaEntity) p)
      .map(PessoaDTO::new)
      .toList();
    return Response.ok(pessoas).build();
  }

}
