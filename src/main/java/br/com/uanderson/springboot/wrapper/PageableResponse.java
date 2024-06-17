package br.com.uanderson.springboot.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Getter
@Setter
public class PageableResponse<T> extends PageImpl<T> {
    private boolean first; // Indica se esta é a primeira página
    private boolean last; // Indica se esta é a última página
    private int totalPages; // Número total de páginas
    private int numberOfElements; // Número de elementos na página atual

    /**
     * Construtor personalizado usado para deserializar a resposta JSON em um objeto PageableResponse.
     *
     * @param content Lista de elementos contidos na página.
     * @param number Número da página atual (base zero).
     * @param size Tamanho da página (número de elementos por página).
     * @param totalElements Número total de elementos disponíveis.
     * @param last Indica se esta é a última página.
     * @param first Indica se esta é a primeira página.
     * @param totalPages Número total de páginas.
     * @param numberOfElements Número de elementos na página atual.
     * @param pageable Informações sobre a paginação (não usado diretamente).
     * @param sort Informações sobre a ordenação (não usado diretamente).
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageableResponse(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") int totalElements,
            @JsonProperty("last") boolean last,
            @JsonProperty("first") boolean first,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("numberOfElements") int numberOfElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("sort") JsonNode sort) {
        // Chama o construtor da superclasse PageImpl para inicializar a página com os dados fornecidos
        super(content, PageRequest.of(number, size), totalElements);

        // Inicializa os atributos adicionais da classe PageableResponse
        this.last = last;
        this.first = first;
        this.totalPages = totalPages;
        this.numberOfElements = numberOfElements;
    }

    /*
        Esta classe estende a funcionalidade de paginação do Spring Data (PageImpl),
        adicionando campos adicionais que são úteis para a resposta da API. Ela é
        usada principalmente em testes para mapear respostas paginadas de endpoints
        REST em objetos Java, facilitando a verificação e asserção dos dados recebidos.


     A anotação @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) é usada para indicar ao
     mecanismo de desserialização do Jackson como criar uma instância da classe a partir
     de dados JSON. Aqui está uma explicação detalhada sobre a sua utilização e o código
      fornecido:

Explicação sobre @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    Contexto
    Quando o Jackson desserializa um objeto JSON, ele precisa saber como mapear os dados
    JSON para os campos ou propriedades do objeto Java. O Jackson oferece diferentes modos
     para isso, e JsonCreator.Mode.PROPERTIES é um desses modos.

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    @JsonCreator: Indica que este construtor deve ser usado pelo Jackson para criar uma
    instância da classe durante a desserialização.
    mode = JsonCreator.Mode.PROPERTIES: Especifica que o Jackson deve mapear as propriedades
     do JSON para os parâmetros do construtor com base no nome. Isso significa que ele usará
     os nomes das propriedades JSON para encontrar os parâmetros correspondentes no construtor.

Considerações
PROPERTIES: É o modo mais detalhado e permite um mapeamento direto de cada propriedade JSON
para um parâmetro específico do construtor ou método de fábrica. É útil para classes que
têm vários campos e um mapeamento explícito é necessário.
DELEGATING: Útil quando você deseja que todo o JSON seja tratado como um único objeto
valor, geralmente um Map ou um objeto de valor específico.
DISABLED: Útil para desabilitar explicitamente o uso de um determinado construtor ou método
de fábrica para a desserialização JSON, talvez em favor de outro método de criação ou para
fins de segurança.
DEFAULT: Comportamento padrão que assume PROPERTIES, mas sem a necessidade de ser explícito.
  */

    }//clas
