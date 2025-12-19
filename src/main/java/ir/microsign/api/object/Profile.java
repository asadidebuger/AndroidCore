package ir.microsign.api.object;

import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.microsign.api.Utils;
import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.view.ProfileImageView;
import ir.microsign.utility.File;
import ir.microsign.utility.Text;

public class Profile extends ir.microsign.dbhelper.object.BaseObject {
  public   enum UserNameType {
        email,tel,code
    }    public String _id,first,last,email,tel,code,permission,lastVisit,password, images;
public Integer autoid,sex;
public static String avatar=null;
public String getFullName(){
    return (first!=null?first+" ":"")+(last!=null?last:"");
}
public UserNameType getUserNameType(){
    if (Text.notEmpty(code))return UserNameType.code;
    if (Text.notEmpty(tel))return UserNameType.tel;
    return UserNameType.email;
//    return Text.isNullOrEmpty(email)&&!Text.isNullOrEmpty(tel);
}

public boolean hasPermission(String ids){
    if (Text.isEmpty(permission))return false;
    if (!ids.contains("["))
    return permission.contains(ids);
    try {
        JSONArray idArray=new JSONArray(ids);
        for (int i=0,l= idArray.length();i<l;i++)
            if (!permission.contains(idArray.getString(i)))return false;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return true;

}
public boolean hasPermission(String [] ids){
        if (Text.isEmpty(permission))return false;
            for (int i=0,l= ids.length;i<l;i++)
                if (!permission.contains(ids[i]))return false;
        return true;
    }
public boolean emailIsValid(){
	 String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    // Pattern match for email id
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(email);
     return m.matches();
}
public boolean codeIsValid(){
    return Text.notEmpty(code);
//	 String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
//
//    // Pattern match for email id
//    Pattern p = Pattern.compile(regEx);
//    Matcher m = p.matcher(email);
//     return m.matches();
}
public boolean phoneIsValid(String tel){
    String regEx = "\\b0?[0-9]{10}\\b";

    // Pattern match for email id
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(tel);
    return m.matches();
}
public boolean phoneIsValid(){
    String regEx = "\\b0?[0-9]{10}\\b";

    // Pattern match for email id
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(tel);
    return m.matches();
}
public void setPhone(String countryCode,String tel){
    if (tel.startsWith("0"))tel=tel.substring(1);
    this.tel=countryCode+" "+tel;
}
public String getCountryCode(){
    if (tel==null)return "";
    String[] c=tel.split(" ");
    if (c[0].startsWith("+"))return c[0];
    return "";
}
public String getPurePhone(){
    if (tel==null)return "";
    String[] c=tel.split(" ");
    if (c.length>1&&c[0].startsWith("+"))return c[1];
    return "";
}
public int getSexIndex(){
    switch (sex){
        case 1:return 0;
        case 2:return 1;
    }
    return 0;
}
public void setSexFromIndex(int index){
   sex=getSexFromIndex(index);
}
public int getSexFromIndex(int sex){
    switch (sex){
        case 0:return 1;
        case 1:return 2;
    }
    return 0;
}
//    public static boolean isLogin(){
//
//        return  Text.notEmpty(Utils.getProfileSecret());
//    }
    public static boolean isSignIn(){
        return !Text.isNullOrEmpty(Utils.getProfileSecret());
    }
public static Profile getSavedProfile(Context context){
 return    DataSourceApi.getSource(context).getProfile();
}public static Profile getSavedProfileOrEmpty(Context context){
	if (!isSignIn())return new Profile();
 Profile p=    DataSourceApi.getSource(context).getProfile();
 return p==null?new Profile():p;
}

    @Override
    public List<String> getExceptionFields() {
        List<String> ex= super.getExceptionFields();
        ex.add("avatar");
        return ex;
    }
    static List<String> listFromJson(String json){
        List<String> list=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    list.add(jsonArray.getString(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static void insertProfile(Context context,Profile p){
    Profile profile=getSavedProfileOrEmpty(context);
        p.password = profile.password;
        DataSourceApi.getSource(context).insertProfile(p);
    }
    public String getAvatarUrl() {
    if (!isSignIn())return null;
    if (avatar!=null)return avatar;
        avatar="";
        if (Text.isNullOrEmpty(images)) {return avatar;}
        List<String> images= listFromJson(this.images);
        if (images.size()<1)return avatar;
        avatar=images.get(images.size()-1);
        if (avatar.startsWith("http://") || avatar.startsWith("https://")) return avatar;

        avatar= Utils.getUrl(Utils.getConnectionProtocol(),Utils.getServerAddress(),Utils.getServerPort(),null)+avatar;
        return avatar;
    }

    public String getAvatarSavePath(Context context) {
        if (Text.isNullOrEmpty(getAvatarUrl())) return "";
        return File.GetRoot(context) + "/avatar/" + File.ConvertUrlToStoragePath(getAvatarUrl());
    }
    public ProfileImageView getProfileImageView(Context context){
    return new ProfileImageView(context,this);
    }

    @Override
    public boolean equals(Object obj) {
    if (!(obj instanceof Profile))return false;
    Profile p= (Profile) obj;
        return (((first==null&&p.first==null)
                        ||(p.first!=null&&p.first.equals(first)))&&
				((last==null&&p.last==null)
                        ||(p.last!=null&&p.last.equals(last)))&&
				((images==null&&p.images==null)
                        ||(p.images!=null&&p.images.equals(images))));
    }
    //    @Override
//    public String[] getMapColumns() {
//        return new String[]
//    }

    //    @Override
//    public String[] getAllColumns() {
//        return super.getAllColumns();
//    }
}
