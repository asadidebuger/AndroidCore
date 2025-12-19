package ir.microsign.contentcore.utils;
import ir.microsign.apphelper.object.MenuItem;
import ir.microsign.R;

/**
 * Created by Mohammad on 8/5/14.
 */
public class Menu {
    public static MenuItem mFirstPage, mAddFav, mRemoveFav, mFav, mSearch, mShowImages, mExit, mSettings, mReshape, mDownloadAllImages, mCleanAll, mReport, mAbout, mShare, mClean, mCheckUpdate, mUserProfile, mNotifications, mPurchase;
    static MenuItem[] mMenuItemsRight = null;
    static MenuItem[] mMenuItemsLeft = null;

    public static void inIt() {
        mAbout = new MenuItem(0,R.string.menu_title_about_us,R.string.menu_desc_about_us, R.drawable.icon_about);
        mFirstPage = new MenuItem(1,R.string.menu_title_first_page,R.string.menu_desc_first_page, R.drawable.icon_home);
        mAddFav = new MenuItem(2,R.string.menu_title_add_fav,R.string.menu_desc_add_fav, R.drawable.icon_mark);
        mFav = new MenuItem(3,R.string.menu_title_fav,R.string.menu_desc_fav, R.drawable.icon_favorites);
        mSearch = new MenuItem(4,R.string.menu_title_search,R.string.menu_desc_search, R.drawable.icon_filter);
        mShowImages = new MenuItem(5,R.string.menu_title_show_images,R.string.menu_desc_show_images, R.drawable.icon_cam);
        mExit = new MenuItem(6,R.string.menu_title_exit,R.string.menu_desc_exit, R.drawable.icon_exit);
        mSettings = new MenuItem(7,R.string.menu_title_settings,R.string.menu_desc_settings, R.drawable.icon_settings);
        mReshape = new MenuItem(8,R.string.menu_title_persian_problem,R.string.menu_desc_persian_problem, R.drawable.icon_text);
//		mCleanImages = new MenuItem(9,R.string.menu_title_clean_images,R.string.menu_desc_clean_images, R.drawable.icon_delete);
//		mCleanAll = new MenuItem(10,R.string.menu_title_clean_all,R.string.menu_desc_clean_database, R.drawable.icon_delete);
        mReport = new MenuItem(11,R.string.menu_title_report,R.string.menu_desc_report, R.drawable.icon_report);
        mShare = new MenuItem(12,R.string.menu_title_share,R.string.menu_desc_share, R.drawable.icon_share);

        mClean = new MenuItem(13,R.string.menu_title_clean,R.string.menu_desc_clean, R.drawable.icon_delete);
        mCheckUpdate = new MenuItem(14,R.string.menu_title_update,R.string.menu_desc_update, R.drawable.icon_refresh);
        mUserProfile = new MenuItem(15,R.string.menu_title_profile,R.string.menu_desc_profile, R.drawable.icon_user);
        mRemoveFav = new MenuItem(16,R.string.menu_title_del_fav,R.string.menu_desc_del_fav, R.drawable.icon_mark);
        mNotifications = new MenuItem(16,R.string.menu_title_notification,R.string.menu_desc_notification, ir.microsign.R.drawable.icon_notifications);
        mPurchase = new MenuItem(17,R.string.menu_title_purchase,R.string.menu_desc_purchase, R.drawable.icon_pay);
        mDownloadAllImages = new MenuItem(17,R.string.menu_title_download_all_images,R.string.menu_desc_download_all_images, R.drawable.ic_file_download_black);

    }

    public static MenuItem[] getRightMenuItems(boolean showBuy) {
        inIt();
        mMenuItemsRight = new MenuItem[]{
//				mAddFav,
                mSearch,
//				mShowImages,
                showBuy ? mPurchase : mCheckUpdate,
                mShare,
//				mFirstPage,

                mExit
        };
        return mMenuItemsRight;

    }

    public static MenuItem[] getRightMenuItemsFullContent(boolean marked) {
        inIt();
        mMenuItemsRight = new MenuItem[]{
                mShare,
                marked ? mRemoveFav : mAddFav,
                mFav,
                mSearch,

//				mCheckUpdate,

                mSettings,
//				mFirstPage,
                mExit
        };
        return mMenuItemsRight;

    }

    public static MenuItem[] getLeftMenuItems() {
        inIt();
        mMenuItemsLeft = new MenuItem[]{
                mFav
                , mSettings
                , mReshape
                , mUserProfile
                , mReport
                , mNotifications
                , mAbout
                , mExit
        };
        return mMenuItemsLeft;

    }



}
