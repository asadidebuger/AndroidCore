package ir.microsign.api.view;

import android.content.Context;

import ir.microsign.api.object.Developer;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.view.ViewHtml;

public class DeveloperView extends BaseView {

    public DeveloperView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_about_us,this,true);
        setView();
    }
    void setView() {
Developer dev=getObject();

        ViewHtml viewHtml=findViewById(R.id.html);
        viewHtml.setContent(dev.getHtml(getContext()));
    }


    public Developer getObject() {
        return (Developer) super.getDbObject();
    }
}
