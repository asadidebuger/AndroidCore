package ir.microsign.content.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.apphelper.dialog.DialogMenuListener;
import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.apphelper.object.MenuItem;
import ir.microsign.apphelper.view.MenuView;
import ir.microsign.content.Const;
import ir.microsign.R;
import ir.microsign.content.adapter.ContentPagerAdapter;
import ir.microsign.content.dialog.DialogMarkup;
import ir.microsign.content.object.Content;
import ir.microsign.content.utils.Menu;
import ir.microsign.content.view.FullContentView;
import ir.microsign.content.view.PagerView;
import ir.microsign.contentcore.object.Category;
import ir.microsign.context.app;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.settings.activity.ActivitySetting;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/29/14.
 */
public class ActivityFullContent extends ActivityBase {
    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_MAX_OFF_PATH = 2500;
    private static final int SWIPE_THRESHOLD_VELOCITY = 0;
    PagerView mViewPager = null;
    ContentPagerAdapter mContentPagerAdapter = null;
    DialogMenuListener mdDialogMenu = null;
    boolean isNight = false;
    GestureDetector mChildGesture = null;
    View.OnTouchListener mChildOnTouchListener = null;
    int timerFuture = 4000;
    LinearLayout mToolbar = null;
    List<BaseObject> mListItems = null;
    View.OnTouchListener gestureListener;
    boolean locked = false;
    CountDownTimer mToolbarTimer = new CountDownTimer(timerFuture, timerFuture) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            showToolbar(false);
        }
    };
    View.OnClickListener mToolbarClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mToolbarTimer.cancel();
            mToolbarTimer.start();
            int id = v.getId();
            if (id == R.id.img_zoom_in) ;
            else if (id == R.id.img_zoom_out) ;
            else if (id == R.id.img_next) {
                int total = mContentPagerAdapter.getCount();
                int current = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem((current + 1) % total, true);
            } else if (id == R.id.img_back) {
                int total = mContentPagerAdapter.getCount();
                int current = mViewPager.getCurrentItem();
                if (current == 0) current = total;
                mViewPager.setCurrentItem((current - 1) % total, true);
            } else if (id == R.id.img_rotation) {

            } else if (id == R.id.img_light) {
                isNight = !isNight;
                setContentLight();
            } else if (id == R.id.img_full_screen) {
                View title = findViewById(R.id.pager_title_strip);
                view.invertVisibility(title);
                ((ImageView) mToolbar.findViewById(id)).setImageResource(view.isVisible(title) ? R.drawable.icon_full_screen_white : R.drawable.icon_return_from_full_screen_white);
                hideToolbar();
            }

        }
    };
    private GestureDetector gestureDetector;

    public ActivityFullContent() {

    }

    //	DataSource mdDataSourceContent = null;
    public static void show(Context context, String index, ArrayList<String> ids) {
        Intent fullContent = new Intent(context, ActivityFullContent.class);
        fullContent.putExtra(Const.Activity.CURRENT_ITEM, index);
        fullContent.putStringArrayListExtra("id_list", ids);
        context.startActivity(fullContent);

    }

    void showMenu(boolean show) {
        CreateMenu();
        if (show) mdDialogMenu.show();
        else mdDialogMenu.dismiss();
    }

    void CreateMenu() {
//		if (mdDialogMenu == null) {
        mdDialogMenu = new DialogMenuListener(this);
        MenuView menu = new MenuView(this);
        menu.addItems(Menu.getRightMenuItemsFullContent(getCurrent().marked()));
        mdDialogMenu.setViewMenu(menu);
        mdDialogMenu.setMenuItemSelected(new Listener.MenuItemSelectedListener() {
            @Override
            public void onItemSelected(MenuItem selectedItem) {
                ActivityFullContent.this.onItemSelected(selectedItem);
            }
        });
//		}
        mdDialogMenu.setPosition(findViewById(R.id.img_header_settings));
    }

    void onItemSelected(MenuItem selectedItem) {
        if (selectedItem.equals(Menu.mShare)) Text.Share(this, getCurrent().getForShare(), getCurrent().getPageTitle());
        else if (selectedItem.equals(Menu.mExit)) app.exitApp(this);
        else if (selectedItem.equals(Menu.mSettings)) startSettingActivity();
        else if (selectedItem.equals(Menu.mSearch)) showSearch();
        else if (selectedItem.equals(Menu.mFav)) new DialogMarkup(this).show();
        else if (selectedItem.equals(Menu.mAddFav)) DialogMarkup.addBookmark(this, getCurrent());
        else if (selectedItem.equals(Menu.mRemoveFav)) DialogMarkup.removeBookmark(this, getCurrent());

    }

    void startSettingActivity() {
        Intent settingIntent = new Intent(this, ActivitySetting.class);
        startActivity(settingIntent);
    }

    void showSearch() {
        Intent intent = new Intent(this, ActivitySearch.class);
        intent.putExtra("current_category", getCurrent().cat);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("lastItemPosition", mViewPager.getCurrentItem());
		super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		if (savedInstanceState!=null)return;
        super.onCreate(savedInstanceState);
        int lastItem = -1;
        if (savedInstanceState != null) lastItem = savedInstanceState.getInt("lastItemPosition", -1);
        String catId = getIntent().getStringExtra(Const.Activity.CURRENT_PARENT),
                currentItemId = getIntent().getStringExtra(Const.Activity.CURRENT_ITEM);
        List<?> contents;
        if (Text.notEmpty(catId)) {
            Category parentCategory = new Category();
            parentCategory._id = catId;
            contents = getDataSource().getContents(parentCategory);
        } else {
            ArrayList<String> ids = getIntent().getStringArrayListExtra("id_list");
            contents = getDataSource().select(new Content(), "_id", ids);
//			contents=ActivitySearch.mItems;
        }
        setContents((List<BaseObject>) contents);
        int position = 0;
        if (lastItem > -1) position = lastItem;
        else {
            for (int i = 0; i < contents.size(); i++) {

                if (((Content) contents.get(i))._id.equals(currentItemId)) {
                    position = i;
                    break;
                }

            }
        }
        mViewPager.setCurrentItem(position, false);
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				setToolbar(true);
//			}
//		},500);

    }

    void setToolbarActions() {
        List<View> childs = view.getAllChilds(mToolbar, ImageView.class);
        for (View view : childs)
            view.setOnClickListener(mToolbarClicked);
    }

    void setContentLight() {

        ImageView light = (ImageView) findViewById(R.id.img_light);
        light.setImageResource(isNight ? R.drawable.icon_day : R.drawable.icon_night);
        Font font = null;
        if (isNight) {
            font = Font.getFont(this,Font.TextPos.web);
            font.setColor(Color.WHITE);
            font.setBackColor(Color.BLACK);

        }
        List<View> childs = view.getAllChilds(mViewPager, FullContentView.class);
        for (View view : childs)
            ((FullContentView) view).setView();

//		android.support.v4.app.NoSaveStateFrameLayout
//		mViewPager.getChildAt(mViewPager.getCurrentItem()).getClass();
        FullContentView.setCustomFont(font);
//		((FullContentView)((FrameLayout)mViewPager.getChildAt(mViewPager.getCurrentItem())).getChildAt(0)).setView();
//		for (int i=0;i<mViewPager.getChildCount();i++)
//			((FullContentView)((FrameLayout)mViewPager.getChildAt(i)).getChildAt(2)).setView();

//		FragmentFullContent page = (FragmentFullContent)mViewPager.getAdapter().get.findFragmentByTag("android:switcher:" + R._id.pager + ":" + mViewPager.getCurrentItem());
        // based on the current position you can then cast the page to the correct
        // class and call the method:
//		if (page != null) {
//			page.refresh();
//		}
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        if (root != null)
            root.removeAllViews();
        super.setContentView(R.layout.layout_full_content);

        mViewPager = (PagerView) findViewById(R.id.pager);
        if (gestureDetector == null || gestureListener == null) {
            gestureDetector = new GestureDetector(this, new MyGestureDetector());
            gestureListener = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            };
        }
        if (mContentPagerAdapter != null) mContentPagerAdapter.setOnTouchListener(gestureListener);
        findViewById(R.id.img_header_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.img_header_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(true);
            }
        });
        mToolbar = (LinearLayout) findViewById(R.id.ll_content_items_root);
