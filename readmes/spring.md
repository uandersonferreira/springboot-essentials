
```java
/*
    GenerationType.AUTO:
        O provedor JPA (Hibernate, EclipseLink, etc.) escolhe a
        estratégia apropriada automaticamente com base no banco de dados
        utilizado. É uma escolha genérica e a estratégia real utilizada
        pode variar entre IDENTITY, SEQUENCE ou TABLE, dependendo do suporte
        do banco de dados subjacente.

    GenerationType.IDENTITY:
        Usa colunas de autoincremento do banco de dados para gerar valores
        de identificação. Comumente usado com bancos de dados como MySQL,
        PostgreSQL e SQL Server que suportam autoincremento. Cada nova entidade
        inserida recebe um valor de ID gerado automaticamente pelo banco de dados.

    GenerationType.SEQUENCE:
        Usa sequências de banco de dados para gerar valores de identificação.
        Geralmente usado com bancos de dados que suportam sequências, como Oracle
        e PostgreSQL. Requer a definição de uma sequência no banco de dados e
        permite a personalização do incremento de valores.

    GenerationType.TABLE:
        Usa uma tabela no banco de dados para gerar valores de identificação.
        Geralmente usado quando as outras estratégias não são adequadas. Essa estratégia
        cria e mantém uma tabela separada para armazenar os valores de ID gerados.
        É mais lenta e menos eficiente que IDENTITY ou SEQUENCE, mas pode ser usada
        em situações onde essas outras estratégias não são suportadas ou desejadas.

*/
```

Aqui está um exemplo prático de como usar essas estratégias em uma entidade JPA:

```java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
public class ExampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idAuto;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIdentity;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "example_sequence", allocationSize = 1)
    private Long idSequence;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
    @TableGenerator(name = "table_gen", table = "id_gen_table", pkColumnName = "gen_name",
                    valueColumnName = "gen_value", pkColumnValue = "example_id", allocationSize = 1)
    private Long idTable;

    // Getters and setters
}
```

### Considerações:

- **GenerationType.AUTO**: Delega ao provedor JPA a escolha da estratégia mais apropriada para o banco de dados em uso. Ideal para situações onde a portabilidade entre diferentes bancos de dados é necessária.
- **GenerationType.IDENTITY**: Útil em bancos de dados que suportam a funcionalidade de autoincremento, evitando a necessidade de gerenciar manualmente a geração de IDs.
- **GenerationType.SEQUENCE**: Oferece maior controle sobre a geração de IDs e é eficiente para bancos de dados que suportam sequências.
- **GenerationType.TABLE**: É mais genérico e flexível, mas geralmente é menos eficiente devido ao overhead de acessar uma tabela separada para obter IDs.

## Spring validation

