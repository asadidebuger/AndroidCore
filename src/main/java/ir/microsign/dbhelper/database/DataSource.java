package ir.microsign.dbhelper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Text;

public class DataSource {

    private SQLiteDatabase mdatabase;
    private DbHelper dbHelper;


    public DataSource(DbHelper newDbHelper) {
        dbHelper = newDbHelper;
    }


    //	SQLiteDatabase getDatabase(){
//		return mdatabase;
//	}
    public int getVersion() {
        return getInt("PRAGMA user_version;");
    }

    public Object getRaw(BaseObject object, String func, String fieldName, String where) {

        String query = "SELECT " + func + "(" + fieldName + ") AS val FROM " + object.getTableName();
        if (!Text.isNullOrEmpty(where)) query += " WHERE " + where;
        if (!openDatabase()) return object.fieldIsInt(fieldName) ? 0 : "";
        Cursor cursor = getDatabase().rawQuery(query, null);

        Object value = null;
        if (cursor.moveToFirst()) {
            do {
                if (object.fieldIsInt(fieldName)) {
                    value = cursor.getInt(0);
                    if (value == null) value = 0;
                } else if (object.fieldIsLong(fieldName)) {
                    value = cursor.getLong(0);
                    if (value == null) value = 0;
                } else{
                    value = cursor.getString(0);
                    if (value == null) value = "";
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return value;
    }

    public int getCount(String table, String where) {
        String query = "SELECT COUNT(*) AS val FROM " + table;
        if (!Text.isNullOrEmpty(where)) query += " WHERE " + where;
        if (!openDatabase()) return -1;
        Cursor cursor = getDatabase().rawQuery(query, null);

       int result=0;
        if (cursor.moveToFirst()) {
            result= cursor.getInt(0);
        }
        cursor.close();
        close();
        return result;
    }
    public int getInt(String query) {
        if (!openDatabase()) return -1;
        Cursor cursor = getDatabase().rawQuery(query, null);

       int result=0;
        if (cursor.moveToFirst()) {
            result= cursor.getInt(0);
        }
        cursor.close();
        close();
        return result;
    }
    public boolean isTableEmpty(String table){
        return getCount(table,null)<1;
    }
    public Integer getRawInt(BaseObject object, String func, String fieldName, String where) {
        return (Integer) getRaw(object, func, fieldName, where);
    }

    public String getRawString(BaseObject object, String func, String fieldName, String where) {
        return (String) getRaw(object, func, fieldName, where);
    }

    public Integer getRaw(BaseObject object, String func, String fieldName) {
        return (Integer) getRaw(object, func, fieldName, null);
    }

    public Integer getMax(BaseObject object, String fieldName) {
        return getRaw(object, "MAX", fieldName);
    }

    public Integer getMaxId(BaseObject object) {
        return getRaw(object, "MAX", "id");
    }

    public void autoInsertUpdateById(BaseObject item) {
        autoInsertUpdateByColumn(item, "id");
    }

    public void autoInsertUpdateByColumn(BaseObject item, String fieldName) {
        if (!openDatabase()) return;
        getDatabase().execSQL(item.getInsertStatement(true, fieldName));
        close();
    }

    public void autoInsertUpdateById(List<BaseObject> items) {

        insert(items, 50000, true, "id");

    }   public void autoInsertUpdateById(List<BaseObject> items,int countLimit) {

        insert(items, countLimit, true, "id");

    }

    public void autoInsertUpdateByColumn(List<BaseObject> items, String fieldName) {
        insert(items, 50000, true, fieldName);
    }

    public boolean insert(BaseObject item) {
        item.nullAutoId();
        if (!openDatabase()) return false;
        getDatabase().insert(item.getTableName(), null, item.getContentValue());
        close();
        return true;
    }


    public boolean insert(List<BaseObject> objects) {
        return insert(objects, 50000, false, null);
    }

    public boolean insert(List<BaseObject> objects, int countLimit, boolean conflictReplace, String conflictKey) {
        if (objects == null || objects.size() < 1) return true;
        try {
            if (countLimit <= 0) countLimit = objects.size();
            if (!openDatabase()) return false;
            for (int i = 0, objectsSize = objects.size(); i <= objectsSize/countLimit; i++) {
                getDatabase().beginTransaction();
                for (int j = i * countLimit, limit = Math.min(objectsSize, countLimit * (i+ 1) );j < limit;
                     j++
                        ) {
//                    StringBuilder sql=new StringBuilder();
//                    for (int k=j, klimit= Math.min(objectsSize,j+50);k<klimit;k++,j++) {
                        BaseObject object = objects.get(j);
//                        sql.append(object.getInsertStatement(conflictReplace, conflictKey));//.append(";\n");
//                    }
                    getDatabase().execSQL(object.getInsertStatement(conflictReplace, conflictKey)) ;
                }
//				i*=countLimit;
                getDatabase().setTransactionSuccessful();
                getDatabase().endTransaction();
//                Log.i("DataSource","insert update "+i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            getDatabase().endTransaction();
            return false;
        }
        close();
        return true;
    }

    public boolean update(BaseObject item, String eqColumn) {
        item.nullAutoId();
        if (!openDatabase()) return false;
        getDatabase().update(item.getTableName(), item.getContentValue(), eqColumn + " = " + item.getDbValue(eqColumn), null);
        close();
        return true;
    }

    public boolean update(BaseObject item, String[] eqColumns) {
        StringBuilder where = new StringBuilder();
        for (int i = 0, eqColumnsLength = eqColumns.length; i < eqColumnsLength; i++) {
            String cl = eqColumns[i];
            if (i > 0) where.append(" AND ");
            where.append(eqColumns[i]).append("=").append(item.getDbValue(cl));
        }
        item.nullAutoId();
        if (!openDatabase()) return false;
        getDatabase().update(item.getTableName(), item.getContentValue(), where.toString(), null);
        close();
        return true;
    }


    public boolean update(String table, ContentValues values, String where) {
        if (!openDatabase()) return false;
        getDatabase().update(table, values, where, null);
        close();
        return true;
    }

//    public boolean delete(BaseObject item, String[] eqColumns) {
//        if (item == null) return false;
//        if (!openDatabase()) return false;
//        String where="";
//        for (int i = 0; i < eqColumns.length; i++) {
//            String eqColumn = eqColumns[i];
//            where += eqColumn + " = " + item.getDbValue(eqColumn);
//            if (i<eqColumns.length-1)where+= " AND ";
//        }
//        getDatabase().delete(item.getTableName(), where, null);
//        close();
//        return true;
//    }
    public boolean delete2(BaseObject item, String where) {
        if (item == null) return false;
        if (!openDatabase()) return false;
        getDatabase().delete(item.getTableName(), where, null);
        close();
        return true;
    }
    public boolean clean(String table) {
        if (table == null) return false;
        if (!openDatabase()) return false;
        getDatabase().delete(table,null,null);
        close();
        return true;
    }

    public boolean delete(BaseObject item, String[] eqColumns) {
        StringBuilder where = new StringBuilder();
        for (int i = 0, eqColumnsLength = eqColumns.length; i < eqColumnsLength; i++) {
            String cl = eqColumns[i];
            if (i > 0) where.append(" AND ");
            where.append(eqColumns[i]).append("=").append(item.getDbValue(cl));
        }

        if (item == null) return false;
        if (!openDatabase()) return false;
        getDatabase().delete(item.getTableName(), where.toString(), null);
        close();
        return true;
    }

    public boolean cleanTable(String table,boolean vacuum) {
        if (!openDatabase()) return false;
        try {
            getDatabase().execSQL("DELETE FROM " + table );
            try {
                getDatabase().execSQL("DELETE FROM [sqlite_sequence] WHERE [name]='" + table + "'");
            }catch (Exception e){}
            if (vacuum) getDatabase().execSQL("VACUUM");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
            close();

        return true;
    }

    public boolean execSQL(String query) {
        if (!openDatabase()) return false;
        boolean result = true;
        try {
            getDatabase().execSQL(query);

        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
            close();

        return result;
    }

    public List<?> select(BaseObject item, String where, int limit) {
        boolean isSortable = item.isSortable();
        return select(item, where, isSortable ? item.getOrderColumnName() : null, !item.getReverseOrder(), false, limit);
    }

    public List<?> select(BaseObject item, String where) {

        return select(item, where, 0);
    }
    public List<?> select(BaseObject object,String table, String where, int limit, int offset, String orderBy, Boolean asc, boolean caseSensitive) {
        return select(object,"*",table,false,where,limit,offset,orderBy,asc,caseSensitive);
    }

    public List<?> select(BaseObject object,String columns ,String table,boolean distinct, String where, int limit, int offset, String orderBy, Boolean asc, boolean caseSensitive) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " );
        if (distinct) sb.append("DISTINCT " );
        sb.append(Text.isNullOrEmpty(columns) ? object.getAllColumns() : columns);
        sb.append(" FROM [").append(table).append("] ");
        if (!Text.isNullOrEmpty(where)){
            sb.append(" WHERE(").append(where).append(") ");
        if (!caseSensitive)sb.append(" COLLATE NOCASE ");
        }


        if (!Text.isNullOrEmpty(orderBy)){
            sb.append(" ORDER BY [").append(orderBy).append("]");
            if (asc!=null)sb.append(asc?" ASC ":" DESC ");
        }
        if (limit > 0) sb.append(String.format(Locale.ENGLISH, "LIMIT %d ", limit));
        if (offset > -1) sb.append(String.format(Locale.ENGLISH, "OFFSET %d ", offset));
        return selectByQuery(object, sb.toString());
    }
    public List<?> select(BaseObject object, String where, int limit, int offset, String orderBy, Boolean asc, boolean caseSensitive) {
        return select(object, object.getTableName(), where, limit, offset, orderBy, asc, caseSensitive);
    }
    public List<?> select(BaseObject object, int limit, int offset, String orderBy, Boolean asc, boolean caseSensitive) {
        return select(object,object.getTableName(),object.getWhere(),limit,offset,  orderBy,  asc,  caseSensitive);
    }

    public List<?> select(BaseObject item, String where, String orderBy, Boolean acs, boolean caseSensitive, int limit) {
        List<BaseObject> items = new ArrayList<BaseObject>();
        try {

        if (!openDatabase()) return items;
//        Cursor cursor = getDatabase().query(item.getTableName(),item.getAllColumns(), where, null, null, null, orderBy == null ? null : (orderBy + (caseSensitive ? " " : " COLLATE NOCASE ") + (acs ? "ASC" : "DESC")), limit < 1 ? null : String.format(Locale.ENGLISH, "%d", limit));
        Cursor cursor = getDatabase().query(item.getTableName(),null, where, null, null, null, orderBy == null ? null : (orderBy + (caseSensitive ? " " : " COLLATE NOCASE ") + (acs ? "ASC" : "DESC")), limit < 1 ? null : String.format(Locale.ENGLISH, "%d", limit));

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Object newItem = item.newInstance();
            ((BaseObject) newItem).initFromCursor(cursor);
            items.add(((BaseObject) newItem));
            cursor.moveToNext();
        }
        cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        close();
        return items;
    }
    public Object selectByQueryFirst(BaseObject item, String query) {
        List list=selectByQuery(item,query);
        if (list.size()<1)return null;
        return list.get(0);
    }
    public List<?> selectByQuery(BaseObject item, String query) {
        List<BaseObject> items = new ArrayList<BaseObject>();
        if (!openDatabase()) return items;
        try {
            Cursor cursor = getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Object newItem = item.newInstance();
                ((BaseObject) newItem).initFromCursor(cursor);
                items.add(((BaseObject) newItem));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        close();
        return items;
    }    public List<?> selectByQuery(BaseObject item) {
        return selectByQuery(item,item.getSelectQuery());
    }

    public List<?> select(BaseObject item, String fieldName, List<?> list) {
        if (list.size() < 1) return new ArrayList<Object>();

        if (list.size() == 1){
            if (list.get(0).getClass().equals(String.class))
            return select(item,  fieldName+"='"+ list.get(0)+"'");
            else  return select(item, String.format(Locale.ENGLISH, "%s=%d", fieldName, list.get(0)));
        }
        StringBuilder sb = new StringBuilder();
//		String where="";
        sb.append(fieldName + " IN (");
        for (int i1 = 0; i1 < list.size() - 1; i1++) {
            Object i = list.get(i1);
            sb.append(i.getClass().equals(String.class)?"'"+i+"'," :String.format(Locale.ENGLISH,"%d,",i));

        }
        Object i = list.get(list.size() - 1);
//        sb.append(i.getClass().equals(String.class)?String.format("'%s'",i):i );
        sb.append(i.getClass().equals(String.class)?"'"+i+"'" :String.format(Locale.ENGLISH,"%d",i));

        sb.append(")");
//		if (where.contains(","))where=where.substring(0,where.length()-1);
//		where=fieldName+" IN ("+where+")";
        return select(item, new String(sb));
    }

    public String getInPhrase(List<?> list, String fieldName, String valueField) {
        if (list.size() < 1) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(fieldName + " IN (");
        for (int i1 = 0; i1 < list.size() - 1; i1++) {
//			int i = ;
            sb.append(((BaseObject) list.get(i1)).getDbValue(valueField) + ",");
        }
        sb.append(((BaseObject) list.get(list.size() - 1)).getDbValue(valueField));
        sb.append(")");
//		if (where.contains(","))where=where.substring(0,where.length()-1);
//		where=fieldName+" IN ("+where+")";
        return new String(sb);

    }

    public List<?> select(BaseObject item) {
        return select(item, item.getWhere());
    }
    public <T> T select(Class T) {
        try {
            BaseObject item= (BaseObject) T.newInstance();
            return (T) select(item, item.getWhere());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) new ArrayList<T>();

    }

    public BaseObject selectFirst(BaseObject item, String where) {
        List<?> list = select(item, where, 1);
        return list != null && list.size() > 0 ? (BaseObject) list.get(0) : null;
    }

    public BaseObject selectFirst(BaseObject item) {
        List<?> list = select(item, item.getWhere(), 1);
        return list != null && list.size() > 0 ? (BaseObject) list.get(0) : null;
    }

    public BaseObject selectLast(BaseObject item, String field) {
        List<?> list = select(item, null, field, false, false, 1);
        return list != null && list.size() > 0 ? (BaseObject) list.get(0) : null;
    }

    public BaseObject selectLast(BaseObject item) {
        return selectLast(item, "autoid");
    }

    public boolean openDatabase() {
        if (mdatabase == null || !mdatabase.isOpen()) return open();
        return true;
    }

    private boolean open() {
        try {
            if (dbHelper != null) {
                mdatabase = dbHelper.getWritableDatabase();

                return mdatabase != null;
            }
            if (mdatabase != null) {
                return true;
            }
        } catch (Exception ex) {
            Log.e("datasource",ex.getMessage());
            return false;
        }
        return false;
    }

    public void close() {
        if (dbHelper != null)
            dbHelper.close();
        else {
            if (mdatabase != null && mdatabase.isOpen()) mdatabase.close();
        }
    }

    public SQLiteDatabase getDatabase() {
//		openDatabase();
        if (mdatabase == null) open();
        return mdatabase;
    }

    public Context getContext() {
        return dbHelper.getContext();
    }

} 