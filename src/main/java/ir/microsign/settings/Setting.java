package ir.microsign.settings;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.R;
import ir.microsign.settings.object.MetaObject;
import ir.microsign.settings.object.SettingItem;

/**
 * Created by Mohammad on 9/9/14.
 */
public class Setting {
    public static class Connection {
        static SettingItem[] mItems = null;

        public static SettingItem[] getAll(Context context) {
            if (mItems != null) return mItems;
            mItems = new SettingItem[1];
            int index = 0;
//			mItems[index++] = new SettingItem(context, Keys.receive_broadcast_this, Keys.prefix, true);
//			mItems[index++] = new SettingItem(context, Keys.receive_broadcast_all, Keys.prefix, true);
//			mItems[index++] = new SettingItem(context, Keys.receive_update, Keys.prefix, true);
//			mItems[index++] = new SettingItem(context, Keys.receive_images, Keys.prefix, true);
//			mItems[index++] = new SettingItem(context, Keys.archive_time_length, Keys.prefix, 0, 0, 365, 1);
//			mItems[index++] = new SettingItem(context, Keys.archive_time_receive, Keys.prefix, 0, 0, 90, 1);
//			mItems[index++] = new SettingItem(context, Keys.images_size, Keys.prefix, 400, 200, 1600, 50);
            mItems[index++] = new SettingItem(context, Keys.parallel_receive, Keys.prefix, context.getResources().getInteger(R.integer.setting_advance_parallel_receive_default), 20, 1500, 10);

            return mItems;
        }

        public static int getParallelReceive(Context context) {
            return (Integer) getAll(context)[0].getValue(getAll(context)[0].getDefaultValue());
        }

        public static boolean getRecieveBroadcast(Context context) {
            return (Boolean) getAll(context)[0].getValue(getAll(context)[0].getDefaultValue());
        }

        public static class Keys {
            public static String parallel_receive = "parallel_receive",
                    archive_time_length = "archive_time_length",
                    archive_time_receive = "archive_time_receive",
                    receive_broadcast_this = "receive_broadcast_this",
                    receive_broadcast_all = "receive_broadcast_all",
                    receive_update = "receive_update",
                    receive_images = "receive_images",
                    images_size = "images_size",
                    prefix = "setting_connection_";
        }

    }

    public static class Advance {
        static List<SettingItem> mItems = null;
        static List<SettingItem> mExtra = null;
        static List<MetaObject> mListLocale = null;
        static String[] mLocale, mLocaleTitle, mLocaleDesc;
        static List<MetaObject> mListCalendar = null;
        static String[] mCalendar, mCalendarTitle, mCalendarDesc;

        public static List<SettingItem> getExtra() {
            return mExtra;
        }

        public static void AddExtra(List<SettingItem> items) {
            mExtra = items;
        }

        public static List<SettingItem> getAll(Context context) {
//            if (mItems != null) return mItems;
            mItems = new ArrayList<SettingItem>();
//			int index = 0;
            mItems.add(new SettingItem(context, Keys.locale, Keys.prefix, 0, getListLocale(context)));
            mItems.add(new SettingItem(context, Keys.calendar, Keys.prefix, 1, getListCalendar(context)));
            if (mExtra != null && mExtra.size() > 0) mItems.addAll(mExtra);
            return mItems;
        }

        public static String getCalendarType(Context context) {
            return getListCalendar(context).get((Integer) getAll(context).get(1).getRegistedValue()).getObject().toString();
        }

        public static String getLocale(Context context) {
            return getListLocale(context).get((Integer) getAll(context).get(0).getRegistedValue()).getObject().toString();
        }

        public static List<MetaObject> getListLocale(Context context) {
            if (mListLocale != null) return mListLocale;
            mListLocale = new ArrayList<MetaObject>();
            mLocale = context.getResources().getStringArray(R.array.locales);
            mLocaleTitle = context.getResources().getStringArray(R.array.locales_title);
            mLocaleDesc = context.getResources().getStringArray(R.array.locales_desc);
            for (int i = 0; i < mLocale.length; i++)
                mListLocale.add(new MetaObject(mLocale[i], mLocaleTitle[i], mLocaleDesc[i]));
            return mListLocale;
        }

        public static List<MetaObject> getListCalendar(Context context) {
            if (mListCalendar != null) return mListCalendar;
            mListCalendar = new ArrayList<MetaObject>();
            mCalendar = context.getResources().getStringArray(R.array.calendar_types);
            mCalendarTitle = context.getResources().getStringArray(R.array.calendar_title);
            mCalendarDesc = context.getResources().getStringArray(R.array.calendar_desc);
            for (int i = 0; i < mCalendar.length; i++)
                mListCalendar.add(new MetaObject(mCalendar[i], mCalendarTitle[i], mCalendarDesc[i]));
            return mListCalendar;
        }

        public static class Keys {
            public static String
                    locale = "locale",
                    calendar = "calendar",
                    prefix = "setting_advance_";
        }
    }
}
