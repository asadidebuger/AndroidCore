package ir.microsign.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.R;
import ir.microsign.utility.Text;

/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 6/25/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class WaitingDialog extends AlertDialog {

    static WaitingDialog mWaitingDialog = null;
String text=null;
    public WaitingDialog(Context context) {
        super(context);
    }
    public WaitingDialog(Context context,String text) {
        super(context);
        this.text=text;
    }

    public static void showWaitingDialog(Context context, boolean show) {
        if (show && (mWaitingDialog == null || !mWaitingDialog.isShowing())) {

            mWaitingDialog = new WaitingDialog(context);

            mWaitingDialog.show();
            return;
        }
        if (!show && mWaitingDialog != null)
            mWaitingDialog.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = Display.getHeightWithoutBar(getContext());
        layoutParams.width = Display.getWidth(getContext());
        window.setAttributes(layoutParams);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.layout_waiting);
        if(text==null)text=getContext().getString( R.string.please_wait);
        Text.setText(findViewById(R.id.txt_waite),text, Font.TextPos.h1);
        setCancelable(false);
    }

    @Override
    public void hide() {
        try {
            if (this.isShowing())
                dismiss();
        } catch (Exception e) {
            Log.e("waiting dialog", e.getMessage());
        }

    }

}
