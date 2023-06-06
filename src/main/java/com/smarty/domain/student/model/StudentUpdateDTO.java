package com.smarty.domain.student.model;

import com.smarty.domain.account.model.AccountUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentUpdateDTO(

        @NotBlank(message = "Name can't be blank")
        String name,

        @NotBlank(message = "Surname can't be blank")
        String surname,

        @Min(value = 1, message = "Minimum year for student is 1")
        @Max(value = 4, message = "Maximum year for student is 4")
        int year,

        @Min(value = 1, message = "Value of semester is minimum 1")
        @Max(value = 8, message = "Value of semester is maximum 8")
        int semester,

        @NotNull(message = "Major can't be null")
        Long majorId,

        @NotNull(message = "Study status can't be null")
        Long statusId,

        @Valid
        AccountUpdateDTO account

) {

}
