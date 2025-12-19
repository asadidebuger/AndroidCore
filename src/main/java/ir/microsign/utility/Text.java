package ir.microsign.utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Math;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.microsign.R;
import ir.microsign.context.Preference;

/**
 * Created by Mohammad on 03/12/2017.
 */

public class Text {
    public static Boolean mBeReshape = null;
    public static Boolean mBeReshapeWeb = null;
    public static boolean getBeReshape(Context context){
        if (mBeReshape==null)
            mBeReshape = Preference.getBool(context, context.getString(R.string.settings_key_reshape), true);
        return mBeReshape;

    }
    public static boolean getBeReshapeWeb(Context context){
        if (mBeReshapeWeb==null)
            mBeReshapeWeb = Preference.getBool(context, context.getString(R.string.settings_key_reshape_web), true);
        return mBeReshapeWeb;

    }
   public static String reshape(String txt) {
        return txt;
    }

    public static String[] reshape(Context context,String[] strArr) {
if (!getBeReshape(context))return strArr;
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = reshape(strArr[i]);
        }
        return strArr;
    }


    public static String reshape(Context context,int stringId) {
        if (!getBeReshape(context))return context.getString(stringId);
        return reshape(context.getString(stringId));
    }
    public static String reshapeWeb(Context context,String Str) {
        if (!getBeReshapeWeb(context))return Str;
        return reshape(Str );
    }
    public static String reshape(Context context,String Str) {
        if (!getBeReshape(context))return Str;
        return reshape(Str );
    }
//    public static String reshape(String Str) {
//        if (Str == null) return Str;
//        return ir.microsign.Reshape.Reshape.Reshape(Str);
//    }
    static String ex = null;

    public static String getClearDouble(String digit) {
        if (isNullOrEmpty(digit)) return "0";
        if (digit.endsWith(".") || digit.endsWith(".0"))
            return digit.substring(0, digit.lastIndexOf("."));
        return digit;
    }
    public static int indexOf(String src,char target,int startIndex,int endIndex,int count){

        int occured=0;
        for (int i = Math.min(startIndex,0), l = Math.min(src.length(),endIndex+1); i<l; i++){
            if (src.charAt(i)==target)occured++;
            if (occured==count)return i;
        }
        return -1;
    }public static int lastIndexOf(String src,char target,int startIndex,int endIndex,int count){

        int occured=0;
        for (int i = Math.min(src.length()-1,endIndex), l = Math.min(startIndex,0)-1; i>l; i--){
            if (src.charAt(i)==target)occured++;
            if (occured==count)return i;
        }
        return -1;
    }
    public static String replaceRegex(String src, String dest, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(src);
        StringBuffer sb = new StringBuffer();
        while (m.find())
            m.appendReplacement(sb, dest);
        m.appendTail(sb);
        return sb.toString();
    }

    public static String replaceRegexWithPattern(String src, String regex, String pattern) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(src);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String match = m.group(0);
            match = String.format(pattern, match);
            m.appendReplacement(sb, match);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static boolean isMatchByRegex(String src, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(src);
//		StringBuffer sb = new StringBuffer();
        return m.matches();
//	 	while(m.find())
//			m.appendReplacement(sb, dest);
//		m.appendTail(sb);
//		return sb.toString();
    }

    public static List<String> getMatches(String src, String pattern) {

        List<String> matches = new ArrayList<String>();
        if (isNullOrEmpty(src)) return matches;
        Matcher m = Pattern.compile(pattern).matcher(src);
        while (m.find()) {
            matches.add(m.group());
        }
        return matches;
    }
    public static boolean hasMatches(String src, String pattern) {
        if (isNullOrEmpty(src)) return false;
        Matcher m = Pattern.compile(pattern).matcher(src);
        while (m.find()) {
            return true;
        }
        return false;
    }

    public static String tryGetMatch(String src, String pattern, int index) {

        List<String> matches = getMatches(src, pattern);
        return index >= matches.size() ? null : matches.get(index);
    }

    //	public static DecimalFormat decimalFormatFloat;//=new DecimalFormat( "#,###.##");
//	public static DecimalFormat decimalFormatInteger;//=new DecimalFormat( "#,###");

    public static boolean equals(String s1,String s2){
            return  (s1==null && s2==null)||(s1!=null&&s1.equals(s2));
    }
    public static String replaceLatin(String txt) {
        if (txt == null) return null;
        return FixString(txt).replace('؛', ';').replace('،', ',').replace('؟', '?');
    }

    public static String getText(View textView) {
        if (textView == null) return null;
        return ((TextView) textView).getText().toString();
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.length() < 1 || input.trim().length() < 1;
    }
    public static boolean isEmpty(String input) {
        return input == null || input.length() < 1 || input.trim().length() < 1;
    }
    public static boolean notEmpty(String input) {
return !isEmpty(input);
    }

    //    public static boolean isNullOrEmpty(String input) {
