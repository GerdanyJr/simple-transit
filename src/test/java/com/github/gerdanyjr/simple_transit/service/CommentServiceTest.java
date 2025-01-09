package com.github.gerdanyjr.simple_transit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.REPORT_NOT_FOUND;
import static com.github.gerdanyjr.simple_transit.constants.ErrorMessages.USER_NOT_FOUND;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateCommentReq;
import com.github.gerdanyjr.simple_transit.model.entity.Comment;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.repository.CommentRepository;
import com.github.gerdanyjr.simple_transit.repository.ReportRepository;
import com.github.gerdanyjr.simple_transit.repository.UserRepository;
import com.github.gerdanyjr.simple_transit.service.impl.CommentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

        @Mock
        private ReportRepository reportRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private CommentRepository commentRepository;

        @InjectMocks
        private CommentServiceImpl commentService;

        private Report mockReport;

        private User mockUser;

        private Comment mockComment;

        private CreateCommentReq createCommentReq;

        private Principal principal;

        @BeforeAll
        static void setupMockRequest() {
                MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                mockRequest.setRequestURI("/comentarios");
                ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
                RequestContextHolder.setRequestAttributes(attributes);
        }

        @BeforeEach
        void setup() {
                principal = mock(Principal.class);

                mockUser = new User(1,
                                "Name",
                                "Login",
                                "Password",
                                List.of());

                mockReport = new Report(1,
                                "Summary",
                                "Description",
                                LocalDateTime.now(),
                                "Address",
                                -12.9714,
                                -15.9714,
                                null,
                                mockUser,
                                mock(ReportType.class));

                createCommentReq = new CreateCommentReq("Comment",
                                LocalDateTime.now(),
                                mockReport.getId());

                mockComment = new Comment(1,
                                "Comment",
                                LocalDateTime.now(),
                                mockUser,
                                mockReport);
        }

        @Test
        @DisplayName("should return created comment location when a valid comment is passed")
        void givenValidComment_whenCreate_thenReturnComment() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReport));

                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(mockUser));

                when(commentRepository.save(any(Comment.class)))
                                .thenReturn(mockComment);

                URI uri = commentService.create(createCommentReq, principal);

                assertNotNull(uri);
                assertEquals("/comentarios/1", uri.getPath());
        }

        @Test
        @DisplayName("should throw a exception when inexistent reportId is passed")
        void givenInvalidReportId_whenCreate_thenThrowNotFoundException() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.empty());

                NotFoundException e = assertThrows(NotFoundException.class,
                                () -> commentService.create(createCommentReq, principal));

                assertEquals(REPORT_NOT_FOUND.apply(createCommentReq.reportId()),
                                e.getMessage());
        }

        @Test
        @DisplayName("should throw a exception when inexistent principal is provided")
        void givenInexistentPrincipal_whenCreate_thenThrowNotFoundException() {
                String login = "login";

                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReport));

                when(principal.getName())
                                .thenReturn(login);

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.empty());

                NotFoundException e = assertThrows(NotFoundException.class,
                                () -> commentService.create(createCommentReq, principal));

                assertEquals(USER_NOT_FOUND.apply(login), e.getMessage());
        }
}
