package ir.microsign.contentcore.object;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import ir.microsign.contentcore.database.DataSource;
import ir.microsign.utility.Text;

public class Navigator {
    int mContentPosition = 0;
    private DataSource mDataSource = null;
    private Context mContext = null;
    private NavigatorEngine mEngine = null;
    private String mTitle = null, mTagRootCat = null;
    private int mLimitLevel = 10;
    private BookIndexItem mIndexParent, mIndexSelected;
    private Category mCatRoot = null, mCatParent = null, mCatSelected = null;
    private Content mContentSelected = null;
    private boolean mIsInFullHtml = false;
public void nullCatRoot(){
    mCatRoot=null;
}
    public Navigator() {

    }

    public Navigator(Context context, NavigatorEngine engine, DataSource dataSource, String rootTag, Bundle state) {
        mEngine = engine;

        mContext = context;
        mDataSource = dataSource;
        mTagRootCat = rootTag;
        if (state == null) return;
        mContentPosition = state.getInt("mposition", mContentPosition);
        mCatRoot = dataSource.getCategory(state.getString("mcatroot"));
        mCatParent = dataSource.getCategory(state.getString("mcatparent"));
        mCatSelected = dataSource.getCategory(state.getString("mcatselected"));
        mContentSelected = dataSource.getContent(state.getString("mcontentselected"));
        mIndexParent = dataSource.getBookIndexItems(state.getString("mindexparent"));
        mIndexSelected = dataSource.getBookIndexItems(state.getString("mindexselected"));

    }

    public Navigator(Context context, NavigatorEngine engine, DataSource dataSource, String rootTag) {
        mEngine = engine;
        mTagRootCat = rootTag;
        mContext = context;
        mDataSource = dataSource;

    }

    public Category getRootCat() {
        return mCatRoot;
    }

    public Bundle saveState(Bundle state) {
//		published.putString("mroottag", mTagRootCat);
        state.putInt("mposition", mEngine.getContentPosition());
        if (mCatRoot != null) state.putString("mcatroot", mCatRoot._id);
        if (mCatParent != null) state.putString("mcatparent", mCatParent._id);
        if (mCatSelected != null) state.putString("mcatselected", mCatSelected._id);
        if (mContentSelected != null) state.putString("mcontentselected", mContentSelected._id);
        if (getParentIndexItem() != null) state.putString("mindexparent", getParentIndexItem()._id);
        if (mIndexSelected != null) state.putString("mindexselected", mIndexSelected._id);
        return state;
    }

    public String getTagRootCat() {
        return mTagRootCat;
    }

    private DataSource getDataSource() {
        if (mDataSource == null) mDataSource = Helper.getHelper().getDataSource();
        return mDataSource;}

    private Category initRootCatByTag() {
        if (mCatRoot != null) return mCatRoot;
        Category category = new Category();
        category.alias = mTagRootCat;
        mCatRoot = (Category) getDataSource().selectFirst(category);
        mCatParent = mCatRoot;
        return mCatRoot;
    }

    private boolean isLastNode(BookIndexItem item) {
        return item.level >= mLimitLevel || item.isLastNode(getDataSource());
    }

    public void onStart() {
        if (mCatRoot == null) {
            mCatSelected = initRootCatByTag();
            onCategoryClicked(mCatRoot);
        } else refresh();
    }

    public void onCategoryClicked(Category category) {
        if (category == null) return;
        mContentSelected = null;
        mIndexParent = mIndexSelected = null;
        if (!category.equals(mCatSelected)) {
            mCatParent = getDataSource().getCategory(category.parent);
            mCatSelected = category;
        }
        if (getDataSource().getSubCategories(category).size() > 0) mEngine.setCategories(mCatSelected);
        else mEngine.selectCategory(mCatSelected);
        if (!Text.isNullOrEmpty(mCatSelected.description)) mEngine.setCategoryDescription(mCatSelected);
        else mEngine.setRightPanelByContentList(null);
        mEngine.setIndexList(mCatSelected, null, null);
        mEngine.refreshIcons(canExit(), true);
        mEngine.pathChanged(mCatSelected, mIndexSelected, mContentSelected);
    }

    private boolean isInContentViewMode() {
        return mContentSelected != null;
    }

    public void onListClicked(BookIndexItem indexItem) {
        if (indexItem == null && (mCatSelected != null)) {
            mIndexSelected = mIndexParent = null;
            onCategoryClicked(mCatSelected);
            return;
        }

        mContentSelected = indexItem.getContent(getDataSource());
        if (mContentSelected != null && !Text.isNullOrEmpty(mContentSelected.getFull())) {
            mIndexSelected = indexItem;
            if (getDataSource().getBookIndexItemChildCount(indexItem._id)> 0) {
                mIndexParent = indexItem;
                mEngine.setIndexList(mCatSelected, mIndexParent, null);
            } else {
                mEngine.setIndexListSelected(mIndexSelected);
            }
            mEngine.setRightPanelByContent(mIndexSelected, null, -1);
        } else {
            mContentSelected = null;
            List<BookIndexItem> childs = (List<BookIndexItem>) getDataSource().getBookIndexItemChilds(indexItem);
            if (isLastNode(indexItem)) {
                mIndexSelected = indexItem;
                mEngine.setIndexListSelected(mIndexSelected);
                if (childs.size() == 1) {
                    mContentSelected = getDataSource().getContent(childs.get(0).content_id);
                    mEngine.setRightPanelByContent(childs.get(0), null, -1);
                } else {
                    mEngine.setRightPanelByContentList(mIndexSelected);
                }
            } else if (childs.size() > 0) {
                if (childs.size() == 0) {
                    mIndexSelected = indexItem;
                } else {
                    mIndexParent = indexItem;
                    mIndexSelected = null;
                }
                mEngine.setIndexList(mCatSelected, mIndexParent, null);
            }
        }
        mEngine.pathChanged(mCatSelected, mIndexSelected, mContentSelected);
    }

