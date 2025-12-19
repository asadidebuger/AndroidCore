package ir.microsign.contentcore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/24/14.
 */
public class SimpleLargeImageView extends LinearLayout {
    int mWidth, mHeight;

    Image mImage;
    public SimpleLargeImageView(Context context) {
        super(context);
    }

    public SimpleLargeImageView(Context context, Image image) {
        super(context);
        mImage=image;

    }

    public SimpleLargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //	public LargeImageView(Context activity, AttributeSet attrs, int defStyle) {
//		((LinearLayout)super).this(activity,attrs,defStyle);
//	}
//    @Override
//    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
//        super.initFromBaseObject(baseObject);
//        if (isInEditMode()) return;
//        getLayoutInflater().inflate(R.layout.layout_simple_large_image, this);
//
//        setView();
//    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
                view.getLayoutInflater(getContext()).inflate(R.layout.layout_simple_large_image, this);
init();
    }

    public void setImage(Image image, int w, int h) {
        mWidth = w;
        mHeight = h;
        mImage=image;
        init();

    }

    void init() {
        if (mImage== null) return;
        Text.setText(findViewById(R.id.txt_image_title),"", Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_image_desc), "", Font.TextPos.p);
        Image.setImage((ImageView) findViewById( R.id.img_display),null,findViewById(R.id.prg_wait),mImage);
//        getImage().setImageToImageView(this, R.id.img_display, R.id.prg_wait, Image.NORMAL, mWidth, mHeight);
    }

//    public Image getImage() {
//        return (Image) getDbObject();
//    }


}
