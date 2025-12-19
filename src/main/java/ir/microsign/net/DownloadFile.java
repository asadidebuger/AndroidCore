package ir.microsign.net;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ir.microsign.utility.Text;
import okhttp3.Response;

public class DownloadFile {

    InputStream input = null;
    OutputStream output = null;
    String saveFilePath = null;
    int timeout=15;
//    int mBufferSize;
//    OnDownloadListener mDownloadListener =null;

//    public void GetFile(int bufferSize, String savePath, String url, OnDownloadListener downloadCompletedListener) {
//        mBufferSize = bufferSize;
//        mDownloadListener = downloadCompletedListener;
//        new DownloadFileFromURL().execute(url, savePath);
//    }
//
//    public void GetFile(String savePath, String url, OnDownloadListener downloadCompletedListener) {
//        GetFile(10 * 1024, savePath, url, downloadCompletedListener);
//    }
//
//    public int GetFileDirect(int bufferSize, String savePath, String url) {
//        mBufferSize = bufferSize;
//        mDownloadListener = null;
//        try {
//            return new DownloadFileFromURL().execute(url, savePath).get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }
    public Integer downloadFile(final String url, final String savePath, final int bufferSize, final OnDownloadListener l) {

        Http.get(url, null, null, new Http.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response==null||!response.isSuccessful()) {
                    l.OnDownloadCompleted(false, saveFilePath);
                    return;
                }
                boolean pathIsDir= ir.microsign.utility.File.pathIsDirectory(savePath);
            String len = response.headers().get("Content-Length");
            if (len==null)len= response.headers().get("bytes");

            int contentLen=Text.isNullOrEmpty(len)?0:Integer.parseInt(len);
            String fileName = response.headers().get("filename");
//            String folder=pathIsDir?savePath:new File(savePath).getParent();

            if (Text.isNullOrEmpty(fileName)) {
                if (pathIsDir) {
                    int i = url.lastIndexOf("/") + 1;
                    if (i > -1)
                        fileName = url.substring(url.lastIndexOf("/") + 1);
                    else fileName = new File(savePath).getName();
                } else {
                    fileName = new File(savePath).getName();
                }
            }

                saveFilePath =pathIsDir ? savePath + File.separator + fileName : savePath;
            if (new File(saveFilePath).exists()){
                l.OnDownloadCompleted(true, saveFilePath);
                return;
            }
//            String len = response.headers().get("bytes");
//				String contentType = httpConn.getContentType();
//				int contentLength = httpConn.getContentLength();
          //  httpConn.getHeaderFields();

//            if (disposition != null) {
//                // extracts file name from header field
//                int index = disposition.indexOf("filename=");
//                if (index >= 0) {
//                    fileName = disposition.substring(index + "filename=".length());
////						mP2Title=fileName;
//                }
//            } else {
//                // extracts file name from URL
//                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
//                        fileURL.length());
//            }
            if (!ir.microsign.utility.File.AppendDir(saveFilePath))
            {
                Log.e("downloadFile","cant create path for:"+saveFilePath);
                l.OnDownloadCompleted(false, saveFilePath);

                return;
            }


                input = response.body().byteStream();
//            saveFilePath = ir.microsign.File.pathIsDirectory(savePath) ? savePath + File.separator + fileName : savePath;


                try {
                    output = new FileOutputStream(saveFilePath);

                    int totalRead = 0;
                    int count;
                    boolean succeed = true;
                    byte[] buffer = new byte[bufferSize];
                    while ((count = input.read(buffer)) != -1) {
                        totalRead += count;
                        output.write(buffer, 0, count);
                        if (!l.downloading(totalRead, contentLen)) {

                            succeed = false;
                            cancel(saveFilePath);
                            break;
                        }

                    }

                    output.close();
                    input.close();
                    System.out.println("File downloaded");

//			} else {
//				Log.e("DownloadSilentFile", httpConn.getResponseMessage()+"\n"+fileURL);
//				cancel();return -1;
//			}
//            httpConn.disconnect();
                    l.OnDownloadCompleted(succeed, saveFilePath);
                } catch (Exception e) {
                    l.OnDownloadCompleted(false, saveFilePath);
                    Log.e("DownloadFile", e.getMessage());
                    cancel(saveFilePath);
//                    return -1;
                }
            }
        },timeout);
        return 1;
    };
