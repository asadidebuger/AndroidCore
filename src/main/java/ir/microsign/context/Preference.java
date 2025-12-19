package ir.microsign.context;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Mohammad on 9/25/14.
 */
public class Preference {
    static SharedPreferences mPreferences;

    public static void setSharedPreferences(Context context) {
        if (mPreferences == null) mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void set(Context context, String key, Object value) {
        setSharedPreferences(context);
        SharedPreferences.Editor editor = mPreferences.edit();
        if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof Float)
            editor.putFloat(key, (Float) value);
        else if (value instanceof Long)
            editor.putLong(key, (Long) value);
        else if (value instanceof Double)
            editor.putFloat(key,((Double) value).floatValue());
        editor.commit();
    }
    public static void remove(Context context, String key) {
        setSharedPreferences(context);
        SharedPreferences.Editor editor = mPreferences.edit();
     editor.remove(key);
        editor.commit();
    }

    public static Object getValue(Context context, String key, Object defValue) {
        setSharedPreferences(context);
        if (defValue instanceof Boolean)
            return mPreferences.getBoolean(key, (Boolean) defValue);
        else if (defValue instanceof String)
            return mPreferences.getString(key, (String) defValue);
        else if (defValue instanceof Integer)
            return mPreferences.getInt(key, (Integer) defValue);
        else if (defValue instanceof Float)
            return mPreferences.getFloat(key, (Float) defValue);
        else if (defValue instanceof Long)
            return mPreferences.getLong(key, (Long) defValue);
        else if (defValue instanceof Double)
            return ((Float)mPreferences.getFloat(key, (((Double) defValue).floatValue()))).doubleValue();
        return defValue;


    }

    public static boolean getBool(Context context, String key, boolean defValue) {
        return (Boolean) getValue(context, key, defValue);

    }
    public static Integer getInteger(Context context, String key, Integer defValue) {
        return (Integer) getValue(context, key, defValue);
    }


    public static Long getLong(Context context, String key, Long defValue) {
        return (Long) getValue(context, key, defValue);
    }
    public static Double getDouble(Context context, String key, Double defValue) {
        return (Double) getValue(context, key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        return (String) getValue(context, key, defValue);
    }


}
