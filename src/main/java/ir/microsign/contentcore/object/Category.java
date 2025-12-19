package ir.microsign.contentcore.object;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ir.microsign.contentcore.database.DataSource;
import ir.microsign.dbhelper.Const;
import ir.microsign.utility.Image;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/14/14.
 */
public class Category extends BaseObject {
    public String  _id,title, alias, create,access, description, lang, parent, parents,search;
    public Integer autoid,__v;
    public Boolean published;
   public Image mFirstImage = null;
    public List<Image> mImages = null;
    @Override
    public Const.fieldType getSQLFieldType(String name) {
        if (name.equals("_id")) return Const.fieldType.STRING_UNIQUE;
        return super.getFieldType(name);
    }
    public boolean search(String txt) {
        return Text.isContains(title, txt, true) || Text.isContains(description, txt, true);
    }
    public boolean isUserAccess() {return true;
//        if (Text.isEmpty(access))return true;
//        return isUserAccess(utility.getSavedUser(false));

    }

//    public boolean isUserAccess(User user) {
//        if (user == null)
//            user = utility.getSavedUser(false);
//        if (user == null) return false;// isAvailable();
//        return user.hasAccess(access);
//    }
//public String[] getParents(){
//        if (Text.isEmpty(parents))return new String[]{};
//        return parents.replaceAll("\\[]","").split(",");
//}
public String getInParentsPhrase(){
        if (Text.isEmpty(parents))return  "('')";
        return parents.replace("[","(").replace("]",")");//.replace(",","','");
}
    @Override
    public String getJsonArrayName() {
        return "categories";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Category)) return false;
        return (this._id.equals(((Category) o)._id));
    }

    public boolean hasSearchOption() {
        return !Text.isNullOrEmpty(search);
    }

    public Map<String,String> getSearchPairs(String txt, boolean force) {
        if (force && !hasSearchOption()) search = Content.getHelper().getDefaultSearchOptions();
        Map<String,String>map=new HashMap<>();
        if (Text.isNullOrEmpty(search)) return map;
        String[] parts = search.replace('(', '<').replace(')', '>').split(",");
        if (parts == null || parts.length == 0) return map;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String[] field = part.split("\\.");
            String like = field.length == 1 ? txt : String.format(Locale.ENGLISH, field[1], txt ).replace("*", "%");
            like ="\'" +
//                    Text.getLike(
                    like
//            )
                    + "\'";

            map.put(field[0], like);
        }
        return map;
    }

    public String getPath() {
       List<Category> cats= (List<Category>) DataSource.getDataSource().select(new Category(),"_id IN "+parents.replace("[","(").replace("]",")"),"parents",false,false,0);
       String path="";
        for (Category cat : cats) {

            path+=cat.title+"/";
        }
        path+=title;
        return path;
    }

    public List<String> getExceptionFields() {
        List<String> list = super.getExceptionFields();
        list.add("mFirstImage");
        list.add("mImages");
        return list;
    }

    public Image getFirstImage(boolean thumb) {
        return Content.getHelper().getCategoryFirstImage(this,thumb);
    }

    @Override
    public Class getViewClass() {
        return Helper.getHelper().getCategoryViewClass();
    }

    public List<Image> getImages() {
        return Content.getHelper().getCategoryImages(this);
    }

    public Image getCoverImage(DataSource dataSource) {
        return getFirstImage(true);
//		if (firstImage!=null)return firstImage;
//		Content intro= dataSource.getIntroContent(_id);
//		if (intro==null)return null;
//		return intro.getThumbImage();
    }
//	public int position=0;

    @Override
    public String getOrderColumnName() {
        return "order";
    }

    @Override
    public boolean getReverseOrder() {
        return false;
    }
}

