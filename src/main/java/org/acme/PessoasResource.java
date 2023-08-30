package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.validation.Valid;
import org.jboss.logging.Logger;
import jakarta.transaction.*;
import jakarta.persistence.*;
import java.util.*;
import java.util.stream.*;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.*;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.CompositeException;
import io.smallrye.mutiny.Uni;

@Path("/pessoas")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class PessoasResource {

  Logger log = Logger.getLogger(PessoasResource.class);
  
  @POST
  public Uni<Response> cria(@Valid PessoaDTO pessoa, @Context UriInfo uriInfo) {
    return Panache
      .withTransaction(() -> {
        var pessoaEntity = new PessoaEntity();
        pessoaEntity.nome = pessoa.getNome();
        pessoaEntity.apelido = pessoa.getApelido();
        pessoaEntity.nascimento = pessoa.getNascimento();
        if (pessoa.getStack() != null) {
          pessoaEntity.stack = pessoa.getStack().stream().collect(Collectors.joining(","));
        }
        return pessoaEntity.persistAndFlush(); // force exceptions
      }).onItem().transform(entity -> {
        var pessoaEntity = (PessoaEntity) entity;
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(pessoaEntity.id.toString());
        return Response.created(uriBuilder.build()).build();
      }).onFailure().recoverWithItem(Response.ok().status(422).build());
  }

  @GET
  @Path("/{id}")
  public Uni<Response> consulta(@PathParam("id") String idPessoa) {
    return PessoaEntity.findById(UUID.fromString(idPessoa))
      .onItem().ifNotNull().transform(entity -> 
          Response.ok(new PessoaDTO((PessoaEntity)entity)).build())
      .replaceIfNullWith(() -> Response.status(404).build());
  }

  @GET
  public Uni<Response> pesquisa(@QueryParam("t") String termoBusca) {
    if (termoBusca == null) {
      throw new WebApplicationException("Termo de busca é obrigatório", 400);
    }
    var predicates = new ArrayList<String>();
    var params = new HashMap<String, Object>();
    predicates.add("apelido ilike :termoBusca");
    predicates.add("nome ilike :termoBusca");
    predicates.add("stack ilike :termoBusca");
    params.put("termoBusca", "%"+termoBusca+"%");
    var whereClauses = predicates.stream().collect(Collectors.joining(" or "));
    log.info(whereClauses);
    return PessoaEntity.find(whereClauses, params).page(Page.ofSize(50)).list()
      .onItem()
      .transform(pessoas -> Response.ok(pessoas.stream().map(p -> (PessoaEntity) p)
          .map(PessoaDTO::new)
          .collect(Collectors.toList())).build());
  }

}
