package ir.microsign.content.object;

import ir.microsign.dbhelper.object.BaseObject;

/**
 * Created by Mohammad on 6/14/14.
 */
public class BookIndexItem extends BaseObject {

    public Integer autoid, order,  version;
    public String  _id, catid,access;
    public Boolean published;

    @Override
    public String getJsonArrayName() {
        return "bookindexlists";
    }

    @Override
    public String getOrderColumnName() {
        return null;
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

}

