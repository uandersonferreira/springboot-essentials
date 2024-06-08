package br.com.uanderson.springboot.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Spring boot security documentation:
 * https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html#servlet-authentication-unpwd
 */
@EnableWebSecurity//deixou de ter o @Configuration
@Configuration//para ser entendido com um bean pelo espring
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@Log4j2
@EnableMethodSecurity//Configuração que ativa/valida a anotação @PreAuthorize("hasRole('ADMIN')") do controller
public class SecurityConfig {

    /**
     * ALGUNS FILTROS EXISTENTES NO SPRING SECURITY, QUE FAZEM
     * PARTE DO PROCESSO DE:
     *  Authentication -> Authorization
     *
     * BasicAuthenticationFilter
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter
     * DefaultLogoutPageGeneratingFilter
     * FilterSecurityInterceptor
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean//CONTÉM A CONFIGURAÇÃO DO QUÊ ESTAMOS PROTEGENDO COM O PROTOCOLO HTTP
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);//desabilitando o CSRF (cenário de estudo)
        http.authorizeHttpRequests((authorize -> authorize
                        .anyRequest().authenticated())
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return http.build();
        /*
            Neste primeiro momento está dizendo que qualquer
            requisição http deve passar pela autenticação básica
            do htpp.
            Significa dizer que deve ser informado:
                - username  and  - password

            CookieCsrfTokenRepository.withHttpOnlyFalse();
            O httpOnly é um atributo de cookie que, quando definido como
            true, impede que o cookie seja acessado por JavaScript no navegador.
            Ao definir httpOnly como false, você permite que o JavaScript acesse
            o cookie
         */
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoder {}", passwordEncoder.encode("test"));

        UserDetails admin = User
                .withUsername("Uanderson")
                .password(passwordEncoder.encode("academy"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails user = User.withUsername("devdojo")
                .password(passwordEncoder.encode("123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

}//class
/*
Ao realizar uma requisição 'QUE ALTERE O ESTADO DO SERVIDO'
O CSRF TOKEN entra em ação, para verificar a origem das solicitações
caso esteja ativado.

Neste sentindo, para prevenir ataques CSRF em aplicações Spring Boot, o framework fornece o suporte
para geração e validação de tokens CSRF. O CSRF token (ou token de proteção CSRF) é um
valor único e aleatório que é gerado e associado a cada sessão de usuário. Esse token é
enviado ao cliente (geralmente como um cookie ou um cabeçalho HTTP) e deve ser incluído
em todas as solicitações que alteram o estado do servidor.

"Alterar o estado do servidor" significa fazer mudanças nos dados,
configurações ou funcionalidades de um servidor de computador.
Isso inclui ações como adicionar/remover informações, configurar
regras ou modificar configurações. Qualquer ação que afete o
funcionamento ou conteúdo do servidor é considerada uma alteração
de estado. (CONTRÁRIO DA IDEMPOTÊNCIA)

CSRF - Quando alguém executa uma ação em seu nome.
O CSRF (Cross-Site Request Forgery) é um tipo de ataque que ocorre quando um
       invasor explora a confiança de um usuário autenticado para realizar ações
       não autorizadas em seu nome. Esse tipo de ataque geralmente envolve o envio
       de solicitações não autorizadas de um site malicioso para um site confiável
       no qual o usuário já está autenticado.

EXEMPLO DE SITUAÇÃO COM CSRF TOKEN:

  1. Um usuário autenticado acessa o site e inicia uma solicitação
     para atualizar sua senha.
  2- O servidor Spring Boot gera um token CSRF exclusivo e o
     associa à sessão do usuário.
  3- Quando o usuário envia a solicitação para atualizar sua senha,
    o token CSRF é incluído automaticamente na solicitação como um
    cabeçalho HTTP ou um campo de formulário oculto.
  4- O servidor Spring Boot verifica se o token CSRF na solicitação
    corresponde ao token associado à sessão do usuário. Se corresponder,
    a solicitação é processada; caso contrário, a solicitação é rejeitada.

Isso garante que apenas o usuário autenticado possa atualizar sua
senha, pois apenas ele tem acesso ao token CSRF correto.

SCRIPT PARA PEGAR O COOKIE NO POSTMAN E REALIZAR A SIMULAÇÃO DO CSRF:
var xsrfCookie = postman.getResponseCookie("XSRF-TOKEN");
postman.setEnvironmentVariable('x-xsrf-token', xsrfCookie.value);

spring boot security doc CSRF: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html

EXPLICAÇÃO BÁSICA DO QUE CADA FILTRO REALIZA NO SPRING SECURITY:

 BasicAuthenticationFilter:
 - Este filtro lida com a autenticação HTTP básica, geralmente usando o cabeçalho
 Authorization com credenciais codificadas em Base64.
 - É usado principalmente para autenticação em aplicações que utilizam HTTP Basic
 Authentication, onde o nome de usuário e a senha são enviados diretamente no
 cabeçalho da solicitação.
 - Ele é processado em cada solicitação que contém o cabeçalho Authorization apropriado.

UsernamePasswordAuthenticationFilter:
 Este filtro lida com a autenticação de nome de usuário e senha, sendo mais flexível
 do que o BasicAuthenticationFilter.
 Permite que os usuários enviem suas credenciais por meio de um formulário da web
 (form login).
 Este filtro é comumente usado em aplicativos da web que fornecem um formulário de login.
 Normalmente, intercepta solicitações de login POST enviadas para uma URL específica
 (por exemplo, /login).

 DefaultLoginPageGeneratingFilter:
 Este filtro é usado para gerar automaticamente uma página de login padrão, caso não
 seja fornecida uma página personalizada.
 É útil para desenvolvimento rápido e prototipagem, fornecendo uma página de login
 simples gerada automaticamente.
 Pode ser substituído por uma página de login personalizada configurada no Spring Security.

 DefaultLogoutPageGeneratingFilter:
 Semelhante ao DefaultLoginPageGeneratingFilter, este filtro gera uma página de logout
 padrão se você não fornecer uma página personalizada.
 Ajuda a gerenciar o processo de logout do usuário, fornecendo uma interface simples
 para sair da aplicação.
 Pode ser substituído por uma página de logout personalizada configurada no Spring Security.

 FilterSecurityInterceptor:
 Este não é um filtro de autenticação, mas um filtro de autorização.
 Ele intercepta solicitações após a autenticação e verifica se o usuário tem permissão
 para acessar um recurso específico com base nas regras de autorização configuradas.
 Aplica autorizações granulares com base em configurações específicas, como anotações
 @Secured, @PreAuthorize, ou configurações no arquivo de configuração de segurança.
 Trabalha em conjunto com AccessDecisionManager e SecurityMetadataSource para fazer
 decisões de autorização.

 */