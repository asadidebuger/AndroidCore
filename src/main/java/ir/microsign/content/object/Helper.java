package ir.microsign.content.object;

import android.content.Context;

import java.util.List;
import java.util.Locale;

import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.Profile;
import ir.microsign.content.view.CategoryView;
import ir.microsign.content.view.FullContentView;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.object.Category;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.Id;
import ir.microsign.contentcore.object.Suggest;
import ir.microsign.context.Preference;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 25/10/2015.
 */
public class Helper extends ir.microsign.contentcore.object.Helper {

public FullContentView getFullContentView(Context context, Content mContent){
    return new FullContentView(context, mContent);
}
    public String getDbSuffix() {
        return "_content";
    }

    @Override
    public List<Id> getUpdatedListAndClearUnused(ir.microsign.dbhelper.database.DataSource dataSource) {
        Profile user = DataSourceApi.getSource(dataSource.getContext()).getProfile();//.getSavedUser(false);
        String access = null;
        if (user != null) access = user.permission.replace('[','(').replace(']',')');
//        List<Id> ids = new ArrayList<>();
        String sqlDeleteUnpublishedContents = "DELETE FROM contents WHERE contents._id IN (SELECT bookindexitems._id AS _id FROM bookindexitems  WHERE bookindexitems.[published]<1)";
        String sqlDeleteUnpublishedIndexess = "DELETE FROM bookindexitems WHERE bookindexitems.[published]<1";

        String query = "SELECT bookindexitems._id AS id, contents.full AS ft, contents.version AS cversion FROM bookindexitems LEFT JOIN contents ON bookindexitems._id=contents._id  WHERE ( (cversion is null OR bookindexitems.version>cversion)"
//		+((access!=null)?" AND bookindexitems.[access] in " +access+") OR (ft LIKE '%YOU_DONT_ACCESS_THIS_ARTICLE%' AND bookindexitems.[access] in "+access+")":")");
                + ((access != null) ? ") OR (ft LIKE '%YOU_DONT_ACCESS_THIS_ARTICLE%' AND bookindexitems.[access] in " + access + ")" : ")");
        dataSource.execSQL(sqlDeleteUnpublishedContents);
        dataSource.execSQL(sqlDeleteUnpublishedIndexess);
        return  (List<Id>) dataSource.selectByQuery(new Id(), query);
//		}
//        return ids;

    }

    public static DataSource mDataSource=null;
    public DataSource getDataSource(boolean reset){
        if (reset||mDataSource==null){
            mDataSource=new ir.microsign.content.database.DataSource( getContext());
        }
        return mDataSource;
    }
    public  DataSource getDataSource(){
        return getDataSource(false);
    }
    @Override
    public int getDbVersion() {
        return Preference.getInteger(getContext(),"content_db_version",4);
    }

    public Object[] getDBNewObjects() {
        return new Object[]{new ir.microsign.content.object.Content(), new Category(), new BookIndexItem(),new Suggest()};
    }
    public String getOrderColumnName() {
        return null;
    }

    public boolean getReverseOrder() {
        return false;
    }

    public String getPrefix() {
        return "content";
    }

    public String getAction() {
        return "net.tarnian.content";
    }


    public String getPath(Content mContent, DataSource dataSource) {
        if (mContent.mPath != null) return mContent.mPath;
//		Category category=;
        mContent.mPath = dataSource.getCategory(mContent.cat).getPath()+"/"+mContent.title;//category.getPath();
//        String[] parts = mContent.mPath.split("/");
//        mContent.mPath = "";
//        for (String part : parts) {
//            if (!part.contains("-")) {
//                try {
//                    int id = dataSource.getContext().getResources().getIdentifier(part, "string", dataSource.getContext().getPackageName());
//                    if (id > 0) part = dataSource.getContext().getResources().getString(id);
//                } catch (Exception e) {
//
//                }
//            } else part = part.replace("-", " ");
//            if (!Text.isNullOrEmpty(part))
//                mContent.mPath += part + "/";

//        }
        return mContent.mPath;
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
//    public Price getPayment(String accessLevel) {
//        Payment payment;
//        payment = ir.microsign.payment.network.utility.getSavedPayment("useraccesslevels=" + String.format(Locale.ENGLISH, "%d", accessLevel));
//        return payment;
//    }

    public String getPaymentLink(Content mContent) {
        return String.format(Locale.ENGLISH, "%s:payment:%d", getPrefix(), mContent.access);
    }

    public String getUpdateLink(Content mContent) {
        return String.format(Locale.ENGLISH, "%s:update:%d", getPrefix(), mContent.cat);
    }


    public String getViewPath(boolean featured) {
        return featured ? "ir.microsign.content.view.FullContentView" : "ir.microsign.content.view.ContentView";
    }
    public Class<?> getCategoryViewClass(){
        return CategoryView.class;
    }
//    public String getLink(String content, String link, String extraStyle) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<a style=\"").append(extraStyle).append("\" href=\"").append(link).append("\">").append(content).append("</a>");
//        return sb.toString();
//    }

    public String getIntroText(Content content) {

        if (content.mIntro != null) return content.mIntro;
        if (!content.isAvailable()) {
            content.mIntro = "";
            return content.mIntro;
        }
        if (!Text.isNullOrEmpty(content.intro)) {
            content.mIntro = Content.cleanHtml(content.intro).replaceAll("[\\n\\r]", "");
            return content.mIntro;
        }
        content.mIntro = Text.safeCut(content.full, 300, true);
        content.mIntro = Content.cleanHtml(content.mIntro).replaceAll("[\\n\\r]", "");
        return content.mIntro;
    }

//    public BaseObject getCategorySample() {
//        return new Category();
//    }
    public BaseObject getContentSample() {
        return new ir.microsign.content.object.Content();
    }
    public BaseObject getBookIndexSample() {
        return new BookIndexItem();
    }
}
