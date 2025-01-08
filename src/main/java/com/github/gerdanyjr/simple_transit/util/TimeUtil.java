package com.github.gerdanyjr.simple_transit.util;

import java.time.Duration;
import java.time.Instant;

public class TimeUtil {

    public static Boolean isBeforeDaysAgo(Instant date, Integer daysBefore) {
        Instant target = Instant.now().minus(Duration.ofDays(daysBefore));
        return date.isBefore(target);
    }

}
