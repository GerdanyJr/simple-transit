package com.github.gerdanyjr.simple_transit.service;

import java.security.Principal;
import java.time.LocalDateTime;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.PageRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ReportRes;
import com.github.gerdanyjr.simple_transit.model.entity.Report;

public interface ReportService {
    Report create(CreateReportReq req, Principal principal);

    void delete(Integer reportId, Principal principal);

    ReportRes findById(Integer reportId);

    PageRes<ReportRes> findAll(Integer reportTypeId,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            String address,
            String keyword,
            Integer page,
            String sortDirection,
            String sortBy);
}
