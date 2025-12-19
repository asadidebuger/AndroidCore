package ir.microsign.contentcore.object;

import java.util.Locale;

import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/15/14.
 */
public class MarkedJoinedObject extends BaseObject {
    public Integer autoid, marked, learned, id, catid;
    public String address, title, introtext, fulltext, cat_title;

    static public String getSelectString(String catId, int marked, int learned) {
        StringBuilder sb = new StringBuilder(300);
        sb.append("SELECT m.autoid,m._id, m.marked,m.learned,m.address,a.title,a.intro,a.full,c.title AS cat_title,a.cat FROM contents AS a LEFT JOIN markeds AS m ON m._id=a._id LEFT JOIN categories AS c ON a.cat=c._id");

        sb.append(" WHERE ");
        if (Text.notEmpty(catId)) sb.append(String.format(Locale.ENGLISH, "a.cat=\'%s\' AND ", catId));
        if (marked > 0) sb.append(String.format(Locale.ENGLISH, "m.marked=%d", marked));
        if (learned > 0) sb.append(String.format(Locale.ENGLISH, "m.learned=%d", learned));
        return sb.toString();
    }

    //	@Override
//	public String getJsonArrayName() {
//		return "order";
//	}
    public String getTitle() {
        return title == null ? "" : title;

    }

    public String getCatTitle() {
        return cat_title == null ? "" : cat_title + "> ";

    }

    public String getFullText() {
        return fulltext == null ? "" : fulltext;

    }

    public String getIntroText() {
        return introtext == null ? "" : introtext;

    }

//    public StringBuilder getHtml() {
//
//        try {
//            return getHtmlText();
//        }
////
////
////		StringBuilder sb=new StringBuilder();
////		sb.append("<a style=\"color:inherit;text-decoration:none;\" href=\"").append(address==null?"":address).append("\">")
////		.append(getHtmlText()).append("</a>");
////		return sb.toString();}
//        catch (Exception ex) {
//            ex.printStackTrace();
//            return new StringBuilder();//"error:" + ex.getMessage();
//        }
//    }

   public StringBuilder getHtml(StringBuilder sb) {
        String[] add = address.split("\\.");
        int len = add.length;//
        String tag = add[len - 2];
        int index = Integer.parseInt(add[len - 1]);
        String src=getIntroText() + getFullText();
        String target=Text.tryGetMatch(src,"<"+tag+".*?</"+tag+">",index);
//        StringBuilder sb=new StringBuilder(target.length());
        sb
                .append("<a class=\"delete ltr\" href=\"").append( String.format(Locale.ENGLISH, "%s:delete:mark.%d.%d.%s.%d.%d.%d",Content.getHelper().getPrefix(), catid, id, tag, index, isMarked() ? 1 : 0, isLearned() ? 1 : 0)).append("\"> x ").append("</a>")
                .append("<a class=\"marked ltr\" href=\"").append( address == null ? "" : address).append("\">")

                .append(target)

                .append("<div class=\"path\">").append(getCatTitle() + " " + getTitle())
                .append("</div>")

                .append("</a>");

//
//        Element content = (Element) Document.getAddress(getIntroText() + getFullText(), tag, index);
//        if (content == null) {
//            content = (Element) Document.getAddress("<" + tag + ">" + title /*activity.getContext().getString(R.string.content_marked_not_found)*/ + "</" + tag + ">", tag, index);
//        }
//
//        content.setAttribute("style", content.getAttribute("style"));
//        org.w3c.dom.Document doc = content.getOwnerDocument();
//        Element div = doc.createElement("div");
//        div.setTextContent(getCatTitle() + " " + getTitle());
//        div.setAttribute("style", "font-size:60%;");
//        content.appendChild(div);
//        Element div0 = doc.createElement("div");
//        div0.setAttribute("style", "display:block;");
//        Element delete = doc.createElement("a");
//        delete.setAttribute("style", "color:red;text-decoration:none;font-size:100%;float:right;border-radius:3px;border:1px dashed red;padding:1px 5px 1px 5px; margin:2px;");
//        delete.setAttribute("href", String.format(Locale.ENGLISH, "dic:delete:mark.%d.%d.%s.%d.%d.%d", cat, _id, tag, index, isMarked() ? 1 : 0, isLearned() ? 1 : 0));
//        delete.setTextContent(" x ");
//        Element a = doc.createElement("a");
//        a.setAttribute("style", "color:inherit;text-decoration:none;");
//        a.setAttribute("href", address == null ? "" : address);
//        content.getParentNode().appendChild(div0);
//        a.appendChild(content);
//        div0.appendChild(delete);
//        div0.appendChild(a);
//        Document.getNodeString(div0);
        return sb;
    }

    @Override
    public String getTableName() {
        return "markeds";
    }

    public boolean isMarked() {
        return marked != null && marked > 0;
    }

    public boolean isLearned() {
        return learned != null && learned > 0;
    }
}
