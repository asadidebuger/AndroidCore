package ir.microsign.contentcore.dialog;

import android.content.Context;

import ir.microsign.R;
import ir.microsign.dialog.MessageDialog;

/**
 * Created by Mohammad on 9/10/14.
 */
public class Message {
    static MessageDialog mSettDownloadImages = null;

    public static void showDowanloadAllImages(Context context, String imagesNum, MessageDialog.OnDialogResultListener l) {
//		if (mSettDefaultALL==null)
        mSettDownloadImages = new MessageDialog(context);
        mSettDownloadImages.setOnDialogResultListener(l);
        mSettDownloadImages.show(context.getString(R.string.message_dowanload_all_images_title), String.format(context.getString(R.string.message_dowanload_all_images_desc), imagesNum), "download_all_images");
    }

}
