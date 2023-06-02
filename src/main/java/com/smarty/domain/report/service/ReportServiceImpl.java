package com.smarty.domain.report.service;

import com.smarty.domain.report.entity.Report;
import com.smarty.domain.report.model.ReportRequestDTO;
import com.smarty.domain.report.model.ReportResponseDTO;
import com.smarty.domain.report.model.ReportUpdateDTO;
import com.smarty.domain.report.repository.ReportRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String REPORT_NOT_EXISTS = "Report with id %d doesn't exist";

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    public ReportResponseDTO createReport(ReportRequestDTO reportDTO) {
        Report report = reportMapper.toReport(reportDTO);

        reportRepository.save(report);

        return reportMapper.toReportResponseDTO(report);
    }

    @Override
    public Page<ReportResponseDTO> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable).map(reportMapper::toReportResponseDTO);
    }

    @Override
    public ReportResponseDTO getReportById(Long id) {
        return reportMapper.toReportResponseDTO(getById(id));
    }

    private Report getById(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new NotFoundException(REPORT_NOT_EXISTS.formatted(id)));
    }

    @Override
    public ReportResponseDTO updateReport(Long id, ReportUpdateDTO reportDTO) {
        Report report = getById(id);
        reportMapper.updateReportFromDTO(reportDTO, report);

        reportRepository.save(report);

        return reportMapper.toReportResponseDTO(report);
    }

    @Override
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new NotFoundException(REPORT_NOT_EXISTS.formatted(id));
        }

        reportRepository.deleteById(id);
    }

}
