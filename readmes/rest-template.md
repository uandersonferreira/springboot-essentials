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