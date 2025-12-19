package ir.microsign.settings.object;

//import net.tarnian.content.Const;

import android.content.Context;

/**
 * Created by Mohammad on 6/15/14.
 */
public abstract class BaseObject extends ir.microsign.dbhelper.object.BaseObject {
    public BaseObject newInstance() {
        try {
            return getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getViewPath(Context context) {
        return "net.tarnian.settings.view." + getClass().getSimpleName() + "View";
    }

    @Override
    public Class<?> getBaseClass() {
        return BaseObject.class;
    }
}
