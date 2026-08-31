package com.escola.escola.controller;

import com.escola.escola.models.User;
import com.escola.escola.repository.UserRepository;
import com.escola.escola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("usuarios/lista");
        try {
            List<User> users = userService.findUsers();
            modelAndView.addObject(users);
        } catch (Exception e) {
            modelAndView.addObject("mensagemErro", e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getUser(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("users/details");
        try {
            User user = userService.findUser(id);
            modelAndView.addObject("user", user);
        } catch (Exception e) {
            modelAndView.addObject("mensagemErro", e.getMessage());
        }
        return modelAndView;
    }





}
