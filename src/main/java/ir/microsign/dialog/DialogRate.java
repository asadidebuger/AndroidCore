package ir.microsign.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Mohammad on 19/04/2015.
 */
public class DialogRate extends BaseDialog {
    public DialogRate(Context context) {
        super(context);
    }

    public DialogRate(Activity activity, Bundle bundle) {
        super(activity, bundle);
    }

    public DialogRate(Context context, int theme) {
        super(context, theme);
    }

    public DialogRate(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void inIt() {
        super.inIt();
    }
}
