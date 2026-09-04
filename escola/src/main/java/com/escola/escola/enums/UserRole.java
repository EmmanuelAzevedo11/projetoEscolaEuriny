package com.escola.escola.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_COMUM("comum"),
    ROLE_ADMIN("admin"),
    ROLE_CHAPA("chapa");

    private String type;

    UserRole(String type){
        this.type = type;
    }
}
