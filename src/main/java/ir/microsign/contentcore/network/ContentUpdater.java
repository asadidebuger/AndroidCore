package ir.microsign.contentcore.network;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.api.Api;
import ir.microsign.api.object.Response;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.Id;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Text;


/**
 * Created by Mohammad on 05/02/2015.
 */
public class ContentUpdater {
    public int limitCat = 1000, limitList = 1000;
    public int counter = 0;
    public boolean canceled = false;
    public ContentUpdateListener l;
    public Activity activity;
    public String catId = null,catAlias=null;
    public DataSource mDataSource = null;
    boolean start = false;

    public ContentUpdater(Activity activity, String catId, String catAlias, ContentUpdateListener l) {
        this.catId = catId;
        this.catAlias = catAlias;
        this.activity = activity;
        this.l = l;
    }

//    public String getUrlCategories() {
//        return ir.microsign.api.network.utility.makeUrl(false, "categories", "categories");
//    }

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
//
//        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        pairs.add(new BasicNameValuePair("ids", idS));
//        return ir.microsign.api.network.utility.makeUrl(false, "content", "articles", pairs);
    }

    public String getUrlArticleList() {
        return "";
//        return ir.microsign.api.network.utility.makeUrl(false, "bookindexlists", "bookindexlists");
    }

    public void start() {
        updateByCat();
    }

    public void cancel() {
        canceled = true;
    }

    public boolean checkCancel() {
        if (canceled) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    l.OnCanceled();
                }
            });
        }
        return canceled;
    }

    public void updateContentsPrepare() {
        if (checkCancel()){return;}
        l.OnPrepare();
        updateContentsGetCats();
//        ir.microsign.api.network.utility.getBaseParams(false, new ReceiverAdaptor.ParamsReceived() {
//            @Override
//            public void OnReceived(List<NameValuePair> params, boolean success) {
//                if (checkCancel()) return;
//                l.OnPrepared(success);
//                if (success) updateContentsGetCats(params);
//                else l.OnFinished(false);
//            }
//        });

    }

    public DataSource getDataSource() {
        if (mDataSource == null) mDataSource = new DataSource(activity);
        return mDataSource;

    }

    public void updateContentsGetCats() {
        if (checkCancel()) return;
        l.OnCategoriesReceive(0, 0);
        ArrayList<String> args=new ArrayList();
        if (Text.notEmpty(this.catId)){
            args.add("parent");
            args.add(this.catId);

        } if (Text.notEmpty(this.catAlias)){
            args.add("alias");
            args.add(this.catAlias);

        }
        String[] arg=new String[]{};
        Api.get("content", "cat", new Api.onResponseListener() {
            @Override
            public void onResponse(final Response response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.succeed()) {
                            failed();
                            return;
                        }
                         List<BaseObject> list = response.getObjectArray(getSampleItemCategory().getClass(), "cat");
                        if (checkCancel()) return;
                        insertCats(list, true);
                        insertCats(list, false);

                        l.OnCategoriesReceiveEnd(list.size());


                        if (checkCancel()) return;
                        updateContentsGetLists();
                    }
                });
            }
        },args.toArray(arg));
