package ir.microsign.content.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.R;
import ir.microsign.content.adapter.BaseAdapter;
import ir.microsign.content.database.DataSource;
import ir.microsign.content.object.ContentAlert;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Display;
import ir.microsign.utility.Text;

//import net.tarnian.content.object.BaseObject;

/**
 * Created by Mohammad on 6/25/14.
 */
public class FragmentBase extends Fragment implements AdapterView.OnItemClickListener {
    public String mSearchText = "";
    Listener.EventRequestListener mEventRequestListener = null;
    Listener.FragmentListener mFragmentListener = null;
    //	private GridView mGridView = null;
    ListView mList = null;
    private LayoutInflater mInflater = null;
    private ViewGroup mContainer = null;
    private View mRootView = null;
    private Listener.DbItemSelectedListener mDbItemSelectedListener = null;
    private BaseAdapter mBaseAdapter = null;
//    private DataSource mDataSource = null;

    DataSource getDataSource() {
//        if (mDataSource == null) mDataSource = new DataSource(getContext());
        return DataSource.getDataSource();
    }

    public void setEventRequestListener(Listener.EventRequestListener l) {
        mEventRequestListener = l;
    }

    public void setOnItemSelected(Listener.DbItemSelectedListener dbItemSelectedListener) {
        mDbItemSelectedListener = dbItemSelectedListener;
    }

    public void OnItemSelected(BaseObject selectedItem) {
        if (mDbItemSelectedListener != null && !(selectedItem instanceof ContentAlert))
            mDbItemSelectedListener.onItemSelected(selectedItem);

    }
//
//    public void search(String txt) {
//        mSearchText = txt;
////		reLoad();
//
//    }

    //	public void reLoad(){
//
//	}
    public boolean isSearchMode() {
        return !Text.isNullOrEmpty(mSearchText);
    }

    public void setAdapter(BaseAdapter adapter) {
        mBaseAdapter = adapter;
        final ViewGroup group = (ViewGroup) mList.getParent();
        final ViewGroup.LayoutParams layoutParams = mList.getLayoutParams();
        group.removeView(mList);
//		synchronized(mList) {
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				mBaseAdapter = adapter;
//				AttributeSet attrs=((ParallaxListView) mList).getAttributeSet();

//				mList=new ParallaxListView(getContext(),attrs);
//				((ParallaxListView) mList).para
        group.addView(mList, layoutParams);
        mList.setAdapter(mBaseAdapter);
        mList.setOnItemClickListener(FragmentBase.this);
//			}
//		},50);

//		}
    }

    public void setAdapter(List<BaseObject> baseObjects) {
        mBaseAdapter = new BaseAdapter(getContext(), baseObjects);
        setAdapter(mBaseAdapter);
    }

//	public void setDataSource(DataSource dataSource) {
//		mDataSource = dataSource;
//	}

//	public void setNumColumns(int i) {
////		mList.setNumColumns(i);
//	}

//	public void setVerticalSpacing(int i) {
//		mGridView.setVerticalSpacing(i);
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;
        setContentView(getLayoutResource());

        return mRootView;

    }

    public int getLayoutResource() {
        return R.layout.layout_fragment_list;
    }

    public void setFragmentListener(Listener.FragmentListener l) {
        mFragmentListener = l;
    }

    public void setContentView(int resId) {
        mRootView = mInflater.inflate(resId, mContainer, false);
    }

    public void afterCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		mGridView = (GridView) findViewById(net.tarnian.content.R._id.gridView);
        mList = (ListView) findViewById(R.id.list_view);
//		mList.setAnimationCacheEnabled(true);
    }

    public View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        afterCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFragmentListener != null) mFragmentListener.onStart(this);
    }

    public Context getContext() {
        return getActivity().getApplicationContext();
    }

//	@Override
//	public void onStop() {
//		super.onStop();
//		mDataSource.close();
//	}

    public boolean isLandscape() {
        return Display.isLandscape(getContext());
    }

    public List<BaseObject> getItems() {
        if (mBaseAdapter == null) return null;
        return mBaseAdapter.getItems();
    }

    public void setItems(List<BaseObject> baseObjects) {
        setAdapter(baseObjects);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OnItemSelected((BaseObject) mBaseAdapter.getItem(position));
    }
}
