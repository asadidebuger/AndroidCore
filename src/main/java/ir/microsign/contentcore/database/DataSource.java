package ir.microsign.contentcore.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ir.microsign.contentcore.object.BookIndexItem;
import ir.microsign.contentcore.object.Category;
import ir.microsign.contentcore.object.CategorySimple;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.Helper;
import ir.microsign.contentcore.object.Id;
import ir.microsign.contentcore.object.Marked;
import ir.microsign.contentcore.object.MarkedJoinedObject;
import ir.microsign.contentcore.object.Suggest;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/25/14.
 */
public class DataSource extends ir.microsign.dbhelper.database.DataSource {
    Category mRoot = null;
//   static DataSource mDataSource=null;
    public static DataSource getDataSource(){
//        if (mDataSource==null)mDataSource=new DataSource(activity!=null?activity: Application.getContext());
        return Helper.getHelper().getDataSource();

    }
    public DataSource(Context context) {
        super(new DbHelper(context));
    }

    public DataSource(DbHelper newDbHelper) {
        super(newDbHelper);
    }


public List<Image> getAllImages(){
    String like="\"%<img%>%\"";
   BaseObject cnt= getContentSampleForQuery();

    String q="SELECT * FROM "+cnt.getTableName()+" WHERE published>0 "+" AND ( intro LIKE "+like+" OR full LIKE "+like+")";//"full LIKE "+like+" OR intro LIKE "+like ;
   List<Content> contentList= (List<Content>) selectByQuery(getContentSampleForQuery(),q);
    List<Image> imageList=new ArrayList<>();
    for (Content content:contentList)
    {
        List<Image> imgs= content.getImagesAll();
        for (Image img : imgs) {
            if (!imageList.contains(img))
                imageList.add(img);
        }
    }

    q="SELECT * FROM categories WHERE description LIKE "+like;//"full LIKE "+like+" OR intro LIKE "+like ;
    List<Category> catList= (List<Category>) selectByQuery(new Category(),q);
//    List<Image> imageList=new ArrayList<>();
    for (Category category:catList)
    {
        List<Image> imgs=Content.getImages(category.description);
        for (Image img : imgs) {
            if (!imageList.contains(img))
                imageList.add(img);
        }
    }
    return imageList;
}
    public Category getCategoryByTag(String tag) {
        Category category = new Category();
        category.alias = tag;
        return (Category) selectFirst(category);
    }
   public Category getSearchOptionCategory(String catId) {
        Category cat=getCategory(catId);
//        cat.getParents();
        String q="SELECT * FROM categories WHERE [search]<>'' AND _id IN "+cat.getInParentsPhrase()+" ORDER BY parents LIMIT 1";
//       StringBuilder sb=new StringBuilder(200);
//       sb.append("SELECT * FROM categories AS C1 LEFT JOIN categories AS C2 ON C1.lft <= C2.lft AND C1.rgt>=C2.rgt WHERE C1.note <> '' AND C2._id=").append(catId).append(" ORDER BY C1.lft DESC LIMIT 1");
       List list= selectByQuery(new Category(),q);
       if (list.size()>0)return (Category) list.get(0);
       return new Category();
    }

    public List<?> getCategories(String where) {
        return select(new Category());
    }

    public List<?> getSubCategories(String parentId) {
        if (Text.isNullOrEmpty(parentId)) return new ArrayList<>();
        Category category = new Category();
        category.parent = parentId;
        return select(category);
    }
    public List<?> getSimpleSubCategories(int parentId) {
        if (parentId < 0) return new ArrayList<>();
        CategorySimple category = new CategorySimple();
        category.parent_id = parentId;
        return select(category);
    }
    public  List<?> getSuggest(String txt,String cat, int limit, int offset) {
        if (Text.isNullOrEmpty(txt)) return new ArrayList<>();
        StringBuilder sb = new StringBuilder();
//        if (cat > 0) sb.append("(");
//        String lchar="\ufffe";

        sb .append("[title] >='").append(txt).append("' COLLATE NOCASE");
//                .append(" AND [title] <='").append(txt).append(lchar).append(lchar)
//                .append(")");

        if (Text.notEmpty(cat))sb.append(" AND [cat]='").append(cat).append("'");
        sb.append(" ORDER BY title COLLATE NOCASE");

        return select(new Suggest(),"title","contents",true, sb.toString(), limit, offset,null,true,false);
    }
    public Category getCategory(String id) {
        if (Text.isEmpty(id)) return null;
        Category category = new Category();
        category._id = id;
        return (Category) selectFirst(category);
    }

