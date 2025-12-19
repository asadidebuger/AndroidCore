package ir.microsign.content.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.View;

import java.util.List;

import ir.microsign.content.fragment.FragmentFullContent;
import ir.microsign.content.object.Content;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Text;

//import net.tarnian.content.object.BaseObject;

/**
 * Created by Mohammad on 6/29/14.
 */
public class ContentPagerAdapter extends FragmentStatePagerAdapter {
    List<BaseObject> mdBaseObjects = null;
    View.OnTouchListener mOnTouchListener = null;

    //	List<FragmentFullContent> mFragmentFullContents=null;
    public ContentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //	 Font mCustomFont=null;
//	public void setCustomFont(Font font)
//	{
//		mCustomFont=font;
//	}
    public ContentPagerAdapter(FragmentManager fm, List<BaseObject> baseObjects) {
        super(fm);
        mdBaseObjects = baseObjects;

//		mFragmentFullContents=new ArrayList<FragmentFullContent>();
//	for (int i=0;i<mdDbObjects.size();i++)
//		mFragmentFullContents.add(null);
    }

    public void setOnTouchListener(View.OnTouchListener l) {
        mOnTouchListener = l;
    }

    //	public void Refresh(int index){
//
//	}
    @Override
    public Fragment getItem(int i) {
        FragmentFullContent fragmentFullContent = new FragmentFullContent();

        Bundle bundle = new Bundle();
        bundle.putInt("_id", i);

        fragmentFullContent.setArguments(bundle);
//		FragmentFullContent.setCustomFont(mCustomFont);
        FragmentFullContent.setContentPagerAdapter(this);
        fragmentFullContent.setOnTouchListener(mOnTouchListener);
//		fragmentFullContent.setRetainInstance(false);
//		fragmentFullContent.refresh();
        return fragmentFullContent;
    }

    public Content getContent(int i) {
        return (Content) mdBaseObjects.get(i);
    }

    @Override
    public int getCount() {
        return mdBaseObjects == null ? 0 : mdBaseObjects.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Text.reshape(getContent(position).getPageTitle());
    }
}
