package ir.microsign.api;

import android.content.Context;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.App;
import ir.microsign.api.object.Message;
import ir.microsign.api.object.Profile;
import ir.microsign.api.object.Recover;
import ir.microsign.api.object.Response;
import ir.microsign.api.object.Ticket;
import ir.microsign.context.Application;
import ir.microsign.context.System;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.net.FilePart;
import ir.microsign.net.Http;
import ir.microsign.net.Utility;
import ir.microsign.utility.Text;

public class Api {


    public enum RES {
        Profile,
        Ticket,
        App,

    }

    public enum FUNC {
        SignIn,
        SignUp,
        Update,
        Recover,
        newPass,
        Ticket,
        Message,
        Profile,
        Info,

    }

    public enum METHOD {
        GET,
        POST,
        UPDATE,
        DELETE,
        PUT,
        PATCH

    }

    public static class STATE {
        public static final String
                NOT_FOUND = "not_found",
                NOT_EXIST = "not_exist",
                WRONG_PASS = "wrong_pass",
                ALREADY_EXIST = "already_exist",
                MUST_LOGIN = "most_login",
                TIMEOUT = "timeout",
                LIMITED = "limited",
                MISSING_ARGS = "missing_args",
                NO_NETWORK = "no_network";
    }
    static void updateApps(final Context context,final onResultListener l){
            Utility.CheckInternet(context, new ConnectDialog.OnDialogResultListener() {
                @Override
                public void OnDialogResult(boolean ok, boolean isConnect) {
                    if (!isConnect){if (l!=null)l.error(STATE.NO_NETWORK);return;}
//                    WaitingDialog.showWaitingDialog(context,true);
                    Api.get("App", "Apps", new onResponseListener() {
                        @Override
                        public void onResponse(Response response) {
                            if (!response.succeed()){
                                if (l!=null)l.error(response.getString());
                                return;
                            }
                            final List<BaseObject> apps=response.getObjectArray(App.class,"Apps");
                            long time=Calendar.getInstance().getTimeInMillis();
                            for (BaseObject app : apps) {
                                ((App)app).insert_date=time;
                            }
                            DataSourceApi.getSource(context).cleanAndInsertObjects(new App().getTableName(),apps);
                            if (l!=null)l.succeed(apps);

                            }
                    });
                }
            });

    }
    public interface onSignInListener {
        void signed(Profile profile);
        void wrongPass();
        void notExist();
        void error(String e);
    }
    public interface onSignUpListener {
        void registered(Profile profile);
        void error(String e);
        void alreadyExist();
    }
    public interface onResultListener {
        void succeed(Object o);
        void error(String e);
    }
    public interface onUpdateListener {
        void updated(Profile profile);
        void mustLogin();
        void error(String e);
    }  public interface onGetProfileListener {
        void succeed(Profile profile);
        void error(String e);
        void mustLogin();
    }
    public interface onDetachListener {
        void detached();
        void error(String e);

    }
public interface onRecoverListener {
    void verificationSent(Recover recover);
    void limited();
//    void recovered(Profile profile);
//
//    void timeout();
//
//    void invalid();

    void error(String e);
}
public interface onNewPassListener {
//    void verificationSent(Recover recover);

    void recovered(Profile profile);

    void timeout();
    void limited();

    void invalid();

    void error(String e);
}
    public static void getAppInfo(Context context,onResponseListener l){
        sendRequest(METHOD.GET,RES.App,FUNC.Info,null,l);
    }
    public static void postTicket(Ticket ticket,onResponseListener l){
        sendRequest(METHOD.POST,RES.Ticket,FUNC.Ticket,ticket,l);
    }
    public static void postMessage(Message message, onResponseListener l){
        sendRequest(METHOD.POST,RES.Ticket,FUNC.Message,message,l);
    }
    public static void getTickets(onResponseListener l){
        sendRequest(METHOD.GET,RES.Ticket,FUNC.Ticket,null,l);
    }
    public static void getMessages(Message message,onResponseListener l){
        sendRequest(METHOD.GET,RES.Ticket,FUNC.Message,message,l);
    }
    public static void signIn(Profile profile, final onSignInListener l) {
        sendRequest(METHOD.POST, RES.Profile, FUNC.SignIn, profile, new onResponseListener() {
            @Override
            public void onResponse(Response response) {


                if (response.succeed()) {
                    Profile p=response.getObject(Profile.class, FUNC.SignIn.toString());
                    Utils.setProfileSecret(p.password);
                    l.signed(p);

                } else {
                    String e = response.getString();
                    if (STATE.WRONG_PASS.equals(e)) {
                        l.wrongPass();
                    } else if (STATE.NOT_EXIST.equals(e)) {
                        l.notExist();
                    } else l.error(e);

                }
            }
        });
    }

