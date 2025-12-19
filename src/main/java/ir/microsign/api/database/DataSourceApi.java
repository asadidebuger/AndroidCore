package ir.microsign.api.database;

import android.content.Context;

import java.util.List;

import ir.microsign.api.object.App;
import ir.microsign.api.object.Message;
import ir.microsign.api.object.Profile;
import ir.microsign.api.object.Recover;
import ir.microsign.api.object.Ticket;
import ir.microsign.dbhelper.object.BaseObject;

//import net.tarnian.content.object.DbObject;


public class DataSourceApi extends ir.microsign.dbhelper.database.DataSource {


    static DataSourceApi dataSource=null;
    public static DataSourceApi getSource(Context context){
        if (dataSource==null)dataSource=new DataSourceApi(context);
        return dataSource;
    }
    public DataSourceApi(Context context) {
        super(new DbHelperApi(context));
    }

//    public List<User> getUsers(String where) {
//
//        return (List<User>) select(new User(), where);
//    }

    public Profile getProfile() {
        return (Profile) selectFirst(new Profile());
    }
    public boolean insertProfile(Profile profile) {
        cleanTable(profile.getTableName(),true);
        Profile.avatar=null;
        return insert(profile);
    }
    public boolean insertRecover(Recover recover) {
        cleanTable(recover.getTableName(),true);
        return insert(recover);
    }
    public Recover getRecover() {
       return (Recover) selectFirst(new Recover());
    }
    public boolean cleanAndInsertObjects(List<BaseObject> baseObjects) {
        if (baseObjects.size()<1)return false;
        cleanTable(baseObjects.get(0).getTableName(),true);
        return insert(baseObjects);
    }
    public boolean cleanAndInsertObjects(String table,List<BaseObject> baseObjects) {
//        if (baseObjects.size()<1)return false;
        cleanTable(table,true);
        return insert(baseObjects);
    }
    public boolean insertMessages(List<BaseObject> baseObjects,String ticketId) {
        delete2(new Message(),"[ticket]=\'"+ticketId+"\'");
        if (baseObjects.size()<1)return false;
        return insert(baseObjects);
//        cleanTable(baseObjects.get(0).getTableName(),true);
//        return insert(baseObjects);
    }
    public boolean cleanAndInsertObject(BaseObject baseObjects) {
        if (baseObjects==null)return false;
        cleanTable(baseObjects.getTableName(),true);
        return insert(baseObjects);
    }
//    public boolean updateProfile(Profile profile){
//        if (profile.getUserNameType())profile.email="";else profile.tel="";
////        select(profile,"tel='+98 9132179141' AND email=''")
//        return update(profile,new String[]{"tel","email"});
//    }


    public List<?> getTickets() {
        return super.select(new Ticket());
    }
    public List<?> getMessages(String ticketId) {
        Message message=new Message();
        message.ticket=ticketId;
        return super.select(message);
    }
    public App getApp(){
        return getApp(getContext().getPackageName());
    } public App getApp(String packageName){
        App app=new App();
        app.pkg=packageName;
        return (App) selectFirst(app);
    }
    public List<BaseObject> getApps(){

//        app.pkg=getContext().getPackageName();
        return (List<BaseObject>) select(new App(),"pkg IS NOT \'"+getContext().getPackageName()+"\'");
    }
}