package ir.microsign.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import ir.microsign.R;


/**
 * Created by Mohammad on 9/2/14.
 */
public class Font {
    final static String FONTS_PATH = "fonts";
//    static Map<String,Font> mFontMap=null;
//    static List<Font> mFontList=null;
    static String[] mFontNames = null;
    static Map<String,Typeface> typefaceMap=new LinkedHashMap<>();
    static SharedPreferences mSharedPreferences = null;
    int mIndex = 0, mColor = 1, mBackColor = 1;
    float mSize = 0, mLineHeight = 0;

    //	 public String getKey(){
//		 return
//	 }
    Context context=null;
    public Font(Context context,int index, float size) {
        this.context=context;
        mIndex = index;
        mSize = size;
    }
    public Font(Context context,String name, float size) {

        this.context=context;
        mIndex= getFontByName(context,name,true).mIndex;
        mSize = size;
    }

    public Font(Context context,int index, float size, float lineHeight, int color, int backColor) {
        this.context=context;
        mIndex = index;
        mSize = size;
        mLineHeight = lineHeight;
        mColor = color;
        mBackColor = backColor;
    }

    public Font(Context context) {
        this.context=context;
    }

    public static Font getDefaultFont(Context context,String key) {
//		if (!key.equals(TextPos.web)||!key.equals(TextPos.webrtl))
        return getDefaultFontFromResource(context,key);
//		Font web= getDefaultFontFromResource(key);
//		web.setColor(Color.parseColor("#111111"));
//		web.setBackColor(Color.WHITE);
//		web.setLineHeight(140);
//		return web;//new Font(7, 17, 140, Color.parseColor("#222222"), Color.WHITE);
    }

    public static Font getDefaultFontFromResource(Context context,String key) {
//        Context context = activity.getContext();

        int resName = context.getResources().getIdentifier("name_" + key, "string", context.getPackageName());
        int resIdSize = context.getResources().getIdentifier("size_" + key, "integer", context.getPackageName());
        int lineheight = context.getResources().getIdentifier("line_" + key, "integer", context.getPackageName());
        int color = context.getResources().getIdentifier("color_" + key, "color", context.getPackageName());
        int backColor = context.getResources().getIdentifier("backcolor_" + key, "color", context.getPackageName());


        Font font =null;
        if (resName>0)
            font=new Font(context,context.getResources().getString(resName), context.getResources().getInteger(resIdSize));
            else   {
            int resIdIndex = context.getResources().getIdentifier("index_" + key, "integer", context.getPackageName());
            font=new Font(context,context.getResources().getInteger(resIdIndex), context.getResources().getInteger(resIdSize));}
        if (color > 0) font.setColor(context.getResources().getColor(color));
        if (backColor > 0) font.setBackColor(context.getResources().getColor(backColor));
        if (lineheight > 0) font.setLineHeight(context.getResources().getInteger(lineheight));
        return font;

    }

    public static Font[] getDefaultFonts(Context context) {
        String[] allKeys = TextPos.getAll();
        Font[] fonts = new Font[allKeys.length];
        for (int i = 0; i < allKeys.length; i++) fonts[i] = getDefaultFont(context,allKeys[i]);
        return fonts;
    }

