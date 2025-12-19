package ir.microsign.settings.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;

import java.util.List;

import ir.microsign.R;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;


/**
 * Created by Mohammad on 7/23/14.
 */
public class SelectFontView extends LinearLayout implements View.OnClickListener {

    Listener.ResultListener mResultListener = null;
    int mSelected = -1;
    //	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public SelectFontView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		inIt();
//	}
    String[] mFonts = null;

    public SelectFontView(Context context, int selected) {
        super(context);
        mSelected = selected;
        inIt();
    }

    public SelectFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }

    public void setResultListener(Listener.ResultListener l) {
        mResultListener = l;

    }

    void inIt() {
        ScrollView scrollView = (ScrollView) view.getLayoutInflater(getContext()).inflate(ir.microsign.R.layout.layout_scrollbar, null);
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(VERTICAL);
        mFonts = Font.getFonts(getContext());
        setOrientation(VERTICAL);
        removeAllViews();
        float sizeH1 = Font.getFont(getContext(),Font.TextPos.h1).getSize();
        for (int i = 0; i < mFonts.length; i++) {
            View view = ir.microsign.utility.view.getLayoutInflater(getContext()).inflate(R.layout.layout_font_name_item, null);
            RadioButton rdb = (RadioButton) view.findViewById(R.id.rdb_font_name);
            rdb.setChecked(i == mSelected);
            rdb.setOnClickListener(this);
            rdb.setTag(i);
            String fontName = Font.getFontName(getContext(),i, true);
            String desc = String.format(getContext().getString(R.string.setting_font_sample_text), fontName);
            Text.setText(rdb, fontName + "\n\n" + desc, new Font(getContext(),i, sizeH1), false);
            root.addView(view);
        }
        scrollView.addView(root, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(scrollView);
    }

    public void setSelection(int index) {
        mSelected = index;
        List<View> rdbs = view.getAllChilds(this, RadioButton.class);
        for (View rdb : rdbs)
            ((RadioButton) rdb).setChecked(rdb.getTag().equals(index));

    }


    @Override
    public void onClick(View v) {
        setSelection((Integer) v.getTag());
        if (mResultListener != null) mResultListener.onResult(null, (Integer) v.getTag());
    }
}
