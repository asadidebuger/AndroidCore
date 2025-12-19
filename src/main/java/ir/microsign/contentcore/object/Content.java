package ir.microsign.contentcore.object;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ir.microsign.calendar.Calendar;
import ir.microsign.calendar.Date;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.dbhelper.Const;
import ir.microsign.settings.Setting;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/15/14.
 */
public class Content extends BaseObject {
    public static DataSource mDataSource = null;

    public boolean languageIs(String lang){
        if (lang==null) return this.lang ==null;
        return lang.equals(this.lang);
    }
    final String[] mCols = new String[]{ "autoid","_id", "title",  "alias", "intro", "full","lang","images", "cat", "create", "modify", "publish", "access", "published", "featured","order","search", "version"};
    @Override
    public Const.fieldType getSQLFieldType(String name) {
        if (name.equals("_id")) return Const.fieldType.STRING_UNIQUE;
        return super.getFieldType(name);
    }

    public String lang,_id, cat,access, create, modify, publish, intro, full, images, title, alias,search;
    public Integer autoid, version, order ;
   public Boolean featured,published;
    public String mPath = null;
    public View mSearchView = null, mFeatureView = null;
    public List<Image> mImagesIntro = null;
    public List<Image> mImagesFullText = null;
    public List<Image> mImagesAll = null;
    public String mIntro = null;
    public String mIntroSimple = null;
    public String mFullText = null;
    public String mHtmlTitle = null, mHtmlFullText = null, mHtmlItem = null,mExtra=null;

    public static Helper getHelper() {

        return Helper.getHelper();
    }
    public static Helper getHelper(Context context) {

        return Helper.getHelper(context);
    }

    public static String cleanHtml(String html) {
        return Text.CleanHtml(Text.replaceRegex(html, "", "<img[^>]*/>"));
    }

    public static List<Image> getImages(String src) {
       return Image.getImages(getHelper().getContext(),src ,getHelper().getBaseUrl());
//        List<Image> _images = new ArrayList<Image>();
//        if (Text.isNullOrEmpty(src)) return _images;
//        List<String> matches = Text.getMatches(src, "<img[^>]*/>");
//        for (String imageId : matches) {
//            _images.add(new Image(activity,,imageId));
//        }
//        return _images;
    }

    @Override
    public String[] getAllColumns() {
        return mCols;
    }

    @Override
    public String getOrderColumnName() {
        return getHelper().getOrderColumnName();
    }

    @Override
    public boolean getReverseOrder() {
        return getHelper().getReverseOrder();
    }

    public boolean isFeatured() {
        return featured!=null&&featured;
    }


    //	public View getSearchView(Context activity) {
//		if (mSearchView != null) return mSearchView;
//
////		mSearchView=new ContentSearchView(activity,this);
//		return mSearchView;
//
//	}
    public StringBuilder getSearchedHtml(StringBuilder sb, DataSource dataSource, Map<String,String> options, boolean noCase) {
        return getHelper().getSearchedHtml(sb,this, dataSource, options,noCase);
    }

    public String getPath(DataSource dataSource) {

        return getHelper().getPath(this, dataSource);

    }

    //	public stat mShowFirstImage=true;
//    @Override
//    public String[] getExceptionFields() {
//        return new String[]{"mDataSource", "mPath", "mSearchView", "mImagesIntro", "mImagesFullText", "mImagesAll", "mIntro", "mIntroSimple", "mFullText", "mFeatureView", "mShowFirstImage", "mHtmlTitle", "mHtmlFullText", "mHtmlItem", "mPrefix", "mBorderColor", "mHelper","mExtra"};
//    }

    public DataSource getDataSource(Context context) {
        if (mDataSource == null) mDataSource = new DataSource(context);
        return mDataSource;
    }

    public String getIntroText() {
        return getHelper().getIntroText(this);
    }

    public String getSimpleIntroText() {
        if (mIntroSimple != null) return mIntroSimple;
        String intro = cleanHtml(getIntroText());
        mIntroSimple = Text.safeCut(intro, 100, true).replaceAll("[\\n\\r]", "") + "...";
        return mIntroSimple;
    }

    public String getForShare() {
        return cleanHtml(getFull());

    }

    public String getIntroTextIfAvailable() {
        if (Text.isNullOrEmpty(intro)) return null;
        return getIntroText();
    }