    public List<?> getUpCategories(Category category) {
        return getSubCategories(category.parent);
    }

    public List<?> getSubCategories(Category category) {
        return getSubCategories(category._id);
    }

    public List<Category> getAllSubCategories(String catId) {
        List<Category> temp = (List<Category>) getSubCategories(catId);
        List<Category> result = new ArrayList<Category>();
        if (temp.size() > 0) result.addAll(temp);
        for (Object category : temp) {
            List<Category> temp2 = getAllSubCategories(((Category) category)._id);
            if (temp2.size() > 0)
                result.addAll(temp2);
        }
        return result;

    }

    public Category getRoot(String id, boolean reNew) {
        if (mRoot != null && !reNew) return mRoot;
        Category category = new Category();
//		mCategory.title="ROOT";
        category._id = id;
        BaseObject dbObject = selectFirst(category);
        if (dbObject == null) {
            mRoot = new Category();
            mRoot._id = "";
        } else mRoot = (Category) dbObject;
        return mRoot;
    }

    public Category getRoot(String id) {
        return getRoot(id, false);
    }

    public Category getRoot() {
        return mRoot;
    }

    public String getRootId() {
        Category root = getRoot();

        return root == null ?"": root._id;
    }

    public List<?> search(String txt) {
        Category content = new Category();
//		content.images="";
        List<Category> contents = (List<Category>) select(content);
        List<Category> results = new ArrayList<Category>();
        for (Category content1 : contents)
            if (content1.search(txt)) results.add(content1);
        return results;
    }

    public List<?> getContents(String where) {

        return select(getContentSampleForQuery(), where);
    }

    public Content getContent(String id) {

        return (Content) selectFirst(getContentSampleForQuery(), String.format(Locale.ENGLISH, "_id='%s'", id));
    }

    public void setMark( String contentId, String tag, int index, int important, int learned) {
        Marked marked = getMarked(contentId, tag, index);

        if (marked != null) {
            marked.learned = learned;
            marked.marked = important;
//            marked.cat = catId;
            if (marked.learned > 0 || marked.marked > 0)
                update(marked, new String[]{"_id", "address"});
            else delete(marked, new String[]{"_id", "address"});
            return;
        }
        marked = new Marked();
        marked.address =Content.getHelper().makeAddress(contentId,tag,index);// String.format(Locale.ENGLISH, Content.getHelper().getPrefix()+":article.%d.%s.%d", contentId, tag, index);
        marked.id = contentId;
        marked.learned = learned;
        marked.marked = important;
//        marked.cat = catId;

        insert(marked);
    }

    public Marked getMarked(String contentId, String tag, int index) {
        Marked marked = new Marked();
//		marked._id=contentId;
//		marked.address=String.format(Locale.ENGLISH,"h1.%d",h1Index);
        marked = (Marked) selectFirst(marked, String.format(Locale.ENGLISH, "_id='%s' AND address LIKE \'%%%s.%d\'", contentId, tag, index));
        return marked;
    }

    public List<?> getMarkedJoined(String parentCat, int marked, int learned) {
        return selectByQuery(new MarkedJoinedObject(), MarkedJoinedObject.getSelectString(parentCat, marked, learned));

    }

    public List<?> getBookIndexItems(String parentCat, String parenItem) {

        BookIndexItem select = new BookIndexItem();
        if (Text.isEmpty(parenItem) ) select.cat = parentCat;

        select.parent_id = ( Text.isEmpty(parenItem)) ? parenItem : "";
        return select(select);
    }

