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

     */