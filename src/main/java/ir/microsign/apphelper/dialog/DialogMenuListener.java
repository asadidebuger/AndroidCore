package ir.microsign.apphelper.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.apphelper.view.MenuView;
import ir.microsign.utility.Display;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/29/14.
 */
public class DialogMenuListener extends AlertDialog implements Listener.MenuItemSelectedListener {
    MenuView mViewMenu = null;

//    ScrollView mScrollMenu = null;
    WindowManager.LayoutParams mLayoutParams = null;
    int[] mPosition = new int[]{0, 0};
    View mRootView = null;
    Listener.MenuItemSelectedListener mMenuItemSelectedListener = null;

    public DialogMenuListener(Context context) {
        super(context);
    }

    public void setMenuItemSelected(Listener.MenuItemSelectedListener l) {
        mMenuItemSelectedListener = l;
        mViewMenu.setMenuItemSelected(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//		requestWindowFeature(R.style.dialogStyle_transparent);
        Window window = getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//		mLayoutParams.windowAnimations = android.R.anim.slide_in_left;

        window.setAttributes(mLayoutParams);
//		getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        setContentView(R.layout.layout_menu);
//        mScrollMenu = (ScrollView) findViewById(R.id.scroll);
        mRootView = findViewById(android.R.id.content);
		 FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) mRootView.getLayoutParams();
		 layoutParams.gravity=Gravity.RIGHT;
//        mScrollMenu.addView(mViewMenu);
        setContentView(mViewMenu);
		  ViewGroup.LayoutParams params= mRootView.getLayoutParams();
		width=Display.dpToPx(getContext(),220);
		  params.width=width;
        setPosition();


    }
    int width=0;

//	@Override
//	protected void onStart() {
//		super.onStart(); setPosition();
//	}

    public void setViewMenu(MenuView viewMenu, int[] Position) {
        mViewMenu = viewMenu;
        mPosition = Position;
    }

    public void setViewMenu(MenuView viewMenu) {
        mViewMenu = viewMenu;
//		LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) viewMenu.getLayoutParams();
//		if (params==null)params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		params.setMargins(9,9,9,9);
//
//		viewMenu.setLayoutParams(params);
    }

    public void show(boolean show) {
        if (show) show();
        else hide();
    }

    public void invert() {
        if (!isShowing()) show();
        else hide();
    }

    @Override
    public void show() {
        try {


            super.show();

//if (mViewMenu)
//            mViewMenu.removeAllViews();
//            mViewMenu.addView(mViewMenu);
        } catch (Exception ex) {
            Log.e(getClass().getName(), ex.getMessage());
        }
    }

    public void setPosition(int[] position) {
        mPosition = position;
    }

    public void setPosition(View parent) {
        int x = view.getRelativeLeft(parent);
        int y = view.getRelativeTop(parent) + parent.getHeight();
        mPosition = new int[]{x, y};
    }

    public void setPosition() {

        mLayoutParams.x = mPosition[0] - mRootView.getWidth();
        mLayoutParams.y = mPosition[1] - Display.getStatusBarHeight(getContext());
    }

    public void show(MenuView viewMenu) {

        mViewMenu = viewMenu;

        show();
    }



	@Override
	public void onItemSelected(ir.microsign.apphelper.object.MenuItem selectedItem) {
		hide();
		mMenuItemSelectedListener.onItemSelected(selectedItem);
	}
}
