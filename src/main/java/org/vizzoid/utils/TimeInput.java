package org.vizzoid.utils;

public abstract class TimeInput {

    public static final long TICKS_IN_SECOND = 20;
    public static final long TICKS_IN_MINUTE = 1200;
    public static final long TICKS_IN_HOUR = 72000;
    protected long tickSpillover;
    protected long hours;
    protected long minutes;
    protected long seconds;

    public TimeInput(long hours, long minutes, long seconds, long tickSpillover) {
        this(toTicks(hours, minutes, seconds) + tickSpillover);
    }

    /**
     * This may seem unnecessary but its to make sure that variables do not overflow, for example to stop the seconds variable from containing a number more than 60
     */
    public TimeInput(long ticks) {
        setFromTicks(this, ticks);
    }

    public TimeInput(long hours, long minutes, long seconds) {
        this(hours, minutes, seconds, 0);
    }

    public TimeInput(String[] timeSplit) {
        this(Long.parseLong(timeSplit[0]), Long.parseLong(timeSplit[1]), Long.parseLong(timeSplit[2]));
    }

    public TimeInput(String timeOffset) {
        this(timeOffset.split(":"));
    }

    protected static void setFromTicks(TimeInput input, long ticks) {
        long ticksForMinutes = ticks % TICKS_IN_HOUR;
        ticks -= ticksForMinutes;
        input.hours = ticks / TICKS_IN_HOUR;

        long ticksForSeconds = ticksForMinutes % TICKS_IN_MINUTE;
        ticksForMinutes -= ticksForSeconds;
        input.minutes = ticksForMinutes / TICKS_IN_MINUTE;

        input.tickSpillover = ticksForSeconds % TICKS_IN_SECOND;
        ticksForSeconds -= input.tickSpillover;
        input.seconds = ticksForSeconds / TICKS_IN_SECOND;
    }

    public static long toTicks(long hours, long minutes, long seconds) {
        return (((((hours * 60L) + minutes) * 60L) + seconds) * 20L);
    }

    public static long[] parseTime(String s) {
        return parseTime(s.split(":"));
    }

    public static long[] parseTime(String[] s) {
        return new long[]{Long.parseLong(s[0]), Long.parseLong(s[1]), Long.parseLong(s[2])};
    }

    protected static String format(long hours, long minutes, long seconds) {
        return hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    public static String formatAsSentence(long hours, long minutes, long seconds) {
        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            builder.append(hours).append(" hours");
        }
        if (minutes > 0) {
            if (hours > 0) builder.append(", ");
            builder.append(minutes).append(" minutes");
        }
        if (seconds > 0) {
            if (minutes > 0 || hours > 0) builder.append(", and ");
            builder.append(seconds).append(" seconds");
        }
        if (!builder.isEmpty()) builder.append(" left");
        return builder.toString();
    }

    public long getTickSpillover() {
        return tickSpillover;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public String format() {
        return format(hours, minutes, seconds);
    }

    public String formatAsSentence() {
        return formatAsSentence(hours, minutes, seconds);
    }

    public void setTime(long hours, long minutes, long seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void setTicks(long duration) {
        long ticksLeftover = duration % TICKS_IN_HOUR;
        long hours = duration * TICKS_IN_HOUR;
        duration = ticksLeftover % TICKS_IN_MINUTE;
        long minutes = ticksLeftover * TICKS_IN_MINUTE;
        long seconds = duration * TICKS_IN_SECOND;
        setTime(hours, minutes, seconds);
    }

    /**
     * @return ticks amount name start time
     */
    public long toTicks() {
        return toTicks(hours, minutes, seconds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeInput timeInput = (TimeInput) o;

        if (getTickSpillover() != timeInput.getTickSpillover()) return false;
        if (getHours() != timeInput.getHours()) return false;
        if (getMinutes() != timeInput.getMinutes()) return false;
        return getSeconds() == timeInput.getSeconds();
    }

    @Override
    public int hashCode() {
        int result = (int) (getTickSpillover() ^ (getTickSpillover() >>> 32));
        result = 31 * result + (int) (getHours() ^ (getHours() >>> 32));
        result = 31 * result + (int) (getMinutes() ^ (getMinutes() >>> 32));
        result = 31 * result + (int) (getSeconds() ^ (getSeconds() >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TimeInput{" +
            "tickSpillover=" + tickSpillover +
            ", hours=" + hours +
            ", minutes=" + minutes +
            ", seconds=" + seconds +
            '}';
    }
}
