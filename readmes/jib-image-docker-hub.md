# Documentação de Configuração e Execução do Projeto Spring Boot com Jib e Docker Compose

## Pré-requisitos

- Docker e Docker Compose instalados
- Maven instalado
- Conta no Docker Hub (para enviar imagens)
- 
## Links Úteis

- [Início Rápido com Jib](https://cloud.google.com/java/getting-started/jib?hl=pt-br)
- [Repositório Jib Maven Plugin no GitHub](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin)
- [Imagens Distroless Disponíveis](https://console.cloud.google.com/gcr/images/distroless/GLOBAL)
- [Código do Projeto no GitHub](https://github.com/uandersonferreira/springboot-essentials-devdojo-update)

---
## Configuração do Projeto

### `pom.xml`

Adicione as seguintes propriedades e o plugin Jib ao seu `pom.xml`:

```xml
<properties>
    <java.version>17</java.version>
    <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
    <maven-compiler-plugin.version>3.13.0</maven-compiler-plugin.version>
    <lombok.version>1.18.32</lombok.version>
    <jib-maven-plugin.version>3.4.3</jib-maven-plugin.version>
    <docker.distroless.image>gcr.io/distroless/java17</docker.distroless.image>
    <!-- Para enviar a imagem ao Docker Hub, inclua (Faça o teste) "registry.hub.docker.com/uandersonferreira" -->
    <docker.repo.url>uandersonferreira</docker.repo.url> <!-- Após enviar a imagem ao Docker Hub, remova "registry.hub.docker.com" -->
    <docker.repo.project>springboot-essentials-update</docker.repo.project>
    <docker.image.name>${docker.repo.url}/${docker.repo.project}</docker.image.name>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>com.google.cloud.tools</groupId>
            <artifactId>jib-maven-plugin</artifactId>
            <version>${jib-maven-plugin.version}</version>
            <configuration>
                <from>
                    <image>${docker.distroless.image}</image>
                </from>
                <to>
                    <image>${docker.image.name}</image>
                    <tags>
                        <tag>${project.version}</tag>
                    </tags>
                </to>
                <container>
                    <mainClass>br.com.uanderson.springboot.SpringbootEssentialsApplication</mainClass>
                </container>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Configuração do Docker Compose

Crie ou edite o arquivo `docker-compose.yml` com as seguintes configurações:

```yaml
version: '3.8'
x-database-variables: &database-variables
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres_anime_container:5432/db_anime
  SPRING_DATASOURCE_USERNAME: postgres
  SPRING_DATASOURCE_PASSWORD: postgres

services:
  db:
    image: postgres
    container_name: postgres_anime_container
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: db_anime
    ports:
      - "5432:5432"
    volumes:
      - db_data:/var/lib/postgresql/data
    mem_limit: 512m

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    volumes:
      - "./src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml"
    command: "--config.file=/etc/prometheus/prometheus.yml"
    ports:
      - "9090:9090"
    extra_hosts:
      - 'host.docker.internal:host-gateway'
    mem_limit: 128m

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    restart: unless-stopped
    ports:
      - '3000:3000'
    mem_limit: 128m

  springboot-essentials-app:
    # image: registry.hub.docker.com/uandersonferreira/springboot-essentials-update:0.0.1-SNAPSHOT
    image: uandersonferreira/springboot-essentials-update:0.0.1-SNAPSHOT <!-- Após enviar para o Docker Hub, remova "registry.hub.docker.com" -->
    ports:
      - "8080:8080"
    environment:
      <<: *database-variables
    mem_limit: 512m

volumes:
  db_data:
```

## Passo a Passo para Configurar e Executar
> ! Lembrar de criar um Repository na sua conta do docker hub para receber a imagem.

### 1. Construir a Imagem Docker usando Jib

Utilize os seguintes comandos para construir e enviar a imagem Docker para o Docker Hub:

```sh
# Construir a imagem Docker localmente
sudo mvn jib:dockerBuild

# Enviar a imagem Docker para o Docker Hub
sudo mvn jib:build
```

### 2. Configurar o Docker Compose

Certifique-se de que o arquivo `docker-compose.yml` está configurado corretamente e contém todos os serviços necessários (banco de dados, Prometheus, Grafana e a aplicação Spring Boot).

### 3. Executar o Docker Compose

Execute os seguintes comandos para iniciar os serviços definidos no `docker-compose.yml`:

```sh
# Parar e remover todos os contêineres
docker-compose down

# Iniciar todos os contêineres
docker-compose up
```

### 4. Acessar os Serviços

- **Grafana**: Acesse o Grafana em `http://localhost:3000/`
- **Prometheus**: Acesse o Prometheus em `http://localhost:9090/`
- **Aplicação Spring Boot**: Acesse a aplicação em `http://localhost:8080/`

---

