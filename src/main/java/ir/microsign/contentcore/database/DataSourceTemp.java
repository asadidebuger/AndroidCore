package ir.microsign.contentcore.database;

import android.content.Context;

import ir.microsign.contentcore.object.Category;

/**
 * Created by Mohammad on 6/25/14.
 */
public class DataSourceTemp extends ir.microsign.dbhelper.database.DataSource {
    Category mRoot = null;
   static DataSourceTemp mDataSource=null;
    public static DataSourceTemp getDataSource(Context context){
        if (mDataSource==null)mDataSource=new DataSourceTemp(context);
        return mDataSource;

    }
    public DataSourceTemp(Context context) {
        super(new DbHelperTemp(context));
    }

}
