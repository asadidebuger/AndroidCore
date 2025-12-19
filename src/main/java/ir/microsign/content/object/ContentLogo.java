package ir.microsign.content.object;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import ir.microsign.R;
import ir.microsign.content.view.ContentLogoView;
import ir.microsign.context.Application;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Font;

/**
 * Created by Mohammad on 9/30/14.
 */
public class ContentLogo extends BaseObject {
    public String mDesc1;
    public Font mFont1, mFont2;
    public Drawable mIcon;
    ContentLogoView mView = null;

    public ContentLogo(String desc1, Font font1, Font font2, Drawable icon) {
//		title = title;
        mDesc1 = desc1;
        mFont1 = font1;
        mFont2 = font2;
        mIcon = icon;

    }

    public ContentLogo(String desc1, Drawable icon) {

        mDesc1 = desc1;
        mFont1 = Font.getFont(Application.getContext(),Font.TextPos.h1).clone();
        mFont2 = Font.getFont(Application.getContext(),Font.TextPos.h1).clone();
        mFont2.setColor(Application.getContext().getResources().getColor(R.color.tomato));
        mIcon = icon;
    }

    @Override
    public View getView(Context context) {
        if (mView != null) return mView;
        mView = new ContentLogoView(context, this);
        return mView;
    }
}
