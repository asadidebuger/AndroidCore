package ir.microsign.api;

import android.os.Build;

import java.util.HashMap;
import java.util.Locale;

import ir.microsign.calendar.Calendar;
import ir.microsign.context.Application;
import ir.microsign.context.Preference;
import ir.microsign.context.System;
import ir.microsign.R;
import ir.microsign.utility.Text;

public class Utils {

    private static String token=null;
public static int getTimeout(){
    return 15;
}
    public static String getToken() {
        if (token==null)token=Preference.getString(Application.getContext(),"lastToken","");
        return token;
    }

    private static String authorizationToken=null;

    public static String getAuthorizationToken() {
        if (authorizationToken==null)authorizationToken=Preference.getString(Application.getContext(),"authorizationToken","");
        return authorizationToken;
    }
    public static void setAuthorizationToken(String token) {
        if (token==null||token.isEmpty()||token.equals(Utils.token))return;
        Preference.set(Application.getContext(),"authorizationToken",token);
        Utils.authorizationToken = token;
    }
    private static String profileSecret=null;

    public static String getProfileSecret() {
        if (profileSecret==null)profileSecret=Preference.getString(Application.getContext(),"profileSecret","");
        return profileSecret;
    }

    public static void setProfileSecret(String profileSecret) {
        if (profileSecret==null||profileSecret.isEmpty()||profileSecret.equals(Utils.profileSecret))return;
        Utils.profileSecret = profileSecret;
        Preference.set(Application.getContext(),"profileSecret",profileSecret);
    }
    public static void removeProfileSecret() {
        profileSecret = null;
		authorizationToken=null;
		token=null;
        Preference.set(Application.getContext(),"profileSecret","");
        Preference.set(Application.getContext(),"authorizationToken","");
        Preference.set(Application.getContext(),"lastToken","");
    }
    public static void setToken(String token) {
        if (token==null||token.isEmpty()||token.equals(Utils.token))return;
        Preference.set(Application.getContext(),"lastToken",token);
        Utils.token = token;
    }

    private static String serverAddress=null,serverPort=null,serverProtocol=null,serverApiPath;
    public static String getServerAddress(){
//        return "192.168.62.101";
        if (serverAddress==null)serverAddress=Application.getStr(R.string.api_server_address);
        return serverAddress;
//        return "192.168.1.2";
    }
    public static String getConnectionProtocol(){
        if (serverProtocol==null)serverProtocol=Application.getStr(R.string.api_server_protocol);
        return serverProtocol;
    }
    public static String getServerPort(){
        if (serverPort==null)serverPort=Application.getStr(R.string.api_server_port);
        return serverPort;
    }
    public static String getApiPath(){
        if (serverApiPath==null)serverApiPath=Application.getStr(R.string.api_server_path);
        return serverApiPath;
    }
    public static String getAgent(){
        return "microapi";
    }
    public static String getAgentVersion(){
        return "1";
    }
   private static HashMap<String, String> requestHeader =null;
    public static HashMap<String, String> getConstRequestHeader(){
        if (requestHeader !=null)return requestHeader;
        requestHeader =new HashMap<>();
        requestHeader.put("user-agent",getAgent());
        requestHeader.put("model", System.getPhoneModel());
        requestHeader.put("imei", System.getUniqueId(Application.getContext()));
        requestHeader.put("agent-version",getAgentVersion());
        requestHeader.put("pkg",Application.getContext().getPackageName());
        requestHeader.put("pkg-version",Application.getVersionCode()+"");
        requestHeader.put("os-version", Build.VERSION.SDK_INT+"");
        requestHeader.put("Accept-Encoding","x-compress; x-zip");
//        requestHeader.put("Authorization","user  fred:mypassword");
return requestHeader;
    }
    public static HashMap<String, String> getRequestHeader(){
//        HashMap<String, String> requestHeader=getConstRequestHeader();
        getConstRequestHeader().put("token",getToken());
        Utils.requestHeader.put("agent",getAgent());
        Utils.requestHeader.put("Accept-Encoding","x-compress; x-zip");
        Utils.requestHeader.put("Set-Cookie","name=value");
        Utils.requestHeader.put("secret",getProfileSecret());
        Utils.requestHeader.put("Authorization",getAuthorizationToken());
//        requestHeader.put("Authorization","user  fred:mypassword");
return Utils.requestHeader;
    }
   static HashMap<String, String> arrayToParams(String...params){

        HashMap<String, String> p=new HashMap<>();
       if (params==null)return p;
        for (int i = 0; i < params.length; i++) {
            p.put(params[i],params[++i]);
        }
        return p;
    }
    public static String getUrl(String protocol,String address,String port,String path)
    {
        if (path==null)
        return Text.isEmpty(port)?String.format(Locale.ENGLISH,"%s://%s",protocol,address):String.format(Locale.ENGLISH,"%s://%s:%s",protocol,address,port);
        else  return Text.isEmpty(port)?String.format(Locale.ENGLISH,"%s://%s/%s",protocol,address,path):String.format(Locale.ENGLISH,"%s://%s:%s/%s",protocol,address,port,path);
    }
    public static String getApiUrl(){
        return getUrl(getConnectionProtocol(),getServerAddress(),getServerPort(),getApiPath());
    }
    public static String getSimpleUrl(){
        return getUrl(getConnectionProtocol(),getServerAddress(),getServerPort(),null);
    }
////    public static String getApiUrl(String resource,String function){
////      return   String.format("%s/api?resource=%s&func=%s",getApiUrl(),resource,function);
////    }
    private static String serverDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
public static String getDate(String date){
        return Calendar.fromString(date,serverDateFormat, ir.microsign.calendar.Date.Type.persian).toString(true,false);
}public static String getDate(String date,boolean showTime){
        return Calendar.fromString(date,serverDateFormat, ir.microsign.calendar.Date.Type.persian).toString(showTime,false);
}
    public static String getDate(String date,String format,boolean showTime){
        return Calendar.fromString(date,format, ir.microsign.calendar.Date.Type.persian).toString(showTime,false);
    }
    public static  String CurrectDigits(String str) {

		return str
				.replace('۰', '0')
				.replace('۱', '1')
				.replace('۲', '2')
				.replace('۳', '3')
				.replace('۴', '4')
				.replace('۵', '5')
				.replace('۶', '6')
				.replace('۷', '7')
				.replace('۸', '8')
				.replace('۹', '9');
	}


}
