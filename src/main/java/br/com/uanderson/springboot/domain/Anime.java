package br.com.uanderson.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

/*
Erros de 'serializer', 'BeanSerializer' , 'Serialização':
normalmente é por causa dos getters ou setters, pois o Jackson que mapeia os
objetos irá tentar criar um novo objeto utilizando eles,
mas quando não se têm ele não consegue realizar a criação de novos objetos.

OBSERVATION:
Jackson - @RequestBody do ControllerAnime
    - realiza o mapeado dos atributos
    - Se encontrar o JSON como o mesmo nome dos atributos declarados na classe
    irá fazer automáticamnete o mapeamento, caso contrário é necessário
    utilizar a anotaçõa:
 @JsonProperty("nome do atributo JSON") em cima do atributo da classe
 ex:
 @JsonProperty("name")
 private String nameAnime;
- Leitura: o atributo JSON é {name}, mas eu quero que voçê mapei para  dentro de nameAnime.

    GenerationType.AUTO:
        O provedor JPA (Hibernate, EclipseLink, etc.) escolhe a
        estratégia apropriada automaticamente com base no banco de dados
        utilizado. É uma escolha genérica e a estratégia real utilizada
        pode variar entre IDENTITY, SEQUENCE ou TABLE, dependendo do suporte
        do banco de dados subjacente.

    GenerationType.IDENTITY:
        Usa colunas de autoincremento do banco de dados para gerar valores
        de identificação. Comumente usado com bancos de dados como MySQL,
        PostgreSQL e SQL Server que suportam autoincremento. Cada nova entidade
        inserida recebe um valor de ID gerado automaticamente pelo banco de dados.

    GenerationType.SEQUENCE:
        Usa sequências de banco de dados para gerar valores de identificação.
        Geralmente usado com bancos de dados que suportam sequências, como Oracle
        e PostgreSQL. Requer a definição de uma sequência no banco de dados e
        permite a personalização do incremento de valores.

    GenerationType.TABLE:
        Usa uma tabela no banco de dados para gerar valores de identificação.
        Geralmente usado quando as outras estratégias não são adequadas. Essa estratégia
        cria e mantém uma tabela separada para armazenar os valores de ID gerados.
        É mais lenta e menos eficiente que IDENTITY ou SEQUENCE, mas pode ser usada
        em situações onde essas outras estratégias não são suportadas ou desejadas.


 */