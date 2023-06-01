package com.smarty.domain.activity.model;

import jakarta.validation.constraints.Min;

public record ActivityUpdateDTO(

        @Min(value = 0, message = "Number of points can't be negative")
        double points,

        String comment

) {

}
