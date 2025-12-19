package ir.microsign.net;

import android.os.AsyncTask;
import android.util.Log;

import ir.microsign.utility.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadSilentFile {

    InputStream input = null;
    OutputStream output = null;
    String saveFilePath = null;
    int mBufferSize;
    DownloadCompleted mDownloadListener =null;
//    DownloadCompleted mDownloadListener = null;

    public void GetFile(int bufferSize, String savePath, String url, DownloadCompleted downloadCompletedListener) {
        mBufferSize = bufferSize;
        mDownloadListener = downloadCompletedListener;
        new DownloadFileFromURL().execute(url, savePath);
    }

    public void GetFile(String savePath, String url, DownloadCompleted downloadCompletedListener) {
        GetFile(10 * 1024, savePath, url, downloadCompletedListener);
    }

    public int GetFileDirect(int bufferSize, String savePath, String url) {
        mBufferSize = bufferSize;
        mDownloadListener = null;
        try {
            return new DownloadFileFromURL().execute(url, savePath).get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Integer downloadFile(int bufferSize, String savePath, String fileURL) {
        URL url = null;
        if (Text.isNullOrEmpty(fileURL) || fileURL.startsWith("null/")) return -1;
        try {
            url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            boolean isError = httpConn.getResponseCode() >= 400;
//			if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
//				String contentType = httpConn.getContentType();
//				int contentLength = httpConn.getContentLength();
            httpConn.getHeaderFields();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index >= 0) {
                    fileName = disposition.substring(index + "filename=".length());
//						mP2Title=fileName;
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
            ir.microsign.utility.File.AppendDir(savePath);
            input = isError ? httpConn.getErrorStream() : httpConn.getInputStream();
            saveFilePath = ir.microsign.utility.File.pathIsDirectory(savePath) ? savePath + File.separator + fileName : savePath;


            output = new FileOutputStream(saveFilePath);

            int count;
            byte[] buffer = new byte[bufferSize];
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
                if (checkCancel()) return -1;
            }

            output.close();
            input.close();
            System.out.println("File downloaded");

//			} else {
//				Log.e("DownloadSilentFile", httpConn.getResponseMessage()+"\n"+fileURL);
//				cancel();return -1;
//			}
            httpConn.disconnect();
        } catch (Exception e) {
            Log.e("DownloadSilentFile", e.getMessage());
            cancel();
            return -1;
        }
        return 1;
    }

    void cancel() {
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
    }

    Boolean checkCancel() {
        return false;
    }


    public class DownloadFileFromURL extends AsyncTask<String, Integer, Integer> {
        void SetDownloadCompleteListener(Boolean succeed) {
            if (mDownloadListener != null)
                mDownloadListener.OnDownloadCompleted(succeed, saveFilePath);
        }

        @Override
        protected Integer doInBackground(String... args) {
//			publishProgress(1, mUrl.size(), 0, 0);
            try {
                String _url = args[0], _savePath = args[1];
                return downloadFile(mBufferSize, _savePath, _url);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                DownloadSilentFile.this.cancel();
                return -1;
            }
        }


        @Override
        protected void onPostExecute(Integer result) {
            SetDownloadCompleteListener(result > 0);
        }

    }

    public interface DownloadCompleted {
//        void downloading(int downloaded);
        public void OnDownloadCompleted(boolean succeed, String path);
    }




}

