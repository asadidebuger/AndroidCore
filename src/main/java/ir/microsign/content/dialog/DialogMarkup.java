package ir.microsign.content.dialog;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.R;
import ir.microsign.content.activity.ActivityFullContent;
import ir.microsign.content.database.DataSource;
import ir.microsign.content.object.Content;
import ir.microsign.settings.dialog.DialogSelectItem;
import ir.microsign.settings.interfaces.Listener;
import ir.microsign.settings.object.SelectableItem;


/**
 * Created by Mohammad on 24/01/2015.
 */
public class DialogMarkup extends DialogSelectItem {
//    static DataSource mDataSource = null;

    public DialogMarkup(Activity activity) {
        super(activity, null, null, -1);

    }

    static public DataSource getDataSource(Context context) {
//        if (mDataSource == null)
//            mDataSource = new DataSource(activity);
//        return mDataSource;
        return DataSource.getDataSource();
    }

    public static void addBookmark(Context context, Content content) {
        content.marked = true;
        getDataSource(context).autoInsertUpdateByColumn(content,"_id");

    }

    public static void removeBookmark(Context context, Content content) {
        content.marked = false;
        getDataSource(context).autoInsertUpdateById(content);

    }

//	public DialogMarkup(Context activity) {
//		super(activity, null, null, -1);
//	}

//    public DataSource getDataSource() {
//        if (mDataSource == null)
//            mDataSource = new DataSource(getContext());
//        return mDataSource;
//    }

    @Override
    public void inIt() {
        init();
        super.inIt();
        setOnResultListener(new Listener.ResultListener() {
            @Override
            public void onResult(Object o, int i) {
                if (i > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    String selected="";
                    for (SelectableItem item : mItems) {
                        if (item.getId()==i)selected=item.getExtra();
                        list.add(item.getExtra());

                    }
                    ActivityFullContent.show(getParentActivity(), selected, list);
                }
                hide();
            }
        });

    }

    void init() {
        mTitle = getString(R.string.content_markup_title);
        List<Content> markupList = (List<Content>) getDataSource(getContext()).getMarkedContents(null);
        mItems = new ArrayList<SelectableItem>();
        for (Content markup : markupList) {
            mItems.add(new SelectableItem(markup.autoid, markup.title, markup.getSimpleIntroText(), false).setExtra(markup._id));
        }

//		inIt();

    }


}
