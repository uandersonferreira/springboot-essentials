package br.com.uanderson.springboot.handler;

import br.com.uanderson.springboot.exception.BadRequestException;
import br.com.uanderson.springboot.exception.BadRequestExceptionDetails;
import br.com.uanderson.springboot.exception.ExceptionDetails;
import br.com.uanderson.springboot.exception.ValidationExceptionDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice // Mais generico padrão MVC - retornar as views html ou JSON ou XML
//@RestControllerAdvice// mais específico e adaptado para controladores RESTful retorna dados JSON ou XML
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    //   handler Global -  Manipulação/Tratamento global
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(
            BadRequestException badRequestException) {
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
    }//method

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField)
                .collect(Collectors.joining(", "));

        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                // Criação de uma resposta personalizada para exceções do tipo MethodArgumentNotValidException
                ValidationExceptionDetails.builder()
                        .title("Bad Request Exception, Invalid fields")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .details(exception.getMessage())//poderia ser uma mensagem mais curta tipo: "Check the field(s) errors"
                        .developerMessage(exception.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST
        );
        /*
         - Para saber Qual é a class da exception correta gerada na url adicione: '?trace=true'
            - http:localhost:8080/animes?trace=true
         - Em resumo, o método getBindingResult() é usado para obter um objeto BindingResult que contém
          informações sobre os erros de validação de um formulário em um aplicação Spring.
         - Sobreescrevendo um tratamento de erro padrão da class ResponseEntityExceptionHandler
         do spring para um personalizada. (@Override)

         */
    }//method

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode.value())
                .title(ex.getCause().getMessage())
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();
        return createResponseEntity(exceptionDetails, headers, statusCode, request);
        /*
         Sobreescrevendo um tratamento de erro padrão da class ResponseEntityExceptionHandler
         do spring para um personalizada.(@Override)
         */
    }


}//class
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

OBS: A fim de manter uma padronização das mensagens de erros do spring,
        podemos sobreescrever o comportamento dos method já existente de tratamento do spring
        para um, que tenha como retorno o que queremos. Dentre as classes de erros que podemos
        sobrescrever temos:

CLASSES QUE ESTÃO CONTIDAS NO(ResponseEntityExceptionHandler) TRATAMENTO DE ERRO DO SPRING POR DEFAULT:

	@ExceptionHandler({
			HttpRequestMethodNotSupportedException.class,
			HttpMediaTypeNotSupportedException.class,
			HttpMediaTypeNotAcceptableException.class,
			MissingPathVariableException.class,
			MissingServletRequestParameterException.class,
			MissingServletRequestPartException.class,
			ServletRequestBindingException.class,
			MethodArgumentNotValidException.class,
			HandlerMethodValidationException.class,
			NoHandlerFoundException.class,
			NoResourceFoundException.class,
			AsyncRequestTimeoutException.class,
			ErrorResponseException.class,
			MaxUploadSizeExceededException.class,
			ConversionNotSupportedException.class,
			TypeMismatchException.class,
			HttpMessageNotReadableException.class,
			HttpMessageNotWritableException.class,
			MethodValidationException.class,
			BindException.class
		})
*/