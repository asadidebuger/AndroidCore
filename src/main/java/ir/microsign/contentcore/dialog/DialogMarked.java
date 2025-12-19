package ir.microsign.contentcore.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import java.util.List;

import ir.microsign.contentcore.view.MarkedLisView;
import ir.microsign.dialog.BaseDialog;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.settings.object.SelectableItem;
import ir.microsign.utility.Display;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogMarked extends BaseDialog {
    //	public	int mSelected = -1;
    public String mTitle = "";
    public MarkedLisView mMarkedLisView = null;
    public List<SelectableItem> mItems = null;
    int mMarked, mLearned;
    String mParentCat ="", mSelectedCat = "";
    MessageDialog.OnDialogResultListener mOnDialogResultListener = new MessageDialog.OnDialogResultListener() {
        @Override
        public void OnDialogResult(boolean ok, String key) {
            if (ok) hide();
        }
    };

    //	public DialogMarked(Activity activity, String title, List<SelectableItem> items, int selected) {
//
//		super(activity, null);
//		mSelected = selected;
//		mItems = items;
//		title = title;
//	}
    public DialogMarked(Context context, String title, String parentCat, String selectedCat, int marked, int learned) {

        super(context);
        mParentCat = parentCat;
        mMarked = marked;
        mLearned = learned;
        mTitle = title;
        mSelectedCat = selectedCat;
    }

    public void setItems(List<SelectableItem> items) {
        mItems = items;
        inIt();
    }

    public void setOnDialogResultListener(MessageDialog.OnDialogResultListener l) {
        mOnDialogResultListener = l;
        if (mMarkedLisView != null)
            mMarkedLisView.setOnDialogResultListener(l);
    }

    @Override
    public void inIt() {
        if (mMarkedLisView == null) mMarkedLisView = new MarkedLisView(getContext());
        mMarkedLisView.mMarked = mMarked;
        mMarkedLisView.mLearned = mLearned;
        mMarkedLisView.mParentCatId = mParentCat;
        mMarkedLisView.mSelectedCat = mSelectedCat;
        mMarkedLisView.mTitle = mTitle;
        mMarkedLisView.setView();
        mMarkedLisView.setOnDialogResultListener(mOnDialogResultListener);

        setContentView(mMarkedLisView);
    }

    public void setLayout() {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(Display.getWidth(getContext()), Display.getHeightWithoutBar(getContext()));
    }
}

