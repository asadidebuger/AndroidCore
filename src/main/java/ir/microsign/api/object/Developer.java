package ir.microsign.api.object;

import android.content.Context;

import ir.microsign.api.view.DeveloperView;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.R;
import ir.microsign.utility.Text;

public class Developer extends BaseObject {
    public String name="",web="",email="",tell="",description="";

    @Override
    public Class getViewClass() {
        return DeveloperView.class;
    }
    public String getHtml(Context context){
        String html=  Text.ReadResTxt(context, R.raw.developer);
        html= html.replace("__tell",tell).replace("__email",email).replace("__website",web).replace("__developer",name).replace("__description",description);
      return html;
    }
}
