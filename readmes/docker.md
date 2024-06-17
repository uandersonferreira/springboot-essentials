## POR QUE DOCKER?

- No mundo dos microservices, posso ter 100 microservices em execução para minha aplicação. O que usar para alcançar com menos esforço e custo?

    - **Resposta**: Docker. Ele permite empacotar, implantar e gerenciar aplicativos de forma eficiente e econômica, facilitando a execução de múltiplos serviços em um ambiente de microservices.

- Como podemos implantar todos esses microservices?

    - **Resposta**: Usando Docker, podemos implantar facilmente os microservices usando contêineres Docker, que são leves e portáteis, permitindo uma implantação rápida e consistente.

- Como podemos mover nossos microservices por todos os ambientes (por exemplo, desenvolvimento, teste, produção) com menos esforço e custo? Como vamos gerenciar as configurações em diferentes ambientes?

    - **Resposta**: Com o Docker, podemos criar imagens de contêiner que encapsulam o aplicativo e suas dependências, garantindo consistência em todos os ambientes. As configurações podem ser gerenciadas de forma centralizada e distribuídas com os contêineres, reduzindo a sobrecarga e os erros associados.

- Como podemos dimensionar nossas aplicações de microservices sob demanda e em tempo real?

    - **Resposta**: O Docker facilita a escalabilidade horizontal, permitindo que os contêineres sejam replicados ou dimensionados dinamicamente com base na demanda. Isso permite que as aplicações de microservices respondam rapidamente às flutuações no tráfego e na carga de trabalho, garantindo uma experiência de usuário consistente.


## Comandos Docker - Frequentemente utilizados !!

## Comandos básicos para gerenciamento de container docker

- `docker images`
    - Lista todas as imagens do Docker presentes no servidor.

- `docker image inspect (image-id)`
    - Exibe informações detalhadas sobre a imagem

- `docker image rm (image-id)`
    - Para remover uma ou várias imagens pelos IDs das imagens fornecidos.

- `docker ps`
    - Para mostrar todos os containers

- `docker build . -t [nome-da-imagem]`
    - Gera uma imagem do Docker com o nome da imagem fornecido

- `docker run -p [porta-do-host]:[porta-do-container] [nome-da-imagem]`
    - Inicia o container do Docker para a imagem fornecida

- `docker container start [container-id]`
    - Inicia um ou mais containers parados com o ID fornecido.

- `docker container stop [container-id]`
    - Para um ou mais containers em execução com o ID fornecido.

- `docker container kill [container-id]`
    - Mata um ou mais containers instantaneamente.

- `docker container restart [container-id]`
    - Reinicia um ou mais containers.

- `docker container logs [container-id]`
    - Obtém logs do container com o ID do container fornecido

- `docker image pull <tag>`
    - Baixa a imagem especificada, este comando pode ser executado implicitamente, quando o Docker precisa de uma imagem para outra operação e não consegue encontrá-la no cache local.

- `docker image ls`
    - Lista todas as imagens já baixadas, também é possível usar a sintaxe antiga: docker images

- `docker image rm <tag>`
    - Remove uma imagem do cache local, também é possível usar a sintaxe antiga: docker rmi <tag>

- `docker image inspect <tag>`
    - Extrai várias informações usando um formato JSON da imagem indicada.

- `docker image tag <source> <tag>`
    - Cria uma nova tag baseada em uma tag anterior ou hash.

- `docker image build -t <tag>`
    - Permite a criação de uma nova imagem, como veremos melhor em build.

- `docker image push <tag>`
    - Permite o envio de uma imagem ou tag local para um registro


## Formas de iniciar um projeto spring boot

STEOS TO DOCKERIZE OUR SORING BOOT APPLICATION
- Run mvn clean install -> create fat jar file in target folder
- Create docker file with the commands
- Run docker build . -t my-app/dockerdemo:1
- Run docker run -d -p 8080:8080 my-app/dockerdemo:1

1°. Gerar o .jar com o maven: mvn clean package

1. `mvn clean package`: Este comando limpa o projeto excluindo o diretório "target", compila o código-fonte, executa testes e empacota o código compilado em um formato distribuível, como um arquivo JAR, WAR ou EAR. O artefato empacotado é armazenado no diretório "target", mas não é instalado no repositório local do Maven.

2. `mvn clean install`: Este comando realiza todas as etapas do `mvn clean package`, mas também instala o artefato empacotado no repositório local do Maven. Isso é útil quando você tem outros projetos que dependem do projeto atual, pois agora eles podem acessar o artefato instalado a partir do seu repositório local.

Resumindo, a principal diferença entre os dois comandos é que `mvn clean install` instala o artefato empacotado no repositório local do Maven, enquanto `mvn clean package` não o faz


