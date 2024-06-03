package br.com.uanderson.springboot.repository;

import br.com.uanderson.springboot.domain.Anime;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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
    @DisplayName("Save persists anime when Successful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        //está sendo utilizado a importação do pacotes: import org.assertj.core.api.Assertions;| não do Junit5 padrão
        Assertions.assertThat(animeSaved).isNotNull();//se é != null
        Assertions.assertThat(animeSaved.getId()).isNotNull();//se id != null
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
        // se o nome do objeto a ser salvo é o mesmo que definimos para ser criado

    }

    @Test
    @DisplayName("Save update anime when Successful")
    void save_UpdateAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        animeSaved.setName("Overlord");
        Anime animeUpdated = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdated).isNotNull();//se é != null
        Assertions.assertThat(animeUpdated.getId()).isNotNull();//se id != null
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());// se o nome do objeto Atualizado é o mesmo que pedimos para ser atualizado
    }

    @Test
    @DisplayName("Delete update anime when Successful")
    void delete_RemovesAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        /*
            Lógica para testar um method sem retorno (delete):
             - Buscamos o anime salvo pelo id através do method findById é esse anime
               será retornando em um Optional, pois têm a possibilidade de ser null/vazio.
               Então é isso que iremos testar, se o Optional é realmente vazio como deve ser
               já que deletamos o anime do id informado.
         */
        this.animeRepository.delete(animeSaved);//deletando o anime salvo
        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();//verifica se o anime é realmente vazio
    }

    @Test
    @DisplayName("Find by name returns list of anime when Successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        String name = animeSaved.getName();
        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes)
                .isNotEmpty() // se a lista não é vazia
                .contains(animeSaved);// e se o animeSaved está contido na lista de animes retornada.
    }

    /*
        DICA: Cria cenários de testes, que não estão explicitamente
        visíveis, ou seja seja, cenários/comportamento que deveria ser executado
        quando ocorrer erros, principalmente os que foram implementados no código.
     */
    @Test
    @DisplayName("Find by name returns empty list when no anime is found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
        //Já inicia o banco vazio, pois é testes unitários
        //OBS: empty/vazio é diferente de null/nulo
        List<Anime> animes = this.animeRepository.findByName("xaxaxa");
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty() {
        Anime anime = new Anime();

    /*
       1° - Aqui testamos somente se a Exception está sendo gerada, ao chamar o method
       save passando um anime com 'name' inválido.

       Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);

        2° - Aqui informamos que estamos esperando uma ConstraintViolationException,
        que é lançanda quando chamamos o method save passando um anime com 'name' inválido
        e que esperamos que contenha uma mensagem de erro, definida na nossa validação
        anteriomente, vale lembrar que a message deve está idêntica a definida nas validações dos atributos.
    */
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("The anime name cannot be empty");
    }

    private Anime createAnime() {
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

Empty (vazio): Em geral, "empty" refere-se a uma coleção, uma string ou um array que
              não contém elementos ou caracteres. O objeto existe, mas não contém
              elementos. Seu tamanho ou comprimento é zero.

String emptyString = "";
List<String> emptyList = new ArrayList<>();
int[] emptyArray = new int[0];

Null (nulo): "Null" é um valor especial em Java que representa a ausência de um objeto
             ou a falta de referência para um objeto. É diferente de um objeto vazio ou
              uma coleção vazia. O objeto não existe, a variável não referencia nenhuma
              instância de objeto.

String nullString = null;
List<String> nullList = null;
int[] nullArray = null;


Ex:
String emptyString = "";
String nullString = null;

if (emptyString.isEmpty()) {
    System.out.println("A string está vazia.");
}

if (nullString == null) {
    System.out.println("A string é nula.");
}

 */