package ir.microsign.settings.object;

/**
 * Created by Mohammad on 10/5/14.
 */
public class MetaObject {
    Object mObject = null;
    String mTitle = null, mDescription = null;

    public MetaObject(Object o, String title, String description) {
        mObject = o;
        mTitle = title;
        mDescription = description;
    }

    public Object getObject() {
        return mObject;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }
}
