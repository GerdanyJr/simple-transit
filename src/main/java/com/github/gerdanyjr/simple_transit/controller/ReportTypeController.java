package com.github.gerdanyjr.simple_transit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.service.ReportTypeService;

@RestController
@RequestMapping("/tipo-ocorrencias")
public class ReportTypeController {

    private final ReportTypeService reportTypeService;

    public ReportTypeController(ReportTypeService reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ReportType>> findAll() {
        return ResponseEntity.ok(reportTypeService.findAll());
    }
}
