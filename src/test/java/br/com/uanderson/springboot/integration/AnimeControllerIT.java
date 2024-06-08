package br.com.uanderson.springboot.integration;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.repository.AnimeRepository;
import br.com.uanderson.springboot.util.AnimeCreator;
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
import org.springframework.http.HttpMethod;

/**
 * Simulando um Deploy em Produção: O teste está simulando um ambiente de produção
 * completo, o que significa que ele testa a aplicação em condições muito semelhantes
 * às que ela enfrentaria em produção.
 *
 * Porta Aleatória: Usar uma porta aleatória evita colisões com outras instâncias da
 * aplicação que possam estar em execução.
 *
 * Banco de Dados em Memória: O banco de dados usado para os testes é um banco de dados
 * em memória, o que é importante para isolar os testes do banco de dados real e garantir
 * que os testes sejam independentes e repetíveis.
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase // Ativa as configurações para usar um banco de dados em memória durante os testes
class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate; // Template para enviar requisições HTTP nos testes

    @LocalServerPort//forma de pegar a porta aleatória gerada durante a execução dos testes @Value("${local.server.port}")
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
 */
