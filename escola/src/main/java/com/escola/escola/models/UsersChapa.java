package com.escola.escola.models;

import com.escola.escola.enums.UserChapaRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "users_chapa")
public class UsersChapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapa_id", nullable = false)
    private Chapa chapa;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_chapa", nullable = false)
    private UserChapaRole roleChapa; //caso um dia tenha cargo

}
