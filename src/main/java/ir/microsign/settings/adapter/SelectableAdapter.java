package ir.microsign.settings.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.settings.object.SelectableItem;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/26/14.
 */
public class SelectableAdapter extends android.widget.BaseAdapter {
    List<SelectableItem> mItems = null;
    List<SelectableItem> mItemsOrigin = null;
    Context mContext = null;

    public SelectableAdapter(Context context, List<SelectableItem> items) {
        mItemsOrigin = items;
        mContext = context;
        filter(null);
    }

    public void filter(String key) {
        mItems = new ArrayList<SelectableItem>();
        if (key == null || key.length() < 1) {
            mItems.addAll(mItemsOrigin);
            return;
        }
        for (SelectableItem item : mItemsOrigin)
            if (isContain(item.getTitle(), key) || isContain(item.getDescription(), key)) mItems.add(item);
    }

    boolean isContain(String src, String key) {
        if (src == null) return false;
        return Text.FixString(src).toLowerCase().contains(Text.FixString(key).toLowerCase());
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems == null || mItems.size() < position ? null :
                mItems.get(position);
    }

    public SelectableItem get(int position) {
        return mItems == null || mItems.size() < position ? null :
                mItems.get(position);
    }

    public List<SelectableItem> getItems() {
        return mItemsOrigin;
    }


    @Override
    public long getItemId(int position) {
        return get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return get(position).getView(mContext);
    }
}
