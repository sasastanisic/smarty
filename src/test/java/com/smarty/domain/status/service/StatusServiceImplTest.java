package com.smarty.domain.status.service;

import com.smarty.domain.status.entity.Status;
import com.smarty.domain.status.repository.StatusRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServiceImplTest {

    Status status;

    @InjectMocks
    StatusServiceImpl statusService;

    @Mock
    StatusRepository statusRepository;

    @BeforeEach
    void setUp() {
        status = new Status();
        status.setId(1L);
        status.setType("Traditional");
    }

    @Test
    void testGetStatusById() {
        doReturn(Optional.ofNullable(status)).when(statusRepository).findById(1L);
        Assertions.assertEquals(statusService.getStatusById(1L), status);
    }

    @Test
    void testGetStatusById_NotFound() {
        doReturn(Optional.empty()).when(statusRepository).findById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> statusService.getStatusById(1L));
    }

    @Test
    void testStatusExists() {
        doReturn(true).when(statusRepository).existsById(1L);
        Assertions.assertDoesNotThrow(() -> statusService.existsById(1L));
        verify(statusRepository, times(1)).existsById(1L);
    }

    @Test
    void testStatusExists_NotFound() {
        doReturn(false).when(statusRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> statusService.existsById(1L));
    }

}
