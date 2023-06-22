package com.smarty.domain.student.model;

import com.smarty.domain.account.model.AccountResponseDTO;
import com.smarty.domain.major.model.MajorResponseDTO;
import com.smarty.domain.status.entity.Status;

public record StudentResponseDTO(

        Long id,
        String name,
        String surname,
        int index,
        int year,
        int semester,
        MajorResponseDTO major,
        Status status,
        AccountResponseDTO account

) {

}
