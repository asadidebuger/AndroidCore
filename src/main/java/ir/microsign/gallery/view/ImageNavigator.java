package ir.microsign.gallery.view;

import android.content.Context;
import android.view.View;

import ir.microsign.utility.Display;
import ir.microsign.utility.view;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.gallery.object.BaseObject;
import ir.microsign.gallery.object.Image;
import ir.microsign.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ImageNavigator extends BaseView {
    List<Image> mImageList = new ArrayList<Image>();
    SimpleLargeImageView mLargeImageView = null;
    int position = 0;

    public ImageNavigator(Context context) {
        super(context);
    }

    public ImageNavigator(Context context, BaseObject baseObject) {
        super(context, baseObject);

    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        init();
    }

    void init() {
        if (getChildCount() > 0) return;
        getLayoutInflater().inflate(R.layout.layout_image_navigator, this);
        if (isInEditMode()) return;
        mLargeImageView = (SimpleLargeImageView) findViewById(R.id.img_large_image);
        findViewById(R.id.img_nav_right).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        findViewById(R.id.img_nav_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    void next() {
        setPosition(position + 1);
    }

    void back() {
        setPosition(position - 1);
    }

    public void setPosition(int position) {
        if (mLargeImageView == null)
            mLargeImageView = (SimpleLargeImageView) findViewById(R.id.img_large_image);
        if (mImageList == null || mImageList.size() < 1) {
            view.setVisibility(mLargeImageView, false);
            return;
        }
        view.setVisibility(mLargeImageView, true);
        view.setVisibility(findViewById(R.id.img_nav_right), mImageList.size() > 1);
        view.setVisibility(findViewById(R.id.img_nav_left), mImageList.size() > 1);

        if (position >= mImageList.size()) this.position = position % mImageList.size();
        else if (position < 0) this.position = mImageList.size() - 2 - position;
        else this.position = position;

        mLargeImageView.setImage(mImageList.get(this.position), Display.getWidth(getContext()), 0);
    }

    public void setImageList(List<Image> imageList) {
        mImageList = imageList;

        init();
        setPosition(0);
    }


}
