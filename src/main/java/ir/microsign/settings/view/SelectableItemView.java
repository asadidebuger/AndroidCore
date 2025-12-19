package ir.microsign.settings.view;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.TimeZone;

import ir.microsign.calendar.Calendar;
import ir.microsign.calendar.Date;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.R;
import ir.microsign.settings.Setting;
import ir.microsign.settings.object.SelectableItem;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;


/**
 * Created by Mohammad on 7/23/14.
 */
public class SelectableItemView extends SettingItemBaseView implements View.OnTouchListener, View.OnClickListener {
    //	 String mTitle=null,mDesc=null;
    Boolean mChecked = false, mDeletable = false;
    int mId = 0;
    String mDate;
    OnClickListener mOnClickListener = null;

    public SelectableItemView(Context context) {
        super(context);
    }

    public SelectableItemView(Context context, int id, String title, String desc, boolean checked) {
        super(context);
        mTitle = title;
        mDescription = desc;
        mChecked = checked;
        mId = id;

        inIt();
    }

    public SelectableItemView(Context context, SelectableItem selectableItem) {
        super(context);
        mId = selectableItem.getId();
        mTitle = selectableItem.getTitle();
        mDescription = selectableItem.getDescription();
        mChecked = selectableItem.getSelected();
        mDate = selectableItem.getDate();
        mDeletable = selectableItem.getDeletable();
        inIt();
    }

    public int getID() {
        return mId;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        findViewById(R.id.ll_intent).setBackgroundColor(Color.parseColor(mChecked ? "#d41c2b" : "#c1c1c1"));
        ((ImageView) findViewById(R.id.img_intent)).setImageResource(mChecked ? R.drawable.icon_intent : R.drawable.icon_intent_uncheck);

    }

    public void setTitle(String title) {
        mTitle = title;
        Text.setText(findViewById(R.id.txt_value_title), mTitle, Font.TextPos.h1, true);
    }

    public void setDescription(String desc) {
        mDescription = desc;
        Text.setText(findViewById(R.id.txt_value_description), mDescription, Font.TextPos.p, true);
    }

    public void setDate(String date) {
        mDate = date;
        if (Text.isEmpty(date))return ;
        String p="yyyy/MM/dd HH:mm:ss";//"2018/08/19 08:57:36"
        String d =Text.isEmpty(date)?null: Calendar.fromString(date,p, Date.Type.valueOf(Setting.Advance.getCalendarType(getContext())), TimeZone.getDefault()).toString(true, false);
        Text.setText(findViewById(R.id.txt_date), d == null ? "" : d, Font.TextPos.small, true);

    }

    @Override
    public void inIt() {
//		super.inIt();
//		View imgDelete=;
//		imgDelete.setOnClickListener(this);
        view.setVisibility(findViewById(R.id.img_delete), mDeletable);

        setTitle(mTitle);
        setDescription(mDescription);
        setChecked(mChecked);
        setDate(mDate);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
        findViewById(R.id.ll_root1).setOnClickListener(this);
        if (mDeletable) findViewById(R.id.img_delete).setOnClickListener(this);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        findViewById(R.id.ll_root1).setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
//
        boolean onDelete = false;
        if (v.getId() == R.id.img_delete) {
            v.setTag(getID());
            onDelete = true;
        }
        if (mOnClickListener == null) return;
        mOnClickListener.onClick(onDelete ? v : this);


    }

    @Override
    public int getViewResource() {
        return R.layout.layout_selectable_item;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setMotion(event);
        return false;
    }

    void setMotion(MotionEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_DOWN) || (event.getAction() == MotionEvent.ACTION_POINTER_DOWN)) {
            findViewById(R.id.ll_intent).setBackgroundColor(Color.parseColor("#d41c2b"));
            ((ImageView) findViewById(R.id.img_intent)).setImageResource(R.drawable.icon_intent);
        } else if ((event.getAction() == MotionEvent.ACTION_POINTER_UP) || (event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_OUTSIDE) || (event.getAction() == MotionEvent.ACTION_SCROLL) || (event.getAction() == MotionEvent.ACTION_CANCEL)) {
            findViewById(R.id.ll_intent).setBackgroundColor(Color.parseColor(mChecked ? "#d41c2b" : "#c1c1c1"));
            ((ImageView) findViewById(R.id.img_intent)).setImageResource(mChecked ? R.drawable.icon_intent : R.drawable.icon_intent_uncheck);
        }
    }
}
