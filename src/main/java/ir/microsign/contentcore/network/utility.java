package ir.microsign.contentcore.network;

import android.content.Context;

import java.util.List;

import ir.microsign.api.Api;
import ir.microsign.api.object.Response;
import ir.microsign.contentcore.object.Content;
import ir.microsign.dbhelper.object.BaseObject;

/**
 * Created by Mohammad on 6/17/14.
 */
public class utility {

//    public static String getUrlArticles() {
//        return ir.microsign.api.network.utility.makeUrl(false, "content", "articles");
//    }

    public static void getArticles(final Context context,  String catId, final onReceivedListener l) {
        Content content =new Content();
        content.cat=catId;
        Api.get("content", "content", content, new Api.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (!response.succeed()){
                    l.error();
                    return;
                }
                l.onSucceed(response.getObjectArray(Content.class,"content"));
            }
        });
//        iutility.getBaseParams(false, new ReceiverAdaptor.ParamsReceived() {
//            @Override
//            public void OnReceived(List<NameValuePair> params, boolean success) {
//                ReceiverAdaptor receiveDbObjects = new ReceiverAdaptor();
//                params.add(new BasicNameValuePair("cat", String.valueOf(catId)));
//                receiveDbObjects.getDbObjectsStepByStep(activity, new Content(), getUrlArticles(), params, l);
//
//            }
//        });
    }
    public static interface onReceivedListener{
        void onSucceed(List<BaseObject> list);
        void  error();
    }


}
