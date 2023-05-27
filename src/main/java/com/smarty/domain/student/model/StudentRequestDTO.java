package com.smarty.domain.student.model;

import com.smarty.domain.account.model.AccountRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StudentRequestDTO(

        @NotBlank(message = "Name can't be blank")
        String name,

        @NotBlank(message = "Surname can't be blank")
        String surname,

        @Min(value = 1, message = "Index must be greater than 0")
        int index,

        @Min(value = 1, message = "Minimum year for student is 1")
        @Max(value = 4, message = "Maximum year for student is 4")
        int year,

        @Min(value = 1, message = "Value of semester is minimum 1")
        @Max(value = 8, message = "Value of semester is maximum 8")
        int semester,

        @Valid
        AccountRequestDTO account

) {

}
