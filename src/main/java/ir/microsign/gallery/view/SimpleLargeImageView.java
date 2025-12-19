package ir.microsign.gallery.view;

import android.content.Context;
import android.util.AttributeSet;

import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.gallery.object.BaseObject;
import ir.microsign.gallery.object.Image;

import ir.microsign.R;

/**
 * Created by Mohammad on 6/24/14.
 */
public class SimpleLargeImageView extends BaseView {
    int mWidth, mHeight;

    public SimpleLargeImageView(Context context) {
        super(context);
    }

    public SimpleLargeImageView(Context context, BaseObject baseObject) {
        super(context, baseObject);

    }

    public SimpleLargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //	public LargeImageView(Context context, AttributeSet attrs, int defStyle) {
//		((LinearLayout)super).this(context,attrs,defStyle);
//	}
    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        if (isInEditMode()) return;
        getLayoutInflater().inflate(R.layout.layout_simple_large_image, this);

        init();
    }

    public void setImage(Image image, int w, int h) {
        mWidth = w;
        mHeight = h;
        setDbObject(image);
        init();

    }

    void init() {
        if (getImage() == null) return;
        Text.setText(findViewById(R.id.txt_image_title), getImage().imgtitle, Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_image_desc), getImage().imgtext, Font.TextPos.p);
        getImage().setImageToImageView(this, R.id.img_display, R.id.prg_wait, Image.NORMAL, mWidth, mHeight);
    }

    public Image getImage() {
        return (Image) getDbObject();
    }


}
