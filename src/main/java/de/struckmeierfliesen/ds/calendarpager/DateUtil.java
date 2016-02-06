package de.struckmeierfliesen.ds.calendarpager;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Some useful static methods for working with dates ({@link Date}).
 */
public class DateUtil {
    private DateUtil() {}

    /**
     * Check wether two {@link Date} Objects fall on the same day.
     *
     * @param date1 First date
     * @param date2 Second date
     * @return      true when the dates fall on the same day
     *              or if either date is null
     *              otherwise false
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return date1 == null || date2 == null
                || DateFormat.format("dd.MM.yy", date1).equals(DateFormat.format("dd.MM.yy", date2));
    }

    /**
     * Adds some amount of days to a {@link Date}.
     *
     * @param date   The starting date.
     * @param addend How many days are added.
     * @return       result
     */
    public static Date addDays(Date date, int addend) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, addend);
        return c.getTime();
    }

    /**
     * Abbreviates the day of week
     *
     * @param date
     * @return     Abbreviation of day of week
     */
    public static String getDayAbbrev(Date date) {
        return DateFormat.format("EE", date).toString();
    }

    /**
     * Calculates the difference in days between two given {@link Date} objects.
     *
     * @param date1 Starting {@link Date}
     * @param date2 Ending {@link Date}
     * @return      Difference in days between the dates.
     */
    public static int getDayDifference(Date date1, Date date2) {
        date1 = getStartOfDay(date1);
        date2 = getStartOfDay(date2);
        long diffInMillies = date1.getTime() - date2.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns a new {@link Date} object where the time is set to the end of the day.
     *
     * @param date Starting {@link Date}
     * @return     {@link Date} object with the time set to just before midnight.
     */
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * Returns a new {@link Date} object where the time is set to the start of the day.
     *
     * @param date Starting {@link Date}
     * @return     {@link Date} object with the time set to midnight.
     */
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Returns the weekday as a word or "Tomorrow", "Today" or "Yesterday" if appropriate.
     *
     * @param date
     * @return     Weekday
     */
    @NonNull
    public static String getWeekday(Date date) {
        // Standard language is english
        String[] translation = {"Tomorrow", "Today", "Yesterday"};

        if(Locale.getDefault().getCountry().equals(Locale.GERMAN.getCountry())) {
            translation = new String[] {"Morgen", "Heute", "Gestern"};
        }
        switch (getDayDifference(new Date(), date)) {
            case -1:
                return translation[0];
            case 0:
                return translation[1];
            case 1:
                return translation[2];
            default:
                return DateFormat.format("EEEE", date).toString();
        }
    }

    /**
     * Format the {@Date} in the dd.MM.yy format.
     *
     * @param date {@Date} to format
     * @return     Formatted {@String}
     */
    public static String formatDate(Date date) {
        if (date == null) return "null";
        return DateFormat.format("dd.MM.yy", date).toString();
    }
}
