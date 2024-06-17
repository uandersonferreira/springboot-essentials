package br.com.uanderson.springboot.repository;

import br.com.uanderson.springboot.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findByName(String name);

}
/*   List<Anime> listAll(); Não é mais preciso pois por default já temos
        alguns method crud implementados por causa da interface JpaRepository.
     Obs: ctrl + b na interface para ver os methods.

    OBS: o method List<Anime> findByName(String name);
     Utiliza um recurso do spring data chamado 'Query Methods', que permite a criação
     de consultas sql baseadas em palavras chaves, combinadas com os nomes dos
     atributos da class e operadores lógicos.

     link: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
*/