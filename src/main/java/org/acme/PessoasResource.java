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
import jakarta.inject.Inject;

@Path("/pessoas")
@Produces("application/json")
@Consumes("application/json")
public class PessoasResource {

  Logger log = Logger.getLogger(PessoasResource.class);

  @Inject
  EntityManager em;
  
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
      var text = new StringBuilder()
        .append(pessoa.getNome().toLowerCase())
        .append(pessoa.getApelido().toLowerCase());
      if (pessoa.getStack() != null) {
        pessoa.getStack().forEach(s -> {
          text.append(s.toLowerCase());
        });
      }
      pessoaEntity.text = text.toString();
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
    if (termoBusca == null) {
      return Response.status(400).build();
    }
    var q = em.createQuery("select p from PessoaEntity p "
        + "where p.text ilike :term ", PessoaEntity.class);
    q.setParameter("term", "%"+termoBusca.toLowerCase()+"%");
    return Response.ok(q.getResultList()
        .stream()
        .map(PessoaDTO::new)
        .collect(Collectors.toList())).build();
  }

}
