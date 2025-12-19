package ir.microsign.content.view;

import android.content.Context;
import android.widget.ImageView;

import ir.microsign.R;
import ir.microsign.content.object.ContentLogo;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ContentLogoView extends BaseView {
    public ContentLogoView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_content_logo, this);
//		Text.setText(findViewById(R._id.txt_title), mContentLogo.title, mContentLogo.mFont1,true);
        Text.setText(findViewById(R.id.txt_description1), getContentLogo().mDesc1, getContentLogo().mFont2, true);
        ((ImageView) findViewById(R.id.img_icon)).setImageDrawable(getContentLogo().mIcon);

        setLayoutParams(R.id.ll_root, -1, -2);

    }

    ContentLogo getContentLogo() {
        return (ContentLogo) getDbObject();
    }
}