//    public Integer downloadFile(OnDownloadListener l,int bufferSize, String savePath, String fileURL){
//        URL url = null;
//        if (Text.isNullOrEmpty(fileURL) || fileURL.startsWith("null/")) return -1;
//        try {
//            url = new URL(fileURL);
//            HttpURLConnection httpConn;
//            if (url.getProtocol().toLowerCase().equals("https")){
//                httpConn=(HttpsURLConnection) url.openConnection();
//            }
//            else httpConn = (HttpURLConnection) url.openConnection();
//            boolean isError = httpConn.getResponseCode() >= 400;
////			if (responseCode == HttpURLConnection.HTTP_OK) {
//            String fileName = "";
//            String disposition = httpConn.getHeaderField("Content-Disposition");
////				String contentType = httpConn.getContentType();
//				int contentLength = httpConn.getContentLength();
//          //  httpConn.getHeaderFields();
//
//            if (disposition != null) {
//                // extracts file name from header field
//                int index = disposition.indexOf("filename=");
//                if (index >= 0) {
//                    fileName = disposition.substring(index + "filename=".length());
////						mP2Title=fileName;
//                }
//            } else {
//                // extracts file name from URL
//                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
//                        fileURL.length());
//            }
//            ir.microsign.File.AppendDir(savePath);
//            input = isError ? httpConn.getErrorStream() : httpConn.getInputStream();
//            saveFilePath = ir.microsign.File.pathIsDirectory(savePath) ? savePath + File.separator + fileName : savePath;
//
//
//
//            output = new FileOutputStream(saveFilePath);
//
//            int totalRead=0;
//            int count;
//			boolean succeed=true;
//            byte[] buffer = new byte[bufferSize];
//            while ((count = input.read(buffer)) != -1) {
//                totalRead+=count;
//                output.write(buffer, 0, count);
//				if(!l.downloading(totalRead,contentLength)){succeed=false;break;}
//
//            }
//
//            output.close();
//            input.close();
//            System.out.println("File downloaded");
//
////			} else {
////				Log.e("DownloadSilentFile", httpConn.getResponseMessage()+"\n"+fileURL);
////				cancel();return -1;
////			}
//            httpConn.disconnect();
//			l.OnDownloadCompleted((contentLength<0&&succeed)||totalRead==contentLength,saveFilePath);
//        } catch (Exception e) {
//			l.OnDownloadCompleted(false,saveFilePath);
//            Log.e("DownloadFile", e.getMessage());
//            cancel();
//            return -1;
//        }
//        return 1;
//    }

    void cancel(String savedFile) {
        try {
            if (output != null)
                output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (output != null)
                output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (input != null)
                input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new File(savedFile).delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//void v(){
//    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//
//    DefaultHttpClient client = new DefaultHttpClient();
//
//    SchemeRegistry registry = new SchemeRegistry();
//    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//    registry.register(new Scheme("https", socketFactory, 443));
//    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//    DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//
//// Set verifier
//    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//
//// Example send http request
//    final String url = "https://encrypted.google.com/";
//    HttpPost httpPost = new HttpPost(url);
//    HttpResponse response = httpClient.execute(httpPost);
//}

//    public class DownloadFileFromURL extends AsyncTask<String, Integer, Integer> {
//		boolean downloadContinue=true;
////        void SetDownloadCompleteListener(Boolean succeed) {
////            if (mDownloadListener != null)
////                mDownloadListener.OnDownloadCompleted(succeed, saveFilePath);
////        }
//
//        @Override
//        protected Integer doInBackground(String... args) {
////			publishProgress(1, mUrl.size(), 0, 0);
//            OnDownloadListener l=new OnDownloadListener() {
//                @Override
//                public boolean downloading(int downloaded, int total) {
//                    DownloadFileFromURL.this.onProgressUpdate(downloaded ,total);
//					return downloadContinue;
//                }
//
//                @Override
//                public void OnDownloadCompleted(boolean succeed, String path) {
//mDownloadListener.OnDownloadCompleted(succeed,path);
//                }
//            };
//            try {
//                String _url = args[0], _savePath = args[1];
//                return 1;//downloadFile(l,mBufferSize, _savePath, _url);
//            } catch (Exception e) {
//                Log.e("Error: ", e.getMessage());
//                DownloadFile.this.cancel(args[1]);
//                return -1;
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//			downloadContinue=  mDownloadListener.downloading(values[0],values[1]);
//        }
//
////        @Override
////        protected void onPostExecute(Integer result) {
////            SetDownloadCompleteListener(result > 0);
////        }
//
//    }

    public interface OnDownloadListener{
        boolean downloading(int downloaded, int total);
         void OnDownloadCompleted(boolean succeed, String path);
    }




}

