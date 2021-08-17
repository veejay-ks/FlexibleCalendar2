package com.p_v.flexiblecalendar;

import ohos.app.Context;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author p-v
 */
public class FlexibleCalendarHelper {

    private final static int mOffset = 3;
    /**
     * Set the next month for the details passed
     *
     * @param year     year
     * @param month    month
     * @param nextDate next month empty array
     */
    public static void nextMonth(int year, int month, int[] nextDate) {
        if (month == 11) {
            year++;
            month = 0;
        } else {
            month++;
        }
        nextDate[0] = year;
        nextDate[1] = month;
    }

    /**
     * Set the previous month for the details passed
     *
     * @param year         year
     * @param month        month
     * @param previousDate previous month empty array
     */
    public static void previousMonth(int year, int month, int[] previousDate) {
        if (month == 0) {
            year--;
            month = 11;
        } else {
            month--;
        }
        previousDate[0] = year;
        previousDate[1] = month;
    }

    /**
     * @return Get the array for week days for the current locale
     */
    public static String[] getWeekDaysList(Context context) {
        DateFormatSymbols symbols = new DateFormatSymbols(getLocale(context));
        return Arrays.copyOfRange(symbols.getShortWeekdays(), 1, 8);
    }

    /**
     * Get the current locale
     */
    public static Locale getLocale(Context context) {
        return context.getResourceManager().getConfiguration().getFirstLocale();
    }

    /**
     * @return the localized calendar instance
     */
    public static Calendar getLocalizedCalendar(Context context) {
        return Calendar.getInstance(getLocale(context));
    }

    /**
     * Get the number of rows for the provided month
     *
     * @param year  year
     * @param month month
     * @return number of rows
     */
    public static int getNumOfRowsForTheMonth(int year, int month, int startDayOfTheWeek) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return getRowOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + 1;
    }

    /**
     * @return Which row day is in.
     */
    public static int getRowOf(int day) {
        return (day + mOffset - 1) / 7;
    }

    /**
     * Get number of month difference with the current month
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDifference(int year, int month) {
        System.out.println("monthDifference stat "+year+" "+month);
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        System.out.println("monthDifference stat second "+currentMonth+" "+currentYear);
        int x = (currentYear - year) * 12 + currentMonth - month;
        System.out.println("monthDifference stat second x --- "+x);
        return x;
    }

    /**
     * Get number of month difference between two the start and end month/year
     *
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static int getMonthDifference(int startYear, int startMonth, int endYear, int endMonth) {
        return (endYear - startYear) * 12 + endMonth - startMonth;
    }

}
