package ir.microsign.settings.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import ir.microsign.colorpicker.dialog.DialogColorPicker;
import ir.microsign.colorpicker.view.ColorPickerView;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.settings.dialog.DialogSelectFont;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.view.ViewHtml;

/**
 * Created by Mohammad on 7/23/14.
 */
public class EditFontView extends BaseView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    Font mFontCurrent = null, mFontDefault = null, mFontCurrentTemp = null;
    String mTitle = null, mDesc = null;
    Listener.SettingItemClickListener mSettingItemClickListener = null;
    Listener.ResultListener mResultListener = null;
    Activity mParentActivity = null;
    String mFontKey = "";
    View mTxtName, mTxtSize, mLlColor, mLlBackColor, mTxtLineHeight;
    ViewHtml mWebDesc;
    SeekBar mSeekSize, mSeekLineHeight;
    DialogSelectFont mDialogSelectFont = null;
    DialogColorPicker mDialogColorPicker = null;
    int mCurrentColorDialog = 0;
    boolean lock = false;
    int lineHeightMin = 80;

    public EditFontView(Activity context, String fontKey) {
        super(context);
        mParentActivity = context;
        mFontKey = fontKey;

        int titleId = getResources().getIdentifier("setting_font_title_" + mFontKey, "string", getContext().getPackageName());
        int descId = getResources().getIdentifier("setting_font_desc_" + mFontKey, "string", getContext().getPackageName());
        String title = getContext().getString(titleId), desc = getContext().getString(descId);
        mTitle = title;
        mDesc = desc;
        inIt();
    }

    public void setOnResultListener(Listener.ResultListener l) {
        mResultListener = l;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof EditFontView)) return false;
        return ((EditFontView) o).getId() == getId();
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        if (getChildCount() < 1) getLayoutInflater().inflate(R.layout.layout_edit_font, this);
        setLayoutParams(R.id.ll_root, -1, -1);
    }

    void inIt() {

        mFontCurrent = Font.getFont(getContext(),mFontKey);
        mFontCurrentTemp = mFontCurrent.clone();
        mFontDefault = Font.getDefaultFont(getContext(),mFontKey);
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setImageResource(R.drawable.icon_close_white);
//        ImageView imgOk = (ImageView) findViewById(R.id.img_ok);
//        imgOk.setImageResource(R.drawable.icon_ok_white);
        ImageView imgDefault = (ImageView) findViewById(R.id.img_default);
        imgDefault.setImageResource(ir.microsign.R.drawable.icon_refresh_white);


        imgClose.setOnClickListener(this);
        Text.setText(findViewById(R.id.txt_title), getTitle(), Font.TextPos.h1, false);
        Text.setText(findViewById(R.id.txt_font_line_height), R.string.setting_font_title_font_line_height, Font.TextPos.h1, false);
        Text.setText(findViewById(R.id.txt_font_color), R.string.setting_font_title_font_color, Font.TextPos.h1, false);
        Text.setText(findViewById(R.id.txt_font_back_color), R.string.setting_font_title_font_back_color, Font.TextPos.h1, false);
        Text.setText(findViewById(R.id.txt_font_size), R.string.setting_font_title_font_size, Font.TextPos.h1, false);
        Text.setText(findViewById(R.id.txt_font_sample), R.string.setting_font_title_font_sample, Font.TextPos.h1, false);
        Text.setText(findViewById(R.id.txt_font_name), R.string.setting_font_title_font_name, Font.TextPos.h1, false);

        mTxtName = findViewById(R.id.txt_font_name_value);
        mTxtSize = findViewById(R.id.txt_font_size_value);
        mLlColor = findViewById(R.id.ll_font_color_value);
        mLlBackColor = findViewById(R.id.ll_font_back_color_value);
        mTxtLineHeight = findViewById(R.id.txt_font_line_height_value);
        mWebDesc = (ViewHtml) findViewById(R.id.web_sample_text);


        findViewById(R.id.img_default).setOnClickListener(this);
//        findViewById(R.id.img_ok).setOnClickListener(this);
        findViewById(R.id.btn_font_def_color).setOnClickListener(this);
        findViewById(R.id.btn_font_def_font).setOnClickListener(this);
        findViewById(R.id.btn_font_def_line_height).setOnClickListener(this);
        findViewById(R.id.btn_font_def_size).setOnClickListener(this);

        findViewById(R.id.btn_font_change_font).setOnClickListener(this);
        findViewById(R.id.btn_font_change_color).setOnClickListener(this);
        findViewById(R.id.btn_font_change_back_color).setOnClickListener(this);

findViewById(R.id.ll_font_name).setOnClickListener(this);
        findViewById(R.id.ll_font_color).setOnClickListener(this);
        findViewById(R.id.ll_font_back_color).setOnClickListener(this);


        mSeekSize = ((SeekBar) findViewById(R.id.seek_font_size));
        mSeekSize.setOnSeekBarChangeListener(this);
        mSeekLineHeight = ((SeekBar) findViewById(R.id.seek_font_line_height));
        mSeekLineHeight.setOnSeekBarChangeListener(this);
//		tarnian.view.setVisibility(findViewById(R.id.ll_font_color),mFontDefault.getColor()<1);
        view.setVisibility(findViewById(R.id.ll_font_back_color), mFontDefault.getBackColor() < 1);
        view.setVisibility(findViewById(R.id.ll_font_line_height), mFontDefault.getLineHeight() > 0);
        view.setVisibility(findViewById(R.id.ll_font_color), mFontDefault.getColor() < 1);
        setValues(mFontCurrent);
    }

    void setValues(Font font) {
        Text.setText(mTxtName, font.getFontName(true), Font.TextPos.h1);
        Text.setText(mTxtSize, String.format("%.1fsp", font.getSize()), Font.TextPos.h1);
        Text.setText(mTxtLineHeight, String.format("%.0f%%", font.getLineHeight()), Font.TextPos.h1);
        mLlColor.setBackgroundColor(font.getColor());
        mLlBackColor.setBackgroundColor(font.getBackColor());
        StringBuilder sb=new StringBuilder(mDesc.length()*3);
        boolean strong=font.isBold();
        if (strong)sb.append("<strong>");
        sb.append(mDesc).append("<br/>").append(mDesc);
        if(strong)sb.append("</strong>");
        mWebDesc.setContent(sb.toString(), mFontCurrent, true, true);

        if (lock) return;
        mSeekLineHeight.setProgress((int) ((font.getLineHeight() - lineHeightMin) / 5f));
        mSeekSize.setProgress((int) (font.getSize() * 2));
    }

    public Font getFontCurrent() {
        return mFontCurrent;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_default) {
            mFontCurrent = mFontDefault;
            setValues(mFontCurrent);
        } else if (v.getId() == R.id.img_ok) {
            onResult(this, 1);
        } else if (v.getId() == R.id.img_close) onResult(this, -1);
        else if (v.getId() == R.id.btn_font_change_font||v.getId() == R.id.ll_font_name) showSelectFont();
        else if (v.getId() == R.id.btn_font_change_color||v.getId() == R.id.ll_font_color) showSelectColor(0);
        else if (v.getId() == R.id.btn_font_change_back_color||v.getId() == R.id.ll_font_back_color) showSelectColor(1);
        else if (v.getId() == R.id.btn_font_def_font) {
            mFontCurrent.setIndex(Font.getDefaultFont(getContext(),mFontKey).getIndex());
            setValues(mFontCurrent);
        } else if (v.getId() == R.id.btn_font_def_color) {
            mFontCurrent.setColor(Font.getDefaultFont(getContext(),mFontKey).getColor());
            setValues(mFontCurrent);
        } else if (v.getId() == R.id.btn_font_def_back_color) {
            mFontCurrent.setBackColor(Font.getDefaultFont(getContext(),mFontKey).getBackColor());
            setValues(mFontCurrent);
        } else if (v.getId() == R.id.btn_font_def_line_height) {
            mFontCurrent.setLineHeight(Font.getDefaultFont(getContext(),mFontKey).getLineHeight());
            setValues(mFontCurrent);
        } else if (v.getId() == R.id.btn_font_def_size) {
            mFontCurrent.setSize(Font.getDefaultFont(getContext(),mFontKey).getSize());
            setValues(mFontCurrent);
        }
    }

    void showSelectFont() {
        if (mDialogSelectFont == null) mDialogSelectFont = new DialogSelectFont(getContext(), mFontCurrent.getIndex());
        mDialogSelectFont.show(mFontCurrent.getIndex());
        mDialogSelectFont.setOnResultListener(new Listener.ResultListener() {
            @Override
            public void onResult(Object o, int i) {
                mFontCurrent.setIndex(i);
                mDialogSelectFont.hide();
                setValues(mFontCurrent);
            }
        });


    }

    void showSelectColor(int select) {
        mCurrentColorDialog = select;
        if (mDialogColorPicker == null)
            mDialogColorPicker = new DialogColorPicker(getContext(), select == 0 ? mFontCurrent.getColor() : mFontCurrent.getBackColor(), false, new ColorPickerView.OnColorPickerListener() {
                @Override
                public void onCancel(ColorPickerView dialog) {
                    mDialogColorPicker.hide();
                }

                @Override
                public void onOk(ColorPickerView dialog, int color) {
                    mDialogColorPicker.hide();
                    if (mCurrentColorDialog == 0) mFontCurrent.setColor(color);
                    else mFontCurrent.setBackColor(color);
                    mDialogColorPicker.hide();
                    setValues(mFontCurrent);
                }
            });
        mDialogColorPicker.show(mFontCurrent.getColor());
    }

    public String getKey() {
        return mFontKey;
    }

    public void onResult(Object o, int i) {

        if (i < 0) mFontCurrent = mFontCurrentTemp;
        if (mResultListener != null) mResultListener.onResult(o, i);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;

        lock = true;
        int val = (int) (lineHeightMin + (float) mSeekLineHeight.getProgress() * 5f);
        mFontCurrent.setSize((float) mSeekSize.getProgress() / 2f);
        mFontCurrent.setLineHeight(val == lineHeightMin ? 0 : val);
        setValues(mFontCurrent);
        lock = false;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
