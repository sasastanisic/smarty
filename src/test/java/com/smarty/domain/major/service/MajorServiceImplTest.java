package com.smarty.domain.major.service;

import com.smarty.domain.major.entity.Major;
import com.smarty.domain.major.model.MajorRequestDTO;
import com.smarty.domain.major.model.MajorResponseDTO;
import com.smarty.domain.major.model.MajorUpdateDTO;
import com.smarty.domain.major.repository.MajorRepository;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.MajorMapperImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MajorServiceImplTest {

    Major major;

    Page<Major> majors;

    @InjectMocks
    MajorServiceImpl majorService;

    @Mock
    MajorRepository majorRepository;

    @Mock
    MajorMapperImpl majorMapper;

    @BeforeEach
    void setUp() {
        major = new Major();
        major.setId(1L);
        major.setCode("SE");
        major.setFullName("Software engineering");
        major.setDescription("Software engineering major");
        major.setDuration(4);

        List<Major> majorList = new ArrayList<>();
        majorList.add(major);
        majors = new PageImpl<>(majorList);
    }

    @Test
    void testCreateMajor() {
        MajorRequestDTO majorRequestDTO = new MajorRequestDTO("SE", "Software engineering", "Software engineering major", 4);
        MajorResponseDTO majorResponseDTO = new MajorResponseDTO(1L, "SE", "Software engineering", "Software engineering major", 4);

        when(majorMapper.toMajor(majorRequestDTO)).thenReturn(major);
        when(majorRepository.save(major)).thenReturn(major);
        when(majorMapper.toMajorResponseDTO(major)).thenReturn(majorResponseDTO);

        var createdMajorDTO = majorService.createMajor(majorRequestDTO);

        assertThat(majorResponseDTO).isEqualTo(createdMajorDTO);
    }

    @Test
    void testMajorByCode_NotValid() {
        MajorRequestDTO majorRequestDTO = new MajorRequestDTO("SE", "Software engineering", "Software engineering major", 4);

        when(majorRepository.existsByCode("SE")).thenReturn(true);
        Assertions.assertThrows(ConflictException.class, () -> majorService.createMajor(majorRequestDTO));
    }

    @Test
    void testGetAllMajors() {
        Pageable pageable = mock(Pageable.class);
        MajorResponseDTO majorResponseDTO = new MajorResponseDTO(1L, "SE", "Software engineering", "Software engineering major", 4);
        when(majorMapper.toMajorResponseDTO(major)).thenReturn(majorResponseDTO);

        var expectedMajors = majors.map(major -> majorMapper.toMajorResponseDTO(major));
        doReturn(majors).when(majorRepository).findAll(pageable);
        var majorPage = majorService.getAllMajors(pageable);

        Assertions.assertEquals(expectedMajors, majorPage);
    }

    @Test
    void testGetMajorById() {
        MajorResponseDTO majorResponseDTO = new MajorResponseDTO(1L, "SE", "Software engineering", "Software engineering major", 4);
        when(majorMapper.toMajorResponseDTO(major)).thenReturn(majorResponseDTO);

        var expectedMajor = majorMapper.toMajorResponseDTO(major);
        doReturn(Optional.of(major)).when(majorRepository).findById(1L);
        var returnedMajor = majorService.getMajorById(1L);

        Assertions.assertEquals(expectedMajor, returnedMajor);
    }

    @Test
    void testGetMajorById_NotFound() {
        doReturn(Optional.empty()).when(majorRepository).findById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> majorService.getMajorById(1L));
    }

    @Test
    void testMajorExists() {
        doReturn(true).when(majorRepository).existsById(1L);
        Assertions.assertDoesNotThrow(() -> majorService.existsById(1L));
        verify(majorRepository, times(1)).existsById(1L);
    }

    @Test
    void testMajorExists_NotFound() {
        doReturn(false).when(majorRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> majorService.existsById(1L));
    }

    @Test
    void testUpdateMajor() {
        MajorUpdateDTO majorUpdateDTO = new MajorUpdateDTO("Software engineering", "Software engineering major", 5);
        MajorResponseDTO majorResponseDTO = new MajorResponseDTO(1L, "SE", "Software engineering", "Software engineering major", 5);

        when(majorRepository.findById(1L)).thenReturn(Optional.of(major));
        doCallRealMethod().when(majorMapper).updateMajorFromDTO(majorUpdateDTO, major);
        when(majorRepository.save(major)).thenReturn(major);
        when(majorMapper.toMajorResponseDTO(major)).thenReturn(majorResponseDTO);

        var updatedMajorDTO = majorService.updateMajor(1L, majorUpdateDTO);

        assertThat(majorResponseDTO).isEqualTo(updatedMajorDTO);
    }

    @Test
    void testDeleteMajor() {
        when(majorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(majorRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> majorService.deleteMajor(1L));
    }

    @Test
    void testDeleteMajor_NotFound() {
        doReturn(false).when(majorRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> majorService.deleteMajor(1L));
    }

}
