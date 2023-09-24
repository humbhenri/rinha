package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.validation.Valid;
import org.jboss.logging.Logger;
import jakarta.transaction.*;
import jakarta.persistence.*;

import java.net.URI;
import java.util.*;
import java.util.stream.*;
import jakarta.inject.Inject;

@Path("/pessoas")
@Produces("application/json")
@Consumes("application/json")
public class PessoasResource {

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
        pessoaEntity.stack = String.join(",", pessoa.getStack());
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
      return Response.created(URI.create("/pessoas/" + pessoaEntity.id)).build();
    } catch(PersistenceException e) {
      return Response.status(422).entity(e.getMessage()).build();
    }
  }

  @GET
  @Path("/{id}")
  public Response consulta(@PathParam("id") String idPessoa) {
    var pessoa = PessoaEntity.findByIdOptional(UUID.fromString(idPessoa))
      .orElseThrow(NotFoundException::new);
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
