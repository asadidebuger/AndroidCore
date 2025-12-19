package ir.microsign.content.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Mohammad on 30/04/2016.
 */
public class AutoCompleteTxt extends AutoCompleteTextView {
    public AutoCompleteTxt(Context context) {
        super(context);
    }

    public AutoCompleteTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteTxt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        setDropDownAnchor();
    }

    @Override
    public void showDropDown() {
        super.showDropDown();
    }
}
