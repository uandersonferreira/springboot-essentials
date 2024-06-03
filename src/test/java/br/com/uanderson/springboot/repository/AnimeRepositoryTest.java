package br.com.uanderson.springboot.repository;

import br.com.uanderson.springboot.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest//Anotação focada em testes para os componentes do JPA
@DisplayName("Tests for Anime Repository")//é usada no JUnit para fornecer um nome descritivo aos testes ou às classes de teste.
class AnimeRepositoryTest {//sem public pois estamos exportando tudo do Junit

    @Autowired
    private AnimeRepository animeRepository;


    //Conversão de criação de nomes de Test (Recomendando pelo Devdojo)
    //NOME DO METHOD + O QUE PRECISA FAZER ? + QUANDO ISSO DEVE ACONTECER ? || separando por underscore.
    //Seguindo a sintaxe de nomes compostos do java.
    //EX: save_PersistAnime_WhenSuccessful
    @Test
    @DisplayName("Save creates anime when Successful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        //está sendo utilizado a importação do pacotes: import org.assertj.core.api.Assertions;| não do Junit5 padrão
        Assertions.assertThat(animeSaved).isNotNull();//se é != null
        Assertions.assertThat(animeSaved.getId()).isNotNull();//se id != null
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
        // se o nome do objeto a ser salvo é o mesmo que definimos para ser criado

    }

    private Anime createAnime(){
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }

}
/*
OBS: Não é necessário ter a aplicação em execução
     para rodar teste unitários,pois é diferente de test
     de integrção.

@DataJpaTest - A anotação @DataJpaTest no Spring Boot é usada para testar a camada
               de persistência de dados, especificamente a interação com o banco de
               dados usando o Spring Data JPA. Quando você usa @DataJpaTest em um teste,
               o Spring Boot configura automaticamente um ambiente de teste que inclui
               um banco de dados em memória e o suporte para transações.

 */