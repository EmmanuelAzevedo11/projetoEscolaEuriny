package com.escola.escola.services;

import com.escola.escola.enums.UserRole;
import com.escola.escola.exceptions.ChapaNaoEncontradaException;
import com.escola.escola.exceptions.VotoJaRealizadoException;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.User;
import com.escola.escola.models.Voto;
import com.escola.escola.repository.ChapaRepository;
import com.escola.escola.repository.UsersChapaRepository;
import com.escola.escola.repository.VotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final ChapaRepository chapaRepository;
    private final UsersChapaRepository usersChapaRepository;

    public VotoService(VotoRepository votoRepository,
                       ChapaRepository chapaRepository,
                       UsersChapaRepository usersChapaRepository){
        this.votoRepository = votoRepository;
        this.chapaRepository = chapaRepository;
        this.usersChapaRepository = usersChapaRepository;
    }

    @Transactional
    public void postVoto(Voto voto, User user){
        if (pertenceAChapa(user)) {
            throw new VotoJaRealizadoException("Alunos cadastrados em uma chapa não podem votar.");
        }
        if(votoRepository.existsByUserId(user.getId())){
            throw new VotoJaRealizadoException("Você já realizou o seu voto!");
        }
        if(voto == null || voto.getChapa() == null || voto.getChapa().getId() == null){
            throw new ChapaNaoEncontradaException("Selecione uma chapa válida para votar.");
        }
        Chapa chapa = chapaRepository.findById(voto.getChapa().getId())
                .orElseThrow(() -> new ChapaNaoEncontradaException("Chapa não encontrada."));

        Voto novoVoto = new Voto();
        novoVoto.setChapa(chapa);
        novoVoto.setUser(user);
        votoRepository.save(novoVoto);
    }

    public boolean jaVotou(Long userId) {
        return votoRepository.existsByUserId(userId);
    }

    public boolean pertenceAChapa(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        return usersChapaRepository.existsByUserId(user.getId())
                || user.getRole() == UserRole.ROLE_CHAPA;
    }

    public Long contarVotosPorChapa(Long chapaId) {
        Long count = votoRepository.countVotos(chapaId);
        return count != null ? count : 0L;
    }
}
