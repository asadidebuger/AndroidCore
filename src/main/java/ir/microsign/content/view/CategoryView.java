package ir.microsign.content.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ir.microsign.R;
import ir.microsign.content.database.DataSource;
import ir.microsign.contentcore.object.Category;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;


/**
 * Created by Mohammad on 6/24/14.
 */
public class CategoryView extends BaseView {
//	Category mCategory = null;

//    static DataSource mDataSource = null;

    public CategoryView(Context context) {
        super(context);
    }
//
//	public CategoryView(Context activity, AttributeSet attrs) {
//		super(activity, attrs);
//	}

    public CategoryView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_item_category, this);
        TextView txtTitle = (TextView) findViewById(R.id.txt_titr);
        Text.setText(txtTitle, getCategory().title, Font.TextPos.h1);
        findViewById(R.id.ll_root2).setBackgroundResource(getCategory().mPosition % 2 == 0 ? R.drawable.back_selectable_category_odd : R.drawable.back_selectable_category_even);
        setImage();
    }

    DataSource getDataSource() {
        return DataSource.getDataSource();
//        if (mDataSource != null) return mDataSource;
//        mDataSource = new DataSource(getContext());
//        return mDataSource;
    }

    void setImage() {

        Image thumb = getCategory().getFirstImage(true);
        ImageView imageView = (ImageView) findViewById(R.id.img_image);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.prg_wait);
        if (thumb == null) {
            view.setVisibility(progressBar, false);
            imageView.setImageDrawable(null);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = 0;
            return;
        }
//        thumb.setImageToImageView(this, R._id.img_image, R._id.prg_wait, Image.THUMB, imageView.getWidth(), imageView.getHeight());
    }

    public Category getCategory() {
        return (Category) getDbObject();
    }

}
