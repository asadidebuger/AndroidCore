package ir.microsign.contentcore.view;

import android.content.Context;
import android.widget.TextView;

import ir.microsign.R;
import ir.microsign.contentcore.object.Category;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/24/14.
 */
public class CategoryView extends BaseView {
    public CategoryView(Context context) {
        super(context);
    }

    public CategoryView(Context context, ir.microsign.contentcore.object.BaseObject baseObject) {
        super(context, baseObject);
    }


    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        setView();

    }

    public void setView() {
        getLayoutInflater().inflate(R.layout.layout_item_cat, this);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        Text.setText(txtTitle, getItem().title, Font.TextPos.h1);
        setSelected(getItem().mSelected, true);
    }

    //
//	}
    public Category getItem() {
        return (Category) getDbObject();
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
