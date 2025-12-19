package ir.microsign.gallery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.WindowManager;

import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.R;
import ir.microsign.gallery.adapter.FullScreenImagePathAdapter;
import ir.microsign.gallery.database.DataSourceImage;
import ir.microsign.gallery.helper.Utils;


import java.util.ArrayList;
import java.util.List;

public class FullScreenImagesPathActivity extends Activity {

    DataSourceImage mDataSourceImage = null;
    private Utils utils;
    private FullScreenImagePathAdapter adapter;
    private ViewPager viewPager;

    public static void show(Context context, String folder, String currentFileName, boolean showAll) {
        Intent i = new Intent(context, FullScreenImagesPathActivity.class);
        i.putExtra("folder", folder);
        i.putExtra("current", currentFileName);
        i.putExtra("showall", showAll);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

//	DataSourceImage getDataSource(){
//		 if (mDataSourceImage!=null)return mDataSourceImage;
//		mDataSourceImage=new DataSourceImage(this);
//		return mDataSourceImage;
//	}

    //	public void openFolder()
//	{
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
//		intent.setDataAndType(uri, "vnd.android.cursor.dir/lysesoft.andexplorer.directory");
//		startActivity(Intent.createChooser(intent, "Open folder"));
//	}
//	void downloadOriginalImage(){
//		List<String> list=new ArrayList<String>();
//	Image image=adapter.getItem(viewPager.getCurrentItem());
//
//		list.add(utility.getImageFileUrl(image.id,Image.ORIGINAL));
//		image.downloadOriginalVisual(this, Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS, new DownloadFiles.DownloadCompletedListener() {
//			@Override
//			public void OnFileDownloaded(boolean succeed, String path) {
//				if (succeed) openImage(path);
//			}
//
//			@Override
//			public void OnFinish(boolean allSucceed, String root) {
//
//			}
//		})  ;
//
//	}
//	public void openImage(String path){
//		try {
//			Intent intent = new Intent();
//			intent.setAction(Intent.ACTION_VIEW);
//			intent.setDataAndType(Uri.parse("file://" + path), "image/*");
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//		}   catch (Exception ex){}
//
//	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_fullscreen_image);
        boolean showAll = getIntent().getBooleanExtra("showall", true);
        if (showAll) {
            findViewById(R.id.img_nav_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.arrowScroll(ViewPager.FOCUS_RIGHT);
                }
            });
            findViewById(R.id.img_nav_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.arrowScroll(ViewPager.FOCUS_LEFT);
                }
            });
        } else {
            view.setVisibility(findViewById(R.id.img_nav_right), false);
            view.setVisibility(findViewById(R.id.img_nav_left), false);
        }
        Text.setText(findViewById(R.id.txt_image_download_original), R.string.gallery_download_original, Font.TextPos.h1);
        view.setVisibility(findViewById(R.id.ll_download_original), false);
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.pager);

        utils = new Utils(getApplicationContext());

        Intent i = getIntent();
        String current = i.getStringExtra("current");
        String folder = i.getStringExtra("folder");

        String[] files = new java.io.File(folder).list();
        List<String> images = new ArrayList<>();
        int position = 0;
        if (!showAll) images.add(folder + "/" + current);
        else {
            for (int i1 = 0; i1 < files.length; i1++) {
                String file = files[i1];
                images.add(folder + "/" + file);
                if (file.endsWith(current)) position = i1;
            }
        }
        adapter = new FullScreenImagePathAdapter(FullScreenImagesPathActivity.this, images);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

    }
}
