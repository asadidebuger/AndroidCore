package ir.microsign.contentcore.view;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ir.microsign.R;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.object.Category;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.MarkedJoinedObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.view.ViewHtml;

/**
 * Created by Mohammad on 6/24/14.
 */
public class MarkedLisView extends BaseView {

//	net.tarnian.content.object.Content mContent = null;

    public int mMarked, mLearned;
    public String mParentCatId = "", mSelectedCat = "";
    public String mTitle;
    public MessageDialog.OnDialogResultListener mOnDialogResultListener = null;
    Category mCatAll = null;
    RichWebView richWebView = null;
    LinearLayout llCats = null;
    CategoriesView mCategoriesView = null;
    private DataSource mDataSource = null;

    public MarkedLisView(Context context) {
        super(context);
    }


    public void setView() {

        if (getChildCount() < 1) {
            getLayoutInflater().inflate(R.layout.layout_marked_list, this, true);
            setCategoryView(mParentCatId, mSelectedCat);
            richWebView = (RichWebView) findViewById(R.id.rich_web_view);
            richWebView.mTextAlign = "unset";
            richWebView.mPadding_heights = 5;
            this.setBackgroundColor(getContext().getResources().getColor(R.color.color_headlines_right_solid));
            richWebView.setOnUrlClickListener(new ViewHtml.OnUrlClickListener() {
                @Override
                public boolean onClick(WebView view, String url) {
                    if (url.startsWith(Content.getHelper().getPrefix() + ":delete:"))
                    {
                        deleteMarked(url);
                        if (mOnDialogResultListener != null) mOnDialogResultListener.OnDialogResult(false, url);
                    } else if (mOnDialogResultListener != null) mOnDialogResultListener.OnDialogResult(true, url);
                    return true;
                }

                @Override
                public boolean onClicked(WebView view, String url) {
return false;
                }
            });

            findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDialogResultListener != null) mOnDialogResultListener.OnDialogResult(true, null);
                }
            });
            richWebView.mPadding_sides = 0;

            Text.setText(findViewById(R.id.txt_title), mTitle, Font.TextPos.h1);


        }
    }

    public void deleteMarked(String address) {
        String[] add = address.split("\\.");
        int pos = Display.pxToDp(getContext(), getRichWebView().getScrollY());

//        int catId = Integer.parseInt(add[1]);
        String id = add[2];
        String tag = add[3];
        int index = Integer.parseInt(add[4]);
        int marked = mMarked > 0 ? 0 : Integer.parseInt(add[5]), learned = mLearned > 0 ? 0 : Integer.parseInt(add[6]);
        getDataSource().setMark(id, tag, index, marked, learned);
        getRichWebView().mStartPosition = pos;
        setContent();
        getRichWebView().mStartPosition = 0;
    }

    public RichWebView getRichWebView() {
        if (richWebView == null) {
            removeAllViews();
            setView();
        }

        return richWebView;

    }

    public Content getContent() {
        return (Content) getDbObject();
    }

    public void setContent() {
        List<MarkedJoinedObject> list = (List<MarkedJoinedObject>) getDataSource().getMarkedJoined(mSelectedCat, mMarked, mLearned);

        final StringBuilder sb = new StringBuilder(list.size()*100);
        for (MarkedJoinedObject content : list) {
           content.getHtml(sb).append("<hr/>");
        }


        getRichWebView().setContent(sb.toString(), true, 5);

    }

    void setCategoryView(String parentId, String selectedId) {
        mCatAll = new Category();
        mCatAll._id = "";
        mCatAll.title = getContext().getString(R.string.content_all_cats);
        llCats = (LinearLayout) findViewById(R.id.ll_cats);
        if (llCats.getChildCount() > 0 && llCats.getChildAt(0) instanceof CategoriesView)
            mCategoriesView = (CategoriesView) llCats.getChildAt(0);
        else {
            mCategoriesView = new CategoriesView(getContext());
            llCats.addView(mCategoriesView, -1, getResources().getDimensionPixelOffset(R.dimen.category_item_height));
        }

        if (Text.isEmpty(parentId)) {
            parentId = selectedId;
            selectedId = "";
        }
        mSelectedCat = selectedId;
        List<ir.microsign.dbhelper.object.BaseObject> categories = new ArrayList<>();
        categories.add(mCatAll);
        if (Text.isEmpty(parentId))
            categories.addAll((Collection<? extends ir.microsign.dbhelper.object.BaseObject>) getDataSource().getSubCategories(parentId));

        mCategoriesView.setItems(categories, new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryClicked(v);
            }
        });
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCategoriesView.setItemSelectedById(mSelectedCat);
                setContent();
            }
        }, 500);
    }

    void onCategoryClicked(View v) {
        Category mSelectedCat = ((CategoryView) v).getItem();
        mCategoriesView.setItemSelected(mSelectedCat);
        this.mSelectedCat = mSelectedCat._id;
        setContent();
    }


    public DataSource getDataSource() {
        if (mDataSource == null) mDataSource = new DataSource(getContext());
        return mDataSource;
    }

    public void setOnDialogResultListener(MessageDialog.OnDialogResultListener l) {
        mOnDialogResultListener = l;
    }
}
