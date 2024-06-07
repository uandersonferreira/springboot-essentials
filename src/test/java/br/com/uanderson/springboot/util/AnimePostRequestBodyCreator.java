package br.com.uanderson.springboot.util;

import br.com.uanderson.springboot.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
    // Método estático que cria um objeto AnimePostRequestBody
    public static AnimePostRequestBody createAnimePostRequestBody() {
        // Retorna um novo objeto AnimePostRequestBody usando o padrão builder
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
        // Define o nome do AnimePostRequestBody(DTO) como o nome do anime a ser salvo,
        // obtido do AnimeCreator E Constrói e retorna o objeto AnimePostRequestBody
    }

}//class
