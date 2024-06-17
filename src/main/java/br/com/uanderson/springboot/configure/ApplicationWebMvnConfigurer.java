package br.com.uanderson.springboot.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration//bean de configuração global
public class ApplicationWebMvnConfigurer implements WebMvcConfigurer {
    @Override//Sobreescrevendo a Configuração padrão do spring a respeito da paginação!
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        var pageaHandler = new PageableHandlerMethodArgumentResolver();
        pageaHandler.setFallbackPageable(PageRequest.of(0, 5, Sort.by("id").descending()));
        resolvers.add(pageaHandler);
        /*
        Neste exemplo estamos sobreescrendo/definindo a página e quantidade de objetos
        mostrados na tela ao se requisitado todos os animes, ORDENANDO pelo id em ordem DECRESCENTE (maior para o menor).
        URL parametros( ?sort=id,desc) - http://localhost:8080/animes?size=5&pag=0&sort=id,desc
            - asc (ordem alfabetica | menor para o menor)
            - desc (ordem alfabetica inversa | maior para o menor)
        ESSAS  ALTERAÇÕES SÃO A NÍVEL DO BANCO DE DADOS, NÃO DA APLICAÇÃO.

         */
    }
}//class
/*

@Configuration – define uma classe como fonte de definições/configurações de beans e é uma das
anotações essenciais se você estiver usando a configuração baseada em Java.
Injenção de dependencia, flag que sinaliza que o spring têm que gerênciar as configurações.

-Sem a annotação as configurações não seram aplicadas, prevalecendo as padrões.

-> WebMvcConfigure
Sobreescrevendo a Configuração do spring a respeito da paginação!

        Todas as class que implementam a interface Pageable contém métodos para
        acessar e manipular as informações da página, tais como:

        - getPageNumber(): Retorna o número da página solicitada.
        - getPageSize(): Retorna o tamanho da página, ou seja, o número máximo de registros por página.
        - getOffset(): Retorna o deslocamento, ou seja, o índice inicial do primeiro registro da página.
        - getSort(): Retorna as opções de ordenação aplicadas à consulta.
        - hasPrevious(): Verifica se há uma página anterior disponível.
        - previousOrFirst(): Retorna a página anterior, se disponível, ou a primeira página.
        - next(): Retorna a próxima página.
        - hasNext(): Verifica se há uma próxima página disponível.
        - first(): Retorna a primeira Pageable. Isso é útil para redefinir a página de volta à primeira página,
         descartando qualquer estado de paginação anterior.
        - nextOrLast(): Retorna a próxima Pageable se disponível, caso contrário, retorna a última Pageable.
             Isso é útil quando você deseja obter a próxima página, mas também deseja garantir que, se a página
             seguinte não estiver disponível, você obtenha a última página.
        - withPage(int pageNumber): Retorna uma nova instância de Pageable com o número de página fornecido,
             mantendo o tamanho da página e as opções de ordenação. Isso é útil quando você deseja alterar o número
             da página, mas manter as outras configurações de paginação.
        - previous(): Retorna a página anterior, se disponível. Isso é útil quando você deseja navegar para a
            página anterior.
        - hasPrevious(): Verifica se há uma página anterior disponível.
        - getSortOr(Sort defaultSort): Retorna as opções de ordenação aplicadas à consulta ou, se não houver
            opções de ordenação definidas, retorna um valor padrão fornecido. Isso é útil quando você deseja obter
            as opções de ordenação, mas também deseja fornecer um valor padrão se não houver opções de ordenação definidas.

 */