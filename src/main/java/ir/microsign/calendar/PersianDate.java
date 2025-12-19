package ir.microsign.calendar;

import ir.microsign.R;

/**
 * @author Amir
 * @author ebraminio (implementing isLeapYear)
 */
public class PersianDate extends Date {

//    private static final String[] persianMonthName = {"", "فروردین",
//            "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان",
//            "آذر", "دی", "بهمن", "اسفند"};

    /**
     * Months Names in Dari, needed for special Dari Version. Provided by:
     * Mohammad Hamid Majidee
     */
//    private static final String[] dariMonthName = {"", "حمل", "ثور", "جوزا",
//            "سرطان", "اسد", "سنبله", "میزان", "عقرب", "قوس", "جدی", "دلو",
//            "حوت"};

    private boolean isDari = false;

//    public String[] getMonthsList() {
//        return isDari ? dariMonthName : persianMonthName;
//    }

    public PersianDate(int year, int month, int day) {
       this(year,month,day,-1,0,0,0);
    }
    public PersianDate(int year, int month, int day,int hour,int min, int sec) {
        this(year,month,day,hour,min,sec,0);
    }
    public PersianDate(int year, int month, int day, int hour, int min, int sec, int ms){
              setMonthNames(Date.getContext().getResources().getStringArray(ir.microsign.R.array.calendar_months_persian));
        setYear(year);
        // Initialize day, so that we get no exceptions when setting month
        this.day = 1;
        setMonth(month);
        setDayOfMonth(day);
        this.hour=hour;
        this.minute=min;
        this.second=sec;
        this.milisecond=ms;
    }

    public void setDari(boolean isDari) {
        this.isDari = isDari;
    }

    public PersianDate clone() {
        return new PersianDate(getYear(), getMonth(), getDayOfMonth());
    }

    public void setDayOfMonth(int day) {
        if (day < 1)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (month <= 6 && day > 31)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (month > 6 && month <= 12 && day > 30)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if ((isLeapYear() && month == 12 && day > 30) || ((!isLeapYear()) && month == 12 && day > 29))
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
        int y;
        if (year > 0)
            y = year - 474;
        else
            y = 473;
        return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
    }


}
