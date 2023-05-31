package com.smarty.domain.professor.model;

import com.smarty.domain.account.model.AccountResponseDTO;

public record ProfessorResponseDTO(

        Long id,
        String name,
        String surname,
        int yearsOfExperience,
        AccountResponseDTO account

) {

}
