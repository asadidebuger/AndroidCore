package ir.microsign.settings.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.microsign.R;
import ir.microsign.settings.dialog.DialogEditFont;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.settings.view.EditFontView;
import ir.microsign.settings.view.SettingItemBaseView;
import ir.microsign.settings.view.SettingItemView;
import ir.microsign.utility.Font;

/**
 * Created by Mohammad on 6/22/14.
 */
public class FragmentSettingsFont extends FragmentSettingsBase implements Listener.SettingItemClickListener {
    Font[] mFonts = null;
    SettingItemView[] mItems = null;
    String[] mKeys = null;
    LinearLayout mRootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (LinearLayout) inflater.inflate(R.layout.layout_setting_root, container, false);
        mFonts = Font.getDefaultFonts(getContext());
        LinearLayout settingRoot = (LinearLayout) mRootView.findViewById(R.id.ll_setting_root);
        addFonts(settingRoot);

        return mRootView;
    }

    void addFonts(LinearLayout root) {

        mKeys = Font.TextPos.getAll();
        mItems = new SettingItemView[mKeys.length];
        for (int i = 0; i < mKeys.length; i++) {
            Font font = Font.getFont(getContext(),mKeys[i]);
            int titleId = getResources().getIdentifier("setting_font_title_" + mKeys[i], "string", getActivity().getPackageName());
            int descId = getResources().getIdentifier("setting_font_desc_" + mKeys[i], "string", getActivity().getPackageName());
            String title = getString(titleId), desc = getString(descId);
            SettingItemView item = new SettingItemView(getActivity(), i, title, desc, null, font);
            item.setSettingItemSelectedListener(this);
            item.setTag(i);
            root.addView(item);
            mItems[i] = item;
        }
    }

    @Override
    public void onItemChangeClick(SettingItemBaseView selectedItem) {
        final DialogEditFont mDialogEditFont = new DialogEditFont(getActivity(), mKeys[selectedItem.getId()]);
        mDialogEditFont.setParentActivity(getActivity());
        mDialogEditFont.show();
        mDialogEditFont.setOnResultListener(new Listener.ResultListener() {
            @Override
            public void onResult(Object o, final int i) {
                EditFontView fontView = ((EditFontView) o);

                int index = 0;
                for (; index < mKeys.length; index++)
                    if (mKeys[index].equals(fontView.getKey())) break;
                mItems[index].setFont(fontView.getFontCurrent());
                mDialogEditFont.forceHide();
               ok( mItems[index]);
            }
        });
    }

    @Override
    public void onItemDefaultClick(SettingItemBaseView selectedItem) {
        selectedItem.setFont(Font.getDefaultFont(getContext(),mKeys[selectedItem.getId()]));
    }

    @Override
    public void okAll() {
        if (mItems == null) return;
        for (SettingItemBaseView item : mItems) ok(item);
    }

    public void ok(SettingItemBaseView selectedItem) {
        Font.setFontByKey(getActivity(), selectedItem.getFont(), mKeys[selectedItem.getId()]);
    }

    @Override
    public void defaultAll() {
        for (SettingItemView item : mItems)
            item.onItemDefault();
    }

}
