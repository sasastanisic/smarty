package com.smarty.domain.report.model;

import jakarta.validation.constraints.NotBlank;

public record ReportRequestDTO(

        @NotBlank(message = "Course can't be blank")
        String course,

        @NotBlank(message = "Term can't be blank")
        String term,

        @NotBlank(message = "Description can't be blank")
        String description

) {

}
