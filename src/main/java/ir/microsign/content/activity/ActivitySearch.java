package ir.microsign.content.activity;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.object.Content;



/**
 * Created by Mohammad on 17/12/2014.
 */
public class ActivitySearch extends ir.microsign.contentcore.activity.ActivitySearch {
    public static void show(Context context, String rootCatTag, int catId) {
        Intent intent = new Intent(context, ActivitySearch.class);
        intent.putExtra("current_category", catId);
        intent.putExtra("root_tag", rootCatTag);
        context.startActivity(intent);
    }

    @Override
    public boolean onSearchedItemClicked(String url) {
        ArrayList<String> list = new ArrayList<>();
        for (Object content : mItems)
            list.add(((Content) content)._id);
//	ActivityFullContent.show(this, , list);


        if (url.startsWith("content:article.")) {
            String[] parts=url.split("\\.");

            String sId =parts[1];//. Text.tryGetMatch(url, "\\d+", 0);
//			ArrayList<Integer> ids=new ArrayList<>();
//			ids.add(Integer.parseInt(sId));
            ActivityFullContent.show(this, sId, list);
        }
//		super.onSearchedItemClicked(url);
        return false;
    }

    public DataSource getDataSource() {
//        if (mDataSource == null) mDataSource = new net.tarnian.content.database.DataSource(this);
        return ir.microsign.content.database.DataSource.getDataSource();
    }
}
