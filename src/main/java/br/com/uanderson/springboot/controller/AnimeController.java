package br.com.uanderson.springboot.controller;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.requests.AnimePostRequestBody;
import br.com.uanderson.springboot.requests.AnimePutRequestBody;
import br.com.uanderson.springboot.service.AnimeService;
import br.com.uanderson.springboot.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")//normalmente declarado no plural
@Log4j2
@RequiredArgsConstructor //Cria um construtor com todos os atributos finais de um class
//@AllArgsConstructor//Cria um construtor com todos os atributos não finais de uma class
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping(path = "/all")
    @Operation(summary = "List all animes without pagination", description = "Returns a list of all animes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<List<Anime>> listAllNoPageable() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAllNoPageable());
    }

    @GetMapping()
    @Operation(
            summary = "List all animes paginated",
            description = "The default size is 5, use the parameter size to change the default value",
            tags = {"anime"}
    )
    public ResponseEntity<Page<Anime>> listAllPageable(@ParameterObject Pageable pageable) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAllPageable(pageable), HttpStatus.OK);
        /*
        Boas práticas: retornar conteúdo extras, tipo status da request,
        ao invés de somente o conteúdo solicitado, na resquet.

        O spring traduz as @RequestParam: size=5 & page2 para object Pageable.
        - http://localhost:8080/animes?size=5&page=0
        - Dica validar as RequestParam (controllers e services )caso optar por receber-las como parametro:
             - @Positive @Max(30) int size, @PositiveOrZero int page
             - Aceitar somente números positivos ou positivo + zero
             - O valor máximo permitido é 30
         - Dica 02: Caso opter pelas @RequestParam: criar um objetoDTO para retornar
            os objetos paginados.

        Pageable é uma interface fornecida pelo Spring Framework que permite a paginação
        de resultados em consultas de banco de dados. Ela encapsula informações sobre a
        página solicitada, como o número da página, o tamanho da página e as opções de ordenação.

         */
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find anime by ID", description = "Returns a single anime by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Anime not found")
    })
    public ResponseEntity<Anime> findById(@Parameter(description = "ID of the anime to be searched") @PathVariable Long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
        /*
          - @GetMapping(path = "/{id}")
           Quando temos mais de 1 method http (GET) é necessário diferenciá-los por um
          'path' caminho que apronta pra um endpoint /animes/{id}. Nesse exemplo
          estamos usando @PathVariable onde o id faz parte da url

         */
    }

    @GetMapping(path = "by-id/{id}")
    @Operation(summary = "Find anime by ID with user details", description = "Returns a single anime by its ID, logs user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Anime not found")
    })
    //@PreAuthorize("hasRole('ADMIN')")//verifica se o usuário atual logado possui a permissão de "ADMIN"
    //(E mais recomendado utilizar um padrão de url's é aplicar a proteção com um antMatcher)
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(
            @Parameter(description = "ID of the anime to be searched") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails){
        log.info("Name user logado: {}", userDetails.getUsername());
        return new ResponseEntity<>(animeService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
        //@AuthenticationPrincipal pega o user autenticado
    }

    @PostMapping
    @Operation(summary = "Save new anime", description = "Creates a new anime")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Anime created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid fields")
    })
    //@PreAuthorize("hasRole('ADMIN')")//verifica se o usuário atual logado possui a permissão de "ADMIN"
    //(E mais recomendado utilizar um padrão de url's é aplicar a proteção com um antMatcher)
    //@ResponseStatus(HttpStatus.CREATED) //outra forma de retornar o status
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
        /*
           @RequestBody Anime anime -> Aqui o Jackson entra em cena realizando o mapeamento
           do objeto recebido no corpo(body) para um "Anime", para isso o nome dos atributos/propriedades
           devem ser as mesma, caso contrário é necessário informar ao Jackson o nome do atributo que deve ser
           mapeado, através da anotação:  @JsonProperty("nome do atributo JSON vindo do corpo") em
            cima do atributo da classe "Anime"
        */
    }

    @DeleteMapping(path = "/admin/{id}")
    @Operation(summary = "Delete anime by ID", description = "Deletes an anime by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "204", description = "Succesful Operation"),
            @ApiResponse(responseCode =  "400", description = "When Anime does not exists in the Database")
    })
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID of the anime to be deleted") @PathVariable Long id){
        animeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        /*
           Normalmete, deletamos apenas pelo id passado, assim como o findById
           e não retornamos nada, 'NO_CONTENT'
        */
    }

    @PutMapping
    @Operation(summary = "Replace anime", description = "Replaces an existing anime")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid fields")
    })
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        /*
        Quando realizamos um PUT(update/replace) estamos substituindo o ESTADO
        inteiro do objeto, de forma idempotente.

        O ESTADO são os valores atribuídos aos atributos de um objeto.
        Diferentemente do COMPORTAMENTO que são os métodos da classe,que como o próprio
        nome diz, é o comportamento do objeto.

         */
    }

    @GetMapping(path = "/find")
    @Operation(summary = "Find animes by name", description = "Returns a list of animes by their name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "404", description = "Animes not found")
    })
    public ResponseEntity<List<Anime>> findByName(@Parameter(description = "Name of the animes to be searched") @RequestParam String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    /*
    Quando temos mais de um método HTTP utilizando/respondendo no mesmo endpoint, como a seguir:
    - @GetMapping(path = "/{name}")
    - @GetMapping(path = "/{id}")
    Gera ambiguidade, pois o Spring não consegue diferenciar, já que estamos dizendo que
    no mesmo endpoint são aceitos dois tipos de dados (@PathVariable).

    Neste caso, o recomendável é criar um novo endpoint que aceite o segundo tipo de dado.

    Uma das opções é utilizando o @RequestParam (parâmetros passados na URL), que
    por padrão são obrigatórios. Exemplo:
    - http://localhost:8080/animes/find?name=Naruto Shippuden
    - @GetMapping(path = "/find/{name}") - Outra opção é usar @PathVariable, mas pode ser mais difícil de lembrar,
    especialmente se precisar usar muitos parâmetros para construir a URL/endpoint.


    DIFERENÇA ENTRE @RequestParam e @PathVariable
    @RequestParam - Parâmetros passados na URL -> animes/find?name=naruto&idade=20&cargo=hokage
        - A anotação @RequestParam é usada para mapear parâmetros de consulta (query parameters) em uma URL.
        - É possível definir valores padrão para os parâmetros usando o atributo
          defaultValue da anotação @RequestParam (@RequestParam(defaultValue = "") String name).
        - Caso haja mais de um parâmetro, eles são diferenciados pelo '&'.
        Exemplo: http://localhost:8080/animes/find?name=naruto&id=2

    @PathVariable - Parâmetros/valores que fazem parte da URL -> animes/find/id (animes/find/{id})
        - A anotação @PathVariable é usada para mapear partes variáveis de uma URL.
    */
    }


}//class
/*
@RestController -> Retorna um corpo contento somente String/Json
@Controller -> Retorna uma pagina html inteira

------------------------------------------------------
MÉTODOS IDEMPOTENTES: Significa dizer que, não importa
quantas vezes forem executados(request), o seu resultado final(response)
deve ser o mesmo.

Um método idempotente é um método em que uma requisição idêntica
pode ser feita uma ou mais vezes, em sequência, com o mesmo efeito,
enquanto deixa o servidor no mesmo estado.

- O "Hypertext Transfer Protocol (HTTP) Method Registry" foi
preenchido com os registros abaixo:

https://datatracker.ietf.org/doc/html/rfc7231

   +---------+------+------------+---------------+
   | Method  | Safe | Idempotent | Reference     |
   +---------+------+------------+---------------+
   | CONNECT | no   | no         | Section 4.3.6 |
   | DELETE  | no   | yes        | Section 4.3.5 |
   | GET     | yes  | yes        | Section 4.3.1 |
   | HEAD    | yes  | yes        | Section 4.3.2 |
   | OPTIONS | yes  | yes        | Section 4.3.7 |
   | POST    | no   | no         | Section 4.3.3 |
   | PUT     | no   | yes        | Section 4.3.4 |
   | TRACE   | yes  | yes        | Section 4.3.8 |
   +---------+------+------------+---------------+

OBS: o method delete, pode ser não-idepontente
ex: caso ele esteja alterando sempre a ultima posição de
uma lista ele estará alterando o estado do servidor.

+---------+-------------------------------------------------+-------+
| Método  | Descrição                                       | Sec.  |
+---------+-------------------------------------------------+-------+
| GET     | Transfere uma representação atual do recurso    | 4.3.1 |
|         | alvo.                                           |       |
| HEAD    | Igual ao GET, mas apenas transfere a linha de   | 4.3.2 |
|         | status e a seção de cabeçalho.                  |       |
| POST    | Realiza processamento específico do recurso no  | 4.3.3 |
|         | payload da requisição.                          |       |
| PUT     | Substitui todas as representações atuais do     | 4.3.4 |
|         | recurso alvo com o payload da requisição.       |       |
| DELETE  | Remove todas as representações atuais do        | 4.3.5 |
|         | recurso alvo.                                   |       |
| CONNECT | Estabelece um túnel para o servidor identificado| 4.3.6 |
|         | pelo recurso alvo.                              |       |
| OPTIONS | Descreve as opções de comunicação para o        | 4.3.7 |
|         | recurso alvo.                                   |       |
| TRACE   | Realiza um teste de loop-back de mensagem ao    | 4.3.8 |
|         | longo do caminho até o recurso alvo.            |       |
+---------+-------------------------------------------------+-------+

```
CÓDIGOS DE RETORNO:

   +-------+-------------------------------+---------------------------------------------+----------------+
   | Value | Description                   | Descrição                                  | Reference      |
   +-------+-------------------------------+---------------------------------------------+----------------+
   | 100   | Continue                      | Continue                                   | Section 6.2.1  |
   | 101   | Switching Protocols           | Mudando Protocolos                         | Section 6.2.2  |
   | 200   | OK                            | OK                                         | Section 6.3.1  |
   | 201   | Created                       | Criado                                     | Section 6.3.2  |
   | 202   | Accepted                      | Aceito                                     | Section 6.3.3  |
   | 203   | Non-Authoritative Information | Informação Não-Autoritativa                | Section 6.3.4  |
   | 204   | No Content                    | Sem Conteúdo                               | Section 6.3.5  |
   | 205   | Reset Content                 | Redefinir Conteúdo                         | Section 6.3.6  |
   | 300   | Multiple Choices              | Múltiplas Escolhas                         | Section 6.4.1  |
   | 301   | Moved Permanently             | Movido Permanentemente                     | Section 6.4.2  |
   | 302   | Found                         | Encontrado                                 | Section 6.4.3  |
   | 303   | See Other                     | Ver Outro                                  | Section 6.4.4  |
   | 305   | Use Proxy                     | Usar Proxy                                 | Section 6.4.5  |
   | 306   | (Unused)                      | (Não Utilizado)                            | Section 6.4.6  |
   | 307   | Temporary Redirect            | Redirecionamento Temporário                | Section 6.4.7  |
   | 400   | Bad Request                   | Requisição Inválida                        | Section 6.5.1  |
   | 402   | Payment Required              | Pagamento Necessário                       | Section 6.5.2  |
   | 403   | Forbidden                     | Proibido                                   | Section 6.5.3  |
   | 404   | Not Found                     | Não Encontrado                             | Section 6.5.4  |
   | 405   | Method Not Allowed            | Método Não Permitido                       | Section 6.5.5  |
   | 406   | Not Acceptable                | Não Aceitável                              | Section 6.5.6  |
   | 408   | Request Timeout               | Tempo de Requisição Esgotado               | Section 6.5.7  |
   | 409   | Conflict                      | Conflito                                   | Section 6.5.8  |
   | 410   | Gone                          | Não Disponível                             | Section 6.5.9  |
   | 411   | Length Required               | Comprimento Necessário                     | Section 6.5.10 |
   | 413   | Payload Too Large             | Payload Muito Grande                       | Section 6.5.11 |
   | 414   | URI Too Long                  | URI Muito Longa                            | Section 6.5.12 |
   | 415   | Unsupported Media Type        | Tipo de Mídia Não Suportado                | Section 6.5.13 |
   | 417   | Expectation Failed            | Falha na Expectativa                       | Section 6.5.14 |
   | 426   | Upgrade Required              | Atualização Necessária                     | Section 6.5.15 |
   | 500   | Internal Server Error         | Erro Interno do Servidor                   | Section 6.6.1  |
   | 501   | Not Implemented               | Não Implementado                           | Section 6.6.2  |
   | 502   | Bad Gateway                   | Gateway Inválido                           | Section 6.6.3  |
   | 503   | Service Unavailable           | Serviço Indisponível                       | Section 6.6.4  |
   | 504   | Gateway Timeout               | Tempo de Gateway Esgotado                  | Section 6.6.5  |
   | 505   | HTTP Version Not Supported    | Versão HTTP Não Suportada                  | Section 6.6.6  |
   +-------+-------------------------------+---------------------------------------------+----------------+
```
 */