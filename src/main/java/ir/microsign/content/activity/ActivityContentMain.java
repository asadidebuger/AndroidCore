package ir.microsign.content.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.widget.SweetAlertDialog;
import ir.microsign.api.activity.ActivityIntro;
import ir.microsign.api.activity.ActivitySignUp;
import ir.microsign.api.activity.ActivityTicket;
import ir.microsign.api.object.Profile;
import ir.microsign.api.view.ProfileImageView;
import ir.microsign.apphelper.dialog.DialogMenuListener;
import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.apphelper.object.MenuItem;
import ir.microsign.apphelper.view.MenuView;
import ir.microsign.content.Const;
import ir.microsign.R;
import ir.microsign.content.database.DataSource;
import ir.microsign.content.dialog.DialogMarkup;
import ir.microsign.content.fragment.FragmentCategory;
import ir.microsign.content.fragment.FragmentContent;
import ir.microsign.content.network.ContentUpdater;
import ir.microsign.content.object.Content;
import ir.microsign.content.object.ContentAlert;
import ir.microsign.content.view.ContentAlertView;
import ir.microsign.contentcore.activity.ActivityContentUpdaterUi;
import ir.microsign.contentcore.dialog.Message;
import ir.microsign.contentcore.object.Category;
import ir.microsign.context.app;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.dialog.WaitingDialog;
import ir.microsign.net.Utility;
import ir.microsign.settings.activity.ActivitySetting;
import ir.microsign.utility.Display;
import ir.microsign.utility.File;
import ir.microsign.utility.Font;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.view.DoubleProgressDialog;
import ir.microsign.view.ReshapeSolver;

//import net.tarnian.content.object.BaseObject;

//import net.tarnian.aboutus.ActivityAboutUs;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ActivityContentMain extends ActivityBase implements Listener.DbItemSelectedListener, View.OnClickListener, Listener.MenuItemSelectedListener, Listener.FragmentListener, Listener.EventRequestListener {
    public static AfterUpdate mAfterUpdate = null;
    static boolean mLastIsLandscape = true;
    static String mParentId ="";
    static DoubleProgressDialog mProgressDialog = null;
    static ContentUpdater mContentUpdater = null;
    static ContentUpdater.ContentUpdateListener mUpdateListener;
    static int mUpdateStepTotal = 0, mUpdateStep;
    public static Activity mActivity = null;
    public static ActivityContentMain mActivityContentMain = null;
    public String mTitle = null;
    String mRootTag = null;
    LinearLayout mRoot = null;
    FragmentManager mFragmentManager = null;
    FragmentTransaction mFragmentTransaction = null;
    FragmentCategory mFragmentCategory = null;
    FragmentContent mFragmentContent = null;
    DialogMenuListener mDialogMenu = null;
    MenuView mScrollMenu = null;
    DrawerLayout mDrawerLayout = null;
    ImageView mImgBack = null;
    ImageView mImgSearch = null;
    Category mSelectedCategory = null;
    boolean mPaused = false;
    LinearLayout mLL1, mLL2;
    //	WaitingDialog mWaitingDialog = null;
//	int updateNum = 0;
    TextView mTxtTitle = null;
//    DialogEditProfile mDialogEditProfile = null;
    boolean started = false;
    String mRootId = "";

    public static void show(Context context, String rootTag, String splitText) {
        Intent intent = new Intent(context, ActivityContentMain.class);
        intent.putExtra("tag", rootTag);
        if (!Text.isNullOrEmpty(splitText)) intent.putExtra("splittext", splitText);
        Category category=DataSource.getDataSource().getCategoryByTag(rootTag);
        if (category!=null)
        mParentId =category._id;
        else mParentId=null;
        context.startActivity(intent);

    }

    public static void show(Context context, String rootTag) {
        show(context, rootTag, null);

    }

    static void stopDownload() {
//		mWaitingDialog.hide();
        Display.releaseScreen(mActivity);
        mProgressDialog.hide();
    }

    static void finishDownload() {
        try {
            mProgressDialog.hide();
            Display.releaseScreen(mActivity);
            if (mAfterUpdate != null) mAfterUpdate.updateFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void startDownload() {
        mProgressDialog = new DoubleProgressDialog(mActivity, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mContentUpdater.cancel();
            }
        });
        mUpdateStep = 1;
        Display.fixScreen(mActivity);
    }

    static void updateProgress(int titleP2Id, int p2, int max2) {

        mProgressDialog.show(mActivity.getString(R.string.update_content_title), mActivity.getString(R.string.update_content_p1_title), mActivity.getString(titleP2Id), "%d/%d", "%d/%d", mUpdateStep, mUpdateStepTotal, p2, max2, true);
    }

    public static void updateContents( Activity activity, final String catId, final String catAlias) {
        mActivity = activity;
        Utility.CheckInternet(activity, new ConnectDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, boolean isConnect) {
                if (!isConnect) return;
                if (!mActivity.getResources().getBoolean(R.bool.content_updater_step_updating))
                    ActivityContentUpdaterUi.show(mActivity,false,2000);
                else
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateStep = 0;
                        mUpdateStepTotal = 5;
                        mUpdateListener = new UpdateListener();
                        mContentUpdater = new ContentUpdater(mActivity, catId,catAlias, mUpdateListener);
                        mContentUpdater.start();
                    }
                },300);

            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        mActivity=this;
        mActivityContentMain=this;
        super.setContentView(R.layout.layout_activity_content);
        mRootTag = getIntent().getStringExtra("tag");
        View refresh = findViewById(R.id.img_header_icon1);
        view.setVisibility(refresh, true);
        refresh.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//		findViewById(R._id.img_header_settings).setOnClickListener(this);
        findViewById(R.id.img_header_setting2).setOnClickListener(this);
        findViewById(R.id.ll_header_menu0).setOnClickListener(this);
