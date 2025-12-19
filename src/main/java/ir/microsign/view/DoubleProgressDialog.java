package ir.microsign.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.R;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;


/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 10/15/13
 * Time: 9:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleProgressDialog extends AlertDialog implements View.OnClickListener {
    static DoubleProgressDialog _DoubleProgressDialog = null;
    //	public boolean Result = false;
    public String mTitle, mTitleP1, mTitleP2;
    //	public Font mFont = tarnian.getPublicFont();
    public boolean mShowCancel = false;

    public ProgressBar mPb1 = null, mPb2 = null;
    public TextView mTxtTitle = null, mTxtTitleP1 = null, mTxtTitleP2 = null, mTxtDescP1 = null, mTxtDescP2 = null;
    public String mDesc1Format = null, mDesc2Format = null;
    public View mBtnCancel = null;
    public boolean canceled = false;
    public int mP1 = 0, mP2 = 0, mMax1 = 100, mMax2 = 100;
    OnCancelListener _OnCancelListener = null;
    boolean _canceled = false;

    public DoubleProgressDialog(Context context) {
        super(context);
//		Tarnian._context = context;
    }


    public DoubleProgressDialog(Context context, int theme) {
        super(context, theme);
//		Tarnian._context = context;
    }

    public DoubleProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
//		Tarnian._context = context;
    }

    public static void Cancel() {
        if (_DoubleProgressDialog != null && _DoubleProgressDialog.isShowing())
            _DoubleProgressDialog.cancel();
    }

    public static boolean isCanceled() {
        if (_DoubleProgressDialog != null) return _DoubleProgressDialog._canceled;
        return false;
    }

    public static void Hide() {
        if (_DoubleProgressDialog != null && _DoubleProgressDialog.isShowing())
            _DoubleProgressDialog.dismiss();
    }

    public static void show(Context context, String title, String titleP1, String titleP2, String desc1Format, String desc2Format, int p1, int max1, int p2, int max2, boolean showCancel) {
        if (_DoubleProgressDialog == null || !_DoubleProgressDialog.isShowing())
            _DoubleProgressDialog = new DoubleProgressDialog(context);
        _DoubleProgressDialog.show(title, titleP1, titleP2, desc1Format, desc2Format, p1, max1, p2, max2, showCancel);
    }

    public static void SetSecondProgressbarVisibility(boolean visibility) {
        if (_DoubleProgressDialog != null)
            _DoubleProgressDialog.setSecondProgressbarVisibility(visibility);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_progress);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = (int) (Display.getWidth(getContext()) * .9);
        params.height = (int) (WindowManager.LayoutParams.WRAP_CONTENT);
//		params.height = WindowManager.LayoutParams.FILL_PARENT;
//		params.width = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);

        initialize();
        init();
    }

    void initialize() {
        mPb1 = (ProgressBar) findViewById(R.id.progressBarHorizontal1);
        mPb2 = (ProgressBar) findViewById(R.id.progressBarHorizontal2);

        mBtnCancel = findViewById(R.id.img_progress_cancel);
        mTxtTitle = (TextView) findViewById(R.id.txt_progress_Title);
        mTxtTitleP1 = (TextView) findViewById(R.id.txt_p1_title);
        mTxtTitleP2 = (TextView) findViewById(R.id.txt_p2_title);
        mTxtDescP1 = (TextView) findViewById(R.id.txt_p1_desc);
        mTxtDescP2 = (TextView) findViewById(R.id.txt_p2_desc);
    }

    public void init() {


        mPb1.setMax(mMax1);
        mPb1.setProgress(mP1);
        mPb2.setMax(mMax2);
        mPb2.setProgress(mP2);

        view.setVisibility(mBtnCancel, mShowCancel);
        Text.setText(mTxtTitle, mTitle, Font.TextPos.h1);

        Text.setText(mTxtTitleP1, mTitleP1, Font.TextPos.h2);
        Text.setText(mTxtTitleP2, mTitleP2, Font.TextPos.h2);

        Text.setText(mTxtDescP1, String.format(mDesc1Format, mP1, mMax1), Font.TextPos.p);
        Text.setText(mTxtDescP2, String.format(mDesc2Format, mP2, mMax2), Font.TextPos.p);

        mBtnCancel.setOnClickListener(this);
    }

    public void setZero() {
        mMax1 = 0;
        mP1 = 0;
        mMax2 = 0;
        mP2 = 0;
        mTitleP1 = "";
        mTitleP2 = "";
        init();
    }

    public void setSecondProgressbarVisibility(boolean visibility) {
        view.setVisibility(findViewById(R.id.group_secondProgress), visibility);
    }

    public void show(String title, String titleP1, String titleP2, String desc1Format, String desc2Format, int p1, int max1, int p2, int max2, boolean showCancel) {
        _canceled = false;
        mTitle = title;
        mShowCancel = showCancel;
//		mFont = fontName;
        mDesc1Format = desc1Format;
        mDesc2Format = desc2Format;
        mTitleP1 = titleP1;
        mTitleP2 = titleP2;
        mMax1 = max1;
        mMax2 = max2;
        mP1 = p1;
        mP2 = p2;

        if (!isShowing())
            this.show();
        init();


    }

    @Override
    public void hide() {
//        Display.releaseScreen();
//        Display.releasScreenOn();
        super.hide();

    }

    @Override
    public void dismiss() {
        Display.releasScreenOn();
        super.dismiss();

    }

    @Override
    public void show() {
//        Display.fixScreen();
//        Display.keepScreenOn(getContext());
        try {
            super.show();
        } catch (Exception ex) {
            Log.e("DoubleProgressDialog", ex.getMessage());
        }
    }

    @Override
    protected void onStop() {
        setZero();
//        Display.releaseScreen();
//        Display.releasScreenOn();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_progress_cancel) {
            cancel();

        }
    }

    @Override
    public void cancel() {
        _canceled = true;
        super.cancel();

    }
}
