package org.ikernits.sample.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static final DateTimeFormatter dateDashFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter timeColonFormatter = DateTimeFormat.forPattern("HH:mm:ss");
    public static final DateTimeFormatter dateTimeDashFormatter = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm-ss");
    public static final DateTimeFormatter dateSlashFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
    public static final DateTimeFormatter dateHourSlashFormatter = DateTimeFormat.forPattern("yyyy/MM/dd/HH");

    public static String dateTimeDashString() {
        return Instant.now().toString(dateTimeDashFormatter);
    }

    public static String dateDashString() {
        return Instant.now().toString(dateDashFormatter);
    }

    public static String dateDashFormat(ReadableInstant instant, DateTimeZone timeZone) {
        return new DateTime(instant, timeZone).toString(dateDashFormatter);
    }

    public static String dateTimeDashFormat(ReadableInstant instant, DateTimeZone timeZone) {
        return new DateTime(instant, timeZone).toString(dateTimeDashFormatter);
    }

    public static String timeColonFormat(ReadableInstant instant, DateTimeZone timeZone) {
        return new DateTime(instant, timeZone).toString(timeColonFormatter);
    }
}
