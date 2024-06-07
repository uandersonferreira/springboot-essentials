package br.com.uanderson.springboot.util;

import br.com.uanderson.springboot.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    // Método estático que cria um objeto AnimePutRequestBody
    public static AnimePutRequestBody createAnimePutRequestBody() {
        // Retorna um novo objeto AnimePutRequestBody usando o padrão builder
        return AnimePutRequestBody.builder()
                .id(AnimeCreator.createValidUpdateAnime().getId())
                .name(AnimeCreator.createValidUpdateAnime().getName())
                .build();
        // Define o id e nome do AnimePutRequestBody(DTO) como o nome do anime que será atualizado,
        // obtido do AnimeCreator E Constrói e retorna o objeto AnimePutRequestBody
    }

}//class
