package ir.microsign.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.PowerManager;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by Mohammad on 9/25/14.
 */
public class Display {
    static protected PowerManager.WakeLock mWakeLock;
    static PowerManager.WakeLock wl = null;
    static PowerManager pm = null;

    //	static Activity mActivity = null;
    public static int getScreenOrientation(Context context,WindowManager windowManager) {
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int width = getWidth(context);
        int height = getHeight(context);
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }
        return orientation;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isLandscape(Context context) {
        int display_mode = context.getResources().getConfiguration().orientation;

        return (display_mode != 1);
    }

//    public static int getWidth(Context context) {
//        return context.getResources().getDisplayMetrics().widthPixels;
//    }

    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

//    public static int getHeight() {
//        return context.getResources().getDisplayMetrics().heightPixels;
//    }

    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getHeightWithoutBar(Context context) {
        return getHeight(context) - getStatusBarHeight(context);
    }
//    public static int getHeightWithoutBar(Context context) {
//        return getHeight() - getStatusBarHeight();
//    }
    public static int pxToDp(Context context, int px) {
        return (int) ((px / context.getResources().getDisplayMetrics().density) + 0.5);
    }

    public static int pxToDp(Resources resources, int px) {
        return (int) ((px / resources.getDisplayMetrics().density) + 0.5);
    }

    public static int dpToPx(Context context, float dp) {
        return (int) ((dp * context.getResources().getDisplayMetrics().density) + 0.5);
    }

    public static int dpToPx(Resources resources, float dp) {
        return (int) ((dp * resources.getDisplayMetrics().density) + 0.5);
    }

    public static int spToPx(Context context, float sp) {
        return (int) ((sp * context.getResources().getDisplayMetrics().scaledDensity) + 0.5);
    }

    public static int spToPx(Resources resources, float sp) {
        return (int) ((sp * resources.getDisplayMetrics().scaledDensity) + 0.5);
    }
static int LAST_SCREEN_ORIENTATION=-1;
    @SuppressLint("WrongConstant")
    public static void fixScreen(Activity activity) {
        if (activity == null) return;
        LAST_SCREEN_ORIENTATION=activity.getRequestedOrientation();
//        if (activity.getActivity().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED||
//                activity.getActivity().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)return;        if (activity.getActivity().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE||
//                activity.getActivity().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)return;

        activity.setRequestedOrientation(
//                    ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
                    getScreenOrientation(activity,activity.getWindowManager())
                     );
    }

    public static void releasScreenOn() {
        if (mWakeLock == null) return;
        mWakeLock.release();
    }
    public static void keepAwake(Context activity) {
        pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Dont Lock");
        wl.acquire();
    }
    public static void keepScreenOn(Context activity) {
        keepAwake(activity);
    }
    public static void keepAwakeOff() {
        try{if (wl == null) return;
        wl.release();}
        catch (Exception ex){ex.printStackTrace();}
    }

    public static void fixScreenLandscape(Activity act) {
//        activity.setActivity(act);
        if (act == null) return;
        act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void fixScreenPortrait(Activity act) {
//        activity.setActivity(act);
        if (act == null) return;
        act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    public static void fixScreen(Activity act) {
//        activity.setActivity(act);
//
//        fixScreen();
//    }

//    @SuppressLint("WrongConstant")
//    public static void releaseScreen(Activity activity) {
//        if (activity== null) return;
//        activity.setRequestedOrientation(
//                LAST_SCREEN_ORIENTATION
////                ActivityInfo.SCREEN_ORIENTATION_SENSOR
//        );
//    }

    @SuppressLint("WrongConstant")
    public static void releaseScreen(Activity activity) {
        if (activity == null) return;
        activity.setRequestedOrientation(
                LAST_SCREEN_ORIENTATION
//                ActivityInfo.SCREEN_ORIENTATION_SENSOR
        );
    }


}
