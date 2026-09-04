package com.escola.escola.controller;

import com.escola.escola.enums.UserChapaRole;
import com.escola.escola.form.GerenciarMembrosForm;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.UsersChapa;
import com.escola.escola.repository.UsersChapaRepository;
import com.escola.escola.services.ChapaService;
import com.escola.escola.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chapas")
public class ChapaController {

    ChapaService chapaService;
    UsersChapaRepository usersChapaRepository;
    UserService userService;

    public ChapaController(ChapaService chapaService,
                           UsersChapaRepository usersChapaRepository,
                           UserService userService){
        this.chapaService = chapaService;
        this.usersChapaRepository = usersChapaRepository;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("chapas/lista");
        try {
            List<Chapa> chapas = chapaService.findChapas();
            modelAndView.addObject("chapas", chapas);
        } catch (Exception ex){
            modelAndView.addObject("mensagemErro", ex.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getChapa(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("chapas/details");
        try {
            Chapa chapa = chapaService.findChapa(id);
            modelAndView.addObject("chapa", chapa);
        } catch (Exception ex) {
            modelAndView.addObject("mensagemErro", ex.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/salvar")
    public ModelAndView postChapa(@Valid  @ModelAttribute Chapa chapa){
        chapaService.saveChapa(chapa);

        return new ModelAndView("chapa:/chapas");
    }

    @PutMapping("/{id}")
    public ModelAndView putChapa(@PathVariable("id") Long id, @Valid @ModelAttribute Chapa chapa ){
        chapa.setId(id);
        chapaService.putChapa(chapa);
        return new ModelAndView("chapa:/chapas");
    }

    //relação de membros
    @GetMapping("/{id}/membros")
    public ModelAndView gerenciarMembros(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("chapas/membros");
        Chapa chapa = chapaService.findChapa(id);

        GerenciarMembrosForm form = new GerenciarMembrosForm();
        List<UsersChapa> membrosAtuais = usersChapaRepository.findByChapaId(id);

        // Cria um campo no formulário para CADA cargo que existe no seu Enum
        for (UserChapaRole role : UserChapaRole.values()) {
            GerenciarMembrosForm.MembroChapaFormDto dto = new GerenciarMembrosForm.MembroChapaFormDto();
            dto.setRole(role);

            // Se já existir alguém nesse cargo (edição), preenche o ID para o HTML mostrar selecionado
            membrosAtuais.stream()
                    .filter(m -> m.getRoleChapa() == role)
                    .findFirst()
                    .ifPresent(m -> dto.setUserId(m.getUser().getId()));

            form.getMembros().add(dto);
        }

        mv.addObject("chapa", chapa);
        mv.addObject("form", form);
        mv.addObject("todosUsuarios", userService.findUsers()); // Para o Select2
        return mv;
    }

    @PostMapping("/{id}/membros")
    public ModelAndView salvarMembros(@PathVariable("id") Long id,
                                      @ModelAttribute("form") GerenciarMembrosForm form,
                                      RedirectAttributes redirectAttributes) {

        chapaService.atualizarMembrosDaChapa(id, form);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Membros atualizados com sucesso!");
        return new ModelAndView("redirect:/chapas");
    }

}
