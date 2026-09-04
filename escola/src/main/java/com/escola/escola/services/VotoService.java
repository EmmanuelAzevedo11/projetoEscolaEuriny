package com.escola.escola.services;

import com.escola.escola.exceptions.VotoJaRealizadoException;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.User;
import com.escola.escola.models.Voto;
import com.escola.escola.repository.ChapaRepository;
import com.escola.escola.repository.VotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotoService {

    private final VotoRepository votoRepository;

    public VotoService(VotoRepository votoRepository){
        this.votoRepository = votoRepository;

    }

    @Transactional
    public void postVoto(Voto voto, User user){
        Voto voto1 = votoRepository.findByUserId(user.getId());
        if(voto1 != null){
            throw new VotoJaRealizadoException("Você já realizou o voto");
        }
        voto.setUser(user);
        votoRepository.save(voto);
    }


}
