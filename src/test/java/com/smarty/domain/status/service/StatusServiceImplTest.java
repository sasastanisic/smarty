package com.smarty.domain.status.service;

import com.smarty.domain.status.entity.Status;
import com.smarty.domain.status.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
