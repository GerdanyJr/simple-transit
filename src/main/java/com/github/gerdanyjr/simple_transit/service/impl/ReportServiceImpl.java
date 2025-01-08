package com.github.gerdanyjr.simple_transit.service.impl;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.PageRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ReportRes;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ArgumentNotValidException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.UnauthorizedException;
import com.github.gerdanyjr.simple_transit.repository.ReportRepository;
import com.github.gerdanyjr.simple_transit.repository.ReportTypeRepository;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.ReportService;
import com.github.gerdanyjr.simple_transit.specifications.ReportSpecifications;
import com.github.gerdanyjr.simple_transit.util.Mapper;
import com.github.gerdanyjr.simple_transit.util.TimeUtil;

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
                                .orElseThrow(() -> new NotFoundException(
                                                "ReportType não encontrado com id: " + req.reportTypeId()));

                User user = userRepository
                                .findByLogin(principal.getName())
                                .orElseThrow(() -> new NotFoundException(
                                                "Usuário não encontrado com login: " + principal.getName()));

                if (TimeUtil.isBeforeDaysAgo(req.timestamp(), 2)) {
                        throw new ArgumentNotValidException(
                                        "Não é possível cadastrar eventos que ocorreram a mais de 2 dias");
                }

                Report report = Mapper.fromCreateReportReqToReport(req, user, reportType);

                return reportRepository.save(report);
        }

        @Override
        public void delete(Integer reportId, Principal principal) {
                Report foundReport = reportRepository
                                .findById(reportId)
                                .orElseThrow(() -> new NotFoundException(
                                                "Ocorrência não encontrada com id: " + reportId));

                User foundUser = userRepository
                                .findByLogin(principal.getName())
                                .orElseThrow(() -> new NotFoundException(
                                                "Usuário não encontrado com login: " + principal.getName()));

                if (foundReport.getUser().getId() != foundUser.getId()) {
                        throw new UnauthorizedException("Não é possível excluir este post");
                }

                reportRepository.delete(foundReport);
        }

        @Override
        public ReportRes findById(Integer reportId) {
                Report foundReport = reportRepository
                                .findById(reportId)
                                .orElseThrow(() -> new NotFoundException(
                                                "Ocorrência não encontrada com id: " + reportId));

                return Mapper.fromReportToReportRes(foundReport);
        }

        @Override
        public PageRes<ReportRes> findAll(Integer reportTypeId,
                        LocalDateTime startsAt,
                        LocalDateTime endsAt,
                        String address,
                        String keyword,
                        Integer page,
                        String sortDirection,
                        String sortBy) {
                Specification<Report> spec = ReportSpecifications
                                .buildReportSpecification(
                                                reportTypeId,
                                                startsAt,
                                                endsAt,
                                                address,
                                                keyword);

                Pageable pageable = PageRequest.of(page,
                                10,
                                Sort.Direction.fromString(sortDirection),
                                sortBy);

                Page<ReportRes> reportPage = reportRepository
                                .findAll(spec, pageable)
                                .map(Mapper::fromReportToReportRes);

                return Mapper.fromPageToPageRes(reportPage);
        }

}
