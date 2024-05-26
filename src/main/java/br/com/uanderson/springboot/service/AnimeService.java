package br.com.uanderson.springboot.service;

import br.com.uanderson.springboot.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnimeService {
    //private final AnimeRepository animeRepository;
    private List<Anime> animes = List.of(
            new Anime(1L, "Naruto"),
            new Anime(2L, "Boruto"),
            new Anime(3L, "Boku no Hero Academy")
    );

    public List<Anime> listAll() {
        return animes;
    }

    public Anime findById(Long id) {
        return animes.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
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

}//class