//		findViewById(R._id.img_header_icon).setOnClickListener(this);
        mImgBack = (ImageView) findViewById(R.id.img_header_back);
        mImgSearch = (ImageView) findViewById(R.id.img_search);
        view.setVisibility(mImgSearch, true);
        mImgBack.setOnClickListener(this);
        mImgSearch.setOnClickListener(this);
        mLastIsLandscape = Display.isLandscape(this);
        mScrollMenu = (MenuView) findViewById(R.id.slide_menu);
//        mScrollMenu.addItems(ir.microsign.content.utils.Menu.getLeftMenuItems());

        MenuItem[] menuItems = ir.microsign.content.utils.Menu.getLeftMenuItems();
        MenuItem[] menuItems1 = new MenuItem[menuItems.length + 1];
        menuItems1[0] = new MenuItem() {

            @Override
            public View getView(Context context) {
                return getProfileView();
            }
        };
        int i = 1;
        for (MenuItem item : menuItems) {
            menuItems1[i++] = item;
        }
        menuItems = menuItems1;


        mScrollMenu.addItems(menuItems);
        mScrollMenu.setMenuItemSelected(this);


        mScrollMenu.setMenuItemSelected(this);
//		mEtxtSearch= (EditText) findViewById(R._id.etxt_search);
//        ActivityFeedBack.showNewQuestionExist(this);

//        ReshapeSolver.ShowForFirst(this);

