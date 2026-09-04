package com.escola.escola.form;

import com.escola.escola.dto.MembroChapaFormDto;
import com.escola.escola.enums.UserChapaRole;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

import java.util.ArrayList;

@Getter
@Setter
public class GerenciarMembrosForm {

    private List<MembroChapaFormDto> membros = new ArrayList<>();

    @Getter
    @Setter
    public static class MembroChapaFormDto {
        private UserChapaRole role;
        private Long userId;
    }
}
