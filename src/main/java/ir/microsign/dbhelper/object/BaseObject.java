package ir.microsign.dbhelper.object;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ir.microsign.dbhelper.Const;
import ir.microsign.dbhelper.view.BaseView;

/**
 * Created by Mohammad on 6/15/14.
 */
public class BaseObject implements Comparable<BaseObject>,SimpleObject {
    static HashMap mFieldMap = null;
    public Integer autoid,mPosition;
    public View mView = null;
    public boolean mSelected = false;
    //	public boolean mNeedUpdateView=false;
    String mJsonArrayName = null;
    //	List<String> getAllColumns(Class<?> cls){
////		getClass().getSuperclass()
//		Field[] fields = cls.getDeclaredFields();
//		List<String> fields2 = new ArrayList<String>();
//		Field[] columns = getFieldClass().getDeclaredFields();
//		List<String> columnsName = new ArrayList<String>();
//		for (Field field : columns)
//			columnsName.add(field.getName());
//		for (int i = 0; i < fields.length; i++) {
//			if (columnsName.contains(fields[i].getName())) fields2.add(fields[i].getName());
//		}
//	if (cls.getSuperclass()!=null)	fields2.addAll(getAllColumns(cls.getSuperclass()));
//		return fields2;//.toArray(new String[fields2.size()]);
//	}
    Boolean isSortable = null;
    String[] mColumns = null;
    String mOrderColumn = null;
    boolean mReverseOrder = false;

