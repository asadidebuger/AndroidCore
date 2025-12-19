package ir.microsign.api.view;

import android.content.Context;

import ir.microsign.api.object.App;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.view.ViewHtml;

public class LargeAppView extends BaseView {

    public LargeAppView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_about_us,this,true);
        setView();
    }
    void setView() {
        App app=getObject();

        ViewHtml viewHtml=findViewById(R.id.html);
        viewHtml.setContent(app.getHtml(getContext()));
    }


    public App getObject() {
        return (App) super.getDbObject();
    }
}
