package ir.microsign.api.object;

import android.content.Context;

import ir.microsign.api.Utils;
import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.view.TicketView;
import ir.microsign.dbhelper.Const;

public class Ticket extends ir.microsign.dbhelper.object.BaseObject {
    public String _id,title,date,type;
    public Long last_pdate;

public Boolean fromUser;

//    @Override
//    public String[] getMapColumns() {
//        return new String[]
//    }

    //    @Override
//    public String[] getAllColumns() {
//        return super.getAllColumns();
//    }



    @Override
    public Class getViewClass() {
        return TicketView.class;
    }
    @Override
    public Const.fieldType getSQLFieldType(String name) {
        if (name.equals("_id")) return Const.fieldType.STRING_UNIQUE;
        return super.getFieldType(name);
    }
    public String getDate() {
        return Utils.getDate(date);
    }

    @Override
    public String getOrderColumnName() {
        return "date";
    }

    @Override
    public boolean getReverseOrder() {
        return true;
    }
    public  int countMessages(Context context){
        return DataSourceApi.getSource(context).getCount(new Message().getTableName(),"ticket=\'"+_id+"\'");
    }
}
