package ir.microsign.api.object;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.api.Utils;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.R;
import ir.microsign.utility.Text;

public class ChangeLog extends BaseObject {
    public String date,versionCode,version,file,changes;

    static List<ChangeLog> fromJson(String json){
        List<ChangeLog> list=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                try {

                    list.add((ChangeLog)  new ChangeLog().initFormJSONObject(jsonObject));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    String getChanges(){
        if (changes==null)return "";
        String output="<ul>";
        JSONArray array= null;
        try {
            array = new JSONArray(changes);

        for (int i=0;i<array.length();i++)
        {
            output+="<li>"+array.getString(i)+"</li>";
        }

        } catch (Exception e) {
        e.printStackTrace();

    }
    output+="</ul>";
        return output;
    }
    public String getHtml(Context context){
        String html=  Text.ReadResTxt(context, R.raw.changelog);
        html= html.replace("__version",context.getString(R.string.txt_intro_version_title,version)).replace("__date", Utils.getDate(date,false)).replace("__changes",getChanges());
        return html;
    }
}
