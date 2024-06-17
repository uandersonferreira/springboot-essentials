package br.com.uanderson.springboot.controller;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.requests.AnimePostRequestBody;
import br.com.uanderson.springboot.requests.AnimePutRequestBody;
import br.com.uanderson.springboot.service.AnimeService;
import br.com.uanderson.springboot.util.AnimeCreator;
import br.com.uanderson.springboot.util.AnimePostRequestBodyCreator;
import br.com.uanderson.springboot.util.AnimePutRequestBodyCreator;
import br.com.uanderson.springboot.util.DateUtil;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)//Usado para integrar o JUnit 5 com o Spring, permitindo que o contexto do Spring seja carregado e gerenciado durante os testes.
class AnimeControllerTest {
    @InjectMocks//Utiliza-sse quando queremos testar a classe em si.
    //Em resumo, cria uma instância de AnimeController e injeta os mocks criados com @Mock nesta instância.
    private AnimeController animeController;
    @Mock // Utiliza-se para todas as injenções de dependências(DI) que estão contidas na classe que queremos testar.
    //Ou seja, Cria um mock da dependência AnimeService que será injetado na classe AnimeController.
    private AnimeService animeServiceMock;
    @Mock
    // Cria um mock da dependência DateUtil que será injetado na classe AnimeController.
    private DateUtil dateUtilMock;

    @BeforeEach
        // Executa este método antes de cada método de teste (@Test), configurando os mocks e preparando o ambiente de teste.
    void setUp() {
        // Cria uma página contendo um anime válido para ser usada nos testes
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

    /*
    OBSERVATION: Sempre que quisermos testar um método do controller, devemos primeiro definir
    um comportamento para poder isolar o código que está sendo testado de suas dependências externas.
    Isso é conhecido como "mocking" (simulação) de objetos.
    */

        // Configura o mock animeServiceMock para retornar animePage quando o método listAll for chamado,
        // independentemente do argumento passado.
        BDDMockito.when(animeServiceMock.listAllPageable(ArgumentMatchers.any()))
                .thenReturn(animePage); // Então, retorna o objeto animePage.

        // Configura o mock animeServiceMock para retornar uma lista contendo um anime válido quando o método listAllNoPageable for chamado.
        BDDMockito.when(animeServiceMock.listAllNoPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime())); // Então, retorna uma lista com um anime válido.

