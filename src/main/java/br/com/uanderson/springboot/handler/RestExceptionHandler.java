package br.com.uanderson.springboot.handler;

import br.com.uanderson.springboot.exception.BadRequestException;
import br.com.uanderson.springboot.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice // Mais generico padrão MVC - retornar as views html ou JSON ou XML
//@RestControllerAdvice// mais específico e adaptado para controladores RESTful retorna dados JSON ou XML
public class RestExceptionHandler {
    //   handler Global -  Manipulação/Tratamento global
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(
            BadRequestException badRequestException){
        return new ResponseEntity<>(
                // Criação de uma resposta personalizada para exceções do tipo BadRequestException
                BadRequestExceptionDetails.builder()
                        .title("Bad Request Exception, Check the Documentation")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .details(badRequestException.getMessage())
                        .developerMessage(badRequestException.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
}
/*

@ControllerAdvice:
 - É uma anotação usada para definir um componente global que lida
  com exceções em vários controladores do Spring.

Ele oferece a possibilidade de tratar exceções de forma centralizada, personalizar
respostas de erro e compartilhar atributos em modelos.
Isso ajuda a melhorar a consistência e a modularidade do tratamento de exceções em toda a aplicação.
Em essência, atua como uma barreira que intercepta todas as exceções lançadas nos controladores.

@ExceptionHandler(exceptionPersonalizada.class):
 - Esta anotação é usada para lidar com exceções específicas e personalizar as respostas enviadas ao cliente.
Quando uma exceção do tipo exceptionPersonalizada é lançada em qualquer controlador do Spring,
este método é acionado para fornecer uma resposta adequada.
Isso permite que você manipule diferentes tipos de exceções de maneira personalizada,
fornecendo respostas detalhadas e úteis para o cliente.

A diferença entre @ControllerAdvice e @RestControllerAdvice está relacionada ao
tipo de controlador que eles aconselham e afetam.

@ControllerAdvice:
@ControllerAdvice é uma anotação usada para definir um componente global que lida com
exceções em vários controladores do Spring. Ele oferece a possibilidade de tratar exceções
de forma centralizada, personalizar respostas de erro e compartilhar atributos em modelos.
Geralmente é usado para controladores tradicionais do Spring MVC que retornam tanto
views quanto dados.

@RestControllerAdvice:
@RestControllerAdvice é uma extensão de @ControllerAdvice e é usada especificamente
para controladores RESTful no Spring. Assim como @ControllerAdvice, ele lida com
exceções de forma global, mas é mais adequado para controladores que retornam dados
formatados (como JSON ou XML) em vez de views HTML.

É útil quando você deseja fornecer respostas de erro padronizadas em uma API REST.

Em resumo, enquanto @ControllerAdvice é mais genérico e adequado para controladores
tradicionais do Spring MVC, @RestControllerAdvice é mais específico e adaptado para
controladores RESTful que retornam dados formatados.

*/