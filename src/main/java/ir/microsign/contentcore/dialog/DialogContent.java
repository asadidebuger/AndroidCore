package ir.microsign.contentcore.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.view.DialogContentView;
import ir.microsign.R;
import ir.microsign.utility.Display;


/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 10/15/13
 * Time: 9:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class DialogContent extends AlertDialog implements View.OnClickListener {
    public boolean mResult = false;
//    public String title, mKey, mCheckBoxTitle, des;
//    public boolean mShowCancel = false, mShowCheckbox = false;
    OnDialogResultListener mOnDialogResultListener = null;
//    String mOk, mCancel;
    public DialogContent(Context context) {
        super(context);
    }

//    public static void showMessage(Context activity, String title, String desc) {
//        new DialogContent(activity).show(title, desc);
//    }

    public static void showMessage(Context context,Content content) {
        new DialogContent(context).show(content);
    }

    public void setOnDialogResultListener(OnDialogResultListener l) {
        mOnDialogResultListener = l;

    }
Content mContent=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(new DialogContentView(getContext(), mContent, new OnDialogResultListener() {
			@Override
			public void OnDialogResult(boolean ok, String key) {
				dismiss();
			}
		}));


        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = (int) (Display.getWidth(getContext()) * .98);
        layoutParams.height = (int) (Display.getHeightWithoutBar(getContext()) * .92);
        window.setAttributes(layoutParams);
//        setView();
    }

//    public void setView() {
//
////        ViewHtml webView = (ViewHtml) findViewById(R._id.web_content);
////        View btnOk = findViewById(R._id.txt_ok);
////        View btnCancel = findViewById(R._id.txt_cancel);
////        TextView txtTitle = (TextView) findViewById(R._id.txt_title);
////        CheckBox chbxRemember = (CheckBox) findViewById(R._id.chbx_remember);
////        Text.setText(btnOk, Text.isNullOrEmpty(mOk) ? Application.getContext().getString(R.string.ok) : mOk, Font.TextPos.h1, true);
////        Text.setText(btnCancel, Text.isNullOrEmpty(mCancel) ? Application.getContext().getString(R.string.cancel) : mCancel, Font.TextPos.h1, true);
////
////
////        chbxRemember.setChecked(false);
////        if (mKey != null && mKey.length() > 0 && mShowCheckbox)
////            chbxRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                @Override
////                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                    Preference.set(getContext(), mKey, isChecked);
////                }
////            });
////
////
////        btnCancel.setOnClickListener(this);
////        btnOk.setOnClickListener(this);
////        Font font = Font.getFont(Font.TextPos.web);
////        font.setBackColor(getContext().getResources().getColor(R.color.color_message_box_background));
////        webView.setContent(des, font, true);
////
////        view.setVisibility(chbxRemember, mShowCheckbox);
////        view.setVisibility(btnCancel, mShowCancel);
////        Text.setText(chbxRemember, mCheckBoxTitle, Font.TextPos.p);
////        Text.setText(txtTitle, title, Font.TextPos.h1);
//
//
//    }

//    public void show(int title, int description, String key) {
//        show(getContext().getString(title), getContext().getString(description), key);
//    }

//    public void show(int title, int description, String ok, String cancel, String key) {
//        mOk = ok;
//        mCancel = cancel;
//        show(getContext().getString(title), getContext().getString(description), key);
//    }

//    public void show(String title, String description, String key) {
//        mKey = key;
//        mShowCheckbox = !Text.isNullOrEmpty(key);
//        mCheckBoxTitle = getContext().getString(R.string.remember);
//        title = title;
//        des = description;
//        mShowCancel = true;
//        this.setCancelable(false);
//        show();
//    }
//
//    public void show(String title, String description, boolean showCancel, int command, boolean showCheckbox, String checkBoxTitle, String key) {
//        this.show(title, description, true, showCancel, command, showCheckbox, checkBoxTitle, key);
//
//    }
//
//    public void show(String title, String description, boolean cancelable, boolean showCancel, int command, boolean showCheckbox, String checkBoxTitle, String key) {
//        mKey = key;
//        mShowCheckbox = showCheckbox;
//        mCheckBoxTitle = checkBoxTitle;
//        title = title;
//        des = description;
////		Command = command;
//        mShowCancel = showCancel;
//        this.setCancelable(cancelable);
//
//        this.show();
//    }

    @Override
    public void show() {
//        boolean notShow = (Boolean) Preference.getValue(getContext(), mKey, false);
//        if (notShow) {
//            onResult(true, mKey);
//            return;
//        }
        try {
            super.show();
//            setView();
        } catch (Exception ex) {
            Log.e("MessageDialog", ex.getMessage());
        }

    }

//    public void show(String title, String description, int command, boolean showCheckbox, String checkBoxTitle, String key) {
//        show(title, description, true, command, showCheckbox, checkBoxTitle, key);
//    }

    public void show(Content content) {
        mContent=content;
        show();
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
//        mKey = "";
        onResult(mResult, null);
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
