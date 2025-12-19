package ir.microsign.settings.view;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ir.microsign.settings.interfaces.Listener;

/**
 * Created by Mohammad on 7/23/14.
 */
public class SettingView extends LinearLayout {
    ArrayList<SettingItemView> mItems = new ArrayList<SettingItemView>();
    Listener.SettingItemClickListener mSettingItemClickListener = null;

    public SettingView(Context context) {
        super(context);
        inIt();
    }

    public void SettingView(Listener.SettingItemClickListener l) {
        mSettingItemClickListener = l;
        for (SettingItemView item : mItems)
            item.setSettingItemSelectedListener(mSettingItemClickListener);
    }

    void inIt() {
//		this.setVerticalScrollBarEnabled(true);
        setOrientation(VERTICAL);
        removeAllViews();
        for (SettingItemView Item : mItems) {
            addView(Item);
            Item.setSettingItemSelectedListener(mSettingItemClickListener);
        }

    }

    public void clearItems() {
        mItems.clear();
        inIt();
    }

    public void addItem(SettingItemView item) {
        mItems.add(item);
        addView(item);
        item.setSettingItemSelectedListener(mSettingItemClickListener);
    }

    public void addItems(SettingItemView[] items) {
        for (SettingItemView item : items) addItem(item);
    }


}
