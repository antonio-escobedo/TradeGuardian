package com.tradeguardian.utils;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

public class KillzoneManager {

    // Zona horaria de referencia: New York (EST/EDT)
    private static final ZoneId NY_ZONE = ZoneId.of("America/New_York");

    // Killzones en horario de NY
    private static final LocalTime[] KILLZONES = {
            LocalTime.of(1, 0),  // Inicio LDN Killzone 1
            LocalTime.of(5, 0),  // Fin LDN Killzone 1
            LocalTime.of(7, 0),  // Inicio NY Killzone
            LocalTime.of(10, 0), // Fin NY Killzone
            LocalTime.of(19, 0), // Inicio LDN Killzone 2
            LocalTime.of(0, 0)   // Fin LDN Killzone 2
    };

    public static class KillzoneInfo {
        public boolean isInKillzone;
        public String killzoneName;
        public String motivationalPhrase;

        public KillzoneInfo(boolean isInKillzone, String killzoneName, String motivationalPhrase) {
            this.isInKillzone = isInKillzone;
            this.killzoneName = killzoneName;
            this.motivationalPhrase = motivationalPhrase;
        }
    }

    public static KillzoneInfo checkCurrentKillzone() {
        ZonedDateTime nyTime = ZonedDateTime.now(NY_ZONE);
        LocalTime currentTime = nyTime.toLocalTime();

        // Verificar cada killzone
        if (isBetween(currentTime, KILLZONES[0], KILLZONES[1])) {
            return new KillzoneInfo(true, "LDN Killzone", getMotivationalPhrase("LDN"));
        }
        if (isBetween(currentTime, KILLZONES[2], KILLZONES[3])) {
            return new KillzoneInfo(true, "NY Killzone", getMotivationalPhrase("NY"));
        }
        if (isBetween(currentTime, KILLZONES[4], KILLZONES[5]) ||
                currentTime.isAfter(KILLZONES[5])) {
            return new KillzoneInfo(true, "ASIA Killzone", getMotivationalPhrase("ASIA"));
        }

        return new KillzoneInfo(false, "Fuera de Killzone",
                "Horario de menor volatilidad. Considera esperar.");
    }

    private static boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
        if (end.equals(LocalTime.MIDNIGHT)) {
            end = LocalTime.of(23, 59, 59);
        }
        return !time.isBefore(start) && time.isBefore(end);
    }

    private static String getMotivationalPhrase(String zone) {
        String[] phrases = {
                "¡Estás en la zona! La disciplina es tu mejor indicador.",
                "Mercado activo. Mantén tu edge y sigue tu plan.",
                "Volatilidad óptima. Confía en tu análisis.",
                "Momento de acción. Sé preciso como un cirujano.",
                "La consistencia construye fortunas. ¡Enfócate!",
                "Cada operación cuenta. Ejecuta con convicción."
        };

        int index = (int) (Math.random() * phrases.length);
        return phrases[index];
    }

    public static String getFormattedNYTime() {
        ZonedDateTime nyTime = ZonedDateTime.now(NY_ZONE);
        return String.format(Locale.getDefault(),
                "%02d:%02d (NY Time)",
                nyTime.getHour(),
                nyTime.getMinute());
    }
}