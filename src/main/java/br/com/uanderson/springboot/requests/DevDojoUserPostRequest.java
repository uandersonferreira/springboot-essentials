package br.com.uanderson.springboot.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevDojoUserPostRequest {
    @Schema(description = "Nome do usuário", example = "John Doe")
    @NotEmpty(message = "The user's name cannot be empty") //Pega os null também
    private String name;

    @Schema(description = "Nome de usuário", example = "john")
    @NotEmpty(message = "The user's username cannot be empty")
    private String username;

    @Schema(description = "Senha do usuário", example = "123")
    @NotEmpty(message = "The user's password cannot be empty")
    private String password;

    @Schema(description = "Autoridades do usuário separadas por vírgulas", example = "ROLE_ADMIN, ROLE_USER")
    @NotEmpty(message = "The user's authorities cannot be empty")
    private String authorities;

}