//        return input == null || input.length() < 1 || input.trim().length() < 1;
//    }
    public static void setText(View textView, int stringId, String textPos, boolean goneIfEmpty) {
        setText(textView, textView.getContext().getString(stringId), textPos, goneIfEmpty);
    }

    public static void setText(View textView, int stringId, String textPos) {
        if (textView == null) return;
        setText(textView, textView.getContext().getString(stringId), textPos);
    }

    public static void setHint(View textView, int stringId, String textPos) {
        if (textView == null) return;
        setHint(textView, textView.getContext().getString(stringId), textPos);
    }

    public static void setText(View textView, String txt, String textPos,float scaleSize, boolean goneIfEmpty) {
        if (textView == null) return;
        setText(textView, txt, textView.isInEditMode() ? null : Font.getFont(textView.getContext(),textPos),scaleSize, goneIfEmpty);
    }
    public static void setText(View textView, String txt, String textPos, boolean goneIfEmpty) {
        if (textView == null) return;
        setText(textView, txt, textView.isInEditMode() ? null : Font.getFont(textView.getContext(),textPos), goneIfEmpty);
    }

    public static void setText(View textView, String txt, Font font, boolean goneIfEmpty) {
        setText(textView, txt, font,1, goneIfEmpty);
    }

    public static void setHint(View textView, String txt, String textPos) {
        Font font = Font.getFont(textView.getContext(),textPos);
        setHint(textView, txt, font);
    }

    public static void setText(View textView, String txt, Font font,float scaleSize, boolean goneIfEmpty) {
        if (textView == null) return;
        if (font == null) {
            ((TextView) textView).setText(txt);
            return;
        }
        if (goneIfEmpty && isNullOrEmpty(txt)) {
            view.setVisibility(textView, false);
            return;
        } else view.setVisibility(textView, true);

        Typeface t= font.getTypeface();
        if (t!=null)((TextView) textView).setTypeface(t);
        if (font.getSize() > 0) ((TextView) textView).setTextSize(font.getSize()*scaleSize);

        if (font.getColor() < 1) ((TextView) textView).setTextColor(font.getColor());
        if (font.getBackColor() < 1) textView.setBackgroundColor(font.getBackColor());
        ((TextView) textView).setText(isNullOrEmpty(txt) ? "" : reshape(txt));
    }

    public static void setHint(View textView, String txt, Font font) {
        if (textView == null) return;
//		if (goneIfEmpty && (txt == null || txt.length() < 1)) {
//			tarnian.view.setVisibility(textView, false);
//			return;
//		} else tarnian.view.setVisibility(textView, true);

        ((TextView) textView).setHint(reshape(txt));
        ((TextView) textView).setTypeface(font.getTypeface());
        if (font.getSize() > 0) ((TextView) textView).setTextSize(font.getSize());
        if (font.getColor() < 1) ((TextView) textView).setTextColor(font.getColor());
        if (font.getBackColor() < 1) textView.setBackgroundColor(font.getBackColor());
    }

    public static void setHint(View textView, String txt) {
        if (textView == null) return;
        ((TextView) textView).setHint(reshape(txt));
    }

    public static void setHint(View textView, int resId) {
        if (textView == null) return;
        ((TextView) textView).setHint(reshape(textView.getContext().getString(resId)));
    }

    public static boolean isContains(String src, String dest, boolean caseSensitivity, boolean fixStrings) {
        if (isNullOrEmpty(src)) return false;
        if (isNullOrEmpty(dest)) return true;
        if (!caseSensitivity) {
            src = src.toLowerCase();
            dest = dest.toLowerCase();
        }
        if (fixStrings) {
            src = FixString(src);
            dest = FixString(dest);
        }
        return src.contains(dest);
    }

    public static boolean isContains(String src, String dest, boolean caseSensitivity) {
        return isContains(src, dest, caseSensitivity, true);
    }

    public static void setText(View textView, String txt, String textPos) {
        if (textView == null) return;
        setText(textView, txt, textPos, true);
    }

    public final static void setText(View root, int id, String txt, String textPos) {
        try {
            View txtView = root.findViewById(id);
            if (txtView == null) return;
            Text.setText(txtView, txt, textPos);
        } catch (Exception e) {
            Log.e("Text id=" + id + " text=" + txt, e.getMessage());
            return;
        }
    }

