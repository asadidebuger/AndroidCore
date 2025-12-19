package ir.microsign.gallery.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ir.microsign.gallery.object.Category;

import java.util.ArrayList;

public class GridViewCategoryAdapter extends BaseAdapter {

    private Activity _activity;
    private ArrayList<Category> mImages = new ArrayList<Category>();
//	private int imageWidth;

    public GridViewCategoryAdapter(Activity activity, ArrayList<Category> filePaths) {
        this._activity = activity;
        this.mImages = filePaths;
//		this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return this.mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category image = (Category) getItem(position);


        convertView = image.getView(_activity);
        return convertView;
    }
}
