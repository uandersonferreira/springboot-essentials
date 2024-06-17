package br.com.uanderson.springboot.util;

import br.com.uanderson.springboot.domain.Anime;

public class AnimeCreator {
    /*
     Aqui centralizamos a criação dos tipos de Objetos (animes) que precisamos para realizar
     os teste de unidade (do controller), ou seja os valores que seriam usados/retornados
     caso tivessemos persistindo em um banco de dados.
     */

    public static Anime createAnimeToBeSaved() {
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
        //Aqui sabemos que sera um anime sem ID, pois iremos enviar para salvar
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1L)
                .name("Hajime no Ippo")
                .build();
        //Aqui sabemos que o anime foi salvo e, é válido pois gerou o ID
    }

    public static Anime createValidUpdateAnime() {
        return Anime.builder()
                .id(1L)
                .name("Hajime no Ippo 2")
                .build();
        //Aqui sabemos que atualizamos com sucesso, pois é o mesmo ID do anime
        //que salvamos.
    }

}//class