//    public static void setBrowser(ViewHtml webView, String html) {
//        webView.setContent(replaceLatin(html), Font.TextPos.web, true, false);
//    }

    public static String safeCut(String input, int cutIndex, boolean cutWord) {
        return safeCut(input,cutIndex,cutWord,"");
    }
    public static String safeCut(String input, int cutIndex, boolean cutWord,String etc) {
        if (input == null) return input;
        int len = input.length();
        if (cutIndex >= len) return input;

        input = input.substring(0, cutIndex);
        if (!cutWord) return input+etc;
        char[] separators = new char[]{' ', '\r', '\n', ',', ';'};
        len = 0;
        for (char sep : separators) {
            int last = input.lastIndexOf(sep);
            if (len < last) len = last;
        }
        if (len < 1) return input;
        return input.substring(0, len)+etc;
    }

    public static String ReadResTxt(Context context,int resID) {
        String txt = "";
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(resID);
            byte[] reader = new byte[inputStream.available()];
            while (inputStream.read(reader) != -1) {
                // html += new String(reader);
            }
            txt = new String(reader);

        } catch (IOException e) {
            Log.e("LOG_APP_TAG", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("LOG_APP_TAG", e.getMessage());
                }
            }
        }
        return txt;
    }

    public static List<String> readResLines(Context context,int resID) {
        InputStream inputStream = null;
        List<String> lines = new ArrayList<String>();
        try {
            inputStream =context.getResources().openRawResource(resID);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = r.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            Log.e("LOG_APP_TAG", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("LOG_APP_TAG", e.getMessage());
                }
            }
        }
        return new ArrayList<String>();
    }

    public static String CleanHtml(String html) {
        return CleanHtml(html,false);
    }
    public static String CleanHtml(String html,boolean regex) {
        if (isNullOrEmpty(html))return html;
        return regex?html.replaceAll("</?.+?>",""): Html.fromHtml(html).toString();
    }
    public static void Share(Context context, String content, String subject) {
        Share(context, content, subject,null);
    }
    public static void Share(Context context, String content, String subject,String chooserTitle) {
        List<Intent> targetedShareIntents = new ArrayList<>();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for (final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);
            targetedShareIntent.setType("text/plain");
            targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            targetedShareIntent.putExtra(Intent.EXTRA_TEXT, content);
            targetedShareIntent.setPackage(packageName);
            targetedShareIntents.add(targetedShareIntent);
        }
        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0),chooserTitle==null? "به اشتراک گذاشتن محتوا":chooserTitle);
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
        try {
            context.startActivity(chooserIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//           Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//           sharingIntent.setType("text/plain");
//           sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
//           sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
//           context.startActivity(Intent.createChooser(sharingIntent, "Share with"));
    }

    //		return st.replace("٠", "0").replace("١", "1").replace("٢", "2").replace("٣", "3").replace("٤", "4").replace("٥", "5").replace("٦", "6").replace("٧", "7").replace("٨", "8").replace("٩", "9");
//	}
    public static String FixString(String text) {

        if (text == null)
            return null;
        return text.replace("\u0660", "\u06F0") // ۰
                .replace("\u0661", "\u06F1") // ۱
                .replace("\u0662", "\u06F2") // ۲
                .replace("\u0663", "\u06F3") // ۳
                .replace("\u0664", "\u06F4") // ۴
                .replace("\u0665", "\u06F5") // ۵
                .replace("\u0666", "\u06F6") // ۶
                .replace("\u0667", "\u06F7") // ۷
                .replace("\u0668", "\u06F8") // ۸
                .replace("\u0669", "\u06F9") // ۹
                .replace("\u0643", "\u06A9") // ک
                .replace("\u0649", "\u06CC") // ی
                .replace("\u064A", "\u06CC") // ی
                .replace("\u06C0", "\u0647\u0654") // هٔ
                .replace("؛", ";") // هٔ
                .replaceAll("[ًٌٍَُِّْء]", "")
                .replaceAll("[أإ]", "ا")
                .replaceAll("[؟�]", "?");

    }
//	static String Standardialize(String st) {

    public static String[] getLikes(String ch) {
        if (ch.equals("\u0660") || ch.equals("\u06F0")) return new String[]{"\u06F0", "\u0660"}; // ۰
        if (ch.equals("\u0661") || ch.equals("\u06F1")) return new String[]{"\u06F0", "\u0660"}; // ۱
        if (ch.equals("\u0662") || ch.equals("\u06F2")) return new String[]{"\u0662", "\u06F2"}; // ۲
        if (ch.equals("\u0663") || ch.equals("\u06F3")) return new String[]{"\u0663", "\u06F3"}; // ۳
        if (ch.equals("\u0664") || ch.equals("\u06F4")) return new String[]{"\u0664", "\u06F4"}; // ۴
        if (ch.equals("\u0665") || ch.equals("\u06F5")) return new String[]{"\u0665", "\u06F5"}; // ۵
        if (ch.equals("\u0666") || ch.equals("\u06F6")) return new String[]{"\u0666", "\u06F6"}; // ۶
        if (ch.equals("\u0667") || ch.equals("\u06F7")) return new String[]{"\u0667", "\u06F7"}; // ۷
        if (ch.equals("\u0668") || ch.equals("\u06F8")) return new String[]{"\u0668", "\u06F8"}; // ۸
        if (ch.equals("\u0669") || ch.equals("\u06F9")) return new String[]{"\u0669", "\u06F9"}; // ۹
        if (ch.equals("\u0643") || ch.equals("\u06A9")) return new String[]{"\u0643", "\u06A9"}; // ک
        if (ch.equals("\u0649") || ch.equals("\u06CC") | ch.equals("\u064A"))
            return new String[]{"\u0649", "\u06CC", "\u064A"}; // ی
        if (ch.equals("\u06C0") || ch.equals("\u0647\u0654")) return new String[]{"\u06C0", "\u0647\u0654"}; // هٔ
        if (ch.equals("أ") || ch.equals("ا") || ch.equals("إ")) return new String[]{"أ", "ا", "إ"};
        if (ch.equals("؟") || ch.equals("?") || ch.equals("�")) return new String[]{"؟", "?", "�"};
        if (ex == null) ex = "ًٌٍَُِّْء";
        if (ex.contains(ch)) return new String[]{};
        return new String[]{ch};
    }

    public static String getRegex(String input, boolean inLine) {
        String regex = inLine ? "^.*C.*" : ".*C.*";
        String sub = "";
        for (int i = 0; i < input.length(); i++) {
            String[] chars = getLikes(input.substring(i, i + 1));
            if (chars.length == 0) sub += ".";
            else if (chars.length == 1) sub += chars[0];
            else {
                String p = "(";
                for (String sh : chars) {
                    p += sh + "|";
                }
                p = p.substring(0, p.length() - 1) + ")";
                sub += p;
            }
        }

        return regex.replace("C", sub);
    }

    public static String getLike(String input) {
        String regex = "C";
        String sub = "";
        for (int i = 0; i < input.length(); i++) {
            String[] chars = getLikes(input.substring(i, i + 1));
            if (chars.length == 0) sub += "_";
            else if (chars.length == 1) sub += chars[0];
            else sub += "_";
//			{
//				String p="(";
//				for (String sh:chars){
//					p+=sh+"|";
//				}
//				p=p.substring(0,p.length()-1)+")";
//				sub+=p;
//			}
        }

        return regex.replace("C", sub);
    }

    public static boolean supportRtl() {
        return (android.os.Build.VERSION.SDK_INT > 9);
    }

    public static String getValueInParams(String params, String name) {
        Pattern p = Pattern.compile("\"" + name + "\":\".*?\"");
        Matcher m = p.matcher(params);
        String patt = "";
        try {
            if (m.find())
                patt = m.group(0);
            else return "";
//            int len=4+name.length();
            return patt.substring(4+name.length(),patt.length()-1);

//            p = Pattern.compile("[^(\"" + name + ":\")][^\"|:]*");
//            m = p.matcher(patt);
//            patt = "";
//            if (m.find())
//                patt = m.group();
//            return patt;
        } catch (Exception ex) {
            return "";
        }
    }

    public static boolean isContainByRegex(String src, String pattern) {
        if (isNullOrEmpty(src)) return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(src);
        return m.find();
    }

//    public static void setText(View view, String s, Font mTitleFont, boolean b) {
//        setText(view,s,mTitleFont,false);
//    }

    public static class Parse {
        public static Integer ParseInt(String txt, Integer defaultValue) {
            try {
//				String num=txt.replace(String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator()),"");
//				return 	Integer.parseInt(num);
                try {
                    if (txt.toLowerCase().equals("nan")) return Integer.MAX_VALUE;
//				String num=txt.replace(String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator()),"");
                    DecimalFormat df;
                    df = Format.getDecimalFormat("#,###");
                    Number n = null;
                    n = df.parse(txt);
                    return n.intValue();
//						return 	Double.parseDouble(num);
                } catch (Exception e) {
                    return defaultValue;
                }
            } catch (Exception e) {
                return defaultValue;
            }

        }

        public static Integer ParseIntView(View txtView, Integer defaultValue) {
            return ParseInt(getText(txtView), defaultValue);
        }

        public static Double ParseDouble(String txt, Double defaultValue) {
            try {
                if (txt.toLowerCase().equals("nan")) return Double.NaN;
//				String num=txt.replace(String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator()),"");
                DecimalFormat df;
                df = Format.getDecimalFormat("#,###.##########");
                Number n = null;
                n = df.parse(txt);
                return n.doubleValue();
//						return 	Double.parseDouble(num);
            } catch (Exception e) {
                return defaultValue;
            }

        }

        public static Double ParseDoubleView(View txtView, Double defaultValue) {
            return ParseDouble(getText(txtView), defaultValue);
        }
    }

    public static class Validate {
        public static boolean isEmail(String email) {

            return email != null && email.length() > 6 && email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        }
    }
}
