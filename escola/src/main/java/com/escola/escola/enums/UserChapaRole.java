package com.escola.escola.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum UserChapaRole {
    ROLE_PRESIDENTE("presidente"),
    ROLE_VICE_PRESIDENTE("vice_presidente"),
    ROLE_VICE_SECRETARIO("secretário"),
    ROLE_TESOUREIRO("tesoureiro"),
    ROLE_CULTURA("cultura"),
    ROLE_ESPORTE("esporte"),
    ROLE_COMUNICACAO("comunicação"),
    ROLE_EVENTOS("eventos");

    private String type;

    UserChapaRole(String type){
        this.type = type;
    }

}