    public static String getLikeForSearch(String col, String input) {
        return col + " LIKE " + input;

    }
    public BookIndexItem getBookIndexItemByContentId(String content_id) {
        if (Text.isEmpty(content_id)) return null;
        BookIndexItem select = new BookIndexItem();
        select.content_id = content_id;

        return (BookIndexItem) selectFirst(select);
    }
    public int getParalellContentsCount(String content_id) {
        String tabale=new BookIndexItem().getTableName();
        return getInt(new StringBuilder(100).append("SELECT COUNT(*) AS val FROM ").append(tabale).append(" AS b WHERE b.parent=(SELECT b1.parent FROM ").append(tabale).append(" AS b1 WHERE b1.content_id='").append(content_id).append("')").toString());
//        if (content_id < 0) return null;
//        BookIndexItem select = new BookIndexItem();
//        select.content_id = content_id;select.parent
//
//        return (BookIndexItem) selectFirst(select);
    }

    public BookIndexItem getBookIndexItemParentByContentId(String content_id) {
        if (Text.isEmpty(content_id)) return null;
        BookIndexItem select = new BookIndexItem();
        select.content_id = content_id;

        BookIndexItem item = (BookIndexItem) selectFirst(select);
        if (item == null) return null;
        select = new BookIndexItem();
        select._id = item.parent_id;
        return (BookIndexItem) selectFirst(select);
    }

    public BookIndexItem getParentBookIndexItemByContentId(String content_id) {
        if (Text.isEmpty(content_id)) return null;

        BookIndexItem select = getBookIndexItemByContentId(content_id);
        if (select == null) return null;
        BookIndexItem select2 = new BookIndexItem();
        select2._id = select.parent_id;

        return (BookIndexItem) selectFirst(select2);
    }

    public List<?> getBookIndexItemChilds(BookIndexItem parenItem) {
        if (parenItem == null) return new ArrayList<>();
        BookIndexItem select = new BookIndexItem();
        select.parent_id = parenItem._id;
        return select(select);
    }
    public int getBookIndexItemChildCount(String parentId) {
        return getCount(new BookIndexItem().getTableName(),"parent='"+parentId+"'");
//        if (parenItem == null) return new ArrayList<>();
//        BookIndexItem select = new BookIndexItem();
//        select.parent = parenItem._id;
//        return select(select);
    }
    public List<?> getSubContentItemChilds(BookIndexItem parenItem) {
        if (parenItem == null) return new ArrayList<>();
        StringBuilder q=new StringBuilder("SELECT a.* FROM ").append(getContentSampleForQuery().getTableName()).append(" AS a LEFT JOIN ").append(parenItem.getTableName()).append(" AS b ON a._id=b.content_id WHERE b.parent='").append(parenItem._id).append("'");
        if (getContentSampleForQuery().isSortable())q.append(" ORDER BY b.").append(getContentSampleForQuery().getOrderColumnName()).append(getContentSampleForQuery().getReverseOrder()?" DESC":" ASC");
        return selectByQuery(getContentSampleForQuery(),q.toString());
    }

    //	public boolean getBookIndexItemHasChild(BookIndexItem parenItem) {
//
//		BookIndexItem select=new BookIndexItem();
//		select.parent=parenItem._id;
//		return selectFirst(select)!=null;
//	}
    public boolean isBookIndexItemLeaf(BookIndexItem item) {
//		BookIndexItem select=new BookIndexItem();
//		select.parent=item._id;
        String query = "SELECT Count(*) AS id From [bookindexitems] WHERE parent='" + item._id+"'";
        List<Id> countList = (List<Id>) selectByQuery(new Id(), query);
        if (countList.size() < 1) return false;
        return Integer.parseInt(countList.get(0).id) < 1;
    }

