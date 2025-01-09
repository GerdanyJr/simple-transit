package com.github.gerdanyjr.simple_transit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.github.gerdanyjr.simple_transit.model.entity.Comment;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReportRepository reportRepository;

    private User mockUser;

    private Report mockReport;

    private Comment mockComment;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 10);

        mockUser = new User(null,
                "teste",
                "teste",
                "teste",
                List.of());

        mockReport = new Report(null,
                "summary1",
                "description1",
                LocalDateTime.now(),
                "address1",
                null,
                null,
                null,
                mockUser,
                new ReportType(1, "description"));

        mockComment = new Comment(null,
                "comment",
                LocalDateTime.now(),
                mockUser, mockReport);

        userRepository.save(mockUser);
        reportRepository.save(mockReport);
        commentRepository.save(mockComment);
    }

    @Test
    @DisplayName("should return report comments")
    void givenReport_whenFindCommentById_thenReturnComment() {
        Page<Comment> page = commentRepository.findByReport(mockReport, pageable);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
    }

}