    public static void setDefaultFonts(Context context) {
        String[] allKeys = TextPos.getAll();
        for (String key : allKeys)
            setFontByKey(context, getDefaultFont(context,key), key);
    }
//public static void resetToDefault(Context context){
//
//}
    public static String[] getFonts(Context context) {
        if (mFontNames != null) return mFontNames;
        try {
            ArrayList<String> fontList = new ArrayList<String>(Arrays.asList(context.getAssets().list(FONTS_PATH)));
            Collections.sort(fontList);
            mFontNames = fontList.toArray(new String[fontList.size()]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mFontNames;
    }

    public static String getFont(Context context, int index) {
        if (mFontNames == null) getFonts(context);
        return mFontNames[index < mFontNames.length ? index : 0];
    }

    public static String getFontPath(Context context,int index) {
        return "file:///android_asset/" + FONTS_PATH + "/" + getFont(context, index);
    }

    public static String getFontName(Context context,int index, boolean translate) {
        String font = getFont(context, index);
        String name = font.substring(0, font.lastIndexOf("."));
        if (!translate) return name;
        int id =context.getResources().getIdentifier("font_name_" + name.toLowerCase(), "string",context.getPackageName());
        if (id == 0) return name;
        return context.getString(id);
    }
    public static String getFontNameForWeb(Context context,int index, boolean removeWeight) {
        String font = getFontName(context,index,false);
        if (font.endsWith("bold")&&removeWeight)return font.substring(0,font.length()-4);
        return font;

    }

//    public static String getFont(Context context,int index) {
//        return getFont(context, index);
//    }

    public static Font getBaseFont(Context context,int index) {
        return new Font(context,index, -1);

    }

    public static Font getFont(Context context,String key) {
        return getFontByKey(context, key);
    }

    public static Font getFontByName(Context context, String name, boolean exactly) {
        if (mFontNames == null) getFonts(context);
        for (int i = 0; i < mFontNames.length; i++) {
            String font = mFontNames[i];
            if (exactly && font.toLowerCase().equals(name.toLowerCase() + ".ttf"))
                return getBaseFont(context,i);
            else if (font.contains(name.toLowerCase()))
                return getBaseFont(context,i);
        }
        return getBaseFont(context,0);
    }

    public static Typeface getTypeFace(Context context, int index) {

        return getTypeFace(context, getFont(context,index));
    }

//    public static Typeface getTypeFace(int index) {
//
//        return getTypeFace(activity.getContext(), index);
//    }

//    public static Typeface getTypeFace(String fontName) {
//
//        return getTypeFace(activity.getContext(), fontName);
//    }

    public static Typeface getTypeFace(Context context, String fontName) {

        return getTypeFace(context, FONTS_PATH, fontName);
    }
    public static Typeface getTypeFace(Context context, String fontFolder, String fontName) {
        String key=fontFolder + "/" + fontName;
        Typeface typeFace =typefaceMap.get(key);
        if (typeFace!=null)return typeFace;

        AssetManager assetManager = context.getAssets();
        typeFace = Typeface.createFromAsset(assetManager, key);
        typefaceMap.put(key,typeFace);
        return typeFace;
    }

    public static Font getFontByKey(Context context, String key) {
        if (mSharedPreferences == null) mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Font font = new Font(context);
        font.setIndex(mSharedPreferences.getInt(context.getString(R.string.key_font_index) + key, -1));
        if (font.getIndex() < 0) {
            setDefaultFonts(context);
            font.setIndex(mSharedPreferences.getInt(context.getString(R.string.key_font_index) + key, -1));
        }

        font.setColor(mSharedPreferences.getInt(context.getString(R.string.key_font_color) + key, -1));
        font.setBackColor(mSharedPreferences.getInt(context.getString(R.string.key_font_back_color) + key, -1));
        font.setSize(mSharedPreferences.getFloat(context.getString(R.string.key_font_size) + key, -1));
        font.setLineHeight(mSharedPreferences.getFloat(context.getString(R.string.key_font_line_height) + key, -1));
        return font;
    }

    public static void setFontByKey(Context context, Font font, String key) {
        if (mSharedPreferences == null) mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(context.getString(R.string.key_font_index) + key, font.getIndex());
        editor.putInt(context.getString(R.string.key_font_color) + key, font.getColor());
        editor.putInt(context.getString(R.string.key_font_back_color) + key, font.getBackColor());
        editor.putFloat(context.getString(R.string.key_font_size) + key, font.getSize());
        editor.putFloat(context.getString(R.string.key_font_line_height) + key, font.getLineHeight());
        editor.commit();

    }

    public Font clone() {
        Font temp = new Font(context,mIndex, mSize, mLineHeight, mColor, mBackColor);
        return temp;
    }

    public int getIndex() {
        return mIndex;
    }

    public Font setIndex(int index) {
        mIndex = index;
        return this;
    }

    public String getFontName(boolean translate) {
        return getFontName(context,mIndex, translate);
    }

    public String getFontName() {
        return getFontName(context,mIndex, false);
    }
    public String getNormalFontName() {
        return getFontNameForWeb(context,mIndex, true);
    }
public boolean isBold(){
    return getFontName().endsWith("bold");
}
    public int getColor() {
        return mColor;
    }

    public Font setColor(int color) {
        mColor = color;
        return this;
    }

    public String getColorString() {
        String color = mColor > 0 ? "#000000" : String.format(Locale.ENGLISH, "#%06X", 0xFFFFFF & mColor);
        return color;
    }

    public int getBackColor() {
        return mBackColor;
    }

    public Font setBackColor(int backColor) {
        mBackColor = backColor;return this;
    }

    public String getBackColorString() {

       return mBackColor > 0 ? "#ffffff" : String.format(Locale.ENGLISH, "#%06X", 0xFFFFFF & mBackColor);
    }

    public float getSize() {
        return mSize;
    }

    public Font setSize(float size) {
        mSize = size;return this;
    }

//	public float getSizePx() {
//		return Display.spToPx(mSize);
//	}

    public float getLineHeight() {
        return mLineHeight;
    }

    public Font setLineHeight(float lineHeight) {
        mLineHeight = lineHeight;return this;
    }

//	public float getLineHeightPx() {
//		return Display.spToPx(mLineHeight);
//	}

    public Typeface getTypeface() {
        return getTypeFace(context,mIndex);
    }

    public static class TextPos {
        public static final String def = "def", h1 = "h1", h2 = "h2", h3 = "h3", p = "p", web = "web", webrtl = "webrtl", small = "small";

        public static String[] getAll() {
            return new String[]{h1, h2, h3, p, web,/* webrtl,*/ small, def};
        }
    }

    @Override
    public String toString() {
        return getFontName();
    }
}
