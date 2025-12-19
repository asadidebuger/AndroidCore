package ir.microsign.content.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import ir.microsign.content.object.Content;
import ir.microsign.content.view.FullContentView;
import ir.microsign.utility.Display;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogFullContent extends AlertDialog {
    Content mContent = null;
    ViewPager mViewPager = null;

    public DialogFullContent(Context context) {
        super(context);
    }

    public DialogFullContent(Context context, int theme) {
        super(context, theme);
    }

    public DialogFullContent(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(R.style.dialogStyle);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = Display.getHeight(getContext());
        layoutParams.width = Display.getWidth(getContext());
        window.setAttributes(layoutParams);
        mViewPager = new ViewPager(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//		mViewPager.setLayoutParams(lp);
        setContentView(mViewPager, lp);
    }

    @Override
    public void show() {
        super.show();
        setContentView(new FullContentView(getContext(), mContent));

    }

    public void show(Content content) {
        mContent = content;
        show();


    }


}
