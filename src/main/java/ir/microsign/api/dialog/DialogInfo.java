package ir.microsign.api.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import ir.microsign.R;
import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;


/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogInfo extends AlertDialog {
//    Notification mContent = null;

public View content;
private String title;
    public DialogInfo(Context context,String title, View view,onDialogListener l) {
        super(context);
        content=view;
        this.title=title;
        mOnDialogListener=l;
    }
//
//    public DialogInfo(Context context, int theme) {
//        super(context, theme);
//    }

//    public DialogInfo(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }
public void updateView(View view){
    content=view;

    LinearLayout llContent=findViewById(R.id.ll_content);
    llContent.removeAllViews();
    llContent.addView(content,-1,-1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(R.style.dialogStyle);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height =Display.getHeightWithoutBar(getContext());
        layoutParams.width = Display.getWidth(getContext());
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(ir.microsign.R.drawable.transparent);
//		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//		setContentView(new NotificationViewLarge(getContext()), lp);
    }

    @Override
    public void show() {
        super.show();
//        NotificationViewLarge viewLarge = new NotificationViewLarge(getContext(), mContent);
//        viewLarge.mOnDialogResultListener = new MessageDialog.OnDialogResultListener() {
//            @Override
//            public void OnDialogResult(boolean ok, String key) {
//                dismiss();
//            }
//        };
//        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        layoutParams.height =Display.getHeight(getContext());
//        layoutParams.width =Display.getWidth(getContext());
        setContentView(R.layout.layout_dialog_info);
        Text.setText(findViewById(R.id.txt_title),title, Font.TextPos.h1,false);
//        view.setVisibility(findViewById(R.id.img_header_icon1),false);
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        LinearLayout llContent=findViewById(R.id.ll_content);
        llContent.addView(content,-1,-1);
        findViewById(ir.microsign.R.id.img_header_icon1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnDialogListener!=null)mOnDialogListener.onUpdateRequest();
            }
        });
//        setContentView(content);

    }
private onDialogListener mOnDialogListener;
//    public void show(Notification content) {
//        mContent = content;
//        show();
//
//
//    }
public interface onDialogListener{
        void onUpdateRequest();
}

}
