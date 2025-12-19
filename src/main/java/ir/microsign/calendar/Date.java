package ir.microsign.calendar;

import android.content.Context;

import java.util.Locale;

public abstract class Date {
static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Date.context = context;
    }

    public static String[] monthNames = null;
    public int year = 0;
    public int month = 0;
    public int day = 1;
    public int hour = -1, minute, second, milisecond;
    String mSeparator = "/";

    static boolean isInt(CharSequence c) {
        try {
//            "0123456789"

            Integer.parseInt(c.toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

//    public static Date fromString(String date, Type inType, Type outType,) {
//        if (date == null || date.trim().length() < 1) return null;
//        List<String> stepList = new ArrayList<String>();
//        String step = "";
//        for (int i = 0; i < date.length(); i++) {
//            String ch = date.substring(i, i + 1);
//            if (isInt(ch) && i != date.length() - 1) {
//                step += ch;
//                continue;
//            }
//            if (isInt(ch) && i == date.length() - 1) step += ch;
//            {
//                if (step.length() > 0) stepList.add(step);
//                step = "";
//            }
//        }
//
//        int year = Integer.parseInt(stepList.get(0));
//        int month = Integer.parseInt(stepList.get(1));
//        int day = Integer.parseInt(stepList.get(2));
//        int h, m, s;
//        Date date1 = null;
//        if (stepList.size() >= 6) {
//            h = Integer.parseInt(stepList.get(3));
//            m = Integer.parseInt(stepList.get(4));
//            s = Integer.parseInt(stepList.get(5));
//            date1 = new CivilDate(year, month, day, h, m, s);
//        } else date1 = new CivilDate(year, month, day);
//        if (inType == Type.persian) date1 = new PersianDate(year, month, day);
//        else if (inType == Type.islamic) date1 = new IslamicDate(year, month, day);
//        return Converter.Convert(date1, inType, outType);
////		2011-01-01 00:00:01
//    }

    public String[] getMonthsList() {
        return monthNames;
    }

    public void setDate(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(day);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year == 0)
            throw new YearOutOfRangeException("Year 0 is invalid!");
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
    }

    public String getMonthName() {
        return getMonthNames()[getMonth()];
    }


    public String[] getMonthNames() {
        return monthNames;
    }

    public void setMonthNames(String[] monthNames) {
        this.monthNames = monthNames;
    }

    public int getDayOfMonth() {
        return day;
    }

    public void setDayOfMonth(int day) {
        this.day = day;
    }

    public int getDayOfWeek() {
        return -1;
    }

    public int getDayOfYear() {
        return -1;
    }

    public int getWeekOfYear() {
        return -1;
    }

    public int getWeekOfMonth() {
        return -1;
    }

    public boolean equals(Object date) {
        if (!(date instanceof Date)) return false;
        if (this.getDayOfMonth() == ((Date) date).getDayOfMonth()
                && this.getMonth() == ((Date) date).getMonth()
                && this.getYear() == ((Date) date).getYear())
            return true;
        return false;
    }

    public abstract boolean isLeapYear();

    @Override
    public String toString() {
        return toString(false, false);
    }

    public String toString(boolean showTime, boolean standard) {
        String d = standard ? "%02d" : "%d";
        if (showTime && hour >= 0 & minute >= 0 & second >= 0)
            return String.format(Locale.ENGLISH, d + mSeparator + d + mSeparator + d + " " + d + ":" + d + ":" + d, getYear(), getMonth(), getDayOfMonth(), hour, minute, second);
        return String.format(Locale.ENGLISH, d + mSeparator + d + mSeparator + d, getYear(), getMonth(), getDayOfMonth());
    }

    /**
     * Returns a string specifying the event of this date, or null if there are
     * no events for this year.
     */
//    public abstract String getEvent();
    public enum Type {
        gregorian,
        persian,
        islamic
    }
}
