package ir.microsign.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import ir.microsign.utility.Display;

/**
 * Created by Mohammad on 6/29/14.
 */
public class BaseDialog extends BaseAlterDialog {
//    public Activity mParentActivity = null;
//    Bundle mBundle = null;
//    Boolean mMaximum = true;

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Activity activity, Bundle bundle) {
        super(activity);
        mParentActivity = activity;
        mBundle = bundle;
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

//    public Activity getParentActivity() {
//        return mParentActivity;
//    }

    public void setParentActivity(Activity activity) {
        mParentActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(R.style.dialogStyle);
        super.onCreate(savedInstanceState);

//		setLayout();
//		inIt();
    }

    public void setLayout() {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(Display.getWidth(getContext()), Display.getHeight(getContext()));
    }

    public void inIt() {

    }


    @Override
    public void show() {
        super.show();

        Display.fixScreen(mParentActivity);
        inIt();
        setLayout();
    }

//    @Override
//    public void hide() {
//        dismiss();
//    }

//    @Override
//    public void dismiss() {
//        if (getParentActivity() != null)
//            Display.releaseScreen(getParentActivity());
//        super.dismiss();
//    }

//    public String getString(int id) {
//        return getContext().getString(id);
//    }
//
//    public String getString(int id, Object... args) {
//        return getContext().getString(id, args);
//    }
}
