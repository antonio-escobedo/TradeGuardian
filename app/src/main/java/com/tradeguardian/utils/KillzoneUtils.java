package com.tradeguardian.utils;

import java.time.LocalTime;
import java.time.ZoneId;

public class KillzoneUtils {

    public static Killzone getCurrentKillzone() {

        LocalTime ny = LocalTime.now(ZoneId.of("America/New_York"));

        // London Killzone: 01:00 - 05:00
        if (!ny.isBefore(LocalTime.of(1, 0)) &&
                ny.isBefore(LocalTime.of(5, 0))) {
            return Killzone.LONDON;
        }

        // New York Killzone: 07:00 - 10:00
        if (!ny.isBefore(LocalTime.of(7, 0)) &&
                ny.isBefore(LocalTime.of(10, 0))) {
            return Killzone.NEW_YORK;
        }

        // Asia Killzone: 19:00 - 00:00
        if (!ny.isBefore(LocalTime.of(19, 0)) ||
                ny.isBefore(LocalTime.of(0, 0))) {
            return Killzone.ASIA;
        }

        return Killzone.NONE;
    }

    private static boolean inRange(LocalTime t, int start, int end) {
        return !t.isBefore(LocalTime.of(start, 0))
                && t.isBefore(LocalTime.of(end, 0));
    }

    public enum Killzone {
        LONDON,
        NEW_YORK,
        ASIA,
        NONE
    }
}