        // Configura o mock animeServiceMock para retornar um anime válido quando o método findByIdOrThrowBadRequestException for chamado
        // com qualquer argumento do tipo Long.
        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime()); // Então, retorna um anime válido.

        // Configura o mock animeServiceMock para retornar uma lista contendo um anime válido quando o método findByName for chamado
        // com qualquer argumento do tipo String.
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime())); // Então, retorna uma lista com um anime válido.

        // Configura o mock animeServiceMock para retornar um anime válido quando o método save for chamado
        // com qualquer argumento do tipo AnimePostRequestBody.
        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime()); // Então, retorna um anime válido.

        // Configura o mock animeServiceMock para não fazer nada quando o método replace (void) for chamado
        // com qualquer argumento do tipo AnimePutRequestBody.
        //Obs:configuração do mock para métodos void
        BDDMockito.doNothing().when(animeServiceMock)
                .replace(ArgumentMatchers.any(AnimePutRequestBody.class)); // Então, não faz nada, pois é void.

        // Configura o mock animeServiceMock para não fazer nada quando o método delete (void) for chamado
        BDDMockito.doNothing().when(animeServiceMock)//doNothing-não faça nada/Por estar chamando um method sem retorno
                .deleteById(ArgumentMatchers.anyLong());

    }


    @Test
    @DisplayName("List returns list of anime inside page object when successful")
        // Verifica se o método listAll retorna uma lista de animes dentro de um objeto Page com sucesso.
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();//recuperando o name do animeValid criado para testes

        Page<Anime> animePage = animeController.listAllPageable(null).getBody();//Recuperando todos os Page<Anime> do
        // "banco" e como só deve ter 1, estamos pegando o seu conteúdo no caso anime.

        Assertions.assertThat(animePage).isNotEmpty();//verifica se o objeto Page<Anime> não é vazio

        Assertions.assertThat(animePage.toList())//pega a lista de animePage
                .isNotEmpty()//verifica se não é vazia
                .hasSize(1);//é se seu tamanho é realmente 1 como definimos anteriomente no 'setUp'

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
        //verifica se o nome do 1° obj da lista é igual ao nome esperado
    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        // Recupera o nome do anime criado para o teste.

        List<Anime> animes = animeController.listAllNoPageable().getBody();
        // Chama o método listAllNoPageable do controlador e obtém o corpo da resposta, que é uma lista de animes.

        Assertions.assertThat(animes)
                .isNotNull() // Verifica se a lista de animes não é nula.
                .isNotEmpty() // Verifica se a lista de animes não está vazia.
                .hasSize(1); // Verifica se a lista contém exatamente 1 anime.

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado.
    }


    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        Long expectedId = AnimeCreator.createValidAnime().getId();
        // Recupera o ID do anime criado para o teste.

        Anime anime = animeController.findById(1L).getBody();
        // Chama o método findById do controlador com o ID 1 e obtém o corpo da resposta, que é um anime.

        Assertions.assertThat(anime).isNotNull();
        // Verifica se o objeto anime não é nulo.

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
        // Verifica se o ID do anime não é nulo e é igual ao ID esperado.
    }


    @Test //Cobrindo um cenário de teste, não tão visível
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        // Sobrescreve o comportamento definido no @BeforeEach para o método findByName(), configurando-o para retornar uma lista vazia.
        // Comportamentos específicos definidos dentro de um teste têm preferência sobre configurações globais( @BeforeEach - setUp() ).
        // Isso é útil para cobrir cenários de erro específicos.
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                // Configura o mock animeServiceMock para retornar uma lista vazia quando findByName for chamado com qualquer string.
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeController.findByName("anime").getBody();
        // Chama o método findByName do controlador com a string "anime" e obtém o corpo da resposta, que é uma lista de animes.

        Assertions.assertThat(animeList)
                .isNotNull() // Verifica se a lista de animes não é nula (mesmo que esteja vazia, deve ser uma lista inicializada).
                .isEmpty(); // Verifica se a lista de animes está realmente vazia.
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        Anime anime = animeController.save(AnimePostRequestBodyCreator
                .createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Replace updates return anime when successful")
    void replace_ReturnsAnime_WhenSuccessful() {
        AnimePutRequestBody requestBody = AnimePutRequestBodyCreator.createAnimePutRequestBody();

        // 1ª forma de testar um método sem retorno (void) - verificando se o código não lança exceções.
    /*
     O objetivo deste teste é garantir que a chamada ao método replace do animeController
     com os argumentos fornecidos não resulte no lançamento de exceções.
     Assertions.assertThatCode(codigo) - é usado para verificar se o código passado
     não lança nenhuma exceção durante sua execução.
    */
        // 1ª forma de testar um método sem retorno (void) - verificando se o código não lança exceções.
        Assertions.assertThatCode(() -> animeController.replace(requestBody))
                .doesNotThrowAnyException(); // Verifica se nenhuma exceção é lançada durante a execução.

        // 2ª forma de testar um método sem retorno (void) - verificando o ResponseEntity<?> retornado.
        ResponseEntity<Void> entity = animeController.replace(requestBody);

        Assertions.assertThat(entity).isNotNull(); // Verifica se a resposta não é nula.
        Assertions.assertThat(entity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT); // Verifica se o status code é NO_CONTENT, indicando sucesso.

    }

    @Test
    @DisplayName("DeleteById removes return anime when successful")
    void deleteById_RemoveAnime_WhenSuccessful() {
        Long animeId = 1L;

        // 1ª forma de testar um método sem retorno (void) - verificando se o código não lança exceções.
        Assertions.assertThatCode(() -> animeController.deleteById(animeId)).doesNotThrowAnyException(); // Verifica se nenhuma exceção é lançada durante a execução.

        // 2ª forma de testar um método sem retorno (void) - verificando o ResponseEntity<?> retornado.
        ResponseEntity<Void> entity = animeController.deleteById(animeId);

        Assertions.assertThat(entity).isNotNull(); // Verifica se a resposta não é nula.
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT); // Verifica se o status code é NO_CONTENT, indicando sucesso.
    }


}
/*
@ExtendWith(SpringExtension.class)
     - Integra o JUnit 5 com o Spring, permitindo o uso do contexto do Spring nos testes.
     - Informando que queremos herdar os comportamento do Junit para utilizar com Spring
     boot em um contexto mais isolado. Permitindo ao Junit inicializar o contexto do
     Spring antes de executar os testes, permitido assim: acesso
     aos beans do Spring diretamente em nossa classe de teste.


Porque não estamos usando o @SpringBootTest ?

@SpringBootTest - porque é usada no ecossistema do Spring para testar a aplicação como um todo,
                  em um contexto mais amplo. Sendo assim, necessita de dependências externas, como
                  banco de dados, serviços web, serviços REST e outros. Além disso, têm a questão do
                  Overhead de inicialização, que  inicia todo o contexto da aplicação Spring, incluindo
                  a inicialização do contêiner Spring IoC (Inversão de Controle) e a configuração de todos
                  os beans gerenciados, ou seja, necessita que a aplicação esteja rodando,
                  pois  tenta inicializar a aplicação  para realizar os testes. Portanto, sua utilização
                  e mais recomendada nos teste de integração.

@InjectMocks:
    - Utiliza-se quando queremos testar a classe em si.
    - Cria uma instância da classe e injeta os mocks criados com @Mock nela.

@Mock:
 - Utiliza-se para todas as classes que estão dentro da classe que
  queremos testar (Que de alguma forma está sendo necessário sua utilização).
 - Cria mocks das dependências que são injetadas na classe que estamos testando.


Ex:
  Queremos testar a class AnimeController e dentro dela estamos utilizando uma
  referência das classes:
     - DateUtil dateUtil;
     - AnimeService animeService;

E dentro do AnimeService estamos utilizando uma referência da class(Interface):
   - AnimeRepository animeRepository

then - então
when - quando
that - isso/esse
assertThat - afirmar isso
inside - dentro

contexto (given), a ação (when) e as asserções (then) - Segue o padrão Given-When-Then para estrutura de testes:
  - Given (contexto): Define o estado inicial ou as precondições.
  - When (ação): Executa a ação que está sendo testada.
  - Then (asserções): Verifica os resultados esperados.


*/