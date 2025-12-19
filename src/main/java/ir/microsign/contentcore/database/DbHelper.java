package ir.microsign.contentcore.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ir.microsign.contentcore.object.Content;
import ir.microsign.dbhelper.Const;

public class DbHelper extends ir.microsign.dbhelper.database.DbHelper {
    public DbHelper(Context context) {
        super(context, Const.Database.getDatabaseName(context) + Content.getHelper(context).getDbSuffix(), Content.getHelper(context).getDbVersion());
    }

//    public DbHelper(Context activity, String dbName, int dbVersion) {
//
//
//        super(activity, dbName, dbVersion);
//    }

    @Override
    public Object[] getNewObjects() {
        return  Content.getHelper(getContext()).getDBNewObjects();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Content.getHelper().onDbUpgrade( db,  oldVersion, newVersion);
    }
}
