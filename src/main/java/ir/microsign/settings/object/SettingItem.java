package ir.microsign.settings.object;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.context.Preference;


/**
 * Created by Mohammad on 9/9/14.
 */
public class SettingItem {
    Object mValue = null, mDefValue;
    String mName = null;
    int mStep = 1, mType = 0, mId = 0;
    Object mDefaultValue = null;
    String mPreKey = "";
    int mMin = 0, mMax = 0;
    Context mContext = null;
    List<MetaObject> mItems = null;
    List<SelectableItem> mList = null;

    public SettingItem(Context context, String name, String preKey, boolean defValue) {
        mContext = context;
        mName = name;

        mPreKey = preKey;
        mDefaultValue = defValue;
        mType = 0;
        mValue = getRegistedValue();
    }

    public SettingItem(Context context, String name, String preKey, int defValue, int min, int max, int step) {
        mContext = context;
        mName = name;

        mMax = max;
        mMin = min;
        mPreKey = preKey;
        mDefaultValue = defValue;
        mStep = step;
        mType = 1;
        mValue = getRegistedValue();

    }

    public SettingItem(Context context, String name, String preKey, int defValue, List<MetaObject> items) {
        mContext = context;
        mName = name;

        mType = 2;
        mItems = items;
        mPreKey = preKey;
        mDefaultValue = defValue;
        mValue = getRegistedValue();

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
//	public Object getDefaultValue() {
//		return mDefValue;
//	}

    public Object getValue(Object defValue) {
        return mValue == null ? defValue : mValue;
    }

    public Object getValue() {
        return mValue;
    }

    public void setValue(Object value) {
        mValue = value;
    }

    public int getType() {
        return mType;
    }

    public int getStep() {
        return mStep;
    }

    public void setStep(int step) {
        mStep = step;
    }

    public String getPreKey() {
        return mPreKey;
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        mMin = min;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void registValue() {
        Preference.set(mContext, getPreKey() + getName(), getValue(getDefaultValue()));
    }

    public Object getRegistedValue() {
        return Preference.getValue(mContext, getFullKey(), getDefaultValue());
    }

    public List<SelectableItem> getSelectableItems() {
        if (mItems == null) return new ArrayList<SelectableItem>();
        if (mList != null) return mList;
        mList = new ArrayList<SelectableItem>();
        int value = (Integer) getValue();
        for (int i = 0; i < mItems.size(); i++) {
            MetaObject item = mItems.get(i);
            mList.add(new SelectableItem(i, item.getTitle(), item.getDescription(), i == value));

        }
        return mList;
    }

    public String getFullKey() {
        return mPreKey + getName();
    }

    public Object getDefaultValue() {
        return mDefaultValue;
    }

    public void setDefaultValue(Object value) {
        mDefValue = value;
    }

    public void resetDefault() {
        setValue(getDefaultValue());
    }

    public String getTitle() {
        int id = mContext.getResources().getIdentifier(getPreKey() + "title_" + getName(), "string", mContext.getPackageName());
        return mContext.getString(id);
    }

    public String getDesc() {
        int id = mContext.getResources().getIdentifier(getPreKey() + "desc_" + getName(), "string", mContext.getPackageName());
        return mContext.getString(id);
    }
}
