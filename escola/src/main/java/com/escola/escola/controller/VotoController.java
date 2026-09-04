package com.escola.escola.controller;

import com.escola.escola.exceptions.UsuarioNaoEncontradoException;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.User;
import com.escola.escola.models.Voto;
import com.escola.escola.repository.UserRepository;
import com.escola.escola.services.ChapaService;
import com.escola.escola.services.VotoService;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/votos")
@Controller
public class VotoController {

    private final VotoService votoService;
    private final UserRepository userRepository;
    private final ChapaService chapaService;

    public VotoController(VotoService votoService, UserRepository userRepository, ChapaService chapaService){
        this.votoService = votoService;
        this.userRepository = userRepository;
        this.chapaService = chapaService;
    }

    @PostMapping
    public ModelAndView postVoto(@Valid @ModelAttribute Voto voto,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes)
    {
        if (userDetails == null) {
            return new ModelAndView("redirect:/login");
        }

        User userLogado = userRepository.findByRa(userDetails.getUsername()).orElseThrow(
                () -> new UsuarioNaoEncontradoException("Usuário não encontrado")
        );

        try {
            votoService.postVoto(voto, userLogado);
            String mensagem = "Parabéns pelo voto";
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Voto registrado com sucesso! Obrigado pela participação.");
            return new ModelAndView("redirect:/votar/sucesso");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return new ModelAndView("redirect:/votar");
        }
    }

    @GetMapping("/chapas")
    public ModelAndView getVotosChapa(RedirectAttributes redirectAttributes)
    {
        ModelAndView modelAndView = new ModelAndView("contagemVotos");
        List<Chapa> listaChapas = chapaService.findChapas();
        if(listaChapas.isEmpty()){
            String mensagemErro = "Ainda não há chapas cadastradas";
            redirectAttributes.addFlashAttribute("mensagemErro",mensagemErro);
            return new ModelAndView("redirect:/paginaInicial");
        }

        return modelAndView.addObject("listaChapas", listaChapas);
    }
}
