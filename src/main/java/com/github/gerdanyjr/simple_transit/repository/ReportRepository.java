package com.github.gerdanyjr.simple_transit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.gerdanyjr.simple_transit.model.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    Page<Report> findAll(Specification<Report> spec,Pageable pageable);
}
