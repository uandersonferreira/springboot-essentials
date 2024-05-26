package br.com.uanderson.springboot.domain;

public class Anime {
    private String name;

    public Anime(String name) {
        this.name = name;
    }

    public Anime() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
/*
Erros de 'serializer', 'BeanSerializer' , 'Serialização':
normalmente é por causa dos getters ou setters, pois o Jackson que mapeia os
objetos irá tentar criar um novo objeto utilizando eles,
mas quando não se têm ele não consegue realizar a criação de novos objetos.

 */