    public static void getProfile(final Context context, final onGetProfileListener l) {
        sendRequest(METHOD.GET, RES.Profile, FUNC.Profile, null, new onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.succeed()) {
                    Profile profile= response.getObject(Profile.class,FUNC.Profile.toString());
                    DataSourceApi.getSource(context);
                    if (l!=null)l.succeed(profile);
                    } else {
                    String e = response.getString();
                    if (STATE.MUST_LOGIN.equals(e)) {
                        Utils.removeProfileSecret();
                        if (l!=null)l.mustLogin();
                    } else {
                        if (l!=null)l.error(e);
                    }
                }
            }
        });
    }
    public static void updateProfile(Profile profile, final onUpdateListener l) {
        sendRequest(METHOD.POST, RES.Profile, FUNC.Update, profile, new onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.succeed()) {

                    l.updated((Profile) response.getObject(Profile.class,FUNC.Update.toString()));
                } else {
                    String e = response.getString();
                    if (STATE.MUST_LOGIN.equals(e)) {
                        Utils.removeProfileSecret();
                        l.mustLogin();
                    } else {
                        l.error(e);
                    }
                }
            }
        });
    }
    public static void updatePermission(String permission,boolean add ,final onGetProfileListener l) {
       if (add) sendRequest(METHOD.PUT, RES.Profile, FUNC.Update, null, new onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.succeed()) {
l.succeed((Profile) response.getObject(Profile.class,FUNC.Update.toString()));
                } else {
                    l.error(response.getString());
                }
            }
        });
       else sendRequest(METHOD.DELETE, RES.Profile, FUNC.Update, null, new onResponseListener() {
           @Override
           public void onResponse(Response response) {
               if (response.succeed()) {
                   l.succeed((Profile) response.getObject(Profile.class,FUNC.Update.toString()));
               } else {
                   l.error(response.getString());
               }
           }
       });
    }
    public static void recoverPassword(Profile profile,final onRecoverListener l){
        sendRequest(METHOD.POST, RES.Profile, FUNC.Recover, profile, new onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.succeed()) {
                    l.verificationSent((Recover) response.getObject(Recover.class,FUNC.Recover.toString()));
                } else {
                    String e=response.getString();
                    if (STATE.TIMEOUT.equals(e)) {
                        l.limited();
                    }
                    else  if (STATE.LIMITED.equals(e)) {
                        l.limited();
                    }
                      else   l.error(e);

                }
            }
        });
    }
    public static void setNewPassword(Recover recover,final onNewPassListener l){
        sendRequest(METHOD.POST, RES.Profile, FUNC.newPass, recover, new onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.succeed()) {
                    l.recovered((Profile) response.getObject(Profile.class,FUNC.newPass.toString()));
                } else {
                    String e=response.getString();
                    if (STATE.TIMEOUT.equals(e)) {
                        l.timeout();
                    }
                    else  if (STATE.LIMITED.equals(e)) {
                        l.limited();
                    }
                    else  if (STATE.WRONG_PASS.equals(e)) {
                        l.invalid();
                    }
                        l.error(e);

                }
            }
        });
    }
    public static void signUp(Profile profile, final onSignUpListener l) {

        sendRequest(METHOD.POST, RES.Profile, FUNC.SignUp, profile, new onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.succeed()) {
                    l.registered((Profile) response.getObject(Profile.class,FUNC.SignUp.toString()));
                } else {
                    String e=response.getString();
                    if (STATE.ALREADY_EXIST.equals(e)) {
                        l.alreadyExist();
                    } else l.error(e);

                }
            }
        });
    }

    public static void sendRequest(METHOD method, RES resource, FUNC function, BaseObject object, onResponseListener l, String... params) {
        sendRequest(method, resource.toString(), function.toString(), object, l, params);
    }
    public static void sendRequest(METHOD method, String resource, String function, BaseObject object, onResponseListener l, String... params) {
        sendRequest(Utils.getApiUrl(),method,resource,function,object,l,params);
    }

    public static void sendRequest(String url,METHOD method, String resource, String function, BaseObject object, onResponseListener l, String... params) {
sendRequest( url,  method,  resource,  function,  object, null,  l, params);
    }
        public static void sendRequest(String url, METHOD method, String resource, String function, BaseObject object, List<FilePart> filePartList, onResponseListener l, String... params) {
        if (!Utility.isConnect(Application.getContext())) {
                l.onResponse(new Response(null));
				fireException(exceptions.noNetwork);

            return;
        }
        if (System.getUniqueId(Application.getContext()) == null) {
			fireException(exceptions.nullDeviceId);
            l.onResponse(new Response(null));
            return;
        }
        HashMap hashMap = Utils.arrayToParams(params);
       if (Text.notEmpty(resource)) hashMap.put("res", resource);
        if (Text.notEmpty(function)) hashMap.put("func", function);
        if (object != null)
            hashMap.putAll(object.getMap(false));
        if (Text.isEmpty(url))url=Utils.getApiUrl();
        switch (method) {
            case GET:
                Http.get(url, Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
                break;
            case POST:
                if (filePartList==null||filePartList.size()<1)
                Http.post(url, Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
                else  Http.postForm(url, Utils.getRequestHeader(), hashMap,filePartList, new onResponseListenerClass(l),Utils.getTimeout());
                break;
            case UPDATE:
                Http.update(url, Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
                break;
            case PUT:
                Http.put(url, Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
                break;
            case DELETE:
                Http.delete(url, Utils.getRequestHeader(), hashMap, new onResponseListenerClass(l),Utils.getTimeout());
                break;

        }

    }

    public static void get(String resource, String function, BaseObject object, onResponseListener l, String... params) {
        sendRequest(METHOD.GET, resource, function, null, l, params);
    }

    public static void post(String resource, String function, BaseObject object, onResponseListener l, String... params) {
        sendRequest(METHOD.POST, resource, function, object, l, params);

    }

    public static void post(String resource, String function, onResponseListener l, String... params) {

        post(resource, function, null, l, params);

    }

    public static void get(String resource, String function, onResponseListener l, String... params) {

        get(resource, function, null, l, params);

    }

    public interface onResponseListener {
        void onResponse(Response response);
    }

    //   static onResponseListener listener=new onResponseListener();
    static class onResponseListenerClass implements Http.onResponseListener {
        onResponseListenerClass(onResponseListener l) {
            listener = l;
        }
        onResponseListener listener;

        @Override
        public void onResponse(okhttp3.Response response) {

            Response r=new Response(response);
            if (response==null) {
				fireException(exceptions.timeout);
                listener.onResponse(r);
                return;
            }
            Utils.setToken(r.getHeader("token", ""));
            Utils.setAuthorizationToken(r.getHeader("session", ""));

            if (!r.succeed()){

                String e = r.getString();
                if (STATE.MUST_LOGIN.equals(e)) {
                    Utils.removeProfileSecret();
					fireException(exceptions.mustLogin);
                    return;
                }
                if (STATE.MISSING_ARGS.equals(e)) {
//                    Utils.removeProfileSecret();
					fireException(exceptions.missingArgs);
                    return;
                }

            }
            listener.onResponse(r);
        }
    }

  public   interface onExceptionListener {
        void nullDeviceId();
        void noNetwork();
        void mustLogin();
        void missingArgs();
        void timeout();

    }
    enum  exceptions {
		nullDeviceId,noNetwork,mustLogin,missingArgs,timeout,
	}
//    static onExceptionListener onExceptionListener=null;

   private static Map<String,onExceptionListener>map=new HashMap<>();
    public static void addOnExceptionListener(String key , onExceptionListener l) {
        map.put(key,l);
    }
    public static void removeOnExceptionListener(String key) {
        map.remove(key);
    }
    private static void fireException(exceptions e) {
		 try {
			 Method method = onExceptionListener.class.getMethod(e.name());
			 for (Map.Entry<String, onExceptionListener> entry : map.entrySet()) {
				 method.invoke(entry.getValue());
			 }
		 } catch (Exception e1) {
			 e1.printStackTrace();
		 }
	 }
}




//    static Http.onResponseListener listener=new Http.onResponseListener() {
//        @Override
//        public void onResponse(Response response) {
//
//        }
//    };

