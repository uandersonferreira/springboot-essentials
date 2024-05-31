package br.com.uanderson.springboot.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnimePostRequestBody { //Segue o mesmo padrão/conceito dos DTO's
    @NotEmpty(message = "The anime name cannot be empty") //Pega os null também
    private String name;
    /*
    Obs: poderiamos usar o novo recurso do java 'para construir essa class dto
    chamado records, olhar documentation java.
        public record AnimePostRequestBody(
            @NotEmpty(message = "The anime name cannot be empty")
            String name
        ) { }
     */
}

/*
spring Validation: Possui anotações que permitem realizar
a validação de campos.
Ex:
-      @NotEmpty(message = "The anime name cannot be empty") //Pega os null também
 - null (null) diferente de empty (" ")

OBSERVAÇÃO:
Para que de fato essas validações sejam executadas, precisamos utilizar a
anotação @Valid que irá executar a validação de todas as anotações
da nossa classe.
EX:
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }
EX 2:
    Caso esteja utilizando um objeto de outra class como atributo e
    queira que as validações dessa outra classe seja aplicadas, basta anotar o
    atributo com @Valid:
    public class Pesso{
        private String name;
        @Valid
        private Endereco endereco;
    }

link: https://www.geeksforgeeks.org/spring-mvc-validation/

Algumas validações disponiveis:
- Olhar o readme 'spring.md' para consultar outras

@NotNull - 	It determines that the value can't be null. ( != null )
@Min 	 - It determines that the number must be equal or greater than the specified value. ( >= )
@Max 	 - It determines that the number must be equal or less than the specified value. ( <= )
@Size(min,max)   – Checks if the annotated element’s size is between min value and max value provided (inclusive).
@Pattern - It determines that the sequence follows the specified regular expression. ( Expressão regular/Regex )
@Email   – Checks whether the specified character sequence/string is a valid email address. ( Email válido )
@NotBlank – Checks that the annotated character sequence/string is not null and the trimmed length is greater than 0. ( != null & length > 0 RECOMENDANDO)
@NotEmpty – Checks that the annotated element is not null and not empty.
@AssertFalse – Checks that the annotated element is false.
@AssertTrue – Checks that the annotated element is true.
@NegativeOrZero – Checks if the given element is smaller than or equal to 0. ( <= 0)
@Null – Checks that the annotated value is null. ( == null )
@Negative – Checks if the element is strictly smaller than 0. ( < 0 )
@Positive – Checks if the element is strictly greater than 0. ( > 0 )
@PositiveOrZero – Checks if the given element is greater than or equal to 0. ( >= 0 )
@CPF -  Verifica se é um cpf valido
@CNPJ -  se é um cnpj valido


 */