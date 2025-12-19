package ir.microsign.utility;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Mohammad on 17/09/2015.
 */
public abstract class Document implements org.w3c.dom.Document {
    //	public Document(String text,String rootTag){
//		this=getDocument( text, rootTag);
//	}
    public static org.w3c.dom.Document getDocument(String text, String rootTag) {
        try {
            StringBuilder sb = new StringBuilder(text.length()+rootTag.length()*3);
            DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFact.newDocumentBuilder();
            if (!Text.isNullOrEmpty(rootTag)) {
                sb.append("<").append(rootTag).append(">").append(text).append("</").append(rootTag).append(">");
                return builder.parse(new InputSource(new StringReader(sb.toString())));
            } else return builder.parse(new InputSource(new StringReader(text)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

//    public static List<org.apache.http.NameValuePair> getValuesByAttrib(Node node, final List<String> classes, String replaceBreaks) {
//
//        List<org.apache.http.NameValuePair> result = new ArrayList<>();
//        if (node == null) return result;
////	  if (!node.hasChildNodes())
//        if (!(node instanceof Element)) return result;
//        String cls = ((Element) node).getAttribute("class");
//        if (!Text.isNullOrEmpty(cls)) {
//            for (String str : classes) {
//                if (str.equalsIgnoreCase(cls)) {
//                    String value = getNodeString(new StringBuilder(),node).toString();
//                    if (replaceBreaks != null) {
//                        value = value.replaceAll("<[ /]*br[^>]*>", replaceBreaks);
//                        value = value.replaceAll("<[^<>]*>", "");
//                    }
////					try {
////						if (value==null)value=node.getFirstChild().getNodeValue();
////					}
////					catch (Exception ex){
////						Log.e("Document","Node Value is Null:"+getNodeString(node));
////					}
//                    result.add(new BasicNameValuePair(str, value));
//
//                    break;
//                }
//            }
//        }
//        if (!node.hasChildNodes()) return result;
//        NodeList childs = node.getChildNodes();
//        for (int i = 0; i < childs.getLength(); i++) {
//            result.addAll(getValuesByAttrib(childs.item(i), classes, replaceBreaks));
//        }
//        return result;
//    }

    public static Node getAddress(String txt, String tag, int index) {
//		String[] addreses=address.split("\\.");
//		int index=Integer.parseInt(addreses[1]);
        org.w3c.dom.Document doc = getDocument(txt, "body");
        NodeList list = doc.getElementsByTagName(tag);
        if (list.getLength() <= index) return null;
        return list.item(index);
    }

//    public static String getNodeString(Node node) {
//        try {
//            StringWriter writer = new StringWriter();
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.transform(new DOMSource(node), new StreamResult(writer));
//            String output = writer.toString();
//            return output.substring(output.indexOf("?>") + 2);//remove <?xml version="1.0" encoding="UTF-8"?>
//        } catch (TransformerException e) {
//            e.printStackTrace();
//        }
//        return node.getTextContent();
//    }
    public static StringBuilder getNodeString(StringBuilder sb,Node node ) {

        if (node.getNodeType()==3){ if (node.getNodeValue()!=null)sb.append(node.getNodeValue());return sb;}
        if (node.getNodeType()==5){
            sb.append("&").append(node.getNodeName()).append(";");
            return sb;
        }
        if (node.getNodeType()!=9) {
            sb.append("<").append(node.getNodeName());
            if (node.hasAttributes()) {
                NamedNodeMap attr = node.getAttributes();
                for (int i = 0, l = attr.getLength(); i < l; i++) {
                    Node at = attr.item(i);
                    sb.append(" ").append(at.getNodeName()).append("=\"").append(at.getNodeValue()).append("\"");
                }
            }
            boolean beClose=node.getNodeName().equals("img")||node.getNodeName().equals("br")||node.getNodeName().equals("hr");
           if (beClose)  {sb.append("/>");return sb;}
            sb.append(">");
            if (node.getNodeValue() != null) sb.append(node.getNodeValue());
        }
        if (node.hasChildNodes()){
            NodeList nodeList=node.getChildNodes();
            for (int i=0,l=nodeList.getLength();i<l;i++) {
                getNodeString(sb, nodeList.item(i));
            }}

        if (node.getNodeType()!=9) sb.append("</").append(node.getNodeName()).append(">");
return sb;
    }

    public static void AddAttribToDoc(Node node, String attrsName, String value, String newAttrib, String newValue) {
        try {


            if (node.hasAttributes()) {
                NamedNodeMap attrs = node.getAttributes();
                Node dir = attrs.getNamedItem(attrsName);
                if (dir != null && dir.getNodeValue().equalsIgnoreCase(value)) {

                    ((Element) node).setAttribute(newAttrib, newValue);

                }
            }
            if (node.hasChildNodes()) {
                NodeList list = node.getChildNodes();
                for (int i = 0, count = list.getLength(); i < count; i++)
                    AddAttribToDoc(list.item(i), attrsName, value, newAttrib, newValue);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
