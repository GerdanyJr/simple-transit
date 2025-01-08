package com.github.gerdanyjr.simple_transit.model.dto.req;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentReq(
        @NotBlank @Size(max = 140) String comment,
        @NotNull Instant date,
        @NotNull Integer reportId) {

}
