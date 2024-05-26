package br.com.uanderson.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Anime {
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

 */