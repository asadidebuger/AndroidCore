package ir.microsign.gallery.object;

//import net.tarnian.dbhelper.object.BaseObject;

/**
 * Created by Mohammad on 6/15/14.
 */
public class Category extends BaseObject {
    public Integer autoid, cid, asset_id, owner, exclude_search, lft, rgt, in_hidden, access, exclude_toplists, thumbnail, level, img_position, hidden, published, parent_id;
    public String alias, description, metakey, metadesc, params, password, catpath, name;

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Category)) return false;
        return ((Category) o).cid.equals(cid);
    }
}

