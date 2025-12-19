package ir.microsign.gallery.view;

import android.content.Context;

import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.gallery.object.BaseObject;
import ir.microsign.gallery.object.Image;

import ir.microsign.R;
import ir.microsign.gallery.activity.GalleryImagesActivity;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ImageView extends BaseView {
    static Boolean showTitle = null;

    public ImageView(Context context) {
        super(context);
    }

    public ImageView(Context context, BaseObject baseObject) {
        super(context, baseObject);

    }

    boolean getShowTitle() {
        if (showTitle == null)
            showTitle = getContext().getResources().getBoolean(R.bool.gallery_show_title_image_thumbs);
        return showTitle;
    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_image_item, this);
        setLayoutParams(R.id.ll_root, GalleryImagesActivity.columnWidth, GalleryImagesActivity.columnWidth);
        if (getShowTitle())
            Text.setText(findViewById(R.id.txt_image_title), getImage().imgtitle, Font.TextPos.h2);
        else view.setVisibility(findViewById(R.id.txt_image_title), false);
        getImage().setImageToImageView(this, R.id.img_image, R.id.prg_wait, Image.THUMB, GalleryImagesActivity.columnWidth, GalleryImagesActivity.columnWidth);
    }

    Image getImage() {
        return (Image) getDbObject();
    }


}
