package br.com.uanderson.springboot.repository;

import br.com.uanderson.springboot.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
/*   List<Anime> listAll(); Não é mais preciso pois por default já temos
        alguns method crud implementados por causa da interface JpaRepository.
     Obs: ctrl + b na interface para ver os methods.
 */

}
