package ir.microsign.contentcore.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import ir.microsign.contentcore.view.BookIndexItemView;
import ir.microsign.contentcore.view.CategoryView;
import ir.microsign.dbhelper.object.BaseObject;

/**
 * Created by Mohammad on 6/26/14.
 */
public class BaseAdapter extends ir.microsign.dbhelper.adapter.BaseAdapter {


    public BaseAdapter(Context context, List<BaseObject> contents) {
        super(context, contents);
    }

    public void setItemSelected(int id) {
        setItemSelected("_id", id);
    }

    @Override
    public void onItemViewSelect(View view, boolean selected) {
        if (view instanceof BookIndexItemView) ((BookIndexItemView) view).setSelected(selected, true);
        else if (view instanceof CategoryView) ((CategoryView) view).setSelected(selected, true);
        else
            super.onItemViewSelect(view, selected);
    }


}
