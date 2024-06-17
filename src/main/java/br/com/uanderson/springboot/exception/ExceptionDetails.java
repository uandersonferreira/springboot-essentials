package br.com.uanderson.springboot.exception;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder
public class ExceptionDetails {//Super class - nível mais alto
    //Definindo os campos do modelo da Exception default a todas as exceptions
    protected String title;
    protected int status;
    protected String details;
    protected String developerMessage;
    protected LocalDateTime timestamp;
}
/*
handler Global -  Manipulação/Tratamento global

BadRequestExceptionDetails -> estamos definindo uma class
que irá conter os campos padrões que serão mostrados, quando
for lançado uma bad request.
Definindo o modelo da Exception


OBS: E mais recomendando manter um padrão ao realizar o tramento de exception
ou seja manter uma consistência.

1° - CRIE UMA CLASSE PARA MAPEAR/CRIAR UM MODELO A/DA EXCEPTION
    COM OS CAMPOS DESEJADOS.
2° - CRIE A EXCEPTION PERSONALIZADA QUE DESEJA LANÇAR
3° - CRIE UM HANDLER GLOBAL QUE IRÁ CAPTAR A EXCEPTION PERSONALIZADA
     E DEVOLVER-LÁ COM OS CAMPOS DA CLASS DE MODELO CRIADA.

A anotação @SuperBuilder do Lombok é útil para gerar um padrão de
construção (builder pattern) que suporta herança, permitindo criar instâncias
de classes derivadas usando construtores encadeados de forma conveniente.
No entanto, ela pode falhar ou ser "denied" (negada) por algumas razões comuns,
especialmente quando usada em conjunto com outras anotações como @Data.
Nesse caso, a recomendação é usar anotações mais específicas, como:
@Getter, @Setter, @EqualsAndHashCode, @ToString e @NoArgsConstructor.

@SuperBuilder gera um construtor protegido na classe que usa uma instância
do construtor como parâmetro. Este construtor define os campos da nova
instância com os valores do construtor.

 */