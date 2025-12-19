package ir.microsign.dialog;

import android.content.Context;

import cn.pedant.SweetAlert.widget.SweetAlertDialog;
import ir.microsign.R;
import ir.microsign.net.Utility;


/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 6/25/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectDialog  {
    public interface OnDialogResultListener {
         void OnDialogResult(boolean ok, boolean isConnect);
    }


   public static void show(final Context context, final OnDialogResultListener l){
        if (Utility.isConnect(context)) {
			l.OnDialogResult(true, true);
			return;
		}
        new SweetAlertDialog( context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(R.string.connect_title)
                .setContentText(R.string.connect_desc)
                .setCancelText(R.string.cancel)
                .setConfirmText(R.string.connect_retry)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        l.OnDialogResult(false,Utility.isConnect(context));
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

						if (Utility.isConnect(context)) {
							sDialog.dismiss();
							l.OnDialogResult(true,true);
							return;
						}
						sDialog.setContentText(R.string.connect_retry_desc).changeAlertType(SweetAlertDialog.ERROR_TYPE);

                    }
                })
                .show();
    }

}
