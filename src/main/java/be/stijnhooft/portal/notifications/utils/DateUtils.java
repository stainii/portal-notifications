package be.stijnhooft.portal.notifications.utils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    public static LocalDateTime now(Clock clock) {
        return LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault());
    }

    public static LocalDateTime tomorrow(Clock clock) {
        return now(clock).plusDays(1);
    }
}
