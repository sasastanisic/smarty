package com.smarty.domain.major.service;

import com.smarty.domain.major.entity.Major;
import com.smarty.domain.major.model.MajorRequestDTO;
import com.smarty.domain.major.model.MajorResponseDTO;
import com.smarty.domain.major.model.MajorUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MajorService {

    MajorResponseDTO createMajor(MajorRequestDTO majorDTO);

    Page<MajorResponseDTO> getAllMajors(Pageable pageable);

    MajorResponseDTO getMajorById(Long id);

    Major getById(Long id);

    MajorResponseDTO updateMajor(Long id, MajorUpdateDTO majorDTO);

    void deleteMajor(Long id);

}
