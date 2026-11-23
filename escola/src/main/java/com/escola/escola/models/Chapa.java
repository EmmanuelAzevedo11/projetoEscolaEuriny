package com.escola.escola.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @Size(max = 255, min = 3, message = "O nome deve ter entre 3 e 255 caracteres")
    private String name;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @Column(name = "number", nullable = false)
    @NotNull(message = "O número da chapa é obrigatório")
    private Integer number;

    @Column(name = "objective", nullable = true, columnDefinition = "TEXT")
    private String objective;

    @OneToMany(mappedBy = "chapa", fetch = FetchType.LAZY)
    private List<Voto> votos = new ArrayList<>();

    @OneToMany(mappedBy = "chapa", fetch = FetchType.EAGER)
    private List<UsersChapa> membros = new ArrayList<>();
}
