package br.com.uanderson.springboot.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.Customizer;
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
public class SecurityConfig {
    @Bean//CONTÉM A CONFIGURAÇÃO DO QUÊ ESTAMOS PROTEGENDO COM O PROTOCOLO HTTP
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);//desabilitando o CSRF (cenário de estudos)
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
         */
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoder {}", passwordEncoder.encode("test"));

        UserDetails userDetails = User
                .withUsername("Uanderson")
                .password(passwordEncoder.encode("academy"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

}//class
/*
O CSRF (Cross-Site Request Forgery) é um tipo de ataque que ocorre quando um
       invasor explora a confiança de um usuário autenticado para realizar ações
       não autorizadas em seu nome. Esse tipo de ataque geralmente envolve o envi
       de solicitações não autorizadas de um site malicioso para um site confiável
       no qual o usuário já está autenticado.

Para prevenir ataques CSRF em um aplicativo Spring Boot, o framework fornece o suporte
para geração e validação de tokens CSRF. O CSRF token (ou token de proteção CSRF) é um
valor único e aleatório que é gerado e associado a cada sessão de usuário. Esse token é
enviado ao cliente (geralmente como um cookie ou um cabeçalho HTTP) e deve ser incluído
em todas as solicitações que alteram o estado do servidor.

spring boot security doc CSRF: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html


 */