    public Content getSelectedContent() {
        return mContentSelected;
    }
    public BookIndexItem getSelectedIndex() {
        return mIndexSelected;
    }

    public boolean onBackPressed() {
        if (canExit()) return true;
        if (isInContentViewMode()) {
//			BookIndexItem IndexSelected = getDataSource().getBookIndexItemParentByContentId(mContentSelected._id);
//			mIndexSelected=IndexSelected;
            BookIndexItem item = mIndexSelected == null ? mIndexParent : mIndexSelected;
            List<BookIndexItem> childs = (List<BookIndexItem>) getDataSource().getBookIndexItemChilds(item);

            mContentSelected = null;
            if (childs.size() < 2) {
                BookIndexItem parent = getDataSource().getBookIndexItems(item.parent_id);
                onListClicked(parent);
//
            } else {

                mEngine.setRightPanelByContentList(mIndexSelected);
            }
        } else {
            if (mIndexParent == null && mIndexSelected == null) {
                onCategoryClicked(mCatParent);
            } else
                onListClicked(mIndexParent);


        }
        return false;
    }

    public boolean canExit() {
        return mCatRoot == null || mCatRoot.equals(mCatSelected);
    }

    public String getParentCatId() {
        return getParentCat() == null ? "" : mCatParent._id;
    }

    public Category getParentCat() {
        if (mCatSelected == null) return mCatRoot;
        if (mCatParent != null && mCatParent._id.equals(mCatSelected.parent)) return mCatParent;
        mCatParent = getDataSource().getCategory(mCatSelected.parent);
        if (mCatParent == null) mCatParent = mCatSelected;
        return mCatParent;
    }

    public BookIndexItem getParentIndexItem() {
        if (mIndexSelected == null) return mIndexParent;
        if (mIndexParent != null && mIndexParent._id.equals(mIndexSelected.parent_id)) return mIndexParent;
        mIndexParent = getDataSource().getBookIndexItems(mIndexSelected.parent_id);
        return mIndexParent;
    }

    public String getSelectedCatId() {
        return mCatSelected == null ? "" : mCatSelected._id;
    }

    public void navigate(String contentId, String scrollTag, int scrollPosition) {
        Content ContentSelected = getDataSource().getContent(contentId);
        BookIndexItem ContentIndex = getDataSource().getBookIndexItemByContentId(contentId);
        if (!ContentSelected.equals(mContentSelected)) {
//            if (getDataSource().isBookIndexItemLastNode())
            mContentSelected = ContentSelected;

//			isLastNode(ContentIndex);
            BookIndexItem IndexSelected = getDataSource().getBookIndexItemParentByContentId(contentId);
            if (IndexSelected == null) IndexSelected = ContentIndex;
            if (!IndexSelected.equals(mIndexSelected)) {
                mIndexSelected = IndexSelected;
                BookIndexItem IndexParent = getDataSource().getBookIndexItems(mIndexSelected.parent_id);
//				if (IndexParent==null)IndexParent=mIndexSelected;
                if (IndexParent == null || !IndexParent.equals(mIndexParent)) {
                    mIndexParent = IndexParent;
                    Category CatSelected = getDataSource().getCategory(mContentSelected.cat);
                    if (!CatSelected.equals(mCatSelected)) {
                        mCatSelected = CatSelected;
                        Category CatParent = getDataSource().getCategory(mCatSelected.parent);
                        if (!CatParent.equals(mCatParent)) {
                            mCatParent = CatParent;
                            mEngine.setCategories(mCatParent);
                        }
                        mEngine.refreshIcons(canExit(), true);
                        mEngine.selectCategory(mCatSelected);
                    }
                    mEngine.setIndexList(mCatSelected, mIndexParent, null);
                }
                mEngine.setIndexListSelected(mIndexSelected);
            }
        }
        mEngine.setRightPanelByContent(ContentIndex, scrollTag, scrollPosition);
        mEngine.pathChanged(mCatSelected, mIndexSelected, mContentSelected);
    }

    public void refresh() {

        mEngine.setCategories(getParentCat());

        if (mCatSelected != null) {
            mEngine.selectCategory(mCatSelected);
            mEngine.setIndexList(mCatSelected, getParentIndexItem(), null);
            if (!Text.isNullOrEmpty(mCatSelected.description)) mEngine.setCategoryDescription(mCatSelected);

        }
        if (mIndexSelected != null) {
            if (mContentSelected == null)
                onListClicked(mIndexSelected);
            else {
                mEngine.setIndexListSelected(mIndexSelected);
                mEngine.setRightPanelByContent(getDataSource().getBookIndexItemByContentId(mContentSelected._id), null, mContentPosition);
            }
        }
        mEngine.refreshIcons(canExit(), true);
//		if (mContentSelected!=null)
        mEngine.pathChanged(mCatSelected, mIndexSelected, mContentSelected);
    }
}