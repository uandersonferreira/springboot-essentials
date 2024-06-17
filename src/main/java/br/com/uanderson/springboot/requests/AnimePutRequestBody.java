package br.com.uanderson.springboot.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutRequestBody { //Segue o mesmo padrão/conceito dos DTO's
    private Long id;
    private String name;
}
