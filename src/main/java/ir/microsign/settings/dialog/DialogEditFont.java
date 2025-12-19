package ir.microsign.settings.dialog;

import android.content.Context;

import ir.microsign.dialog.BaseAlterDialog;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.settings.view.EditFontView;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogEditFont extends BaseAlterDialog {
    String mFontKey = null;
    EditFontView mEditFontView = null;

    public DialogEditFont(Context context, String fontKey) {

        super(context);
        mFontKey = fontKey;
    }

    @Override
    public void inIt() {
        setLayout();
        if (mEditFontView == null) mEditFontView = new EditFontView(getParentActivity(), mFontKey);
        setContentView(mEditFontView);
    }
boolean mustSave=true;
    public void forceHide(){
        mustSave=false;
        hide();
    }
    @Override
    public void dismiss() {
        if (mustSave)mEditFontView.onResult(mEditFontView,1);
        super.dismiss();
    }

    public void setOnResultListener(Listener.ResultListener l) {
        mEditFontView.setOnResultListener(l);
    }
}
