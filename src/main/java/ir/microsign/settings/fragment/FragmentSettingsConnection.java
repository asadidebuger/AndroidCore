package ir.microsign.settings.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.microsign.R;
import ir.microsign.settings.Setting;
import ir.microsign.settings.view.SettingItemEditView;

/**
 * Created by Mohammad on 6/22/14.
 */
public class FragmentSettingsConnection extends FragmentSettingsBase {
    LinearLayout mRootView = null;
    SettingItemEditView[] mItems = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (LinearLayout) inflater.inflate(R.layout.layout_setting_root, container, false);
        LinearLayout settingRoot = (LinearLayout) mRootView.findViewById(R.id.ll_setting_root);
        addItems(settingRoot);
        return mRootView;
    }

    void addItems(LinearLayout root) {
        mItems = new SettingItemEditView[Setting.Connection.getAll(getActivity()).length];
        for (int i = 0; i < Setting.Connection.getAll(getActivity()).length; i++) {
            mItems[i] = new SettingItemEditView(getActivity(), Setting.Connection.getAll(getActivity())[i], i);
        }
        for (SettingItemEditView item : mItems)
            root.addView(item);
    }

    @Override
    public void okAll() {
        if (mItems == null) return;
        for (SettingItemEditView item : mItems)
            item.ok();
    }

    @Override
    public void defaultAll() {
        for (SettingItemEditView item : mItems)
            item.onItemDefault();
    }

}
