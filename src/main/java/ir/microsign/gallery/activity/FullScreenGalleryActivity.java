package ir.microsign.gallery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.WindowManager;

import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.R;
import ir.microsign.gallery.adapter.FullScreenImageAdapter;
import ir.microsign.gallery.database.DataSourceImage;
import ir.microsign.gallery.helper.Utils;
import ir.microsign.gallery.network.utility;
import ir.microsign.gallery.object.Image;
import ir.microsign.net.DownloadFiles;


import java.util.ArrayList;
import java.util.List;

public class FullScreenGalleryActivity extends Activity {

    DataSourceImage mDataSourceImage = null;
    private Utils utils;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    public static void show(Context context, int catId, int position) {
        Intent i = new Intent(context, FullScreenGalleryActivity.class);
        i.putExtra("position", position);
        i.putExtra("cat_id", catId);
        context.startActivity(i);
    }

    DataSourceImage getDataSource() {
        if (mDataSourceImage != null) return mDataSourceImage;
        mDataSourceImage = new DataSourceImage(this);
        return mDataSourceImage;
    }

    //	public void openFolder()
//	{
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
//		intent.setDataAndType(uri, "vnd.android.cursor.dir/lysesoft.andexplorer.directory");
//		startActivity(Intent.createChooser(intent, "Open folder"));
//	}
    void downloadOriginalImage() {
        List<String> list = new ArrayList<String>();
        Image image = adapter.getItem(viewPager.getCurrentItem());

        list.add(utility.getImageFileUrl(image.id, Image.ORIGINAL));
        image.downloadOriginalVisual(this, Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS, new DownloadFiles.DownloadCompletedListener() {
            @Override
            public void OnFileDownloaded(boolean succeed, String path) {
                if (succeed) openImage(path);
            }

            @Override
            public void OnFinish(boolean allSucceed, String root) {

            }
        });

    }

    public void openImage(String path) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + path), "image/*");
            startActivity(intent);
        } catch (Exception ex) {
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_fullscreen_image);
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
        Text.setText(findViewById(R.id.txt_image_download_original), R.string.gallery_download_original, Font.TextPos.h1);
        if (getResources().getBoolean(R.bool.gallery_show_download_original))
            findViewById(R.id.ll_download_original).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadOriginalImage();
                }
            });
        else view.setVisibility(findViewById(R.id.ll_download_original), false);
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.pager);

        utils = new Utils(getApplicationContext());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        int catId = i.getIntExtra("cat_id", 0);
        Image select = new Image();
        select.catid = catId;
        adapter = new FullScreenImageAdapter(FullScreenGalleryActivity.this, (ArrayList<Image>) getDataSource().select(select));

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

    }
}
