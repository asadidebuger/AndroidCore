package ir.microsign.contentcore.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ir.microsign.R;
import ir.microsign.dbhelper.object.BaseObject;

/**
 * Created by Mohammad on 30/04/2016.
 */
public class SuggestionAdapter extends ArrayAdapter<BaseObject>{

    public SuggestionAdapter(Context context, int resource) {
        super(context, resource);
    }
     public SuggestionAdapter(Context context,List list) {
        super(context, R.layout.layout_search_sugest,R.id.txt_title,list);
    }public SuggestionAdapter(Context context) {
        super(context, R.layout.layout_search_sugest);
    }
    public void fill(List list) {
        clear();
        if (list==null||list.size()<1){getFilter().filter(null);return;}
if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        addAll( list);
        else {
    for (Object o:list)
        add((BaseObject) o);
        }
        getFilter().filter(null);
//        notifyDataSetInvalidated();
//        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(getContext());
    }
}
