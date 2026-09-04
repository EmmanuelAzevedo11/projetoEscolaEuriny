package com.escola.escola.models;

import com.escola.escola.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username" ,nullable = false)
    @NotBlank(message = "Usuário tem que ter um nome")
    private String username;

    @Column(name = "ra",nullable = true)
    private String ra;

    @Column(name = "ra_digit", nullable = true)
    private String raDigit;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "O usuário tem que ter uma senha padrão")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Selecione um tipo de usuário")
    private UserRole role;

    @Column(name = "class", nullable = true)
    @NotBlank(message = "O usuário tem que ter uma série de forma obrigatória")
    private String team;


}
