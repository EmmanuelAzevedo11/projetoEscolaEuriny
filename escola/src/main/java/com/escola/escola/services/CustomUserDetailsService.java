package com.escola.escola.services;

import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String raInput) throws UsernameNotFoundException {
        if (raInput == null || raInput.isBlank() || "RA".equalsIgnoreCase(raInput.trim())) {
            throw new UsernameNotFoundException("RA inválido");
        }

        String cleaned = raInput.trim();
        Optional<User> userOpt = userRepository.findByRa(cleaned);

        // Caso o aluno digite o RA com traço e dígito (ex: 000115778975-4 ou 115778975-4)
        if (userOpt.isEmpty() && cleaned.contains("-")) {
            cleaned = cleaned.substring(0, cleaned.indexOf('-')).trim();
            userOpt = userRepository.findByRa(cleaned);
        }

        // Caso o aluno digite o RA sem os zeros à esquerda (ex: 115778975 em vez de 000115778975)
        if (userOpt.isEmpty() && cleaned.matches("\\d+") && cleaned.length() < 12) {
            String paddedRa = String.format("%012d", Long.parseLong(cleaned));
            userOpt = userRepository.findByRa(paddedRa);
        }

        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("RA não encontrado"));

        return new org.springframework.security.core.userdetails.User(
                user.getRa(),
                user.getPassword(),
                true,
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
