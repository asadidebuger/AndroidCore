package ir.microsign.apphelper.object;

import android.content.Context;
import android.graphics.drawable.Drawable;

import ir.microsign.apphelper.view.MenuItemView;
import ir.microsign.dbhelper.object.BaseObject;

/**
 * Created by Mohammad on 6/15/14.
 */
public  class MenuItem extends BaseObject {
    public  int id,resIcon=0,resTitle=0,resDes=0;
    public static final int FLAG_SHOW_TITLE=1,FLAG_SHOW_DESC=2,FLAG_SHOW_ICON=4,FLAG_SIMPLE=8;
    public int flag=FLAG_SHOW_DESC|FLAG_SHOW_TITLE|FLAG_SHOW_ICON;
    public  String title, des;
    public Drawable icon;
    public MenuItem setFlag(int f){
        flag=f;
        return this;
    }
    public MenuItem(int id, String title, String description, Drawable icon) {
        this.icon = icon;
        this.id = id;
        this.title = title;
        des = description;



    }
    public MenuItem(int id, String title, String description, int resIcon) {
        this.id = id;
        this.title = title;
        this.resIcon = resIcon;
        des = description;



    }
    public MenuItem(int id, int title, int description, int resIcon) {
        this.id = id;
        this.resTitle = title;
        this.resIcon = resIcon;
        resDes = description;



    }
    public boolean isSimple(){
    	return hasFlag(FLAG_SIMPLE);
	}

    public Drawable getIcon(Context context) {
		if (hasNotFlag(FLAG_SHOW_ICON))return null;
        if (icon==null){
            if (resIcon!=0)icon=context.getResources().getDrawable(resIcon);
        }
        return icon;
    }
    public String getTitle(Context context) {
    	if (hasNotFlag(FLAG_SHOW_TITLE))return null;
        if (title==null){
            if (resTitle!=0)title=context.getResources().getString(resTitle);
        }
        return title;
    }
    public String getDescription(Context context) {
		if (hasNotFlag(FLAG_SHOW_DESC))return null;
        if (des==null){
            if (resDes!=0)des=context.getResources().getString(resDes);
        }
        return des;
    }
boolean hasFlag(int f){
    	return ( f & flag) == f;
}
boolean hasNotFlag(int f){
    	return ( f & flag) != f;
}
    public MenuItem() {
//        super();
    }

    public MenuItem newInstance() {
        try {
            return getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Class getViewClass() {
        return MenuItemView.class;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MenuItemView){
            return ((MenuItemView) o).getId() == id;
        }
        if (o instanceof MenuItem){
            return ((MenuItem) o).id == id;
        }
        if (o instanceof Integer){
            return (int)o==id;
        }
        return false;
    }
}
