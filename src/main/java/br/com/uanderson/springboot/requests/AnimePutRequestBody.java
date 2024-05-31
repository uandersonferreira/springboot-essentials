package br.com.uanderson.springboot.requests;

import lombok.Data;

@Data
public class AnimePutRequestBody { //Segue o mesmo padrão/conceito dos DTO's
    private Long id;
    private String name;
}
