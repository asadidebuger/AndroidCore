package ir.microsign.settings.dialog;

import android.app.Activity;
import android.content.Context;

import ir.microsign.dialog.BaseAlterDialog;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.settings.view.SelectFontView;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogSelectFont extends BaseAlterDialog {
    int mSelected = -1;
    SelectFontView mSelectFontView = null;

    public DialogSelectFont(Activity activity, int selected) {

        super(activity, null);
        mSelected = selected;
    }

    public DialogSelectFont(Context context, int selected) {

        super(context);
        mSelected = selected;
    }

    public void setOnResultListener(Listener.ResultListener l) {
//		  inIt();
        mSelectFontView.setResultListener(l);
    }

    @Override
    public void inIt() {
        if (mSelectFontView == null) mSelectFontView = new SelectFontView(getContext(), mSelected);
        mSelectFontView.setSelection(mSelected);
        setContentView(mSelectFontView);
    }

    //	@Override
    public void show(int selected) {
        mSelected = selected;
        super.show();
        mSelectFontView.setSelection(selected);
    }
}

