package ir.microsign.content.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.microsign.content.adapter.ContentPagerAdapter;
import ir.microsign.content.object.Content;
import ir.microsign.content.object.Helper;
import ir.microsign.content.view.FullContentView;

/**
 * Created by Mohammad on 6/29/14.
 */
public class FragmentFullContent extends Fragment {
    public static Activity mActivity;
    public static ContentPagerAdapter mContentPagerAdapter = null;
    static View.OnTouchListener mOnTouchListener = null;
    Content mContent = null;
    FullContentView mFullContentView = null;

    public FragmentFullContent() {

    }

    //   static Font mCustomFont=null;
//	public static void setCustomFont(Font font){
//		mCustomFont=font;
//	}
    public static void setContentPagerAdapter(ContentPagerAdapter contentPagerAdapter) {
        mContentPagerAdapter = contentPagerAdapter;
    }

    public void refresh() {
        if (mFullContentView == null) return;
        mFullContentView.setView();
    }

    //static 	DataSource mDataSourceContent=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity=getActivity();
//		FullContentView.setCustomFont(mCustomFont);
        int id = getArguments().getInt("_id");

        mContent = mContentPagerAdapter.getContent(id);
        if (mFullContentView == null) mFullContentView =  ((Helper)Content.getHelper()).getFullContentView(getActivity(),mContent);

//        if (mOnTouchListener != null) {
//            List<View> views = view.getAllChilds(mFullContentView);
//            for (View view : views)
//                view.setOnTouchListener(mOnTouchListener);
//        }
        return mFullContentView;


    }

    public void setOnTouchListener(View.OnTouchListener l) {
//		if (l==null)return;
        mOnTouchListener = l;
//		if (mOnTouchListener!=null){
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				List<View> views = view.getAllChilds(mFullContentView);
//				for (View view:views)
//					view.setOnTouchListener(mOnTouchListener);
//			}
//		},300);

    }
//	}
}

