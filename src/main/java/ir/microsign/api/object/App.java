package ir.microsign.api.object;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.List;

import ir.microsign.api.Api;
import ir.microsign.api.Utils;
import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.view.AppView;
import ir.microsign.api.view.LargeAppView;
import ir.microsign.dbhelper.Const;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.dialog.WaitingDialog;
import ir.microsign.R;
import ir.microsign.net.Utility;
import ir.microsign.utility.File;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;

public class App extends BaseObject {
    public String _id,name,icon,description,market,pkg,url,api,dlUrl,version,release,lastUpdate,developer,changeLog,license;
    public Integer versionCode;
    public Long insert_date;

    public Developer getDeveloper(){
        return (Developer) new Developer().initFormJSON(developer);
    }
    public List<Market> getMarkets(){
        return Market.fromJson(market);
    }
    public List<ChangeLog> getChangeLogs(){
        return ChangeLog.fromJson(changeLog);
    }
    @Override
    public Class getViewClass() {
        return AppView.class;
    }
public View getLargeView(Context context){
        return new LargeAppView(context,this);
}
    @Override
    public Const.fieldType getSQLFieldType(String name) {
        if (name.equals("_id")) return Const.fieldType.STRING_UNIQUE;
        return super.getFieldType(name);
    }

    public String getDescription(Context context) {
        return context.getString(R.string.txt_last_update)+":"+Utils.getDate(lastUpdate,false)+
                "\r\n"+context.getString(R.string.txt_last_version)+":"+version;
    }

    public String getHtml(Context context){
        String html=  Text.ReadResTxt(context, R.raw.app);

        List<Market> markets=getMarkets();
        List<ChangeLog> changeLogs=getChangeLogs();

        String marketHtml="";
        if (markets.size()>0) {
            marketHtml = "<h3>" + context.getString(R.string.txt_intro_download_from_markets) + "</h3>";
            for (Market market : markets) {
                marketHtml += market.getHtml(context);
            }
        }
        String changeLog="";
        if (changeLogs.size()>0)changeLog="<h3>"+context.getString(R.string.txt_intro_change_history_title)+"</h3>";
        for (ChangeLog change:changeLogs){
            changeLog+=change.getHtml(context);
        }
        String _url="";
        if (!Text.isNullOrEmpty(url))_url="<a href=\""+url+"\" target=\"_blank\">"+context.getString(R.string.txt_app_url_title)+"</a>";
        html= html.replace("__name",name).replace("__version",version).replace("__date",Utils.getDate(lastUpdate,false)).replace("__market",marketHtml).replace("__url",_url).replace("__history",changeLog).replace("__description",description!=null?description:"");
//        if (Text.notEmpty(license))html+="<hr><div class=\"info\" ><b>"+context.getString( )+"</b><br></div>"+license;



        return html;
    }
    public static void setImage(final ImageView imageView, final View waiting, String url, String icon) {
        Image.setImage(imageView, BitmapFactory.decodeResource(imageView.getContext().getResources(),R.drawable.ic_microsign_watermark),waiting,url,icon);
//        try {
//            if (File.Exist(icon) || !ir.microsign.net.Utility.isConnect(imageView.getContext())) {
//                setImageView(imageView, icon,waiting);
//                return;
//            }
//            setImageView(imageView, null,null);
//            DownloadFile downloadFile = new DownloadFile();
//            downloadFile.downloadFile(url, icon, 1024, new DownloadFile.OnDownloadListener() {
//                @Override
//                public boolean downloading(int downloaded, int total) {
//                    return true;
//                }
//
//                @Override
//                public void OnDownloadCompleted(boolean succeed, final String path) {
//                    if (!succeed) setImageView(imageView, null,waiting);
//                    else
//                        setImageView(imageView, path,waiting);
//                }
//            });
//
//        } catch (Exception ex) {
//            Log.e("NotificationView", ".setImage():" + ex.getMessage());
//            setImageView(imageView, null,waiting);
//        }
    }

    public String getIconUrl() {
        if (Text.isNullOrEmpty(icon)) return null;
        if (icon.startsWith("http://")||icon.startsWith("https://")) return icon;
        return Utils.getSimpleUrl()+icon;
    }
    public String getIconSavePath(Context context) {
        if (Text.isNullOrEmpty(icon)) return "";
        return File.GetRoot(context)+"/apps/"+ File.ConvertUrlToStoragePath(icon);    }
//
//    private static void setImageView(final ImageView imageView, final String file, final View waiting){
//        imageView.post(new Runnable() {
//            public void run() {
//                if (file==null||!File.Exist(file))
//                    imageView.setImageResource(R.drawable.ic_microsign_watermark);
//                else  imageView.setImageBitmap(BitmapFactory.decodeFile(file));
//                _view.setVisibility(waiting,false);
//            }
//        });
//    }
public static List<BaseObject> getApps(Context context){
    return DataSourceApi.getSource(context).getApps();
}
public static App getApp(Context context){
    return DataSourceApi.getSource(context).getApp();
}
	static  private void onResult(final Activity context,Response response, final updateListener l){
		if (!response.succeed()){
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					WaitingDialog.showWaitingDialog(context,false);

					l.failed();
				}
			});

			return;
		}
		final List<BaseObject> apps=response.getObjectArray(App.class,"Apps");
		long time=Calendar.getInstance().getTimeInMillis();
		for (BaseObject app : apps) {
			((App)app).insert_date=time;
//            ((App)app).insert_date=time;

		}
//        app.insert_date= Calendar.getInstance().getTimeInMillis();
		DataSourceApi.getSource(context).cleanAndInsertObjects(new App().getTableName(),apps);
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				WaitingDialog.showWaitingDialog(context,false);

				l.ok();
			}
		});
	}
public static void updateApps(final Activity context,boolean force,final updateListener l){
    App app=getApp(context);
    if (app==null)force=true;
    if (force)
        Utility.CheckInternet(context, new ConnectDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, boolean isConnect) {
                if (!isConnect)return;
                WaitingDialog.showWaitingDialog(context,true);
                Api.get("App", "Apps", new Api.onResponseListener() {
                    @Override
                    public void onResponse(Response response) {

                        onResult(context,response,l);
                    }
                });
            }
        });
    else if (Utility.isConnect(context)&&Calendar.getInstance().getTimeInMillis()-app.insert_date>100*60*10)
    {
        WaitingDialog.showWaitingDialog(context,true);

        Api.get("App", "Apps", new Api.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                onResult(context,response,l);
            }
        });
    }
    else l.ok();
}
	public interface updateListener{
		void ok();
		void failed();
	}
}
