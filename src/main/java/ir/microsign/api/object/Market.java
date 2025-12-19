package ir.microsign.api.object;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.R;
import ir.microsign.utility.Text;

public class Market extends BaseObject {
    public String name,key,url,market;
    static List<Market> fromJson(String json){
        List<Market> list=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                try {

                    list.add((Market)  new Market().initFormJSONObject(jsonObject));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public String getHtml(Context context){
    	if (Text.isNullOrEmpty(url))return "";
        String html=  Text.ReadResTxt(context, R.raw.market);
        html= html.replace("__name",name).replace("__url",url);
        return html;
    }



	@Override
	public boolean equals(Object obj) {
    	return market.equalsIgnoreCase(((Market)obj).market);
    }

	//    @Override
//    public String toString() {
//        return market;
//    }
    public Market setPkgName(String pkg){
    	market=pkg;
    	return this;
	}
}
