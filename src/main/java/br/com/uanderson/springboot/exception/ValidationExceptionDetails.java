package br.com.uanderson.springboot.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails{//Subclass(FILHA) de ExceptionDetails(PAI)
    //Possui os campos de seu 'PAI' e dos demais adicionados aqui.
    private final String fields;
    private final String fieldsMessage;

    /*
    A anotação @SuperBuilder do Lombok é usada para gerar automaticamente um
    construtor que chama o construtor da classe pai (superclasse) e configura
    os campos herdados, é OBRIGATÓRIO a declaração tanto tanto na super class, quanto nas subclass dela.
    Isso é particularmente útil em hierarquias de classes
    quando você deseja criar construtores que inicializem os campos na classe
    atual e também na classe pai.
     */
}
/*
    Class criada para lidar com os campos e mensagens adicionais
    de 'errors' em uma exception.
    - Para saber Qual é a class da exception correta gerada na url adicione: '?trace=true'
            - http:localhost:8080/animes?trace=true

    {
    "timestamp": "2024-05-31T20:24:59.815+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed for object='animePostRequestBody'. Error count: 1",
    "errors": [
        {
            "codes": [
                "NotBlank.animePostRequestBody.name",
                "NotBlank.name",
                "NotBlank.java.lang.String",
                "NotBlank"
            ],
            "arguments": [
                {
                    "codes": [
                        "animePostRequestBody.name",
                        "name"
                    ],
                    "arguments": null,
                    "defaultMessage": "name",
                    "code": "name"
                }
            ],
            "defaultMessage": "The anime 'name' cannot be empty",
            "objectName": "animePostRequestBody",
            "field": "name",
            "rejectedValue": null,
            "bindingFailure": false,
            "code": "NotBlank"
        }
    ],
    "path": "/animes"
}
 */