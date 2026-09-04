package com.escola.escola.services.csvMigrationsServices;

import com.escola.escola.enums.UserRole;
import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class AlunoNineAImportService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoNineAImportService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String ...args) throws Exception{
        boolean response = userRepository.existsByTeam("9a");
        ClassPathResource resource = new ClassPathResource("csvMigrations/listaAlunosNineA.csv");
        if(response == false) {

            try (BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
            ) {

                bufferedReader.readLine();
                bufferedReader.readLine();

                String linha;

                // O laço engloba toda a lógica de fatiamento e salvamento
                while ((linha = bufferedReader.readLine()) != null) {

                    String[] colunas = linha.split(";");

                    if (colunas.length >= 3) {
                        String nome = colunas[0].trim();
                        String ra = colunas[1].trim();
                        String digitoRa = colunas[2].trim();

                        // Importa apenas alunos ativos
                            if (userRepository.findByRa(ra).isEmpty()) {
                                User aluno = new User();
                                aluno.setUsername(nome);
                                aluno.setRa(ra);
                                aluno.setRaDigit(digitoRa);
                                aluno.setPassword(passwordEncoder.encode("Euriny@2026"));
                                aluno.setRole(UserRole.ROLE_COMUM);
                                aluno.setTeam("9a"); // Seta ANTES do save

                                userRepository.save(aluno);
                            }

                    }
                }
                System.out.println("Importação de alunos concluída com sucesso!");
            } catch (Exception e) {
                throw new Exception("Erro: " + e.getMessage());
            }
        }
    }
}
