package ir.microsign.gallery.database;

import android.content.Context;
import ir.microsign.gallery.object.Category;
import ir.microsign.gallery.object.Image;

import ir.microsign.dbhelper.Const;


public class DbHelper extends ir.microsign.dbhelper.database.DbHelper {
    public DbHelper(Context context) {
        super(context, "micro" + "_gallery", 2);
    }

    @Override
    public Object[] getNewObjects() {
        return new Object[]{new Category(), new Image()};
    }

}
