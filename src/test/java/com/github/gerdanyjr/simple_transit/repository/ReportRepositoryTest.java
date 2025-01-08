package com.github.gerdanyjr.simple_transit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.model.entity.User;
import com.github.gerdanyjr.simple_transit.specifications.ReportSpecifications;

@DataJpaTest
public class ReportRepositoryTest {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportTypeRepository reportTypeRepository;

    @Autowired
    private UserRepository userRepository;

    private User mockUser;

    private Report mockReport1;

    private Report mockReport2;

    private ReportType mockReportType1;

    private ReportType mockReportType2;

    private Pageable pageable;

    private LocalDateTime mockReport1Timestamp;

    private LocalDateTime mockReport2Timestamp;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 10);
        mockReportType1 = new ReportType(null, "reportType1");
        mockReportType2 = new ReportType(null, "reportType2");
        mockReport1Timestamp = LocalDateTime.of(2025, 01, 01, 0, 0);
        mockReport2Timestamp = LocalDateTime.of(2024, 01, 01, 0, 0);

        mockUser = new User(null,
                "teste",
                "teste",
                "teste",
                List.of());

        mockReport1 = new Report(null,
                "summary1",
                "description1",
                mockReport1Timestamp,
                "address1",
                null,
                null,
                null,
                mockUser,
                mockReportType1);

        mockReport2 = new Report(null,
                "summary2",
                "description2",
                mockReport2Timestamp,
                "address2",
                null,
                null,
                null,
                mockUser,
                mockReportType2);

        userRepository.save(mockUser);
        reportTypeRepository.save(mockReportType1);
        reportTypeRepository.save(mockReportType2);
        reportRepository.save(mockReport1);
        reportRepository.save(mockReport2);
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
        reportRepository.deleteAll();
        reportTypeRepository.deleteAll();
    }

    @Test
    @DisplayName("should filter reports by reportTypeId when is passed")
    public void givenReportTypeId_whenFindAll_thenReturnFilteredReports() {
        Specification<Report> spec = ReportSpecifications.buildReportSpecification(
                mockReportType1.getId(),
                null,
                null,
                null,
                null);

        Page<Report> page = reportRepository.findAll(spec, pageable);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(page
                .getContent()
                .get(0)
                .getReportType()
                .getId(),
                mockReport1.getId());
    }

    @Test
    @DisplayName("should filter reports by address when is passed")
    public void givenAddress_whenFindAll_thenReturnFilteredReports() {
        Specification<Report> spec = ReportSpecifications.buildReportSpecification(
                null,
                null,
                null,
                "address1",
                null);

        Page<Report> page = reportRepository.findAll(spec, pageable);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(page
                .getContent()
                .get(0)
                .getAddress(),
                mockReport1.getAddress());
    }

    @Test
    @DisplayName("should filter reports by keyword when is passed")
    public void givenKeyword_whenFindAll_thenReturnFilteredReports() {
        Specification<Report> spec = ReportSpecifications.buildReportSpecification(
                null,
                null,
                null,
                null,
                "summary1");

        Page<Report> page = reportRepository.findAll(spec, pageable);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
    }

    @Test
    @DisplayName("should filter reports by start date when is passed")
    public void givenStartsAt_whenFindAll_thenReturnFilteredReports() {
        Specification<Report> spec = ReportSpecifications.buildReportSpecification(
                null,
                mockReport1Timestamp,
                null,
                null,
                null);

        Page<Report> page = reportRepository.findAll(spec, pageable);;

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertTrue(page.getContent().get(0).getTimestamp().isAfter(mockReport2Timestamp));
    }

    @Test
    @DisplayName("should filter reports by end date when is passed")
    public void givenEndsAt_whenFindAll_thenReturnFilteredReports() {
        Specification<Report> spec = ReportSpecifications.buildReportSpecification(
                null,
                null,
                mockReport2Timestamp,
                null,
                null);

        Page<Report> page = reportRepository.findAll(spec, pageable);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
    }
}
