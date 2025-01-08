package com.github.gerdanyjr.simple_transit.model.dto.res;

import java.time.Instant;

public record ReportRes(
        Integer id,
        String summary,
        String description,
        Instant timestamp,
        String address,
        Double latitude,
        Double longitude,
        Integer userId,
        Integer reportTypeId) {

}
