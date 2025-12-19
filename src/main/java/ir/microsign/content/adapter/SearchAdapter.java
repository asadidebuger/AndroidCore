package ir.microsign.content.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.microsign.content.object.Content;
import ir.microsign.dbhelper.object.BaseObject;

//import net.tarnian.content.object.BaseObject;

/**
 * Created by Mohammad on 6/26/14.
 */
public class SearchAdapter extends BaseAdapter {

    public SearchAdapter(Context context, List<BaseObject> contents) {
        super(context, contents);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = ((Content) getItem(position)).getSearchView(mContext);
        return convertView;
    }
}
