package br.com.uanderson.springboot.mapper;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.requests.AnimePostRequestBody;
import br.com.uanderson.springboot.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")//indicar ao MapStruct que ela é uma class de mapeamento
public abstract class AnimeMapper {
    //Mapeamento dos DTO's para a entidade'
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);

}
    /*
    MapStruct- Necessita que configuremos interfaces ou clases de mapeamento que descrevem
              como as propriedades dos objetos de origem e destino devem ser mapeadas.

    @Mapping(source = "nameUser", target = "nome")
    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    - Caso tivemos variaveis com nomes diferentes:
    quando enviamos para salvar e as declaradas na nossa class, o
    mapstruct por default preenche como 'null', para corrigir isso decemos
    usar a anotação: @Mapping(source = "nameUser", target = "name")
    paar que ele entenda e consiga fazer a passagem correta. Igual ao Jackson.
    - se tiver uma regra muito especifica têm que fazer umas coisas, mais avançadas
        olhar o vídeo abaixo paar lembrar.
    DICA DE VÍDEO: https://youtu.be/80aMT02qAbg?si=XGrqwWMRo0RZ8xAx
    DOCUMENTATION VERSION NOW: https://mapstruct.org/documentation/1.5/reference/html/
     */