package br.com.uanderson.springboot.service;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.exception.BadRequestException;
import br.com.uanderson.springboot.mapper.AnimeMapper;
import br.com.uanderson.springboot.repository.AnimeRepository;
import br.com.uanderson.springboot.requests.AnimePostRequestBody;
import br.com.uanderson.springboot.requests.AnimePutRequestBody;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    //@Service representa uma Class que é reponsável pela implementação da REGRA DE NEGÓCIO da aplicação.

    private final AnimeRepository animeRepository;

    public List<Anime> listAllNoPageable() {
        return animeRepository.findAll();
    }

    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
        /*
        Retornar os objetos contidos em Page<Anime>, com opçoes de paginação habilitadas
        ex:
            http://localhost:8080/animes?size=5&page=1
        - size -> quantidade de elementos por página
        - page -> página em si acessada, lembrando que começa em 0 pois é uma lista no fim das contas
        Dica validar as RequestParam  (controllers e services ) caso optar por receber-las como parametro:
             - @Positive @Max(30) int size, @PositiveOrZero int page
             - Aceitar somente números positivos ou positivo + zero
             - O valor máximo permitido é 30
         */
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found with id " + id));
        /*
            Quando fazemos um request para endpoint passando um ID é não encontramos:
             - Muitos retornam 404 - Not Found, mas não se sabe se foi o id que não existe ou
                se foi a url que está errada, não se têm muita informação.
             - Por isso, ao se fazer uma busca por id precisa saber o id antes mesmo de executar a requisição
              então é por isso que retornamos Bad Request - 400 (requisição errada)

          OBS: Mais conversão/padrão do que regra, fica a critério do dev!!

         */
    }

    @Transactional //Habilita o princípio da atomicidade(rollback(): que Cancela uma transação se ocorre erros)
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);
        return animeRepository.save(anime);
        /*
        Uma transação garante que todo o processo deve ser executado com êxito
        seguindo o princípio da atomicidade, que é tudo ou nada.

        @Transactional: Garante que, se ocorrer um erro durante a execução de uma
        transação, a mesma será cancelada completamente, não persistindo no banco de dados.

        Recomenda-se sempre usar a anotação @Transactional quando temos métodos que fazem alguma persistência
        no banco de dados, a fim de garantir o princípio da atomicidade.

        Principais métodos de controle de transações:
        - begin(): Inicia uma transação;
        - commit(): Finaliza uma transação;
        - rollback(): Cancela uma transação.

        OBSERVAÇÕES:
        - Exceções não checadas (unchecked exceptions): As exceções não checadas, ou seja, as subclasses
          de RuntimeException ou Error, geralmente fazem com que a transação seja marcada para
          rollback (desfazer). Isso significa que todas as operações realizadas dentro da transação são
          desfeitas e as alterações no banco de dados são revertidas.

        - Exceções checadas (checked exceptions): As exceções checadas, ou seja, as subclasses de Exception,
          normalmente não são consideradas pelo mecanismo de transação do Spring. Isso significa que, se uma
          exceção checada for lançada dentro de um método anotado com @Transactional, a transação não será
          marcada para rollback automaticamente. A exceção será propagada para o chamador do método e cabe a
          ele decidir como lidar com a exceção.

        No entanto, podemos configurar o comportamento de captura das exceções checadas usando a
        anotação @Transactional com a propriedade 'rollbackFor' informando a instância da classe Exception:
          - @Transactional(rollbackFor = Exception.class): Anotação para captar exceções checadas.

Outras propriedades importantes de @Transactional:
        - propagation: Define o comportamento da transação em relação a transações existentes.
          Exemplos:
          - REQUIRED: Usa a transação atual se existir, ou cria uma nova se não existir.
          - REQUIRES_NEW: Sempre cria uma nova transação, suspendendo a existente se necessário.
          - MANDATORY: Usa a transação atual se existir, ou lança uma exceção se não existir.

        - isolation: Define o nível de isolamento da transação, controlando a visibilidade das alterações feitas por transações concorrentes.
          Exemplos:
          - READ_COMMITTED: Permite leitura de dados que foram comprometidos por outras transações.
          - READ_UNCOMMITTED: Permite leitura de dados não comprometidos, o que pode resultar em leituras sujas.
          - REPEATABLE_READ: Garante que, se uma linha for lida várias vezes na mesma transação, o valor lido será sempre o mesmo.
          - SERIALIZABLE: O nível mais alto de isolamento, onde as transações são completamente isoladas umas das outras, mas pode impactar o desempenho.

        - timeout: Define um tempo limite para a transação, após o qual ela será automaticamente revertida.

        - readOnly: Indica se a transação deve ser apenas de leitura. Isso pode ajudar no desempenho quando não há operações de escrita envolvidas.

        - noRollbackFor: Especifica quais exceções não devem causar o rollback da transação. Por exemplo, @Transactional(noRollbackFor = SpecificException.class) significa que a transação não será revertida se uma SpecificException for lançada.

        - rollbackFor: Especifica quais exceções devem causar o rollback da transação, incluindo exceções verificadas. Por exemplo, @Transactional(rollbackFor = Exception.class) significa que a transação será revertida se uma exceção do tipo Exception for lançada.

        - transactionManager: Define o gerenciador de transações específico a ser usado. Isso é útil em configurações com múltiplos gerenciadores de transações.

        @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            readOnly = false, Permitir operações de escrita
            rollbackFor = Exception.class, //Fazer rollback em exceções verificadas
            noRollbackFor = SpecificException.class //Não fazer rollback para SpecificException
         )
        public void performOperations() {
            // Código que realiza operações de banco de dados
        }

    ex SQL:
        BEGIN TRANSACTION ; --CONTEXTO TRANSAÇÕES
        DELETE FROM anime;
        --EXECUTA MAIS UM MONTE DE TAREFAS
        --OCORREU UM ERRO NO PROCESSO

        ROLLBACK ; --DESFAZ TODAS AS ALTERAÇÕES

        COMMIT ; --NÃO OCORREU ERRO VALIDA DE FATO AS OPERAÇÕES

        SELECT * FROM anime;

    */


    }

    public void delete(Long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
        /*
            Irá buscar o anime pelo id e caso não encontrar irá lançar a bad request
            definida no findById.
            Caso contrário irá remover o anime corretamente.
         */
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);

        /*
        lembrar-do encadeamento das chamadas dos method.
            1° - Pesquisar o anime pelo id, se não existir lança bad request.
            2° - Para ter certeza que estamos atualziado um anime existente informamos o
                id do anime ecnontrado na base de dados.
            3° - Agora a gente passa os dados do anime dto para o anime que será atualziado
                na base de dados.
            4° - chama o method save, que irá atualizar o objeto já existente, ou seja seu id não é
                mais null. save-assume duas funções em razão de ID's null (salva) ou não (atualiza).

        EXTRA: Existe outras forma de passar os dados do DTO para o objeto que irá ser salvo/atualziado
            - recurso:  BeanUtils.copyProperties("Obj-que-irá-receber", "obj-que irá-passar")
            - https://www.baeldung.com/apache-commons-beanutils
            - BeanUtils.copyProperties(anime, animePutRequestBody); - Cópia somente as variaveis com o mesmo nome!!

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