package com.escola.escola.services;

import com.escola.escola.enums.UserRole;
import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminsCreationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String ...args) throws  Exception{

        try {
            long numberAdmins = userRepository.countByRole(UserRole.ROLE_ADMIN);
            if(numberAdmins == 0){
                User user = new User();
                user.setUsername("Emmanuel Azevedo");
                user.setRa("emmanuel");
                user.setRole(UserRole.ROLE_ADMIN);
                String passwordUserEmmanuel = "11042006";
                user.setPassword(passwordEncoder.encode(passwordUserEmmanuel));


                userRepository.save(user);
                System.out.println(">> Criado primeiro admin <<");

                User user2 = new User();
                user2.setUsername("Gabriel Proati");
                user2.setRa("gabriel");
                user2.setRole(UserRole.ROLE_ADMIN);
                String passwordUserGabriel = "123456";

                user2.setPassword(passwordEncoder.encode(passwordUserGabriel));

                userRepository.save(user2);
                System.out.println(">> Criado segundo admin <<");
            }else{
                System.out.println(">>>  Já existe os usuários admins  <<<");
            }

        } catch (Exception e){
            throw new Exception("Erro ao adicionar os usuários: " + e.getMessage());
        }
    }

}
