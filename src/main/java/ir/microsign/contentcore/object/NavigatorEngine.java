package ir.microsign.contentcore.object;

public interface NavigatorEngine {
    void setCategories(Category parent);

    void selectCategory(Category selected);

    void setCategoryDescription(Category selected);

    void setIndexList(Category parentCat, BookIndexItem parentItem, BookIndexItem selectedItem);

    void setIndexListSelected(BookIndexItem selectedItem);

    void setRightPanelByContentList(BookIndexItem selectedItem);

    void setRightPanelByContent(BookIndexItem selectedItem, String scrollTagId, int scrollPosition);

    void refreshIcons(boolean exit, boolean canFullScreen);

    void pathChanged(Category category, BookIndexItem bookIndexItem, Content content);

    int getContentPosition();
//	void refreshView(Category mCategory,BookIndexItem bookIndexItem,Content content);
}
