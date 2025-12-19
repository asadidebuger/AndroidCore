package ir.microsign.context;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.ContextCompat;
import android.os.StrictMode;
import android.util.DisplayMetrics;


import java.lang.System;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 3/9/14.
 */
public class Application extends android.app.Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        if (Application.context != null) return;
        Application.context = context;

    }
    public static boolean checkPermission(String permission) {
        int res = getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    public static int getVersionCode(){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getVersionCode(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static String getVersionName(){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }    public static String getVersionName(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
//    public static  void setLocale(Locale locale) {
////		Log.d(TAG+"set location function: "+localeCode);
////		Locale locale = new Locale(localeCode);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
////        getContext().getBaseContext().getResources().updateConfiguration(config, displayMetrics);
////        getApplicationContext().getResources().updateConfiguration(config, displayMetrics);
//        getContext().getResources().updateConfiguration(config, displayMetrics);
//    }

    @SuppressWarnings("deprecation")
    public static void setLocale(Locale locale){
        Resources resources = getContext().getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
            getContext().getApplicationContext().createConfigurationContext(configuration);

        }
        else{
            configuration.locale=locale;
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }
//    public static String getProPackageName(Context context) {
//        String pkg = context.getPackageName().toLowerCase();
//        if (pkg.endsWith("_demo")) pkg = context.getPackageName().substring(0, pkg.length() - 5);
//        else if (pkg.endsWith("demo")) pkg = context.getPackageName().substring(0, pkg.length() - 4);
//        return pkg;
//    }

    public static boolean isDemo(Context context) {
        return !context.getPackageName().equals(context.getPackageName());
    }

    public static void PrepareRestartApplication(Activity context, Class<?> _class, int delay) {
        PendingIntent p = PendingIntent.getActivity(context, 0, new Intent(context, _class), 0);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + delay, p);

    }

    @Override
    public void onCreate() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate();
        context = getCustomContext(this);// getApplicationContext();
    }

    public Context getCustomContext(Context context) {
        String path = context.getString(ir.microsign.R.string.app_custom_context_class_path);
        if (Text.isNullOrEmpty(path)) return context;

        try {
            return (Context) Class.forName(path).getMethod(context.getString(ir.microsign.R.string.app_custom_context_method),
                    Context.class)
                    .invoke(null, context);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }

public static Locale getCurrentLocale(){
    return getContext().getResources().getConfiguration().locale;
}

    //******************************************************************************
    // onCreate
    //******************************************************************************
//    public static Context getContext() { return context; }

    //******************************************************************************
    // getStr
    //******************************************************************************
    public static String getStr(int id) { return context.getResources().getString(id); }

    //******************************************************************************
    // getInt
    //******************************************************************************
    public static int getInt(int id)
    {
        return context.getResources().getInteger(id);
    }

    //******************************************************************************
    // getClr
    //******************************************************************************
    public static int getClr(int id)
    {
        return context.getResources().getColor(id);
    }

    //******************************************************************************
    // error
    //******************************************************************************
    public static void error(Context context, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //******************************************************************************
    // error
    //******************************************************************************
    public static void error(Context context, int id)
    {
        error(context, getStr(id));
    }

    //******************************************************************************
    // error
    //******************************************************************************
    public static void error(String message)
    {
        error(context, message);
    }

    //******************************************************************************
    // error
    //******************************************************************************
    public static void error(int id)
    {
        error(getStr(id));
    }

    public static void exitApp(Activity activity) {
        activity.finish();
        activity.moveTaskToBack(true);
        System.exit(0);
    }
}
