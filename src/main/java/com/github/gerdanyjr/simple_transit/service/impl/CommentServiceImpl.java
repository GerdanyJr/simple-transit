package com.github.gerdanyjr.simple_transit.service.impl;

import java.security.Principal;

import org.springframework.stereotype.Service;

import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.REPORT_NOT_FOUND;
import com.github.gerdanyjr.simple_transit.model.dto.req.CreateCommentReq;
import com.github.gerdanyjr.simple_transit.model.entity.Comment;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.repository.CommentRepository;
import com.github.gerdanyjr.simple_transit.repository.ReportRepository;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.CommentService;
import com.github.gerdanyjr.simple_transit.util.Mapper;

@Service
public class CommentServiceImpl implements CommentService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(ReportRepository reportRepository, UserRepository userRepository,
            CommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment create(CreateCommentReq req, Principal principal) {
        Report foundReport = reportRepository
                .findById(req.reportId())
                .orElseThrow(() -> new NotFoundException(REPORT_NOT_FOUND.apply(req.reportId())));

        User foundUser = userRepository
                .findByLogin(principal.getName())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.apply(principal.getName())));

        Comment comment = Mapper.fromCreateCommentReqToComment(req, foundUser, foundReport);

        return commentRepository.save(comment);

    }

}