    public boolean getBookIndexItemHaveChilds(BookIndexItem parenItem) {

        BookIndexItem select = new BookIndexItem();
        select.parent_id = parenItem._id;
        return selectFirst(select) != null;
    }

    public boolean isBookIndexItemLastNode(BookIndexItem parenItem) {
        String pid=(Text.isEmpty(parenItem.root_parent_id)) ?parenItem._id: parenItem.root_parent_id;
        String query = "SELECT MAX(level) AS _id From [bookindexitems] WHERE [root_parent_id] LIKE '" + pid + ",%' OR [root_parent_id]='"+pid+"'";
        List<Id> maxList = (List<Id>) selectByQuery(new Id(), query);
        if (maxList.size() < 1) return false;
        return maxList.get(0).id.equals(0) || maxList.get(0).id.equals(parenItem.level + 1);
    }

    public BookIndexItem getBookIndexItems(String id) {

        BookIndexItem select = new BookIndexItem();
        select._id = id;

        return (BookIndexItem) selectFirst(select);
    }

    public List<?> getContentsByAlias(String alias) {
        Content select = (Content) getContentSampleForQuery(true);
        select.alias = alias;
        return select(select, "alias=" + select.getDbValue("alias"));
    }
    public Content getContentsByAlias(String alias,String catAlias) {
        StringBuilder q=new StringBuilder(100).append("SELECT c.* FROM ").append(getContentSampleForQuery().getTableName()).append(" AS c LEFT JOIN categories AS cat ON c.cat=cat._id WHERE cat.alias='").append(catAlias).append("' AND c.alias='").append(alias).append("'");
        List list=selectByQuery(getContentSampleForQuery(),q.toString());
        return list.size()>0? (Content) list.get(0) :null;
//        Content select = (Content) getContentSampleForQuery(true);
//        select.alias = alias;Category
//        return select(select, "alias=" + select.getDbValue("alias"));
    }

    public List<?> getContents(Category parentCat) {
        Content content = (Content) getContentSampleForQuery(true);
        content.cat = parentCat._id;
        return select(content);
    }

    public List<?> getContents(int parentCat, int parentIndex) {
        StringBuilder q =new StringBuilder(200).append("SELECT a.* FROM [contents] AS a left join [bookindexitems] AS b on b.[content_id]=a.[_id] where b.[parent]=").append(parentIndex);
        if (parentCat>-1)q.append(" AND b.[cat]=").append(parentCat);
        if (getContentSampleForQuery().isSortable())q.append(" ORDER BY b.").append(getContentSampleForQuery().getOrderColumnName()).append(getContentSampleForQuery().getReverseOrder()?" DESC":" ASC");

        return selectByQuery( getContentSampleForQuery(), q.toString());

    }

    public List<?> getContents(List<Integer> ids) {
        return select(getContentSampleForQuery(), "_id", ids);
    }


    public List<?> getContentsByCatId(String parentCatId) {
        Content content = (Content) getContentSampleForQuery(true);
        content.cat = parentCatId;
        return select(content);
    }

    public List<?> search(Map<String,String> options, String catId, String txt, int limit, int offset) {
       String query=Content.getHelper().getSearchQuery(options, catId, txt, limit, offset);
        if (query==null)return new ArrayList<>();
        return selectByQuery(Content.getHelper().getContentSampleForSearch(),query);

    }

