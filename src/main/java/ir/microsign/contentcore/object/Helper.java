package ir.microsign.contentcore.object;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import ir.microsign.api.Utils;
import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.Profile;
import ir.microsign.R;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.view.CategoryView;
import ir.microsign.context.Application;
import ir.microsign.context.Preference;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 25/10/2015.
 */
public class Helper {
//    static String mBorderColor = null;
//
//    static String getBorderColor() {
//
//        if (mBorderColor == null)
//            mBorderColor = "#000000";// Graphics.getColorString(activity.getContext().getResources().getColor(R.color.color_headlines_right_border));
//        return mBorderColor;
//    }

   public static DataSource mDataSource=null;
   public  DataSource getDataSource(boolean reset){
        if (reset||mDataSource==null)mDataSource=new DataSource(context);
        return mDataSource;
    }
    public  DataSource getDataSource(){
        return getDataSource(false);
    }
//    public static Helper mHelper = null;
   static  Context context;

    public  Context getContext() {
        return context;
    }

//    public  void setContext(Context activity) {
//        Helper.activity = activity;
//    }

    public static Helper getHelper() {

        return getHelper(null);
    }

  static   Map<String,Helper> helperMap=new HashMap<>();

    public static void setContext(Context context) {
        if (context != null) return;
        Helper.context = context;
    }

    public static Helper getHelper(Context context) {
        if (context != null)
            Helper.context = context;
//        else activity
        if (Helper.context == null) {

            Log.e("ContentHelper", "context is null!");
            Helper.context= Application.getContext();
//            return null;
        }
        try {
            String cls = Helper.context.getString(R.string.content_helper_path);
            Helper h = helperMap.get(cls);
            if (h != null) return h;
            h = (Helper) Class.forName(cls).getConstructors()[0].newInstance();
            helperMap.put(cls, h);
            return h;

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("ContentHelper", "Helper is null!");
        return null;
    }
    public String getDbSuffix() {
        return "_core";
    }
    public int getDbVersion() {
        return Preference.getInteger(getContext(),"content_db_version",3);
    }
    public void onDbUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion <= oldVersion) return;
        Object[] objects= getDBNewObjects();
        for (Object o : objects) {
            db.execSQL(((BaseObject) o).getDropTable());
            db.execSQL(((BaseObject) o).getCreateTable());
        }
    }
    public String getBaseUrl() {
        return Utils.getApiUrl();
    }

    public Object[] getDBNewObjects() {
        return new Object[]{new Content(), new Category(), new BookIndexItem(), new Marked(),new Suggest()};
    }
    public void afterInsertContentsToDB(List<ir.microsign.dbhelper.object.BaseObject> objects, boolean isEmpty){

    }
    public void beforeInsertContentsToDB(List<ir.microsign.dbhelper.object.BaseObject> objects, boolean isEmpty){

    }
    public List<Id> getUpdatedListAndClearUnused(ir.microsign.dbhelper.database.DataSource dataSource) {

        Profile user = DataSourceApi.getSource(dataSource.getContext()).getProfile();//.getSavedUser(false);
        String access = null;
        if (user != null) access = user.permission.replace('[','(').replace(']',')');
        String sqlDeleteUnpublishedContents = "DELETE FROM contents WHERE contents._id IN (SELECT bookindexitems._id AS id FROM bookindexitems  WHERE bookindexitems.[published]<1)";
        String sqlDeleteUnpublishedIndexess = "DELETE FROM bookindexitems WHERE bookindexitems.[published]>0";

        String query = "SELECT bookindexitems._id AS id, contents.full AS ft, contents.version AS cversion FROM bookindexitems LEFT JOIN contents ON bookindexitems._id=contents._id  WHERE ( (cversion is null OR bookindexitems.version>cversion)"
//		+((access!=null)?" AND bookindexitems.[access] in " +access+") OR (ft LIKE '%YOU_DONT_ACCESS_THIS_ARTICLE%' AND bookindexitems.[access] in "+access+")":")");
                + ((access != null) ? ") OR (ft LIKE '%YOU_DONT_ACCESS_THIS_ARTICLE%' AND bookindexitems.[access] in " + access + ")" : ")");
        dataSource.execSQL(sqlDeleteUnpublishedContents);
        dataSource.execSQL(sqlDeleteUnpublishedIndexess);
        return (List<Id>) dataSource.selectByQuery(new Id(), query);
//		}

    }
    public Image getCategoryFirstImage(Category category, boolean thumb) {
        if (category.mFirstImage != null) return category.mFirstImage;
        if (category.getImages().size() < 1) return null;
        category. mFirstImage = category.getImages().get(0);
//        category.mFirstImage.mType = thumb ? Image.THUMB : Image.NORMAL;

        return category.mFirstImage;
    }
    public List<Image> getCategoryImages(Category category) {
        if (category.mImages != null) return category.mImages;
        category.mImages = Content.getImages(category.description);
        return category.mImages;
    }
    public String getOrderColumnName() {
        return "order";
    }
