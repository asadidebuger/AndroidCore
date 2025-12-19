package ir.microsign.content.view;

import android.content.Context;
import android.util.AttributeSet;

import ir.microsign.R;
import ir.microsign.content.object.Content;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ContentSearchView extends BaseView {

//	Content mContent = null;

    public ContentSearchView(Context context) {
        super(context);
    }

    public ContentSearchView(Context context, ir.microsign.contentcore.object.BaseObject baseObject) {
        super(context, baseObject);
    }

    public ContentSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_item_content_search, this);

        mBaseObject = baseObject == null ? new Content() : baseObject;

        try {
            Text.setText(findViewById(R.id.txt_titr), getContent().title, Font.TextPos.h1);
        } catch (NoSuchFieldError error) {
            error.printStackTrace();
        }
        try {
//            Text.setText(findViewById(R._id.txt_content_path), getItem().getPath(getContext()), Font.TextPos.p);
        } catch (NoSuchFieldError error) {
            error.printStackTrace();
        }


    }

    public Content getContent() {
        return (Content) getDbObject();
    }


}