    public String getFull() {
        return getHelper().getFulltext(this);
    }
    public String getIntroAndFulltext() {
        String s="";
        if (intro !=null)s= intro;
        if (full !=null)s+= full;
        return s;
    }

    public StringBuilder getHtmlItem(StringBuilder sb,boolean link) {
        return getHelper().getHtmlItem(sb,this, link);
    }

//	public String getHtmlTitle() {
//		return getHelper().getHtmlTitle(this);
//	}

    public String getHtmlFullText(Context context) {

        return getHelper().getHtmlFullText(this, context);
    }


//    public String getAttribs() {
//        return attribs;
////        if (mFarsiTitle==null)
////            mFarsiTitle= Text.getValueInParams(attribs, "fa_title").replace("\\/", "/").replace("&", "&amp;");
////    return mFarsiTitle;
//    }

    public String getFeaturedText() {
        if (mFullText != null) return mFullText;
        mFullText = "";
        if (!isAvailable()) {
            return null;
        }

        if (!Text.isNullOrEmpty(intro)) mFullText += intro;
        if (!Text.isNullOrEmpty(full)) mFullText += "{break}" + full;
        return mFullText;
    }

    public boolean search(String txt, boolean caseSensitivity, boolean inTitle, boolean inIntro, boolean inFullText) {
        boolean result = inIntro || inTitle || inFullText;
        if (!result) return false;
        if (inTitle) result = Text.isContains(title, txt, caseSensitivity);
        if (result) return true;
        if (inIntro) result = Text.isContains(intro, txt, caseSensitivity);
        if (result) return true;
        if (inFullText) result = Text.isContains(full, txt, caseSensitivity);
        return result;
    }

    public String getDate(Context context) {
        Date date = Calendar.fromString(publish, Date.Type.valueOf(Setting.Advance.getCalendarType(context)));
        return date == null ? "" : date.toString();
    }
//	public boolean isAvailable() {
//
//		return full == null || !full.contains("COM_TARNIANAPI_PLUGIN_CONTENTS_CONTENT_YOU_DONT_ACCESS_THIS_ARTICLE");
//	}

    public boolean isAvailable() {
        return full == null || !full.startsWith("YOU_DONT_ACCESS_THIS_ARTICLE");
    }

    public boolean isUserAccess() {
        if (Text.isEmpty(access))return true;
        return isUserAccess();

    }

//    public boolean isUserAccess(User user) {
//        if (user == null)
//            user = utility.getSavedUser(false);
//        if (user == null) return false;//isAvailable();
//        return user.hasAccess(access);
//    }

    @Override
    public String getJsonArrayName() {
        return "articles";
    }

    public String getPageTitle() {
        return title;
    }

    public Image getFirstImage() {
        if (getImagesIntro().size() > 0) return getImagesIntro().get(0);
        if (getImagesFullText().size() > 0) return getImagesFullText().get(0);
        return null;

    }

    public List<Image> getImagesIntro() {
        if (mImagesIntro != null) return mImagesIntro;
        mImagesIntro = getImages(intro);
        return mImagesIntro;
    }

    public List<Image> getImagesFullText() {
        if (mImagesFullText != null) return mImagesFullText;
        mImagesFullText = getImages(full);
        return mImagesFullText;
    }

    public List<Image> getImagesAll() {
        if (mImagesAll != null) return mImagesAll;
        mImagesAll = new ArrayList<Image>();
        mImagesAll.addAll(getImagesIntro());
        mImagesAll.addAll(getImagesFullText());
        return mImagesAll;
    }

    public boolean haveImage() {
        return haveImageIntro() || haveImageFulltext();
    }

    public boolean haveImageFulltext() {
        return (getImagesFullText().size() > 0);
    }

    public boolean haveImageIntro() {
        return (getImagesIntro().size() > 0);
    }

    public Image getThumbImage() {
        if (haveImageIntro()) return getImagesIntro().get(0);
        if (haveImageFulltext()) return getImagesFullText().get(0);
        return null;
    }

    @Override
    public String getViewPath(Context context) {
        return getHelper().getViewPath(isFeatured());
    }

    @Override
    public boolean equals(Object o) {
        return !(o == null || !(o instanceof Content)) && (this._id.equals(((Content) o)._id));
    }

    @Override
    public String toString() {
        return title;
    }
}
