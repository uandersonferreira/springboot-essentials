package br.com.uanderson.springboot.exception;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class BadRequestExceptionDetails extends ExceptionDetails{//Subclass(FILHA) de ExceptionDetails(PAI)
    /*
        A anotação @SuperBuilder do Lombok é usada para gerar automaticamente um
        construtor que chama o construtor da classe pai (superclasse) e configura
        os campos herdados, é OBRIGATÓRIO a declaração tanto tanto na super class, quanto nas subclass dela.
        Isso é particularmente útil em hierarquias de classes
        quando você deseja criar construtores que inicializem os campos na classe
        atual e também na classe pai.
    */
}
