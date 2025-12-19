package ir.microsign.settings.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;

import ir.microsign.R;
import ir.microsign.settings.adapter.SelectableAdapter;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.settings.object.SelectableItem;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;


/**
 * Created by Mohammad on 7/23/14.
 */
public class SelectItemView extends LinearLayout implements View.OnClickListener {

    Listener.ResultListener mResultListener = null;
    EditText mEtxtFilter = null;
    int mSelected = -1;
    String mTitle = "";
    List<SelectableItem> mItems = null;
    SelectableAdapter mAdapter = null;

    public SelectItemView(Context context, String title, List<SelectableItem> items, int selected) {
        super(context);
        mSelected = selected;
        mItems = items;
        mTitle = title;
        inIt();
    }

    public void setItems(List<SelectableItem> items) {
        mItems = items;
        mAdapter = new SelectableAdapter(getContext(), mItems);
        filter();
    }

//	public SelectItemView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		inIt();
//	}

    public void setResultListener(Listener.ResultListener l) {
        mResultListener = l;

    }

    void inIt() {
        mAdapter = new SelectableAdapter(getContext(), mItems);
        view.getLayoutInflater(getContext()).inflate(R.layout.layout_select_item, this, true);
        LinearLayout root = (LinearLayout) findViewById(R.id.ll_root);
        Text.setText(findViewById(R.id.txt_title), mTitle, Font.TextPos.h1, true);
        Text.setHint(findViewById(R.id.etxt_search), R.string.setting_description_search, Font.TextPos.h1);
        findViewById(R.id.img_close).setOnClickListener(this);
        findViewById(R.id.img_search).setOnClickListener(this);
        mEtxtFilter = (EditText) findViewById(R.id.etxt_search);
        mEtxtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        view.setVisibility(mEtxtFilter, false);
        view.setVisibility(findViewById(R.id.txt_title), true);
        root.setOrientation(VERTICAL);

        filter();
    }

    public void setSelection(int id) {
        mSelected = id;
        for (SelectableItem item : mItems)
            item.setSelected(id == item.getId());
    }

    public void filter() {
        mAdapter.filter(mEtxtFilter.getText().toString());
        inItItems();

    }

    public void filter(String key) {
        mAdapter.filter(key);
        inItItems();

    }

    public void inItItems() {
        LinearLayout root = (LinearLayout) findViewById(R.id.ll_root);
        root.removeAllViews();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, root);
            if (item.getParent() != null) ((ViewGroup) item.getParent()).removeAllViews();
            item.setOnClickListener(this);
            root.addView(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_search) {
            view.invertVisibility(mEtxtFilter);
            view.setVisibility(findViewById(R.id.txt_title), !view.isVisible(mEtxtFilter));
        } else if (v.getId() == R.id.img_close) {
            if (mResultListener != null)
                mResultListener.onResult(null, -1);
        } else if (v instanceof SelectableItemView) {
            setSelection(((SelectableItemView) v).getID());
            if (mResultListener != null)
                mResultListener.onResult(v, ((SelectableItemView) v).getID());
        } else if (v.getId() == R.id.img_delete) {
            if (mResultListener != null)
                mResultListener.onResult(v, (Integer) v.getTag());
        }
    }
}
