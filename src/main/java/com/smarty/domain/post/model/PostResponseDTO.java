package com.smarty.domain.post.model;

import java.time.LocalDateTime;

public record PostResponseDTO(

        Long id,
        String title,
        String description,
        LocalDateTime createdAt

) {

}
