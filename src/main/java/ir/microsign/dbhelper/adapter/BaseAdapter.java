package ir.microsign.dbhelper.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;

/**
 * Created by Mohammad on 6/26/14.
 */
public class BaseAdapter extends android.widget.BaseAdapter {
    public Context mContext = null;List<BaseObject> mItemsOrig = null;
    List<BaseObject> mItems = null;
    View.OnClickListener mOnClickListener;
    BaseObject mLastSelected = null;
    String mFilter=null;
    boolean multiSelect=false;
    public void setMultiSelect(boolean multiSelect){
        this.multiSelect=multiSelect;
    }
    public BaseAdapter(Context context, List<BaseObject> contents) {
       setItems(contents);

        mContext = context;
    }
    boolean mStartWith,mEndWith, mNoCase;
public void filter(String text,boolean startWith,boolean endWith,boolean noCase){
    mFilter=text;
    mStartWith=startWith;mEndWith=endWith;mNoCase=noCase;
   filter();
}
    public void filter(){
        mItems=new ArrayList<>();
    if (mFilter==null||mFilter.isEmpty())
        mItems.addAll(mItemsOrig);
   else {
        for (BaseObject baseObject : mItemsOrig) {
            if (baseObject.filter(mFilter, mStartWith, mEndWith, mNoCase)) mItems.add(baseObject);
        }
    }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

}

    public Context getContext() {
        return mContext;
    }

    public void setOnClickListener(View.OnClickListener l) {
        mOnClickListener = l;
    }

    public void clean() {
        mItems = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public BaseObject setItemSelected(String field, Object value) {

        if (mItems == null) return null;
        for (BaseObject baseObject : mItems)
            if (baseObject.getValue(field).equals(value)) {
                setItemSelected(baseObject);
                return mLastSelected;
            }
        return mLastSelected;
    }

    public BaseObject selectNext(boolean right) {
        if (getCount() < 1) return null;
        int selected = (mLastSelected == null ? -1 : mLastSelected.mPosition);
        int index = (selected + (right ? 1 : -1)) % getCount();
        if (index < 0) index = getCount() - 1;
        setItemSelected(mItems.get(index));
        return mLastSelected;
    }
    public void setItemSelected(BaseObject object){
        setItemSelected(object,true);
    }
public  void setAllSelected(boolean select){
    for (BaseObject object:mItems){
        setSelection(object,select);
    }
}
    public void removeItem(BaseObject object){
        mItemsOrig.remove(object);
        filter();
    }
    public  void setCollectionSelected(String fieldName,List<Object> values,boolean select){
        for (BaseObject object:mItems){
            for (Object val:values) {
                Object v=object.getValue(fieldName);
                if (v==val||(v!=null&&v.equals(val)))
                    setSelection(object, select);
            }
        }
    }
    public List<BaseObject> getSelected(boolean original){
        List<BaseObject> l=new ArrayList<>();
        List<BaseObject> items=original?mItemsOrig:mItems;
        for (BaseObject object:items){
            if (object.mSelected)l.add(object);
        }
        return l;
    }
    public void setItemSelected(BaseObject object,boolean select) {
        if (mItems == null) return;


        if (!multiSelect&&(mLastSelected == null || !mLastSelected.equals(object))) {
            setSelection(mLastSelected, false);
        }
        if (object == null) return;
        int index = object.mPosition == null ? mItems.indexOf(object) : object.mPosition;
        if (index < 0) return;
        mLastSelected = mItems.get(index);
        setSelection(mLastSelected, select);
    }

    public void updateItemsView() {
        for (BaseObject object : mItems) object.UpdateView();
    }

    public BaseObject getSelectedItem() {
        return mLastSelected;
    }

    void setSelection(BaseObject object, boolean selected) {
        if (object == null) return;
        object.mSelected = selected;
        if (object.mView != null) onItemViewSelect(object.mView, selected);

    }

    public void onItemViewSelect(View view, boolean selected) {
        if (view instanceof BaseView)((BaseView)view).setSelected(selected, true);
        else view.setSelected(selected);
    }

    public int getItemPosition(BaseObject item) {
        if (mItems == null) return -1;
        return mItems.indexOf(item);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public int getCount(boolean original) {
        if (original)
        return mItemsOrig == null ? 0 : mItemsOrig.size();
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position<0|| mItems == null || mItems.size() <= position ? null :
                mItems.get(position);
    }

    public List<BaseObject> getItems() {
        return mItems;
    }

    public void setItems(List<BaseObject> items) {
        mItemsOrig= items;
        filter();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseObject baseObject = (BaseObject) getItem(position);
        baseObject.mPosition = position;
        convertView = baseObject.getView(mContext);
        if (mOnClickListener != null) convertView.setOnClickListener(mOnClickListener);
        return convertView;
    }
}
