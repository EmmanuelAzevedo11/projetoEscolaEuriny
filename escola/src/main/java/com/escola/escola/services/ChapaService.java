package com.escola.escola.services;

import com.escola.escola.exceptions.ChapaNaoEncontradaException;
import com.escola.escola.models.Chapa;
import com.escola.escola.repository.ChapaRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ChapaService {

    private final ChapaRepository chapaRepository;

    public ChapaService(ChapaRepository chapaRepository){
        this.chapaRepository = chapaRepository;
    }

    public List<Chapa> findChapas(){
        List<Chapa> chapas = chapaRepository.findAll();

        if(chapas.isEmpty()){
            throw new ChapaNaoEncontradaException("Chapas não encontradas");
        }

        return chapas;
    }

    public Chapa findChapa(Long id){
        Chapa chapa = chapaRepository.findById(id).orElseThrow(() ->
                new ChapaNaoEncontradaException("Chapa não encontrada"));

        return chapa;
    }

    public void saveChapa(Chapa chapa){
        chapaRepository.save(chapa);
    }

    public void putChapa(Chapa chapa){
        chapaRepository.save(chapa);
    }

}
