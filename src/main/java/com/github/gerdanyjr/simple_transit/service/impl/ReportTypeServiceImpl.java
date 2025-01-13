package com.github.gerdanyjr.simple_transit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.gerdanyjr.simple_transit.model.entity.ReportType;
import com.github.gerdanyjr.simple_transit.repository.ReportTypeRepository;
import com.github.gerdanyjr.simple_transit.service.ReportTypeService;

@Service
public class ReportTypeServiceImpl implements ReportTypeService {

    private final ReportTypeRepository reportTypeRepository;

    public ReportTypeServiceImpl(ReportTypeRepository reportTypeRepository) {
        this.reportTypeRepository = reportTypeRepository;
    }

    @Override
    public List<ReportType> findAll() {
        return reportTypeRepository.findAll();
    }

}
