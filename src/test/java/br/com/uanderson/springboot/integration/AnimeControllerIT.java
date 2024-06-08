package br.com.uanderson.springboot.integration;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.repository.AnimeRepository;
import br.com.uanderson.springboot.requests.AnimePostRequestBody;
import br.com.uanderson.springboot.util.AnimeCreator;
import br.com.uanderson.springboot.util.AnimePostRequestBodyCreator;
import br.com.uanderson.springboot.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

/**
 * Simulando um Deploy em Produção: O teste está simulando um ambiente de produção
 * completo, o que significa que ele testa a aplicação em condições muito semelhantes
 * às que ela enfrentaria em produção.
 * <p>
 * Porta Aleatória: Usar uma porta aleatória evita colisões com outras instâncias da
 * aplicação que possam estar em execução.
 * <p>
 * Banco de Dados em Memória: O banco de dados usado para os testes é um banco de dados
 * em memória, o que é importante para isolar os testes do banco de dados real e garantir
 * que os testes sejam independentes e repetíveis.
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase // Ativa as configurações para usar um banco de dados em memória durante os testes
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)//faz com que o contexto da applicação seja recriado antes da execução do próximo teste (drop na base de dados)
class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate; // Template para enviar requisições HTTP nos testes

    @LocalServerPort
//forma de pegar a porta aleatória gerada durante a execução dos testes @Value("${local.server.port}")
    private int port; // Porta aleatória usada pelo servidor embutido durante os testes

    @Autowired
    private AnimeRepository animeRepository; // Repositório de animes usado para interagir com o banco de dados

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        // Salva um anime de teste no repositório
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        // Obtém o nome esperado do anime salvo
        String expectedName = savedAnime.getName();

        // Faz uma requisição GET para o endpoint "/animes" e espera uma resposta do tipo PageableResponse<Anime>
        PageableResponse<Anime> animePage = testRestTemplate.exchange(
                "/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }
        ).getBody();

        // Verifica se a resposta da página não é vazia
        Assertions.assertThat(animePage).isNotEmpty();
        // Converte a página em uma lista e verifica se não está vazia e contém exatamente um elemento
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("List all returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        // Salva um anime de teste no banco de dados
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        // Obtém o nome esperado do anime salvo
        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        // Faz uma requisição GET para "/animes/all" e obtém a lista de animes

        Assertions.assertThat(animes)
                .isNotNull() // Verifica se a lista não é nula
                .isNotEmpty() // Verifica se a lista não está vazia
                .hasSize(1); // Verifica se o tamanho da lista é 1, como esperado

        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        // Salva um anime de teste no banco de dados
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        // Obtém o ID esperado do anime salvo
        Long expectedId = savedAnime.getId();

        // Faz uma requisição GET para "/animes/{id}" e obtém o anime correspondente
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        // Verifica se o anime retornado não é nulo
        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull() // Verifica se o ID do anime não é nulo
                .isEqualTo(expectedId); // Verifica se o ID do anime é igual ao ID esperado
    }

    @Test
    @DisplayName("FindByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        // Salva um anime de teste no banco de dados
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        // Obtém o nome esperado do anime salvo
        String expectedName = savedAnime.getName();

        String url = String.format("/animes/find?name=%s", expectedName);
        List<Anime> animes = testRestTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        // Faz uma requisição GET para "/animes/find?name={expectedName}" e obtém a lista de animes correspondentes

        Assertions.assertThat(animes)
                .isNotNull() // Verifica se a lista não é nula
                .isNotEmpty() // Verifica se a lista não está vazia
                .hasSize(1); // Verifica se o tamanho da lista é 1, como esperado

        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(expectedName); // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {

        List<Anime> animes = testRestTemplate.exchange(
                "/animes/find?name=dbz",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();
        // Faz uma requisição GET para "/animes/find?name=dbz" onde "dbz" não existe no banco de dados

        Assertions.assertThat(animes)
                .isNotNull() // Verifica se a lista não é nula
                // A lista não pode ser nula, pois a implementação do Spring sempre retorna uma referência de lista, mesmo que vazia.
                .isEmpty(); // Verifica se a lista está vazia
        // Verificamos se a lista está vazia em vez de nula para garantir que a API retornou corretamente um resultado vazio.
    }


    @Test
    @DisplayName("Save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        // Cria um objeto AnimePostRequestBody para o teste
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        // Faz uma requisição POST para "/animes" para salvar o novo anime
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity(
                "/animes",
                animePostRequestBody,
                Anime.class
        );

        // Verifica se a resposta não é nula
        Assertions.assertThat(animeResponseEntity).isNotNull();

        // Verifica se o status da resposta é 201 CREATED
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Verifica se o corpo da resposta não é nulo
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();

        // Verifica se o ID do anime retornado não é nulo
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

        // Verifica se o nome do anime retornado não é nulo
        Assertions.assertThat(animeResponseEntity.getBody().getName()).isNotNull();
    }

    @Test
    @DisplayName("Replace updates return anime when successful")
    void replace_ReturnsAnime_WhenSuccessful() {
        // Salva um anime de teste no banco de dados
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        // Altera o nome do anime salvo
        savedAnime.setName("new name");

        // Faz uma requisição PUT para "/animes" para atualizar o anime
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/animes",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class);

        // Verifica se a resposta não é nula
        Assertions.assertThat(animeResponseEntity).isNotNull();

        // Verifica se o status da resposta é 204 NO CONTENT
        Assertions.assertThat(animeResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DeleteById removes return anime when successful")
    void deleteById_RemoveAnime_WhenSuccessful() {
        // Salva um anime de teste no banco de dados
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        // Faz uma requisição DELETE para "/animes/{id}" para deletar o anime
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId());

        // Verifica se a resposta não é nula
        Assertions.assertThat(animeResponseEntity).isNotNull();

        // Verifica se o status da resposta é 204 NO CONTENT
        Assertions.assertThat(animeResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }


}//class

/*
 * Teste de integração inicia totalmente o servidor da aplicação, simulando um
 * deploy em produção. Neste caso, toda vez que o servidor for iniciado, ele usará
 * uma porta aleatória diferente, garantindo que os testes não colidam com outras
 * instâncias do servidor que possam estar em execução.
 *
 * As configurações do banco de dados são ajustadas para usar um banco de dados em
 * memória, isolando os testes do banco de dados de produção e permitindo testes
 * independentes e repetíveis.
 *
 * @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 * Configura o ambiente de teste para usar uma porta aleatória, garantindo que a
 * aplicação seja iniciada em um servidor web embutido durante os testes.
 *
 *   DICA: SEMPRE UM TESTE FALHAR, MAS A IMPLEMENTAÇÃO ESTIVER CORRETA
 *  OLHE SE NÃO ESQUECEU DE DECLARAR UM CONSTRUTOR, GETTERS E SETTERS,
 *   OU SE NÃO ESQUECEU DE COLOCAR ALGUMA ANOTAÇÃO DO LOMBOK:
 *  @Data
 *   @Builder(OPCIONAL)
 *  @AllArgsConstructor
 *   @NoArgsConstructor (O erro pode está aqui, pois esquecemos de definir a anotação na class)
 *
 *   DEBUG: PARA VER O QUE ESTÁ SENDO GERADO APÓS A COMPILAÇÃO DA CLASS
 *   TARGET > CLASSES > PACKAGE > A-CLASS-EM-QUESTÃO
 *
 *   * @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
 * - Indica que o contexto da aplicação deve ser recriado antes da execução de cada método de teste.
 * - Necessário para simular o "drop" na base de dados, restaurando o contexto original da aplicação antes de cada teste.
 *
 * A anotação @DirtiesContext é fornecida pelo Spring Framework e indica ao Spring que o contexto do aplicativo
 * deve ser marcado como "sujo". Isso significa que o contexto do aplicativo foi modificado durante a execução de um teste
 * e precisa ser recriado antes da execução do próximo teste.
 *
 *
 *
 */
