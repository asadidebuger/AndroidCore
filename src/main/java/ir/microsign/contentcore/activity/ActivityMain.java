package ir.microsign.contentcore.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ir.microsign.api.activity.ActivityIntro;
import ir.microsign.api.activity.ActivitySignUp;
import ir.microsign.api.activity.ActivityTicket;
import ir.microsign.apphelper.dialog.DialogMenuListener;
import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.apphelper.object.MenuItem;
import ir.microsign.apphelper.view.MenuView;
import ir.microsign.R;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.dialog.DialogCategories;
import ir.microsign.contentcore.dialog.DialogMarked;
import ir.microsign.contentcore.dialog.Message;
import ir.microsign.contentcore.network.ContentUpdater;
import ir.microsign.contentcore.object.BookIndexItem;
import ir.microsign.contentcore.object.Category;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.Helper;
import ir.microsign.contentcore.object.Navigator;
import ir.microsign.contentcore.object.NavigatorEngine;
import ir.microsign.contentcore.utils.Menu;
import ir.microsign.contentcore.view.BookIndexItemView;
import ir.microsign.contentcore.view.CategoriesView;
import ir.microsign.contentcore.view.CategoryView;
import ir.microsign.contentcore.view.ContentView;
import ir.microsign.contentcore.view.IndexesView;
import ir.microsign.contentcore.view.NavigationPathView;
import ir.microsign.context.app;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.dialog.WaitingDialog;
import ir.microsign.net.DownloadFilesByMap;
import ir.microsign.net.Utility;
//import ir.microsign.payment.activity.ActivityPayment;
//import ir.microsign.payment.database.DataSourcePayment;
//import ir.microsign.payment.object.PaymentChecker;
//import ir.microsign.service.activity.ActivityNotifyLists;
import ir.microsign.settings.activity.ActivitySetting;
import ir.microsign.utility.Display;
import ir.microsign.utility.File;
import ir.microsign.utility.Font;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.view.DoubleProgressDialog;
import ir.microsign.view.ReshapeSolver;
import ir.microsign.view.ViewHtml;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ActivityMain extends Activity implements View.OnClickListener, Listener.MenuItemSelectedListener, NavigatorEngine,ViewHtml.OnSlideListener {
    public ImageView mImgSearch = null;
    public int mUpdateStepTotal = 0, mUpdateStep;
    public DoubleProgressDialog mProgressDialog = null;
    public ContentUpdater mContentUpdater = null;
    //	LinearLayout llCats = null;
    public ContentView mContentView = null;
    public IndexesView mIndexesView = null;
    public ImageView mImgBack = null, mImgFullScreen = null;
    public DialogMenuListener mDialogMenu = null;
    public MenuView mScrollMenu = null;
    public DrawerLayout mDrawerLayout = null;
    public TextView mTxtTitle = null;
//    public DialogEditProfile mDialogEditProfile = null;
    public ReceiveMessages myReceiver = null;
//    public ir.microsign.payment.database.DataSource mDataSourcePayment = null;
    public LinearLayout llRight = null;

    public Navigator mNavigator = null;
    public DialogMarked mDialogMarked = null;
    ContentUpdater.ContentUpdateListener mUpdateListener = new UpdateListener();
    View.OnClickListener mOnCategoryClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onCategoryViewClicked(v);
        }
    };
    View.OnClickListener mOnIndexItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onIndexItemClicked(v);
        }
    };
    NavigationPathView mNavigationPath = null;
    public LinearLayout mLlCats = null;
  public   CategoriesView mCategoriesView = null;
    String[] titles;
    DialogCategories dialogCategories=null;
    private DataSource mDataSource = null;

    public static void show(Context context, String rootTag) {
        Intent intent = new Intent(context, ActivityMain.class);
        intent.putExtra("tag", rootTag);
        context.startActivity(intent);

    }
WaitingDialog mWaitingDialog=null;
    public void showWaiting(){
        if (mWaitingDialog!=null&&mWaitingDialog.isShowing())return;
        mWaitingDialog=new WaitingDialog(this);
        mWaitingDialog.show();
    }
    public void hideWaiting(){

        if (mWaitingDialog!=null&&mWaitingDialog.isShowing())mWaitingDialog.dismiss();
    }
    public void onCategoryViewClicked(final View v) {
        showWaiting();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNavigator.onCategoryClicked(((CategoryView) v).getItem());
            }
        },50);


    }
    public void onCategoryViewClicked(final Category category) {
        showWaiting();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNavigator.onCategoryClicked(category);
            }
        },50);


    }

    public void onIndexItemClicked(final View v) {
       onIndexItemClicked(((BookIndexItemView) v).getItem());
    }


