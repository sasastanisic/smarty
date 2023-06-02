package com.smarty.domain.post.service;

import com.smarty.domain.post.model.PostRequestDTO;
import com.smarty.domain.post.model.PostResponseDTO;
import com.smarty.domain.post.model.PostUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postDTO);

    Page<PostResponseDTO> getAllPosts(Pageable pageable);

    PostResponseDTO getPostById(Long id);

    List<PostResponseDTO> getLatestPosts();

    PostResponseDTO updatePost(Long id, PostUpdateDTO postDTO);

    void deletePost(Long id);

}
