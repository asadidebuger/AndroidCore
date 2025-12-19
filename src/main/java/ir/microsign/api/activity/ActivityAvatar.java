package ir.microsign.api.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.avito.android.krop.KropView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import ir.microsign.api.Api;
import ir.microsign.api.Utils;
import ir.microsign.api.object.NetErrorMiddleware;
import ir.microsign.api.object.Profile;
import ir.microsign.api.object.Response;
import ir.microsign.dialog.WaitingDialog;
import ir.microsign.R;
import ir.microsign.net.DownloadFile;
import ir.microsign.net.FilePart;
import ir.microsign.utility.File;
import ir.microsign.utility.Font;
import ir.microsign.utility.Graphics;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;


public class ActivityAvatar extends Activity {

	public static void  show(Context context){
		if (!Profile.isSignIn())return;
		bitmap=null;
		Intent intent=new Intent(context,ActivityAvatar.class);
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
View btnRotate;
	//	WaitingDialog waitingDialog;
	KropView kropView;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

		super.onCreate(savedInstanceState);
		if (!ir.microsign.net.Utility.isConnect(this)){

			showMessage(getString(R.string.msg_login_no_network));
			finish();
			return;
		}

		setContentView(R.layout.layout_activity_avatar);
		View btnCam=findViewById(R.id.btn_from_cam);
		View btnFile=findViewById(R.id.btn_from_gallery);
		 btnRotate=findViewById(R.id.btn_rotate);
		View btnOk=findViewById(R.id.btn_ok);
		Text.setText(btnCam,R.string.btn_from_cam,Font.TextPos.h2);
		Text.setText(btnFile,R.string.btn_from_gallery,Font.TextPos.h2);
		Text.setText(btnOk,R.string.btn_set_profile_image,Font.TextPos.h2);
		Text.setText(btnRotate,R.string.btn_rotate,Font.TextPos.h2);
		btnRotate.setEnabled(false);
		btnCam.setOnClickListener(mOnClickListener);
		btnFile.setOnClickListener(mOnClickListener);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				upload();
			}
		});
		btnRotate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rotate();
			}
		});
		btnOk.setEnabled(false);


		kropView=findViewById(R.id.krop_view);

		Profile profile=Profile.getSavedProfileOrEmpty(this);
		String url=profile.getAvatarUrl(),icon=profile.getAvatarSavePath(this);
		if (bitmap!=null){
			setImageView(bitmap);
			return;
		}
		if (Text.isEmpty(url)){setImageView((Bitmap) null);return;}
		if (File.Exist(icon)){setImageView(icon);return;}
		WaitingDialog.showWaitingDialog(this,true);
//		waitingDialog=new WaitingDialog(this);
//		waitingDialog.show();

			DownloadFile downloadFile = new DownloadFile();
			downloadFile.downloadFile(url, icon, 1024, new DownloadFile.OnDownloadListener() {
				@Override
				public boolean downloading(int downloaded, int total) {
					return true;
				}

				@Override
				public void OnDownloadCompleted(boolean succeed, final String path) {
					if (!succeed) setImageView((Bitmap) null);
					else
						setImageView(path);
				}
			});




	}
	void rotate(){
		if (bitmap==null)return;
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		bitmap= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		setImageView(bitmap);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		byte[] bytes=savedInstanceState.getByteArray("image");
		if (bytes!=null)
		bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
		super.onRestoreInstanceState(savedInstanceState);
	}


	void upload() {
		WaitingDialog.showWaitingDialog(ActivityAvatar.this, true);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				_upload();
			}
		}, 100);


	}
	void _upload(){
if (bitmap==null)return;
Bitmap _bitmap=kropView.getCroppedBitmap();
if (_bitmap.getWidth()>500){
	_bitmap=Graphics.getResizedBitmap(_bitmap,500);
}


		String path=File.GetRoot(this)+"/upload/";
		File.AppendDir(path);
		path+="avatar.png";
		try {

			FileOutputStream out = new FileOutputStream(path);
			_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			showMessage(getString(R.string.msg_error_occurred));
			return;
		}



		List<FilePart> fileParts=new ArrayList<>();
		FilePart filePart=new FilePart("avatar",path,"image/png");
		fileParts.add(filePart);
		Api.sendRequest(Utils.getApiUrl(), Api.METHOD.POST, "profile", "avatar", null, fileParts, new Api.onResponseListener() {
			@Override
			public void onResponse(Response response) {
				if (response.succeed()){
					Profile profile=response.getObject(Profile.class,"avatar");
					Profile.insertProfile(ActivityAvatar.this,profile);

					finish();
				}
				else {
					showMessage(getString(R.string.msg_error_occurred));
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						WaitingDialog.showWaitingDialog(null,false);
					}
				});
				Log.e("uploaded!",response.getString());
			}
		});

	}
	View.OnClickListener mOnClickListener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			selectImage(v.getId()==R.id.btn_from_cam);
		}
	};
	void selectImage(boolean fromCam){
if (fromCam)capturePhoto();
else openImage();
	}
	void  setImageView(String path){
		if (path==null)return;
		try{
			setImageView(BitmapFactory.decodeFile(path));

		}catch (Exception e){
			e.printStackTrace();
		}

	}
	static Bitmap bitmap=null;
	void  setImageView(final Bitmap b){

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WaitingDialog.showWaitingDialog(ActivityAvatar.this,false);
				bitmap=b;
				if (b!=null){
					kropView.setBitmap(b);
					btnRotate.setEnabled(true);
				}
				else btnRotate.setEnabled(false);

			}
		});

	}
	void showMessage(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.showToast(ActivityAvatar.this, msg);
			}
		});


	}

	public void openImage() {
		try {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
		} catch (Exception e) {
			Log.e("openImage", "No gallery: " + e);
		}
	}
	public void capturePhoto() {


		try {
			ContentValues values = new ContentValues();
//			values.put(MediaStore.Images.Media.TITLE, "New Picture");
//			values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
			 imageUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, 4);
		}catch ( Exception e){
			e.printStackTrace();
		}


	}
	Uri imageUri;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == 0) return;
		if (resultCode != Activity.RESULT_OK)return;
		if (requestCode == 4) {

			 {

				try {
					Bitmap photo = MediaStore.Images.Media.getBitmap(
							getContentResolver(), imageUri);
					setImageView(photo);
					findViewById(R.id.btn_ok).setEnabled(true);

				} catch (Exception e) {
					e.printStackTrace();
				}

			return;
			}

		} else if (requestCode == 3) {

			Uri uri = data.getData();

			try {
				Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
				setImageView(bitmap);
				findViewById(R.id.btn_ok).setEnabled(true);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

//		super.onActivityResult(requestCode, resultCode, data);


	}

}
