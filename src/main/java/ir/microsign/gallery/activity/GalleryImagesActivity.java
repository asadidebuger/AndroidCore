package ir.microsign.gallery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.R;
import ir.microsign.gallery.adapter.GridViewImageAdapter;
import ir.microsign.gallery.database.DataSourceImage;
import ir.microsign.gallery.helper.AppConstant;
import ir.microsign.gallery.helper.Utils;
import ir.microsign.gallery.object.Image;
import ir.microsign.net.Utility;
import java.util.ArrayList;

public class GalleryImagesActivity extends Activity implements View.OnClickListener {
    public static int columnWidth = 0;
    static float padding = 0;
    private static Utils utils;
    DataSourceImage mDataSource = null;
    //	private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;

    public static void show(Context context, int catId, String title) {
        Intent intent = new Intent(context, GalleryImagesActivity.class);
        intent.putExtra("catid", catId);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    DataSourceImage getDataSource() {
        if (mDataSource == null) mDataSource = new DataSourceImage(this);
        return mDataSource;
    }

    void update2() {
//        WaitingDialog.showWaitingDialog(this, true);
//
//        utility.getGalleryCategories(this, new Listener.ReceiveObjectListener() {
//            @Override
//            public void onReceiveCompleted(List<BaseObject> objects, int resultState) {
//                getDataSource().autoInsertUpdateByColumn(objects, "cid");
//                utility.getGalleryImages(GalleryImagesActivity.this, new Listener.ReceiveObjectListener() {
//                    @Override
//                    public void onReceiveCompleted(List<BaseObject> objects, int resultState) {
//                        getDataSource().autoInsertUpdateByColumn(objects, "id");
//                        WaitingDialog.showWaitingDialog(null, false);
//                        load();
//                    }
//
//                    @Override
//                    public void onReceiveStep(List<BaseObject> objects, int resultState) {
//                        getDataSource().autoInsertUpdateByColumn(objects, "id");
//                    }
//                });
//            }
//
//            @Override
//            public void onReceiveStep(List<BaseObject> objects, int resultState) {
//                getDataSource().autoInsertUpdateByColumn(objects, "cid");
//            }
//        });

    }

    void update() {
        Utility.CheckInternet(this, new ConnectDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, boolean isConnect) {
                if (!isConnect) return;
                update2();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_images_list);
        Text.setText(findViewById(R.id.txt_title), getIntent().getStringExtra("title"), Font.TextPos.h1);
        gridView = (GridView) findViewById(R.id.grid_view);
        findViewById(R.id.img_close).setOnClickListener(this);
        findViewById(R.id.img_header_icon1).setOnClickListener(this);
        utils = new Utils(this);

        InitializeGridLayout();
        load();

    }

    void load() {
        Image select = new Image();
        select.catid = getIntent().getIntExtra("catid", -1);
        adapter = new GridViewImageAdapter(GalleryImagesActivity.this, (ArrayList<Image>) getDataSource().select(select));
        gridView.setAdapter(adapter);
    }

    private void InitializeGridLayout() {
        Resources r = getResources();
        padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
//		gridView.setPadding((int) padding, (int) padding, (int) padding,(int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_close) finish();
        else if (v.getId() == R.id.img_header_icon1) update();

    }
}
