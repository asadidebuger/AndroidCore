package ir.microsign.content.object;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.R;
import ir.microsign.content.view.ContentAlertView;
import ir.microsign.context.Application;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Font;

/**
 * Created by Mohammad on 9/30/14.
 */
public class ContentAlert extends BaseObject {
    public String mDesc1, mDesc2, mTitle, mBtnTitle1, mBtnTitle2;
    public Font mFont1, mFont2;
    public Drawable mIcon;
    public Listener.EventRequestListener mEventRequestListener = null;
    ContentAlertView mContentAlertView = null;

    public ContentAlert(String title, String desc1, String desc2, String btnTitle1, String btnTitle2, Font font1, Font font2, Drawable icon) {
        mTitle = title;
        mDesc1 = desc1;
        mDesc2 = desc2;
        mFont1 = font1;
        mFont2 = font2;
        mBtnTitle1 = btnTitle1;
        mBtnTitle2 = btnTitle2;
        mIcon = icon;

    }

    public ContentAlert(String title, String desc1, String desc2, String btnTitle1, String btnTitle2, Drawable icon) {
        mTitle = title;
        mDesc1 = desc1;
        mDesc2 = desc2;
        mBtnTitle1 = btnTitle1;
        mBtnTitle2 = btnTitle2;
        mFont1 = Font.getFont(Application.getContext(),Font.TextPos.h1).clone();
        mFont2 = Font.getFont(Application.getContext(),Font.TextPos.h1).clone();
        mFont2.setColor(Application.getContext().getResources().getColor(R.color.tomato));
        mIcon = icon;
    }

    public void setEventRequestListener(Listener.EventRequestListener l) {
        mEventRequestListener = l;
    }

    @Override
    public View getView(Context context) {
        if (mContentAlertView != null) return mContentAlertView;
        mContentAlertView = new ContentAlertView(context, this);
        mContentAlertView.setEventRequestListener(mEventRequestListener);
        return mContentAlertView;
    }
}
