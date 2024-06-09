package br.com.uanderson.springboot.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
