package ir.microsign.contentcore.view;

import android.content.Context;
import android.widget.TextView;

import ir.microsign.R;
import ir.microsign.contentcore.object.Suggest;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

//import net.tarnian.content.object.Category;

//import net.tarnian.content.object.BaseObject;

/**
 * Created by Mohammad on 6/24/14.
 */
public class SuggestView extends BaseView {
//	Category mCategory = null;

//    static DataSource mDataSource = null;

    public SuggestView(Context context) {
        super(context);
    }
//
//	public CategoryView(Context activity, AttributeSet attrs) {
//		super(activity, attrs);
//	}

    public SuggestView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_search_sugest, this);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        Text.setText(txtTitle, getItem().title, Font.TextPos.h1);
    }



    public Suggest getItem() {
        return (Suggest) getDbObject();
    }

}
