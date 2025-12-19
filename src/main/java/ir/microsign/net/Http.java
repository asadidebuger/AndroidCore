package ir.microsign.net;

//import org.apache.http.NameValuePair;

import android.util.Log;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http{

private static  OkHttpClient client; //= new OkHttpClient().newBuilder()
//        .connectTimeout(10000, TimeUnit.SECONDS)
//        .readTimeout(30000, TimeUnit.SECONDS)
//        .build();
static String TAG="http";

//    public static OkHttpClient getClient() {
//        return getClient(10000);
//    }

    static    OkHttpClient getClient(int timeout){
        if (client!=null)return client;
    OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder().connectTimeout(timeout, TimeUnit.SECONDS);

    boolean allowUntrusted = true;

    if (  allowUntrusted) {
        Log.w(TAG,"**** Allow untrusted SSL connection ****");
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] cArrr = new X509Certificate[0];
                return cArrr;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        }};

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        clientBuilder.sslSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Log.d(TAG, "Trust Host :" + hostname);
                return true;
            }
        };
        clientBuilder.hostnameVerifier( hostnameVerifier);
    }
client=clientBuilder.build();
    return client;
//    final Call call = clientBuilder.build().newCall(request);
}


        public static Response sendRequest(String method,String url, Map<String,String> header, Map<String,String> query,RequestBody body, onResponseListener l,int timeout){
//        String m=method.toString();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

        Request.Builder builder=new Request.Builder();
         builder.method(method,body);
        if (query!=null)
            for(Map.Entry<String, String> param : query.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(),param.getValue());
            }
        builder.url(httpBuilder.build());
        if (header!=null){
            for (Map.Entry<String, String> head : header.entrySet()) {
                builder.addHeader(head.getKey(),head.getValue());
            }
        }
        Request request =builder.build();
        if (l!=null) {
            exec(request,l,timeout);
            return null;
        }
        try {
            return getClient(timeout).newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Response postForm(String url, Map<String,String> header, Map<String,String> params,List<FilePart> filePartList,onResponseListener l,int timeout) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

        Request.Builder builder=new Request.Builder();
        builder.url(httpBuilder.build());
        if (header!=null){
            for (Map.Entry<String, String> head : header.entrySet()) {
                builder.addHeader(head.getKey(),head.getValue());
            }
        }



//        RequestBody requestBody
        MultipartBody.Builder multiBuilder= new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(filePartList!=null)
        for(FilePart filePart:filePartList) {
            multiBuilder.addFormDataPart(filePart.getName(), filePart.getFileName(), filePart.getFileRequestBody());
        }
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                multiBuilder.addFormDataPart(param.getKey(),param.getValue());
            }
        }


        Request request = //new Request.Builder().url(url)
        builder.post(multiBuilder.build()).build();


        if (l!=null) {
            exec(request,l,timeout);
            return null;
        }
        try {
            return getClient(timeout).newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Response get(String url, Map<String,String> header, Map<String,String> params,onResponseListener l,int timeout) {
        return sendRequest("GET",url,header,params,null,l,timeout);
    }
        public static Response post(String url, Map<String,String> header, Map<String,String> params,onResponseListener l,int timeout) {
            return sendRequest("POST", url, header, null, getBody(params), l, timeout);

        }
        public static Response update(String url, Map<String,String> header, Map<String,String> params,onResponseListener l,int timeout) {
            return sendRequest("UPDATE", url, header, null, getBody(params), l, timeout);

        }
        public static Response delete(String url, Map<String,String> header, Map<String,String> params,onResponseListener l,int timeout) {
            return sendRequest("DELETE", url, header, params, null, l, timeout);

        }
        public static Response put(String url, Map<String,String> header, Map<String,String> params,onResponseListener l,int timeout) {
        return sendRequest("PUT", url, header, null, getBody(params), l, timeout);

    }
    public static Response patch(String url, Map<String,String> header, Map<String,String> params,onResponseListener l,int timeout) {
        return sendRequest("PATCH", url, header, null, getBody(params), l, timeout);

    }
    public static RequestBody getBody(Map<String,String> params){
        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                formBody.add(param.getKey(),param.getValue());
            }
        }
        return formBody.build();
    }
   public static Response get(String url, Map<String,String> header, Map<String,String> params,int timeout){
        return get(url,header,params,null,timeout);
    }
    public static void exec(Request request,onResponseListener l,int timeout){
       Thread thread=new Thread(new httpRunnable(request, l,timeout));
       thread.start();
    }

    //        public  void main(String[] args) throws IOException {
    //            GetExample example = new GetExample();
    //            String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
    //            System.out.println(response);
    //        }
    //         public  final MediaType JSON
    //                 = MediaType.parse("application/json; charset=utf-8");
    //
    //         OkHttpClient client = new OkHttpClient();
    //
    //         String post(String url, String json) throws IOException {
    //             RequestBody body = RequestBody.create(JSON, json);
    //             Request request = new Request.Builder()
    //                     .url(url)
    //                     .post(body)
    //                     .build();
    //             try (Response response = client.newCall(request).execute()) {
    //                 return response.body().string();
    //             }
    //         }
    //
    //         String bowlingJson(String player1, String player2) {
    //             return "{'winCondition':'HIGH_SCORE',"
    //                     + "'name':'Bowling',"
    //                     + "'round':4,"
    //                     + "'lastSaved':1367702411696,"
    //                     + "'dateStarted':1367702378785,"
    //                     + "'players':["
    //                     + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
    //                     + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
    //                     + "]}";
    //         }
    //
    //         public  void main2(String[] args) throws IOException {
    //             PostExample example = new PostExample();
    //             String json = example.bowlingJson("Jesse", "Jake");
    //             String response = example.post("http://www.roundsapp.com/post", json);
    //             System.out.println(response);
    //         }
    //    }
    //
    //    void sendFile(){
    //        MultipartBody m = new MultipartBody.Builder()
    //                .setType(MultipartBody.FORM)
    //                .addPart(new MultipartBody.Builder()
    //                        .body("value")
    //                        .contentDisposition("form-data; name=\"non_file_field\"")
    //                        .build())
    //                .addPart(new Part.Builder()
    //                        .contentType("text/csv")
    //                        .body(aFile)
    //                        .contentDisposition("form-data; name=\"file_field\"; filename=\"file1\"")
    //                        .build())
    //                .build();
    //        OkHttpClient client = new OkHttpClient();
    //        OutputStream out = null;
    //        try {
    //            URL url = new URL("http://www.example.com");
    //            HttpURLConnection connection = client.open(url);
    //            for (Map.Entry<String, String> entry : multipart.getHeaders().entrySet()) {
    //                connection.addRequestProperty(entry.getKey(), entry.getValue());
    //            }
    //            connection.setRequestMethod("POST");
    //            // Write the request.
    //            out = connection.getOutputStream();
    //            multipart.writeBodyTo(out);
    //            out.close();
    //
    //            // Read the response.
    //            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
    //                throw new IOException("Unexpected HTTP response: "
    //                        + connection.getResponseCode() + " " + connection.getResponseMessage());
    //            }
    //        } finally {
    //            // Clean up.
    //            try {
    //                if (out != null) out.close();
    //            } catch (Exception e) {
    //            }
    //        }
    //    }
        public interface onResponseListener {
           void onResponse(Response response);

    }

    static class httpRunnable implements Runnable{
        Request request=null;
        onResponseListener onResponseListener;
        int timeout;
httpRunnable(Request request,onResponseListener l,int timeout){
    this.request=request;
    onResponseListener=l;
    this.timeout=timeout;

}
        @Override
        public void run() {
            synchronized (getClient(timeout)){
                Response response=null;
                try {
                     response= getClient(timeout).newCall(request).execute();
                } catch (IOException e) {
                   onResponseListener.onResponse(null);
                    e.printStackTrace(); return;
                }
                onResponseListener.onResponse(response);
            }
        }

    }

}
