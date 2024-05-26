package br.com.uanderson.springboot.service;

import br.com.uanderson.springboot.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AnimeService {
    //@Service representa uma Class que é reponsável pela implementação
    // da REGRA DE NEGÓCIO da aplicação.

    //private final AnimeRepository animeRepository;
    private static List<Anime> animes;
    //Lista imutável, onde todas as instâncias da classe terão acesso
    // à mesma lista, independentemente de quantos objetos sejam criados

    static {
        //Bloco static é iniciado primeiro, por isso se cria um intância de arrayList e atribui a animes
        animes = new ArrayList<>(
                //Criar uma instância de arrayList para que seja possível
                // manipular a lista 'animes' que é imutável, senão gera um
                // 'UnsupportedOperationException', pois não aceita modificações
                List.of(
                        new Anime(1L, "Naruto"),
                        new Anime(2L, "Boruto"),
                        new Anime(3L, "Boku no Hero Academy")
                )
        );
    }

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

    public Anime save(Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(animes.size(), 1_000_000));
        animes.add(anime);
        return anime;
    }

    public void delete(Long id) {
        animes.remove(findById(id));
        /*
            Irá buscar o anime pelo id e caso não encontrar irá lançar a bad request
            definida no findById.
            Caso contrário irá remover o anime corretamente.
         */
    }

    public void replace(Anime anime) {
        delete(anime.getId());
        animes.add(anime);
        /*
        lembrar-do encadeamento das chamadas dos method.
            1° - Pesquisar o anime pelo id, se não existir lança bad request
            2° - Deletar caso exista
            3° - Adicionar o novo anime

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