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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chapas")
public class ChapaController {

    private final ChapaService chapaService;
    private final UsersChapaRepository usersChapaRepository;
    private final UserService userService;

    public ChapaController(ChapaService chapaService,
                           UsersChapaRepository usersChapaRepository,
                           UserService userService){
        this.chapaService = chapaService;
        this.usersChapaRepository = usersChapaRepository;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("chapas/gerenciar");
        try {
            List<Chapa> chapas = chapaService.findChapas();
            modelAndView.addObject("chapas", chapas);
        } catch (Exception ex){
            modelAndView.addObject("mensagemErro", ex.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/criar")
    public ModelAndView criar(){
        ModelAndView modelAndView = new ModelAndView("chapas/criar");
        modelAndView.addObject("chapa", new Chapa());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getChapa(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try {
            Chapa chapa = chapaService.findChapa(id);
            ModelAndView modelAndView = new ModelAndView("chapas/details");
            modelAndView.addObject("chapa", chapa);
            return modelAndView;
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
            return new ModelAndView("redirect:/chapas");
        }
    }

    @PostMapping("/salvar")
    public ModelAndView postChapa(@Valid @ModelAttribute("chapa") Chapa chapa,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("chapas/criar");
            mv.addObject("chapa", chapa);
            mv.addObject("mensagemErro", "Verifique os campos preenchidos (o nome deve ter ao menos 3 caracteres).");
            return mv;
        }

        try {
            Chapa savedChapa = chapaService.saveChapa(chapa);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Chapa criada com sucesso!");
            return new ModelAndView("redirect:/chapas/" + savedChapa.getId() + "/membros");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não foi possível criar a chapa: " + e.getMessage());
            return new ModelAndView("redirect:/chapas/criar");
        }
    }

    @PostMapping("/{id}/salvar")
    public ModelAndView putChapa(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("chapa") Chapa chapa,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        chapa.setId(id);
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("chapas/details");
            mv.addObject("chapa", chapa);
            mv.addObject("mensagemErro", "Verifique os campos preenchidos (o nome deve ter ao menos 3 caracteres).");
            return mv;
        }
        try {
            chapaService.putChapa(chapa);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Chapa atualizada com sucesso!");
            return new ModelAndView("redirect:/chapas");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar chapa: " + e.getMessage());
            return new ModelAndView("redirect:/chapas/" + id);
        }
    }

    @PostMapping("/{id}/excluir")
    public ModelAndView deleteChapa(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            chapaService.deleteChapa(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Chapa excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir chapa: " + e.getMessage());
        }
        return new ModelAndView("redirect:/chapas");
    }

    @GetMapping("/{id}/membros")
    public ModelAndView gerenciarMembros(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("chapas/gerenciarMembros");
        Chapa chapa = chapaService.findChapa(id);

        GerenciarMembrosForm form = new GerenciarMembrosForm();
        List<UsersChapa> membrosAtuais = usersChapaRepository.findByChapaId(id);

        for (UserChapaRole role : UserChapaRole.values()) {
            GerenciarMembrosForm.MembroChapaFormDto dto = new GerenciarMembrosForm.MembroChapaFormDto();
            dto.setRole(role);

            membrosAtuais.stream()
                    .filter(m -> m.getRoleChapa() == role)
                    .findFirst()
                    .ifPresent(m -> dto.setUserId(m.getUser().getId()));

            form.getMembros().add(dto);
        }

        mv.addObject("chapa", chapa);
        mv.addObject("form", form);
        mv.addObject("todosUsuarios", userService.findUsers());
        return mv;
    }

    @PostMapping("/{id}/membros")
    public ModelAndView salvarMembros(@PathVariable("id") Long id,
                                      @ModelAttribute("form") GerenciarMembrosForm form,
                                      RedirectAttributes redirectAttributes) {
        try {
            chapaService.atualizarMembrosDaChapa(id, form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Membros da chapa atualizados com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar membros: " + e.getMessage());
        }
        return new ModelAndView("redirect:/chapas");
    }
}
