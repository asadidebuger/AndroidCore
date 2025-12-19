package ir.microsign.api;

import android.content.Context;

import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.App;
import ir.microsign.context.Application;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.net.Utility;
import ir.microsign.utility.Text;

//import ir.microsign.net.Response;

public class AppApi {

    static String API_ADDRESS=null;
    static String DL_ADDRESS=null;
    static boolean apiResolved=false,apiSuspend=false;//,apiErro=false;
    public static String getApiAddress(Context context){
        App app=null;

        if (API_ADDRESS==null){
            if (!Utility.isConnect(context)){
                app=DataSourceApi.getSource(Application.getContext()).getApp();
                if (app==null) return null;

            }
            if (app==null){
                for (int i=0;i<20;i++)
                {
                    resolveApi(context);
                    while (apiSuspend) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (apiResolved){
                        app=DataSourceApi.getSource(Application.getContext()).getApp();
                        break;
                    }
                }
            }
            if (app==null) return API_ADDRESS;
            API_ADDRESS=app.api;
            DL_ADDRESS=app.dlUrl;

            if(Text.isEmpty(DL_ADDRESS)&&API_ADDRESS!=null){
                DL_ADDRESS=API_ADDRESS.substring(0,API_ADDRESS.indexOf('/',8));
            }
            if (DL_ADDRESS!=null&&!DL_ADDRESS.endsWith("/"))DL_ADDRESS+="/";
        }
return API_ADDRESS;
    }

