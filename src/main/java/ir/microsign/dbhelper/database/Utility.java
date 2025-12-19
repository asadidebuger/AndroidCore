package ir.microsign.dbhelper.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.io.File;

/**
 * Created by Mohammad on 03/01/2015.
 */
public class Utility {
    public static Context getExternalDbContext(Context context, final String root) {
        ContextWrapper contextWrapper = new ContextWrapper(context) {
            @Override
            public File getDatabasePath(String name) {
                ir.microsign.utility.File.AppendDir(root);
                return super.getDatabasePath(root + "/" + name);
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                ir.microsign.utility.File.AppendDir(root);
                return SQLiteDatabase.openDatabase(getDatabasePath(name).getPath(), factory,
                        SQLiteDatabase.CREATE_IF_NECESSARY);
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                ir.microsign.utility.File.AppendDir(root);
                try {
//					File file=new File(getDatabasePath(name).getPath());
//					if (!file.exists()||file.canRead())
                    return SQLiteDatabase.openDatabase(getDatabasePath(name).getPath(), factory, SQLiteDatabase.CREATE_IF_NECESSARY, errorHandler);
//					return null;
                } catch (Exception ex) {
                    Log.e("n.t.dbhel.datab.Utility", ex.getMessage());
                    return null;
                }
            }
        };
        return contextWrapper;
    }
}