//		downloadAllImages();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(getPackageName()+".broadcast.login_signals");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data=intent.getStringExtra("action");
            switch (data){
                case "sign_in":
                    onSignIn();
                    break;
                case "sign_out":
                    onSignOut();
            }
        }
    };

    public void onSignIn() {

        if (profileImageView!=null) profileImageView.checkChanges();
    }
    public void onSignOut() {
        if (profileImageView!=null) profileImageView.checkChanges();
    }
    ProfileImageView profileImageView=null;

    View getProfileView(){
        if (profileImageView!=null)return profileImageView;
        Profile profile=Profile.getSavedProfileOrEmpty(this);
        return profileImageView=profile.getProfileImageView(this);

    }
    //   Payment mPayment=null;
    void CreateMenu() {

//		if (mDialogMenu == null) {
        mDialogMenu = new DialogMenuListener(this);
        MenuView menu = new MenuView(this);
        menu.addItems(ir.microsign.content.utils.Menu.getRightMenuItems(mFragmentContent != null && mFragmentContent.mPayment != null));
        mDialogMenu.setViewMenu(menu);
        mDialogMenu.setMenuItemSelected(this);
//		}
        mDialogMenu.setPosition(findViewById(R.id.img_header_settings));

    }

    @Override
    protected void onStart() {
//		File.copyDatabases(this);
        super.onStart();
        if (!mPaused) onOrientationChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityContentMain.mAfterUpdate = new AfterUpdate() {
            @Override
            public void updateFinished() {
                setupParentCategory(false);
            }
        };
    }

    @Override
    protected void onPostResume() {

        super.onPostResume();

        if (mLastIsLandscape != Display.isLandscape(this))
            onOrientationChanged();

    }
    public void toggleDrawer() {
        if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.openDrawer(Gravity.LEFT);
        else mDrawerLayout.closeDrawers();
    }
    public void openDrawer(boolean open) {
        if (open)
            mDrawerLayout.openDrawer(Gravity.LEFT);
        else mDrawerLayout.closeDrawers();
    }
    public boolean drawerIsOpen() {
        return mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        showMenu(mDialogMenu == null || !mDialogMenu.isShowing());
        return true;
//		return super.onPrepareOptionsMenu(menu);
    }

    public void onOrientationChanged() {
        setView();
        mFragmentCategory.OnItemSelected(mSelectedCategory);
        mPaused = false;
    }

    @Override
    protected void onPause() {
        mPaused = true;
        mSelectedCategory = mFragmentCategory.getParentCategory();
        mLastIsLandscape = Display.isLandscape(this);
        super.onPause();
    }


    void setView() {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mRoot = (LinearLayout) findViewById(R.id.ll_fragments);
            mFragmentCategory = new FragmentCategory();
            mFragmentCategory.setFragmentListener(this);
            mFragmentContent = new FragmentContent();

            mFragmentCategory.setOnItemSelected(this);
            mFragmentContent.setOnItemSelected(this);
            mFragmentContent.setEventRequestListener(this);
            mLL1 = new LinearLayout(this);
            mLL1.setId(R.id.ll_part_right);
            mLL2 = new LinearLayout(this);
            mLL2.setId(R.id.ll_part_left);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, isLandscape() ? -1 : -2);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(-1, isLandscape() ? -1 : -2);

            lp.weight = 1;
            lp2.weight = 2;
            mLL1.setLayoutParams(lp);
            mLL2.setLayoutParams(lp2);
            mRoot.addView(mLL1);
            mRoot.addView(mLL2);
            mRoot.setOrientation(isLandscape() ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
            mFragmentTransaction.add(isLandscape() ? mLL1.getId() : mLL2.getId(), mFragmentContent);
            mFragmentTransaction.add(isLandscape() ? mLL2.getId() : mLL1.getId(), mFragmentCategory);

            mFragmentTransaction.commit();
            return;
        }

        mFragmentTransaction = mFragmentManager.beginTransaction();
        mRoot.setOrientation(isLandscape() ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        mFragmentTransaction.replace(isLandscape() ? mLL1.getId() : mLL2.getId(), mFragmentContent);
        mFragmentTransaction.replace(isLandscape() ? mLL2.getId() : mLL1.getId(), mFragmentCategory);
//	mRoot.requestLayout();
        mFragmentTransaction.commit();


    }


