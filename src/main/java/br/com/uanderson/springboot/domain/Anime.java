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

 */