public void downloadAllImages(){
    showWaiting();
    new CheckImages().execute();
}
 List<Image> mNoExistImageList =null;
     private void downloadAllImages1(){
       hideWaiting();
        if (mNoExistImageList.size()<1){
            MessageDialog.showMessage(this,R.string.message_dowanload_all_images_nothing_title,R.string.message_dowanload_all_images_nothing_desc);
            return;
        }
        Message.showDowanloadAllImages(this,String.valueOf(mNoExistImageList.size()), new MessageDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, String key) {if (!ok)return;
                Utility.CheckInternet(ActivityMain.this, new ConnectDialog.OnDialogResultListener() {
                    @Override
                    public void OnDialogResult(boolean ok, boolean isConnect) {
                        if (!isConnect)return;
//                        net.tarnian.network.DownloadFiles downloadFiles=new DownloadFiles();
                        Map<String,String> map=new HashMap<>(mNoExistImageList.size());//new ArrayList<>();
                        for (int i = 0; i < mNoExistImageList.size(); i++) {
                            Image image = mNoExistImageList.get(i);
                            try {
                                map.put(image.getUrl(), image.getPath());
                            } catch (Exception ex) {
                                Log.e("showDowanloadAllImages",ex.getMessage()+":"+i);
                            }
                        }
                        new DownloadFilesByMap().GetFiles(ActivityMain.this, getString(R.string.download_all_images_progress_title), getString(R.string.download_all_images_progress_title_p1), map, new DownloadFilesByMap.DownloadCompletedListener() {
                            @Override
                            public void OnFileDownloaded(boolean succeed, String path) {

                            }

                            @Override
                            public void OnFinish(boolean allSucceed, String root) {
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        onDownloadAllImagesFinished();
    }
});
                            }
                        });
                    }
                });
            }
        });
    }
    public void onDownloadAllImagesFinished(){
        afterCreate();
    }
    class CheckImages extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            List<Image> imageList =getDataSource().getAllImages();
            mNoExistImageList =new ArrayList<>();
            for (Image image : imageList) {
                if (!File.Exist(image.getPath())) mNoExistImageList.add(image);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadAllImages1();
                }
            });

            return null;
        }
    }
    public void onIndexItemClicked(final BookIndexItem item) {
        showWaiting();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mNavigator.onListClicked(item);
//				new Handler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
						ir.microsign.dbhelper.object.BaseObject selected=mIndexesView.getSelected();
						if (selected==null)
						mIndexesView.setItemSelected(getDataSource().getBookIndexItems(item.parent_id));
						else {
							mIndexesView.setItemSelected(null);
							mIndexesView.setItemSelected(selected);

						}
//					}
//				},50);

			}
		},50);


    }

    public void afterUpdateFinished() {
		mNavigator.nullCatRoot();
        afterCreate();
    }

    public void finishDownload(boolean succeed) {
        try {
            mProgressDialog.hide();
            Display.releaseScreen(ActivityMain.this);
            if (succeed)
            afterUpdateFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void startDownload() {
        mProgressDialog = new DoubleProgressDialog(ActivityMain.this, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mContentUpdater.cancel();
            }
        });
        mUpdateStep = 1;
        Display.fixScreen(ActivityMain.this);
    }

    public void updateProgress(int titleP2Id, int p2, int max2) {

        mProgressDialog.show(getString(R.string.update_content_title), getString(R.string.update_content_p1_title), getString(titleP2Id), "%d/%d", "%d/%d", mUpdateStep, mUpdateStepTotal, p2, max2, true);
    }
