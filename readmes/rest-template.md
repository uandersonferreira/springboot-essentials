# RestTemplate

`RestTemplate` é uma classe no Spring Framework que fornece uma maneira simplificada de fazer chamadas HTTP e interagir com serviços RESTful. É uma classe altamente configurável que pode ser usada para enviar requisições HTTP de maneira simples e intuitiva.
 - Consumo de API eternas
#### `getForObject`

O método `getForObject` do `RestTemplate` é utilizado para fazer uma requisição HTTP GET e deserializar a resposta diretamente em um objeto.

**Sintaxe:**

```java
<T> T getForObject(String url, Class<T> responseType, Object... uriVariables);
```

- `url`: A URL do serviço REST.
- `responseType`: A classe do objeto no qual a resposta será deserializada.
- `uriVariables`: (Opcional) Variáveis de URI, se necessário.

Exemplo simplificado:

```java
String url = "https://api.exemplo.com/usuarios/1";
Usuario usuario = restTemplate.getForObject(url, Usuario.class);
```

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

O método `getForEntity` é semelhante ao `getForObject`, mas retorna um objeto `ResponseEntity` que contém não apenas o corpo da resposta, mas também informações adicionais como o status da resposta e cabeçalhos HTTP.

**Sintaxe:**

```java
<T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables);
```

- `url`: A URL do serviço REST.
- `responseType`: A classe do objeto no qual a resposta será deserializada.
- `uriVariables`: (Opcional) Variáveis de URI, se necessário.

Exemplo simplificado:

