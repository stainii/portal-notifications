package be.stijnhooft.portal.notifications.utils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    public static LocalDateTime getCurrentTimeAtZeroSeconds(Clock clock) {
        return clock.instant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .withSecond(0);
    }

    public static LocalDateTime getCurrentTimePlusOneMinuteAtZeroSeconds(Clock clock) {
        return clock.instant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .plusMinutes(1)
            .withSecond(0);
    }
}
