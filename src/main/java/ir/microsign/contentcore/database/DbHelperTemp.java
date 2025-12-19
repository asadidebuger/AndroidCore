package ir.microsign.contentcore.database;

import android.content.Context;

import ir.microsign.contentcore.object.Temp;
import ir.microsign.contentcore.object.Update;
import ir.microsign.dbhelper.Const;

public class DbHelperTemp extends ir.microsign.dbhelper.database.DbHelper {
    public DbHelperTemp(Context context) {
        super(context, Const.Database.getDatabaseName(context) +"_temp",3);
    }

//    public DbHelperTemp(Context activity, String dbName, int dbVersion) {
//        super(activity, dbName, dbVersion);
//    }

    @Override
    public Object[] getNewObjects() {
        return new Object[]{ new Temp(),new Update()};
    }
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Content.getHelper().onDbUpgrade( db,  oldVersion, newVersion);
//    }
}
