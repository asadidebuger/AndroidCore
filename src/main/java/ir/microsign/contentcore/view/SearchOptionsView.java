package ir.microsign.contentcore.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ir.microsign.R;
import ir.microsign.context.Preference;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.views.Switch;

/**
 * Created by Mohammad on 8/5/14.
 */
public class SearchOptionsView extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    ScrollView mScrollView = null;
//	MenuView mViewMenu = null;

    public SearchOptionsView(Context context) {
        super(context);
        inIt();
    }

    public SearchOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SearchOptionsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inIt();
    }

    void inIt() {
        if (isInEditMode()) return;
        if (mScrollView == null) {
            mScrollView = (ScrollView) inflate(getContext(), R.layout.layout_scrollbar, null);
        }

        ViewGroup.LayoutParams layoutParams = mScrollView.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
        mScrollView.setLayoutParams(layoutParams);
        LinearLayout ll = (LinearLayout) view.getLayoutInflater(getContext()).inflate(R.layout.layout_search_options, null);
        ViewGroup.LayoutParams layoutParams2 = mScrollView.getLayoutParams();
        if (layoutParams == null)
            layoutParams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
        ll.setLayoutParams(layoutParams2);
        addView(mScrollView);
        mScrollView.addView(ll);

//		Text.setText(findViewById(R._id.txt_search_case_sensitivity),R.string.search_options_case_sensitivity, Font.TextPos.h1);
//
        Text.setText(findViewById(R.id.txt_search_in_all_categories), R.string.search_options_in_all_categories, Font.TextPos.h1);
//		Text.setText(findViewById(R._id.txt_search_in_full_text),R.string.search_options_in_full_text, Font.TextPos.h1);
//		Text.setText(findViewById(R.id.txt_search_in_intro),R.string.search_options_in_intro, Font.TextPos.h1);
//		Text.setText(findViewById(R.id.txt_search_in_titles),R.string.search_options_in_titles, Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_search_options_title), R.string.search_options_title, Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_search_in_time), R.string.search_options_search_in_time, Font.TextPos.h1);

        Text.setText(findViewById(R.id.txt_search_in_time_desc), R.string.search_options_search_in_time_desc, Font.TextPos.p);
        Text.setText(findViewById(R.id.txt_search_in_all_categories_desc), R.string.search_options_in_all_categories_desc, Font.TextPos.p);
//		Text.setText(findViewById(R._id.txt_search_in_full_text_desc),R.string.search_options_in_full_text, Font.TextPos.p);
//		Text.setText(findViewById(R._id.txt_search_in_intro_desc),R.string.search_options_in_intro_desc, Font.TextPos.p);
//		Text.setText(findViewById(R._id.txt_search_in_titles_desc),R.string.search_options_in_titles_desc, Font.TextPos.p);
//		Text.setText(findViewById(R._id.txt_search_case_sensitivity_desc), R.string.search_options_case_sensitivity_desc, Font.TextPos.p);

        Switch swCase, swCat, swFull, swIntro, swTitle, swInTime;
        swCase = ((Switch) findViewById(R.id.sw_search_case_sensitivity));
        swCase.setChecked(getCaseSensitivity());
        swCat = ((Switch) findViewById(R.id.sw_search_in_all_categories));
        swCat.setChecked(getInAllCategories());
        swFull = ((Switch) findViewById(R.id.sw_search_in_full_text));
        swFull.setChecked(getInFullText());
        swIntro = ((Switch) findViewById(R.id.sw_search_in_intro));
        swIntro.setChecked(getInIntro());
        swTitle = ((Switch) findViewById(R.id.sw_search_in_titles));
        swTitle.setChecked(getInTitle());
        swInTime = ((Switch) findViewById(R.id.sw_search_in_time));
        if (!getContext().getResources().getBoolean(R.bool.show_search_in_time)) {
            swInTime.setEnabled(false);
            Preference.set(getContext(), "search_options_in_time", false);
        }

        swInTime.setChecked(getInTime());
        swCase.setOnCheckedChangeListener(this);
        swCat.setOnCheckedChangeListener(this);
        swFull.setOnCheckedChangeListener(this);
        swIntro.setOnCheckedChangeListener(this);
        swTitle.setOnCheckedChangeListener(this);
        swInTime.setOnCheckedChangeListener(this);


    }

    public boolean getCaseSensitivity() {
        return Preference.getBool(getContext(), "search_options_case_sensitivity", false);
    }

    public void setCaseSensitivity(boolean value) {
        Preference.set(getContext(), "search_options_case_sensitivity", value);
    }

    public boolean getInAllCategories() {
        return Preference.getBool(getContext(), "search_options_all_categories", true);
    }

    public void setInAllCategories(boolean value) {
        Preference.set(getContext(), "search_options_all_categories", value);
    }

    public boolean getInFullText() {
        return Preference.getBool(getContext(), "search_options_in_full_text", true);
    }

    public void setInFullText(boolean value) {
        Preference.set(getContext(), "search_options_in_full_text", value);
    }

    public boolean getInIntro() {
        return Preference.getBool(getContext(), "search_options_in_intro", true);
    }

    public void setInIntro(boolean value) {
        Preference.set(getContext(), "search_options_in_intro", value);
    }

    public boolean getInTitle() {
        return Preference.getBool(getContext(), "search_options_in_titles", true);
    }

    public void setInTitle(boolean value) {
        Preference.set(getContext(), "search_options_in_titles", value);
    }

    public boolean getInTime() {
        return Preference.getBool(getContext(), "search_options_in_time", true);
    }

    public void setInTime(boolean value) {
        Preference.set(getContext(), "search_options_in_time", value);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.sw_search_case_sensitivity) {
            setCaseSensitivity(isChecked);
        } else if (i == R.id.sw_search_in_all_categories) {
            setInAllCategories(isChecked);
        } else if (i == R.id.sw_search_in_full_text) {
            setInFullText(isChecked);
        } else if (i == R.id.sw_search_in_intro) {
            setInIntro(isChecked);
        } else if (i == R.id.sw_search_in_titles) {
            setInTitle(isChecked);
        } else if (i == R.id.sw_search_in_time) {
            setInTime(isChecked);
        }

    }
}
