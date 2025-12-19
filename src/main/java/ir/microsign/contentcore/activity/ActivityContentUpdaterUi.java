package ir.microsign.contentcore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import cn.pedant.SweetAlert.widget.SweetAlertDialog;
import ir.microsign.R;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.network.ContentUpdaterAll;
import ir.microsign.contentcore.object.CCtrl;
import ir.microsign.contentcore.view.RichWebView;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.net.Utility;
import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 2016/08/24.
 */
public class ActivityContentUpdaterUi extends  Activity implements ContentUpdaterAll.ContentUpdateAllListener {
//	Activity activity=null;
//	public ActivityContentUpdaterUi(Activity activity){
//		this.activity=activity;
//	}
	ContentUpdaterAll mContentUpdaterAll=null;
//public void start(){
//	mContentUpdaterAll=new ContentUpdaterAll(activity,this);
//	mContentUpdaterAll.check();
//
//}

	public static void show(Activity context,boolean cheked,int resultRequestCode){
		Intent intent=new Intent(context,ActivityContentUpdaterUi.class);
		intent.putExtra("checked",cheked);
		context.startActivityForResult(intent,resultRequestCode);
	}
	boolean mustUpdateByOffline(){
		return getIntent().getBooleanExtra("checked",false);
	}
	boolean mustOnlyGetOffline(){
		return mOnlyOffline;
	}


	boolean mOnlyOffline=true;
	ProgressBar mProgressBar=null;
	View txtProgressTitle=null;
	View txtProgressDesc=null;
	View btn=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_update_content_main);
		 btn=findViewById(R.id.txt_update);
		txtProgressTitle=findViewById(R.id.txt_p1_title);
		txtProgressDesc=findViewById(R.id.txt_p1_desc);
		mProgressBar= (ProgressBar) findViewById(R.id.progressBarHorizontal1);
		mWebView= (RichWebView) findViewById(R.id.web_content);
		Text.setText(findViewById(R.id.txt_title),R.string.txt_update_main_title, Font.TextPos.h1);
		Text.setText(btn,R.string.btn_update_main_check_update, Font.TextPos.h1);
		view.setVisibility(findViewById(R.id.group_firstProgress),false);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utility.CheckInternet(ActivityContentUpdaterUi.this, new ConnectDialog.OnDialogResultListener() {
					@Override
					public void OnDialogResult(boolean ok, boolean isConnect) {
						if (!isConnect) return;
						startUpdate();
					}
				});

			}
		});
		findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setDesc(Text.ReadResTxt(this,R.raw.fullupdatedesc));
		Display.keepAwake(this);

	}
	RichWebView mWebView=null;
	void setDesc(String desc){
		mWebView.setContent(desc, Font.getFontByName(this,"mitrabold",true),true,false);
	}
	void setProgressTitle(String title){

		Text.setText(txtProgressTitle,title, Font.TextPos.h1);
	}
	void setProgressDesc(String title){

		Text.setText(txtProgressDesc,title, Font.TextPos.h3);
	}
void  showProgressGroup(boolean show){
	view.setVisibility(findViewById(R.id.group_firstProgress),show);
}
	void onNewUpdateNotExist(){
		MessageDialog messageDialog=new MessageDialog(this);

		messageDialog.setOnDialogResultListener(new MessageDialog.OnDialogResultListener() {
			@Override
			public void OnDialogResult(boolean ok, String key) {
				finish();
			}
		});
		new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
				.setTitleText(R.string.update_content_title_no_new_update)
				.setContentText(R.string.update_content_desc_no_new_update)
				.setConfirmText(R.string.ok)
				.showCancelButton(false)
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.dismiss();
					}
				})
				.show();
//		messageDialog.show(R.string.update_content_title_no_new_update,R.string.update_content_desc_no_new_update);
	}
	void  startUpdate(){
		try {
			Display.fixScreen(this);
			btn.setEnabled(false);
			AsyncUpdate asyncUpdate = new AsyncUpdate();
			asyncUpdate.execute();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	class AsyncUpdate extends AsyncTask<Void,Void,Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
			if (mContentUpdaterAll==null)mContentUpdaterAll=new ContentUpdaterAll(ActivityContentUpdaterUi.this,ActivityContentUpdaterUi.this);
			if (mustUpdateByOffline())mContentUpdaterAll.prepareToStartDownload();
			else mContentUpdaterAll.check();

			}catch (Exception e){
				Log.e("UpdaterActivity",e.getMessage());
			}
			return null;
		}
	}
	@Override
	public void OnPrepare() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showProgressGroup(true);
				setProgressTitle(getString(R.string.update_main_progress_prepare));
				mProgressBar.setMax(1);
				mProgressBar.setProgress(0);
			}
		});


	}

	@Override
	public void OnPrepared(boolean succeed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
		mProgressBar.setProgress(1);}
		});
	}

	@Override
	public void OnLastUpdateReceiveStart() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
		setProgressTitle(getString(R.string.update_main_progress_get_last));
		mProgressBar.setProgress(0);
			}
		});
	}

	@Override
	public boolean OnLastUpdateReceiveEnd(boolean succeed, final CCtrl update) {
//		boolean con= !mustOnlyGetOffline();
//		if (update==null) return false;
		final boolean isNew=update!=null&&update.version>DataSource.getDataSource().getVersion();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (isNew){
					showProgressGroup(false);
		mProgressBar.setProgress(1);

		getIntent().putExtra("checked",true);
		Text.setText(findViewById(R.id.txt_update),R.string.btn_update_main_update, Font.TextPos.h1);
		btn.setEnabled(true);
//			view.showToast(ActivityContentUpdaterUi.this,R.string.update_content_desc_no_new_update);

			setDesc(update.getDescription());}
				else {
						onNewUpdateNotExist();
					}}
			});
		return isNew;
	}


	@Override
	public void OnPrepareToUpdateStart() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setProgressTitle(getString(R.string.update_main_progress_download_prepare));
				mProgressBar.setProgress(0);}
		});
	}

	@Override
	public void OnPrepareToUpdateEnd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {mProgressBar.setProgress(1);}
		});
	}
int totalSize=0;
	@Override
	public void OnDownloadBegin( int size) {
		totalSize=size;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showProgressGroup(true);
				setProgressTitle(getString(R.string.update_main_progress_download));
				mProgressBar.setMax(totalSize);

				mProgressBar.setProgress(0);}
		});

	}

	@Override
	public void OnDownloadReceiving(int size, int received) {

		mProgressBar.setProgress(received);

	}

	@Override
	public void OnDownloadCompleted() {

	}

	@Override
	public void OnUnzipStarted() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setProgressTitle(getString(R.string.update_main_progress_unzip));
				mProgressBar.setProgress(0);	}
		});
	}

	@Override
	public void OnUnziping(final int size,final int unziped) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mProgressBar.setMax(size);
				mProgressBar.setProgress(unziped);	}
		});
	}


	@Override
	public void OnUnzipEnd() {

	}

	@Override
	public void OnAfterUnzip() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setProgressTitle(getString(R.string.update_main_progress_restore));
			}
		});

	}

	@Override
	public void OnFinished(boolean succeed) {
		setResult(succeed?1:-1);
		finish();

	}

	@Override
	public void OnCanceled() {

	}

	@Override
	public void finish() {
		Display.releaseScreen(this);
		Display.keepAwakeOff();
		super.finish();
	}
}
