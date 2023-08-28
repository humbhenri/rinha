package org.acme;
import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.*;
import java.util.*;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames="apelido"))
public class PessoaEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public UUID id;

  public String nome;
  public String apelido;
  public String nascimento;
  public List<String> stack;
  
}
