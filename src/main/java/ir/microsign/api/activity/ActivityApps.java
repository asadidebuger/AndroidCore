package ir.microsign.api.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import ir.microsign.api.Api;
import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.dialog.DialogInfo;
import ir.microsign.api.object.App;
import ir.microsign.api.object.NetErrorMiddleware;
import ir.microsign.api.view.AppView;
import ir.microsign.calendar.Date;
import ir.microsign.dbhelper.adapter.BaseAdapter;
import ir.microsign.R;
import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

public class ActivityApps extends Activity implements View.OnClickListener {
    GridView listView;
    public static void show(Context context){
        Intent intent=new Intent(context,ActivityApps.class);
        context.startActivity(intent);

    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Api.addOnExceptionListener(this.getClass().getName(),new NetErrorMiddleware(this));

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Api.removeOnExceptionListener(this.getClass().getName());

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);Date.setContext(getBaseContext());
        setContentView(R.layout.layout_activity_apps);

//        Text.setText(findViewById(R.id.btn1),getString(R.string.txt_intro_about_company), Font.TextPos.h2);

        Text.setText(findViewById(R.id.txt_title),getString(R.string.txt_intro_other_apps), Font.TextPos.h1);
//        Text.setText(findViewById(R.id.btn2),getString(R.string.txt_intro_about_app), Font.TextPos.h1);
//        Text.setText(findViewById(R.id.btn3),getString(R.string.txt_intro_other_apps), Font.TextPos.h1);
//        Text.setText(findViewById(R.id.btn4),getString(R.string.txt_intro_other_send_report), Font.TextPos.h1);
//
//        findViewById(R.id.btn1).setOnClickListener(this);
//        findViewById(R.id.btn2).setOnClickListener(this);
//        findViewById(R.id.btn3).setOnClickListener(this);
//        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.img_close).setOnClickListener(this);
        findViewById(R.id.img_header_icon1).setOnClickListener(this);
         listView=findViewById(R.id.app_list);
//        listView.setNumColumns(Display.getWidth(this)/Display.dpToPx(this,150));
        listView.setNumColumns(Display.isLandscape(this)?2:1);
        listView.setSelector(R.drawable.back_transparent);
        int padding=Display.dpToPx(this,5);
        listView.setPadding(padding,padding,padding,padding);

        fill(false,false);
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.btn1) {
//            showInfo(R.id.btn1);
//        } else if (view.getId() == R.id.btn2) {
//            showInfo(R.id.btn2);
//        } else if (view.getId() == R.id.btn3) {
//
//        } else
 if (view.getId() == R.id.img_header_icon1) {
           fill(true,false);

        } else
            if (view.getId() == R.id.img_close) {
            finish();
        }
    }
//     DialogInfo dialogInfo=null;
//    void showDialogInfo(int id,App app){
//        String title=null;
//        View view=null;
//        if (id == R.id.btn1) {
//            title=getString(R.string.txt_intro_about_company);
//            view=app.getDeveloper().getView(ActivityApps.this);
//        } else if (id == R.id.btn2) {
//            title=getString(R.string.txt_intro_about_app);
//            view=app.getView(ActivityApps.this);
//        }
//         dialogInfo = new DialogInfo(ActivityApps.this, title, view, new DialogInfo.onDialogListener() {
//            @Override
//            public void onUpdateRequest() {
//                updateApp(true, new updateListener() {
//                    @Override
//                    public void ok() {
//App o=getApp();
//                        dialogInfo.updateView((dialogInfo.content==null||dialogInfo.content instanceof LargeAppView)?o.getView(ActivityApps.this):o.getDeveloper().getView(ActivityApps.this));
//                    }
//
//                    @Override
//                    public void failed() {
//
//                    }
//                });
//            }
//        });
//        if (dialogInfo.isShowing())dialogInfo.dismiss();
//        dialogInfo.show();
//    }
//    void showInfo(final int id){
//       updateApp(false,new updateListener() {
//            @Override
//            public void ok() {
//
//                        try {
//                            showDialogInfo(id,getApp());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//            }
//
//           @Override
//           public void failed() {
//
//           }
//       });
//
//
//    }
    DialogInfo dialogInfo=null;
    App app;
    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          app=((AppView)view).getObject();
       showInfo();
    }
};
    void showInfo(){
        app=DataSourceApi.getSource(ActivityApps.this).getApp(app.pkg);

        if (dialogInfo!=null&&dialogInfo.isShowing())dialogInfo.dismiss();
        dialogInfo=new DialogInfo(ActivityApps.this, app.name, app.getLargeView(ActivityApps.this), new DialogInfo.onDialogListener() {
            @Override
            public void onUpdateRequest() {
                fill(true,true);
//                App a= DataSourceApi.getSource(ActivityApps.this).getApp(app.pkg);
            }
        });
        dialogInfo.show();
    }

    void fill(boolean forceUpdate,final boolean showInfo){
        App.updateApps(this,forceUpdate, new App.updateListener() {
            @Override
            public void ok() {
                listView.setAdapter(new BaseAdapter(ActivityApps.this,App.getApps(ActivityApps.this)));
                listView.setOnItemClickListener(onItemClickListener);
                if (showInfo)showInfo();
            }

            @Override
            public void failed() {

            }
        });
    }
//    List<BaseObject> getApps(){
//        return DataSourceApi.getSource(this).getApps();
//    }
//    void updateApp(boolean force,final updateListener l){
//        List<BaseObject> apps=getApps();
//        if (apps.size()<1)force=true;
//        if (force)
//        Utility.CheckInternet(this, new ConnectDialog.OnDialogResultListener() {
//            @Override
//            public void OnDialogResult(boolean ok, boolean isConnect) {
//                if (!isConnect)return;
//                WaitingDialog.showWaitingDialog(ActivityApps.this,true);
//                Api.get("App", "Apps", new Api.onResponseListener() {
//                    @Override
//                    public void onResponse(Response response) {
//
//                        onResult(response,l);
//                    }
//                });
//            }
//        });
//        else if (Utility.isConnect(this)&&Calendar.getInstance().getTimeInMillis()-((App)apps.get(0)).insert_date>100*60*1)
//            {
//                WaitingDialog.showWaitingDialog(ActivityApps.this,true);
//
//                Api.get("App", "Apps", new Api.onResponseListener() {
//                    @Override
//                    public void onResponse(Response response) {
//                        onResult(response,l);
//                    }
//                });
//            }
//            else l.ok();
//    }
//    private void onResult(Response response,final App.updateListener l){
//
//        if (!response.succeed()){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    WaitingDialog.showWaitingDialog(ActivityApps.this,false);
//
//                    l.failed();
//                }
//            });
//
//            return;
//        }
//        final List<BaseObject> apps=response.getObjectArray(App.class,"Apps");
//        long time=Calendar.getInstance().getTimeInMillis();
//        for (BaseObject app : apps) {
//            ((App)app).insert_date=time;
////            ((App)app).insert_date=time;
//
//        }
////        app.insert_date= Calendar.getInstance().getTimeInMillis();
//        DataSourceApi.getSource(ActivityApps.this).cleanAndInsertObjects(new App().getTableName(),apps);
//        runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                WaitingDialog.showWaitingDialog(ActivityApps.this,false);
//
//                l.ok();
//            }
//        });
//    }
//    interface updateListener{
//        void ok();
//        void failed();
//    }
}
