package ir.microsign.content.object;

import android.content.Context;
import android.view.View;

import ir.microsign.content.database.DataSource;
import ir.microsign.content.view.ContentSearchView;
import ir.microsign.content.view.FeaturedContentView;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/15/14.
 */
public class Content extends ir.microsign.contentcore.object.Content {
    final String[] mCols = new String[]{ "autoid","_id", "title",  "alias", "intro", "full","lang","images", "cat", "create", "modify", "publish", "access", "published", "featured","order","search", "version","marked"};
    public Boolean marked;
    public String mSplitText = null;

    @Override
    public String[] getAllColumns() {
        return mCols;
    }

    public View getSearchView(Context context) {
        if (mSearchView != null) return mSearchView;

        mSearchView = new ContentSearchView(context, this);
        return mSearchView;

    }

    public View getFeatureView(Context context) {
        if (mFeatureView != null) return mFeatureView;

        mFeatureView = new FeaturedContentView(context, this);
        return mFeatureView;

    }


//    @Override
//    public String[] getExceptionFields() {
//        return new String[]{"order", "mHelper", "mDataSource", "mPath", "mSearchView", "mImagesIntro", "mImagesFullText", "mImagesAll", "mIntro", "mIntroSimple", "mFullText", "mFeatureView", "mSplitText", "mShowFirstImage"};
//    }

    public ir.microsign.contentcore.database.DataSource getDataSource(Context context) {
        if (mDataSource == null) mDataSource = new DataSource(context);
        return mDataSource;
    }

    public boolean marked() {
        return marked ==null?false:marked;

    }

    public String getIntroTextIfAvailable() {
        if (Text.isNullOrEmpty(intro)) return null;
        return getIntroText();
    }

    public String getFull() {
        if (mFullText != null) return mFullText;
        mFullText = "";
        if (!isAvailable()) {
            return null;
        }

        if (!Text.isNullOrEmpty(intro)) mFullText += intro;
        if (!Text.isNullOrEmpty(full)) mFullText += "{BREAKMORE}" + full;
//		mFullText=Text.replaceRegex(mFullText,"","\\{images=\\d+\\}");
        return mFullText;
    }



    public String getPageTitle() {
        return title;
    }

    public Image getThumbImage() {
        if (haveImageIntro()) return getImagesIntro().get(0);
//        if (haveImageFulltext()) return getImagesFullText().get(0);
        return null;
    }


}
