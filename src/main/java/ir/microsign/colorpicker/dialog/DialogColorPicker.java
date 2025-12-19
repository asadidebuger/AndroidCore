package ir.microsign.colorpicker.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import ir.microsign.colorpicker.view.ColorPickerView;
import ir.microsign.dialog.BaseAlterDialog;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogColorPicker extends BaseAlterDialog {
    ColorPickerView.OnColorPickerListener mListener = null;
    int mColor = -1;
    ColorPickerView mColorPickerView = null;
    boolean mSupportAlpha = false;

    public DialogColorPicker(Activity activity, int color, boolean supportAlpha, ColorPickerView.OnColorPickerListener l) {
        super(activity, null);
        mColor = color;
        mSupportAlpha = supportAlpha;
        setOnColorPickerListener(l);


    }

    public DialogColorPicker(Context context, int color, boolean supportAlpha, ColorPickerView.OnColorPickerListener l) {
        super(context);
        mColor = color;
        mSupportAlpha = supportAlpha;
        setOnColorPickerListener(l);


    }

    public void setOnColorPickerListener(ColorPickerView.OnColorPickerListener l) {
        mListener = l;
        if (mColorPickerView != null)
            mColorPickerView.setOnColorPickerListener(l);
    }

    public void setColor(int color) {
        mColor = color;
        if (mColorPickerView != null)
            mColorPickerView.setColor(color);
    }

    public void show(int color) {
        mColor = color;
        super.show();
    }

    @Override
    public void inIt() {
        if (mColorPickerView == null)
            mColorPickerView = new ColorPickerView(getContext(), mColor, mSupportAlpha, mListener);
        else mColorPickerView.setColor(mColor);
        ViewGroup.LayoutParams layoutParams = mColorPickerView.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.height = ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
        mColorPickerView.setLayoutParams(layoutParams);
        setContentView(mColorPickerView);
    }

}
