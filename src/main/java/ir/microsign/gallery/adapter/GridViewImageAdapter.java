package ir.microsign.gallery.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ir.microsign.gallery.activity.FullScreenGalleryActivity;
import ir.microsign.gallery.object.Image;

import java.util.ArrayList;

public class GridViewImageAdapter extends BaseAdapter {

    private Activity _activity;
    private ArrayList<Image> mImages = new ArrayList<Image>();

    public GridViewImageAdapter(Activity activity, ArrayList<Image> filePaths) {
        this._activity = activity;
        this.mImages = filePaths;
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
        Image image = (Image) getItem(position);
        convertView = image.getView(_activity);
        convertView.setOnClickListener(new OnImageClickListener(position));
        return convertView;
    }

    class OnImageClickListener implements OnClickListener {
        int _postion;
        int catId;

        public OnImageClickListener(int position) {
            this._postion = position;
            catId = ((Image) getItem(position)).catid;
        }

        @Override
        public void onClick(View v) {
            FullScreenGalleryActivity.show(_activity, catId, _postion);
        }

    }


}
