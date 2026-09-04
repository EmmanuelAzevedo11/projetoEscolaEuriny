package com.escola.escola.services;

import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String ra) throws UsernameNotFoundException {
        User user = userRepository.findByRa(ra)
                .orElseThrow(() -> new UsernameNotFoundException("RA não encontrado"));

        return new org.springframework.security.core.userdetails.User(
                user.getRa(),
                user.getPassword(),
                true,
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );

    }
}