    public static String getDownloadBaseUrl(Context context){
       if (DL_ADDRESS==null){
           getApiAddress(context);
       }
        return DL_ADDRESS;
    }
    static void resolveApi(Context context){

        apiSuspend=true;
        Api.updateApps(context, new Api.onResultListener() {
            @Override
            public void succeed(Object o) {
                apiResolved=true;
                apiSuspend=false;
            }

            @Override
            public void error(String e) {
                apiResolved=false;
                apiSuspend=false;
            }
        });
    }
    public static void sendRequest(Context context,Api.METHOD method, String resource, String function, BaseObject object, Api.onResponseListener l, String... params) {

        Api.sendRequest(getApiAddress(context),method,resource,function,object,l,params);
    }
    public static void sendRequest(Context context,Api.METHOD method, BaseObject object, Api.onResponseListener l, String... params) {
        AppApi.sendRequest(context,method,null,null,object,l,params);
    }


//    public enum RES {
//        Profile,
//        Ticket,
//        App,
//
//    }
//
//    public enum FUNC {
//        SignIn,
//        SignUp,
//        Update,
//        Recover,
//        newPass,
//        Ticket,
//        Message,
//        Profile,
//        Info,
//
//    }
//
//    public enum METHOD {
//        GET,
//        POST,
//        UPDATE,
//        DELETE,
//        PUT,
//        PATCH
//
//    }
//
//    public static class STATE {
//        public static final String
//                NOT_FOUND = "not_found",
//                NOT_EXIST = "not_exist",
//                WRONG_PASS = "wrong_pass",
//                ALREADY_EXIST = "already_exist",
//                MUST_LOGIN = "most_login",
//                TIMEOUT = "timeout",
//                LIMITED = "limited",
//                MISSING_ARGS = "missing_args";
//    }
//
//    public interface onSignInListener {
//        void signed(Profile profile);
//        void wrongPass();
//        void notExist();
//        void error(String e);
//    }
//    public interface onSignUpListener {
//        void registered(Profile profile);
//        void error(String e);
//        void alreadyExist();
//    }
//    public interface onResultListener {
//        void succeed(Object o);
//        void error(String e);
//    }
//    public interface onUpdateListener {
//        void updated(Profile profile);
//        void mustLogin();
//        void error(String e);
//    }  public interface onGetProfileListener {
//        void succeed(Profile profile);
//        void error(String e);
//        void mustLogin();
//    }
//    public interface onDetachListener {
//        void detached();
//        void error(String e);
//
//    }
//public interface onRecoverListener {
//    void verificationSent(Recover recover);
//    void limited();
////    void recovered(Profile profile);
////
////    void timeout();
////
////    void invalid();
//
//    void error(String e);
//}
//public interface onNewPassListener {
////    void verificationSent(Recover recover);
//
//    void recovered(Profile profile);
//
//    void timeout();
//    void limited();
//
//    void invalid();
//
//    void error(String e);
//}
//    public static void getAppInfo(Context context,onResponseListener l){
//        sendRequest(METHOD.GET, RES.App, FUNC.Info,null,l);
//    }
//    public static void postTicket(Ticket ticket,onResponseListener l){
//        sendRequest(METHOD.POST, RES.Ticket, FUNC.Ticket,ticket,l);
//    }
//    public static void postMessage(Message message, onResponseListener l){
//        sendRequest(METHOD.POST, RES.Ticket, FUNC.Message,message,l);
//    }
//    public static void getTickets(onResponseListener l){
//        sendRequest(METHOD.GET, RES.Ticket, FUNC.Ticket,null,l);
//    }
//    public static void getMessages(Message message,onResponseListener l){
//        sendRequest(METHOD.GET, RES.Ticket, FUNC.Message,message,l);
//    }
//    public static void signIn(Profile profile, final onSignInListener l) {
//        sendRequest(METHOD.POST, RES.Profile, FUNC.SignIn, profile, new onResponseListener() {
//            @Override
//            public void onResponse(Response response) {
//
//
//                if (response.succeed()) {
//                    Profile p=response.getObject(Profile.class, FUNC.SignIn.toString());
//                    Utils.setProfileSecret(p.password);
//                    l.signed(p);
//
//                } else {
//                    String e = response.getString();
//                    if (STATE.WRONG_PASS.equals(e)) {
//                        l.wrongPass();
//                    } else if (STATE.NOT_EXIST.equals(e)) {
//                        l.notExist();
//                    } else l.error(e);
//
//                }
//            }
//        });
//    }
//
//    public static void getProfile(final Context context, final onGetProfileListener l) {
//        sendRequest(METHOD.POST, RES.Profile, FUNC.Profile, null, new onResponseListener() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.succeed()) {
//                    Profile profile= response.getObject(Profile.class, FUNC.Profile.toString());
//                    DataSourceApi.getSource(context);
//                    l.succeed(profile);
//                    } else {
//                    String e = response.getString();
//                    if (STATE.MUST_LOGIN.equals(e)) {
//                        Utils.removeProfileSecret();
//                        l.mustLogin();
//                    } else {
//                        l.error(e);
//                    }
//                }
//            }
//        });
//    }
//    public static void updateProfile(Profile profile, final onUpdateListener l) {
//        sendRequest(METHOD.POST, RES.Profile, FUNC.Update, profile, new onResponseListener() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.succeed()) {
//
//                    l.updated((Profile) response.getObject(Profile.class, FUNC.Update.toString()));
//                } else {
//                    String e = response.getString();
//                    if (STATE.MUST_LOGIN.equals(e)) {
//                        Utils.removeProfileSecret();
//                        l.mustLogin();
//                    } else {
//                        l.error(e);
//                    }
//                }
//            }
//        });
//    }
//    public static void updatePermission(String permission,boolean add ,final onGetProfileListener l) {
//       if (add) sendRequest(METHOD.PUT, RES.Profile, FUNC.Update, null, new onResponseListener() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.succeed()) {
//l.succeed((Profile) response.getObject(Profile.class, FUNC.Update.toString()));
//                } else {
//                    l.error(response.getString());
//                }
//            }
//        });
//       else sendRequest(METHOD.DELETE, RES.Profile, FUNC.Update, null, new onResponseListener() {
//           @Override
//           public void onResponse(Response response) {
//               if (response.succeed()) {
//                   l.succeed((Profile) response.getObject(Profile.class, FUNC.Update.toString()));
//               } else {
//                   l.error(response.getString());
//               }
//           }
//       });
//    }
//    public static void recoverPassword(Profile profile,final onRecoverListener l){
//        sendRequest(METHOD.POST, RES.Profile, FUNC.Recover, profile, new onResponseListener() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.succeed()) {
//                    l.verificationSent((Recover) response.getObject(Recover.class, FUNC.Recover.toString()));
//                } else {
//                    String e=response.getString();
//                    if (STATE.TIMEOUT.equals(e)) {
//                        l.limited();
//                    }
//                    else  if (STATE.LIMITED.equals(e)) {
//                        l.limited();
//                    }
//                      else   l.error(e);
//
//                }
//            }
//        });
//    }
//    public static void setNewPassword(Recover recover,final onNewPassListener l){
//        sendRequest(METHOD.POST, RES.Profile, FUNC.newPass, recover, new onResponseListener() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.succeed()) {
//                    l.recovered((Profile) response.getObject(Profile.class, FUNC.newPass.toString()));
//                } else {
//                    String e=response.getString();
//                    if (STATE.TIMEOUT.equals(e)) {
//                        l.timeout();
//                    }
//                    else  if (STATE.LIMITED.equals(e)) {
//                        l.limited();
//                    }
//                    else  if (STATE.WRONG_PASS.equals(e)) {
//                        l.invalid();
//                    }
//                        l.error(e);
//
//                }
//            }
//        });
//    }
//    public static void signUp(Profile profile, final onSignUpListener l) {
//
//        sendRequest(METHOD.POST, RES.Profile, FUNC.SignUp, profile, new onResponseListener() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.succeed()) {
//                    l.registered((Profile) response.getObject(Profile.class, FUNC.SignUp.toString()));
//                } else {
//                    String e=response.getString();
//                    if (STATE.ALREADY_EXIST.equals(e)) {
//                        l.alreadyExist();
//                    } else l.error(e);
//
//                }
//            }
//        });
//    }
//
//    public static void sendRequest(METHOD method, RES resource, FUNC function, BaseObject object, onResponseListener l, String... params) {
//        sendRequest(method, resource.toString(), function.toString(), object, l, params);
//    }
//
//    public static void sendRequest(METHOD method, String resource, String function, BaseObject object, onResponseListener l, String... params) {
//        if (!Utility.isConnect(Application.getContext())) {
//            if (onExceptionListener != null) {
//                l.onResponse(new Response(null));
//                onExceptionListener.noNetwork();
//            }
//            return;
//        }
//        if (System.getUniqueId(Application.getContext()) == null) {
//            if (onExceptionListener != null)
//                onExceptionListener.nullDeviceId();
//            l.onResponse(new Response(null));
//            return;
//        }
//        HashMap hashMap = Utils.arrayToParams(params);
//        hashMap.put("res", resource);
//        hashMap.put("func", function);
//        if (object != null)
//            hashMap.putAll(object.getMap(false));
//        switch (method) {
//            case GET:
//                Http.get(Utils.getApiUrl(), Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
//                break;
//            case POST:
//                Http.post(Utils.getApiUrl(), Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
//                break;
//            case UPDATE:
//                Http.update(Utils.getApiUrl(), Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
//                break;
//            case PUT:
//                Http.put(Utils.getApiUrl(), Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
//                break;
//            case DELETE:
//                Http.delete(Utils.getApiUrl(), Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
//                break;
//        }
//
//    }
//
//    public static void get(String resource, String function, BaseObject object, onResponseListener l, String... params) {
//        sendRequest(METHOD.GET, resource, function, null, l, params);
//    }
//
//    public static void post(String resource, String function, BaseObject object, onResponseListener l, String... params) {
//        sendRequest(METHOD.POST, resource, function, object, l, params);
//
//    }
//
//    public static void post(String resource, String function, onResponseListener l, String... params) {
//
//        post(resource, function, null, l, params);
//
//    }
//
//    public static void get(String resource, String function, onResponseListener l, String... params) {
//
//        get(resource, function, null, l, params);
//
//    }
//
//    public interface onResponseListener {
//        void onResponse(Response response);
//    }
//
//    //   static onResponseListener listener=new onResponseListener();
//    static class onResponseListenerClass implements Http.onResponseListener {
//        onResponseListenerClass(onResponseListener l) {
//            listener = l;
//        }
//        onResponseListener listener;
//
//        @Override
//        public void onResponse(okhttp3.Response response) {
//            Response r=new Response(response);
//            Utils.setToken(r.getHeader("token", ""));
//
//            if (!r.succeed()){
//
//                String e = r.getString();
//                if (STATE.MUST_LOGIN.equals(e)) {
//                    Utils.removeProfileSecret();
//                    onExceptionListener.mustLogin();
//                    return;
//                }
//                if (STATE.MISSING_ARGS.equals(e)) {
////                    Utils.removeProfileSecret();
//                    onExceptionListener.missingArgs();
//                    return;
//                }
//            }
//            listener.onResponse(r);
//        }
//    }
//
//  public   interface onExceptionListener {
//        void nullDeviceId();
//        void noNetwork();
//        void mustLogin();
//        void missingArgs();
//
//    }
//    static onExceptionListener onExceptionListener=null;
//
//    public static void addOnExceptionListener(onExceptionListener l) {
//        onExceptionListener = l;
//    }
}




//    static Http.onResponseListener listener=new Http.onResponseListener() {
//        @Override
//        public void onResponse(Response response) {
//
//        }
//    };

