package com.smarty.domain.account.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "Email can't be blank")
        @Email(message = "Email should contain '@' and '.'")
        String email,

        @NotBlank(message = "Password can't be blank")
        String password

) {

}