//ActivityContentUpdaterUi mContentUpdaterAllUi=null;

    public boolean getMustUpdateStepByStep(){
      return   getResources().getBoolean(R.bool.content_updater_step_updating);
    }
    public void updateContents(final String catId,final String catAlias) {
        Utility.CheckInternet(ActivityMain.this, new ConnectDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, boolean isConnect) {
                if (!isConnect) return;
                if (!getMustUpdateStepByStep()) {
                    ActivityContentUpdaterUi.show(ActivityMain.this,false,2000);
                }else {
                    mUpdateStep = 0;
                    mUpdateStepTotal = 5;
                    mContentUpdater = new ContentUpdater(ActivityMain.this, catId,catAlias, mUpdateListener);
                    mContentUpdater.start();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        regReceiver();
    }

    public void regReceiver() {
        if (myReceiver == null) myReceiver = new ReceiveMessages();
        myReceiver.register(this);
    }

    public void unregReceiver() {
        if (myReceiver == null) return;
        myReceiver.unRegister(this);
    }

    public DataSource getDataSource() {
        return DataSource.getDataSource();
    }

//    public DataSourcePayment getDataSourcePayment() {
//         return DataSourcePayment.getDataSource(this);
////        if (mDataSourcePayment == null) mDataSourcePayment = new ir.microsign.payment.database.DataSourcePayment(this);
////        return mDataSourcePayment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(savedInstanceState);
        afterCreate();

    }

    public void setContentView(Bundle savedInstanceState) {
        setContentView(R.layout.layout_main);

        titles= getResources().getStringArray(R.array.book_header_buttons);
        for (int i = 0; i < titles.length; i++) {
            View view = findViewById(getResources().getIdentifier(String.format(Locale.ENGLISH, "%s%d", "txt_top_menu", i), "_id", getPackageName()));
            Text.setText(view, titles[i], Font.TextPos.h1);
            view.setOnClickListener(this);
            view.setTag(i);
        }
        mIndexesView = new IndexesView(this);
        ((LinearLayout) findViewById(R.id.ll_part_left)).addView(mIndexesView, -1, -1);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        findViewById(R.id.img_header_settings).setOnClickListener(this);
        findViewById(R.id.img_header_setting2).setOnClickListener(this);
        mImgBack = (ImageView) findViewById(R.id.img_header_back);
        mImgSearch = (ImageView) findViewById(R.id.img_search);
        mImgFullScreen = (ImageView) findViewById(R.id.img_full_screen);
//		mImgBack.setImageResource(R.drawable.icon_close);
//		mImgSearch.setImageResource(R.drawable.icon_search);
        view.setVisibility(mImgSearch, true);
        mImgBack.setOnClickListener(this);
        mImgSearch.setOnClickListener(this);
        mImgFullScreen.setOnClickListener(this);
        mScrollMenu = (MenuView) findViewById(R.id.slide_menu);
        mScrollMenu.addItems(getSlideMenu());
        mScrollMenu.setMenuItemSelected(this);
//		myReceiver = new ReceiveMessages();

//        ActivityFeedBack.showNewQuestionExist(this);

        mNavigator = new Navigator(this, this, getDataSource(), getIntent().getStringExtra("tag"), savedInstanceState == null ? null : savedInstanceState.getBundle("mnavigator"));

    }

    public MenuItem[] getSlideMenu() {
        return Menu.getLeftMenuItems();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void afterCreate() {
        if (mNavigator == null) return;
        mNavigator.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        saveNavigatorState(outState);
    }

    public void saveNavigatorState(Bundle outState) {
        if (mNavigator == null) return;
        outState.putBundle("mnavigator", mNavigator.saveState(new Bundle()));
    }

    public String onUrlReceived(Uri uri) {
        String data = uri.toString();
        String prefix = Content.getHelper().getPrefix();
        if (data.startsWith(prefix + ":article")) {
            String[] parts = data.split("\\.");
            final String id = parts[1];
            final String link = parts.length > 2 ? parts[2] + parts[3] : null;
            if (id == null) return null;
            if (Text.isNullOrEmpty(link)){
                onIndexItemClicked(getDataSource().getBookIndexItemByContentId(id));
            }
            else {
                showWaiting();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            mNavigator.navigate(id, link, -1);
                    }
                }, 50);
            }
            return null;
        }
        if (data.startsWith(prefix + ":category")) {
            String[] parts = data.split("\\.");
            final String id = parts[1];
            if (id == null) return null;
            onCategoryViewClicked(Helper.getHelper().getDataSource().getCategory(id));
            return null;
        }
        if (data.startsWith(prefix + ":payment")) {
            String[] parts = data.split(":");
            String id = parts[parts.length - 1];
            purchase(id);
            return null;
        }
        if (data.startsWith(prefix + ":update")) {
            String[] parts = data.split(":");
            String id = parts[parts.length - 1];
            updateContents(id,null);
            return null;
        } else if (data.startsWith(prefix + ":mark")) {
            int pos = getContentPosition();
            String d = data.substring((prefix + ":mark:").length());
            String[] address = d.split("\\.");
            if (address[0].equalsIgnoreCase("important"))
                getDataSource().setMark( address[2], "h1", Integer.valueOf(address[3]), Integer.valueOf(address[4]) > 0 ? 0 : 1, Integer.valueOf(address[5]));
            else
                getDataSource().setMark(address[2], "h1", Integer.valueOf(address[3]), Integer.valueOf(address[4]), Integer.valueOf(address[5]) > 0 ? 0 : 1);
            scrollToContent(address[2], pos);
            return null;
        }
        return data;
    }

