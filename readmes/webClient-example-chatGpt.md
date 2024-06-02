### Para utilizar o `WebClient` no Spring Boot, que é a alternativa mais recente e moderna ao `RestTemplate`, você pode refatorar seu código da seguinte forma. O `WebClient` é mais flexível e poderoso, suportando chamadas assíncronas e várias outras funcionalidades.

Aqui está um exemplo de como você pode usar `WebClient` para as mesmas operações 
que você fez com `RestTemplate`:

```java
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    private static final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public static void main(String[] args) {
        // GET request using retrieve() method to get an Anime object
        Anime anime = webClient.get()
                .uri("/animes/{id}", 10)
                .retrieve()
                .bodyToMono(Anime.class)
                .block();
        log.info("retrieve() -> {}", anime);

        // GET request using exchange() method to get a ResponseEntity<Anime>
        ResponseEntity<Anime> entity = webClient.get()
                .uri("/animes/{id}", 2)
                .exchangeToMono(response -> response.toEntity(Anime.class))
                .block();
        log.info("exchangeToMono() -> {}", entity);

        // GET request to get an array of Anime objects
        Anime[] animes = webClient.get()
                .uri("/animes/all")
                .retrieve()
                .bodyToMono(Anime[].class)
                .block();
        log.info("Array animes -> {}", Arrays.toString(animes));

        // GET request to get a list of Anime objects using a ParameterizedTypeReference
        List<Anime> animeList = webClient.get()
                .uri("/animes/all")
                .retrieve()
                .bodyToFlux(Anime.class)
                .collectList()
                .block();
        log.info("List animes -> {}", animeList);

        // POST request using retrieve() method to create a new Anime
        Anime kingdom = Anime.builder().name("Kingdom").build();
        Anime kingdomSaved = webClient.post()
                .uri("/animes")
                .body(Mono.just(kingdom), Anime.class)
                .retrieve()
                .bodyToMono(Anime.class)
                .block();
        log.info("postForObject() -> {}", kingdomSaved);

        // POST request using exchange() method to create a new Anime
        Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved = webClient.post()
                .uri("/animes")
                .body(Mono.just(samuraiChamploo), Anime.class)
                .exchangeToMono(response -> response.toEntity(Anime.class))
                .block();
        log.info("exchange-POST() -> {}", samuraiChamplooSaved);

        // PUT request using exchange() method to update an Anime
        Anime animeToBeUpdated = samuraiChamplooSaved.getBody();
        animeToBeUpdated.setName("Samurai Champloo 2");
        ResponseEntity<Void> samuraiChamplooUpdated = webClient.put()
                .uri("/animes")
                .body(Mono.just(animeToBeUpdated), Anime.class)
                .exchangeToMono(response -> response.toEntity(Void.class))
                .block();
        log.info("exchange-PUT -> {}", samuraiChamplooUpdated);

        // DELETE request using exchange() method to delete an Anime by ID
        ResponseEntity<Void> samuraiChamplooDeleted = webClient.delete()
                .uri("/animes/{id}", animeToBeUpdated.getId())
                .exchangeToMono(response -> response.toEntity(Void.class))
                .block();
        log.info("exchange-DELETE -> {}", samuraiChamplooDeleted);
    }
}
```

### Explicação das mudanças:

1. **WebClient Configuration**:
    - `WebClient` é configurado uma vez como `webClient` com a URL base e cabeçalhos padrão.

2. **GET Requests**:
    - **Retrieve and Exchange**:
        - `.retrieve()` é usado para obter diretamente o corpo da resposta.
        - `.exchangeToMono()` é usado para obter a resposta completa (`ResponseEntity`).

3. **POST Requests**:
    - **Retrieve**: Para obter o corpo da resposta.
    - **Exchange**: Para obter a resposta completa.

4. **PUT and DELETE Requests**:
    - Usamos `.exchangeToMono()` para ambos, permitindo capturar o `ResponseEntity` completo, incluindo o status da resposta.

### Observação:

- `block()` é utilizado para fins de simplicidade e exemplo. Em aplicações reativas, você deve evitar bloquear e em vez disso trabalhar com `Mono` e `Flux` de maneira reativa.
- Configurações adicionais de headers podem ser feitas dentro das chamadas de requisição quando necessário.

Esses exemplos mostram como utilizar `WebClient` para realizar operações HTTP de 
forma semelhante ao `RestTemplate`, mas com a vantagem de ser não bloqueante e mais
flexível para uso em aplicações reativas.

#### ===================== EXEMPLO 2 =========================

## Aqui está um exemplo de como você pode usar `WebClient` de maneira reativa 
com `Mono` e `Flux` para os mesmos endpoints fornecidos:

