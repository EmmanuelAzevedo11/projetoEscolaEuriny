package com.escola.escola.controller;

import com.escola.escola.exceptions.UsuarioNaoEncontradoException;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.User;
import com.escola.escola.models.Voto;
import com.escola.escola.repository.UserRepository;
import com.escola.escola.services.ChapaService;
import com.escola.escola.services.VotoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/votar")
    public ModelAndView getVotar(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView("votar");
        modelAndView.addObject("listaChapas", chapaService.findChapas());
        modelAndView.addObject("voto", new Voto());

        if (userDetails != null) {
            userRepository.findByRa(userDetails.getUsername()).ifPresent(user -> {
                modelAndView.addObject("usuarioLogado", user);
                modelAndView.addObject("jaVotou", votoService.jaVotou(user.getId()));
                modelAndView.addObject("membroDeChapa", votoService.pertenceAChapa(user));
            });
        }
        return modelAndView;
    }

    @PostMapping
    public ModelAndView postVoto(@ModelAttribute Voto voto,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return new ModelAndView("redirect:/login");
        }

        User userLogado = userRepository.findByRa(userDetails.getUsername()).orElseThrow(
                () -> new UsuarioNaoEncontradoException("Usuário não encontrado")
        );

        try {
            votoService.postVoto(voto, userLogado);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Voto registrado com sucesso! Obrigado pela participação.");
            return new ModelAndView("redirect:/votos/votar");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return new ModelAndView("redirect:/votos/votar");
        }
    }

    @GetMapping("/chapas")
    public ModelAndView getVotosChapa() {
        ModelAndView modelAndView = new ModelAndView("chapas/contagemVotos");
        List<Chapa> listaChapas = chapaService.findChapas();

        List<ResultadoVotacaoDto> resultadoVotacao = listaChapas.stream()
                .map(c -> new ResultadoVotacaoDto(
                        c.getName(),
                        c.getNumber(),
                        votoService.contarVotosPorChapa(c.getId())
                ))
                .sorted(Comparator.comparingLong(ResultadoVotacaoDto::getTotalVotos).reversed())
                .collect(Collectors.toList());

        long totalGeralVotos = resultadoVotacao.stream()
                .mapToLong(ResultadoVotacaoDto::getTotalVotos)
                .sum();

        modelAndView.addObject("resultadoVotacao", resultadoVotacao);
        modelAndView.addObject("totalGeralVotos", totalGeralVotos);
        return modelAndView;
    }

    public static class ResultadoVotacaoDto {
        private final String nomeChapa;
        private final Integer numeroChapa;
        private final Long totalVotos;

        public ResultadoVotacaoDto(String nomeChapa, Integer numeroChapa, Long totalVotos) {
            this.nomeChapa = nomeChapa;
            this.numeroChapa = numeroChapa;
            this.totalVotos = totalVotos;
        }

        public String getNomeChapa() {
            return nomeChapa;
        }

        public Integer getNumeroChapa() {
            return numeroChapa;
        }

        public Long getTotalVotos() {
            return totalVotos;
        }
    }
}
