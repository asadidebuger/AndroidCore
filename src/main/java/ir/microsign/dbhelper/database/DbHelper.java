package ir.microsign.dbhelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ir.microsign.dbhelper.object.BaseObject;

//import net.tarnian.content.Const;

public class DbHelper extends SQLiteOpenHelper {
    public static Object[] mDbObjects = null;

    //	private static final int DATABASE_VERSION = 1;
//	 public int getDatabaseVersion(){
//		 return  DATABASE_VERSION;
//
//	 }
    Context mContext = null;

    public DbHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        mContext = context;
        mDbObjects = getNewObjects();// this.getWritableDatabase()
    }

    public Context getContext() {
        return mContext;
    }

    public Object[] getNewObjects() {
        return null;
    }

    public Object[] getObjects() {
        if (mDbObjects == null) mDbObjects = getNewObjects();
        return mDbObjects;
    }

//    public String getRootPath() {
//        return null;
//    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            for (Object o : getObjects())
                database.execSQL(((BaseObject) o).getCreateTable());
        } catch (Exception ex) {
            ex.printStackTrace();
//            view.ShowMessage(getContext(),ex.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion <= oldVersion) return;
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        for (Object o : getObjects()) {
            try {
                db.execSQL(((BaseObject) o).getDropTable());
                db.execSQL(((BaseObject) o).getCreateTable());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
