package com.tcc.backend.dtos.user;

import com.tcc.backend.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private Long idUser;

    @NotNull(message = "O tipo de usuário deve ser informado!")
    private UserType type;

    @NotBlank(message = "O nome deve ser informado!")
    private String name;

    @NotBlank(message = "O email deve ser informado!")
    @Email(message = "O email deve ser válido!")
    private String email;

    private String password;

    @NotBlank(message = "O CPF deve ser informado!")
    private String cpf;

    @NotBlank(message = "O telefone deve ser informado!")
    private String phone;

    private String cep;

    private String city;

    private String neighborhood;

    private String street;

    private Integer number;

    private String complement;
}
