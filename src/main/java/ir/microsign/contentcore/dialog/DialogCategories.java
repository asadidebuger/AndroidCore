package ir.microsign.contentcore.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import java.util.List;

import ir.microsign.contentcore.view.CategoriesViewLarge;
import ir.microsign.dialog.BaseDialog;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.settings.object.SelectableItem;
import ir.microsign.utility.Display;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogCategories extends BaseDialog {
    //	public	int mSelected = -1;
    public String mTitle = "";
    public CategoriesViewLarge mView = null;
    public List<SelectableItem> mItems = null;
//    int mMarked, mLearned;
    String mParentCat = "", mSelectedCat = "";
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
    public DialogCategories(Context context, String title, String parentCat) {

        super(context);
        mParentCat = parentCat;
//        mMarked = marked;
//        mLearned = learned;
        mTitle = title;
//        mSelectedCat = selectedCat;
    }

    public void setItems(List<SelectableItem> items) {
        mItems = items;
        inIt();
    }

    public void setOnDialogResultListener(MessageDialog.OnDialogResultListener l) {
        mOnDialogResultListener = l;
        if (mView != null)
            mView.setOnDialogResultListener(l);
    }

    @Override
    public void inIt() {
        if (mView == null) mView = new CategoriesViewLarge(getContext());
//        mView.mMarked = mMarked;
//        mView.mLearned = mLearned;
//        mView.mParentCatId = mParentCat;
//        mView.mSelectedCat = mSelectedCat;
//        mView.title = title;
//        mView.setView();
        mView.setOnDialogResultListener(mOnDialogResultListener);
mView.mTitle=mTitle;
        setContentView(mView);
        mView.setItems(mParentCat, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
    }

    public void setLayout() {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(Display.getWidth(getContext()), Display.getHeightWithoutBar(getContext()));
    }
}

