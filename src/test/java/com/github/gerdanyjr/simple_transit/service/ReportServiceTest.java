package com.github.gerdanyjr.simple_transit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.CommentRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.PageRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ReportRes;
import com.github.gerdanyjr.simple_transit.model.entity.Comment;
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
import com.github.gerdanyjr.simple_transit.service.impl.ReportServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

        @Mock
        private ReportRepository reportRepository;

        @Mock
        private ReportTypeRepository reportTypeRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private CommentRepository commentRepository;

        @InjectMocks
        private ReportServiceImpl reportService;

        private Report mockReport;

        private ReportType mockReportType;

        private Comment mockComment;

        private User mockUser;

        private CreateReportReq req;

        private Principal principal;

        private Page<Report> mockReportsPage;

        private Page<Comment> mockCommentsPage;

        @BeforeEach
        void setup() {
                principal = mock(Principal.class);

                mockReportType = new ReportType(1, "Description");

                mockUser = new User(1,
                                "Name",
                                "Login",
                                "Password",
                                List.of());

                mockReport = new Report(null,
                                "Summary",
                                "Description",
                                LocalDateTime.now(),
                                "Address",
                                -12.9714,
                                -15.9714,
                                null,
                                mockUser,
                                mockReportType);

                mockComment = new Comment(null,
                                "Comment",
                                LocalDateTime.now(),
                                mockUser,
                                mockReport);

                req = new CreateReportReq("Summary",
                                "Description",
                                LocalDateTime.now(),
                                "Address",
                                -12.9714,
                                -15.9714,
                                1);

                mockReportsPage = new PageImpl<>(
                                List.of(mockReport, mockReport),
                                PageRequest.of(0, 10),
                                2);

                mockCommentsPage = new PageImpl<>(
                                List.of(mockComment, mockComment),
                                PageRequest.of(0, 10),
                                2);

        }

        @Test
        @DisplayName("should return created report when a valid report is passed")
        void givenValidReport_whenCreate_thenReturnCreatedReport() {
                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                when(reportTypeRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReportType));

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(mockUser));

                when(reportRepository.save(any(Report.class)))
                                .thenReturn(mockReport);

                Report result = reportService.create(req, principal);

                assertEquals(mockReport, result);
                verify(reportTypeRepository).findById(1);
                verify(userRepository).findByLogin("Login");
                verify(reportRepository).save(mockReport);
        }

        @Test
        @DisplayName("should throw a exception when a inexistent reportTypeId is passed")
        void givenInvalidReportTypeId_whenCreate_thenThrowNotFoundException() {
                when(reportTypeRepository.findById(anyInt()))
                                .thenReturn(Optional.empty());

                NotFoundException e = assertThrows(NotFoundException.class,
                                () -> {
                                        reportService.create(req, principal);
                                });

                assertEquals(e.getMessage(), "ReportType não encontrado com id: 1");
                verify(reportRepository, never()).save(any(Report.class));
        }

        @Test
        @DisplayName("should throw a exception when a inexistent principal is provided")
        void givenInvalidPrincipal_whenCreate_thenThrowNotFoundException() {
                when(reportTypeRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReportType));

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.empty());

                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                NotFoundException e = assertThrows(NotFoundException.class,
                                () -> {
                                        reportService.create(req, principal);
                                });

                assertEquals(e.getMessage(), "Usuário não encontrado com login: " + mockUser.getLogin());
                verify(reportRepository, never()).save(any(Report.class));
        }

        @Test
        @DisplayName("should delete report when valid reportId and user is provided")
        void givenValidReportIdAndUserIsOwner_whenDelete_thenCallDelete() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReport));

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(mockUser));

                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                reportService.delete(1, principal);

                verify(reportRepository, times(1))
                                .delete(mockReport);

        }

        @Test
        @DisplayName("should throw exception when a invalid reportId is provided")
        void givenInvalidReportId_whenDelete_thenThrowNotFoundException() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.empty());

                NotFoundException e = assertThrows(NotFoundException.class, () -> {
                        reportService.delete(1, principal);
                });

                assertEquals("Ocorrência não encontrada com id: 1", e.getMessage());
                verify(reportRepository, never())
                                .delete(mockReport);
        }

        @Test
        @DisplayName("should throw exception when a invalid principal is provided")
        void givenInvalidPrincipal_whenDelete_thenThrowNotFoundException() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReport));

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.empty());

                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                NotFoundException e = assertThrows(NotFoundException.class, () -> {
                        reportService.delete(1, principal);
                });

                assertEquals("Usuário não encontrado com login: " + mockUser.getLogin(), e.getMessage());
                verify(reportRepository, never())
                                .delete(mockReport);
        }

        @Test
        @DisplayName("should throw a unauthorized exception when user isn't owner")
        void givenNotOwnerUser_whenDelete_thenThrowUnauthorizedException() {
                User anotherUser = new User(2,
                                "teste2",
                                "teste2",
                                "senha",
                                List.of());

                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReport));

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(anotherUser));

                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                UnauthorizedException e = assertThrows(UnauthorizedException.class,
                                () -> reportService.delete(1, principal));

                assertEquals("Não é possível excluir este post", e.getMessage());
                verify(reportRepository, never())
                                .delete(mockReport);
        }

        @Test
        @DisplayName("should throw a argument not valid exception when event is older than 2 days")
        void givenOlderEvent_whenCreate_thenThrowArgumentNotValidException() {
                CreateReportReq invalidReq = new CreateReportReq(
                                "Summary",
                                "Description",
                                LocalDateTime.now().minus(Duration.ofDays(2)),
                                "Address",
                                -12.9714,
                                -15.9714,
                                1);

                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                when(reportTypeRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReportType));

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(mockUser));

                assertThrows(ArgumentNotValidException.class, () -> reportService.create(invalidReq, principal));
        }

        @Test
        @DisplayName("should throw a argument not valid exception when event is later than now")
        void givenFutureEvent_whenCreate_thenThrowArgumentNotValidException() {
                CreateReportReq invalidReq = new CreateReportReq(
                                "Summary",
                                "Description",
                                LocalDateTime.now().plus(Duration.ofDays(2)),
                                "Address",
                                -12.9714,
                                -15.9714,
                                1);

                when(principal.getName())
                                .thenReturn(mockUser.getLogin());

                when(reportTypeRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReportType));

                when(userRepository.findByLogin(anyString()))
                                .thenReturn(Optional.of(mockUser));

                assertThrows(ArgumentNotValidException.class, () -> reportService.create(invalidReq, principal));
        }

        @Test
        @DisplayName("should return report when a existing id is passed")
        void givenExistingId_whenFindById_thenReturnReport() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReport));

                ReportRes reportRes = reportService.findById(1);

                assertNotNull(reportRes);
                assertEquals(mockReport.getId(), reportRes.id());
        }

        @Test
        @DisplayName("should throw a not found exception when a inexistent id is passed")
        void givenInexistingId_whenFindByid_thenThrowNotFoundException() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.empty());

                NotFoundException e = assertThrows(NotFoundException.class,
                                () -> reportService.findById(1));

                assertEquals("Ocorrência não encontrada com id: 1",
                                e.getMessage());
        }

        @SuppressWarnings("unchecked")
        @Test
        @DisplayName("should return paginated reports when find all")
        void whenFindAll_thenReturnPaginatedReports() {
                when(reportRepository.findAll(
                                any(Specification.class),
                                any(Pageable.class)))
                                .thenReturn(mockReportsPage);

                PageRes<ReportRes> pageRes = reportService.findAll(null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                "asc",
                                "id");

                assertNotNull(pageRes);
                assertEquals(pageRes.data().size(), 2);
                assertTrue(pageRes.isLastPage());
        }

        @Test
        @DisplayName("should return report comments when a valid reportId is passed")
        void givenValidReportId_whenFindCommentsByReport_thenReturnCommentRes() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.of(mockReport));

                when(commentRepository.findByReport(
                                any(Report.class),
                                any(Pageable.class)))
                                .thenReturn(mockCommentsPage);

                PageRes<CommentRes> commentRes = reportService.findCommentsByReport(0, 0);

                assertNotNull(commentRes);
                assertEquals(2, commentRes.data().size());
                assertTrue(commentRes.isLastPage());
        }

        @Test
        @DisplayName("should return report comments when a valid reportId is passed")
        void givenInexistingReportId_whenFindCommentsByReport_thenThrowException() {
                when(reportRepository.findById(anyInt()))
                                .thenReturn(Optional.empty());

                NotFoundException e = assertThrows(NotFoundException.class,
                                () -> reportService.findCommentsByReport(0, 0));

                assertEquals("Ocorrência não encontrada com id: 0", e.getMessage());
        }
}
