package br.com.uanderson.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{//Exceção BAD_REQUEST customizada
    public BadRequestException(String message) {
        super(message);
        /*
          Chama o construtor da classe pai RuntimeException com a mensagem de erro fornecida.
          Isso permite, fornecer uma mensagem personalizada quando lançar essa exceção.
         */
    }
}//class
/*
Exception:
 - Exception é uma classe base para exceções verificadas.
  Isso significa que, ao lançar uma exceção do tipo Exception ou qualquer subclasse dela,
  o compilador exige que você a trate explicitamente usando um bloco try-catch ou
  declarando que seu método lança essa exceção.

 - As exceções que estendem Exception e não RuntimeException são geralmente usadas para
  representar condições de erro que podem ser recuperadas, como erros de entrada/saída,
  erros de banco de dados, etc.

RuntimeException:
    - RuntimeException é uma subclasse de Exception e representa exceções não verificadas.
     Isso significa que o compilador não exige que você as trate explicitamente.

 - Exemplos comuns de exceções não verificadas incluem NullPointerException,
  IndexOutOfBoundsException, IllegalArgumentException, entre outras.
 - As exceções que estendem RuntimeException são geralmente usadas para representar
  erros de programação, como acessar um objeto nulo, índices de matriz fora dos limites,
  argumentos inválidos passados para um método, etc.

*/