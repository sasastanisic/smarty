package com.smarty.domain.major.service;

import com.smarty.domain.major.entity.Major;
import com.smarty.domain.major.repository.MajorRepository;
import com.smarty.infrastructure.mapper.MajorMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

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

}