```java
String url = "https://api.exemplo.com/usuarios/1";
ResponseEntity<Usuario> response = restTemplate.getForEntity(url, Usuario.class);
HttpStatus statusCode = response.getStatusCode();
Usuario usuario = response.getBody();
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

### Outros Métodos do `RestTemplate`

1. **`postForObject`**
  - Envia uma solicitação HTTP POST e retorna o corpo da resposta convertido em um objeto do tipo especificado.
  - **Sintaxe**:
    ```java
    <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables);
    ```
  - **Exemplo**:
    ```java
    Anime novoAnime = new Anime("My Hero Academia");
    Anime resposta = restTemplate.postForObject("http://localhost:8080/animes", novoAnime, Anime.class);
    ```

2. **`postForEntity`**
  - Envia uma solicitação HTTP POST e retorna a resposta completa como uma instância de `ResponseEntity`.
  - **Sintaxe**:
    ```java
    <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables);
    ```
  - **Exemplo**:
    ```java
    Anime novoAnime = new Anime("My Hero Academia");
    ResponseEntity<Anime> resposta = restTemplate.postForEntity("http://localhost:8080/animes", novoAnime, Anime.class);
    ```

3. **`postForLocation`**
  - Envia uma solicitação HTTP POST e retorna a URL do novo recurso criado.
  - **Sintaxe**:
    ```java
    URI postForLocation(String url, Object request, Object... uriVariables);
    ```
  - **Exemplo**:
    ```java
    Anime novoAnime = new Anime("My Hero Academia");
    URI location = restTemplate.postForLocation("http://localhost:8080/animes", novoAnime);
    ```

4. **`put`**
  - Envia uma solicitação HTTP PUT para atualizar um recurso.
  - **Sintaxe**:
    ```java
    void put(String url, Object request, Object... uriVariables);
    ```
  - **Exemplo**:
    ```java
    Anime animeAtualizado = new Anime(10, "Attack on Titan");
    restTemplate.put("http://localhost:8080/animes/{id}", animeAtualizado, 10);
    ```

5. **`delete`**
  - Envia uma solicitação HTTP DELETE para deletar um recurso.
  - **Sintaxe**:
    ```java
    void delete(String url, Object... uriVariables)
    ```
  - **Exemplo**:
    ```java
    restTemplate.delete("http://localhost:8080/animes/{id}", 10);
    ```

6. **`exchange`**
  - Executa uma solicitação HTTP especificando o método HTTP (GET, POST, PUT, DELETE, etc.) e retorna uma instância de `ResponseEntity`.
  - **Sintaxe**:
    ```java
    <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables)
    ```
  - **Exemplo**:
    ```java
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Anime> requestEntity = new HttpEntity<>(novoAnime, headers);
    ResponseEntity<Anime> response = restTemplate.exchange("http://localhost:8080/animes", HttpMethod.POST, requestEntity, Anime.class);
    ```

7. **`execute`**
  - Executa uma solicitação HTTP genérica, fornecendo controle total sobre a solicitação e a resposta.
  - **Sintaxe**:
    ```java
    <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... uriVariables)
    ```
  - **Exemplo**:
    ```java
    Anime resultado = restTemplate.execute(
        "http://localhost:8080/animes/{id}", 
        HttpMethod.GET, 
        null, 
        clientHttpResponse -> {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(clientHttpResponse.getBody(), Anime.class);
        }, 
        10
    );
    ```

### Exemplo Completo com Comentários

Aqui está o código que demonstra o uso dos principais métodos do `RestTemplate` com comentários adicionais:

```java
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // GET request using getForEntity
        ResponseEntity<Anime> entity = restTemplate.getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info("getForEntity -> {}", entity);

        // GET request using getForObject
        Anime anime = restTemplate.getForObject("http://localhost:8080/animes/{id}", Anime.class, 10);
        log.info("getForObject -> {}", anime);

        // POST request using postForObject
        Anime novoAnime = new Anime("My Hero Academia");
        Anime resposta = restTemplate.postForObject("http://localhost:8080/animes", novoAnime, Anime.class);
        log.info("postForObject -> {}", resposta);

        // POST request using postForEntity
        ResponseEntity<Anime> respostaEntity = restTemplate.postForEntity("http://localhost:8080/animes", novoAnime, Anime.class);
        log.info("postForEntity -> {}", respostaEntity);

        // PUT request
        Anime animeAtualizado = new Anime(10, "Attack on Titan");
        restTemplate.put("http://localhost:8080/animes/{id}", animeAtualizado, 10);
        log.info("put -> Anime atualizado com sucesso");

        // DELETE request
        restTemplate.delete("http://localhost:8080/animes/{id}", 10);
        log.info("delete -> Anime deletado com sucesso");

        // POST request using exchange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Anime> requestEntity = new HttpEntity<>(novoAnime, headers);
        ResponseEntity<Anime> response = restTemplate.exchange("http://localhost:8080/animes", HttpMethod.POST, requestEntity, Anime.class);
        log.info("exchange (POST) -> {}", response);

        // GET request using execute
        Anime resultado = restTemplate.execute("http://localhost:8080/animes/{id}", HttpMethod.GET, null, clientHttpResponse -> {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(clientHttpResponse.getBody(), Anime.class);
        }, 10);
        log.info("execute (GET) -> {}", resultado);
    }
}
```

### Explicações Adicionais

- **`postForObject`**: Facilita o envio de dados e a obtenção direta da resposta deserializada.
- **`postForEntity`**: Similar ao `postForObject`, mas retorna toda a resposta HTTP.
- **`postForLocation`**: Útil para obter a URL do novo recurso criado.
- **`put`**: Atualiza um recurso existente.
- **`delete`**: Remove um recurso.
- **`exchange`**: Proporciona flexibilidade ao permitir especificar o método HTTP e configurar cabeçalhos e entidades de requisição.
- **`execute`**: Oferece controle total sobre a solicitação e resposta, útil para casos mais complexos.

Esses métodos cobrem uma ampla gama de operações que você pode precisar realizar com um serviço RESTful, permitindo interação completa com os endpoints HTTP.



## Alternativa Mais Atualizada: WebClient

`RestTemplate` está sendo gradualmente substituído pelo `WebClient`, que faz parte do Spring WebFlux. `WebClient` é mais flexível e suporta operações assíncronas e reativas, sendo uma escolha melhor para novas aplicações.

#### WebClient

`WebClient` é uma alternativa moderna ao `RestTemplate` e faz parte do Spring WebFlux. Ele é mais poderoso e oferece suporte para programação reativa.

**Configuração básica:**

```java
WebClient webClient = WebClient.create();
```

### `retrieve` e `exchangeToMono`

- `retrieve`: Obtém diretamente o corpo da resposta.
- `exchangeToMono`: Retorna a resposta completa (`ClientResponse`) que pode ser manipulada posteriormente.

Exemplo de uso com `retrieve`:

```java
Usuario usuario = webClient.get()
    .uri("https://api.exemplo.com/usuarios/1")
    .retrieve()
    .bodyToMono(Usuario.class)
    .block();