//    void purchase(Price payment) {
//        Display.fixScreen(this);
//        ActivityPayment.lunchPayment(this, payment.permission,payment.sku, null,true,  new
//                ir.microsign.payment.Interfaces.Listener.PaymentListener() {
//                    @Override
//                    public void onPaymentFinished(PaymentChecker paymentChecker, int result) {
//                        if (paymentChecker != null && paymentChecker.isPurchased())
//                            updateContents(mActivity, null,null);//(getResources().getBoolean(R.bool.update_all_after_payment)) ? -1 : getParentIdForUpdate());
//                        else Display.releaseScreen(ActivityContentMain.this);
//                    }
//                });
//    }

    void setupParentCategory(boolean byTag) {
        mParentId = (byTag || Text.isEmpty(mParentId )) ? getRootIdByTag() : mParentId;
        if (Text.isEmpty(mParentId )) {
            setCover(true);
            return;
        }
        setCover(false);
        mFragmentCategory.setRoot(getRootIdByTag());
        mFragmentCategory.setParent(mParentId);

    }

    void setCover(boolean set) {
        LinearLayout root = (LinearLayout) findViewById(R.id.ll_fragments);
        hideCategories(set);
        hideContents(set);
        int coverId = 111122;
        View coverView = findViewById(coverId);
        if (coverView == null) {
            ContentAlertView cover = new ContentAlertView(this, getContentAlert());
            cover.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            cover.setId(coverId);
            root.addView(cover);
            coverView = findViewById(coverId);
            ((ContentAlertView) coverView).setEventRequestListener(new Listener.EventRequestListener() {
                @Override
                public void onEventRequest(int code, Object... args) {
                    if (code == 1) finish();
                    else if (code == 2) updateContents(ActivityContentMain.this, getParentIdForUpdate(),null);
                }
            });
//			coverView.findViewById(R._id.btn1).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					finish();
//				}
//			});
//			coverView.findViewById(R._id.btn2).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					updateContents(ActivityContentMain.this,getParentIdForUpdate());
//				}
//			});
//			coverView.findViewById(R._id.img_icon).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					updateContents(ActivityContentMain.this,getParentIdForUpdate());
//				}
//			});
        }
        view.setVisibility(coverView, set);

    }

    ContentAlert getContentAlert() {
        return new ContentAlert(getString(R.string.content_alert_down_title),
                getString(R.string.content_alert_down_desc1),
                null,
                getString(R.string.content_alert_back),
                getString(R.string.content_alert_download),
                getResources().getDrawable(R.drawable.icon_download_content)

        );
    }

    String getRootIdByTag() {
        if (!Text.isEmpty(mRootId )) return mRootId;
        Category category = new Category();
        category.alias = mRootTag;
        category = (Category) getDataSource().selectFirst(category);
        if (category == null) return "";
        mRootId = category._id;
        return mRootId;
    }

    LinearLayout getFrameContents() {
        return isLandscape() ? mLL1 : mLL2;
    }

    LinearLayout getFrameCategories() {
        return isLandscape() ? mLL2 : mLL1;
    }

    void hideCategories(boolean hide) {
        view.setVisibility(getFrameCategories(), !hide);
    }

    void hideContents(boolean hide) {
        view.setVisibility(getFrameContents(), !hide);
    }

    void setFramesVisibility() {
        int contents = mFragmentContent.getItems().size();
        int cats = mFragmentCategory.getSubCategories(mFragmentCategory.getParentCategory()).size();

        boolean hideContents = !isLandscape() && contents < 1 && !mFragmentCategory.parentIsRoot() && mFragmentCategory.hasChild();
        boolean hideCats = cats < 1;

        hideContents(hideContents);
        hideCategories(hideCats);
    }

    @Override
    public void onItemSelected(BaseObject selectedItem) {

        if (selectedItem instanceof Category) {

            Category selectedCategory = ((Category) selectedItem);
//			mPayment= mFragmentContent.mPayment;
            mParentId = selectedCategory._id;
            mTitle = selectedCategory.title;
            setTitle(selectedCategory.title);
            List<Category> subCats = (List<Category>) mFragmentCategory.getSubCategories(selectedCategory);
            int subcats = subCats == null ? 0 : subCats.size();
            mFragmentContent.setContents(selectedCategory, selectedCategory, subcats > 0,isLandscape()|| subcats < 4);
            setFramesVisibility();
        } else if (selectedItem instanceof Content) {
            showFullContent((Content) selectedItem);
        }
    }

    @Override
    public void onBackPressed() {
    	if (drawerIsOpen()){
    		openDrawer(false);
    		return;
		}
        if (
                mFragmentCategory.getRoot() == null || mFragmentCategory.parentIsRoot())
            super.onBackPressed();
        else
            mFragmentCategory.back();
    }

    public void setTitle(String title) {
        if (isSearchMode()) return;
        if (mTxtTitle == null) mTxtTitle = (TextView) findViewById(R.id.txt_title);
        Text.setText(mTxtTitle, title, Font.TextPos.h1, false);
    }

    boolean isSearchMode() {
        return false;
//		return view.isVisible(mEtxtSearch);
    }

//    void showFullContent(int contentId) {
//        Intent fullContent = new Intent(this, ActivityFullContent.class);
////		Fragment fullContentFragment=new FragmentFullContent()
//        fullContent.putExtra(Const.Activity.CURRENT_PARENT, mFragmentCategory.getParentCategory()._id);
//        fullContent.putExtra(Const.Activity.CURRENT_ITEM, contentId);
//
//        startActivity(fullContent);
//
//    }
    void showFullContent(Content content) {
        Intent fullContent = new Intent(this, ActivityFullContent.class);
//		Fragment fullContentFragment=new FragmentFullContent()
        fullContent.putExtra(Const.Activity.CURRENT_PARENT, content.cat);
        fullContent.putExtra(Const.Activity.CURRENT_ITEM, content._id);

        startActivity(fullContent);

    }

    void showMenu(boolean show) {
        CreateMenu();
        if (show) mDialogMenu.show();
        else mDialogMenu.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_header_back) onBackPressed();
        else if (v.getId() == R.id.img_header_setting2)
            openDrawer(true);
        else if (v.getId() == R.id.img_search)
            showSearch();
        else if (v.getId() == R.id.img_header_icon1)
            updateContents(this, getParentIdForUpdate(),null);
        else showMenu(true);

    }

    void showSearch() {
        Intent intent = new Intent(this, ActivitySearch.class);
        intent.putExtra("current_category", mParentId);
        startActivity(intent);
    }

    void showProfile() {
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
        if (selectedItem.id==0){
            showProfile();return;
        }
        if (selectedItem.equals(ir.microsign.content.utils.Menu.mReshape))
            ReshapeSolver.Show(this);
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mAbout))
            startAboutUsActivity();
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mUserProfile))
            showProfile();
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mExit)) {
            app.exitApp(this);
        } else if (selectedItem.equals(ir.microsign.content.utils.Menu.mReport))
            startReportActivity();
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mSettings))
            startSettingActivity();
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mCheckUpdate))
            updateContents(this, null,null);
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mFirstPage))
            finish();
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mSearch))
            showSearch();
        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mFav))
            new DialogMarkup(this).show();
