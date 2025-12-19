package ir.microsign.dbhelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/24/14.
 */
public class BaseView extends LinearLayout {
    public BaseObject mBaseObject ;
//	LayoutInflater mLayoutInflater = null;

    public BaseView(Context context) {
        super(context);
        initFromBaseObject(null);
    }

    public BaseView(Context context, BaseObject baseObject) {
        super(context);
        initFromBaseObject(baseObject);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromBaseObject(null);
    }

    public LayoutInflater getLayoutInflater() {
        return view.getLayoutInflater(getContext());
    }

    public void initFromBaseObject(BaseObject baseObject) {
        mBaseObject = baseObject;
    }

    public BaseObject getDbObject() {
        return mBaseObject;
    }

    public void setDbObject(BaseObject baseObject) {
        mBaseObject = baseObject;
    }

    public void setLayoutParams(int viewId, int w, int h) {
        View root = findViewById(viewId);
       setLayoutParams(root,w,h);
    }
    public void setLayoutParams(View root, int w, int h) {
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new ViewGroup.LayoutParams(w, h);
        layoutParams.width = w;
        layoutParams.height = h;
        root.setLayoutParams(layoutParams);
    }

    public void  update() {

    }
    public void setSelected(boolean selected, boolean valid) {
    }
    public void setLayoutParams(int viewId) {
        setLayoutParams(viewId, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public String getString(int id){
        return getContext().getString(id);
    }

//    Resources getResources(){
//        return getContext().getResources();
//    }

}
