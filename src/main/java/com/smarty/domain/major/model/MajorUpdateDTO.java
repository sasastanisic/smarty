package com.smarty.domain.major.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MajorUpdateDTO(

        @NotBlank(message = "Full name of major can't be blank")
        String fullName,

        @NotBlank(message = "Major description can't be blank")
        String description,

        @Min(value = 3, message = "Value of duration is minimum 3")
        @Max(value = 5, message = "Value of duration is maximum 5")
        int duration

) {

}
