package com.smarty.domain.report.service;

import com.smarty.domain.report.entity.Report;
import com.smarty.domain.report.model.ReportRequestDTO;
import com.smarty.domain.report.model.ReportResponseDTO;
import com.smarty.domain.report.model.ReportUpdateDTO;
import com.smarty.domain.report.repository.ReportRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.ReportMapperImpl;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    Report report;

    Page<Report> reports;

    @InjectMocks
    ReportServiceImpl reportService;

    @Mock
    ReportRepository reportRepository;

    @Mock
    ReportMapperImpl reportMapper;

    @BeforeEach
    void setUp() {
        report = new Report();
        report.setId(1L);
        report.setCourse("Test course - IT355");
        report.setTerm("Test report term");
        report.setDescription("This report is used for testing purposes");

        List<Report> reportList = new ArrayList<>();
        reportList.add(report);
        reports = new PageImpl<>(reportList);
    }

    @Test
    void testCreateReport() {
        ReportRequestDTO reportRequestDTO = new ReportRequestDTO("Test course - IT355", "Test report term", "This report is used for testing purposes");
        ReportResponseDTO reportResponseDTO = new ReportResponseDTO(1L, "Test course - IT355", "Test report term", "This report is used for testing purposes");

        when(reportMapper.toReport(reportRequestDTO)).thenReturn(report);
        when(reportRepository.save(report)).thenReturn(report);
        doReturn(reportResponseDTO).when(reportMapper).toReportResponseDTO(report);

        var createdReportDTO = reportService.createReport(reportRequestDTO);

        Assertions.assertEquals(reportResponseDTO, createdReportDTO);
    }

    @Test
    void testGetAllReports() {
        Pageable pageable = mock(Pageable.class);
        ReportResponseDTO reportResponseDTO = new ReportResponseDTO(1L, "Test course - IT355", "Test report term", "This report is used for testing purposes");

        when(reportMapper.toReportResponseDTO(report)).thenReturn(reportResponseDTO);
        var expectedReports = reports.map(report -> reportMapper.toReportResponseDTO(report));
        doReturn(reports).when(reportRepository).findAll(pageable);
        var reportPage = reportService.getAllReports(pageable);

        Assertions.assertEquals(expectedReports, reportPage);
    }

    @Test
    void testGetReportById() {
        ReportResponseDTO reportResponseDTO = new ReportResponseDTO(1L, "Test course - IT355", "Test report term", "This report is used for testing purposes");

        when(reportMapper.toReportResponseDTO(report)).thenReturn(reportResponseDTO);
        var expectedReport = reportMapper.toReportResponseDTO(report);
        doReturn(Optional.of(report)).when(reportRepository).findById(1L);
        var returnedReport = reportService.getReportById(1L);

        Assertions.assertEquals(expectedReport, returnedReport);
    }

    @Test
    void testGetReportById_NotFound() {
        doReturn(Optional.empty()).when(reportRepository).findById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> reportService.getReportById(1L));
    }

    @Test
    void testUpdateReport() {
        ReportUpdateDTO reportUpdateDTO = new ReportUpdateDTO("Update course - IT255", "Update report term", "This report is used for testing purposes");
        ReportResponseDTO reportResponseDTO = new ReportResponseDTO(1L, "Update course - IT255", "Update report term", "This report is used for testing purposes");

        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        doCallRealMethod().when(reportMapper).updateReportFromDTO(reportUpdateDTO, report);
        when(reportRepository.save(report)).thenReturn(report);
        doReturn(reportResponseDTO).when(reportMapper).toReportResponseDTO(report);

        var updatedReportDTO = reportService.updateReport(1L, reportUpdateDTO);

        Assertions.assertEquals(reportResponseDTO, updatedReportDTO);
    }

    @Test
    void testDeleteReport() {
        when(reportRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reportRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> reportService.deleteReport(1L));
    }

    @Test
    void testDeleteReport_NotFound() {
        doReturn(false).when(reportRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> reportService.deleteReport(1L));
    }

}
