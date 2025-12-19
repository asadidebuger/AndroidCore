package ir.microsign.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.R;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.context.Preference;
import ir.microsign.view.ViewHtml;




/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 10/15/13
 * Time: 9:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageDialog extends AlertDialog implements View.OnClickListener {
    public boolean mResult = false;
    public String mTitle, mKey, mCheckBoxTitle, mDescription;
    public boolean mShowCancel = false, mShowCheckbox = false;
    public OnDialogResultListener mOnDialogResultListener = null;
    String mOk, mCancel;
    public MessageDialog(Context context) {
        super(context);
    }

    public static void showMessage(Context context, String title, String desc) {
        new MessageDialog(context).show(title, desc);
    }

    public static void showMessage(Context context, int title, int desc) {
        new MessageDialog(context).show(title, desc);
    }

    public void setOnDialogResultListener(OnDialogResultListener l) {
        mOnDialogResultListener = l;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_messagebox);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = (int) (Display.getWidth(getContext()) * .9);
        layoutParams.height = (int) (Display.getHeightWithoutBar(getContext()) * .9);
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(R.drawable.transparent);
        init();
    }

    public void init() {
        ViewHtml webView = (ViewHtml) findViewById(R.id.web_content);
        View btnOk = findViewById(R.id.txt_ok);
        View btnCancel = findViewById(R.id.txt_cancel);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        CheckBox chbxRemember = (CheckBox) findViewById(R.id.chbx_remember);
        Text.setText(btnOk, Text.isNullOrEmpty(mOk) ? getContext().getString(R.string.ok) : mOk, Font.TextPos.h1, true);
        Text.setText(btnCancel, Text.isNullOrEmpty(mCancel) ? getContext().getString(R.string.cancel) : mCancel, Font.TextPos.h1, true);


        chbxRemember.setChecked(false);
        if (mKey != null && mKey.length() > 0 && mShowCheckbox)
            chbxRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Preference.set(getContext(), mKey, isChecked);
                }
            });


        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        Font font = Font.getFont(getContext(),Font.TextPos.web);
        font.setBackColor(getContext().getResources().getColor(R.color.color_message_box_background));
        webView.setContent(mDescription, font, true);

        view.setVisibility(chbxRemember, mShowCheckbox);
        view.setVisibility(btnCancel, mShowCancel);
        Text.setText(chbxRemember, mCheckBoxTitle, Font.TextPos.p);
        Text.setText(txtTitle, mTitle, Font.TextPos.h1);


    }

    public void show(int title, int description, String key) {
        show(getContext().getString(title), getContext().getString(description), key);
    }

    public void show(int title, int description, String ok, String cancel, String key) {
        mOk = ok;
        mCancel = cancel;
        show(getContext().getString(title), getContext().getString(description), key);
    }

    public void show(String title, String description, String key) {
        show(title,  description, key,true);
    }
    public void show(String title, String description, String key,boolean showCancel) {
        mKey = key;
        mShowCheckbox = !Text.isNullOrEmpty(key);
        mCheckBoxTitle = getContext().getString(R.string.remember);
        mTitle = title;
        mDescription = description;
        mShowCancel = showCancel;
        this.setCancelable(false);
        show();
    }
    public void show(String title, String description, boolean showCancel, int command, boolean showCheckbox, String checkBoxTitle, String key) {
        this.show(title, description, true, showCancel, command, showCheckbox, checkBoxTitle, key);

    }

    public void show(String title, String description, boolean cancelable, boolean showCancel, int command, boolean showCheckbox, String checkBoxTitle, String key) {
        mKey = key;
        mShowCheckbox = showCheckbox;
        mCheckBoxTitle = checkBoxTitle;
        mTitle = title;
        mDescription = description;
//		Command = command;
        mShowCancel = showCancel;
        this.setCancelable(cancelable);

        this.show();
    }

    @Override
    public void show() {
        boolean notShow = (Boolean) Preference.getValue(getContext(), mKey, false);
        if (notShow) {
            onResult(true, mKey);
            return;
        }
        try {
            super.show();
            init();
        } catch (Exception ex) {
            Log.e("MessageDialog", ex.getMessage());
        }

    }

    public void show(String title, String description, int command, boolean showCheckbox, String checkBoxTitle, String key) {
        show(title, description, true, command, showCheckbox, checkBoxTitle, key);
    }

    public void show(String title, String description) {
        show(title, description, false, -1, false, null, null);
    }

    public void show(int title, int description) {
        show(getContext().getString(title), getContext().getString(description));
    }

    public void show(String title, String description, int command, boolean showCancel, boolean showCheckbox, String checkBoxTitle, String key) {

        show(title, description, showCancel, command, showCheckbox, checkBoxTitle, key);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.txt_cancel) {
            mResult = false;

        } else if (i == R.id.txt_ok) {
            mResult = true;

        }
        dismiss();
    }

    @Override
    public void hide() {
//		super.hide();
        dismiss();
    }

    @Override
    public void dismiss() {
        mKey = "";
        onResult(mResult, mKey);
//		Command = COMMAND_NON;
//		mResult = false;
        super.dismiss();

    }

    public void onResult(boolean result, String key) {
        if (mOnDialogResultListener != null) mOnDialogResultListener.OnDialogResult(result, key);
    }

    public interface OnDialogResultListener {
        public void OnDialogResult(boolean ok, String key);
    }
}
