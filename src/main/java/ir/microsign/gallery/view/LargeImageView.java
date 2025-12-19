package ir.microsign.gallery.view;

import android.content.Context;
import android.util.AttributeSet;

import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.gallery.object.BaseObject;
import ir.microsign.gallery.object.Image;

import ir.microsign.R;


/**
 * Created by Mohammad on 6/24/14.
 */
public class LargeImageView extends BaseView {
    static Boolean showTitle = null;
    static Boolean showDesc = null;

    public LargeImageView(Context context) {
        super(context);
    }

    public LargeImageView(Context context, BaseObject baseObject) {
        super(context, baseObject);

    }
    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    boolean getShowTitle() {
        if (showTitle == null)
            showTitle = getContext().getResources().getBoolean(R.bool.gallery_show_title_image_thumbs);
        return showTitle;
    }

    boolean getShowDesc() {
        if (showDesc == null) showDesc = getContext().getResources().getBoolean(R.bool.gallery_show_desc_image_large);
        return showDesc;
    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_large_image, this);
        if (isInEditMode()) return;
        init();
    }

    void init() {
        if (getImage() == null) return;
        if (getShowTitle())
            Text.setText(findViewById(R.id.txt_image_title), getImage().imgtitle, Font.TextPos.h1);
        else view.setVisibility(findViewById(R.id.txt_image_title), false);
        if (getShowDesc())
            Text.setText(findViewById(R.id.txt_image_desc), getImage().imgtext, Font.TextPos.p);
        else view.setVisibility(findViewById(R.id.txt_image_desc), false);
        getImage().setImageToImageView(this, R.id.img_display, R.id.prg_wait, Image.NORMAL, Display.getWidth(getContext()) * 2, Display.getHeight(getContext()) * 2);
    }

    public Image getImage() {
        return (Image) getDbObject();
    }

    public void setImage(Image image) {
        setDbObject(image);
        init();

    }


}