//

    String getLinkArticleId(String dicLink, String type) {
        String[] sep = dicLink.split("\\.");
        for (int i = 0; i < sep.length; i++) {
            if (sep[i].contains(type)) return sep[i + 1];
        }
        return null;
    }

    public MenuItem[] getDialogMenuItems(){
     return Menu.getRightMenuItems(false);
    }
    public void CreateMenu() {

        mDialogMenu = new DialogMenuListener(this);
        MenuView menu = new MenuView(this);
        menu.addItems(getDialogMenuItems());
        mDialogMenu.setViewMenu(menu);
        mDialogMenu.setMenuItemSelected(this);
        mDialogMenu.setPosition(new int []{Display.getWidth(this),20});//findViewById(R._id.img_header_settings));

    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        showMenu(mDialogMenu == null || !mDialogMenu.isShowing());
        return true;
    }

    LinearLayout getRightPanel() {
        if (llRight == null) llRight = (LinearLayout) findViewById(R.id.ll_part_right);
        return llRight;
    }

    public void setRightPanelByContent(BookIndexItem selectedItem, String scrollTagId, int scrollPosition) {
        if (selectedItem == null) {
            getRightPanel().removeAllViews();
            mContentView = null;
            return;
        }
        setupContentView();

        mContentView.setContent(getDataSource(), selectedItem.content_id, scrollTagId, scrollPosition);
        mLastFullContent=selectedItem.content_id;
		hideWaiting();
    }

    public void setupContentView() {
        if (mContentView == null) {

            mContentView = new ContentView(this);
            mContentView.setOnSlideListener(this);
            getRightPanel().addView(mContentView, -1, -1);

        }
    }

    public void scrollToContent(String id, int scrollPosition) {
        setupContentView();
        mContentView.setContent(getDataSource(), id, null, scrollPosition);

    }

    public void setContentViewByText(String html) {
        if (Text.isNullOrEmpty(html)) {
            getRightPanel().removeAllViews();
            mContentView = null;
            return;
        }
        if (mContentView == null) {
            mContentView = new ContentView(this);
            mContentView.setOnSlideListener(this);
            getRightPanel().addView(mContentView, -1, -1);
        }



        mContentView.setDbObject(null);
        mContentView.setContent(html, false, 5);
        mContentView.showNextPage(false);
    }

    @Override
    public void onBackPressed() {
	//	showWaiting();
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {

				if (mNavigator.onBackPressed())ActivityMain.this.finish();
//			}
//		},50);

    }

    public void purchase(String access) {
//        Display.fixScreen(this);
//        ActivityPayment.lunchPayment(this,  access, null, null,true, new
//                ir.microsign.payment.Interfaces.Listener.PaymentListener() {
//                    @Override
//                    public void onPaymentFinished(PaymentChecker paymentChecker, int result) {
//                        if (paymentChecker != null && paymentChecker.isPurchased())
//                            afterSuccessPayment();
//                        else {
//                            Display.releaseScreen(ActivityMain.this);
//                            afterCreate();
//                        }
//                    }
//                });
    }

    public void purchase(String sku,String extra) {
//        Display.fixScreen(this);
//        ActivityPayment.lunchPayment(this, null, sku, extra,true, new
//                ir.microsign.payment.Interfaces.Listener.PaymentListener() {
//                    @Override
//                    public void onPaymentFinished(PaymentChecker paymentChecker, int result) {
//                        if (paymentChecker != null && paymentChecker.isPurchased())
//                            afterSuccessPayment();
//                        else {
//                            Display.releaseScreen(ActivityMain.this);
//                            afterCreate();
//                        }
//                    }
//                });
    }

    public void afterSuccessPayment() {
        if (getMustUpdateStepByStep())  updateContents(getResources().getBoolean(R.bool.update_all_after_payment) ? null : getParentIdForUpdate(),null);
        else afterUpdateFinished();
    }

    public void setTitle(String title) {
        if (isSearchMode()) return;
        if (mTxtTitle == null) mTxtTitle = (TextView) findViewById(R.id.txt_header_title);
        Text.setText(mTxtTitle, title, Font.TextPos.h1, false);
    }

    boolean isSearchMode() {
        return false;
    }

   public void showMenu(boolean show) {
        CreateMenu();
        if (show) mDialogMenu.show();
        else mDialogMenu.dismiss();
    }

    @Override
    public void onClick(View v) {
        int i = v.getTag() == null ? -1 : (int) v.getTag();
        if (i > -1) {
            onTopMenuClicked(i);
//			if (i == 3) {
//				startSettingActivity();
//				return;
//			}
            return;

        }
        if (v.getId() == R.id.img_header_back) onBackPressed();
        else if (v.getId() == R.id.img_full_screen) onFullScreenClicked();
        else if (v.getId() == R.id.img_header_setting2)
            mDrawerLayout.openDrawer(Gravity.LEFT);
        else if (v.getId() == R.id.img_search) showSearch();
//		else if (v.getId() == R._id.btn_update)
//			updateContents(getParentIdForUpdate());
        else showMenu(true);

    }

    public void setFullScreenBtn() {
        boolean visible = mIndexesView != null && mIndexesView.canVisible();
        view.setVisibility(mImgFullScreen, visible);
        getCategoriesView().setVisible(mIndexesView.isVisible()||!visible);

        mImgFullScreen.setImageResource(mIndexesView.isVisible() ? R.drawable.icon_full_screen_white : R.drawable.icon_return_from_full_screen_white);
    }

    public void onFullScreenClicked() {
        if (mIndexesView == null) return;
        mIndexesView.reverseVisible();
//        view.setVisibility(getCategoriesView(), mIndexesView.isVisible()||!mIndexesView.canVisible());
        setFullScreenBtn();


    }

    public void onTopMenuClicked(int index) {
        if (index == 1 || index == 2) {
            showMarked(getResources().getStringArray(R.array.book_header_buttons)[index], index == 2 ? 1 : 0, index == 1 ? 1 : 0);
        } else if (index == 0) showMenu(true);
        else if (index == 3) showFullCats();
        else if (index == 4) showSearch();
    }

void showFullCats(){
    dialogCategories=new DialogCategories(this,titles[3],mNavigator.getRootCat()._id);
    dialogCategories.show();

    dialogCategories.setOnDialogResultListener(new MessageDialog.OnDialogResultListener() {
        @Override
        public void OnDialogResult(boolean ok, String key) {

            if (ok){
                Category cat=getDataSource().getCategory(key);
                mNavigator.onCategoryClicked(cat);
                mCategoriesView.scrollToItem(cat);
//                selectCategory();
            }
            dialogCategories.hide();
        }
    });
}
    public void showMarked(String title, int marked, int learned) {
        mDialogMarked = new DialogMarked(this, title, mNavigator.getParentCatId(), mNavigator.getSelectedCatId(), marked, learned);
        mDialogMarked.setOnDialogResultListener(new MessageDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, String key) {
                if (ok) mDialogMarked.hide();
                if (ok || mNavigator.getSelectedContent() == null) return;
                if (key == null) {
                    return;
                }
                String[] parts = key.split("\\.");
                if (!String.valueOf(mNavigator.getSelectedContent()._id).equals(parts[2])) return;
                int pos = Display.pxToDp(ActivityMain.this, mContentView.getRichWebView().getScrollY());
                scrollToContent(mNavigator.getSelectedContent()._id, pos);
            }
        });
        mDialogMarked.show();
    }

    public void showSearch() {
        ActivitySearch.show(this, mNavigator.getTagRootCat(), mNavigator.canExit() ? "": mNavigator.getSelectedCatId());
    }

    public void showProfile() {
        ActivitySignUp.show(this);
//        mDialogEditProfile = new DialogEditProfile(this);
//        mDialogEditProfile.setOnResultListener(new ir.microsign.settings.interfaces.Listener.ResultListener() {
//            @Override
//            public void onResult(Object o, int i) {
//                mDialogEditProfile.hide();
//            }
//        });
//        mDialogEditProfile.show();
    }

    @Override
    public void onItemSelected(MenuItem selectedItem) {

        if (selectedItem.equals(Menu.mReshape))
            ReshapeSolver.Show(this);

        else if (selectedItem.equals(Menu.mAbout))
            startAboutUsActivity();
        else if (selectedItem.equals(Menu.mUserProfile))
            showProfile();
        else if (selectedItem.equals(Menu.mExit)) {
            app.exitApp(this);
        } else if (selectedItem.equals(Menu.mReport))
            startReportActivity();
        else if (selectedItem.equals(Menu.mSettings))
            startSettingActivity();
        else if (selectedItem.equals(Menu.mShare))
            app.share(this);
        else if (selectedItem.equals(Menu.mCheckUpdate))
            updateContents(getParentIdForUpdate(),null);
        else if (selectedItem.equals(Menu.mDownloadAllImages))
            downloadAllImages();
        else if (selectedItem.equals(Menu.mFirstPage))
            finish();
        else if (selectedItem.equals(Menu.mSearch))
            showSearch();
        else if (selectedItem.equals(Menu.mFav))
            showMarked(getString(R.string.menu_desc_fav), 1, 0);
//        else if (selectedItem.equals(Menu.mNotifications))
//            ActivityNotifyLists.show(this);
    }

    public void startAboutUsActivity() {
        ActivityIntro.show(this);
    }

    public void startReportActivity() {
        ActivityTicket.show(this);
    }

    public void startSettingActivity() {
        Intent settingIntent = new Intent(this, ActivitySetting.class);
        startActivity(settingIntent);
    }

    public String getParentIdForUpdate() {
        return null;
    }

    public void onEventRequest(int code, Object... args) {
        if (code == 0) onBackPressed();
        else if (code == 1) updateContents(getParentIdForUpdate(),null);
        else if (code == 2) {
//			Payment payment = (Payment) args[0];
//			if (payment == null) {
//				payment = new Payment();
//				payment.useraccesslevels = (Integer) args[1];
//			}
            purchase((String) args[1]);
        }
    }

    @Override
    public void finish() {
//		File.copyDatabases(this);
        hideWaiting();
        if (!getResources().getBoolean(R.bool.show_please_rate_on_exit)) {
            unregReceiver();
            super.finish();
            return;
        }
        if (app.getAppRateLunched(this)) {
            unregReceiver();
            super.finish();
            app.mRateLunched = false;
        } else app.showPleaseRate(this);


    }

    public void forceFinish() {
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == 0) {
            app.setAppRated(this);
            finish();
        }else if (requestCode==2000){afterUpdateFinished();}
    }

    public CategoriesView getCategoriesView() {
        if (mCategoriesView != null) return mCategoriesView;
        if (mLlCats == null) mLlCats = (LinearLayout) findViewById(R.id.ll_categories_container);
        if (mLlCats.getChildCount() > 0) {
            mCategoriesView = (CategoriesView) mLlCats.getChildAt(0);
            if (mCategoriesView != null)
                return mCategoriesView;
        }
        mCategoriesView = new CategoriesView(this);
        mLlCats.addView(mCategoriesView, -1, getResources().getDimensionPixelOffset(R.dimen.category_item_height));
        return mCategoriesView;

    }

    @Override
    public void setCategories(Category parent) {
        getCategoriesView().setItems( parent._id, mOnCategoryClicked);
    }

    @Override
    public void selectCategory(Category selected) {
        getCategoriesView().setItemSelected(selected);
    }

    @Override
    public void setCategoryDescription(Category selected) {


        setContentViewByText(Content.getHelper().getHtmlFullText(Content.getHelper().getContentFromCategoryDescription(selected),this));
    }

    @Override
    public void setIndexList(Category parentCat, BookIndexItem parentItem, BookIndexItem selectedItem) {
//		if (mSelectedIndex==null||getDataSource().getBookIndexItemHaveChilds(mSelectedIndex))
        mIndexesView.setItems(getDataSource(), parentCat._id, parentItem == null ? "" : parentItem._id, selectedItem == null ? "": selectedItem._id, mOnIndexItemClicked);

    }

    @Override
    public void setIndexListSelected(BookIndexItem selectedItem) {
        mIndexesView.setItemSelected(selectedItem);
        mIndexesView.ScrollToItem(selectedItem);
    }
