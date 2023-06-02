package com.smarty.infrastructure.mapper;

import com.smarty.domain.report.entity.Report;
import com.smarty.domain.report.model.ReportRequestDTO;
import com.smarty.domain.report.model.ReportResponseDTO;
import com.smarty.domain.report.model.ReportUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    Report toReport(ReportRequestDTO reportRequestDTO);

    ReportResponseDTO toReportResponseDTO(Report report);

    void updateReportFromDTO(ReportUpdateDTO reportUpdateDTO, @MappingTarget Report report);

}
