package com.smarty.domain.account.model;

import com.smarty.domain.account.enums.Role;

public record AccountResponseDTO(

        String email,
        String password,
        Role role

) {

}
