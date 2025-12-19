package ir.microsign.gallery.network;

import android.content.Context;


import java.util.List;
import java.util.Locale;

/**
 * Created by Mohammad on 6/17/14.
 */
public class utility {
    public static String getGalleryCategoriesUrl() {
        return "";
        //return ir.microsign.api.network.utility.makeUrl(false, "gallery", "categories");
    }

    public static String getGalleryImagesUrl() {
        return "";
        //return ir.microsign.api.network.utility.makeUrl(false, "gallery", "images");
    }

    public static String getImageFileUrl(int id, String type) {
        return "";
        //String suffix = "option=com_joomgallery&view=image&format=raw&id=" + id;
       // return ir.microsign.api.network.utility.getUrl(false) + suffix + "&type=" + type;
    }

    public static String getImageFileUrl(String src) {
        return "";
        //String src1 = src.toLowerCase();
//        if (src1.startsWith("http://") || src1.startsWith("https://")) return src;
//        return ir.microsign.api.network.utility.getUrlCustomer(true) + "/" + src;
    }

//    public static void getGalleryCategories(final Context context, final ir.microsign.api.interfaces.Listener.ReceiveObjectListener l) {
//
//        ir.microsign.api.network.utility.getBaseParams(false, new ReceiverAdaptor.ParamsReceived() {
//            @Override
//            public void OnReceived(List<NameValuePair> params, boolean success) {
//                ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor(500);
//                receiveDbObjects.getDbObjectsStepByStep(context, new Category(), getGalleryCategoriesUrl(), params, l);
//            }
//        });
//    }
//
//    public static void getGalleryImages(final Context context, final ir.microsign.api.interfaces.Listener.ReceiveObjectListener l) {
//
//        ir.microsign.api.network.utility.getBaseParams(false, new ReceiverAdaptor.ParamsReceived() {
//            @Override
//            public void OnReceived(List<NameValuePair> params, boolean success) {
//                ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor(500);
//                receiveDbObjects.getDbObjectsStepByStep(context, new Image(), getGalleryImagesUrl(), params, l);
//            }
//        });
//    }
//
//    public static void getGalleryImages(final Context context, final int catid, final ir.microsign.api.interfaces.Listener.ReceiveObjectListener l) {
//
//        ir.microsign.api.network.utility.getBaseParams(false, new ReceiverAdaptor.ParamsReceived() {
//            @Override
//            public void OnReceived(List<NameValuePair> params, boolean success) {
//                ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor(500);
//                params.add(new BasicNameValuePair("catid", String.format(Locale.ENGLISH, "%d", catid)));
//                receiveDbObjects.getDbObjectsStepByStep(context, new Image(), getGalleryImagesUrl(), params, l);
//            }
//        });
//    }
//
//    public static String getUrlPostImage() {
//        return ir.microsign.api.network.utility.makeUrl(false, "gallery", "upload");
//    }
//
//    public static void uploadImage(final String imagePath, final String title, final String desc, final Listener.RemoteResultListener l) {
//
//        ir.microsign.api.network.utility.getBaseParams(false, new ReceiverAdaptor.ParamsReceived() {
//            @Override
//            public void OnReceived(List<NameValuePair> params, boolean success) {
//                ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor();
//                params.add(new BasicNameValuePair("arrscreenshot[]", imagePath));
//                params.add(new BasicNameValuePair("imgtitle", title));
//                if (!Text.isNullOrEmpty(desc)) params.add(new BasicNameValuePair("imgtext", desc));
//
//                receiveDbObjects.upload(getUrlPostImage(), params, l);
//            }
//        });
//
//    }
}
