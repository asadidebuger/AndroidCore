package ir.microsign.content.fragment;

import android.os.Bundle;

import java.util.List;

import ir.microsign.contentcore.object.Category;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Text;

//import net.tarnian.content.object.BaseObject;

/**
 * Created by Mohammad on 6/25/14.
 */
public class FragmentCategory extends FragmentBase {
    static Category mParentCategory = null;
    //	Category mSelectedCategory=null;
    String mRootId = "";

    //   public void Update(){
//	   utility.getCategories(this);
//   }
    @Override
    public void afterCreated(Bundle savedInstanceState) {
//		mDataSource = new DataSource(getContext());
//		setDataSource(mDataSource);
        super.afterCreated(savedInstanceState);

//		if (mParentCategory.title == null)

//		OnItemSelected(mParentCategory);

    }

    public void setParent(String parentId) {
		if (Text.isEmpty(mRootId)){mRootId = parentId;
			getDataSource().getRoot(mRootId,true);	}

        mParentCategory = (Category) getDataSource().getCategory(parentId);
        reLoad();

    }

//    @Override
//    public void search(String txt) {
//        super.search(txt);
//        reLoad();
//    }

    public void reLoad() {
        List<?> subCategories = isSearchMode() ? getDataSource().search(mSearchText) : getSubCategories(mParentCategory);
        if (isSearchMode()) {

        } else {
            if (subCategories.size() > 0) {
                setItems((List<BaseObject>) subCategories);
                super.OnItemSelected(mParentCategory);
            } else super.OnItemSelected(mParentCategory);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public Category getParentCategory() {
        return mParentCategory;
    }

    //	public  Category getSelectedCategory(){
//		return mSelectedCategory;
//	}
    @Override
    public void setItems(List<BaseObject> baseObjects) {
//		setNumColumns(isLandscape() ? 1 : 2);
//		setVerticalSpacing(isLandscape() ? 0 : 2);

//				setNumColumns(1);
//		setVerticalSpacing(0);
        super.setItems(baseObjects);
    }

    public List<?> getSubCategories(Category category) {
        return getDataSource().getSubCategories(category);
    }

    public boolean hasChild() {
        return getSubCategories(getParentCategory()).size() > 0;
    }

    @Override
    public void OnItemSelected(BaseObject selectedItem) {
        if (selectedItem == null) return;
        mSearchText = "";
        mParentCategory = (Category) selectedItem;
        reLoad();
    }

    public void back() {
//		List<BaseObject> baseObjects = getItems();

        if (parentIsRoot()) return;
//		Category firstCategory=(Category) dbObjects.get(0);
        mParentCategory = (Category) getDataSource().getCategory(mParentCategory.parent);
        OnItemSelected(mParentCategory);

    }

    public Category getRoot() {
        return (Category) getDataSource().getRoot();
    }

    public void setRoot(String root) {
        mRootId = root;
        if (Text.notEmpty(mRootId ))
            getDataSource().getRoot(mRootId, true);
//		reLoad();

    }

    public boolean parentIsRoot() {
        if (getRoot() == null) return true;
        return getDataSource().getRootId().equals(mParentCategory._id);
    }

}