String mLastFullContent="";
    @Override
    public void setRightPanelByContentList(BookIndexItem selectedItem) {
        setupContentView();
        mContentView.setContent(getDataSource(), selectedItem,mLastFullContent);
        mLastFullContent="";
    }

    @Override
    public void refreshIcons(boolean exit, boolean canFullScreen) {
        mImgBack.setImageResource(exit ? R.drawable.icon_exit_white : R.drawable.icon_back_white);
        setFullScreenBtn();
    }

    @Override
    public void pathChanged(Category category, BookIndexItem bookIndexItem, Content content) {
        if (mNavigationPath == null) mNavigationPath = (NavigationPathView) findViewById(R.id.navigation_path);
        mNavigationPath.setPath(getDataSource(), bookIndexItem, content);
    hideWaiting();
    }

    @Override
    public int getContentPosition() {
        if (mContentView == null) return 0;
        return Display.pxToDp(this, mContentView.getRichWebView().getScrollY());
    }
	Content mSlideContent=null;
    @Override
    public void onSlide(final boolean next) {
		showWaiting();
		new Handler().postDelayed(new Runnable() {
			@Override
		public void run() {
        if(mNavigator.getSelectedContent()==null||mNavigator.getSelectedIndex()==null){hideWaiting();return;}
		mSlideContent=getDataSource().getNextContent(mNavigator.getSelectedContent()._id,next);
        if (mSlideContent==null){hideWaiting();return;}
			mNavigator.navigate(mSlideContent._id,null,-1);
			}
		},50);


    }


    class UpdateListener implements ContentUpdater.ContentUpdateListener {
        @Override
        public void OnPrepare() {
            startDownload();
            updateProgress(R.string.update_content_p2_prepare, 0, 1);
        }

        @Override
        public void OnPrepared(boolean succeed) {
            updateProgress(R.string.update_content_p2_prepare, 1, 1);
            mUpdateStep++;
        }

        @Override
        public void OnCategoriesReceive(int last, int total) {

            updateProgress(R.string.update_content_p2_cats, last, total);
        }

        @Override
        public void OnCategoriesReceiveEnd(int total) {
            updateProgress(R.string.update_content_p2_cats, total, total);
            mUpdateStep++;
        }

        @Override
        public void OnListReceive(int last, int total) {
            updateProgress(R.string.update_content_p2_book_list, last, total);
        }

        @Override
        public void OnListReceiveEnd(int total) {
            updateProgress(R.string.update_content_p2_book_list, total, total);
            mUpdateStep++;
        }


        @Override
        public void OnUpdatableContentsProceed(int total) {
            updateProgress(R.string.update_content_p2_changes_proceed, total, total);
            mUpdateStep++;
            if (total < 1) {
                finishDownload(true);
                new MessageDialog(ActivityMain.this).show(R.string.update_content_title_no_new_update, R.string.update_content_desc_no_new_update);
            }
        }

        @Override
        public void OnContentReceive(int last, int total) {
            updateProgress(R.string.update_content_p2_contents, last, total);

        }

        @Override
        public void OnContentReceiveEnd(int total) {
            updateProgress(R.string.update_content_p2_contents, total, total);
            mUpdateStep++;
        }


        @Override
        public void OnFinished(boolean succeed) {


            finishDownload(succeed);
            new MessageDialog(ActivityMain.this).show(succeed ? R.string.update_content_title_update_succeed : R.string.update_content_title_update_failed, succeed ? R.string.update_content_desc_update_succeed : R.string.update_content_desc_update_failed);
        }

        @Override
        public void OnCanceled() {
            finishDownload(false);
        }
    }

    public class ReceiveMessages extends BroadcastReceiver {

        public boolean registered = false;

        public void unRegister(Context context) {
            context.unregisterReceiver(this);
            registered = false;
        }

        public void register(Context context) {
            if (registered) return;
            IntentFilter intentFilter = new IntentFilter(Content.getHelper().getAction());
            intentFilter.addDataScheme(Content.getHelper().getPrefix());
            context.registerReceiver(this, intentFilter);
            registered = true;

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            onUrlReceived(intent.getData());
        }
    }

}