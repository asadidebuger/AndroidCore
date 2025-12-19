package ir.microsign.contentcore.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import ir.microsign.R;
import ir.microsign.contentcore.object.CategorySimple;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/24/14.
 */
public class CategorySimpleView extends BaseView {
    public CategorySimpleView(Context context) {
        super(context);
    }

    public CategorySimpleView(Context context, ir.microsign.contentcore.object.BaseObject baseObject) {
        super(context, baseObject);
    }


    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        setView();

    }

    public void setView() {
        getLayoutInflater().inflate(R.layout.layout_item_cat_large, this);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        TextView txtDesc = (TextView) findViewById(R.id.txt_desc);
        Text.setText(txtTitle, getItem().title, Font.TextPos.h1,1.2f,false);
        Text.setText(txtDesc, getItem().metadesc, Font.TextPos.p, .9f, true);
//        setSelected(getItem().mSelected, true);


        if (getItem().getFirstImage(true) == null) {

            view.setVisibility(findViewById(R.id.rl_image), false);
            //((ImageView) findViewById(R._id.img_image)).setImageResource(R.drawable.image_break);
            return;
        }
        if ( getItem().getFirstImage(true)!=null)
        Image.setImage((ImageView) findViewById(R.id.img_image), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.image_break),findViewById(R.id.prg_wait), getItem().getFirstImage(false));
//        getItem().getFirstImage(true).setView(true, this, R.id.img_image, R.id.prg_wait, Image.THUMB, 400, 400, R.drawable.image_break, true);
    }

    //
//	}
    public CategorySimple getItem() {
        return (CategorySimple) getDbObject();
    }

    public void setSelected(boolean selected, boolean valid) {

        getItem().mSelected = selected;
        findViewById(R.id.txt_title).setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return getItem().mSelected;
    }

    //
//
    @Override
    public void setSelected(boolean selected) {
    }

}
