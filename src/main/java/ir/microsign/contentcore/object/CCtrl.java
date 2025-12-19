package ir.microsign.contentcore.object;

import ir.microsign.R;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.context.Application;
import ir.microsign.dbhelper.Const;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/15/14.
 */
public class CCtrl extends BaseObject {
    public static DataSource mDataSource = null;

    public boolean languageIs(String lang){
        if (lang==null) return this.lang ==null;
        return lang.equals(this.lang);
    }
    final String[] mCols = new String[]{"_id", "key",  "file", "lang","cat", "access","create","version", "changes","description","sizeZip","sizeContent"};
    @Override
    public Const.fieldType getSQLFieldType(String name) {
        if (name.equals("_id")) return Const.fieldType.STRING_UNIQUE;
        return super.getFieldType(name);
    }
    public String lang,_id, cat,access, create, file, key, title,description;
    public Integer  version,sizeContent,sizeZip ;
    public String getUrl(){
        return file;
    }
    public String getExtractPath(){
        return Application.getContext().getDatabasePath("microsign").getParent()+"/";
    }
    public String getDescription(){
        return String.format(Text.ReadResTxt(Application.getContext(),R.raw.fullupdatechangesformat),description!=null?description:"",sizeZip/(1024*1024f),sizeContent/(1024*1024f));
    }



}
