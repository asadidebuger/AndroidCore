package ir.microsign.settings.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;

import ir.microsign.dialog.MessageDialog;
import ir.microsign.R;
import ir.microsign.settings.dialog.Message;
import ir.microsign.settings.fragment.FragmentSettingsAdvance;
import ir.microsign.settings.fragment.FragmentSettingsBase;
import ir.microsign.settings.fragment.FragmentSettingsConnection;
import ir.microsign.settings.fragment.FragmentSettingsFont;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

public class ActivitySetting extends FragmentActivity implements View.OnClickListener {


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */

    static FragmentSettingsFont mFragmentSettingsFont = new FragmentSettingsFont();
    static FragmentSettingsConnection mFragmentSettingsConnection = new FragmentSettingsConnection();
    static FragmentSettingsAdvance mFragmentSettingsAdvance = new FragmentSettingsAdvance();
    static FragmentSettingsBase[] mFragments = new FragmentSettingsBase[]{mFragmentSettingsFont, mFragmentSettingsConnection, mFragmentSettingsAdvance};
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    View[] mTabs = null;
    int mCurrent = 0;
    boolean exit = false;

    void setTabs() {
        if (mTabs == null) {
            View txtAppearance = findViewById(R.id.txt_setting_title_appearance), txtConnection = findViewById(R.id.txt_setting_title_connection), txtAdvance = findViewById(R.id.txt_setting_title_advance);
            mTabs = new View[]{txtAppearance, txtConnection, txtAdvance};
        }
        for (int i = 0; i < mTabs.length; i++) {
            View v = mTabs[i];
            v.setOnClickListener(this);
            Text.setText(v, mAppSectionsPagerAdapter.getTitles()[i], Font.TextPos.h1);
        }
        selectTab(mTabs[0]);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(this,getSupportFragmentManager());
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setImageResource(R.drawable.icon_close_white);
        imgClose.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectTab(mTabs[position]);
            }
        });
        // For each of the sections in the app, add a tab to the action bar.
        setTabs();

        ImageView imgOk = (ImageView) findViewById(R.id.img_ok);
        imgOk.setImageResource(R.drawable.icon_ok_white);
        ImageView imgDefault = (ImageView) findViewById(R.id.img_default);
        view.setVisibility(imgOk, false);
        imgDefault.setImageResource(ir.microsign.R.drawable.icon_refresh_white);
        imgOk.setOnClickListener(this);
        imgDefault.setOnClickListener(this);
    }

    void setDefaultAll() {
        Message.showSettDefaultAll(this, mAppSectionsPagerAdapter.getTitles()[mCurrent], new MessageDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, String key) {
                if (!ok) return;
                mFragments[mCurrent].defaultAll();

            }
        });
    }

    void setOkAll() {
//        Message.showSettOkAllSection(this, new MessageDialog.OnDialogResultListener() {
//            @Override
//            public void OnDialogResult(boolean ok, String key) {
//                exit = true;
//                if (!ok) {
//                    finish();
//                    return;
//                }
                for (FragmentSettingsBase fr : mFragments)
                    fr.okAll();
//
//                finish();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_default)
            setDefaultAll();
        else if (v.getId() == R.id.img_ok)
            setOkAll();
        else {

            int position = 0;
            if (v.equals(mTabs[0])) position = 0;
            else if (v.equals(mTabs[1])) position = 1;
            else if (v.equals(mTabs[2])) position = 2;
            else if (v.getId() == R.id.img_close) {
                finish();
                return;
            }

            selectTab(v);
            mViewPager.setCurrentItem(position);
        }
    }

    void selectTab(View v) {
        for (int i = 0, mTabsLength = mTabs.length; i < mTabsLength; i++) {
            View v1 = mTabs[i];
            if (v1.equals(v)) {
                mCurrent = i;
                v1.setSelected(true);
                Text.setText(findViewById(R.id.txt_title), mAppSectionsPagerAdapter.mTitles[i], Font.TextPos.h1);
            } else v1.setSelected(false);
        }

    }

    @Override
    public void finish() {
//        if (!exit)
            setOkAll();
//        else

            super.finish();
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        String[] mTitles = null;
        Context context;

        public AppSectionsPagerAdapter(Context context,FragmentManager fm) {
            super(fm);
            this.context=context;
        }


        @Override
        public Fragment getItem(int i) {
            return mFragments[i];

        }

        @Override
        public int getCount() {
            return getTitles().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTitles()[position];
        }

        public String[] getTitles() {
            if (mTitles != null) return mTitles;
            mTitles = new String[]{context.getString(R.string.setting_title_appearance),
                    context.getString(R.string.setting_title_connection),
                    context.getString(R.string.setting_title_advance)};
            return mTitles;
        }


    }
}
