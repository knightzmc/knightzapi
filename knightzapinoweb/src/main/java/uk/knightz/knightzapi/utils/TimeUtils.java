/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.utils;/*
 *
 * This file is generated under this project, "open-commons-core".
 *
 * Date  : 2018. 1. 9
 *
 * Author: Park_Jun_Hong_(fafanmama_at_naver_com)
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Park_Jun_Hong_(fafanmama_at_naver_com)
 * @since 2018. 1. 9.
 */
public class TimeUtils {

    /**
     * discard none.
     */
    public static final int DC_NONE = 0x00;
    /**
     * discard under nanoseconds
     */
    public static final int DC_NANO = 0x01;
    /**
     * discard under microseconds
     */
    public static final int DC_MICRO = DC_NANO << 1;
    /**
     * discard under milliseconds
     */
    public static final int DC_MILLI = DC_MICRO << 1;
    /**
     * discard under seconds
     */
    public static final int DC_SECOND = DC_MILLI << 1;
    /**
     * discard under minutes
     */
    public static final int DC_MINUTE = DC_SECOND << 1;
    /**
     * discard under hours
     */
    public static final int DC_HOUR = DC_MINUTE << 1;
    /**
     * discard under days
     */
    public static final int DC_DAY = DC_HOUR << 1;
    private static final TimeUnitInfo[] TIME_UNIT_INFO = new TimeUnitInfo[]{ //
            new TimeUnitInfo(TimeUnit.NANOSECONDS, "ns") //
            , new TimeUnitInfo(TimeUnit.MICROSECONDS, "us") //
            , new TimeUnitInfo(TimeUnit.MILLISECONDS, "ms") //
            , new TimeUnitInfo(TimeUnit.SECONDS, "s") //
            , new TimeUnitInfo(TimeUnit.MINUTES, "m") //
            , new TimeUnitInfo(TimeUnit.HOURS, "h") //
            , new TimeUnitInfo(TimeUnit.DAYS, "d") //
    };
    private static final Map<TimeUnit, String> UNIT_STR = new HashMap<>();
    private static final Function<TimeUnit, TimeUnitInfo[]> FN_TIME_UNITS = unit -> {

        ArrayList<TimeUnitInfo> units = new ArrayList<>();

        for (TimeUnitInfo tui : TIME_UNIT_INFO) {
            if (tui.unit.ordinal() >= unit.ordinal()) {
                units.add(tui);
            }
        }

        return units.toArray(new TimeUnitInfo[]{});
    };

    static {
        for (TimeUnitInfo tui : TIME_UNIT_INFO) {
            UNIT_STR.put(tui.unit, tui.unitStr);
        }
    }

    // prevent to create an instance.
    private TimeUtils() {
    }

    public static void main(String[] args) {

        long[] times = new long[]{60, 3600, 24000};
        for (long time : times) {
            System.out.println(String.format("%20s %-2s -> %s", String.format("%,d", time), "ms", toFormattedString(time, TimeUnit.SECONDS)));
        }

        System.out.println("=================================");

//        long time = 50428677591L;
//        for (TimeUnitInfo tui : TIME_UNIT_INFO) {
//            System.out.println(String.format("%20s %-2s -> %s", String.format("%,d", time), tui.unitStr, toFormattedString(time, tui.unit)));
//        }
    }

    private static long mod(long time, TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS: // to nanosecond
            case MILLISECONDS: // to microsecond
            case MICROSECONDS: // to millsecond
                return time % 1000;
            case SECONDS: // to second
                return time % 60;
            case MINUTES: // to minute
                return time % 60;
            case HOURS: // to hour
                return time % 24;
            case DAYS: // to day
                return time % 365;
            default:
                throw new IllegalArgumentException(unit.toString());
        }
    }

    /**
     * <br>
     *
     * <pre>
     * [ê°œì •ì�´ë ¥]
     *      ë‚ ì§œ      | ìž‘ì„±ìž�   |   ë‚´ìš©
     * ------------------------------------------
     * 2018. 1. 9.      ë°•ì¤€í™�         ìµœì´ˆ ìž‘ì„±
     * </pre>
     *
     * @param timeBuf
     * @param time
     * @param unit
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     * @since 2018. 1. 9.
     */
    private static void prependTimeAndUnit(StringBuffer timeBuf, long time, String unit) {
        if (time < 1) {
            return;
        }

        if (timeBuf.length() > 0) {
            timeBuf.insert(0, " ");
        }

        timeBuf.insert(0, unit);
        timeBuf.insert(0, time);
    }

    /**
     * Provide the Millisecond time value in {year}y {day}d {hour}h {minute}m {second}s {millisecond}ms {nanoseconds}ns.
     * <br>
     * Omitted if there is no value for that unit.
     *
     * @param time     time value.
     * @param timeUnit a unit of input time value.
     * @return
     * @since 2018. 1. 9.
     */
    public static String toFormattedString(long time, TimeUnit timeUnit) {

        // if zero ...
        if (time < 1) {
            return "0 " + UNIT_STR.get(timeUnit);
        }

        StringBuffer timeBuf = new StringBuffer();

        long mod = 0L;
        long up = time;

        for (TimeUnitInfo unit : FN_TIME_UNITS.apply(timeUnit)) {
            mod = mod(up, unit.unit);
            prependTimeAndUnit(timeBuf, mod, unit.unitStr);

            up = up(up, unit.unit);

            if (up < 1) {
                return timeBuf.toString();
            }
        }

        prependTimeAndUnit(timeBuf, up, "y");

        return timeBuf.toString();
    }

    private static long up(long time, TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS: // to microsecond & above
            case MILLISECONDS: // to millsecond & above
            case MICROSECONDS: // to second & above
                return time / 1000;
            case SECONDS: // to minute & above
                return time / 60;
            case MINUTES: // to hour & above
                return time / 60;
            case HOURS: // to day & above
                return time / 24;
            case DAYS: // to year & above
                return time / 365;
            default:
                throw new IllegalArgumentException(unit.toString());
        }
    }

    private static class TimeUnitInfo {
        private final TimeUnit unit;
        private final String unitStr;

        public TimeUnitInfo(TimeUnit unit, String unitStr) {
            this.unit = unit;
            this.unitStr = unitStr;
        }
    }

}