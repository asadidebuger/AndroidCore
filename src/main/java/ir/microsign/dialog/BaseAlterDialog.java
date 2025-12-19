package ir.microsign.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ir.microsign.utility.Display;


/**
 * Created by Mohammad on 6/29/14.
 */
public class BaseAlterDialog extends AlertDialog {
    Bundle mBundle = null;
   public Activity mParentActivity = null;

    public BaseAlterDialog(Context context) {
        super(context);
    }

    public BaseAlterDialog(Activity activity, Bundle bundle) {
        super(activity);
        mParentActivity = activity;
        mBundle = bundle;
    }

    public BaseAlterDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseAlterDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public Activity getParentActivity() {
        return mParentActivity;
    }

    public void setParentActivity(Activity activity) {
        mParentActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


//		requestWindowFeature(R.style.dialogStyle);
//		Window window = getWindow();
//		WindowManager.LayoutParams layoutParams = window.getAttributes();
//		layoutParams.height = tarnian.Display.getHeightWithoutBar();
//		layoutParams.width = tarnian.Display.getWidth();
//		window.setAttributes(layoutParams);
        setLayout();
//		inIt();
    }

    public String getString(int id) {
        return getContext().getString(id);
    }

    public String getString(int id, Object... args) {
        return getContext().getString(id, args);
    }

    public void setLayout() {

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(Display.getWidth(getContext()), Display.getHeightWithoutBar(getContext()));
    }

    public void inIt() {

    }

    @Override
    public void show() {

        super.show();
        Display.fixScreen(mParentActivity);
        inIt();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//		List<View> views=view.getAllChilds(findViewById(android.R.id.content), EditText.class);
//		for (View editText:views)
//		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//				}
//			}
//		});

//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void hide() {
        dismiss();
//		tarnian.activity.releaseScreen();
//		super.hide();
    }

    @Override
    public void dismiss() {
        if (getParentActivity() != null)
            Display.releaseScreen(getParentActivity());
        super.dismiss();
    }
    onActionListener onActionListener;

    public void setOnActionListener(onActionListener l) {
        this.onActionListener = l;
    }
    public void emitAction(String action,Object data){
        if (onActionListener==null)return;
        onActionListener.onAction(action,data);
    }

    public interface onActionListener{
        void onAction(String action,Object data);
    }
}
