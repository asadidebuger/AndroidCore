package ir.microsign.contentcore.network;

import android.app.Activity;

import java.util.List;

import ir.microsign.api.Api;
import ir.microsign.api.AppApi;
import ir.microsign.api.object.Response;
import ir.microsign.R;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.database.DataSourceTemp;
import ir.microsign.contentcore.object.BookIndexItem;
import ir.microsign.contentcore.object.CCtrl;
import ir.microsign.contentcore.object.Category;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.Helper;
import ir.microsign.contentcore.object.Marked;
import ir.microsign.contentcore.object.Temp;
import ir.microsign.context.Preference;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.net.DownloadFile;
import ir.microsign.utility.File;
import ir.microsign.utility.Zip;

/**
 * Created by Mohammad on 05/02/2015.
 */
public class ContentUpdaterAll {
//    public int limitOffline = 1000, limitList = 1000;
//    public int counter = 0;
    public boolean canceled = false;
    public ContentUpdateAllListener l;
    public Activity activity;
    public DataSource mDataSource = null;
    CCtrl cCtrl=null;
    boolean start = false;

    public ContentUpdaterAll(Activity activity, ContentUpdateAllListener l) {
        this.activity = activity;
        this.l = l;
    }

//    public String getUrlDownloadOffline() {
//        return "";
////        return ir.microsign.api.network.utility.makeUrl(false, "bookindexlists", "offline");
//    }
//    public String getUrlDownload() {
//        return net.tarnian.api.network.utility.makeUrl(false, "bookindexlists", "offline");
//    }

    public void check() {
        if (checkCancel()){return;}
        l.OnPrepare();
        Api.get("content", "cctrl", new Api.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (checkCancel()) return;
                l.OnPrepared(response.succeed());
				cCtrl= response.getObject(CCtrl.class,"cctrl");
              boolean isNew=  l.OnLastUpdateReceiveEnd(response.succeed(),cCtrl);
//              if (!isNew) l.OnFinished(true);
            }

        },"key",activity.getString(R.string.update_ctrl_update_key));


    }
//    public void updateGetOfflines(CCtrl ctrl) {
//        if (checkCancel()) return;
//        if (ctrl==null||DataSource.getDataSource().getVersion()>=ctrl.version){
//
//        }
//        if (l.OnLastUpdateReceiveEnd(true,ctrl))
//            prepareToStartDownload(ctrl);
////        final ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor(1);
//
//        receiveDbObjects.getDbObjectsStepByStep(activity,new Update(), getUrlDownloadOffline(), params, new Listener.ReceiveObjectListener() {
//            @Override
//            public void onReceiveCompleted(List<BaseObject> objects, int resultState) {
//
//                if (checkCancel()) return;
//
//                if (resultState == 0) {
//                    insertOfflines(objects, false);
//                    Update update= getLastExistUpdate();
//                    if (update==null) {
//                        l.OnLastUpdateReceiveEnd(true,update);
//                        return;
//                    }
//                    if (l.OnLastUpdateReceiveEnd(true,update))
//                    prepareToStartDownload(update);
//                } else l.OnFinished(false);
//            }
//
//            @Override
//            public void onReceiveStep(List<BaseObject> objects, int resultState) {
//                if (checkCancel()) return;
//
////                l.OnCategoriesReceive(receiveDbObjects.mReceived, receiveDbObjects.mTotal);
////                if (resultState > 0) l.OnFinished(false);
////                else if (resultState == 0) insertOfflines(objects, false);
//            }
//        });
//
//    }
public void prepareToStartDownload(){
    l.OnPrepareToUpdateStart();
    List<Marked> lst= (List<Marked>) Helper.getHelper().getDataSource().select(new Marked());
    DataSourceTemp dstemp=DataSourceTemp.getDataSource(activity);
    Temp temp=new Temp();
    dstemp.cleanTable(temp.getTableName(),false);
    for (Marked o:lst)
    dstemp.insert(Temp.fromObject(o));

    l.OnPrepareToUpdateEnd();
    startReceiving(cCtrl);
}
void startReceiving(final CCtrl update){
if (update==null){l.OnFinished(false);return;}
        l.OnDownloadBegin(update.sizeZip);
        DownloadFile.OnDownloadListener l1=new DownloadFile.OnDownloadListener() {
            @Override
            public boolean downloading(int downloaded, int total) {
                l.OnDownloadReceiving(total,downloaded);
                return !checkCancel();
            }

            @Override
            public void OnDownloadCompleted(boolean succeed, String path) {
                if (succeed){l.OnDownloadCompleted();unzip(update,path);}
                else l.OnFinished(false);
            }
        };
        DownloadFile f=new DownloadFile();
//        f.GetFile(1024*5, File.GetRoot(activity,true)+"/temp",update.getUrl(),l1);
        f.downloadFile(AppApi.getDownloadBaseUrl(activity)+update.getUrl(),File.GetRoot(activity,true)+"/temp",1024*5, l1);
    }
