package ir.microsign.api.object;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 03/12/2017.
 */

public class Response {
    okhttp3.Response response=null;
    String string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    public enum Type{
        undefined,
        error,
        json,
        text,


    }
    public String getHeader(String key,String def){
        if (getResponse()==null)return def;
        return getResponse().header(key,def);
    }
//    public String getCookie(String key,String def){
//        if (getResponse()==null)return def;
//        return getResponse().headers("Set-Cookie");
//    }
    public boolean succeed(){
        return response!=null&&response.isSuccessful();
    }
    Type type=Type.undefined;
    public Response(okhttp3.Response response){
        if (response==null){
            string="";
            return;
        }
        this.response=response;
        if (response.isSuccessful()){
//            response.message()
            String type=response.header("Content-Type");
            if (Text.isNullOrEmpty(type)){
                this.type=Type.undefined;
                try {
                    string=response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }
            if (type.toLowerCase().contains("json")){
                try {
                    this.type=Type.json;

                    string=response.body().string();
                    jsonObject=new JSONObject(string);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
//                    e.printStackTrace();
                    try {
                        jsonArray=new JSONArray(string);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            }
             if (type.toLowerCase().contains("text/html")){
                try {
                    this.type=Type.text;

                    string=response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

        }
        else {
            try {
                this.type=Type.error;

                string=response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getString() {
        return string;
    }
//    public String getError() {
//        return string;
//    }

    public okhttp3.Response getResponse() {
        return response;
    }
//    public List<BaseObject> getObjects(Class T){
//        if (jsonObject==null&&jsonArray==null)return new ArrayList<>();
//        if (jsonObject!=null)return castArray(jsonObject,T);
////        try {
//            return CastArray(jsonArray,baseObject);
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////return new ArrayList<>();
//    }
    public <T> T getObject(Class T, String func){
        try {

            JSONObject object= (JSONObject) getJsonObject().get(func);

            return castObject(T,object);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public  static <T> T castObject(Class T,JSONObject object){
        try {
            BaseObject baseObject= (BaseObject) BaseObject.fromJsonObject(T,object);
            baseObject.initFormJSONObject(object);
            return (T) baseObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<BaseObject> getObjectArray(Class T, String func) {
        if (jsonObject==null)return new ArrayList<BaseObject>();
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(func);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<BaseObject>();
        }
        List<BaseObject> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                BaseObject baseObject= castObject(T,jsonArray.getJSONObject(i));
                if (baseObject!=null)
                result.add(baseObject);
                else Log.e("Response.getObjectArray","is null:"+jsonArray.toString());
            } catch (Exception  e) {
                Log.e("ReceiverAdaptor","CastArray"+i+"|"+e.getMessage());
                // e.printStackTrace();
            }
        }
        return result;
    }
    public List<BaseObject> getObjectArray(Class T, JSONObject jsonObject,String arrayName) {
        if (jsonObject==null)return new ArrayList<BaseObject>();
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(arrayName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<BaseObject>();
        }
        List<BaseObject> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                result.add((BaseObject) castObject(T,jsonArray.getJSONObject(i)));
            } catch (Exception  e) {
                Log.e("ReceiverAdaptor","CastArray"+i+"|"+e.getMessage());
                // e.printStackTrace();
            }
        }
        return result;
    }
//    public List<BaseObject> CastArray(JSONArray jsonArray) {
////        JSONArray jsonArray = null;
////        try {
////            jsonArray = jsonObject.getJSONArray(baseObject.getJsonArrayName());
////        } catch (Exception e) {
////            e.printStackTrace();
////            return new ArrayList<BaseObject>();
////        }
//        List<BaseObject> result = new ArrayList<BaseObject>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                result.add(castObject(jsonArray.getJSONObject(i), baseObject));
//            } catch (Exception  e) {
//                Log.e("ReceiverAdaptor","CastArray"+i+"|"+e.getMessage());
//                // e.printStackTrace();
//            }
//        }
//        return result;
//    }
//    public BaseObject CastObject(JSONObject jsonObject, BaseObject baseObject) {
//        BaseObject baseObject1 = baseObject.newInstance();
//        baseObject1.initFormJSONObject(jsonObject);
//        return baseObject1;
//    }
}
