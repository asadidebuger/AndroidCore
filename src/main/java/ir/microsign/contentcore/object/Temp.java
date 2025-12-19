package ir.microsign.contentcore.object;

/**
 * Created by Mohammad on 6/14/14.
 */
public class Temp extends BaseObject {
    public String value,tablename;
    public Integer autoid;
    public static Temp fromObject(BaseObject object){
        Temp temp=new Temp();

        temp.value=object.toJson().toString();
        temp.tablename=object.getTableName();
        return temp;
    }
    public  <T> T  toObject(Class<T> tClass){
        return BaseObject.fromJsonObject(tClass,value);
    }
}

