package com.escola.escola.services;

import com.escola.escola.exceptions.UsuarioNaoEncontradoException;
import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> findUsers(){
        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            throw new UsuarioNaoEncontradoException("Usuários não encontrados");
        }

        return users;
    }

    public User findUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() ->
                new UsuarioNaoEncontradoException("Usuário não econtrado"));
        return user;
    }

    public List<User> findByTeam(String team){
        List<User> users = userRepository.findByTeam(team);

        if(users.isEmpty()){
            throw new UsuarioNaoEncontradoException("Usuários não encontrados");
        }

        return users;
    }

    public User createUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UsuarioNaoEncontradoException("Usuário já cadastrado");
        }
        userRepository.save(user);
        return user;
    }
}
