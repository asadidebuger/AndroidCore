package ir.microsign.content.network;

import android.app.Activity;

import java.util.List;

import ir.microsign.content.database.DataSource;
import ir.microsign.contentcore.object.Id;

/**
 * Created by Mohammad on 05/02/2015.
 */
public class ContentUpdater extends ir.microsign.contentcore.network.ContentUpdater {
    public ContentUpdater(Activity context, final String catId, final String catAlias, final ContentUpdateListener l) {
        super(context, catId,catAlias, l);
    }


    public String getUrlArticles(List<Id> ids, int limit, int page) {
//        return "";
        String idS = "";
        int start = limit * page, end = start + limit;
        if (end > ids.size()) end = ids.size();
        for (int i = start; i < end; i++) {
            String id = ids.get(i).id;
            idS += id + ",";
        }
        if (idS.contains(",")) idS = idS.substring(0, idS.length() - 1);
return idS;
//        List<NameValuePair> pairs = new ArrayList<>();
//        pairs.add(new BasicNameValuePair("ids", idS));
//        return ir.microsign.api.network.utility.makeUrl(false, "content", "contents", pairs);
    }

//    public String getUrlArticleList() {
//        return "";
////        return ir.microsign.api.network.utility.makeUrl(false, "content", "bookindexlists");
//    }

    public ir.microsign.contentcore.database.DataSource getDataSource() {
        if (mDataSource == null) mDataSource = DataSource.getDataSource();
        return mDataSource;

    }

//    public BaseObject getSampleItemBookIndexItem() {
//        return new BookIndexItem();
//    }

//    public BaseObject getSampleItemContent() {
//        return new Content();
//    }
}