//		view.setVisibility(mToolbar, false);
    }

    Content getCurrent() {
        return (Content) mListItems.get(mViewPager.getCurrentItem());
    }

    public void setContents(List<BaseObject> baseObjects) {

        mListItems = baseObjects;

        mContentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(), baseObjects);
        //remover
//		mContentPagerAdapter.setOnTouchListener(gestureListener);
        mViewPager.setAdapter(mContentPagerAdapter);
//		mViewPager.setOnTouchListener(mMotionEvent);
    }

    void onToUp() {
        showToolbar(false);
    }

    void onToDown() {
        showToolbar(true);
    }

    void showToolbar(final boolean show) {
        if (locked) return;
        if (show) {
            showToolbar();
            return;
        }
        hideToolbar();
    }

    View getToolbar() {
        return mToolbar;
    }

    void setToolbar(boolean show) {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_fullcontent_fragment_root);

        if (mToolbar == null) {
            mToolbar = new LinearLayout(this);
            getLayoutInflater().inflate(R.layout.layout_content_items, mToolbar, true);
            rl.addView(mToolbar);
            boolean isLandscape = isLandscape();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(isLandscape ? -2 : -1, isLandscape ? -1 : -2);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            mToolbar.setLayoutParams(layoutParams);
        }

        view.setVisibility(mToolbar, show);
        mChildGesture = new GestureDetector(this, new ChildGesture());
        mChildOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mChildGesture.onTouchEvent(event);
            }
        };
        List<View> childs = view.getAllChilds(mToolbar);
        for (View view : childs)
            view.setOnTouchListener(mChildOnTouchListener);
        setToolbarActions();
        mToolbar.setOnTouchListener(null);

    }

    void showToolbar() {
        mToolbarTimer.cancel();
        if (getToolbar() == null) setToolbar(true);
        else if (view.isVisible(getToolbar())) {
            mToolbarTimer.start();
            return;
        }
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), isLandscape() ? R.anim.left_in2 : R.anim.bottom_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mToolbarTimer.start();
                locked = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mToolbar.startAnimation(animation);
        locked = true;
        view.setVisibility(getToolbar(), true);
    }

    void hideToolbar() {
        mToolbarTimer.cancel();
        if (getToolbar() == null) setToolbar(true);
        else if (!view.isVisible(getToolbar())) {
            locked = false;
            return;
        }
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), isLandscape() ? R.anim.left_out2 : R.anim.bottom_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(getToolbar(), false);
                locked = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mToolbar.startAnimation(animation);
        locked = true;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            try {
//				Log.e("distanceY", String.valueOf(distanceY));
//				Log.e("e1.getY()", String.valueOf(e1.getY()));
//				Log.e("e2.getY()", String.valueOf(e2.getY()));
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;

                // right to left swipe
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(distanceY) > SWIPE_THRESHOLD_VELOCITY) {
                    onToDown();
//					view.showToast(ActivityFullContent.this, "Left Swipe", Toast.LENGTH_SHORT);
                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(distanceY) > SWIPE_THRESHOLD_VELOCITY) {
                    onToUp();
//					view.showToast(ActivityFullContent.this, "Right Swipe", Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

    class ChildGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            try {
                mToolbarTimer.cancel();
                mToolbarTimer.start();
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//			try {
//				Log.e("actiion",String.valueOf(e2.getAction()));
//				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//					return false;
//				// right to left swipe
//				if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//					view.showToast(ActivityFullContent.this, "Left Swipe", Toast.LENGTH_SHORT);
//				}  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//					view.showToast(ActivityFullContent.this, "Right Swipe", Toast.LENGTH_SHORT);
//				}
//			} catch (Exception e) {
//				// nothing
//			}
//			return false;
//		}
}
