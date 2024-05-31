package br.com.uanderson.springboot.service;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.mapper.AnimeMapper;
import br.com.uanderson.springboot.repository.AnimeRepository;
import br.com.uanderson.springboot.requests.AnimePostRequestBody;
import br.com.uanderson.springboot.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    //@Service representa uma Class que é reponsável pela implementação da REGRA DE NEGÓCIO da aplicação.

    private final AnimeRepository animeRepository;

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }
    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found with id " + id));
        /*
            Quando fazemos um request para endpoint passando um ID é não encontramos:
             - Muitos retornam 404 - Not Found, mas não se sabe se foi o id que não existe ou
                se foi a url que está errada, não se têm muita informação.
             - Por isso, ao se fazer uma busca por id precisa saber o id antes mesmo de executar a requisição
              então é por isso que retornamos Bad Request - 400 (requisição errada)

          OBS: Mais conversão/padrão do que regra, fica a critério do dev!!

         */
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);
        return animeRepository.save(anime);
    }

    public void delete(Long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
        /*
            Irá buscar o anime pelo id e caso não encontrar irá lançar a bad request
            definida no findById.
            Caso contrário irá remover o anime corretamente.
         */
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);

        /*
        lembrar-do encadeamento das chamadas dos method.
            1° - Pesquisar o anime pelo id, se não existir lança bad request.
            2° - Para ter certeza que estamos atualziado um anime existente informamos o
                id do anime ecnontrado na base de dados.
            3° - Agora a gente passa os dados do anime dto para o anime que será atualziado
                na base de dados.
            4° - chama o method save, que irá atualizar o objeto já existente, ou seja seu id não é
                mais null. save-assume duas funções em razão de ID's null (salva) ou não (atualiza).

        EXTRA: Existe outras forma de passar os dados do DTO para o objeto que irá ser salvo/atualziado
            - recurso:  BeanUtils.copyProperties("Obj-que-irá-receber", "obj-que irá-passar")
            - https://www.baeldung.com/apache-commons-beanutils
            - BeanUtils.copyProperties(anime, animePutRequestBody); - Cópia somente as variaveis com o mesmo nome!!

        OBSERVATION:
        Quando realizamos um PUT(update/replace) estamos substituindo o ESTADO
        inteiro do objeto, de forma idempotente.

        O ESTADO são os valores atribuídos aos atributos de um objeto.
        Diferentemente do COMPORTAMENTO que são os métodos da classe,que como o próprio
        nome diz, é o comportamento do objeto.

         */

    }
}//class

/*

 */