package ir.microsign.utility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Mohammad on 17/06/2015.
 */
public class Format {
    public static String FormatDouble(double value, int decNum) {

        String formatter = "##";
        for (int i = 0; i < decNum; i++) {
            formatter += (i == 0 ? "0.0" : "0");
        }
//        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
//        DecimalFormat myFormatter = (DecimalFormat)nf;
//        myFormatter.applyPattern(formatter);
//            DecimalFormat myFormatter = new DecimalFormat(formatter);
        DecimalFormat myFormatter = getDecimalFormat(formatter);
        String result = myFormatter.format(value);
        if (result.contains(".") && result.substring(result.lastIndexOf('.')).replace("0", "").endsWith("."))
            return result.substring(0, result.lastIndexOf('.'));
        return result;
//        return String.format("%."+ java.lang.Math.min(Math.getDecimalNums(value),decNum)+"f",value);
    }

    public static DecimalFormat getDecimalFormat(String formatString) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat(formatString, otherSymbols);
        return df;
    }

    public static String getFormattedDigits(Double value) {
        return getFormattedDigits(value, -1);
    }

    public static String getFormattedDigits(Double value, int decNum) {
        if (value.isNaN()) return "NaN";
        if (decNum > -1)
            value = Double.parseDouble(FormatDouble(value, decNum));
        DecimalFormat df;
        DecimalFormat dfnd;
        df = getDecimalFormat("#,###" + NumberTextWatcher.getSimpleFormatter(java.lang.Math.max(decNum, 10)));
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = getDecimalFormat("#,###");
        boolean hasFractionalPart;
        String s = FormatDouble(value, decNum);//Text.getClearDouble(String.valueOf(value));
        if (s.contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
        Number n = null;
        try {
            n = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (hasFractionalPart) {
            return df.format(n);
        } else {
            return dfnd.format(n);
        }
    }
}
