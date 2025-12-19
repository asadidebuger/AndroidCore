package ir.microsign.content.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.R;
import ir.microsign.content.object.ContentAlert;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ContentAlertView extends BaseView {
    Listener.EventRequestListener mEventRequestListener = null;

    public ContentAlertView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    public void setEventRequestListener(Listener.EventRequestListener l) {
        mEventRequestListener = l;
    }

    public void OnEventRequestListener(int code, Object... objects) {
        if (mEventRequestListener == null) return;
        mEventRequestListener.onEventRequest(code, objects);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        init();
    }

    ContentAlert getContentAlert() {
        return (ContentAlert) getDbObject();
    }

    void init() {
//		removeAllViews();
        getLayoutInflater().inflate(R.layout.layout_content_alert, this);
        Text.setText(findViewById(R.id.txt_title), getContentAlert().mTitle, Font.TextPos.h1);
        Text.setText(findViewById(R.id.btn1), getContentAlert().mBtnTitle1, Font.TextPos.h1);
        Text.setText(findViewById(R.id.btn2), getContentAlert().mBtnTitle2, Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_description1), getContentAlert().mDesc1, getContentAlert().mFont1, true);
        Text.setText(findViewById(R.id.txt_description2), getContentAlert().mDesc2, getContentAlert().mFont2, true);
        ((ImageView) findViewById(R.id.img_icon)).setImageDrawable(getContentAlert().mIcon);
        if (mEventRequestListener == null) mEventRequestListener = getContentAlert().mEventRequestListener;
        setClickListeners();


    }

    void setClickListeners() {
        setOnClickListener(null);
        setFocusable(false);
        setFocusableInTouchMode(false);
        findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnEventRequestListener(1, v);
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnEventRequestListener(2, v);
            }
        });
        findViewById(R.id.img_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnEventRequestListener(2, v);
            }
        });
    }
}