//        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mNotifications))
//            ActivityNotifyLists.show(this);
//        else if (selectedItem.equals(ir.microsign.content.utils.Menu.mPurchase))
//            purchase(mFragmentContent.mPayment);
    }

    void startAboutUsActivity() {
        ActivityIntro.show(this);
    }



    void startReportActivity() {
        ActivityTicket.show(this);
    }

    void startSettingActivity() {
        Intent settingIntent = new Intent(this, ActivitySetting.class);
        startActivity(settingIntent);
    }

    @Override
    public void onStart(Fragment fragment) {
        if (started) return;
        started = true;
        setupParentCategory(false);
    }

    String getParentIdForUpdate() {
//        return -1;
	   Category parent=mFragmentCategory.getParentCategory() ;
	   return  parent==null?"":parent._id;
    }

    @Override
    public void onEventRequest(int code, Object... args) {
        if (code == 0) onBackPressed();
        else if (code == 1) updateContents(this, getParentIdForUpdate(),null);
//        else if (code == 2) {
//            Price payment = (Price) args[0];
//            if (payment == null) {
//                payment = new Price();
//                payment.permission = (String) args[1];
//            }
//            purchase(payment);
//        }
//		Message.showDownloadContents(this,title,new MessageDialog.OnDialogResultListener() {
//			@Override
//			public void OnDialogResult(boolean ok, String key) {
//				if (!ok)return;
//
//			}
//		});
    }

    @Override
    public void finish() {
        if (!getResources().getBoolean(R.bool.show_please_rate_on_exit)) {
            if (drawerIsOpen())openDrawer(false);
            else
            super.finish();
            return;
        }
        if (app.getAppRateLunched(this)) {
            super.finish();
            app.mRateLunched = false;
        } else app.showPleaseRate(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == 0) {
            app.setAppRated(this);
            finish();
        }else if (requestCode==2000){afterUpdateFinished();}
    }
    public void afterUpdateFinished() {
        setupParentCategory(false);
    }
    public interface AfterUpdate {
        void updateFinished();
    }

    static class UpdateListener implements ContentUpdater.ContentUpdateListener {
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
                finishDownload();
                new SweetAlertDialog(mActivity, SweetAlertDialog.SUCCESS_TYPE)
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


            finishDownload();
            new SweetAlertDialog(mActivity,succeed? SweetAlertDialog.SUCCESS_TYPE:SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(succeed ? R.string.update_content_title_update_succeed : R.string.update_content_title_update_failed)
                    .setContentText(succeed ? R.string.update_content_desc_update_succeed : R.string.update_content_desc_update_failed)
                    .setConfirmText(R.string.ok)
                    .showCancelButton(false)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }

        @Override
        public void OnCanceled() {
            finishDownload();
        }


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
				Utility.CheckInternet(ActivityContentMain.this, new ConnectDialog.OnDialogResultListener() {
					@Override
					public void OnDialogResult(boolean ok, boolean isConnect) {
						if (!isConnect)return;
////                        net.tarnian.network.DownloadFiles downloadFiles=new DownloadFiles();
//						List<NameValuePair> list=new ArrayList<>(mNoExistImageList.size());
//						for (int i = 0; i < mNoExistImageList.size(); i++) {
//							Image images = mNoExistImageList.get(i);
//							try {
//								list.add(new NameValuePair(images.getUrl(), images.getPath()));
//							} catch (Exception ex) {
//								Log.e("showDowanloadAllImages",ex.getMessage()+":"+i);
//							}
//						}
//						new DownloadFilesByMap().GetFiles(ActivityContentMain.this, getString(R.string.download_all_images_progress_title), getString(R.string.download_all_images_progress_title_p1), list, new DownloadFilesByMap.DownloadCompletedListener() {
//							@Override
//							public void OnFileDownloaded(boolean succeed, String path) {
//
//							}
//
//							@Override
//							public void OnFinish(boolean allSucceed, String root) {
//								runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										onDownloadAllImagesFinished();
//									}
//								});
//							}
//						});
					}
				});
			}
		});
	}
	public void onDownloadAllImagesFinished(){
//		afterCreate();
	}
	class CheckImages extends AsyncTask<Void,Void,Void> {

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
}