//        final ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor(limitCat);
//        insertCats(null, true);
//        receiveDbObjects.getDbObjectsStepByStep(activity, getSampleItemCategory(), getUrlCategories(), params, new Listener.ReceiveObjectListener() {
//            @Override
//            public void onReceiveCompleted(List<BaseObject> objects, int resultState) {
//
//                if (checkCancel()) return;
//                l.OnCategoriesReceiveEnd(receiveDbObjects.mTotal);
//
//
//                if (resultState == 0) {
//                    insertCats(objects, false);
//                    updateContentsGetLists(params);
//                } else l.OnFinished(false);
//            }
//
//            @Override
//            public void onReceiveStep(List<BaseObject> objects, int resultState) {
//                if (checkCancel()) return;
//                l.OnCategoriesReceive(receiveDbObjects.mReceived, receiveDbObjects.mTotal);
//                if (resultState > 0) l.OnFinished(false);
//                else if (resultState == 0) insertCats(objects, false);
//            }
//        });

    }
   public void failed(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                l.OnFinished(false);
            }
        });
    }
    public void updateContentsGetLists() {

        if (checkCancel()) return;
        l.OnListReceive(0, 0);
        Api.get("content", "list", new Api.onResponseListener() {
            @Override
            public void onResponse(final Response response) {
                activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                if (!response.succeed()) {
                    failed(); return;
                }
                 List<BaseObject> list = response.getObjectArray(getSampleItemBookIndexItem().getClass(), "list");
                if (checkCancel()) return;
                getDataSource().cleanTable(getSampleItemBookIndexItem().getTableName(), false);
                getDataSource().insert(list, 50000, false, null);

                        l.OnListReceiveEnd(list.size());

                if (checkCancel()) return;

                List<Id> updatedIds = Content.getHelper().getUpdatedListAndClearUnused(getDataSource());

                        l.OnUpdatableContentsProceed(updatedIds.size());
                        if (updatedIds.size() < 1) {
                            l.OnFinished(true);
                            return;
                        }
                    updateContentsGetContents(updatedIds);



            }
        });

            }
        });

    }
    public  boolean ContentISEmpty(){
         return DataSource.getDataSource().isTableEmpty("contents");
    }
    public void updateContentsGetContents(final List<Id> updatedIds) {
        if (checkCancel()) return;
       String ids= getUrlArticles(updatedIds,updatedIds.size(),0);
        l.OnContentReceive(0,updatedIds.size());

        Api.get("content", "content", new Api.onResponseListener() {
            @Override
            public void onResponse(final Response response) {activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!response.succeed()) {
                    failed(); return;
                }
                List<BaseObject> list = response.getObjectArray(getSampleItemContent().getClass(), "content");
                if (checkCancel()) return;
                 boolean isEmpty=ContentISEmpty();
                 l.OnContentReceiveEnd(list.size());
                insertContentsToDB(list,isEmpty);

                    l.OnFinished(true);

//                if (checkCancel()) return;
//
//                List<Id> updatedIds = Content.getHelper().getUpdatedListAndClearUnused(getDataSource());
//                l.OnUpdatableContentsProceed(updatedIds.size());
//                if (updatedIds.size() < 1) {
//                    l.OnFinished(true);
//                }
//                updateContentsGetContents(updatedIds);
                }
            });
            }
        },"ids",ids);
//        final int limitContent = Setting.Connection.getParallelReceive(activity);
//        counter = 0;
//        final boolean isEmpty=ContentISEmpty();
//        ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor(limitContent);
//        final Listener.ReceiveObjectListener l2 = new Listener.ReceiveObjectListener() {
//            @Override
//            public void onReceiveCompleted(List<BaseObject> objects, int resultState) {
//                if (checkCancel()) return;
//                if (resultState > 0 || objects == null) {
//                    l.OnFinished(false);
//                    return;
//                }
//
//               insertContentsToDB(objects,isEmpty);
//                counter++;
//                int last = Math.min(counter * limitContent, updatedIds.size());
//
//
//                if (last >= updatedIds.size()) {
//                    l.OnContentReceiveEnd(updatedIds.size());
//                    l.OnFinished(true);
//                    return;
//                }
//                l.OnContentReceive(last, updatedIds.size());
//                ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor(limitContent);
//                receiveDbObjects.getDbObjectsStepByStep(activity, getSampleItemContent(), getUrlArticles(updatedIds, limitContent, counter), params, this);
//            }
//
//            @Override
//            public void onReceiveStep(List<BaseObject> objects, int resultState) {
//                insertContentsToDB(objects,isEmpty);
//                if (checkCancel()) return;
//            }
//        };
//        receiveDbObjects.getDbObjectsStepByStep(activity, getSampleItemContent(), getUrlArticles(updatedIds, limitContent, 0), params, l2);
    }
public void insertContentsToDB(List<BaseObject> objects,boolean isEmpty){
    Content.getHelper().beforeInsertContentsToDB(objects,isEmpty);
    getDataSource().insert(objects,50000,!isEmpty,"_id");
    Content.getHelper().afterInsertContentsToDB(objects,isEmpty);
}
    public void updateByCat() {
        updateContentsPrepare();

    }

    public BaseObject getSampleItemBookIndexItem() {
        return Content.getHelper().getBookIndexSample();
    }

    public BaseObject getSampleItemContent() {
        return  Content.getHelper().getContentSample();
    }

    public BaseObject getSampleItemCategory() {
        return Content.getHelper().getCategorySample();
    }

    public void insertCats(List<BaseObject> objects, boolean preStart) {
        if (preStart) {
            start = true;
            return;
        }

        if (start && objects != null) {
            getDataSource().cleanTable(getSampleItemCategory().getTableName(),false);
            start = false;
        }
        getDataSource().insert(objects, 50000, true, "_id");
    }

    public interface ContentUpdateListener {
        public void OnPrepare();

        public void OnPrepared(boolean succeed);

        public void OnCategoriesReceive(int last, int total);

        public void OnCategoriesReceiveEnd(int total);

        public void OnUpdatableContentsProceed(int total);

        public void OnContentReceive(int last, int total);

        public void OnContentReceiveEnd(int total);

        public void OnListReceive(int last, int total);

        public void OnListReceiveEnd(int total);

        public void OnFinished(boolean succeed);

        public void OnCanceled();
//        public void OnError(String e);
    }
}
