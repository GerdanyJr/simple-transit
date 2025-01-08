package com.github.gerdanyjr.simple_transit.service.impl;

import java.security.Principal;

import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.UnauthorizedException;
import com.github.gerdanyjr.simple_transit.repository.ReportRepository;
import com.github.gerdanyjr.simple_transit.repository.ReportTypeRepository;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.ReportService;
import com.github.gerdanyjr.simple_transit.util.Mapper;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final UserRepository userRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ReportTypeRepository reportTypeRepository,
            UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.reportTypeRepository = reportTypeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Report create(CreateReportReq req, Principal principal) {
        ReportType reportType = reportTypeRepository
                .findById(req.reportTypeId())
                .orElseThrow(() -> new NotFoundException("ReportType não encontrado com id: " + req.reportTypeId()));

        User user = userRepository
                .findByLogin(principal.getName())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com login: " + principal.getName()));

        Report report = Mapper.fromCreateReportReqToReport(req, user, reportType);

        return reportRepository.save(report);
    }

    @Override
    public void delete(Integer reportId, Principal principal) {
        Report foundReport = reportRepository
                .findById(reportId)
                .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada com id: " + reportId));

        User foundUser = userRepository
                .findByLogin(principal.getName())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com login: " + principal.getName()));

        if (foundReport.getUser().getId() != foundUser.getId()) {
            throw new UnauthorizedException("Não é possível excluir este post");
        }

        reportRepository.delete(foundReport);
    }

}
