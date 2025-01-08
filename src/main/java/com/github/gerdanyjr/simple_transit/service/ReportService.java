package com.github.gerdanyjr.simple_transit.service;

import java.security.Principal;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.entity.Report;

public interface ReportService {
    Report create(CreateReportReq req, Principal principal);

    void delete(Integer reportId, Principal principal);
}
