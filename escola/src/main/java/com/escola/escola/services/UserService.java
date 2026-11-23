package com.escola.escola.services;

import com.escola.escola.exceptions.UsuarioNaoEncontradoException;
import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findUsers(){
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRa() == null || !"RA".equalsIgnoreCase(u.getRa()))
                .sorted(Comparator.comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public User findUser(Long id){
        return userRepository.findById(id).orElseThrow(() ->
                new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    public List<User> findByTeam(String team){
        return userRepository.findByTeam(team);
    }

    public User createUser(User user){
        if(user.getRa() != null && !user.getRa().isBlank() && userRepository.findByRa(user.getRa()).isPresent()){
            throw new UsuarioNaoEncontradoException("RA já cadastrado");
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UsuarioNaoEncontradoException("Usuário já cadastrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
}
