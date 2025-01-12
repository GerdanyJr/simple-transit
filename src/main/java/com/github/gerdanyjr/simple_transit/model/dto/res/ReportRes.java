package com.github.gerdanyjr.simple_transit.model.dto.res;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReportRes(
                Integer id,
                String summary,
                String description,
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp,
                String address,
                Double latitude,
                Double longitude,
                Integer userId,
                String username,
                Integer reportTypeId,
                String reportTypeName) {

}