public String getIndexOrderColumnName(){
    return "order";
}

    public boolean getReverseOrder() {
        return false;
    }

    public String getPrefix() {
        return "book";
    }
 public String makeAddress(String contentId, String tag, int index) {
        return String.format(Locale.ENGLISH,getPrefix()+":article.%s.%s.%d", contentId, tag, index);
    }
    public String makeAddress(String catId) {
        return String.format(Locale.ENGLISH,getPrefix()+":category.%s",catId);
    }

    public String getAction() {
        return "ir.microsign.bookcore";
    }

    public String getPaymentAlert(String link) {
        return String.format(Locale.ENGLISH, Text.ReadResTxt(context,R.raw.payment_alert), link);
    }

    public String getUpdateAlert(String link) {
        return String.format(Locale.ENGLISH, Text.ReadResTxt(context,R.raw.update_alert), link);
    }

    public StringBuilder getHtmlItem(StringBuilder sb,Content mContent, boolean link) {
        return getDiv(sb,mContent.title, false, "#000", "transparent", "none", "none", "none", "none");
    }
public Content getContentFromCategoryDescription(Category category){
    Content content= new Content();
//    content.title=category.title;
    content.intro ="";
    if (!category.isUserAccess())content.intro =String.format(Locale.ENGLISH,Text.ReadResTxt(context,R.raw.payment_alert),category.title,getPaymentLink(category.access));
    if (category.description!=null)content.intro +=category.description;
    return content;
}
    public String getPath(Content mContent, DataSource dataSource) {
        if (mContent.mPath != null) return mContent.mPath;
        Category category = dataSource.getCategory(mContent.cat);
        mContent.mPath = category.getPath();
        String[] parts = mContent.mPath.split("/");
        mContent.mPath = "";
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (!part.contains("-")) {
                try {
                    int id = dataSource.getContext().getResources().getIdentifier(part, "string", dataSource.getContext().getPackageName());
                    if (id > 0) part = dataSource.getContext().getString(id);
                } catch (Exception e) {e.printStackTrace();}
            } else part = part.replace("-", " ");
            if (!Text.isNullOrEmpty(part))
                mContent.mPath += part + "/";
        }
        BookIndexItem bookIndexItem = dataSource.getBookIndexItemByContentId(mContent._id);
        List<String> stack = new ArrayList<>();
        while (bookIndexItem != null) {
            stack.add(bookIndexItem.title);
            if (Text.isEmpty(bookIndexItem.parent_id)) break;
            bookIndexItem = dataSource.getBookIndexItems(bookIndexItem.parent_id);
        }
        for (int i = stack.size() - 1; i >= 0; i--) {
            String s = stack.get(i);
            mContent.mPath += s + "/";
        }
        return mContent.mPath;

    }

    public StringBuilder getSearchedHtml(StringBuilder sb, Content mContent, DataSource dataSource, Map<String,String> options, boolean noCase) {
        String txt = null;
        int matched=0;
        Set<Map.Entry<String,String>> set= options.entrySet();
        for (Map.Entry<String, String> entry : set) {
            boolean isRtl = entry.getKey().equals("attribs");
            if (!isRtl)
                txt = (String) mContent.getValue(entry.getKey());
            else {
                txt = Text.getValueInParams(mContent.search, (entry.getValue()).split("\"")[1].replace("\\", "").replace(".", "_"));
                String pair0 = entry.getValue();
                pair0 = pair0.substring(pair0.indexOf(":") + 1).replace("\\\"", "");
                entry.setValue(pair0);
//                pair = new NameValuePair("attribs", pair0);
            }
            if (txt == null) continue;
            String link = String.format(Locale.ENGLISH, getPrefix() + ":article.%s", mContent._id);
            String pattern = entry.getValue();
            if (noCase) pattern = pattern.toLowerCase();
            if (pattern.matches(".*<.*>.*")/*&&(pair.getName().equals("full") || pair.getName().equals("intro"))*/) {
//				String tag = (String) pair.getValue();
                String tag = pattern.substring(pattern.indexOf('<') + 1, pattern.indexOf(".*", 2));
                Document doc = ir.microsign.utility.Document.getDocument(txt, "body");
                NodeList tags = doc.getElementsByTagName(tag);

                for (int i = 0; i < tags.getLength(); i++) {
                    String content = ir.microsign.utility.Document.getNodeString(new StringBuilder(), tags.item(i)).toString();
                    if (!Text.isMatchByRegex(noCase ? content.toLowerCase() : content, entry.getValue()))
                        continue;
                    sb.append("<a class=\"search-item\" href=\"").append(link).append(".").append(tag).append(".").append(i).append("\">").append(content).append("</a>");
                }
            } else {

                if ((entry.getKey().equals("full") || entry.getKey().equals("intro"))) {
                    pattern = pattern.replace(".*", "");


                    txt = txt.replaceAll("<.?(br|BR) *?/?>", " ");
String tmp=txt.replaceAll("<.*?/>", "");
                    if ( Text.getMatches(tmp, "<[^/]*?>").size()== Text.getMatches(tmp, "</.*?>").size())
                        pattern = String.format(Locale.ENGLISH, "[^>]*<[^<]*.*?%s.*?<[^>]*>", pattern);
                    else {
                        txt = txt.replaceAll("<.*?>", "");
                        String ex = "[^.:,\\\\/)(!\"'}{?" + "؛،؟" + "]*";
                        pattern = String.format(Locale.ENGLISH, "%s%s%s" ,ex, pattern,ex);
//                        pattern = String.format(Locale.ENGLISH, ".*%s.*" , pattern);
                    }
                }

                List<String> matches = Text.getMatches(noCase ? txt.toLowerCase() : txt, pattern);

                if (matches.size() < 1) continue;
                matched+=matches.size();
                for (int i = 0, l = matches.size(); i < l; i++) {
                    if (Text.isNullOrEmpty(matches.get(i))) continue;
                    sb.append("<a class=\"search-item ").append((isRtl ? "rtl" : "ltr")).append("\" href=\"").append(link).append("\">");
                    if (l > 1)
                        sb.append("<span class=\"search-number\">").append(i + 1).append("</span>");
                    sb.append(matches.get(i)).append("</a>");
                }
            }
        }
        if (matched<1)return sb;
            sb.append("<div class=\"path search\">").append(getPath(mContent, dataSource)).append("</div>");
        return sb;
    }

    public String getIntroText(Content content) {

        if (content.mIntro != null) return content.mIntro;

        content.mIntro = content.intro;
        return content.mIntro;

    }

    public String getDefaultSearchOptions() {
        return "title.*%s*,intro.*%s*,full.*%s*";
    }

    public String getFulltext(Content mContent) {
        if (mContent.mFullText != null) return mContent.mFullText;
        mContent.mFullText = "";
        if (!mContent.isAvailable()) {
            if (!Text.isNullOrEmpty(mContent.intro)) mContent.mFullText = "";
            return mContent.mFullText;
        }

        if (!Text.isNullOrEmpty(mContent.intro)) mContent.mFullText += mContent.intro;
        if (!Text.isNullOrEmpty(mContent.full)) mContent.mFullText += mContent.full;
//		mFullText=Text.replaceRegex(mFullText,"","\\{images=\\d+\\}");
        return mContent.mFullText;
    }

    public String getHtmlFullText(Content mContent, Context context) {
        return "";
    }


    public String getPaymentLink(Content mContent) {
        return String.format(Locale.ENGLISH, "%s:payment:%d", getPrefix(), mContent.access);
    }
    public String getPaymentLink(String access) {
        return String.format(Locale.ENGLISH, "%s:payment:%d", getPrefix(),access);
    }

    public String getUpdateLink(Content mContent) {
        return String.format(Locale.ENGLISH, "%s:update:%d", getPrefix(), mContent.cat);
    }

    public String getHtmlTitle(Content mContent) {
        if (mContent.mHtmlTitle != null) return mContent.mHtmlTitle;
        mContent.mHtmlTitle = mContent.title;
        return mContent.mHtmlTitle;

    }

