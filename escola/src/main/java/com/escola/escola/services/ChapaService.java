package com.escola.escola.services;

import com.escola.escola.enums.UserChapaRole;
import com.escola.escola.exceptions.ChapaNaoEncontradaException;
import com.escola.escola.form.GerenciarMembrosForm;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.User;
import com.escola.escola.models.UsersChapa;
import com.escola.escola.repository.ChapaRepository;
import com.escola.escola.repository.UserRepository;
import com.escola.escola.repository.UsersChapaRepository;
import com.escola.escola.repository.VotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ChapaService {

    private final ChapaRepository chapaRepository;
    private final UsersChapaRepository usersChapaRepository;
    private final UserRepository userRepository;
    private final VotoRepository votoRepository;

    public ChapaService(ChapaRepository chapaRepository,
                        UsersChapaRepository usersChapaRepository,
                        UserRepository userRepository,
                        VotoRepository votoRepository){
        this.chapaRepository = chapaRepository;
        this.usersChapaRepository = usersChapaRepository;
        this.userRepository = userRepository;
        this.votoRepository = votoRepository;
    }

    public List<Chapa> findChapas(){
        return chapaRepository.findAll();
    }

    public Chapa findChapa(Long id){
        return chapaRepository.findById(id).orElseThrow(() ->
                new ChapaNaoEncontradaException("Chapa não encontrada"));
    }

    public String converterLogoParaBase64(MultipartFile logoFile) throws IOException {
        if (logoFile == null || logoFile.isEmpty()) {
            return null;
        }
        String contentType = logoFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            contentType = "image/png";
        }
        String base64 = Base64.getEncoder().encodeToString(logoFile.getBytes());
        return "data:" + contentType + ";base64," + base64;
    }

    public Chapa saveChapa(Chapa chapa){
        return chapaRepository.save(chapa);
    }

    @Transactional
    public void putChapa(Chapa chapaDados){
        Chapa existente = findChapa(chapaDados.getId());
        existente.setName(chapaDados.getName());
        existente.setCoordenador(chapaDados.getCoordenador());
        existente.setNumber(chapaDados.getNumber());
        existente.setDescription(chapaDados.getDescription());
        existente.setObjective(chapaDados.getObjective());
        if (chapaDados.getLogo() != null && !chapaDados.getLogo().isBlank()) {
            existente.setLogo(chapaDados.getLogo());
        }
        chapaRepository.save(existente);
    }

    @Transactional
    public void deleteChapa(Long id){
        Chapa existente = findChapa(id);
        votoRepository.deleteByChapaId(existente.getId());
        usersChapaRepository.deleteByChapaId(existente.getId());
        chapaRepository.deleteById(existente.getId());
    }

    @Transactional
    public void atualizarMembrosDaChapa(Long chapaId, GerenciarMembrosForm form) {
        Chapa chapa = findChapa(chapaId);

        // 1. Limpa os membros atuais (útil para o cenário de edição)
        usersChapaRepository.deleteByChapaId(chapaId);

        if (form == null || form.getMembros() == null) {
            return;
        }

        // 2. Itera sobre o formulário e salva os novos membros
        for (GerenciarMembrosForm.MembroChapaFormDto dto : form.getMembros()) {
            if (dto.getUserId() != null && dto.getRole() != null) {
                User user = userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

                UsersChapa vinculo = new UsersChapa();
                vinculo.setChapa(chapa);
                vinculo.setUser(user);
                vinculo.setRoleChapa(dto.getRole());

                usersChapaRepository.save(vinculo);

                // Se a chapa ainda não tiver coordenador preenchido manualmente e o cargo for Presidente, define-o
                if ((chapa.getCoordenador() == null || chapa.getCoordenador().isBlank())
                        && dto.getRole() == UserChapaRole.ROLE_PRESIDENTE) {
                    chapa.setCoordenador(user.getUsername());
                    chapaRepository.save(chapa);
                }
            }
        }
    }
}
