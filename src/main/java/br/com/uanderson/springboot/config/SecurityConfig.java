package br.com.uanderson.springboot.config;

import br.com.uanderson.springboot.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@Log4j2
@EnableMethodSecurity//Configuração que ativa/valida a anotação @PreAuthorize("hasRole('ADMIN')") do controller
@RequiredArgsConstructor
public class SecurityConfig {

    private final DevDojoUserDetailsService devDojoUserDetailsService;

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
                        .requestMatchers("/animes/admin/**").hasRole("ADMIN")//A Ordem de declaração é importante
                        .requestMatchers("/animes/**").hasRole("USER")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated())
                )
                .httpBasic(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return http.build();
        /*
            Neste primeiro momento está dizendo que qualquer
            requisição http deve passar pela autenticação básica
            do htpp.
            Significa dizer que deve ser informado:
                - username  and  - password
            Agora ao configurar uma requestMatchers(antiga antMatchers) é preciso ter uma atenção:
            - pois a ORDEM DE DECLARAÇÃO é importante
                - Comecando da MENOS restritiva para a MAIS restritiva
            - Também podemos definir uma proteção baseado no tipo do method (http) da requisição em uma
               determinada URL.
               ex: .requestMatchers(HttpMethod.POST, "/animes").hasAnyRole("ADMIN", "USER")

            OBS: Útil quando seguimos um padrão de url(endpoints), o @Preauthorize não deixa de
                ser válido :), basta analisar a necessidade.

            CookieCsrfTokenRepository.withHttpOnlyFalse();
            O httpOnly é um atributo de cookie que, quando definido como
            true, impede que o cookie seja acessado por JavaScript no navegador.
            Ao definir httpOnly como false, você permite que o JavaScript acesse
            o cookie
         */
    }

    @Bean // Define um bean Spring
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) { // Define um método que retorna um AuthenticationManager
        DaoAuthenticationProvider inMemoryProvider = new DaoAuthenticationProvider(); // Cria um provedor de autenticação para autenticação em memória
        inMemoryProvider.setUserDetailsService(inMemoryUserDetailsService()); // Define o serviço de detalhes do usuário para o provedor de autenticação em memória
        inMemoryProvider.setPasswordEncoder(passwordEncoder); // Define o codificador de senha para o provedor de autenticação em memória

        DaoAuthenticationProvider databaseProvider = new DaoAuthenticationProvider(); // Cria um provedor de autenticação para autenticação no banco de dados
        databaseProvider.setUserDetailsService(devDojoUserDetailsService); // Define o serviço de detalhes do usuário para o provedor de autenticação no banco de dados
        databaseProvider.setPasswordEncoder(passwordEncoder); // Define o codificador de senha para o provedor de autenticação no banco de dados

        ProviderManager providerManager = new ProviderManager(inMemoryProvider, databaseProvider); // Cria um ProviderManager com os provedores de autenticação configurados
        providerManager.setEraseCredentialsAfterAuthentication(false); // Define se as credenciais devem ser apagadas após a autenticação

        return providerManager; // Retorna o ProviderManager configurado como AuthenticationManager
        /*
         * Configura um AuthenticationManager que suporta autenticação em memória e no banco de dados.
         *
         * Este método cria dois provedores de autenticação:
         * - Um provedor para autenticação em memória (inMemoryProvider).
         * - Um provedor para autenticação no banco de dados (databaseProvider).
         *
         * Cada provedor é configurado com um UserDetailsService correspondente:
         * - inMemoryProvider usa o serviço inMemoryUserDetailsService() para buscar
         *   os detalhes do usuário em memória.
         * - databaseProvider usa o serviço devDojoUserDetailsService para buscar
         *   os detalhes do usuário no banco de dados.
         *
         * Ambos os provedores são configurados com o mesmo PasswordEncoder para criptografar as senhas.
         *
         * Os provedores configurados são então adicionados a um ProviderManager, que é retornado
         * como o AuthenticationManager configurado.
         */

    }


    @Bean
    public UserDetailsService inMemoryUserDetailsService() {
        UserDetails adminInMemory = User
                .withUsername("admin_memory")
                .password(passwordEncoder().encode("123"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails userInMemory = User
                .withUsername("user_memory")
                .password(passwordEncoder().encode("123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(adminInMemory, userInMemory);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorne um PasswordEncoder adequado, como BCryptPasswordEncoder,
        // para codificar as senhas dos usuários
        return new BCryptPasswordEncoder();
    }


    /*
    POR CAUSA DO POLIMORFISMO, userDetailsService CONSEGUE LOCALIZAR O MÉTODO
    loadUserByUsername(), QUE ESTÁ SENDO IMPLEMENTADO EM DevDojoUserDetailsService, ATRAVÉS
    DA VARIÁVEL DE REFERÊNCIA PASSADA.

    NO MÉTODO loadUserByUsername(), ATRAVÉS DA VARIÁVEL DE REFERÊNCIA DO DevDojoRepository
    (que ESTABELECE A CONEXÃO COM O BANCO DE DADOS), CHAMA-SE O MÉTODO findByUsername(),
    QUE REALIZA UMA CONSULTA E RETORNA UM UserDetails OU UM Optional/OU LANÇA UsernameNotFoundException.

    ****************-------------------------------************************

    A PARTE DE CONFIGURAÇÃO DO SPRING SUPORTA MÚLTIPLA AUTENTICAÇÃO, COM DIFERENTES
    TIPOS DE PROVIDERS. (PENSE EM BANCOS DE DADOS)

    MÚLTIPLA AUTENTICAÇÃO -> MAIS DE UM PONTO DE ENTRADA DE DADOS.

    DICA: NUNCA SALVE SENHAS NO BANCO COMO TEXTO PURO (PLAINTEXT PASSWORD).
    PORTANTO, DEVEMOS UTILIZAR ALGUM TIPO DE CRIPTOGRAFIA PARA AS SENHAS.

        ------------ AUTENTICAÇÃO SOMENTE EM MEMÓRIA --------------
    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoder {}", passwordEncoder.encode("123"));
        //{bcrypt}$2a$10$fRvLXfIR9Thb/RCYxJdG4uzPxrqla2H8ZAjy/Oc7xQQzFrZ2w1mRS

        UserDetails adminInMemory = User
                .withUsername("Uanderson")
                .password(passwordEncoder.encode("academy"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(adminInMemory);
    }*/

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