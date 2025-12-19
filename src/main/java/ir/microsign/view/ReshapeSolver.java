package ir.microsign.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import ir.microsign.utility.Display;
import ir.microsign.R;
import ir.microsign.context.Preference;
import ir.microsign.utility.Font;
import ir.microsign.utility.Reshape;
import ir.microsign.utility.Text;


/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 10/15/13
 * Time: 9:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReshapeSolver extends AlertDialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView mTxtPersianText = null;
    private ViewHtml mWebView=null;
    private ToggleButton mBtnSwitch = null, mBtnSwitchWeb =null;
//    SharedPreferences mPreferences = null;
    onResultListener mOnResultListener=null;

    protected ReshapeSolver(Context context) {
        super(context);
    }

    protected ReshapeSolver(Context context, int theme) {
        super(context, theme);
    }

    protected ReshapeSolver(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static void Show(Context context ,onResultListener l) {
        ReshapeSolver _messageBox = new ReshapeSolver(context);
        _messageBox.mOnResultListener=l;
        _messageBox.setCancelable(true);
        _messageBox.show();

    }    public static void Show(Context context ) {
       Show(context,null);

    }
    public static void ShowForFirst(Context context,onResultListener l) {
        if (Build.VERSION.SDK_INT>=19) return;
        String mustShow="reshape_solver_shown";//context.getString(R.string.settings_key_reshape_must_show);
        if (Preference.getBool(context,mustShow, true)) {

            Show(context,l);
        }
    }
    public static void ShowForFirst(Context context) {
        ShowForFirst(context,null);
    }

    public interface onResultListener{
        void onResult(boolean ok);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reshaperesolver);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);

        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = (int) (Display.getWidth(getContext()) * .9);
        init();
    }

    private void getBeReshape() {

        Reshape.mReshape = Preference.getBool(getContext(),getContext().getString(R.string.settings_key_reshape), false);
        Reshape.mReshapeWeb = Preference.getBool(getContext(),getContext().getString(R.string.settings_key_reshape_web), true);
        if (Reshape.mReshape != mBtnSwitch.isChecked())
            mBtnSwitch.setChecked(Reshape.mReshape);
        if (Reshape.mReshapeWeb != mBtnSwitchWeb.isChecked())
            mBtnSwitchWeb.setChecked(Reshape.mReshapeWeb);
        Text.setText(mTxtPersianText, getContext().getString(R.string.persian_text), Font.TextPos.h1);
        mWebView.setContent(getContext().getString(R.string.persian_text_web),Font.getFont(getContext(),Font.TextPos.web).setBackColor(getContext().getResources().getColor(R.color.color_message_box_background)),true,true);
    }

    private void setBeReshape(boolean beReshape) {
        Preference.set(getContext(),getContext().getString(R.string.settings_key_reshape),beReshape);
        getBeReshape();

    }
    private void setBeReshapeWeb(boolean beReshape) {
        Preference.set(getContext(),getContext().getString(R.string.settings_key_reshape_web),beReshape);

        getBeReshape();

    }

    private void init() {

        mBtnSwitch = (ToggleButton) findViewById(R.id.btn_switch);
        mWebView= (ViewHtml) findViewById(R.id.web_content);
        mBtnSwitchWeb = (ToggleButton) findViewById(R.id.btn_switch_web);
        View btn_Ok = findViewById(R.id.btn_ok);
        mTxtPersianText = (TextView) findViewById(R.id.txt_persianText);
        getBeReshape();
        mBtnSwitch.setOnCheckedChangeListener(this);
        mBtnSwitchWeb.setOnCheckedChangeListener(this);
        btn_Ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Preference.set(getContext(),"reshape_solver_shown", false);
        if (mOnResultListener!=null)mOnResultListener.onResult(true);
        dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if (buttonView.equals(mBtnSwitch)) setBeReshape(isChecked);
      else setBeReshapeWeb(isChecked);
    }


}
