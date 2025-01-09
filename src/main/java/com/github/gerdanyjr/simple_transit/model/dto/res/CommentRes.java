package com.github.gerdanyjr.simple_transit.model.dto.res;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CommentRes(
                Integer id,
                String comment,
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date,
                Integer userId,
                Integer reportId) {

}
