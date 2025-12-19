package ir.microsign.gallery.adapter;

import android.app.Activity;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import ir.microsign.gallery.object.Image;

import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Image> mImageList;
//	private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<Image> imagePaths) {
        this._activity = activity;
        this.mImageList = imagePaths;
    }

    public Image getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public int getCount() {
        return this.mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View viewLayout = mImageList.get(position).getLargeView(_activity);
        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

}
