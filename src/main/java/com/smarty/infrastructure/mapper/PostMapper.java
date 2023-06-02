package com.smarty.infrastructure.mapper;

import com.smarty.domain.post.entity.Post;
import com.smarty.domain.post.model.PostRequestDTO;
import com.smarty.domain.post.model.PostResponseDTO;
import com.smarty.domain.post.model.PostUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostRequestDTO postRequestDTO);

    PostResponseDTO toPostResponseDTO(Post post);

    void updatePostFromDTO(PostUpdateDTO postUpdateDTO, @MappingTarget Post post);

}
