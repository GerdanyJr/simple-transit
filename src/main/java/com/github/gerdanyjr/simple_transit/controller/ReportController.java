package com.github.gerdanyjr.simple_transit.controller;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.PageRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ReportRes;
import com.github.gerdanyjr.simple_transit.model.entity.Report;
import com.github.gerdanyjr.simple_transit.service.ReportService;

import jakarta.validation.Valid;

@RequestMapping("/ocorrencias")
@RestController
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Report> create(
            @RequestBody @Valid CreateReportReq req,
            Principal principal) {

        Report report = reportService.create(req, principal);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(report.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportRes> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageRes<ReportRes>> findAll(
            @RequestParam(value = "reportTypeId", required = false) Integer reportTypeId,
            @RequestParam(value = "startsAt", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime startsAt,
            @RequestParam(value = "endsAt", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime endsAt,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "sortDirection", defaultValue = "ASC", required = false) String sortDirection,
            @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy) {
        return ResponseEntity.ok(reportService
                .findAll(reportTypeId,
                        startsAt,
                        endsAt,
                        address,
                        keyword,
                        page,
                        sortDirection,
                        sortBy));
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> delete(
            @PathVariable("reportId") Integer reportId,
            Principal principal) {

        reportService.delete(reportId, principal);

        return ResponseEntity
                .noContent()
                .build();
    }

}
