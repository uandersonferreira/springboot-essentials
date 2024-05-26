package br.com.uanderson.springboot.controller;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.service.AnimeService;
import br.com.uanderson.springboot.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public ResponseEntity<List<Anime>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);
        //Boas práticas: retornar conteúdo extras, tipo status da request,
        // ao invés de somente o conteúdo solicitado, na resquet.
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {
        return ResponseEntity.ok(animeService.findById(id));
        /*
          - @GetMapping(path = "/{id}")
           Quando temos mais de 1 method http (GET) é necessário diferenciá-los por um
          'path' caminho que apronta pra um endpoint /animes/{id}. Nesse exemplo
          estamos usando @PathVariable onde o id faz parte da url

         */
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED) //outra forma de retornar o status
    public ResponseEntity<Anime> save(@RequestBody Anime anime){
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
        /*
           @RequestBody Anime anime -> Aqui o Jackson entra em cena realizando o mapeamento
           do objeto recebido no corpo(body) para um "Anime", para isso o nome dos atributos/propriedades
           devem ser as mesma, caso contrário é necessário informar ao Jackson o nome do atributo que deve ser
           mapeado, através da anotação:  @JsonProperty("nome do atributo JSON vindo do corpo") em
            cima do atributo da classe "Anime"
        */
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        /*
           Normalmete, deletamos apenas pelo id passado, assim como o findById
           e não retornamos nada, 'NO_CONTENT'
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