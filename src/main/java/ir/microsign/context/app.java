package ir.microsign.context;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
//import androidx.core.content.FileProvider;

import cn.pedant.SweetAlert.widget.SweetAlertDialog;
import ir.microsign.utility.File;
import ir.microsign.R;
import ir.microsign.utility.Text;
import ir.microsign.net.Utility;
import ir.microsign.utility.view;
//import ir.microsign.dialog.MessageDialog;
//import ir.microsign.network.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad on 14/12/2014.
 */
public class app {
    public static boolean mRateLunched = false;

    public static void share(Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("*/*");
        String path = null;

		try {
			path=context.getPackageManager().getPackageInfo(context.getPackageName(),0).applicationInfo.publicSourceDir;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
//		String storage
//		File.copyFile(new FileInputStream(path),File.GetRoot());
//		(new java.io.File(path));
//        for (int i = 0; i < 100; i++)
//            if (File.Exist("data/app/" + context.getPackageName() + "-" + i + ".apk"))
//            {
//                path = "data/app/" + context.getPackageName() + "-" + i + ".apk";break;
//            }
        if (path == null) return;

//        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".microsign" , new java.io.File(path)	);
        Uri uri = Uri.fromFile(new java.io.File(path));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(shareIntent);
    }
    public static String getApkPath(Context context){
        String path = null;
        for (int i = 0; i < 100; i++)
            if (File.Exist("data/app/" + context.getPackageName() + "-" + i + ".apk"))
            {path = "data/app/" + context.getPackageName() + "-" + i + ".apk";break;}
        if (path == null) return null;
        return path;
    }

    public static String getAppLabelByPackage(Context ctx, String packagename) {
        ArrayList<PackageInfo> res = new ArrayList<PackageInfo>();
        PackageManager pm = ctx.getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);

        for (PackageInfo p : packs) {
            if (packagename.equalsIgnoreCase(p.packageName))
                return (String) p.applicationInfo.loadLabel(pm);

//
//			String description = (String) p.applicationInfo.loadDescription(pm);
//			String  label= p.applicationInfo.loadLabel(pm).toString();
//			String packageName = p.packageName;
//			String versionName = p.versionName;
//			int versionCode = p.versionCode;
//			String icon = p.applicationInfo.loadIcon(pm);
        }
        return packagename.substring(packagename.lastIndexOf(".") + 1);
    }

    public static boolean packageExist(Context context, String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage)) return true;
        }
        return false;
    }
    public static boolean hasPermission(Context context,String permissionPattern)
    {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (Text.isMatchByRegex(p,permissionPattern)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void exitApp(Activity activity) {
        activity.finish();
        activity.moveTaskToBack(true);
        java.lang.System.exit(0);
    }

    public static void showPleaseRate(final Activity activity) {
		if (getAppRated(activity)) {
			mRateLunched = true;
			return;
		}

        if (!Utility.isConnect(activity)) return;

        new SweetAlertDialog( activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(R.string.message_rate_app_title)
                .setContentText(R.string.message_rate_app_desc)
                .setCancelText(R.string.btn_cancel_rate_app)
                .setConfirmText(R.string.btn_ok_rate_app)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.setContentText(R.string.message_rate_app_cancel_desc).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								mRateLunched = true;
								sDialog.dismiss();
								activity.finish();


							}
						}).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								mRateLunched = true;
								sDialog.dismiss();
								setAppRated(activity);
								activity.finish();
							}
						}).setCancelText(R.string.btn_rate_app_never).setConfirmText(R.string.btn_rate_app_later).changeAlertType(SweetAlertDialog.ERROR_TYPE);

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        launchMarketRate(activity);
                    }
                })
                .show();

    }

    public static boolean getAppRated(Context context) {
        return Preference.getBool(context, "app_rated_or_canceled", false);
    }

    public static void setAppRated(Context context) {
		setAppRated(context,false);
//        Preference.set(context, "app_rated_or_canceled", true);
//        _view.showToast(context, context.getString(R.string.message_finished_rate_app), 1000);
    }
	public static void setAppRated(Context context,boolean showThanks) {
		Preference.set(context, "app_rated_or_canceled", true);
		if (showThanks) view.showToast(context,R.string.message_finished_rate_app);
//        _view.showToast(context, context.getString(R.string.message_finished_rate_app), 1000);
	}
    public static boolean getAppRateLunched(Context context) {
        return mRateLunched || getAppRated(context) || !Utility.isConnect(context);
    }

    public static void launchMarketRate(Activity context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//        Intent myAppLinkToMarket = new Intent(Intent.ACTION_EDIT, uri);
        try {
            context.startActivityForResult(myAppLinkToMarket, 5);

        } catch (Exception e) {

            //Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}
