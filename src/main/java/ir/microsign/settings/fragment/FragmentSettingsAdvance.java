package ir.microsign.settings.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Locale;

import ir.microsign.context.Application;
import ir.microsign.R;
import ir.microsign.settings.Setting;
import ir.microsign.settings.object.SettingItem;
import ir.microsign.settings.view.SettingItemEditView;

/**
 * Created by Mohammad on 6/22/14.
 */
public class FragmentSettingsAdvance extends FragmentSettingsBase {
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
        List<SettingItem> settingItems = Setting.Advance.getAll(getActivity());
        mItems = new SettingItemEditView[settingItems.size()];
        for (int i = 0; i < settingItems.size(); i++) {
            mItems[i] = new SettingItemEditView(getActivity(), settingItems.get(i), i);
        }
        for (SettingItemEditView item : mItems)
            root.addView(item);
    }

    @Override
    public void okAll() {
        if (mItems == null) return;
        for (SettingItemEditView item : mItems)
            item.ok();
        String locale = Setting.Advance.getLocale(getActivity());

        Application.setLocale(new Locale(locale.equalsIgnoreCase("system") ? "" : locale));
    }

    @Override
    public void defaultAll() {
        for (SettingItemEditView item : mItems)
            item.onItemDefault();
    }


}
