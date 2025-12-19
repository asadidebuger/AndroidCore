package ir.microsign.api.database;

import android.content.Context;

import ir.microsign.api.object.App;
import ir.microsign.api.object.Message;
import ir.microsign.api.object.Profile;
import ir.microsign.api.object.Recover;
import ir.microsign.api.object.Ticket;
import ir.microsign.dbhelper.Const;

public class DbHelperApi extends ir.microsign.dbhelper.database.DbHelper {
    public DbHelperApi(Context context) {
        super(context, Const.Database.getDatabaseName(context) + "_micro_api", 29);
    }

    @Override
    public Object[] getNewObjects() {
        return new Object[]{new Profile(),new Ticket(),new Message(),new App(),new Recover()};
    }

}
