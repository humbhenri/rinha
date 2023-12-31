package org.acme;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "apelido",
  "nome",
  "nascimento",
  "stack"
})
public class PessoaDTO {

  public PessoaDTO() {}

  public PessoaDTO(PessoaEntity entity) {
    this.apelido = entity.apelido;
    this.nome = entity.nome;
    this.nascimento = entity.nascimento;
    this.stack = entity.getStack();
    this.id = entity.id.toString();
  }

  @JsonProperty("id")
  private String id;

  @JsonProperty("apelido")
  @NotNull
  @Size(min=1, max=32)
  private String apelido;

  @JsonProperty("nome")
  @NotNull
  @Size(min=1, max=100)
  private String nome;

  @JsonProperty("nascimento")
  @ValidDateString
  private String nascimento;

  @JsonProperty("stack")
  @ValidStackList
  private List<String> stack;

  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

  @JsonProperty("apelido")
  public String getApelido() {
    return apelido;
  }

  @JsonProperty("apelido")
  public void setApelido(String apelido) {
    this.apelido = apelido;
  }

  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }

  @JsonProperty("nome")
  public void setNome(String nome) {
    this.nome = nome;
  }

  @JsonProperty("nascimento")
  public String getNascimento() {
    return nascimento;
  }

  @JsonProperty("nascimento")
  public void setNascimento(String nascimento) {
    this.nascimento = nascimento;
  }

  @JsonProperty("stack")
  public List<String> getStack() {
    return stack;
  }

  @JsonProperty("stack")
  public void setStack(List<String> stack) {
    this.stack = stack;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
