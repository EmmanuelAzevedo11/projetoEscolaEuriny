package com.escola.escola.dto;

import com.escola.escola.enums.UserChapaRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembroChapaFormDto {
    private Long userId;
    private UserChapaRole role;
}
