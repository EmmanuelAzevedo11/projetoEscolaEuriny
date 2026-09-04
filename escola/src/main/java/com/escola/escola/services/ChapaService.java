package com.escola.escola.services;

import com.escola.escola.exceptions.ChapaNaoEncontradaException;
import com.escola.escola.form.GerenciarMembrosForm;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.User;
import com.escola.escola.models.UsersChapa;
import com.escola.escola.repository.ChapaRepository;

import com.escola.escola.repository.UserRepository;
import com.escola.escola.repository.UsersChapaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ChapaService {

    private final ChapaRepository chapaRepository;
    private final UsersChapaRepository usersChapaRepository;
    private final UserRepository userRepository;

    public ChapaService(ChapaRepository chapaRepository,
                        UsersChapaRepository usersChapaRepository,
                        UserRepository userRepository){
        this.chapaRepository = chapaRepository;
        this.usersChapaRepository = usersChapaRepository;
        this.userRepository = userRepository;
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

    // Dentro do seu ChapaService.java

    @Transactional
    public void atualizarMembrosDaChapa(Long chapaId, GerenciarMembrosForm form) {
        Chapa chapa = findChapa(chapaId);

        // 1. Limpa os membros atuais (útil para o cenário de edição)
        usersChapaRepository.deleteByChapaId(chapaId);

        // 2. Itera sobre o formulário e salva os novos membros
        for (GerenciarMembrosForm.MembroChapaFormDto dto : form.getMembros()) {

            // Só salva se o usuário tiver selecionado alguém no Select2 para esse cargo
            if (dto.getUserId() != null) {
                User user = userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

                UsersChapa vinculo = new UsersChapa();
                vinculo.setChapa(chapa);
                vinculo.setUser(user);
                vinculo.setRoleChapa(dto.getRole());

                usersChapaRepository.save(vinculo);
            }
        }
    }

}