//    public StringBuilder getParagraph(StringBuilder sb,String title, boolean rtl, String padding, String margin) {
////        StringBuilder sb = new StringBuilder();
//        sb
//                .append("<p style=\"margin:").
//                append(margin).
//                append(";padding:").
//                append(padding).
//                append(";text-align:").
//                append(rtl ? "right" : "left").
//                append(";float:").
//                append(rtl ? "right" : "left").
//                append(";\" dir=\"").append(rtl ? "rtl\" class=\"rtl\">" : "ltr\">").append(title).append("</p>");
//
//        return sb;
//    }

//    public String getLink(String content, String link, String extraStyle) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<a style=\"").append(extraStyle).append("\" href=\"").append(link).append("\">").append(content).append("</a>");
//        return sb.toString();
//    }
//public StringBuilder getLink(StringBuilder sb,String content, String link, String clazz) {
////    StringBuilder sb = new StringBuilder(content.length()+link.length()+(clazz==null?25:clazz.length()+25));
//    sb.append("<a ");
//    if (!Text.isNullOrEmpty(clazz))sb.append("class=\"").append(clazz).append("\" ");
//    sb.append("href=\"").append(link).append("\">").append(content).append("</a>");
//    return sb;
//}
    public StringBuilder getDiv(StringBuilder sb,String title, boolean rtl, String color, String bkg, String fontSize, String lineHeight, String padding, String margin) {
        return getDiv(sb,title, rtl, color, bkg, fontSize, lineHeight, padding, margin, "none", "none", "none");
    }

    public StringBuilder getDiv(StringBuilder sb,String title, boolean rtl, String color, String bkg, String fontSize, String lineHeight, String padding, String margin, String borderWidth, String borderStyle, String borderColor) {
//        StringBuilder sb = new StringBuilder();
        sb
                .append("<div style=\"width:100%;display:inline-block;font-size:")
                .append(fontSize).
                append(";margin:").
                append(margin).
                append(";padding:").
                append(padding).
                append(";text-align:").
                append(rtl ? "right" : "left").
                append(";background-color:").
                append(bkg).
                append(";color:").
                append(color).
                append(";line-height:").
                append(lineHeight).
                append(";border-width:").
                append(borderWidth).
                append(";border-style:").
                append(borderStyle).
                append(";border-color:").
                append(borderColor).
                append(";\" dir=\"").append(rtl ? "rtl\">" : "ltr\">").append(title).append("</div>");
        return sb;
    }

    public String getViewPath(boolean featured) {
        return featured ? "ir.microsign.bookcore.view.ContentView" : "ir.microsign.bookcore.view.ContentView";
    }
    public Class<?> getCategoryViewClass(){
        return CategoryView.class;
    }
    public String getSearchQuery(Map<String,String> options, String catId, String txt, int limit, int offset) {
        if ((Text.isNullOrEmpty(txt))) return null;

        if (options.size() < 1) return null;
		 StringBuilder sb = new StringBuilder(250);
		if (Text.notEmpty(catId))sb.append("SELECT C0.* FROM contents AS C0 LEFT JOIN categories AS C1 ON C0.cat=C1._id  WHERE ( C1.parents LIKE '%\"").append(catId).append("\"%' OR C0.cat='").append(catId).append("') AND(");
		else  sb.append("SELECT * FROM contents AS C0 WHERE (");
//        if (catId>0) sb.append("cat=%d) AND (").append(catId);
        Set<Map.Entry<String, String>>nameValuePairs= options.entrySet();
        int i=0;
        for (Map.Entry<String, String> nameValuePair : nameValuePairs) {
//             nameValuePair = options.get(i);
            sb.append(getLikeForSearch("C0."+nameValuePair.getKey(),  nameValuePair.getValue()));
            if (i<nameValuePairs.size()-1)sb.append(" OR ");
            i++;
        }
//        for (int i = 0,ilimit=options.size(); i <ilimit ; i++) {
//            NameValuePair nameValuePair = options.get(i);
//            sb.append(getLikeForSearch(nameValuePair.getName(), (String) nameValuePair.getValue()));
//            if (i<ilimit-1)sb.append(" OR ");
//        }
        sb.append(") ");
        if (limit > 0) sb.append(" LIMIT ").append( limit);
        if (offset > -1) sb.append(" OFFSET ").append(offset);
        return  sb.toString();

    }
    public static String getLikeForSearch(String col, String input) {
        return col + " LIKE " + input;

    }
    Content mSampleContent=null;
    Content mSampleContent1=null;
    public BaseObject getContentSampleForQuery(boolean forceNew) {
        if (forceNew||mSampleContent==null)
        mSampleContent= new Content();
        return mSampleContent;
    }    public BaseObject getContentSampleForQuery() {
        return getContentSampleForQuery(false);
    }
    public BaseObject getContentSampleForSearch() {
        if (mSampleContent1==null)
            mSampleContent1= new Content();
        return mSampleContent1;
    }
    public BaseObject getCategorySample() {
        return new Category();
    }public BaseObject getContentSample() {
        return new Content();
    }public BaseObject getBookIndexSample() {
        return new BookIndexItem();
    }
}
