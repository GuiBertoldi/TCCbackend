package com.tcc.backend.dtos.psychologist;

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
public class PsychologistRequest {

    private Long idUser;

    @NotNull(message = "O tipo de usuário deve ser informado!")
    private UserType type;

    @NotBlank(message = "O nome deve ser informado!")
    private String name;

    @NotBlank(message = "O email deve ser informado!")
    @Email(message = "O email deve ser válido!")
    private String email;

    @NotBlank(message = "A senha é obrigatória!")
    private String password;

    @NotBlank(message = "O CPF deve ser informado!")
    private String cpf;

    private String phone;

    @NotBlank(message = "O CRP é obrigatório!")
    private String crp;
}
