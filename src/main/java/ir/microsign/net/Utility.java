package ir.microsign.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;

import ir.microsign.context.Application;
import ir.microsign.dialog.ConnectDialog;

/**
 * Created by Mohammad on 9/25/14.
 */
public class Utility {

    public static boolean isConnect(Context context) {
//        if (true)return true;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (NetworkInfo anInfo : info)
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void CheckInternet(Context context, ConnectDialog.OnDialogResultListener l) {
//        ConnectDialog mCheckInternetDialog = new ConnectDialog(context);
//        mCheckInternetDialog.setOnDialogResultListener(l);
        ConnectDialog.show(context,l);

    }
    static Vibrator vb;

    public static void Vibrate() {
        Vibrate(Application.getContext(),10);
    }

    public static void Vibrate(Context context,int t) {
        try {
            if (vb == null)
                vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vb.vibrate(t);
        } catch (Exception ex) {
        }
    }

//    public static String getUrlFileName(String url) {
//        if (!url.contains("/")) return url;
//        return url.substring(url.lastIndexOf("/") + 1);
//    }

}
