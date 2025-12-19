package ir.microsign.gallery.adapter;

import android.app.Activity;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import ir.microsign.gallery.view.LargeImagePathView;

import java.util.List;

public class FullScreenImagePathAdapter extends PagerAdapter {

    private Activity _activity;
    private List<String> mImageList;
//	private LayoutInflater inflater;

    // constructor
    public FullScreenImagePathAdapter(Activity activity,
                                      List<String> imagePaths) {
        this._activity = activity;
        this.mImageList = imagePaths;
    }

    public String getItem(int position) {
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

        View viewLayout = new LargeImagePathView(_activity, getItem(position));
        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

}
