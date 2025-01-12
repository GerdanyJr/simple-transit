package com.github.gerdanyjr.simple_transit.model.dto.res;

import java.util.List;

public record PageRes<T>(
                List<T> data,
                Integer currentPage,
                Integer pageNumber,
                Long totalElements,
                Integer numberOfElements,
                Boolean isLastPage) {

}
