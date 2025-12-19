package ir.microsign.calendar;
import java.util.Calendar;
import ir.microsign.R;

public class CivilDate extends Date {
//    private static final String[] weekdayName =


    private static final int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public CivilDate() {
        this(Calendar.getInstance());
    }

    public CivilDate(Calendar calendar) {
        setMonthNames(Date.getContext().getResources().getStringArray(R.array.calendar_months_gregorian));
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }
//    public CivilDate(java.util.Date date) {
//        setMonthNames(activity.getContext().getResources().getStringArray(R.array.calendar_months_gregorian));
//        this.year =date.getYear();// calendar.get(Calendar.YEAR);
//        this.month =date.getMonth(); //calendar.get(Calendar.MONTH) + 1;
//        this.day =date.getDay();// calendar.get(Calendar.DAY_OF_MONTH);
//        hour =date.getHours();// calendar.get(Calendar.HOUR_OF_DAY);
//        minute =date.getMinutes();// calendar.get(Calendar.MINUTE);
//        second =date.getSeconds();// calendar.get(Calendar.SECOND);
//    }

    public CivilDate(int year, int month, int day) {
        this();
        setYear(year);
        setMonth(month);
        setDayOfMonth(day);
    }

    public CivilDate(int year, int month, int day, int h, int m, int s) {
        this();
        setYear(year);
        setMonth(month);
        setDayOfMonth(day);
        hour = h;
        minute = m;
        second = s;
    }

    public int getDayOfMonth() {
        return day;
    }

    public void setDayOfMonth(int day) {
        if (day < 1)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (month != 2 && day > daysInMonth[month])
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (month == 2 && isLeapYear() && day > 29)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (month == 2 && (!isLeapYear()) && day > 28)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        // TODO check for the case of leap year for February
        this.day = day;
    }

    public int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public boolean isLeapYear() {
        if (year % 400 == 0)
            return true;
        else if (year % 100 == 0)
            return false;
        else if (year % 4 == 0)
            return true;
        else
            return false;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException("month " + month
                    + " is out of range!");

        // Set the day again, so that exceptions are thrown if the
        // day is out of range
        setDayOfMonth(getDayOfMonth());

        this.month = month;
    }

//    public String getDayOfWeekName() {
//        return weekdayName[getDayOfWeek()];
//    }
}
