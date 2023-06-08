package com.smarty.domain.post.service;

import com.smarty.domain.post.entity.Post;
import com.smarty.domain.post.model.PostRequestDTO;
import com.smarty.domain.post.model.PostResponseDTO;
import com.smarty.domain.post.model.PostUpdateDTO;
import com.smarty.domain.post.repository.PostRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.PostMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    Post post;

    Page<Post> posts;

    @InjectMocks
    PostServiceImpl postService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostMapperImpl postMapper;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setTitle("Test post");
        post.setDescription("This post is used for testing purposes");
        post.setCreatedAt(LocalDateTime.now());

        List<Post> postList = new ArrayList<>();
        postList.add(post);
        posts = new PageImpl<>(postList);
    }

    @Test
    void testCreatePost() {
        PostRequestDTO postRequestDTO = new PostRequestDTO("Test post", "This post is used for testing purposes");
        PostResponseDTO postResponseDTO = new PostResponseDTO(1L, "Test post", "This post is used for testing purposes", LocalDateTime.now());

        when(postMapper.toPost(postRequestDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        doReturn(postResponseDTO).when(postMapper).toPostResponseDTO(post);

        var createdPostDTO = postService.createPost(postRequestDTO);

        assertThat(postResponseDTO).isEqualTo(createdPostDTO);
    }

    @Test
    void testGetAllPosts() {
        Pageable pageable = mock(Pageable.class);
        PostResponseDTO postResponseDTO = new PostResponseDTO(1L, "Test post", "This post is used for testing purposes", LocalDateTime.now());

        when(postMapper.toPostResponseDTO(post)).thenReturn(postResponseDTO);
        var expectedPosts = posts.map(post -> postMapper.toPostResponseDTO(post));
        doReturn(posts).when(postRepository).findAll(pageable);
        var postPage = postService.getAllPosts(pageable);

        Assertions.assertEquals(expectedPosts, postPage);
    }

    @Test
    void testGetPostById() {
        PostResponseDTO postResponseDTO = new PostResponseDTO(1L, "Test post", "This post is used for testing purposes", LocalDateTime.now());

        when(postMapper.toPostResponseDTO(post)).thenReturn(postResponseDTO);
        var expectedPost = postMapper.toPostResponseDTO(post);
        doReturn(Optional.of(post)).when(postRepository).findById(1L);
        var returnedPost = postService.getPostById(1L);

        Assertions.assertEquals(expectedPost, returnedPost);
    }

    @Test
    void testGetPostById_NotFound() {
        doReturn(Optional.empty()).when(postRepository).findById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> postService.getPostById(1L));
    }

    @Test
    void testGetLatestPosts() {
        List<Post> latestPosts = List.of(post);
        PostResponseDTO postResponseDTO = new PostResponseDTO(1L, "Test post", "This post is used for testing purposes", LocalDateTime.now());

        when(postMapper.toPostResponseDTO(post)).thenReturn(postResponseDTO);
        var expectedList = latestPosts
                .stream()
                .map(postMapper::toPostResponseDTO)
                .toList();
        doReturn(latestPosts).when(postRepository).findByOrderByCreatedAtDesc();
        var returnedList = postService.getLatestPosts();

        Assertions.assertTrue(latestPosts.contains(post));
        Assertions.assertEquals(expectedList, returnedList);
    }

    @Test
    void testUpdatePost() {
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("Update post", "This post is used for testing purposes");
        PostResponseDTO postResponseDTO = new PostResponseDTO(1L, "Update post", "This post is used for testing purposes", LocalDateTime.now());

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        doCallRealMethod().when(postMapper).updatePostFromDTO(postUpdateDTO, post);
        when(postRepository.save(post)).thenReturn(post);
        doReturn(postResponseDTO).when(postMapper).toPostResponseDTO(post);

        var updatedPostDTO = postService.updatePost(1L, postUpdateDTO);

        assertThat(postResponseDTO).isEqualTo(updatedPostDTO);
    }

    @Test
    void testDeletePost() {
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> postService.deletePost(1L));
    }

    @Test
    void testDeletePost_NotFound() {
        doReturn(false).when(postRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> postService.deletePost(1L));
    }

}
