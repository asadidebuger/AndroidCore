package ir.microsign.settings.object;

import android.content.Context;

import ir.microsign.settings.view.SelectableItemView;

/**
 * Created by Mohammad on 9/16/14.
 */
public class SelectableItem {
    int mId = 0;
    String mTitle = "", mDesc = "", mDate = "",mExtra="";
    boolean mSelected = false, mDeletable = false;
    SelectableItemView mView = null;

    public SelectableItem(int id, String title, String desc, boolean selected) {
        mId = id;
        mTitle = title;
        mDesc = desc;
        mSelected = selected;
        mView = null;
    }

    public SelectableItem setExtra(String mExtra) {
        this.mExtra = mExtra;
        return this;
    }

    public SelectableItem(int id, String title, String date, String desc, boolean selected) {
        mId = id;
        mTitle = title;
        mDesc = desc;
        mSelected = selected;
        mDate = date;
        mView = null;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public boolean getDeletable() {
        return mDeletable;
    }

    public SelectableItem setDeletable(boolean deletable) {
        mDeletable = deletable;
        return this;
    }

    public String getDescription() {
        return mDesc;
    }

    public boolean getSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        if (mView != null) mView.setChecked(selected);
    }

    public int getId() {
        return mId;
    }

    public String getExtra() {
        return mExtra;
    }

    public SelectableItemView getView(Context context) {
        if (mView != null) return mView;
        mView = new SelectableItemView(context, this);
        return mView;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
