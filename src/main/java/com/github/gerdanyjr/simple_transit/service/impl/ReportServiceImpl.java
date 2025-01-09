package com.github.gerdanyjr.simple_transit.service.impl;

import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.REPORT_TYPE_NOT_FOUND;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.REPORT_NOT_FOUND;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.constants.ErrorMessages;
import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.CommentRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.PageRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ReportRes;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ArgumentNotValidException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.UnauthorizedException;
import com.github.gerdanyjr.simple_transit.repository.CommentRepository;
import com.github.gerdanyjr.simple_transit.repository.ReportRepository;
import com.github.gerdanyjr.simple_transit.repository.ReportTypeRepository;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.ReportService;
import com.github.gerdanyjr.simple_transit.specifications.ReportSpecifications;
import com.github.gerdanyjr.simple_transit.util.Mapper;
import com.github.gerdanyjr.simple_transit.util.TimeUtil;
import com.github.gerdanyjr.simple_transit.util.UriUtil;

@Service
public class ReportServiceImpl implements ReportService {

        private final ReportRepository reportRepository;
        private final ReportTypeRepository reportTypeRepository;
        private final UserRepository userRepository;
        private final CommentRepository commentRepository;

        public ReportServiceImpl(ReportRepository reportRepository, ReportTypeRepository reportTypeRepository,
                        UserRepository userRepository, CommentRepository commentRepository) {
                this.reportRepository = reportRepository;
                this.reportTypeRepository = reportTypeRepository;
                this.userRepository = userRepository;
                this.commentRepository = commentRepository;
        }

        @Override
        public URI create(CreateReportReq req, Principal principal) {
                ReportType reportType = reportTypeRepository
                                .findById(req.reportTypeId())
                                .orElseThrow(() -> new NotFoundException(
                                                REPORT_TYPE_NOT_FOUND.apply(req.reportTypeId())));

                User user = userRepository
                                .findByLogin(principal.getName())
                                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.apply(principal.getName())));

                if (TimeUtil.isBeforeDaysAgo(req.timestamp(), 2)) {
                        throw new ArgumentNotValidException(ErrorMessages.REPORT_DATE_TOO_OLD);
                }

                if (req.timestamp().isAfter(LocalDateTime.now())) {
                        throw new ArgumentNotValidException(ErrorMessages.FUTURE_REPORT_DATE);
                }

                Report createdReport = reportRepository.save(
                                Mapper.fromCreateReportReqToReport(
                                                req,
                                                user,
                                                reportType));

                return UriUtil.createUri("/{id}", createdReport.getId().toString());
        }

        @Override
        public void delete(Integer reportId, Principal principal) {
                Report foundReport = reportRepository
                                .findById(reportId)
                                .orElseThrow(() -> new NotFoundException(REPORT_NOT_FOUND.apply(reportId)));

                User foundUser = userRepository
                                .findByLogin(principal.getName())
                                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.apply(principal.getName())));

                if (foundReport.getUser().getId() != foundUser.getId()) {
                        throw new UnauthorizedException(ErrorMessages.UNAUTHORIZED);
                }

                reportRepository.delete(foundReport);
        }

        @Override
        public ReportRes findById(Integer reportId) {
                Report foundReport = reportRepository
                                .findById(reportId)
                                .orElseThrow(() -> new NotFoundException(REPORT_NOT_FOUND.apply(reportId)));

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

        @Override
        public PageRes<CommentRes> findCommentsByReport(Integer reportId, Integer page) {
                Report foundReport = reportRepository
                                .findById(reportId)
                                .orElseThrow(() -> new NotFoundException(REPORT_NOT_FOUND.apply(reportId)));

                Pageable pageable = PageRequest.of(page,
                                10,
                                Direction.DESC,
                                "date");

                Page<CommentRes> commentPage = commentRepository
                                .findByReport(foundReport, pageable)
                                .map(Mapper::fromCommentToCommentRes);

                return Mapper.fromPageToPageRes(commentPage);
        }

}
