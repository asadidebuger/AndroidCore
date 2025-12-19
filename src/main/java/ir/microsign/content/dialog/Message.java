package ir.microsign.content.dialog;

import android.content.Context;

import ir.microsign.R;
import ir.microsign.dialog.MessageDialog;

/**
 * Created by Mohammad on 9/10/14.
 */
public class Message {
    static MessageDialog mSettDefaultALL = null;
    static MessageDialog mSettDefaultOkALL = null;
    static MessageDialog mDownloadContents = null;

    public static void showSettDefaultAll(Context context, String section, MessageDialog.OnDialogResultListener l) {
//		if (mSettDefaultALL==null)
        mSettDefaultALL = new MessageDialog(context);
        mSettDefaultALL.setOnDialogResultListener(l);
        mSettDefaultALL.show(context.getString(R.string.message_title_default_section), String.format(context.getString(R.string.message_desc_default_section), section), Keys.set_default_all);
    }

    public static void showSettOkAll(Context context, String section, MessageDialog.OnDialogResultListener l) {
//		if (mSettDefaultOkALL==null)
        mSettDefaultOkALL = new MessageDialog(context);
        mSettDefaultOkALL.setOnDialogResultListener(l);
        mSettDefaultOkALL.show(context.getString(R.string.message_title_ok_section), String.format(context.getString(R.string.message_desc_ok_section), section), Keys.set_ok_all);
    }

    public static void showUpdateContents(Context context, String categoryName, MessageDialog.OnDialogResultListener l) {

        mDownloadContents = new MessageDialog(context);
        mDownloadContents.setOnDialogResultListener(l);
        mDownloadContents.show(context.getString(R.string.message_title_update_contents), String.format(context.getString(R.string.message_desc_update_contents), categoryName), Keys.update_contents);
    }

    public static class Keys {
        public static String set_default_all = "set_default_all", set_ok_all = "set_ok_all", update_contents = "update_contents";
    }
}
