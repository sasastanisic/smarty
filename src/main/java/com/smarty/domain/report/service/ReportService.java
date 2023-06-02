package com.smarty.domain.report.service;

import com.smarty.domain.report.model.ReportRequestDTO;
import com.smarty.domain.report.model.ReportResponseDTO;
import com.smarty.domain.report.model.ReportUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    ReportResponseDTO createReport(ReportRequestDTO reportDTO);

    Page<ReportResponseDTO> getAllReports(Pageable pageable);

    ReportResponseDTO getReportById(Long id);

    ReportResponseDTO updateReport(Long id, ReportUpdateDTO reportDTO);

    void deleteReport(Long id);

}
