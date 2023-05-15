package org.vizzoid.utils;

import org.vizzoid.utils.test.TestThread;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TimeStamp {

    public static final long TICKS_IN_SECOND = 20;
    public static final long TICKS_IN_MINUTE = 1200;
    public static final long TICKS_IN_HOUR = 72000;
    public static final long TICKS_IN_DAY = 1728000;
    public static final long AVERAGE_TICKS_IN_MONTH = 51840000;
    public static final long AVERAGE_TICKS_IN_YEAR = 622080000;

    private static final TimeStamp EMPTY = new TimeStamp(Instant.EPOCH);
    private static final String SPLIT = ":";
    private static final String DATE_SPLIT = "/";
    private final Formatter formatter;
    protected final Instant instant;
    protected final ZonedDateTime zoned;

    public TimeStamp(Instant instant) {
        this(instant, ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    public TimeStamp(Instant instant, ZonedDateTime zoned) {
        this.instant = instant;
        this.zoned = zoned;

        formatter = new Formatter(); // NEEDS TO BE LAST STATEMENT
        // USES FIELDS IN INITIALIZER // not anymore
    }

    public TimeStamp(ZonedDateTime z) {
        this(z.toInstant(), z);
    }

    public TimeStamp(LocalDateTime l) {
        this(ZonedDateTime.of(l, ZoneId.systemDefault()));
    }

    public TimeStamp(long ms) {
        this(Instant.ofEpochMilli(ms));
    }

    public static TimeStamp parse(String s) {
        return s != null ? EMPTY.getFormatter().parse(s) : null;
    }

    public static TimeStamp empty() {
        return EMPTY;
    }

    public static TimeStamp now() {
        Thread thread = Thread.currentThread();
        if (thread instanceof TestThread) return ((TestThread) thread).now;
        return new TimeStamp(Instant.now());
    }

    public int getYear() {
        return zoned().getYear();
    }

    public int getMonth() {
        return zoned().getMonthValue();
    }

    public int getDayOfMonth() {
        return zoned().getDayOfMonth();
    }

    public int getHours() {
        return zoned().getHour();
    }

    public int getMinutes() {
        return zoned().getMinute();
    }

    public int getSeconds() {
        return zoned().getSecond();
    }

    public int getTickSpillover() {
        return zoned().get(ChronoField.MILLI_OF_SECOND) / 50;
    }

    public Instant instant() {
        return instant;
    }

    public ZonedDateTime zoned() {
        return zoned;
    }

    public static String toString(TimeStamp timeStamp) {
        return timeStamp != null ? timeStamp.toString() : null;
    }

    /**
     * Returns null if absent instead of empty time stamp
     */
    public static TimeStamp parseWithNull(String string) {
        TimeStamp timeStamp = parse(string);
        return EMPTY.equals(timeStamp) ? null : timeStamp;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public boolean isEmpty() {
        return equals(empty());
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
                "totalMs=" + toMillis() + ", " +
                "format=" + getFormatter().formatWithSpillover() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeStamp)) return false;
        TimeStamp timeStamp = (TimeStamp) o;

        return toMillis() == timeStamp.toMillis();
    }

    public static TimeStamp ofTicks(long totalTicks) {
        return new TimeStamp(totalTicks * 50);
    }

    @Override
    public int hashCode() {
        int result = getYear() ^ (getYear());
        result = 31 * result + (getMonth() ^ (getMonth()));
        result = 31 * result + (getDayOfMonth() ^ (getDayOfMonth()));
        result = 31 * result + getFormatter().hashCode();
        return result;
    }

    public boolean isAfter(TimeStamp stamp) {
        return toMillis() > stamp.toMillis();
    }

    public boolean isAfter(LocalDateTime stamp) {
        return toMillis() > new TimeStamp(stamp).toMillis();
    }

    public boolean isBefore(TimeStamp stamp) {
        return toMillis() < stamp.toMillis();
    }

    public boolean isBefore(LocalDateTime stamp) {
        return toMillis() < new TimeStamp(stamp).toMillis();
    }

    public boolean isSameDay(TimeStamp stamp) {
        return getYear() == stamp.getYear() &&
                getMonth() == stamp.getMonth() &&
                getDayOfMonth() == stamp.getDayOfMonth();
    }

    public boolean isSameDay(LocalDate date) {
        return getYear() == date.getYear() &&
                getMonth() == date.getMonthValue() &&
                getDayOfMonth() == date.getDayOfMonth();
    }

    public boolean isSameDay(LocalDateTime date) {
        return getYear() == date.getYear() &&
                getMonth() == date.getMonthValue() &&
                getDayOfMonth() == date.getDayOfMonth();
    }

    public TimeStamp plusYears(long years) {
        return new TimeStamp(zoned().plusYears(years));
    }

    public TimeStamp plusMonths(long months) {
        return new TimeStamp(zoned().plusMonths(months));
    }

    public TimeStamp plusDays(long days) {
        return new TimeStamp(zoned().plusDays(days));
    }

    public TimeStamp plusHours(long hour) {
        return new TimeStamp(zoned().plusHours(hour));
    }

    public TimeStamp plusMinutes(long minute) {
        return new TimeStamp(zoned().plusMinutes(minute));
    }

    public TimeStamp plusSeconds(long second) {
        return new TimeStamp(zoned().plusSeconds(second));
    }

    public TimeStamp plusTicks(long ticks) {
        return new TimeStamp(zoned().plusNanos(ticks * 50_000_000));
    }

    public TimeStamp nextYear() {
        return plusYears(1);
    }

    public TimeStamp nextMonth() {
        return plusMonths(1);
    }

    public TimeStamp nextDay() {
        return plusDays(1);
    }

    public TimeStamp nextHour() {
        return plusHours(1);
    }

    public TimeStamp nextMinute() {
        return plusMinutes(1);
    }

    public TimeStamp nextSecond() {
        return plusSeconds(1);
    }

    public TimeStamp nextTick() {
        return plusTicks(1);
    }

    // brought from timeinput
    public String formatAsSentence() {
        ZonedDateTime zoned = zoned();
        return TimeInput.formatAsSentence(zoned.getHour(), zoned.getMinute(), zoned.getSecond());
    }

    /**
     * Returns the tick duration from this timestamp to the inputted one. If number is negative, the input is before this timestamp
     * Returns duration of now until input. If duration < 0, then the input is before this, if duration > 0 the input is after this. If the duration == 0 the input is within one tick before or after this
     */
    public long duration(TimeStamp timeStamp) {
        return timeStamp.toMillis() - toMillis();
    }

    public long durationInSeconds(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_SECOND);
    }

    public long durationInMinutes(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_MINUTE);
    }

    public long durationInHours(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_HOUR);
    }

    /**
     *
     */
    public long durationInDays(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_DAY);
    }

    private static long divide(long dividend, long divisor) {
        boolean negative = dividend < 0;
        if (negative) dividend = -dividend;
        return ((dividend - (dividend % divisor)) / divisor) * (negative ? -1 : 1);
    }

    /**
     * Turns TimeStamp into ticks from January 1, 1970, 0:00:00 GMT.
     */
    public long toTicks() {
        return instant.toEpochMilli() / 50;
    }

    public long toMillis() {
        return instant.toEpochMilli();
    }

    public class Formatter {

        private Formatter() {
        }

        public String getYear() {
            return String.format("%04d", TimeStamp.this.getYear());
        }

        public String getMonth() {
            return String.format("%02d", TimeStamp.this.getMonth());
        }

        public String getDayOfMonth() {
            return String.format("%02d", TimeStamp.this.getDayOfMonth());
        }

        public String getHours() {
            return String.format("%02d", TimeStamp.this.getHours());
        }

        public String getMinutes() {
            return String.format("%02d", TimeStamp.this.getMinutes());
        }

        public String getSeconds() {
            return String.format("%02d", TimeStamp.this.getSeconds());
        }

        public String getTickSpillover() {
            return String.format("%02d", TimeStamp.this.getTickSpillover());
        }

        public TimeStamp parse(String s) {
            return parse(s, SPLIT);
        }

        public TimeStamp parse(String s, String split) {
            int count = s.split(split).length - 1; // This count plus one is how many inputs there are
            return switch (count) {
                case 2 -> parseFromTimeWithoutSpillover(s, split);
                case 3 -> parseFromTimeWithSpillover(s, split);
                case 5 -> parseFromDateTimeWithoutSpillover(s, split);
                case 6 -> parseFromDateTimeWithSpillover(s, split);
                default -> empty();
            };
        }

        public TimeStamp parseFromTimeWithoutSpillover(String s) {
            return parseFromTimeWithoutSpillover(s, SPLIT);
        }

        public TimeStamp parseFromTimeWithSpillover(String s) {
            return parseFromTimeWithSpillover(s, SPLIT);
        }

        public TimeStamp parseFromDateTimeWithSpillover(String s) {
            return parseFromDateTimeWithSpillover(s, SPLIT);
        }

        public TimeStamp parseFromDateTimeWithoutSpillover(String s) {
            return parseFromDateTimeWithoutSpillover(s, SPLIT);
        }

        public TimeStamp parseFromTimeWithoutSpillover(String s, String split) {
            List<Integer> values = Arrays.stream(s.split(split)).map(Integer::parseInt).toList();
            return new TimeStamp(ZonedDateTime.of(0, 1, 1, values.get(0), values.get(1), values.get(2), 0, ZoneId.systemDefault()));
        }

        public TimeStamp parseFromTimeWithSpillover(String s, String split) {
            List<Integer> values = Arrays.stream(s.split(split)).map(Integer::parseInt).toList();
            return new TimeStamp(ZonedDateTime.of(0, 1, 1, values.get(0), values.get(1), values.get(2), values.get(3) * 50_000_000, ZoneId.systemDefault()));
        }

        public TimeStamp parseFromDateTimeWithSpillover(String s, String split) {
            List<Integer> values = Arrays.stream(s.split(split)).map(Integer::parseInt).toList();
            return new TimeStamp(ZonedDateTime.of(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5), values.get(6) * 50_000_000, ZoneId.systemDefault()));
        }

        public TimeStamp parseFromDateTimeWithoutSpillover(String s, String split) {
            List<Integer> values = Arrays.stream(s.split(split)).map(Integer::parseInt).toList();
            return new TimeStamp(ZonedDateTime.of(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5), 0, ZoneId.systemDefault()));
        }

        public String format() {
            return format(SPLIT);
        }

        public String formatWithSpillover() {
            return formatWithSpillover(SPLIT);
        }

        public String formatWithoutSpillover() {
            return formatWithoutSpillover(SPLIT);
        }

        public String formatTime() {
            return formatTime(SPLIT);
        }

        public String formatTimeWithSpillover() {
            return formatTimeWithSpillover(SPLIT);
        }

        public String formatTimeWithoutSpillover() {
            return formatTimeWithoutSpillover(SPLIT);
        }

        public String formatDate() {
            return formatDate(DATE_SPLIT);
        }

        public String format(String split) {
            return TimeStamp.this.getYear() != 0 ?
                    (TimeStamp.this.getTickSpillover() != 0 ?
                            formatWithSpillover(split) :
                            formatWithoutSpillover(split)) :
                    (formatTime(split));
        }

        public String formatWithSpillover(String split) {
            return (formatWithoutSpillover(split) + split + getTickSpillover());
        }

        public String formatWithoutSpillover(String split) {
            return (formatDate(split) + split + formatTimeWithoutSpillover(split));
        }

        public String formatTime(String split) {
            return TimeStamp.this.getTickSpillover() != 0 ? formatTimeWithSpillover(split) :
                    formatTimeWithoutSpillover(split);
        }

        public String formatTimeWithSpillover(String split) {
            return (formatTimeWithoutSpillover(split) + split + getTickSpillover());
        }

        public String formatTimeWithoutSpillover(String split) {
            return (getHours() + split + getMinutes() + split + getSeconds());
        }

        public String formatDate(String split) {
            return Locale.ROOT.equals(Locale.US) ?
                    (formatDateUS(split)) :
                    (getDayOfMonth() + split + getMonth() + split + getYear());
        }

        public String formatDateUS(String split) {
            return getMonth() + split + getDayOfMonth() + split + getYear();
        }

        public String formatDateUS() {
            return formatDateUS(DATE_SPLIT);
        }

    }

}
