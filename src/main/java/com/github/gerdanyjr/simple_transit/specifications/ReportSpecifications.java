package com.github.gerdanyjr.simple_transit.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.github.gerdanyjr.simple_transit.model.entity.Report;

public class ReportSpecifications {

    public static Specification<Report> buildReportSpecification(
            Integer reportTypeId,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            String address,
            String keyword) {
        Specification<Report> spec = Specification.where(null);

        if (reportTypeId != null) {
            spec = spec.and(reportTypeEquals(reportTypeId));
        }

        if (startsAt != null) {
            spec = spec.and(dateStartsAt(startsAt));
        }

        if (endsAt != null) {
            spec = spec.and(dateEndsAt(endsAt));
        }

        if (address != null && !address.isBlank()) {
            spec = spec.and(fieldContainsIgnoreCase("address", address));
        }

        if (keyword != null && !keyword.isBlank()) {
            Specification<Report> keywordInSummary = fieldContainsIgnoreCase("summary", keyword);
            Specification<Report> keywordInDescription = fieldContainsIgnoreCase("description", keyword);

            spec = spec.and(keywordInSummary.or(keywordInDescription));
        }

        return spec;
    }

    private static Specification<Report> reportTypeEquals(Integer reportTypeId) {
        return (root, query, builder) -> builder.equal(root.get("reportType").get("id"), reportTypeId);
    }

    private static Specification<Report> dateStartsAt(LocalDateTime date) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("timestamp").as(LocalDateTime.class),
                date);
    }

    private static Specification<Report> dateEndsAt(LocalDateTime date) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("timestamp").as(LocalDateTime.class), date);
    }

    private static Specification<Report> fieldContainsIgnoreCase(String field, String keyword) {
        return (root, query, builder) -> builder
                .like(builder.lower(root.get(field)), "%" + keyword.toLowerCase() + "%");
    }

}