```Java
 java -jar caminho-do-arquivo.jar
```
Ex:
- java -jar target/DockerDemo-0.0.1-SNAPSHOT.jar

## Configurando o DockerFile
```DockerFile
# Define a imagem base
FROM openjdk:17-jdk-slim
LABEL org.app.image.author="Uanderson Oliveira"
# Copia o arquivo JAR do seu projeto para dentro do container
COPY /target/DockerDemo-0.0.1-SNAPSHOT.jar my-app.jar
#Especifica o comando executável padrão.
ENTRYPOINT "java" "-jar" "my-app.jar"
```
## Gerando uma imagem apartir das configurações do DockerFile
```java
docker build . -t my-app/dockerdemo:1

```

## Iniciando um container docker com a nossa image gerada

```
docker run -p 8080:8080 my-app/dockerdemo:1

```

## Arquitetura

**Máquinas Virtuais (VMs)**:
- Uma VM inclui um sistema operacional completo, incluindo um kernel, além das bibliotecas e aplicativos necessários.
- As VMs são executadas sobre um hypervisor, que pode ser do tipo 1 (bare-metal, como VMware ESXi) ou tipo 2 (hospedado, como VirtualBox).
- Cada VM tem seu próprio sistema operacional, o que consome mais recursos (CPU, memória, armazenamento).

**Docker (Contêineres)**:
- Docker compartilha o kernel do sistema operacional host entre todos os contêineres.
- Cada contêiner inclui apenas as bibliotecas e binários necessários para o aplicativo, tornando-os mais leves.
- Docker utiliza tecnologias de virtualização a nível de sistema operacional, como namespaces e cgroups, para isolamento.

### Desempenho

**Máquinas Virtuais**:
- VMs são mais pesadas porque cada uma executa um sistema operacional completo.
- Isso pode resultar em maior uso de recursos e menor desempenho, especialmente ao executar muitas VMs.

**Docker**:
- Contêineres são mais leves porque compartilham o kernel do SO e não precisam de um sistema operacional completo.
- Isso resulta em um uso mais eficiente dos recursos e melhor desempenho, especialmente ao executar muitos contêineres.

### Tempo de Inicialização

**Máquinas Virtuais**:
- O tempo de inicialização de uma VM é relativamente alto, pois o sistema operacional precisa ser carregado.

**Docker**:
- Os contêineres iniciam muito rapidamente, geralmente em segundos, porque não há necessidade de carregar um sistema operacional completo.

### Portabilidade e Consistência

**Máquinas Virtuais**:
- VMs são portáteis, mas a migração pode ser complexa devido ao tamanho das imagens e à necessidade de compatibilidade do hypervisor.

**Docker**:
- Contêineres são altamente portáteis e consistentes, garantindo que um aplicativo funcione da mesma forma em qualquer ambiente que suporte Docker.

### Isolamento e Segurança

**Máquinas Virtuais**:
- VMs oferecem forte isolamento, pois cada VM opera de forma independente com seu próprio sistema operacional.
- A segurança é elevada devido ao isolamento completo do sistema operacional.

**Docker**:
- Contêineres compartilham o kernel do SO, o que pode levar a um isolamento menos robusto comparado às VMs.
- Contudo, Docker implementa várias camadas de segurança e o isolamento é suficiente para a maioria dos casos de uso.

### Uso de Recursos

**Máquinas Virtuais**:
- VMs consomem mais recursos, pois cada uma precisa de seu próprio sistema operacional e recursos alocados (CPU, RAM, armazenamento).

**Docker**:
- Contêineres são mais eficientes em termos de uso de recursos, pois compartilham o kernel do sistema operacional e apenas o necessário para rodar o aplicativo é incluído.

### Exemplos de Uso

**Máquinas Virtuais**:
- Ideal para casos onde é necessário um isolamento completo, como em ambientes multi-tenant, onde diferentes clientes precisam de segurança e isolamento completos.
- Útil para rodar diferentes sistemas operacionais no mesmo hardware.

**Docker**:
- Perfeito para desenvolvimento e implantação contínua (CI/CD) devido à sua rapidez e leveza.
- Ideal para microservices e aplicações distribuídas, onde a eficiência e a escalabilidade são importantes.

### Conclusão

Em resumo, Docker e máquinas virtuais oferecem soluções para executar aplicativos em ambientes isolados, mas com abordagens diferentes. Docker é mais leve e eficiente em termos de recursos, com inicialização rápida e portabilidade superior, enquanto máquinas virtuais oferecem um isolamento mais robusto e são mais adequadas para rodar diferentes sistemas operacionais ou em ambientes que requerem isolamento completo. A escolha entre Docker e VMs depende dos requisitos específicos do seu projeto e das prioridades em termos de desempenho, isolamento e gerenciamento de recursos.