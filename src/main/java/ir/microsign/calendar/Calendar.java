package ir.microsign.calendar;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mohammad on 9/23/14.
 */
public class Calendar {
    public static String serverDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static void setContext(Context context){
        Date.setContext(context);
    }
    public static Date fromString(String dateStr,String pattern, Date.Type outType) {
        return fromString(dateStr, pattern, outType, TimeZone.getTimeZone("UTC"));
    }
    public static Date fromString(String dateStr, Date.Type outType) {
   return fromString(dateStr,serverDateFormat,outType);
    }
    public static Date fromString(String dateStr,String pattern, Date.Type outType,TimeZone timeZone){
//        String serverdateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
//        String ourdate;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
//            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            formatter.setTimeZone(timeZone);
            java.util.Date value = formatter.parse(dateStr);


            java.util.Calendar calendar= java.util.Calendar.getInstance();
            calendar.setTime(value);
//        } else date1 = new CivilDate(year, month, day);
            Date date1 = new CivilDate(calendar.get(java.util.Calendar.YEAR),calendar.get(java.util.Calendar.MONTH)+1, calendar.get(java.util.Calendar.DAY_OF_MONTH), calendar.get(java.util.Calendar.HOUR_OF_DAY), calendar.get(java.util.Calendar.MINUTE), calendar.get(java.util.Calendar.SECOND));
//        if (inType == Date.Type.persian) date1 = new PersianDate(value.getYear(), value.getMonth(), value.getDay(), value.getHours(), value.getMinutes(), value.getSeconds());
//        else if (inType == Date.Type.islamic) date1 = new IslamicDate(year, month, day);
        return Converter.Convert(date1, Date.Type.gregorian, outType);

//            TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
//            SimpleDateFormat dateFormatter = new SimpleDateFormat(serverdateFormat, Locale.ENGLISH); //this format changeable
//            dateFormatter.setTimeZone(timeZone);


//            ourdate = dateFormatter.format(value);
//            return formatter.getCalendar().getTime().toString();
            //Log.d("OurDate", OurDate);
        } catch (Exception e) {
            e.printStackTrace();
//            ourdate = "0000-00-00 00:00:00";
        }
        return null;
    }
    public static Date getCurrentDate(Date.Type type) {
        CivilDate date = new CivilDate(java.util.Calendar.getInstance());
        return Converter.Convert(date, Date.Type.gregorian, type);
    }
//    public static Date fromString(String dateStr,String pattern, Date.Type inType, Date.Type outType,int timeOffset) {
//
//         //dateStr = "Jul 16, 2013 12:08:59 AM";//"MMM dd, yyyy HH:mm:ss a";
//        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//        java.util.Date d = null;
//        try {
//            d = df.parse(dateStr);
//        } catch (ParseException e) {
//            return null;
//            e.printStackTrace();
//        }
//        df.setTimeZone(TimeZone.getDefault());
////        String formattedDate = df.format(d);
//
//
//
//
//        if (date == null || date.trim().length() < 1) return null;
//        List<String> stepList = new ArrayList<String>();
//        String step = "";
//        for (int i = 0; i < date.length(); i++) {
//            String ch = date.substring(i, i + 1);
//            if (Date.isInt(ch) && i != date.length() - 1) {
//                step += ch;
//                continue;
//            }
//            if (Date.isInt(ch) && i == date.length() - 1) step += ch;
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
//            if (timeOffset!=0){
//                int total=(timeOffset+m);
//                int over=total/60;
//                if (over==0){
//                    m=timeOffset+m;
//                }
//                else if(over>0){
//                    m=total%60;
//                    total=total-over;
//
//                }else {
//
//                }
//                int fh=h+oh;
//            }
//            date1 = new CivilDate(year, month, day, h, m, s);
//        } else date1 = new CivilDate(year, month, day);
//        if (inType == Date.Type.persian) date1 = new PersianDate(year, month, day);
//        else if (inType == Date.Type.islamic) date1 = new IslamicDate(year, month, day);
//        return Converter.Convert(date1, inType, outType);
////		2011-01-01 00:00:01
//    }
}
