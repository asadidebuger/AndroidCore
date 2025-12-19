package ir.microsign.contentcore.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.Locale;

import ir.microsign.R;
import ir.microsign.contentcore.adapter.BaseAdapter;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/24/14.
 */
public class CategoriesViewLarge extends CategoriesView {


//    public BaseAdapter mAdapter = null;
//    HListView mList = null;
//    OnClickListener mOnItemClickListener = null;
//    ImageView mImgLeft = null, mImgRight = null;
//    OnClickListener mOnNavClicked = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            onCategoriesNavClicked(v.equals(mImgRight));
//        }
//    };
public String mTitle="";
    public CategoriesViewLarge(Context context) {
        super(context);
    }

    public CategoriesViewLarge(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CategoriesViewLarge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItemSelected(BaseObject selectedItem) {
        if (mAdapter == null) return;
        mAdapter.setItemSelected(selectedItem);
    }

    public void setItemSelectedById(int id, int nextTry) {
        if (mAdapter == null) return;
        mAdapter.setItemSelected("id", id);
        if (mAdapter.getSelectedItem() == null || !mAdapter.getSelectedItem().getValue("id").equals(id))
            mAdapter.setItemSelected("id", nextTry);
        scrollToItem(mAdapter.getSelectedItem());
    }

    public void setItemSelectedById(int id) {
        if (mAdapter == null) return;
        scrollToItem(mAdapter.setItemSelected("_id", id));
    }

//    HListView getListView() {
//        if (mList != null) return mList;
//        this.setOrientation(HORIZONTAL);
//        mList = new HListView(getContext());
//        mList.setFadingEdgeLength(5);
//        int padding = Display.dpToPx(getContext(), 5);
//        mImgLeft = new ImageView(getContext());
//        mImgLeft.setAdjustViewBounds(true);
//        mImgLeft.setImageResource(R.drawable.nav_left);
//
//
//        mImgRight = new ImageView(getContext());
//        mImgRight.setAdjustViewBounds(true);
//        mImgRight.setImageResource(R.drawable.nav_right);
//        this.addView(mImgLeft, new LayoutParams(-2, -1));
//
//        this.addView(mList, new LayoutParams(-1, -2, 1));
//
//
//        this.addView(mImgRight, new LayoutParams(-2, -1));
//        mImgLeft.setPadding(padding, padding, padding, padding);
//        mImgRight.setPadding(padding, padding, padding, padding);
//        mImgLeft.setOnClickListener(mOnNavClicked);
//        mImgRight.setOnClickListener(mOnNavClicked);
//        return mList;
//    }
    ListView mList=null;
    ListView getListView() {
        if (mList != null) return mList;

        this.setOrientation(VERTICAL);
        View v= view.getLayoutInflater(getContext()).inflate(R.layout.layout_categories_large,this,true);
        v.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
Text.setText(v.findViewById(R.id.txt_title), mTitle, Font.TextPos.h1);
        v.findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDialogResultListener != null)
                    mOnDialogResultListener.OnDialogResult(false, null);
            }
        });
        mList = (ListView) findViewById(R.id.listView);
//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mOnDialogResultListener != null)
//                    mOnDialogResultListener.OnDialogResult(true, String.format(Locale.ENGLISH,"%d",((CategorySimpleView)view).getItem().id));
//            }
//        });
//        mList.setFadingEdgeLength(5);
//        int padding = Display.dpToPx(getContext(), 5);
//        mImgLeft = new ImageView(getContext());
//        mImgLeft.setAdjustViewBounds(true);
//        mImgLeft.setImageResource(R.drawable.nav_left);
//
//
//        mImgRight = new ImageView(getContext());
//        mImgRight.setAdjustViewBounds(true);
//        mImgRight.setImageResource(R.drawable.nav_right);
//        this.addView(mImgLeft, new LayoutParams(-2, -1));

//        this.addView(mList, new LayoutParams(-1, -2, 1));


//        this.addView(mImgRight, new LayoutParams(-2, -1));
//        mImgLeft.setPadding(padding, padding, padding, padding);
//        mImgRight.setPadding(padding, padding, padding, padding);
//        mImgLeft.setOnClickListener(mOnNavClicked);
//        mImgRight.setOnClickListener(mOnNavClicked);
        return mList;
    }

    void onCategoriesNavClicked(boolean right) {
        BaseObject item = getAdapter().selectNext(right);
        scrollToItem(item);
        onItemClicked(item.getView(getContext()));

    }

    public BaseAdapter getAdapter() {
        try {
            return (BaseAdapter) getListView().getAdapter();
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
//        if (mOnItemClickListener == null) return;
//        mOnItemClickListener.onClick(view);
        if (mOnDialogResultListener != null)
            mOnDialogResultListener.OnDialogResult(true, String.format(Locale.ENGLISH,"%d",((CategorySimpleView)view).getItem().id));
    }

//    void setNavVisible(boolean visible) {
//        view.setVisibility(mImgLeft, visible);
//        view.setVisibility(mImgRight, visible);
//
//    }

    public void setItems( int ParentCat, OnClickListener l) {
        List<BaseObject> items = (List<BaseObject>) DataSource.getDataSource().getSimpleSubCategories(ParentCat);
        setItems(items, l);
    }

    public void setItems(List<BaseObject> items, OnClickListener l) {

        mOnItemClickListener = l;

        if (getAdapter() == null) {
            mAdapter = new BaseAdapter(getContext(), items);
            getListView().setAdapter(mAdapter);
        } else if (items.size() > 0)
            getAdapter().setItems(items);
        else
            getAdapter().clean();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(view);
            }
        });
//        setNavVisible(items.size() > 4);

    }

    public void scrollToItem(BaseObject item) {
        if (item == null) return;
        final int position = item.mPosition == null ? mAdapter.getItemPosition(item) : item.mPosition;

        int first = getListView().getFirstVisiblePosition();
        if (Math.abs(position - first) < 10) {
            getListView().smoothScrollToPosition(position);
            return;
        }
        getListView().post(new Runnable() {
            @Override
            public void run() {
                getListView().setSelection(position);
                View v = getListView().getChildAt(position);
                if (v != null) {
                    v.requestFocus();
                }
            }
        });

    }
    public MessageDialog.OnDialogResultListener mOnDialogResultListener = null;
    public void setOnDialogResultListener(MessageDialog.OnDialogResultListener l) {
        mOnDialogResultListener = l;
    }
}