```java
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    private static final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public static void main(String[] args) {
        // Exemplo de uso de Mono e Flux de maneira reativa
        getAnimeById(10)
                .doOnNext(anime -> log.info("retrieve() -> {}", anime))
                .subscribe();

        getAnimeEntityById(2)
                .doOnNext(entity -> log.info("exchangeToMono() -> {}", entity))
                .subscribe();

        getAllAnimesArray()
                .doOnNext(animes -> log.info("Array animes -> {}", Arrays.toString(animes)))
                .subscribe();

        getAllAnimesList()
                .doOnNext(animeList -> log.info("List animes -> {}", animeList))
                .subscribe();

        postNewAnime("Kingdom")
                .doOnNext(anime -> log.info("postForObject() -> {}", anime))
                .subscribe();

        postNewAnimeEntity("Samurai Champloo")
                .doOnNext(entity -> log.info("exchange-POST() -> {}", entity))
                .subscribe();

        updateAnime("Samurai Champloo 2")
                .doOnNext(response -> log.info("exchange-PUT -> {}", response))
                .subscribe();

        deleteAnimeById(10)
                .doOnNext(response -> log.info("exchange-DELETE -> {}", response))
                .subscribe();
    }

    private static Mono<Anime> getAnimeById(int id) {
        return webClient.get()
                .uri("/animes/{id}", id)
                .retrieve()
                .bodyToMono(Anime.class);
    }

    private static Mono<ResponseEntity<Anime>> getAnimeEntityById(int id) {
        return webClient.get()
                .uri("/animes/{id}", id)
                .exchangeToMono(response -> response.toEntity(Anime.class));
    }

    private static Mono<Anime[]> getAllAnimesArray() {
        return webClient.get()
                .uri("/animes/all")
                .retrieve()
                .bodyToMono(Anime[].class);
    }

    private static Flux<Anime> getAllAnimesList() {
        return webClient.get()
                .uri("/animes/all")
                .retrieve()
                .bodyToFlux(Anime.class);
    }

    private static Mono<Anime> postNewAnime(String name) {
        Anime anime = Anime.builder().name(name).build();
        return webClient.post()
                .uri("/animes")
                .body(Mono.just(anime), Anime.class)
                .retrieve()
                .bodyToMono(Anime.class);
    }

    private static Mono<ResponseEntity<Anime>> postNewAnimeEntity(String name) {
        Anime anime = Anime.builder().name(name).build();
        return webClient.post()
                .uri("/animes")
                .body(Mono.just(anime), Anime.class)
                .exchangeToMono(response -> response.toEntity(Anime.class));
    }

    private static Mono<ResponseEntity<Void>> updateAnime(String name) {
        return getAnimeById(2)
                .flatMap(anime -> {
                    anime.setName(name);
                    return webClient.put()
                            .uri("/animes")
                            .body(Mono.just(anime), Anime.class)
                            .exchangeToMono(response -> response.toEntity(Void.class));
                });
    }

    private static Mono<ResponseEntity<Void>> deleteAnimeById(int id) {
        return webClient.delete()
                .uri("/animes/{id}", id)
                .exchangeToMono(response -> response.toEntity(Void.class));
    }
}
```

### Explicação:

1. **Mono e Flux**:
   - `Mono`: Representa um único valor ou nenhum valor.
   - `Flux`: Representa uma sequência de 0 a N valores.

2. **GET Requests**:
   - `getAnimeById`: Retorna um `Mono<Anime>` para obter um anime por ID.
   - `getAnimeEntityById`: Retorna um `Mono<ResponseEntity<Anime>>` para obter uma resposta completa.
   - `getAllAnimesArray`: Retorna um `Mono<Anime[]>` para obter todos os animes como um array.
   - `getAllAnimesList`: Retorna um `Flux<Anime>` para obter todos os animes como uma lista reativa.

3. **POST Requests**:
   - `postNewAnime`: Retorna um `Mono<Anime>` para criar um novo anime.
   - `postNewAnimeEntity`: Retorna um `Mono<ResponseEntity<Anime>>` para criar um novo anime e obter a resposta completa.

4. **PUT Request**:
   - `updateAnime`: Atualiza um anime existente e retorna um `Mono<ResponseEntity<Void>>`.

5. **DELETE Request**:
   - `deleteAnimeById`: Deleta um anime por ID e retorna um `Mono<ResponseEntity<Void>>`.

### Reatividade:
- As chamadas `subscribe()` são usadas para iniciar a execução das operações reativas.
- Os operadores `doOnNext()` são usados para registrar informações quando os dados são emitidos.

Com essa abordagem, você pode trabalhar com as respostas de forma reativa, aproveitando o suporte do `WebClient` para programação reativa no Spring Boot.