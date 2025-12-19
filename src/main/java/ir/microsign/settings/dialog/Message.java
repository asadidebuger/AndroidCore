package ir.microsign.settings.dialog;

import android.content.Context;

import ir.microsign.dialog.MessageDialog;
import ir.microsign.R;

//import net.tarnian.content.R;

/**
 * Created by Mohammad on 9/10/14.
 */
public class Message {
    static MessageDialog mSettDefaultALL = null;
    static MessageDialog mSettDefaultOkALL = null;
    static MessageDialog mSettDefaultOkALLSECTIONS = null;

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

    public static void showSettOkAllSection(Context context, MessageDialog.OnDialogResultListener l) {
//		if (mSettDefaultOkALL==null)
        mSettDefaultOkALLSECTIONS = new MessageDialog(context);
        mSettDefaultOkALLSECTIONS.setOnDialogResultListener(l);
        mSettDefaultOkALLSECTIONS.show(context.getString(R.string.message_title_ok_section), context.getString(R.string.message_desc_ok_section_all), Keys.set_ok_all_sections);
    }

    public static class Keys {
        public static String set_default_all = "set_default_all", set_ok_all = "set_ok_all", set_ok_all_sections = "set_ok_all_sections";
    }

}
