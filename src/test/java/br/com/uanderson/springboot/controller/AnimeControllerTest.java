package br.com.uanderson.springboot.controller;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.service.AnimeService;
import br.com.uanderson.springboot.util.AnimeCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)//Usado para integrar o JUnit 5 com o Spring, permitindo que o contexto do Spring seja carregado e gerenciado durante os testes.
class AnimeControllerTest {
    @InjectMocks//Utiliza-se quando queremos testar a classe em si.
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
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        /*
        OBSERVATION: Sempre que quisermos testar um metódo do controller devemos primeiro definir
        um comportamento para poder isolar o código que está sendo testado de suas dependências externas.
        Isso é conhecido como "mocking" (simulação) de objetos.
         */
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))// Configura o mock animeServiceMock para
                // retornar animePage quando o método listAll for chamado, independentemente do argumento passado.
                .thenReturn(animePage);// Então, retorna o objeto animePage.

    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
        // Verifica se o método listAll retorna uma lista de animes dentro de um objeto Page com sucesso.
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();//recuperando o name do animeValid criado para testes

        Page<Anime> animePage = animeController.listAll(null).getBody();//Recuperando todos os Page<Anime> do
        // "banco" e como só deve ter 1, estamos pegando o seu conteúdo no caso anime.

        Assertions.assertThat(animePage).isNotEmpty();//verifica se o objeto Page<Anime> não é vazio

        Assertions.assertThat(animePage.toList())//pega a lista de animePage
                .isNotEmpty()//verifica se não é vazia
                .hasSize(1);//é se seu tamanho é realmente 1 como definimos anteriomente no 'setUp'

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
        //verifica se o nome do 1° obj da lista é igual ao nome esperado
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