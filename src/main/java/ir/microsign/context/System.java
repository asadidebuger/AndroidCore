package ir.microsign.context;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.microsign.R;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 9/25/14.
 */
public class System {

    static String mAndroidId = null;

    public static String getAndroidId(Context context) {
        if (mAndroidId != null) return mAndroidId;
        mAndroidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (mAndroidId==null||mAndroidId.trim().isEmpty()||"9774d56d682e549c".equals(mAndroidId))
            mAndroidId = getUniqueId(context);

        return mAndroidId;
    }
//    public static List<String>  getIMEI(Context context){
//        TelephonyManager TelephonyMgr = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        List<String> imeiList=new ArrayList<>();
//
//        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.M){
//            for (int i=0;i<3;i++)
//            {
//                String imei=TelephonyMgr.getDeviceId(i);
//                if (!imeiList.contains(imei))imeiList.add(imei);
//            }
//                String m_szImei = ;
//        }
//
//        return m_szImei;
//    }

    public static String getUniqueId(Context context){
        if (!Application.checkPermission(Manifest.permission.READ_PHONE_STATE))return null;
        try{
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyMgr.getDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!Application.checkPermission(Manifest.permission.ACCESS_WIFI_STATE))return null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wInfo = wifiManager.getConnectionInfo();
       return wInfo.getMacAddress();
    }
    public static String getPhoneModel() {
        return Build.MODEL;
    }

//
    public static String getAndroidVersion() {
        return String.format(Locale.ENGLISH, "%d", Build.VERSION.SDK_INT);
    }
//
//    public static String getPackageName(Context context) {
//        return context.getPackageName();
//    }
//
////    public static List<org.apache.http.NameValuePair> getDeviceParams(Context context) {
////        List<org.apache.http.NameValuePair> params = new ArrayList<NameValuePair>();
////        params.add(new BasicNameValuePair(Utility.CONST.TAG_PHONE_MODEL, getPhoneModel()));
////        params.add(new BasicNameValuePair(Utility.CONST.TAG_PACKAGE_NAME, getPackageName(context)));
////        params.add(new BasicNameValuePair(Utility.CONST.TAG_ANDROID_ID, getAndroidId(context)));
////        params.add(new BasicNameValuePair(Utility.CONST.TAG_ANDROID_VERSION, getAndroidVersion()));
////        return params;
////    }
//
    public static String getAppVersion(Context context) {
        try {
//			Context context = activity.getContext();
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return e.getMessage();
        }
    }
//
    public static String getAppVersionCode(Context context) {
        try {
//			Context context = activity.getContext();
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return String.format("%d", pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return e.getMessage();
        }
    }
    public interface permissionListener{
        void onResult(boolean ok);

    }
  public   static void tryPermission(Activity activity, String[] permissions,int requestCode,permissionListener l) {
        List<String> should = new ArrayList();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    activity, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    view.showToast(activity, activity.getString(R.string.alert_permission_need_grant, permission));
                } else {
                    should.add(permission);
                }
            }

        }
      if (should.size()<1){
            l.onResult(true);
          return;
      }


        String[] a = new String[should.size()];
	  for (int i = 0, shouldSize = should.size(); i < shouldSize; i++) {
		  a[i]= should.get(i);
	  }
        ActivityCompat.requestPermissions(activity, a, requestCode);

    }
//    public static void onResultGrab(){
//
//    }
//
//    public static void setLocale(Context context, String lang) {
//        Resources res = context.getResources();
//        // Change locale settings in the app.
//        DisplayMetrics dm = res.getDisplayMetrics();
//        android.content.res.Configuration conf = res.getConfiguration();
//        conf.locale = new Locale(lang.toLowerCase());
//        res.updateConfiguration(conf, dm);
//    }

//    public static String getDeviceID(Context context) {
//
///*String Return_DeviceID = USERNAME_and_PASSWORD.getString(DeviceID_key,"Guest");
//return Return_DeviceID;*/
//
//        TelephonyManager TelephonyMgr = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String m_szImei = TelephonyMgr.getDeviceId(); // Requires
//        if (m_szImei!=null&&m_szImei.replace("0","").length()>1)return m_szImei;
////        if (getPhoneModel().equals("sdk")) return "1";
//// READ_PHONE_STATE
//
//// 2 compute DEVICE ID
//        String m_szDevIDShort = "35"
//                 // we make this look like a valid IMEI
//                +Build.BOARD.length() % 10 + Build.BRAND.length() % 10
//                + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
//                + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
//                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
//                + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
//                + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
//                + Build.USER.length() % 10; // 13 digits
//        if (m_szDevIDShort.replace("0","").length()>2) return m_szDevIDShort;
//        // 3 android ID - unreliable
//        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//// 4 wifi manager, read MAC address - requires
//// android.permission.ACCESS_WIFI_STATE or comes as null
//        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
//// 5 Bluetooth MAC address android.permission.BLUETOOTH required
//        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String m_szBTMAC = m_BluetoothAdapter.getAddress();
////		System.out.println("m_szBTMAC "+m_szBTMAC);
//
//// 6 SUM THE IDs
//        String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
////		System.out.println("m_szLongID "+m_szLongID);
//        MessageDigest m = null;
//        try {
//            m = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
//        byte p_md5Data[] = m.digest();
//
//        String m_szUniqueID = new String();
//        for (int i = 0; i < p_md5Data.length; i++) {
//            int b = (0xFF & p_md5Data[i]);
//// if it is a single digit, make sure it have 0 in front (proper
//// padding)
//            if (b <= 0xF)
//                m_szUniqueID += "0";
//// add number to string
//            m_szUniqueID += Integer.toHexString(b);
//        }
//        m_szUniqueID = m_szUniqueID.toUpperCase();
//
////		Log.i("-------------DeviceID------------", m_szUniqueID);
////		Log.d("DeviceIdCheck", "DeviceId that generated MPreferenceActivity:"+m_szUniqueID);
//
//        return m_szUniqueID;
//
//    }
}
