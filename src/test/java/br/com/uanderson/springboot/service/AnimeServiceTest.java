package br.com.uanderson.springboot.service;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.exception.BadRequestException;
import br.com.uanderson.springboot.repository.AnimeRepository;
import br.com.uanderson.springboot.util.AnimeCreator;
import br.com.uanderson.springboot.util.AnimePostRequestBodyCreator;
import br.com.uanderson.springboot.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)//Usado para integrar o JUnit 5 com o Spring, permitindo que o contexto do Spring seja carregado e gerenciado durante os testes.
class AnimeServiceTest {
    @InjectMocks//Utiliza-sse quando queremos testar a classe em si.
    //Em resumo, cria uma instância de AnimeController e injeta os mocks criados com @Mock nesta instância.
    private AnimeService animeService;
    @Mock // Utiliza-se para todas as injenções de dependências(DI) que estão contidas na classe que queremos testar.
    //Ou seja, Cria um mock da dependência AnimeService que será injetado na classe AnimeController.
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
        // Executa este método antes de cada método de teste (@Test), configurando os mocks e preparando o ambiente de teste.
    void setUp() {
        /*
        OBSERVATION: Sempre que quisermos testar um método do controller, devemos primeiro definir
        um comportamento para poder isolar o código que está sendo testado de suas dependências externas.
        Isso é conhecido como "mocking" (simulação) de objetos.
        */

        // Cria uma página contendo um anime válido para ser usada nos testes
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        // Configura o mock animeRepositoryMock para retornar animePage quando o método listAll for chamado,
        // independentemente do argumento passado.
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage); // Então, retorna o objeto animePage.

        // Configura o mock animeRepositoryMock para retornar uma lista contendo um anime válido quando o método listAllNoPageable for chamado.
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime())); // Então, retorna uma lista com um anime válido.

        // Configura o mock animeRepositoryMock para retornar um anime válido quando o método findByIdOrThrowBadRequestException for chamado
        // com qualquer argumento do tipo Long.
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime())); // Então, retorna um anime válido.

        // Configura o mock animeRepositoryMock para retornar uma lista contendo um anime válido quando o método findByName for chamado
        // com qualquer argumento do tipo String.
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime())); // Então, retorna uma lista com um anime válido.

        // Configura o mock animeRepositoryMock para retornar um anime válido quando o método save for chamado
        // com qualquer argumento do tipo AnimePostRequestBody.
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime()); // Então, retorna um anime válido.

        // Configura o mock animeServiceMock para não fazer nada quando o método delete (void) for chamado
        BDDMockito.doNothing().when(animeRepositoryMock)//doNothing-não faça nada/Por estar chamando um method sem retorno
                .delete(ArgumentMatchers.any(Anime.class));
    }


    @Test
    @DisplayName("ListAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        // Cria um anime válido e obtém seu nome esperado
        String expectedName = AnimeCreator.createValidAnime().getName();

        // Chama o serviço para listar todos os animes de forma paginada
        Page<Anime> animePage = animeService.listAllPageable(PageRequest.of(1, 1));

        // Verifica se a página retornada não é nula
        Assertions.assertThat(animePage).isNotNull();
        // Verifica se a lista dentro da página não está vazia e contém um elemento
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNoPageable returns list of anime when successful")
    void listAllNoPageable_ReturnsListOfAnime_WhenSuccessful(){
        // Cria um anime válido e obtém seu nome esperado
        String expectedName = AnimeCreator.createValidAnime().getName();

        // Chama o serviço para listar todos os animes sem paginação
        List<Anime> animeList = animeService.listAllNoPageable();

        // Verifica se a lista retornada não é nula, não está vazia e contém um elemento
        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful(){
        // Cria um anime válido e obtém seu ID esperado
        Long expectedId = AnimeCreator.createValidAnime().getId();

        // Chama o serviço para encontrar um anime pelo ID
        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        // Verifica se o anime retornado não é nulo
        Assertions.assertThat(anime).isNotNull();

        // Verifica se o ID do anime retornado não é nulo e é igual ao ID esperado
        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test//Cobrindo um cenário de teste, não tão visível
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when Anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound(){
        // Configura o mock para retornar um Optional vazio ao procurar por um ID
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        // Verifica se o método lança uma BadRequestException(Exceção BAD_REQUEST customizada)
        // quando o anime não é encontrado e se contém a message de erro definida.
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1L))
                .withMessageContaining("Anime not found");

    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        // Cria um anime válido e obtém seu nome esperado
        String expectedName = AnimeCreator.createValidAnime().getName();

        // Chama o serviço para encontrar animes pelo nome
        List<Anime> animeList = animeService.findByName("Overlod");

        // Verifica se a lista retornada não é nula, não está vazia e contém um elemento
        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
        // Configura o mock para retornar uma lista vazia ao procurar por um nome
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        // Chama o serviço para encontrar animes pelo nome
        List<Anime> animeList = animeService.findByName("Overlod");

        // Verifica se a lista retornada não é nula e está vazia
        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns Anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        // Chama o serviço para salvar um anime
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());

        // Verifica se o anime retornado não é nulo e é igual ao anime válido criado
        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("replace updates AnimePutRequestBody when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        // Verifica se o método replace do serviço não lança nenhuma exceção
        Assertions.assertThatCode(() -> animeService
                        .replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        // Verifica se o método delete do serviço não lança nenhuma exceção ao deletar por ID
        Assertions.assertThatCode(() -> animeService.deleteById(1L))
                .doesNotThrowAnyException();
    }

}