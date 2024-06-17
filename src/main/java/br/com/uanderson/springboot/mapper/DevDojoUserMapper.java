package br.com.uanderson.springboot.mapper;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.domain.DevDojoUserDetails;
import br.com.uanderson.springboot.requests.AnimePostRequestBody;
import br.com.uanderson.springboot.requests.AnimePutRequestBody;
import br.com.uanderson.springboot.requests.DevDojoUserPostRequest;
import br.com.uanderson.springboot.requests.DevDojoUserPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")//indicar ao MapStruct que ela é uma class de mapeamento
public abstract class DevDojoUserMapper {
    //Mapeamento dos DTO's para a entidade'
    public static final DevDojoUserMapper INSTANCE = Mappers.getMapper(DevDojoUserMapper.class);
    public abstract DevDojoUserDetails toDevDojoUser(DevDojoUserPostRequest devDojoUserPostRequest);
    public abstract DevDojoUserDetails toDevDojoUser(DevDojoUserPutRequest devDojoUserPutRequest);

}
