package ir.microsign.gallery.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.LinearLayout;

import ir.microsign.utility.File;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.R;

/**
 * Created by Mohammad on 6/24/14.
 */
public class LargeImagePathView extends LinearLayout {
    public String mPath = null;

    //	public LargeImagePathView(Context context) {
//		super(context);
//	}
    public LargeImagePathView(Context context, String path) {
        super(context);
        mPath = path;
        view.getLayoutInflater(getContext()).inflate(R.layout.layout_touchable_large_image, this);
        init();

    }

    void init() {
        if (Text.isNullOrEmpty(mPath) || !File.Exist(mPath)) return;
        android.widget.ImageView imageView = (android.widget.ImageView) findViewById(R.id.img_display);
        imageView.setImageBitmap(BitmapFactory.decodeFile(mPath));
        view.setVisibility(findViewById(R.id.prg_wait), false);
//		getImage().setImageToImageView(this,R.id.img_display,R.id.prg_wait,Image.NORMAL,Display.getWidth()*2,Display.getHeight()*2);
    }


}
