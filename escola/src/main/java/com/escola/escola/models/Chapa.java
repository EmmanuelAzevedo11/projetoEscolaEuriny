package com.escola.escola.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "chapa")
public class Chapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Nome tem que ser preenchido")
    @Size(max = 255, min = 3, message = "O nome tem que ter no máximo 255 e no mínimo 255 caracteres ")
    private String name;

    @Column(name = "description", nullable = false)

    private String description;

    //caso alguma hora tenha número
    @Column(name = "number", nullable = false)
    @NotBlank(message = "O número da chapa é obrigatório")
    private Integer number;
}
