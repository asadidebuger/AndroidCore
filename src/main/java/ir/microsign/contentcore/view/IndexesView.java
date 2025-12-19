package ir.microsign.contentcore.view;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ir.microsign.R;
import ir.microsign.contentcore.adapter.BaseAdapter;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.object.BookIndexItem;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/24/14.
 */
public class IndexesView extends LinearLayout {

    BaseAdapter mAdapter = null;
    ListView mListView = null;
    CountDownTimer mTimer = new CountDownTimer(2000, 2000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            setFastScrollVisible(false);
        }
    };
    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mTimer.start();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mTimer.cancel();
            setFastScrollVisible(true);
        }
    };
    OnClickListener mOnItemClickListener = null;

    public IndexesView(Context context) {
        super(context);
    }

    public IndexesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutInflater getLayoutInflater() {
        return view.getLayoutInflater(getContext());
    }
    EditText mEtxtSearch=null;
    View mBtnClear=null;

    void setView() {
        if (getChildCount() > 0) return;
        View view = getLayoutInflater().inflate(R.layout.layout_indexes, this, false);

        this.addView(view, -1, -1);
        mEtxtSearch= (EditText) findViewById(R.id.etxt_filter);
        mBtnClear=findViewById(R.id.img_clear);
        Text.setHint(mEtxtSearch, R.string.hint_filter_text, Font.TextPos.h1);
        ir.microsign.utility.view.setVisibility(mBtnClear,!Text.isNullOrEmpty( mEtxtSearch.getText().toString()));
        mBtnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtxtSearch.setText(null);
            }
        });

        mEtxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {

                String s0=s.toString();
                ir.microsign.utility.view.setVisibility(mBtnClear,!Text.isNullOrEmpty(s0));
                filter(s0, false, false, true);
            }
        });

        mEtxtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    search(v.getText().toString(), null);
//                    return true;
//                }
                return false;
            }
        });
    }

    public void setItemSelected(BaseObject selectedItem) {
        if (mAdapter == null) return;
        mAdapter.setItemSelected(selectedItem);
    }
public BaseObject getSelected(){
    if (mAdapter == null) return null;
 return  mAdapter.getSelectedItem();
}
    ListView getListView() {
        if (mListView == null) {
            mListView = (ListView) findViewById(R.id.list_view0);
            mListView.setFadingEdgeLength(0);
//			mListView.setSelector(null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mListView.setOnScrollListener(mOnScrollListener);
                mListView.setVerticalScrollbarPosition(SCROLLBAR_POSITION_RIGHT);

            }
        }
        return mListView;
    }

    void setFastScrollVisible(boolean active) {
        if (active && (getAdapter() == null || getAdapter().getCount() < 50)) active = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mListView.setFastScrollEnabled(active);
            mListView.setFastScrollAlwaysVisible(active);
//			mListView.setVerticalScrollbarPosition(active?SCROLLBAR_POSITION_RIGHT:SCROLLBAR_POSITION_LEFT);

        }
    }

    BaseAdapter getAdapter() {
        try {
            return (BaseAdapter) getListView().getAdapter();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
public void filter(String text,boolean startWith,boolean endWith,boolean noCase){
    getAdapter().filter(text, startWith, endWith,noCase);
//    Object o=getAdapter().getItem(0);
//    if (o==null)return;
    getListView().postDelayed(new Runnable() {
        @Override
        public void run() {
            ScrollToItem((BookIndexItem) getAdapter().getItem(0));
        }
    },100);

}
    void onItemClicked(View view) {
        if (mOnItemClickListener == null) return;
        mOnItemClickListener.onClick(view);
    }

    public void setItems(DataSource dataSource, String ParentCat, String ParentItem, String selectedItem, OnClickListener l) {
        setView();
        mOnItemClickListener = l;
        List<BaseObject> items = (List<BaseObject>) dataSource.getBookIndexItems(ParentCat, ParentItem);
        if (getAdapter() == null) {
            mAdapter = new BaseAdapter(getContext(), items);
            getListView().setAdapter(mAdapter);
        } else if (items.size() > 0)
            getAdapter().setItems(items);
        else
            getAdapter().clean();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                onItemClicked(view);
            }
        });
//		BookIndexItem selected=new BookIndexItem();selected._id=selectedItem;
        getAdapter().setItemSelected("_id",selectedItem);
        view.setVisibility(this, items.size() > 0);
        getLayoutParams().width = items.size() > 0 ? -1 : 0;
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

    public void ScrollToItem(BookIndexItem item) {
        if (item == null) return;
        final int position = item.mPosition == null ? mAdapter.getItemPosition(item) : item.mPosition;
        int first = getListView().getFirstVisiblePosition();
        if (Math.abs(position - first) < 40) {
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

}
