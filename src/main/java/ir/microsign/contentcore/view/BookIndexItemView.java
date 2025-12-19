package ir.microsign.contentcore.view;

import android.content.Context;
import android.util.AttributeSet;

import ir.microsign.R;
import ir.microsign.contentcore.object.BaseObject;
import ir.microsign.contentcore.object.BookIndexItem;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/24/14.
 */
public class BookIndexItemView extends BaseView {

//	net.tarnian.content.object.Content mContent = null;

    public BookIndexItemView(Context context) {
        super(context);
    }

    public BookIndexItemView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    public BookIndexItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_dic_item, this);
        Text.setText(this, R.id.txt_title, getItem().title, Font.TextPos.h1);

        findViewById(R.id.ll_root2).setBackgroundResource(getItem().mPosition % 2 == 0 ? R.drawable.back_selectable_category_odd : R.drawable.back_selectable_category_even);
        setSelected(getItem().mSelected, true);
    }

    public void setSelected(boolean selected, boolean valid) {

        getItem().mSelected = selected;
        view.setVisibility(findViewById(R.id.img_indent), selected);
        findViewById(R.id.ll_root2).setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return getItem().mSelected;
    }

    @Override
    public void setSelected(boolean selected) {
    }

    public BookIndexItem getItem() {
        return (BookIndexItem) getDbObject();
    }


}
