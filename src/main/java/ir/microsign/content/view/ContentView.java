package ir.microsign.content.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import ir.microsign.R;
import ir.microsign.content.object.Content;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ContentView extends BaseView {

//	net.tarnian.content.object.Content mContent = null;

    public ContentView(Context context) {
        super(context);
    }

    public ContentView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_item_content, this);
        try {
            Text.setText(this, R.id.txt_titr, getContent().title, Font.TextPos.h1);
        } catch (NoSuchFieldError e) {
        }

        try {
            Text.setText(this, R.id.txt_date, getResources().getBoolean(R.bool.show_content_date) ? getContent().getDate(getContext()) : null, Font.TextPos.small);
        } catch (NoSuchFieldError e) {
        }
        try {
            Text.setText(this, R.id.txt_short_content, getResources().getBoolean(R.bool.show_content_intro_text) ? getContent().getIntroText() : null, Font.TextPos.p);
        } catch (NoSuchFieldError e) {
        }
        findViewById(R.id.ll_root2).setBackgroundResource(getContent().mPosition % 2 == 0 ? R.drawable.back_selectable_content_even : R.drawable.back_selectable_content_odd);

        setImage();

    }

    void setImage() {
        Image thumb = getContent().getThumbImage();
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

    public Content getContent() {
        return (Content) getDbObject();
    }



}