//   public Update getLastExistUpdate(){
//        String last=Preference.getString(activity,"last_update_created_date","");
//        Update update=new Update();
//        update= (Update) DataSourceTemp.getDataSource(activity).selectByQueryFirst(new Update(),"SELECT * FROM "+update.getTableName()+" WHERE create>'"+last+"' ORDER BY create DESC LIMIT 1 ");
//        return update;
//
//    }
    void  unzip(final CCtrl update,final String zipFilePath){
        l.OnUnzipStarted();

        Zip zip=new Zip();
        zip.Unzip(zipFilePath,update.getExtractPath(), 1024 * 5, new Zip.OnExtractListener() {
            @Override
            public boolean extracting(int extracted) {
                 l.OnUnziping(update.sizeContent,extracted);
                return(!checkCancel());
            }

            @Override
            public void finished(boolean succeed) {
				File.Delete(zipFilePath);
               if (succeed) {l.OnUnzipEnd();
				   restoreTemps(update);}
                else l.OnFinished(false);

            }
        });

    }
    public void restoreTemps(CCtrl update){
    	Preference.set(activity,"content_db_version",update.version);

        l.OnAfterUnzip();
        List<Temp> lst= (List<Temp>) DataSourceTemp.getDataSource(activity).select(new Temp());
        DataSource dstemp=Helper.getHelper().getDataSource(true);
        for (Temp o:lst)
            dstemp.insert(o.toObject(Marked.class));
//        Preference.set(activity,"last_update_created_date",update.created);

        l.OnFinished(true);

    }
    public void cancel() {
        canceled = true;
    }

    public boolean checkCancel() {
        if (canceled) l.OnCanceled();
        return canceled;
    }

    public BaseObject getSampleItemBookIndexItem() {
        return new BookIndexItem();
    }

    public BaseObject getSampleItemContent() {
        return new Content();
    }

    public BaseObject getSampleItemCategory() {
        return new Category();
    }

    public void insertOfflines(List<BaseObject> objects, boolean preStart) {
        if (preStart) {
            start = true;
            return;
        }

        if (start && objects != null) {
            DataSourceTemp.getDataSource(activity).cleanTable(getSampleItemCategory().getTableName(),false);
            start = false;
        }
        DataSourceTemp.getDataSource(activity).insert(objects, 50000, true, "_id");
    }

    public interface ContentUpdateAllListener {
        public void OnPrepare();

        public void OnPrepared(boolean succeed);

        public void OnLastUpdateReceiveStart();

        public boolean OnLastUpdateReceiveEnd(boolean succeed, CCtrl update);

        public void OnPrepareToUpdateStart();

        public void OnPrepareToUpdateEnd();

        public void OnDownloadBegin(int size);

        public void OnDownloadReceiving(int size,int received);

        public void OnDownloadCompleted();

        public void OnUnzipStarted();

        public void OnUnziping(int size, int unziped);

        public void OnUnzipEnd();

        public void OnAfterUnzip();

        public void OnFinished(boolean succeed);

        public void OnCanceled();
    }
}
