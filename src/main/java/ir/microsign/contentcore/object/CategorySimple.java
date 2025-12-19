package ir.microsign.contentcore.object;

import java.util.List;

import ir.microsign.contentcore.database.DataSource;
import ir.microsign.utility.Image;

/**
 * Created by Mohammad on 6/14/14.
 */
public class CategorySimple extends BaseObject {
    public String description,title, metadesc;
    public Integer autoid, access, id, level, lft, parent_id, published, rgt;
    Image mFirstImage = null;
    List<Image> mImages = null;

//    public boolean search(String txt) {
//        return Text.isContains(title, txt, true) || Text.isContains(description, txt, true);
//    }

    @Override
    public String getJsonArrayName() {
        return "categories";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof CategorySimple)) return false;
        return (this.id.equals(((CategorySimple) o).id));
    }


//    public String[] getExceptionFields() {
//        return new String[]{"mImages", "mFirstImage"};
//    }
    @Override
    public List<String> getExceptionFields() {
        List<String> list = super.getExceptionFields();
        list.add("mFirstImage");
        list.add("mImages");
        return list;
    }
    public Image getFirstImage(boolean thumb) {
        if (mFirstImage != null) return mFirstImage;
        if (getImages().size() < 1) return null;
        mFirstImage = getImages().get(0);
//        mFirstImage.mType = thumb ? Image.THUMB : Image.NORMAL;

        return mFirstImage;
    }

    List<Image> getImages() {
        if (mImages != null) return mImages;
        mImages = Content.getImages(description);
        return mImages;
    }

    @Override
    public String getTableName() {
        return "categories";
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
        return "lft";
    }

    @Override
    public boolean getReverseOrder() {
        return false;
    }
}

