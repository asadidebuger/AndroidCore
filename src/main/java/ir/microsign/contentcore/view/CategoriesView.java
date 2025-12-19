package ir.microsign.contentcore.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import ir.microsign.R;
import ir.microsign.contentcore.adapter.BaseAdapter;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Display;
import ir.microsign.utility.view;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Mohammad on 6/24/14.
 */
public class CategoriesView extends LinearLayout {


    public BaseAdapter mAdapter = null;
    HListView mList = null;
    OnClickListener mOnItemClickListener = null;
    ImageView mImgLeft = null, mImgRight = null;
    OnClickListener mOnNavClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onCategoriesNavClicked(v.equals(mImgRight));
        }
    };

    public CategoriesView(Context context) {
        super(context);
    }

    public CategoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CategoriesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItemSelected(final BaseObject selectedItem) {
        if (mAdapter == null) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setItemSelected(selectedItem);
            }
        },100);

    }

    public void setItemSelectedById(String id, String nextTry) {
        if (mAdapter == null) return;
        mAdapter.setItemSelected("_id", id);
        if (mAdapter.getSelectedItem() == null || !mAdapter.getSelectedItem().getValue("_id").equals(id))
            mAdapter.setItemSelected("_id", nextTry);
        scrollToItem(mAdapter.getSelectedItem());
    }

    public void setItemSelectedById(String id) {
        if (mAdapter == null) return;
        scrollToItem(mAdapter.setItemSelected("_id", id));
    }

    it.sephiroth.android.library.widget.HListView getList() {
        if (mList != null) return mList;
        this.setOrientation(HORIZONTAL);
        mList = new HListView(getContext());
        mList.setFadingEdgeLength(5);
        int padding = Display.dpToPx(getContext(), 5);
        mImgLeft = new ImageView(getContext());
        mImgLeft.setAdjustViewBounds(true);
        mImgLeft.setImageResource(R.drawable.nav_left);


        mImgRight = new ImageView(getContext());
        mImgRight.setAdjustViewBounds(true);
        mImgRight.setImageResource(R.drawable.nav_right);
        this.addView(mImgLeft, new LayoutParams(-2, -1));

        this.addView(mList, new LayoutParams(-1, -2, 1));


        this.addView(mImgRight, new LayoutParams(-2, -1));
        mImgLeft.setPadding(padding, padding, padding, padding);
        mImgRight.setPadding(padding, padding, padding, padding);
        mImgLeft.setOnClickListener(mOnNavClicked);
        mImgRight.setOnClickListener(mOnNavClicked);
        return mList;
    }

    void onCategoriesNavClicked(boolean right) {
        BaseObject item = getAdapter().selectNext(right);
        scrollToItem(item);
        onItemClicked(item.getView(getContext()));

    }

    public BaseAdapter getAdapter() {
        try {
            return (BaseAdapter) getList().getAdapter();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public boolean canVisible() {
        return getAdapter() != null && getAdapter().getCount() > 0;
    }

    public boolean isVisible() {
        return view.isVisible(this);
    }

    public void setVisible(boolean visible) {
        if (!canVisible() && visible) return;
        view.setVisibility(this, visible);
    }

    public boolean reverseVisible() {
        return canVisible() && view.invertVisibility(this);
    }

    public void onItemClicked(View view) {
        if (mOnItemClickListener == null) return;
        mOnItemClickListener.onClick(view);
    }

    void setNavVisible(boolean visible) {
        view.setVisibility(mImgLeft, visible);
        view.setVisibility(mImgRight, visible);

    }

    public void setItems(String ParentCat, OnClickListener l) {
        List<BaseObject> items = (List<BaseObject>) DataSource.getDataSource().getSubCategories(ParentCat);
        setItems(items, l);

    }

    public void setItems(List<BaseObject> items, OnClickListener l) {

        mOnItemClickListener = l;

        if (getAdapter() == null) {
            mAdapter = new BaseAdapter(getContext(), items);
            getList().setAdapter(mAdapter);
        } else if (items.size() > 0)
            getAdapter().setItems(items);
        else
            getAdapter().clean();
        getList().setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(view);
            }
        });
        setNavVisible(items.size() > 4);

    }

    public void scrollToItem(BaseObject item) {
        if (item == null) return;
        final int position = item.mPosition == null ? mAdapter.getItemPosition(item) : item.mPosition;

        int first = getList().getFirstVisiblePosition();
        if (Math.abs(position - first) < 10) {
            getList().smoothScrollToPosition(position);
            return;
        }
        getList().post(new Runnable() {
            @Override
            public void run() {
                getList().setSelection(position);
                View v = getList().getChildAt(position);
                if (v != null) {
                    v.requestFocus();
                }
            }
        });

    }

}
