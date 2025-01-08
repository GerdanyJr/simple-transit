package com.github.gerdanyjr.simple_transit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.model.exception.impl.ArgumentNotValidException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.NotFoundException;
import com.github.gerdanyjr.simple_transit.model.exception.impl.UnauthorizedException;
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

        @InjectMocks
        private ReportServiceImpl reportService;

        private Report mockReport;

        private ReportType mockReportType;

        private User mockUser;

        private CreateReportReq req;

        private Principal principal;

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
                                Instant.now(),
                                "Address",
                                -12.9714,
                                -15.9714,
                                null,
                                mockUser,
                                mockReportType);

                req = new CreateReportReq("Summary",
                                "Description",
                                Instant.now(),
                                "Address",
                                -12.9714,
                                -15.9714,
                                1);
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
                                Instant.now().minus(Duration.ofDays(2)),
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
}
