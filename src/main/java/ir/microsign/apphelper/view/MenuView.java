package ir.microsign.apphelper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.apphelper.object.MenuItem;
import ir.microsign.dbhelper.adapter.BaseAdapter;
import ir.microsign.dbhelper.object.BaseObject;


/**
 * Created by Mohammad on 8/5/14.
 */
public class MenuView extends LinearLayout {
    public GridView listView=null;
    public BaseAdapter adapter=null;


	public void refreshView() {
		if (adapter==null)return;
		adapter.notifyDataSetInvalidated();
//		adapter.updateItemsView();
	}

	public MenuView(Context context) {
        super(context);
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		inIt();
	}

	public void inIt() {
        if (isInEditMode()) return;

        if (listView != null) return;
        listView=new GridView(getContext());
		listView.setSelector(ir.microsign.R.drawable.transparent);
//		mScrollView.setScrollBarStyle(R.style.scrollbar_style);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        layoutParams.gravity=1;

        addView(listView,layoutParams);

        adapter=new BaseAdapter(getContext(),menuItemViewList);
        listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (menuItemSelectedListener==null)return;
				if (!(view instanceof MenuItemView)){
					menuItemSelectedListener.onItemSelected((MenuItem) adapter.getItem(position));

				}else
				menuItemSelectedListener.onItemSelected(((MenuItemView) view).getItem());
			}
		});
    }

    public void setMenuItemSelected(Listener.MenuItemSelectedListener l) {
       menuItemSelectedListener=l;
    }
	protected Listener.MenuItemSelectedListener menuItemSelectedListener=null;
    public void addItem(MenuItem item) {
        menuItemViewList.add(item);
		if (adapter!=null)adapter.setItems(menuItemViewList);

    }
    List<BaseObject> menuItemViewList=new ArrayList<>();

    public void addItems(MenuItem[] items) {
		menuItemViewList.addAll(Arrays.asList(items));
		if (adapter!=null)adapter.setItems(menuItemViewList);

    }
}
