package br.com.uanderson.springboot.client;

import br.com.uanderson.springboot.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        // Faz uma solicitação HTTP GET e retorna a resposta completa como uma instância de ResponseEntity
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity(
                "http://localhost:8080/animes/{id}", // URL com um placeholder para o ID do anime
                Anime.class, // Tipo da resposta esperada
                2 // Valor a ser substituído no placeholder {id}
        );
        // Registra a resposta completa, incluindo o corpo, código de status e cabeçalhos
        log.info("getForEntity -> {}", entity);

        // Faz uma solicitação HTTP GET e retorna o corpo da resposta mapeado para o tipo especificado (Anime)
        Anime anime = new RestTemplate().getForObject(
                "http://localhost:8080/animes/{id}", // URL com um placeholder para o ID do anime
                Anime.class, // Tipo da resposta esperada
                10 // Valor a ser substituído no placeholder {id}
        );
        // Registra apenas o corpo da resposta
        log.info("getForObject -> {}", anime);

        // Faz uma solicitação HTTP GET e retorna o corpo da resposta mapeado para um array de objetos Anime
        Anime[] animes = new RestTemplate().getForObject(
                "http://localhost:8080/animes/all", // URL para obter todos os animes
                Anime[].class // Tipo da resposta esperada
        );
        // Registra o array de animes obtido
        log.info("Array animes -> {}", Arrays.toString(animes));

        // Faz uma solicitação HTTP GET usando exchange para obter uma lista de animes
        //SUPER TYPE TOKENS
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange(
                "http://localhost:8080/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {}
                // Tipo da resposta esperada, usando ParameterizedTypeReference para lidar com tipos genéricos.
                //Pega o tipo do Objeto passado na lista e converte para uma Lista do mesmo tipo.
        );
        //@formatter:on

        log.info(exchange.getBody()); // Obtendo a lista de Animes do corpo da resposta e mostra no log
        List<Anime> animeList = exchange.getBody();// Obtendo a lista de Animes do corpo da resposta
        animeList.forEach(a -> System.out.print(a.getName() + ", "));// Imprimindo a lista de Animes

        /*
        url: A URL para a qual você deseja enviar a solicitação.
        method: O tipo de método HTTP que você deseja usar, como GET, POST, PUT, DELETE, etc.
        requestEntity: Um objeto HttpEntity que representa a solicitação HTTP, contendo o corpo
                       da solicitação, cabeçalhos personalizados, etc.
        responseType: A classe Java/Objeto que você deseja usar para mapear o corpo da resposta.

        Super Type Token - new ParameterizedTypeReference<List<Anime>>() {}

         */


    }//main
}//class
/*

#### `getForObject`
- O método `getForObject` faz uma solicitação HTTP GET e retorna diretamente o corpo da resposta mapeado para o tipo especificado.
- Ele converte automaticamente a resposta JSON (ou outro formato) para um objeto Java, usando a conversão de dados do Spring.
- Se a resposta for um JSON, por exemplo, o `RestTemplate` usará um `HttpMessageConverter` adequado para fazer a conversão para o tipo especificado.
- Se a resposta for vazia, o `getForObject` retornará `null`.

**Exemplo de Resultado da Consulta:**
```
Anime(id=10, name=Black Clover)
```
#### `getForEntity`

- O método `getForEntity` faz uma solicitação HTTP GET e retorna a resposta completa como uma instância de `ResponseEntity`.
- `ResponseEntity` contém o corpo da resposta, bem como informações adicionais, como o código de status, cabeçalhos da resposta, etc.
- Com `ResponseEntity`, você tem acesso a todas as informações da resposta HTTP, permitindo um maior controle e flexibilidade no processamento da resposta.
- Se a resposta for vazia, o `getForEntity` retornará uma instância de `ResponseEntity` com corpo vazio, mas ainda com as informações de cabeçalho e código de status.

```
<200 OK OK,
Anime(id=2, name=Naruto),
[Content-Type:"application/json",
Transfer-Encoding:"chunked",
Date:"Sat, 01 Jun 2024 22:41:15 GMT",
Keep-Alive:"timeout=60",
Connection:"keep-alive"]>
```

 */