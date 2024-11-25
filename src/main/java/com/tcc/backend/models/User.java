package com.tcc.backend.models;

import com.tcc.backend.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    private Long idUser;
    @Enumerated(EnumType.STRING)
    @Column(name = "type_user", nullable = false)
    private UserType type;
    @Column(name = "name_user")
    private String name;
    @Column(name = "email_user")
    private String email;
    @Column(name = "pass_user")
    private String password;
    @Column(name = "cpf_user")
    private String cpf;
    @Column(name = "phone_user")
    private String phone;
    @Column(name = "cep_user")
    private String cep;
    @Column(name = "city_user")
    private String city;
    @Column(name = "neig_user")
    private String neighborhood;
    @Column(name = "stre_user")
    private String street;
    @Column(name = "num_user")
    private Integer number;
    @Column(name = "comp_user")
    private String complement;
}
