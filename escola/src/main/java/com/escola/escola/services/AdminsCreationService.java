package com.escola.escola.services;

import com.escola.escola.enums.UserRole;
import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminsCreationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String ...args) throws Exception {
        try {
            // Remove registro inválido de cabeçalho CSV caso tenha sido importado anteriormente
            userRepository.findByRa("RA").ifPresent(invalidUser -> {
                try {
                    userRepository.delete(invalidUser);
                } catch (Exception ignored) {
                }
            });

            long numberAdmins = userRepository.countByRole(UserRole.ROLE_ADMIN);
            if (numberAdmins == 0) {
                User user = new User();
                user.setUsername("Emmanuel Azevedo");
                user.setRa("emmanuel");
                user.setTeam("none");
                user.setRole(UserRole.ROLE_ADMIN);
                String passwordUserEmmanuel = "11042006";
                user.setPassword(passwordEncoder.encode(passwordUserEmmanuel));

                userRepository.save(user);
                System.out.println(">> Criado primeiro admin <<");

                User user2 = new User();
                user2.setUsername("Gabriel Proati");
                user2.setRa("gabriel");
                user2.setRole(UserRole.ROLE_ADMIN);
                user2.setTeam("none");
                String passwordUserGabriel = "123456";

                user2.setPassword(passwordEncoder.encode(passwordUserGabriel));


                userRepository.save(user2);
                System.out.println(">> Criado segundo admin <<");

                User user3 = new User();
                user3.setUsername("Diretoria");
                user3.setRa("Diretoria");
                user3.setRole(UserRole.ROLE_ADMIN);
                user3.setTeam("none");
                String passwordDiretoria = "diretoria123456";

                user3.setPassword(passwordEncoder.encode(passwordDiretoria));
                userRepository.save(user3);
                System.out.println(">> Criado usuário admin da diretoria");

                User user4 = new User();
                user4.setUsername("Professor");
                user4.setRa("Professor");
                user4.setRole(UserRole.ROLE_ADMIN);
                user4.setTeam("none");
                String passwordProfessor = "professor123456";

                user4.setPassword(passwordEncoder.encode(passwordProfessor));
                userRepository.save(user4);
                System.out.println(">> Criado usuário admin de professor");
            } else {
                System.out.println(">>>  Já existe os usuários admins  <<<");
            }

        } catch (Exception e) {
            throw new Exception("Erro ao adicionar os usuários: " + e.getMessage());
        }
    }
}
