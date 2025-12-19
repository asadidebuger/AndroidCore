package ir.microsign.apphelper.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import ir.microsign.R;
import ir.microsign.apphelper.object.MenuItem;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 7/23/14.
 */
public class MenuItemView extends BaseView {

    public MenuItemView(Context context, BaseObject baseObject) {
        super(context,baseObject);
    }

//    public MenuItemView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    public void init() {
        View txtTitle = findViewById(R.id.txt_menu_title),
		txtDescription = findViewById(R.id.txt_menu_description);
        Text.setText(txtTitle, getTitle(), Font.TextPos.h1, true);
        Text.setText(txtDescription, getDescription(), Font.TextPos.p, true);
        ImageView imgIcon = (ImageView) findViewById(R.id.img_icon);
        if (getIcon() == null) view.setVisibility(imgIcon, false);
        else view.setVisibility(imgIcon, true);
        imgIcon.setImageDrawable(getIcon());
    }

//    public void setMenuItemSelectedListener(Listener.MenuItemSelectedListener menuItemSelectedListener) {
//        mMenuItemSelectedListener = menuItemSelectedListener;
//        LinearLayout root = (LinearLayout) findViewById(R.id.ll_root);
//        root.setOnClickListener(this);
//    }

    @Override
    public boolean equals(Object o) {
    	if (o instanceof MenuItemView){
			return ((MenuItemView) o).getId() == getId();
		}
		if (o instanceof MenuItem){
			return ((MenuItem) o).id == getId();
		}
		if (o instanceof Integer){
			return (int)o== getId();
		}
        return false;
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
    	super.initFromBaseObject(baseObject);
        if (getChildCount() < 1) getLayoutInflater().inflate(getItem().isSimple()?R.layout.layout_menu_item_simple:R.layout.layout_menu_item, this);
        setLayoutParams(R.id.ll_root);
		init();
    }

    public String getTitle() {
        return getItem().getTitle(getContext());
    }

    public String getDescription() {
        return getItem().getDescription(getContext());
    }

    public Drawable getIcon() {
        return getItem().getIcon(getContext());
    }

    public int getId() {

        return getItem().id;
    }

    public MenuItem getItem() {
        return (MenuItem) getDbObject();
    }
}
