package com.github.gerdanyjr.simple_transit.util;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateCommentReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.PageRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ReportRes;
import com.github.gerdanyjr.simple_transit.model.entity.Comment;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;

public class Mapper {

    public static User fromRegisterReqToUser(RegisterUserReq req, BCryptPasswordEncoder encoder) {
        User user = new User();
        user.setLogin(req.login());
        user.setPassword(encoder.encode(req.password()));
        user.setName(req.name());

        return user;
    }

    public static Report fromCreateReportReqToReport(CreateReportReq req, User user, ReportType reportType) {
        Report report = new Report();
        report.setSummary(req.summary());
        report.setDescription(req.description());
        report.setTimestamp(req.timestamp());
        report.setAddress(req.address());
        report.setLatitude(req.latitude());
        report.setLongitude(req.longitude());
        report.setUser(user);
        report.setReportType(reportType);

        return report;
    }

    public static Comment fromCreateCommentReqToComment(CreateCommentReq req, User user, Report report) {
        Comment comment = new Comment();
        comment.setComment(req.comment());
        comment.setDate(req.date());
        comment.setUser(user);
        comment.setReport(report);

        return comment;
    }

    public static ReportRes fromReportToReportRes(Report report) {
        return new ReportRes(
                report.getId(),
                report.getSummary(),
                report.getDescription(),
                report.getTimestamp(),
                report.getAddress(),
                report.getLatitude(),
                report.getLongitude(),
                report.getUser().getId(),
                report.getReportType().getId());
    }

    public static <T> PageRes<T> fromPageToPageRes(Page<T> page) {
        return new PageRes<>(page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.isLast());
    }

}
