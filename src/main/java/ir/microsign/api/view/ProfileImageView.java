package ir.microsign.api.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import ir.microsign.api.activity.ActivityAvatar;
import ir.microsign.api.object.Profile;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.net.DownloadFile;
import ir.microsign.utility.File;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.view.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class ProfileImageView extends BaseView {
	public ProfileImageView(Context context, BaseObject baseObject) {
		super(context, baseObject);
	}
	Profile getObject(){
		return (Profile) getDbObject();
	}

	@Override
	public void initFromBaseObject(BaseObject baseObject) {
		super.initFromBaseObject(baseObject);
		getLayoutInflater().inflate(R.layout.layout_view_profile_image,this,true);
		setView();
	}

//	@Override
//	protected void onAttachedToWindow() {
//		super.onAttachedToWindow();
//		setView();
//	}

	public void checkChanges(){
		Profile p=Profile.getSavedProfileOrEmpty(getContext());
		if (getObject().equals(p))return;
		setDbObject(p);
		setView();
	}

	public void setView(){


		Text.setText(findViewById(R.id.txt_name), getObject().getFullName(),Font.TextPos.h1);
		View txtChangePic=findViewById(R.id.txt_change_picture);
		if (Profile.isSignIn()) {
			findViewById(R.id.ll_profile_text).setVisibility(VISIBLE);

			Text.setText(txtChangePic, R.string.txt_change_picture, Font.TextPos.h2);
			txtChangePic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActivityAvatar.show(getContext());
				}
			});
		}
		else {
			findViewById(R.id.ll_profile_text).setVisibility(GONE);
			txtChangePic.setVisibility(GONE);
		}
		CircleImageView imageView= findViewById(R.id.img_face);
		ImageView bkg=(ImageView) findViewById(R.id.img_bkg);
		String avatarUrl=getObject().getAvatarUrl();
		Blurry.with(getContext()).radius(25)
				.sampling(1)
				.color(Color.argb(66, 255, 255, 0))
				.async()
				.from(BitmapFactory.decodeResource(getResources(),R.drawable.bkg5))
				.into(bkg);
		if (!Text.isEmpty(avatarUrl)){
			setImage(imageView,BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_user_large),findViewById(R.id.prg_wait),avatarUrl,getObject().getAvatarSavePath(getContext()));
		}else imageView.setImageResource(R.drawable.ic_user_large);

		}
	public  void setImage(final CircleImageView imageView, final Bitmap defaultImage, final View waiting, String url, String icon) {
		try {
			if (Text.isEmpty(url)||File.Exist(icon) || !ir.microsign.net.Utility.isConnect(imageView.getContext())) {
				setImageView(imageView,defaultImage, icon,waiting);
				return;
			}
			setImageView(imageView, defaultImage,null,null);
			DownloadFile downloadFile = new DownloadFile();
			downloadFile.downloadFile(url, icon, 1024, new DownloadFile.OnDownloadListener() {
				@Override
				public boolean downloading(int downloaded, int total) {
					return true;
				}

				@Override
				public void OnDownloadCompleted(boolean succeed, final String path) {
					if (!succeed) setImageView(imageView,defaultImage, null,waiting);
					else
						setImageView(imageView,defaultImage, path,waiting);
				}
			});

		} catch (Exception ex) {
			setImageView(imageView, defaultImage,null,waiting);
		}
	}
	private  void setImageView(final CircleImageView imageView,final Bitmap defaultImage, final String file, final View waiting){
		imageView.post(new Runnable() {
			public void run() {
				if (file==null||!File.Exist(file)) {
					imageView.setImageBitmap(defaultImage);

				}
				else {
					Bitmap b=BitmapFactory.decodeFile(file);
					imageView.setImageBitmap(b);

					try {

						Blurry.with(getContext()).radius(25)
								.sampling(1)
								.color(Color.argb(66, 255, 255, 0))
								.async()
								.from(b)
								.into((ImageView) findViewById(R.id.img_bkg));
//                        Blurry.with(getActivity())
//                                .radius(25)
//                                .sampling(1)
//                                .color(Color.argb(66, 255, 255, 0))
////                                .async()
//                                .capture(view)
////                                .capture(view.findViewById(R.id.img_face))
//                                .into((ImageView) view.findViewById(R.id.img_bkg));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				view.setVisibility(waiting,false);
			}
		});
	}





}