    public BaseObject getContentSampleForQuery(boolean forceNew) {
        return  Content.getHelper().getContentSampleForQuery(forceNew);
    }
    public BaseObject getContentSampleForQuery() {
        return  Content.getHelper().getContentSampleForQuery();
    }
  public Content getNextContent(String contentId,boolean next) {
      boolean reverseOrder=getContentSampleForQuery().getReverseOrder();
      StringBuilder q = new StringBuilder(60);
      q.append("SELECT * FROM bookindexitems WHERE content_id='").append(contentId).append("' LIMIT 1");

      BookIndexItem b= (BookIndexItem) selectByQueryFirst( new BookIndexItem(), q.toString());
      if (b==null)return null;
      q = new StringBuilder(200)
              .append("SELECT * FROM contents WHERE _id=(SELECT content_id FROM bookindexitems AS b1 WHERE b1.cat=").append(b.cat).append(" AND b1.parent='").append(b.parent_id)
			.append("' AND '").append(b.title.toLowerCase()).append("'").append(next?"<":">").append("b1.title COLLATE NOCASE ")
			  .append(" ORDER BY b1.title   COLLATE NOCASE ").append(next?(reverseOrder?" DESC":" ASC"):(!reverseOrder?" DESC":" ASC"))
			  .append(" LIMIT 1) LIMIT 1");

     // .append("SELECT c.*  FROM contents AS c left join bookindexitems AS b ON b.content_id=c._id LEFT JOIN bookindexitems AS b1 ON b1.cat=b.cat AND b1.parent=b.parent WHERE b1.content_id=").append(contentId).append(" AND  LOWER(b.title)").append(next?">":"<").append("LOWER(b1.title) ORDER BY LOWER(c.title) ").append(next?(reverseOrder?" DESC":" ASC"):(!reverseOrder?" DESC":" ASC")).append(" LIMIT 1");

    return (Content)  selectByQueryFirst( getContentSampleForQuery(), q.toString());

    }

    public List<?> filterByRegex(List<?> list, List<String> fieldNames, String regex, boolean force) {
        if (!force && !regex.contains("(")) return list;
        List<BaseObject> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            BaseObject object = (BaseObject) list.get(i);
            for (String f : fieldNames) {
                if (Text.isMatchByRegex(String.format(Locale.ENGLISH, "%s", object.getValue(f)), regex)) {
                    result.add(object);
                    break;
                }
            }
        }
        return result;
    }

    public Content getFirstHaveImageContent(String catId) {

        BaseObject baseObject = selectFirst(getContentSampleForQuery(), "cat='" + catId + "' AND images LIKE '[%/%]'");
        if (baseObject == null) return null;
        return (Content) baseObject;

    }

    public Content getIntroContent(String catId) {

        BaseObject baseObject = selectFirst(getContentSampleForQuery(), "cat='" + catId + "' AND alias='intro'");
        if (baseObject == null) return null;
        return (Content) baseObject;

    }

    //	public void updateOrder(List<Ordering> orderingList)
//	{   String table=getContentSampleForQuery().getTableName();
////		select.order=order.
//
//
////		SQLiteDatabase database=getDatabase();
//		if (!openDatabase())return;
//		for (Ordering order:orderingList){
//			ContentValues values = new ContentValues();
//			values.put("order",order.order);
//			getDatabase().update(table, values, "_id="+String.valueOf(order._id), null);
//		}
//		close();
//	}
    public void autoInsertUpdateContent(List<BaseObject> items) {
        if (items == null || items.size() < 1) return;
//		for (int i = 0; i < items.size(); i++) {
//			if (!((Content) items.get(i)).isAvailable()) items.get(i).setValue("version",-1);
//		}
        autoInsertUpdateById(items);
    }

    public static void registHistory(Context context,Suggest suggest){
//        Suggest suggest1= (Suggest) getDataSource(activity).selectFirst(suggest);
//        if (suggest1!=null)
             getDataSource().delete(suggest,new String[]{"title"});
        getDataSource().insert(suggest);

    }

    public static List getHistory(Context context,int limit){
//        Suggest suggest1= (Suggest) getDataSource(activity).selectFirst(suggest);
//        if (suggest1!=null)
    return   getDataSource().select(new Suggest(),null,limit);

    }
    public static List getHistory(Context context,int limit,String like){
//        Suggest suggest1= (Suggest) getDataSource(activity).selectFirst(suggest);
//        if (suggest1!=null)
    return   getDataSource().select(new Suggest(),"title like '"+like+"%'",limit);

    }
    public static boolean haveHistory(Context context){
    return   getDataSource().getCount(new Suggest().getTableName(),null)>0;

    }
}
