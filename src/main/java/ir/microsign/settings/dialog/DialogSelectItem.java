package ir.microsign.settings.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

import ir.microsign.dialog.BaseAlterDialog;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.settings.object.SelectableItem;
import ir.microsign.settings.view.SelectItemView;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogSelectItem extends BaseAlterDialog {
    public int mSelected = -1;
    public String mTitle = "";
    public SelectItemView mSelectItemView = null;
    public List<SelectableItem> mItems = null;

    public DialogSelectItem(Activity activity, String title, List<SelectableItem> items, int selected) {

        super(activity, null);
        mSelected = selected;
        mItems = items;
        mTitle = title;
    }

    public DialogSelectItem(Context context, String title, List<SelectableItem> items, int selected) {

        super(context);
//		if (context instanceof Activity)mParentActivity=(Activity)context;
        mSelected = selected;
        mItems = items;
        mTitle = title;
    }

    public void setItems(List<SelectableItem> items) {
        mItems = items;
        inIt();
    }

    public void setOnResultListener(Listener.ResultListener l) {
        mSelectItemView.setResultListener(l);
    }

    @Override
    public void inIt() {
        if (mSelectItemView == null) mSelectItemView = new SelectItemView(getContext(), mTitle, mItems, mSelected);
        else mSelectItemView.setItems(mItems);
        mSelectItemView.setSelection(mSelected);
        setContentView(mSelectItemView, new ViewGroup.LayoutParams(-1, -1));

    }

    @Override
    public void setLayout() {
        super.setLayout();

    }

    //	@Override
    public void show(int selected) {
        mSelected = selected;
        show();

    }
}

