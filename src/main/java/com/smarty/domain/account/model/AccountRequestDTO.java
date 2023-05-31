package com.smarty.domain.account.model;

import com.smarty.domain.account.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequestDTO(

        @NotBlank(message = "Email can't be blank")
        @Email(message = "Email should contain '@' and '.'")
        String email,

        @NotBlank(message = "Password can't be blank")
        String password,

        @NotNull(message = "Role can't be null")
        Role role

) {

}
