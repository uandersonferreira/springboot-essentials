# Configuração das metricas do prometheus

## 1° Criar o file prometheus.yml
```yaml
global:
  scrape_interval:     15s # By default, scrape targets every 15 seconds.

  external_labels:
    monitor: 'codelab-monitor'

scrape_configs:
  - job_name: 'prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'springboot-essentials-actuator'
    scrape_interval: 5s
    metrics_path: "actuator/prometheus"
    static_configs:
      - targets: [ 'host.docker.internal:8080' ]
```
### Configurações Globais

```yaml
global:
  scrape_interval: 15s # Por padrão, coleta as métricas dos alvos a cada 15 segundos.
  
  external_labels:
    monitor: 'codelab-monitor' # Adiciona um rótulo externo a todas as séries temporais coletadas.
```

**Explicação:**

- `scrape_interval: 15s`: Define o intervalo global de coleta de métricas, ou seja, Prometheus tentará coletar métricas de todos os alvos configurados a cada 15 segundos. Isso pode ser sobrescrito em configurações específicas de trabalho (`job`).

- `external_labels`: Permite adicionar rótulos a todas as métricas coletadas, o que pode ser útil para identificar de qual instância ou cluster os dados foram coletados. No exemplo, o rótulo `monitor` com o valor `codelab-monitor` será adicionado a todas as métricas.

### Configurações de Scraping

#### Job: `prometheus`

```yaml
scrape_configs:
  - job_name: 'prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['localhost:9090']
```

**Explicação:**

- `job_name: 'prometheus'`: Nome do trabalho de scraping. Este nome será usado como valor do rótulo `job` nas métricas coletadas.

- `scrape_interval: 5s`: Sobrescreve o intervalo global, configurando Prometheus para coletar métricas deste trabalho a cada 5 segundos.

- `static_configs`: Configurações estáticas de alvo.

    - `targets: ['localhost:9090']`: Define os alvos de scraping como `localhost:9090`. Isso significa que Prometheus vai coletar métricas de si mesmo (já que a interface web do Prometheus e o endpoint de métricas rodam na mesma porta por padrão).

#### Job: `springboot-essentials-actuator`

```yaml
  - job_name: 'springboot-essentials-actuator'
    scrape_interval: 5s
    metrics_path: "actuator/prometheus"
    static_configs:
      - targets: [ 'host.docker.internal:8080' ]
```

**Explicação:**

- `job_name: 'springboot-essentials-actuator'`: Nome do trabalho de scraping. Este nome será usado como valor do rótulo `job` nas métricas coletadas.

- `scrape_interval: 5s`: Define que Prometheus deve coletar métricas deste trabalho a cada 5 segundos.

- `metrics_path: "actuator/prometheus"`: Define o caminho onde as métricas estão expostas na aplicação Spring Boot. Por padrão, o Spring Boot Actuator expõe métricas em `/actuator/prometheus`.

- `static_configs`: Configurações estáticas de alvo.

    - `targets: [ 'host.docker.internal:8080' ]`: Define o alvo de scraping como `host.docker.internal:8080`. `host.docker.internal` é um endereço especial que permite que contêineres Docker acessem serviços rodando na máquina host, útil quando o serviço Spring Boot está rodando fora do contêiner.

### Resumo

O arquivo `prometheus.yml` configura Prometheus para coletar métricas de dois alvos:

1. **Prometheus**: Coleta suas próprias métricas a cada 5 segundos.
2. **Spring Boot Application**: Coleta métricas da aplicação Spring Boot a cada 5 segundos, acessando o endpoint `/actuator/prometheus` exposto na porta 8080 do host Docker.

Essas configurações garantem que Prometheus tenha uma visão detalhada e 
em tempo real do desempenho de si mesmo e da aplicação Spring Boot
monitorada.


## 2° Configurações do Serviço Prometheus no `docker-compose.yml`

```yaml
prometheus:
  image: prom/prometheus:latest
  container_name: prometheus
  volumes:
    - "./src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml" # Copia o arquivo de configuração local para o contêiner.
  command: "--config.file=/etc/prometheus/prometheus.yml" # Define a localização do arquivo de configuração no contêiner.
  ports:
    - "9090:9090" # Mapeia a porta 9090 do contêiner para a porta 9090 do host.
  extra_hosts:
    - 'host.docker.internal:host-gateway' # Permite que o contêiner acesse serviços rodando na máquina host.
```

**Explicação Detalhada:**

- **image**: `prom/prometheus:latest`
    - Especifica a imagem Docker a ser usada para o serviço Prometheus. `prom/prometheus:latest` refere-se à última versão disponível da imagem oficial do Prometheus.

- **container_name**: `prometheus`
    - Define o nome do contêiner como `prometheus`. Isso facilita a identificação e a gestão do contêiner.

- **volumes**:
    - `./src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml`
        - Monta o arquivo de configuração `prometheus.yml` da máquina host para o contêiner. O arquivo local `./src/main/resources/prometheus.yml` será acessível no contêiner em `/etc/prometheus/prometheus.yml`.

- **command**: `--config.file=/etc/prometheus/prometheus.yml`
    - Define um comando a ser executado quando o contêiner é iniciado. Neste caso, especifica que o Prometheus deve usar o arquivo de configuração localizado em `/etc/prometheus/prometheus.yml`.

- **ports**:
    - `"9090:9090"`
        - Mapeia a porta 9090 do contêiner para a porta 9090 do host. Isso permite que o Prometheus seja acessado via `http://localhost:9090` no host.

- **extra_hosts**:
    - `'host.docker.internal:host-gateway'`
        - Adiciona uma entrada no arquivo `/etc/hosts` do contêiner, permitindo que o contêiner acesse serviços rodando na máquina host através do endereço `host.docker.internal`. Isso é particularmente útil quando você deseja que o contêiner interaja com serviços que estão rodando fora do ambiente Docker, como uma aplicação Spring Boot rodando localmente.

### Resumo

A configuração do serviço Prometheus no `docker-compose.yml` especifica:

1. **Imagem Docker**: Utiliza a última versão da imagem oficial do Prometheus.
2. **Nome do Contêiner**: Define o nome do contêiner como `prometheus`.
3. **Volumes**: Monta o arquivo de configuração local `prometheus.yml` no contêiner.
4. **Comando de Inicialização**: Especifica o caminho do arquivo de configuração a ser usado pelo Prometheus.
5. **Mapeamento de Portas**: Mapeia a porta 9090 do contêiner para a porta 9090 do host, permitindo acesso via navegador.
6. **Hosts Adicionais**: Permite que o contêiner acesse serviços rodando na máquina host usando `host.docker.internal`.

Com essas configurações, o Prometheus será configurado para usar um arquivo de configuração personalizado, estará acessível na porta 9090 do host, e poderá se comunicar com serviços externos ao Docker.