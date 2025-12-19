package ir.microsign.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by Mohammad on 05/03/2015.
 */
public class NumberTextWatcher implements TextWatcher {

    //	public static String getSimpleFormatterWithSep(int decNum) {
//		String formatter="";
//		for (int i=0;i<decNum;i++){
//			formatter+=(i==0?".#":"#");}
//		return formatter;
//	}
    private static final String TAG = "NumberTextWatcher";
    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;
    private EditText et;

    public NumberTextWatcher(EditText et, int decNum) {
        df = Format.getDecimalFormat("#,###" + getSimpleFormatter(decNum));
        df.setDecimalSeparatorAlwaysShown(false);
        dfnd = Format.getDecimalFormat("#,###");
        this.et = et;
        hasFractionalPart = false;
        this.et.addTextChangedListener(this);
    }

    public NumberTextWatcher(EditText et) {
        this(et, 0);
    }

    public static String getSimpleFormatter(int decNum) {
        String formatter = "";
        for (int i = 0; i < decNum; i++) {
            formatter += (i == 0 ? ".#" : "#");
        }
        return formatter;
    }

    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();
            String gsep = String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator());
            String v = s.toString().replace(gsep, "");
            Number n = df.parse(v);
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {

                String val = df.format(n);
                String sep = String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator());
                if (v.contains(sep) && !val.contains(sep)) val += ".";
                et.setText(val);
            } else {
                et.setText(dfnd.format(n));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (Exception e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }
}