```

Exemplo de uso com `exchangeToMono`:

```java
ClientResponse response = webClient.get()
    .uri("https://api.exemplo.com/usuarios/1")
    .exchangeToMono(clientResponse -> Mono.just(clientResponse))
    .block();

HttpStatus statusCode = response.statusCode();
Usuario usuario = response.bodyToMono(Usuario.class).block();
```

### Conclusão

- **`RestTemplate`**:
    - `getForObject`: Simples e retorna o corpo da resposta diretamente.
    - `getForEntity`: Retorna um `ResponseEntity` com status e cabeçalhos além do corpo da resposta.

- **`WebClient`**:
    - Moderno e suporta programação reativa.
    - Métodos como `retrieve` e `exchangeToMono` oferecem uma interface mais flexível e poderosa para manipulação de respostas HTTP.

Para novos projetos, é recomendado o uso de `WebClient` devido à sua capacidade de lidar com operações assíncronas e sua flexibilidade.


# O que é Super Type Token?

O conceito de **Super Type Token** foi introduzido por Neal Gafter e é usado para 
resolver um problema com tipos genéricos em Java. Quando você usa genéricos, a informação
do tipo é perdida durante a compilação devido ao **type erasure**. Isso pode ser problemático
quando você deseja deserializar uma resposta JSON para um tipo genérico complexo, como `List<Anime>`.

## Como o `ParameterizedTypeReference` resolve isso?

`ParameterizedTypeReference` é uma classe do Spring que retém a informação do tipo genérico em tempo de execução, permitindo que o `RestTemplate` saiba exatamente qual é o tipo de retorno esperado, mesmo quando esse tipo é genérico.

## Exemplo no seu código

No seu exemplo, você usa `ParameterizedTypeReference` para especificar que a resposta deve ser deserializada como `List<Anime>`. Aqui está o trecho de código comentado:

```java
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // Faz uma solicitação HTTP GET usando exchange para obter uma lista de animes
        ResponseEntity<List<Anime>> exchange = restTemplate.exchange(
                "http://localhost:8080/animes/all", // URL para obter todos os animes
                HttpMethod.GET, // Método HTTP GET
                null, // Não há entidade de requisição a ser enviada
                new ParameterizedTypeReference<List<Anime>>() {} // Tipo da resposta esperada, usando ParameterizedTypeReference para lidar com tipos genéricos.
        );
        // Log da resposta que é uma lista de animes
        log.info("exchange -> {}", exchange.getBody());
    }
}
```

### Comentários Explicativos

1. **`RestTemplate.exchange`**:
    - **URL**: `"http://localhost:8080/animes/all"` (endpoint para obter todos os animes)
    - **Método HTTP**: `HttpMethod.GET` (realiza uma solicitação GET)
    - **Entidade de Requisição**: `null` (não há necessidade de enviar um corpo na requisição GET)
    - **Tipo da Resposta**: `new ParameterizedTypeReference<List<Anime>>() {}` (usa `ParameterizedTypeReference` para informar ao `RestTemplate` que esperamos um `List<Anime>`)

2. **Log**:
    - `log.info("exchange -> {}", exchange.getBody());` (registra o corpo da resposta, que é uma lista de animes)

### Por que isso é necessário?

Sem `ParameterizedTypeReference`, você não poderia diretamente especificar que espera um `List<Anime>`, pois a informação sobre os tipos genéricos seria perdida devido ao type erasure. `ParameterizedTypeReference` retém essa informação, permitindo a desserialização correta do JSON para o tipo genérico apropriado.

#### Existem outras maneiras de implementar e utilizar o conceito de Super Type Token além do `ParameterizedTypeReference` do Spring. Aqui estão algumas abordagens comuns:

## 1. **TypeToken do Gson**

A biblioteca Gson do Google fornece a classe `TypeToken`, que é usada para capturar e manter informações sobre tipos genéricos em tempo de execução. Isso é útil para deserializar JSON em tipos genéricos complexos.

**Exemplo com Gson**:

```java
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class GsonExample {
    public static void main(String[] args) {
        Gson gson = new Gson();
        String json = "[{\"id\":1, \"name\":\"My Hero Academia\"}, {\"id\":2, \"name\":\"Attack on Titan\"}]";

        // Usando TypeToken para capturar o tipo genérico List<Anime>
        Type listType = new TypeToken<List<Anime>>() {}.getType();
        List<Anime> animeList = gson.fromJson(json, listType);

        // Imprimindo a lista de animes
        animeList.forEach(anime -> System.out.println(anime.getName()));
    }
}
```

## 2. **Java's TypeReference do Jackson**

A biblioteca Jackson fornece a classe `TypeReference`, que também é usada para reter informações sobre tipos genéricos em tempo de execução. Isso é especialmente útil para deserializar JSON em tipos genéricos complexos.

**Exemplo com Jackson**:

```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JacksonExample {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[{\"id\":1, \"name\":\"My Hero Academia\"}, {\"id\":2, \"name\":\"Attack on Titan\"}]";

        // Usando TypeReference para capturar o tipo genérico List<Anime>
        TypeReference<List<Anime>> typeRef = new TypeReference<List<Anime>>() {};
        List<Anime> animeList = objectMapper.readValue(json, typeRef);

        // Imprimindo a lista de animes
        animeList.forEach(anime -> System.out.println(anime.getName()));
    }
}
```

### 3. **Guava's TypeToken**

A biblioteca Guava também oferece uma implementação de Super Type Token chamada `TypeToken`, que pode ser usada para capturar e manipular informações de tipos genéricos.

**Exemplo com Guava**:

```java
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class GuavaExample {
    public static void main(String[] args) {
        TypeToken<List<Anime>> typeToken = new TypeToken<List<Anime>>() {};
        Type type = typeToken.getType();
        
        // Supondo que você tenha um método para desserializar JSON
        // List<Anime> animeList = deserializeJson(json, type);

        // Imprimir o tipo capturado
        System.out.println(type);
    }
}
```

### 4. **Custom Type Capture**

Você pode criar suas próprias classes para capturar tipos genéricos, semelhante ao que bibliotecas como Gson e Jackson fazem. Isso geralmente envolve usar reflexão para capturar a informação do tipo em tempo de execução.

**Exemplo Customizado**:

```java
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> {
    private final Type type;

    protected TypeReference() {
        this.type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }
}

public class CustomTypeTokenExample {
    public static void main(String[] args) {
        TypeReference<List<Anime>> typeRef = new TypeReference<List<Anime>>() {};
        System.out.println("Captured Type: " + typeRef.getType());
    }
}
```

### Resumo

O conceito de Super Type Token pode ser implementado de várias maneiras usando diferentes
bibliotecas ou até mesmo criando sua própria solução customizada. 
As abordagens mais comuns incluem:

1. **`TypeToken` do Gson**
2. **`TypeReference` do Jackson**
3. **`TypeToken` do Guava**
4. **Implementações Customizadas**

