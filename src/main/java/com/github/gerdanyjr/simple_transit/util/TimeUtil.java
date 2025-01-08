package com.github.gerdanyjr.simple_transit.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtil {

    public static Boolean isBeforeDaysAgo(LocalDateTime date, Integer daysBefore) {
        LocalDateTime target = LocalDateTime.now().minus(Duration.ofDays(daysBefore));
        return date.isBefore(target);
    }

}
