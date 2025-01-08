package com.github.gerdanyjr.simple_transit.model.dto.req;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportReq(
        @NotBlank String summary,
        @NotBlank String description,
        @NotNull LocalDateTime timestamp,
        @NotBlank String address,
        Double latitude,
        Double longitude,
        @NotNull Integer reportTypeId) {

}
