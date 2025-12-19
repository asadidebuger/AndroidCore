package ir.microsign.api.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import ir.microsign.api.Api;
import ir.microsign.api.dialog.DialogInfo;
import ir.microsign.api.object.App;
import ir.microsign.api.object.NetErrorMiddleware;
import ir.microsign.api.view.LargeAppView;
import ir.microsign.calendar.Date;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

public class ActivityIntro extends Activity implements View.OnClickListener {
    public static void show(Context context){
        Intent intent=new Intent(context,ActivityIntro.class);
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
        setContentView(R.layout.layout_activity_intro);

//        Text.setText(findViewById(R.id.btn1),getString(R.string.txt_intro_about_company), Font.TextPos.h2);

        Text.setText(findViewById(R.id.btn1),getString(R.string.txt_intro_about_company), Font.TextPos.h1);
        Text.setText(findViewById(R.id.btn2),getString(R.string.txt_intro_about_app), Font.TextPos.h1);
        Text.setText(findViewById(R.id.btn3),getString(R.string.txt_intro_other_apps), Font.TextPos.h1);
        Text.setText(findViewById(R.id.btn4),getString(R.string.txt_intro_other_send_report), Font.TextPos.h1);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.img_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            showInfo(R.id.btn1);
        } else if (view.getId() == R.id.btn2) {
            showInfo(R.id.btn2);
        } else if (view.getId() == R.id.btn3) {
ActivityApps.show(this);
        } else if (view.getId() == R.id.btn4) {
            ActivityTicket.show(this);

        } else if (view.getId() == R.id.img_close) {
            finish();
        }
    }
     DialogInfo dialogInfo=null;
    void showDialogInfo(int id,App app) {
        String title = null;
        View view = null;
        if (id == R.id.btn1) {
            title = getString(R.string.txt_intro_about_company);
            view = app.getDeveloper().getView(ActivityIntro.this);
        } else if (id == R.id.btn2) {
            title = getString(R.string.txt_intro_about_app);
            view = app.getLargeView(ActivityIntro.this);
        }
        dialogInfo = new DialogInfo(ActivityIntro.this, title, view, new DialogInfo.onDialogListener() {
            @Override
            public void onUpdateRequest() {
                App.updateApps(ActivityIntro.this,true, new App.updateListener() {
                    @Override
                    public void ok() {
                        App o = App.getApp(ActivityIntro.this);
                        dialogInfo.updateView((dialogInfo.content == null || dialogInfo.content instanceof LargeAppView) ? o.getLargeView(ActivityIntro.this) : o.getDeveloper().getView(ActivityIntro.this));
                    }

                    @Override
                    public void failed() {

                    }
                });
            }
        });
        if (dialogInfo.isShowing()) dialogInfo.dismiss();
        dialogInfo.show();
    }
    void showInfo(final int id){
       App.updateApps(this,false,new App.updateListener() {
            @Override
            public void ok() {

                        try {
                            showDialogInfo(id,App.getApp(ActivityIntro.this));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
            }

           @Override
           public void failed() {

           }
       });


    }
//    App getApp(){
//        return DataSourceApi.getSource(this).getApp();
//    }
//    void updateApp(boolean force,final updateListener l){
//        App app=getApp();
//        if (app==null)force=true;
//        if (force)
//        Utility.CheckInternet(this, new ConnectDialog.OnDialogResultListener() {
//            @Override
//            public void OnDialogResult(boolean ok, boolean isConnect) {
//                if (!isConnect)return;
//                WaitingDialog.showWaitingDialog(ActivityIntro.this,true);
//                Api.get("App", "Apps", new Api.onResponseListener() {
//                    @Override
//                    public void onResponse(Response response) {
//
//                        onResult(response,l);
//                    }
//                });
//            }
//        });
//        else if (Utility.isConnect(this)&&Calendar.getInstance().getTimeInMillis()-app.insert_date>100*60*1)
//            {
//                WaitingDialog.showWaitingDialog(ActivityIntro.this,true);
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
//                    WaitingDialog.showWaitingDialog(ActivityIntro.this,false);
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
//        DataSourceApi.getSource(ActivityIntro.this).cleanAndInsertObjects(new App().getTableName(),apps);
//        runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                WaitingDialog.showWaitingDialog(ActivityIntro.this,false);
//
//                l.ok();
//            }
//        });
//    }
//   public interface updateListener{
//        void ok();
//        void failed();
//    }
}