| Anotação            | Descrição                                                                                     |
|---------------------|-----------------------------------------------------------------------------------------------|
| @NotNull            | Determina que o valor não pode ser nulo. ( != null )                                          |
| @Min                | Determina que o número deve ser igual ou maior que o valor especificado. ( >= )               |
| @Max                | Determina que o número deve ser igual ou menor que o valor especificado. ( <= )               |
| @Size(min, max)     | Verifica se o tamanho do elemento anotado está entre os valores mínimos e máximos fornecidos (inclusive). |
| @Pattern            | Determina que a sequência segue a expressão regular especificada. ( Expressão regular/Regex ) |
| @Email              | Verifica se a sequência de caracteres/string especificada é um endereço de email válido. ( Email válido ) |
| @NotBlank           | Verifica se a sequência de caracteres/string anotada não é nula e seu comprimento aparado é maior que 0. ( != null & length > 0 RECOMENDADO) |
| @NotEmpty           | Verifica se o elemento anotado não é nulo e não está vazio.                                   |
| @AssertFalse        | Verifica se o elemento anotado é falso.                                                       |
| @AssertTrue         | Verifica se o elemento anotado é verdadeiro.                                                  |
| @NegativeOrZero     | Verifica se o elemento dado é menor ou igual a 0. ( <= 0 )                                    |
| @Null               | Verifica se o valor anotado é nulo. ( == null )                                               |
| @Negative           | Verifica se o elemento é estritamente menor que 0. ( < 0 )                                    |
| @Positive           | Verifica se o elemento é estritamente maior que 0. ( > 0 )                                    |
| @PositiveOrZero     | Verifica se o elemento dado é maior ou igual a 0. ( >= 0 )                                    |
| @CPF                | Verifica se o valor anotado é um CPF válido.                                                  |
| @CNPJ               | Verifica se o valor anotado é um CNPJ válido.                                                 |
| @Future             | Verifica se a data é no futuro.                                                              |
| @Past               | Verifica se a data é no passado.                                                             |
| @FutureOrPresent    | Verifica se a data é no futuro ou no presente.                                               |
| @PastOrPresent      | Verifica se a data é no passado ou no presente.                                              |
| @DecimalMin(value)  | Verifica se o valor é um número decimal mínimo.                                              |
| @DecimalMax(value)  | Verifica se o valor é um número decimal máximo.                                              |
| @Digits(integer, fraction) | Verifica se o valor numérico tem um número de dígitos inteiros e fracionários dentro dos limites. |
| @Range(min, max)       | Verifica se o valor está dentro do intervalo especificado (inclusive).                         |
| @Length(min, max)      | Verifica se o comprimento da string está dentro do intervalo especificado (inclusive).         |
| @URL                   | Verifica se a string é uma URL válida.                                                         |
| @CreditCardNumber      | Verifica se a string é um número de cartão de crédito válido.                                  |
| @LuhnCheck             | Verifica se a string atende ao algoritmo de Luhn (útil para validação de números de cartão de crédito). |
| @EAN                   | Verifica se a string é um código EAN válido (usado em códigos de barras).                      |
| @ISBN                  | Verifica se a string é um ISBN válido (usado em livros).                                       |
| @UUID                  | Verifica se a string é um UUID válido.                                                         |
| @NotBlank              | Verifica se a string não é nula e não está em branco após a remoção de espaços em branco.      |
| @NotEmpty              | Verifica se a string não é nula e não está vazia.                                              |
| @SafeHtml              | Verifica se a string contém apenas HTML seguro (usado para prevenir ataques XSS).              |
| @Currency              | Verifica se a string representa um código de moeda válido.                                     |
| @Pattern.Flag          | Define flags de expressão regular, como CASE_INSENSITIVE, para a anotação @Pattern.            |
| @TimeZone              | Verifica se a string representa um fuso horário válido.                                        |
| @PhoneNumber           | Verifica se a string é um número de telefone válido.                                           |

### Explicações adicionais

- **@Range(min, max)**: Útil para validar valores numéricos que devem estar dentro de um intervalo específico. Exemplo: @Range(min=1, max=10)
- **@Length(min, max)**: Útil para validar o comprimento de strings, garantindo que elas estejam dentro de um intervalo específico. Exemplo: @Length(min=5, max=20)
- **@URL**: Valida se a string é uma URL válida. Exemplo: @URL
- **@CreditCardNumber**: Valida números de cartões de crédito. Exemplo: @CreditCardNumber
- **@LuhnCheck**: Aplica o algoritmo de Luhn para validação de números de cartões de crédito. Exemplo: @LuhnCheck
- **@EAN**: Valida códigos EAN (European Article Number). Exemplo: @EAN
- **@ISBN**: Valida números ISBN. Exemplo: @ISBN
- **@UUID**: Valida UUIDs (Universally Unique Identifiers). Exemplo: @UUID
- **@NotBlank**: Garante que a string não é nula, não é vazia e não consiste apenas em espaços em branco. Exemplo: @NotBlank
- **@NotEmpty**: Garante que a string não é nula e não está vazia. Exemplo: @NotEmpty
- **@SafeHtml**: Verifica se a string contém apenas HTML seguro. Exemplo: @SafeHtml
- **@Currency**: Verifica se a string representa um código de moeda válido (ISO 4217). Exemplo: @Currency
- **@Pattern.Flag**: Define flags de expressão regular para uso com @Pattern. Exemplo: @Pattern(flag = Pattern.Flag.CASE_INSENSITIVE)
- **@TimeZone**: Verifica se a string representa um fuso horário válido. Exemplo: @TimeZone
- **@PhoneNumber**: Valida números de telefone. Exemplo: @PhoneNumber

### Exemplo de Uso

```java
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

public class User {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number")
    private String phoneNumber;

    @URL
    private String website;

    @Range(min = 18, max = 65)
    private int age;

    @Length(min = 8, max = 20)
    private String password;

    @CreditCardNumber
    private String creditCard;

    // getters and setters
}
```
