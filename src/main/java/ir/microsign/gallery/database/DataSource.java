package ir.microsign.gallery.database;

import android.content.Context;

//import net.tarnian.content.object.DbObject;

public class DataSource extends ir.microsign.dbhelper.database.DataSource {


    public DataSource(Context context) {
        super(new DbHelper(context));
    }


} 