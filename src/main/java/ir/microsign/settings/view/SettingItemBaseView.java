package ir.microsign.settings.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 7/23/14.
 */
public class SettingItemBaseView extends BaseView implements View.OnClickListener {
    int mId;
    String mTitle, mDescription;
    Drawable mIcon = null;
    Listener.SettingItemClickListener mSettingItemClickListener = null;
    Object mValue = null;
    Object mValueDefault = null;
    LinearLayout root = null;

    Font mTitleFont = null, mDescFont = null;

    public SettingItemBaseView(Context context) {
        super(context);
    }

    public SettingItemBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingItemBaseView(Context context, int id, String title, String description, Drawable icon) {
        super(context);
        mTitleFont = Font.getFont(getContext(),Font.TextPos.h1);
        mDescFont = Font.getFont(getContext(),Font.TextPos.p);
        mIcon = icon;
        mId = id;
        mTitle = title;
        mDescription = description;
        inIt();
    }

    public SettingItemBaseView(Context context, int id, String title, String description, Drawable icon, Font font) {

        super(context);
        mIcon = icon;
        mId = id;
        mTitle = title;
        mDescription = description;
        mDescFont = mTitleFont = font;
        inIt();
    }

    public void setSettingItemSelectedListener(Listener.SettingItemClickListener l) {
        mSettingItemClickListener = l;
        setOnClickListener(this);
        if (findViewById(R.id.btn_setting_item_change) != null)
            findViewById(R.id.btn_setting_item_change).setOnClickListener(this);
        if (findViewById(R.id.btn_setting_item_default) != null)
            findViewById(R.id.btn_setting_item_default).setOnClickListener(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof SettingItemBaseView)) return false;
        return ((SettingItemBaseView) o).getId() == getId();
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        if (getChildCount() < 1) getLayoutInflater().inflate(getViewResource(), this);
        setLayoutParams(R.id.ll_root);
    }

    //public void setDefault(){
//
//}
    public int getViewResource() {
        return R.layout.layout_setting_item_compact;
    }

    public Font getFont() {
        return mTitleFont;
    }

    public void setFont(Font font) {
        mDescFont = mTitleFont = font;
        inIt();
    }

    public void inIt() {
        View txtTitle = findViewById(R.id.txt_setting_item_title), txtDescription = findViewById(R.id.txt_setting_item_description);
        Text.setText(txtTitle, getTitle(), mTitleFont, true);
        Text.setText(txtDescription, getDescription(), mDescFont, true);
        ImageView imgIcon = (ImageView) findViewById(R.id.img_icon);
        view.setVisibility(imgIcon, getIcon() != null);
        imgIcon.setImageDrawable(getIcon());
        ImageButton btnChange = (ImageButton) findViewById(R.id.btn_setting_item_change);
        ImageButton btnDefault = (ImageButton) findViewById(R.id.btn_setting_item_default);
        btnChange.setImageResource(R.drawable.icon_edit);
        btnDefault.setImageResource(R.drawable.icon_refresh);
//		int resBkgId=getResources().getIdentifier("color_t_panel" + ((getId() % 4) + 2), "color", getContext().getPackageName()) ;
//		root = (LinearLayout) findViewById(R.id.ll_root);
//		root.setBackgroundColor(getContext().getResources().getColor(resBkgId));
    }

    public Object getValue() {
        return mValue;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public int getId() {
        return mId;
    }

    @Override
    public void onClick(View v) {
//		if (mSettingItemClickListener == null) return;
        if (v instanceof SettingItemBaseView||v.getId() == R.id.btn_setting_item_change)
            onItemChange();
        else if (v.getId() == R.id.btn_setting_item_default)
            onItemDefault();
    }

    public void onItemChange() {
        if (mSettingItemClickListener != null)
            mSettingItemClickListener.onItemChangeClick(this);
    }

    public void onItemDefault() {
        if (mSettingItemClickListener != null)
            mSettingItemClickListener.onItemDefaultClick(this);
    }
}
