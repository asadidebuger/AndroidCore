package ir.microsign.calendar;

import ir.microsign.R;

/**
 * @author Amir
 * @author ebraminio
 */
public class IslamicDate extends Date {
//    private static final String[] islamicMonthName = {"", "محرم",
//            "صفر", "ربيع الاول","ربيع الثاني", "جمادي الاول", "جمادي الثاني", "رجب", "شعبان", "رمضان", "شوال",
//            "ذي القعده", "ذي الحجه"};
    public IslamicDate(int year, int month, int day) {
        setMonthNames(Date.getContext().getResources().getStringArray(R.array.calendar_months_islamic));
        setYear(year);
        // Initialize day, so that we get no exceptions when setting month
        this.day = 1;
        setMonth(month);
        setDayOfMonth(day);
    }

    public void setDayOfMonth(int day) {
        // TODO This check is not very exact! But it's not worth of it
        // to compute the number of days in this month exactly
        if (day < 1 || day > 30)
            throw new DayOutOfRangeException("day " + day + " is out of range!");
        this.day = day;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException("month " + month
                    + " is out of range!");

        // Set the day again, so that exceptions are thrown if the
        // day is out of range
        setDayOfMonth(day);

        this.month = month;
    }

    public boolean isLeapYear() {
        return false;
    }

}
