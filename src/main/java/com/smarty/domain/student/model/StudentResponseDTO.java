package com.smarty.domain.student.model;

import com.smarty.domain.account.model.AccountResponseDTO;

public record StudentResponseDTO(

        Long id,
        String name,
        String surname,
        int index,
        int year,
        int semester,
        double averageGrade,
        AccountResponseDTO account

) {

}
