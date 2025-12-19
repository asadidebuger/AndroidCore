package ir.microsign.settings.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import ir.microsign.R;
import ir.microsign.utility.Font;


/**
 * Created by Mohammad on 7/23/14.
 */
public class SettingItemView extends SettingItemBaseView {

    public SettingItemView(Context context) {
        super(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingItemView(Context context, int id, String title, String description, Drawable icon, Font font) {
        super(context, id, title, description, icon, font);
    }

    public SettingItemView(Context context, int id, String title, String description, Drawable icon) {
        super(context, id, title, description, icon);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof EditFontView)) return false;
        return ((EditFontView) o).getId() == getId();
    }

    @Override
    public int getViewResource() {
        return R.layout.layout_setting_item_compact;
    }
}
