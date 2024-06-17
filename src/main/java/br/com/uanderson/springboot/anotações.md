# Spring Boot - Estudos

Este repositório contém anotações e exemplos práticos sobre diversos tópicos do Spring Boot 2. 

## Tópicos

1. [Componentes e Injeção de Dependência](#componentes-e-injeção-de-dependência)
   - @Component
   - @Autowired
   - @SpringBootApplication
2. [Hot Swap com Spring Boot Devtools](#hot-swap-com-spring-boot-devtools)
3. [Gerando Projeto com start.spring.io](#gerando-projeto-com-startspringio)
4. [Métodos HTTP](#métodos-http)
   - GET
   - POST
   - DELETE
   - PUT
5. [Spring Data JPA](#spring-data-jpa)
6. [Framework de Mapeamento MapStruct](#framework-de-mapeamento-mapstruct)

## Componentes e Injeção de Dependência

### @Component
Anotação usada para indicar que uma classe é um componente gerenciado pelo Spring.

**Sintaxe:**
```java
import org.springframework.stereotype.Component;

@Component
public class MyComponent {
    // lógica do componente
}
```

### @Autowired
Anotação usada para injetar automaticamente dependências gerenciadas pelo Spring.

**Sintaxe:**
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyService {
    private final MyComponent myComponent;

    @Autowired
    public MyService(MyComponent myComponent) {
        this.myComponent = myComponent;
    }
}
```

### @SpringBootApplication
Anotação usada na classe principal para habilitar várias funcionalidades do Spring Boot, como configuração automática.

**Sintaxe:**
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Hot Swap com Spring Boot Devtools

O Spring Boot Devtools facilita o desenvolvimento, permitindo a reinicialização automática da aplicação quando há mudanças no código.

**Configuração:**
Adicionar a dependência no `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

## Gerando Projeto com start.spring.io

1. Acesse [start.spring.io](https://start.spring.io/).
2. Configure o projeto selecionando:
    - Project: Maven ou Gradle
    - Language: Java
    - Spring Boot: versão mais recente estável
    - Dependencies: escolha as dependências necessárias
3. Clique em "Generate" para baixar o projeto.

## Métodos HTTP

### Método GET
Usado para buscar recursos do servidor.

**Sintaxe:**
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping("/resource")
    public String getResource() {
        return "Resource";
    }
}
```

### Método POST
Usado para criar novos recursos no servidor.

**Sintaxe:**
```java
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @PostMapping("/resource")
    public String createResource(@RequestBody Resource resource) {
        return "Resource created";
    }
}
```

### Método DELETE
Usado para deletar recursos do servidor.

**Sintaxe:**
```java
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @DeleteMapping("/resource/{id}")
    public String deleteResource(@PathVariable Long id) {
        return "Resource deleted";
    }
}
```

### Método PUT
Usado para atualizar recursos no servidor.

**Sintaxe:**
```java
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @PutMapping("/resource/{id}")
    public String updateResource(@PathVariable Long id, @RequestBody Resource resource) {
        return "Resource updated";
    }
}
```

## Spring Data JPA

### Parte 1
Integração do Spring com JPA para facilitar operações de banco de dados.

**Configuração:**
Adicionar dependência no `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Parte 2
Criar repositórios JPA para gerenciar entidades.

**Exemplo:**
```java
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AnimeRepository  extends JpaRepository<Anime, Long> {

}//class
```

## Framework de Mapeamento MapStruct

MapStruct é usado para mapeamento de objetos DTOs para entidades e vice-versa.

**Configuração:**
Adicionar dependência no `pom.xml`:
```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${mapstruct.version}</version>
</dependency>
```

**Exemplo:**
```java
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")//indicar ao MapStruct que ela é uma class de mapeamento
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);

    /*
    MapStruct- Necessita que configuremos interfaces ou clases de mapeamento que descrevem
              como as propriedades dos objetos de origem e destino devem ser mapeadas.

     */
}
```

Para que eu possa lembrar deste tópico sobre **JPA Query Methods** no futuro, você pode referenciar esta informação de maneira clara e específica. Aqui está um exemplo de como você pode fazer isso:

**Referência para lembrar do tópico JPA Query Methods:**


# JPA Query Methods - Referência

Este tópico aborda as diversas maneiras de criar consultas com Spring Data JPA, incluindo estratégias de busca de consultas, consultas derivadas, e a criação de consultas.

### Conteúdo
- **Query Lookup Strategies**: Definindo consultas manualmente ou derivando do nome do método.
- **Derived Queries**: Utilização de predicados como `IsStartingWith`, `StartingWith`, `IsEndingWith`, etc.
- **Declared Queries**: Uso de consultas nomeadas ou anotação `@Query`.
- **Query Creation**: Mecanismo de criação de consultas em JPA.
- **Keywords Suportadas**: Palavras-chave como `Distinct`, `And`, `Or`, `Is`, `Equals`, `Between`, etc.

### Exemplos
- **Exemplo de criação de consulta a partir dos nomes dos métodos**:
```java
public interface UserRepository extends Repository<User, Long> {
  List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
}
```
- **Tabela de Keywords**: Palavras-chave suportadas e suas traduções para JPQL.

### Considerações sobre `Distinct`
- Diferença entre `select distinct u from User u` e `select distinct u.lastname from User u`.
- Consultas `countDistinctByLastname(String lastname)` podem produzir resultados inesperados.

Para detalhes completos, consulte o documento original ou peça a referência "JPA Query Methods".

---

**Nota:** Para consultar ou aprender mais sobre este tópico no futuro, utilize a referência "JPA Query Methods".


