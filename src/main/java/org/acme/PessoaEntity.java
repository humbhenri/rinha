package org.acme;
import jakarta.persistence.*;
import io.quarkus.hibernate.reactive.panache.*;
import java.util.*;
import java.util.stream.*;

@Entity
@Cacheable
@Table(uniqueConstraints=@UniqueConstraint(columnNames="apelido"))
public class PessoaEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public UUID id;

  public String nome;
  public String apelido;
  public String nascimento;
  public String stack;

  public List<String> getStack() {
    if (stack == null) {
      return null;
    }
    return Arrays.asList(stack.split(","));
  }
  
}
