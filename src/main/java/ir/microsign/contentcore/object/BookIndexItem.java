package ir.microsign.contentcore.object;

import ir.microsign.contentcore.database.DataSource;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/14/14.
 */
public class BookIndexItem extends BaseObject {
    public String title, root_parent_id, lang, content_id, access,_id, parent_id, cat;
    public Integer autoid,  level, order;
    public Boolean published;
public boolean languageIs(String lang){
    if (lang==null) return this.lang ==null;
    return lang.equals(this.lang);
}
    @Override
    public String getJsonArrayName() {
        return "bookindexlists";
    }

    public boolean isLastNode(DataSource dataSource) {
        return dataSource.isBookIndexItemLastNode(this);
    }
//	@Override
//	public String[] getExceptionFields() {
//		return new String[]{"position","selected"};
//	}

    @Override
    public String getOrderColumnName() {
        return "title";
    }

    @Override
    public boolean getReverseOrder() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BookIndexItem)) return false;
        return _id.equals(((BookIndexItem) o)._id);//&& ((BookIndexItem)o).cat.equals(cat);
    }

    public Content getContent(DataSource dataSource) {
        return dataSource.getContent(content_id);
    }

    public String getPath(DataSource dataSource) {
        return dataSource.getContent(content_id).getPath(dataSource);
    }

    @Override
    public String toString() {
        return title;
    }
    public boolean filter(String text,boolean startWith,boolean endWith,boolean noCase) {

        if (Text.isEmpty(text)) return true;
        String src = noCase ? toString().toLowerCase()
//                .replaceAll("  +", " ")
                : toString()
//                .replaceAll("  +", " ")
                , filter = text
                .replaceAll("  +", " ");
//        if (startWith && endWith) return src.equals(filter);
//        if (!startWith && !endWith) return src.contains(filter);
//        if (startWith) return src.startsWith(filter);
        return src.startsWith(filter)||src.contains(" "+filter);
    }

}

