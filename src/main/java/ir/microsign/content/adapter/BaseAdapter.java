package ir.microsign.content.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.microsign.R;
import ir.microsign.content.object.Content;
import ir.microsign.content.object.ContentAlert;
import ir.microsign.dbhelper.object.BaseObject;

/**
 * Created by Mohammad on 6/26/14.
 */
public class BaseAdapter extends ir.microsign.dbhelper.adapter.BaseAdapter {
//	List<BaseObject> mItems = null;
//	Context mContext = null;

    Boolean mHaveAnimation = null;
//	View.OnClickListener onClickListener;
//	public void setOnClickListener(View.OnClickListener l){
//		onClickListener=l;
//	}
//	public BaseAdapter(Context activity, List<BaseObject> contents) {
//		mItems = contents;
//		int pos=0;
//		for (int i=0;i<mItems.size();i++)
//		{
//
//			BaseObject object=mItems.get(i);
//			 if (object instanceof Content &&!((Content)object).isFeatured())
//			 {object.setValue("position",pos);pos++;continue;}
//			if (object instanceof Category) {object.setValue("position",pos);pos++;}
//		}
//		mContext = activity;
//	}

//	@Override
//	public int getCount() {
//		return mItems == null ? 0 : mItems.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return mItems == null || mItems.size() < position ? null :
//				mItems.get(position);
//	}
//
//	public List<BaseObject> getItems() {
//		return mItems;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}

    public BaseAdapter(Context context, List<BaseObject> contents) {
        super(context, contents);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseObject baseObject = (BaseObject) getItem(position);
        baseObject.mPosition = position;
        if (baseObject instanceof Content && ((Content) baseObject).isFeatured()) {
            convertView = new View(mContext);
        } else {
            convertView = baseObject.getView(mContext);
        }

//        if (baseObject instanceof Content && haveAnimation())
//            convertView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item));

        if (baseObject instanceof ContentAlert) {
            convertView.setFocusableInTouchMode(false);
            convertView.setFocusable(false);
        }
        return convertView;
    }

    boolean haveAnimation() {
        if (mHaveAnimation != null) return mHaveAnimation;
        mHaveAnimation = mContext.getResources().getBoolean(R.bool.content_list_have_animation);
        return mHaveAnimation;
    }
}
