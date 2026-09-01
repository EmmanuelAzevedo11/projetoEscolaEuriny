package com.escola.escola.controller;

import com.escola.escola.models.Chapa;
import com.escola.escola.services.ChapaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/chapas")
public class ChapaController {

    ChapaService chapaService;

    public ChapaController(ChapaService chapaService){
        this.chapaService = chapaService;
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

}
