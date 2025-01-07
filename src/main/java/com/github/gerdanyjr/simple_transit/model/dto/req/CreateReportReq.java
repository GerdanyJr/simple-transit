package com.github.gerdanyjr.simple_transit.model.dto.req;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportReq(
        @NotBlank String summary,
        @NotBlank String description,
        @NotNull Instant timestamp,
        @NotBlank String address,
        Double latitude,
        Double longitude,
        @NotNull Integer reportTypeId) {

}
