package ir.microsign.settings.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.settings.dialog.DialogSelectItem;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.settings.object.SettingItem;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 7/23/14.
 */
public class SettingItemEditView extends BaseView implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    //	LinearLayout root = null;
    Font mTitleFont = null, mDescFont = null;
    SettingItem mSettingItem = null;
    boolean lock = false;
    DialogSelectItem mDialogSelectItem = null;

    public SettingItemEditView(Context context) {
        super(context);
    }

    public SettingItemEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingItemEditView(Context context, SettingItem settingItem, int id) {
        super(context);
        mSettingItem = settingItem;
        setTag(id);
        mTitleFont = Font.getFont(context,Font.TextPos.h1);
        mDescFont = Font.getFont(context,Font.TextPos.p);
        mSettingItem.setId(id);
        inIt();
    }

    public Font getFont() {
        return mTitleFont;
    }

    public void setFont(Font font) {
        mDescFont = mTitleFont = font;
        inIt();
    }

//	public Drawable getIcon() {
//		return mSettingItem.get;
//	}

    public Object getValue() {
        return mSettingItem.getValue();
    }

    public void setValue(Object value) {
        mSettingItem.setValue(value);
    }

    public Object getDefaultValue() {
        return mSettingItem.getDefaultValue();
    }

    public void setDefaultValue(Object value) {
        mSettingItem.setDefaultValue(value);
    }

    public String getTitle() {
        return mSettingItem.getTitle();
    }

    public String getDescription() {
        return mSettingItem.getDesc();
    }

    public int getId() {
        return mSettingItem.getId();
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        if (getChildCount() < 1) getLayoutInflater().inflate(R.layout.layout_setting_item_edit_mode, this);
        setLayoutParams(R.id.ll_root);
    }

    public int getMax() {

        return mSettingItem.getMax();
    }

    public void setMax(int max) {
        mSettingItem.setMax(max);
        inIt();
    }

    public int getMin() {

        return mSettingItem.getMin();
    }

    public int getStep() {

        return mSettingItem.getStep();
    }

    public void setmStep(int step) {
        mSettingItem.setStep(step);
        inIt();
    }

    public void inIt() {
        View txtTitle = findViewById(R.id.txt_setting_item_title),
                txtDescription = findViewById(R.id.txt_setting_item_description);
        Text.setText(txtTitle, getTitle(), mTitleFont, true);
        Text.setText(txtDescription, getDescription(), mDescFont, true);
        ImageButton btnDefault = (ImageButton) findViewById(R.id.btn_setting_item_default);
        btnDefault.setImageResource(R.drawable.icon_refresh);
        btnDefault.setOnClickListener(this);setOnClickListener(this);
        setVisibility();
        if (getType() == 0) {
            inItType0();
        } else if (getType() == 1) {
            inItType1();
        } else if (getType() == 2) {
            inItType2();
        }

    }

    void inItType0() {
        CheckBox chbxValue = (CheckBox) findViewById(R.id.chbx_setting_item_value);
        chbxValue.setOnCheckedChangeListener(this);
        Text.setText(findViewById(R.id.txt_setting_item_value), "", mTitleFont, false);
        chbxValue.setChecked((Boolean) getValue());
    }

    void inItType1() {
        SeekBar seekValue = ((SeekBar) findViewById(R.id.seek_setting_item_value));
        Text.setText(findViewById(R.id.txt_setting_item_value), getValue().toString(), mTitleFont, false);
        if (lock) return;
        seekValue.setMax((getMax() - getMin()) / getStep());
        seekValue.setProgress((((Integer) getValue() - getMin()) / getStep()));
        seekValue.setOnSeekBarChangeListener(this);
    }

    void inItType2() {
        ImageButton btnChange = (ImageButton) findViewById(R.id.btn_setting_item_change);
        btnChange.setImageResource(R.drawable.icon_edit);
        btnChange.setOnClickListener(this);
        Text.setText(findViewById(R.id.txt_setting_item_value), mSettingItem.getSelectableItems().get((Integer) getValue()).toString(), mTitleFont, false);
    }

    void setVisibility() {
        if (getType() == 0) {
            view.setVisibility(findViewById(R.id.seek_setting_item_value), false);
            view.setVisibility(findViewById(R.id.chbx_setting_item_value), true);
//            view.setVisibility(findViewById(R.id.btn_setting_item_change), false);
        } else if (getType() == 1) {
            view.setVisibility(findViewById(R.id.seek_setting_item_value), true);
            view.setVisibility(findViewById(R.id.chbx_setting_item_value), false);
//            view.setVisibility(findViewById(R.id.btn_setting_item_change), false);
        } else if (getType() == 2) {
            view.setVisibility(findViewById(R.id.seek_setting_item_value), false);
            view.setVisibility(findViewById(R.id.chbx_setting_item_value), false);
//            view.setVisibility(findViewById(R.id.btn_setting_item_change), true);
        }

    }

    public int getType() {
        return mSettingItem.getType();
    }

    public void ok() {
        mSettingItem.registValue();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;
        lock = true;
        setValue(progress * getStep() + getMin());
        onItemChange();
        inIt();
        lock = false;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setValue(isChecked);
        onItemChange();
    }

    @Override
    public void onClick(View v) {
        if (getType()==2&&v instanceof SettingItemEditView/*v.getId() == R.id.btn_setting_item_change*/)
            showSelector();
        else if (v.getId() == R.id.btn_setting_item_default)
            onItemDefault();
    }

    public void onItemChange() {

    }

    void showSelector() {
        mDialogSelectItem = new DialogSelectItem(getContext(), mSettingItem.getTitle(), mSettingItem.getSelectableItems(), (Integer) getValue());
        mDialogSelectItem.show();
        mDialogSelectItem.setOnResultListener(new Listener.ResultListener() {
            @Override
            public void onResult(Object o, int i) {
                mDialogSelectItem.hide();
                if (i < 0) {
                    return;
                }

                setValue(i);
                inItType2();
            }
        });
    }

    public void onItemDefault() {
        mSettingItem.resetDefault();
        inIt();
    }
}