    public static Field getGlobalField(Class<?> cls, String fieldName) {
        try {

            Field field = cls.getDeclaredField(fieldName);
            return field;
        } catch (NoSuchFieldException e) {
//			Class<?> superClass=
            if (!cls.equals(BaseObject.class)) return getGlobalField(cls.getSuperclass(), fieldName);
        } catch (Exception e) {
            return null;
        }
        return null;
    }
	public static   <T> T fromJsonObject(Class<T> tClass, String jsonObjectString) {
		try {
			return fromJsonObject(tClass,new JSONObject(jsonObjectString));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static   <T> T fromJsonObject(Class<T> tClass, JSONObject jsonObject) {
if (jsonObject==null)return null;
		try {
			BaseObject object= (BaseObject) tClass.newInstance();
			return (T) object.initFormJSONObject(jsonObject);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
return null;
	}

    @Override
    public int compareTo(BaseObject o) {

        BaseObject item = o;
        Object thisOrder = this.getValue(getOrderColumnName()),
                objectOrder = item.getValue(getOrderColumnName());
        if (thisOrder == null && objectOrder == null) return 0;
        if (thisOrder == null) return getReverseOrder() ? 1 : -1;
        if (objectOrder == null) return getReverseOrder() ? -1 : 1;
        if (thisOrder instanceof Integer)
            return ((Integer) (getReverseOrder() ? thisOrder : objectOrder)).compareTo((Integer) (getReverseOrder() ? objectOrder : thisOrder));
        return ((String) (getReverseOrder() ? thisOrder : objectOrder)).compareToIgnoreCase((String) (getReverseOrder() ? objectOrder : thisOrder));

    }

    public Boolean isSortable() {
        if (isSortable != null) return isSortable;
        try {
            if (getOrderColumnName()==null||getOrderColumnName().isEmpty())
                 isSortable = false;
            isSortable = getClass().getField(getOrderColumnName()) != null;

        } catch (Exception ex) {
            isSortable = false;
        }
        return isSortable;
    }

    public String getOrderColumnName() {
        return mOrderColumn;
    }
//public boolean isOrderable()
//{
//    return !Text.isNullOrEmpty(getOrderColumnName());
//}
    public void setOrderColumnName(String columnName) {
        mOrderColumn = columnName;
    }

    public boolean getReverseOrder() {
        return mReverseOrder;
    }

    public void setReverseOrder(boolean reverse) {
        mReverseOrder = reverse;
    }

    public String getTableName() {
        String name = getClass().getSimpleName();
        if (name.substring(name.length() - 1).toLowerCase().equals("y"))
            name = name.substring(0, name.length() - 1) + "ies";
        else name = name + "s";
        return name.toLowerCase();
    }

    public String getJsonArrayName() {
        if (mJsonArrayName == null) return getTableName();
        return mJsonArrayName;
    }

    public BaseObject newInstance() {
        try {
            return getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseObject clone() {
        BaseObject clone = null;
        try {
            clone = getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String[] cols = getAllColumns();
        for (String col : cols)
            clone.setValue(col, getValue(col));
        return clone;
    }

    public List<String> getExceptionFields() {
        return new LinkedList<String>(Arrays.asList("$change", "serialVersionUID"));

//        return new ArrayList<String>();;
//        return new LinkedList<String>();//Arrays.asList(split));

//        return Arrays.asList("$change", "serialVersionUID");
    }

    public String[] getAllColumns() {
        if (mColumns != null) return mColumns;
        if (mFieldMap == null) mFieldMap = new HashMap();
        mColumns = (String[]) mFieldMap.get(getClass().getName());
        if (mColumns != null) return mColumns;
        Field[] fields = getClass().getDeclaredFields();
        List<String> fieldsOut = new ArrayList<String>();
        List<String> exceptionFields =getExceptionFields();
        if (exceptionFields==null)exceptionFields = new ArrayList<>();
        if (exceptionFields.size() < 1) {
            for (Field field : fields) fieldsOut.add(field.getName());
        } else
            for (Field field1 : fields) {
                String field = field1.getName();
                if (!exceptionFields.contains(field)) fieldsOut.add(field);
            }
        mColumns = fieldsOut.toArray(new String[fieldsOut.size()]);
        mFieldMap.put(getClass().getName(), mColumns);
        return mColumns;
    }
//    public String[] getAllColumnsForSelect() {
//        if (mColumns != null) return mColumns;
//        if (mFieldMap == null) mFieldMap = new HashMap();
//        mColumns = (String[]) mFieldMap.get(getClass().getName());
//        if (mColumns != null) return mColumns;
//        Field[] fields = getClass().getDeclaredFields();
//        List<String> fieldsOut = new ArrayList<String>();
//        List<String> exceptionFields =getExceptionFields();
//        if (exceptionFields==null)exceptionFields = new ArrayList<>();
//        if (exceptionFields.size() < 1) {
//            for (Field field : fields) fieldsOut.add(field.getName());
//        } else
//            for (Field field1 : fields) {
//                String field = field1.getName();
//                if (!exceptionFields.contains(field)) fieldsOut.add(field);
//            }
//        mColumns = fieldsOut.toArray(new String[fieldsOut.size()]);
//        mFieldMap.put(getClass().getName(), mColumns);
//        return mColumns;
//    }

    public String getCreateTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE [").append(getTableName()).append("] (");
        String[] allColumns = getAllColumns();
        for (int i = 0; i < allColumns.length; i++) {
            if (i > 0) sb.append(",");
            sb.append("[").append(allColumns[i]).append("] ").append(getSQLFieldType(allColumns[i]).toString().replace("_"," "));
        }
        sb.append(");");
        return sb.toString();
    }

    public String getDropTable() {
        return Const.Command.DropTable + getTableName();
    }

    //	 public String[] getUniqueFields(){
//		 return new String[0];
//	 }
    public Const.fieldType getFieldType(String name) {
//	boolean isUnique=Arrays.asList(getUniqueFields()).contains(name);
        if (name.equals("autoid")) return Const.fieldType.INTEGER_PRIMARY_KEY_AUTOINCREMENT_NOT_NULL;
        return getFieldType(getGlobalField(getClass(), name));
    }

    public Const.fieldType getFieldType(Field field) {
        if (field == null) return Const.fieldType.STRING;
        if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) return Const.fieldType.INT;
        if (field.getType().equals(long.class) || field.getType().equals(Long.class)) return Const.fieldType.LONG;
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) return Const.fieldType.BOOLEAN;
        return Const.fieldType.STRING;
    }

    public Const.fieldType getSQLFieldType(String name) {
        if (name.equals("autoid")) return Const.fieldType.INTEGER_PRIMARY_KEY_AUTOINCREMENT_NOT_NULL;
        return getSQLFieldType(getGlobalField(getClass(), name));
    }

    public Const.fieldType getSQLFieldType(Field field) {
        if (field == null) return Const.fieldType.STRING;
        if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) return Const.fieldType.INT;
        if (field.getType().equals(long.class) || field.getType().equals(Long.class)) return Const.fieldType.LONG;
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) return Const.fieldType.SHORT;
        return Const.fieldType.STRING;
    }

    public void setValue(String column, Object value) {
        try {
            getGlobalField(getClass(), column).set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nullAutoId() {
        autoid = null; //
//        rowid=null;
        // 	setValue(Const.Column.autoid, -1);
    }

    public Object getValue(String column) {
        try {
			if (column.equals("autoid"))return autoid;
            return getGlobalField(getClass(), column).get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDbValue(String column) {
        try {

            Field field = getGlobalField(getClass(), column);
            Object value = field.get(this);
            if (value == null) return "null";

            if (fieldIsInt(column)) return String.valueOf(value);
//            if (fieldIsLong(column)) return String.valueOf(value);
//            if (fieldIsLong(column)) return String.valueOf(value);
            if (fieldIsBool(column)) return ((Boolean)value)==true?"1":"0";
            return "\'" + String.valueOf(value).replace("\'", "\'\'") + "\'";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean fieldIsInt(String fieldName) {
        Const.fieldType type= getFieldType(fieldName);
        return (type== Const.fieldType.INT||type== Const.fieldType.INTEGER_PRIMARY_KEY_AUTOINCREMENT_NOT_NULL);
    }
    public boolean fieldIsBool(String fieldName) {
        Const.fieldType type= getFieldType(fieldName);
        return (type== Const.fieldType.BOOLEAN||type== Const.fieldType.BOOLEAN_UNIQUE);
    }
    public boolean fieldIsLong(String fieldName) {
        return getFieldType(fieldName)== Const.fieldType.LONG;
    }
    public boolean fieldIsString(String fieldName) {
        Const.fieldType type= getFieldType(fieldName);
        return type== Const.fieldType.STRING||type== Const.fieldType.STRING_UNIQUE;
    }
    public boolean fieldIsLong(Field field) {
        return getFieldType(field)== Const.fieldType.LONG||getFieldType(field)== Const.fieldType.LONG_UNIQUE;
    }

    public boolean fieldIsInt(Field field) {
        Const.fieldType type= getFieldType(field);
        return (type== Const.fieldType.INT||type== Const.fieldType.INTEGER_PRIMARY_KEY_AUTOINCREMENT_NOT_NULL);
    }
    public boolean fieldIsBool(Field field) {
        Const.fieldType type= getFieldType(field);
        return (type== Const.fieldType.BOOLEAN||type== Const.fieldType.BOOLEAN_UNIQUE);
    }

    //	public Object initFromHashMap(HashMap<String, String> temp) {
//
//		Field[] fields = getClass().getDeclaredFields();
//		for (int i = 0; i < fields.length; i++) {
//			Field field = fields[i];
//			String fieldName = field.getName();
//			if (!temp.containsKey(fieldName)) continue;
//			String sVal = temp.get(fieldName);
//			if (sVal == null || sVal.length() < 1) continue;
//			if (fieldIsInt(fieldName)) {
//				int val = Integer.parseInt(sVal);
//				try {
//					field.set(this, val);
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			} else if (field.getType().equals(String.class)) {
//				try {
//					field.set(this, sVal);
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return this;
//	}
	public Object initFormJSONObject(JSONObject jsonObject) {
		String[] allColumns = getAllColumns();
		for (int i = 0; i < allColumns.length; i++) {
			String columnName = allColumns[i];
			if (columnName.equals("autoid") || !jsonObject.has(columnName)) continue;
			try {

			    if (fieldIsBool(columnName)) {
			        Object val=jsonObject.opt(columnName);
			        if (val==null)continue;
			        if (val instanceof Boolean)
                    setValue(columnName, val);
			        else if (val instanceof Integer)
                    {
                        setValue(columnName,!val.equals(0));
                    }

                }
			    else {
                    String val = jsonObject.getString(columnName);

                    setValue(columnName, val.equals("null")?null:val);
                }
			} catch (Exception e) {
			    Log.e("initFormJSONObject",columnName);
				e.printStackTrace();
			}
		}
		return this;
	}
	public Object initFormJSON(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            return initFormJSONObject(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return this;
    }
//    public static Object initFormJSONObject(JSONObject jsonObject) {
//        String[] allColumns = getAllColumns();
//        for (int i = 0; i < allColumns.length; i++) {
//            String columnName = allColumns[i];
//            if (columnName.equals("autoid") || !jsonObject.has(columnName)) continue;
//            try {
//                setValue(columnName,jsonObject.getString(columnName));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return this;
//    }
	public JSONObject toJson(){
		JSONObject jsonObject=new JSONObject();
		String[] allColumns = getAllColumns();
		for (String col:allColumns) {
			try {
				jsonObject.put(col, getValue(col));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}
    public Object initFromHashMap(HashMap<String, String> temp) {
        for (Map.Entry<String, String> entry : temp.entrySet()) {
			setValue(entry.getKey(),entry.getValue());
             }
        return this;
    }
	void setValue(String fieldName ,String sVal) {
		Field field = getGlobalField(getClass(), fieldName);
		setValue(field,sVal);
	}
void setValue(Field field ,String sVal){
    if (field == null || sVal==null||sVal.isEmpty()) return;
	if (fieldIsInt(field)) {
		int val = Integer.parseInt(sVal);
		try {
			field.set(this, val);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	} else if (field.getType().equals(String.class)) {
		try {
			field.set(this, sVal);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
    public ContentValues getContentValue() {
        ContentValues values = new ContentValues();
        String[] allColumns = getAllColumns();
        for (int i = 0; i < allColumns.length; i++) {
            String column = allColumns[i];
            Object value = getValue(column);
//			String fieldType=  getFieldType(column);
            if (column.equals("autoid") && value == null) continue;
            if (value == null) {
                values.put(column, "");
                continue;
            }
            if (fieldIsInt(column)) values.put(column, (Integer) value);
            else values.put(column, String.valueOf(value));
        }
        return values;
    }

    public String[] getMapColumns() {
        return getAllColumns();
    }

    public Map<String,String> getMap(boolean addNullFields){
        Map<String,String> map=new HashMap<>();
        String[] allColumns = getAllColumns();
        for (int i = 0; i < allColumns.length; i++) {
            String column = allColumns[i];
            Object value = getValue(column);
//			String fieldType=  getFieldType(column);
            if ("autoid".equals(column) && value == null) continue;
            if (value == null &&addNullFields) {
                map.put(column, "");
                continue;
            }else if (value==null) continue;
            if (fieldIsInt(column))
                map.put(column,value==null?"":String.valueOf(value) );
            else map.put(column, value==null?"":String.valueOf(value));

        }
        return map;
    }
//    public List<NameValuePair> getNameValuePairs() {
//        List<NameValuePair> values = new ArrayList<NameValuePair>();
//        String[] allColumns = getAllColumns();
//        for (int i = 0; i < allColumns.length; i++) {
//            String column = allColumns[i];
//            Object value = getValue(column);
////			String fieldType=  getFieldType(column);
//            if (column.equals("autoid") && value == null) continue;
//            if (value == null) {
//                values.add(new BasicNameValuePair(column, ""));
//                continue;
//            }
//            if (fieldIsInt(column))
//                values.add(new BasicNameValuePair(column, String.format(Locale.ENGLISH, "%d", value)));
//            else values.add(new BasicNameValuePair(column, String.valueOf(value)));
//
//        }
//        return values;
//    }

    public Object initFromCursor(Cursor cursor) {
        String[] allColumns = getAllColumns();
        for (int i = 0; i < allColumns.length; i++) {
            String columnName = allColumns[i];
            try {
                Field field = getGlobalField(getClass(), columnName);// getClass().getDeclaredField(columnName);
                if (field == null) continue;
                Object value = null;
                int index=cursor.getColumnIndex(columnName);
                if (index<0)continue;
                if (fieldIsInt(field))//cursor.getString(index)
                    value = cursor.getInt(index);
                else if (fieldIsLong(field)) value = cursor.getLong(index);
                else if (fieldIsBool(field)) value = cursor.getShort(index) >= 1;// cursor.getType(index);
                else  value = cursor.getString(index);
                field.set(this, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
//            *   <li>{@link #FIELD_TYPE_NULL}</li>
//            *   <li>{@link #FIELD_TYPE_INTEGER}</li>
//            *   <li>{@link #FIELD_TYPE_FLOAT}</li>
//            *   <li>{@link #FIELD_TYPE_STRING}</li>
//            *   <li>{@link #FIELD_TYPE_BLOB}</li>
    public Object initFromObject(BaseObject object) {
        String[] allColumns = getAllColumns();
        for (int i = 0; i < allColumns.length; i++) {
            String columnName = allColumns[i];
            try {
                Field field = getGlobalField(getClass(), columnName);// getClass().getDeclaredField(columnName);
                Object value = object.getValue(columnName);
//				if (fieldIsInt(columnName))
//					value = cursor.getInt(cursor.getColumnIndex(columnName));
//				else value = cursor.getString(cursor.getColumnIndex(columnName));
                field.set(this, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }


    public static Map<String,String> mAllFieldsList=null;
public  String getAllFieldString(){
    if (mAllFieldsList==null)mAllFieldsList=new HashMap<>();
    String result=mAllFieldsList.get(getTableName());
    if (result!=null)return result;//mAllFieldsList.put(getTableName(),)
    String[] cols = getAllColumns();
    StringBuilder sql = new StringBuilder();
    for (int i = 0, colsLength = cols.length; i < colsLength; i++) {
        if (i > 0) sql.append(",");
        sql.append("[").append(cols[i]).append("]");
    }
    String s=sql.toString();
    mAllFieldsList.put(getTableName(),s);
    return s;

}
    public String getInsertStatement(boolean conflictReplace, String conflictKey) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT ");
        if (conflictReplace) sql.append("OR REPLACE ");
        sql.append("INTO ").append(getTableName()).append('(');
        String[] cols = getAllColumns();
        sql.append(getAllFieldString());
//        for (int i = 0, colsLength = cols.length; i < colsLength; i++) {
//            if (i > 0) sql.append(",");
//            sql.append(cols[i]);
//        }
        sql.append(')');
        sql.append(" VALUES (");
        for (int i = 0, colsLength = cols.length; i < colsLength; i++) {
            String col = cols[i];
            if (i > 0) sql.append(",");
            if (conflictReplace && col.equals("autoid")) {
                sql.append("(SELECT [").append(col).append("] FROM [").append(getTableName()).append("] WHERE ").append("[").append(conflictKey).append("]=").append(getDbValue(conflictKey)).append(")");
            } else
                sql.append(getDbValue(col));
        }
        sql.append(')');
        return sql.toString();

    }
public  String getSelectQuery(){
    String q= "SELECT * FROM ["+getTableName()+"]";
    String w=getWhere();
    if (w==null||w.isEmpty())return q;
    return q+" WHERE "+w;
}
    public String getWhere() {
        String[] allColumns = getAllColumns();
        StringBuilder where =new StringBuilder(allColumns.length*10);
        for (int i = 0; i < allColumns.length; i++) {
            String columnName = allColumns[i];
            try {
                Field field = getGlobalField(getClass(), columnName);
                Object value = null;
                value = field.get(this);
                if (value == null) continue;
                String sValue = "";
                if (fieldIsInt(columnName)){
                    if (fieldIsBool(field))
                        sValue= ((boolean) value) ?"1":"0";
                    else sValue = String.valueOf(value);
                }
                else sValue = "\'" + String.valueOf(value) + "\'";
                if (where.length() >0 )where.append(" AND ");
                where.append("[").append(columnName).append("]=").append(sValue);

//                += (? "" : Const.Command.AND) + columnName + Const.Separator.EQ + sValue;
            } catch (Exception e) {
            }
        }
        return where.length() < 1 ? null : where.toString();
    }

//    public String getViewPath(Context context) {
//
//        return "net.tarnian.aboutus.view." + getClass().getSimpleName() + "View";
//    }
    public String getViewPath(Context context) {

        return  context.getPackageName()+".view." + getClass().getSimpleName() + "View";
    }

    public String getOverrideViewPath(Context context) {
        String viewpath = getViewPath(context);
        int id = context.getResources().getIdentifier(viewpath, "string", context.getPackageName());
        if (id > 0) return context.getString(id);

        return viewpath;
    }


    public Class<?> getBaseClass() {
        return BaseObject.class;
    }

    public void UpdateView() {
        if (mView == null) return;
        if (mView instanceof BaseView)
            ((BaseView) mView).update();
    }

    public View getView(Context context) {
        if (mView != null) return mView;
        if (getViewClass()==null) {
            String clsName = getOverrideViewPath(context);
            try {
                Class<?> cls = Class.forName(clsName);
                Constructor<?>[] constructors = cls.getConstructors();
                for (Constructor<?> ctor : constructors) {
                    Class<?>[] paramTypes = ctor.getParameterTypes();

                    if (paramTypes.length != 2 || !classIsOk(paramTypes[1], getBaseClass()))
                        continue;
                    Object[] convertedArgs = new Object[]{context, this};
                    mView = (View) ctor.newInstance(convertedArgs);
                    return mView;
                }
            } catch (Exception e) {
                Log.e("BaseObject", clsName + e.getMessage());
            }
            Log.e("BaseObject", clsName + ":View is null");
            return null;
        }
        else {
            try {
                Class<?> cls =getViewClass();// Class.forName(clsName);
                Constructor<?>[] constructors = cls.getConstructors();
                for (Constructor<?> ctor : constructors) {
                    Class<?>[] paramTypes = ctor.getParameterTypes();

                    if (paramTypes.length != 2 || !classIsOk(paramTypes[1], getBaseClass()))
                        continue;
                    Object[] convertedArgs = new Object[]{context, this};
                    mView = (View) ctor.newInstance(convertedArgs);
                    return mView;
                }
            } catch (Exception e) {
                Log.e("BaseObject", getClass() + e.getMessage());
            }
            Log.e("BaseObject", getClass()  + ":View is null");
            return null;
        }
    }
    public Class getViewClass(){
    return null;
    }



    boolean classIsOk(Class<?> baseClass, Class<?> target) {
        if (target == null || target.equals(Object.class)) return false;
        if (baseClass.equals(target)) return true;
        return classIsOk(baseClass, target.getSuperclass());
    }
   public boolean filter(String text,boolean startWith,boolean endWith,boolean noCase) {

       if (text==null||text.isEmpty()) return true;
       String src = noCase ? toString().toLowerCase().replaceAll("  +", " ") : toString().replaceAll("  +", " "), filter = text.replaceAll("  +", " ");
       if (startWith && endWith) return src.equals(filter);
       if (!startWith && !endWith) return src.contains(filter);
       if (startWith) return src.startsWith(filter);
       return src.endsWith(filter);
   }
//	public static Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> clazz) {
//		List<Class<?>> res = new ArrayList<>();
//
//		do {
//			res.add(clazz);
//
//			// First, add all the interfaces implemented by this class
//			Class<?>[] interfaces = clazz.getInterfaces();
//			if (interfaces.length > 0) {
//				res.addAll(Arrays.asList(interfaces));
//
//				for (Class<?> interfaze : interfaces) {
//					res.addAll(getAllExtendedOrImplementedTypesRecursively(interfaze));
//				}
//			}
//
//			// Add the super class
//			Class<?> superClass = clazz.getSuperclass();
//
//			// Interfaces does not have java,lang.Object as superclass, they have null, so break the cycle and return
//			if (superClass == null) {
//				break;
//			}
//
//			// Now inspect the superclass
//			clazz = superClass;
//		} while (!"java.lang.Object".equals(clazz.getCanonicalName()));
//
//		return new HashSet<Class<?>>(res);
//	}
}
