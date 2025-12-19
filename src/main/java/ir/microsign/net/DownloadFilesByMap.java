package ir.microsign.net;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import ir.microsign.utility.Text;
import ir.microsign.view.DoubleProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadFilesByMap {

    int mBufferSize, mPeriodProgressUpdate;
//    Set<Map.Entry<String, String>> mUrls=null;
    List<Map.Entry<String, String>> mUrls=null;
    Context mContext = null;
    DownloadCompletedListener mDownloadCompletedListener = null;
    //	 List<String> mUrls = null;
//    String mSavePath = null;
    Boolean mCanceled = false;
     String mTitle = "", mP1Title = "", mP2Title = "", mFileRecievedFormat = "%d/%d", mByteRecievedFormat = "%d kb/%d kb";
    DoubleProgressDialog mDoubleProgressDialog;
    int mTotalFiles = 0;

    public void GetFiles(Activity activity, int bufferSize, int periodProgressUpdate, String title, String P1Title, Map<String,String> urls, DownloadCompletedListener l) {
//        ir.microsign.activity.setActivity(activity);
        mBufferSize = bufferSize;
        mPeriodProgressUpdate = periodProgressUpdate;
        mP1Title = P1Title;
        mContext = activity;
        mTitle = title;
//        mSavePath = savePath;
        mUrls=new ArrayList<>();
        for (Map.Entry<String, String> stringStringEntry : urls.entrySet()) {
            mUrls.add(stringStringEntry);
        }
//		mUrls = urls.entrySet().toArray();
        mCanceled = false;
        mDownloadCompletedListener = l;
//        urls.entrySet()

        new DownloadFileFromURLMap().execute();
    }

    public void GetFiles(Activity activity, String title, String P1Title, Map<String,String> urls, DownloadCompletedListener l) {
        GetFiles(activity, 10 * 1024, 20, title, P1Title, urls, l);
    }

    public interface DownloadCompletedListener {
        public void OnFileDownloaded(boolean succeed, String path);

        public void OnFinish(boolean allSucceed, String root);
    }

    public class DownloadFileFromURLMap extends AsyncTask<Integer, Integer, Integer> implements DialogInterface.OnCancelListener {
        InputStream input = null;
        OutputStream output = null;
        String mCurrentFileName = null;
        boolean allSucceed = true;
        int currentIndex=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDoubleProgressDialog = new DoubleProgressDialog(mContext, true, this);

            publishProgress(0, 0, 0, 0);
        }

        void onFileDownloaded(final Boolean succeed) {

            if (!succeed) allSucceed = false;
//			String path=mSavePath+"/"+mCurrentFileName;
            if (!succeed) cleanFile();
            if (mDownloadCompletedListener != null)
                mDownloadCompletedListener.OnFileDownloaded(succeed, getCurrentFilePath());


//			new Handler().postDelayed(,50);


        }

        String getCurrentFilePath() {
            return mUrls.get(currentIndex).getValue();
//            return net.tarnian.utility.File.pathIsDirectory(mSavePath) ? mSavePath + "/" + mCurrentFileName : mSavePath;

        }

        void cleanFile() {

            ir.microsign.utility.File.Delete(getCurrentFilePath());
        }

        void onFinish() {
            if (mDownloadCompletedListener != null)
                mDownloadCompletedListener.OnFinish(allSucceed, "");
        }

        Boolean checkCancel() {
            if (mCanceled) cancelOperation();
            return mCanceled;
        }

        void cancelOperation() {
            try {
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected Integer doInBackground(Integer... params) {
            mP2Title = "";
//            mTotalFiles = urls.length;
            mTotalFiles=mUrls.size();
            publishProgress(1,mTotalFiles, 0, 0);
            if (mUrls.size()<1)return 1;
//            String f=mUrls.get(0).getValue().toString();
//            String s=new File(f).getParent();

//            net.tarnian.utility.File.AppendDir(s);
            try {
//                for (int i = 0; i < mUrls.size(); i++) {
//                    Map.Entry<String, String> urlSet = mUrls.get(i);
//                    currentIndex = i;
//
//                    if (checkCancel()) return -1;
//                    String url = urlSet.getKey();
//                    int result = 0;
//                    if (Text.isEmpty(url) || url.startsWith("null/")) result = -1;
//                    else result = downloadFile(index, mBufferSize, mPeriodProgressUpdate);
//                    onFileDownloaded(result > 0);
//                }
                for (int index = 0; index <mTotalFiles; index++) {
                    currentIndex=index;

                    if (checkCancel()) return -1;
                    String url = mUrls.get(index).getKey();
                    int result = 0;
                    if(Text.isNullOrEmpty(url) || url.startsWith("null/"))result=-1;
                    else result = downloadFile(index, mBufferSize, mPeriodProgressUpdate);
                    onFileDownloaded(result > 0);
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return -1;
            }

            return 1;
        }

        Integer downloadFile( int urlIndex , int bufferSize, int updatePeriod) {
            String fileURL=mUrls.get(urlIndex).getKey();
            String savePath=mUrls.get(urlIndex).getValue();
            if (Text.isNullOrEmpty(fileURL) || fileURL.startsWith("null/")) return -1;
            URL url = null;
            try {
                url = new URL(fileURL);

                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {
//				String fileName = "";
                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    String contentType = httpConn.getContentType();
                    int contentLength = httpConn.getContentLength();
                    httpConn.getHeaderFields();

                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            mCurrentFileName = disposition.substring(index + "filename=".length());
                            mP2Title = mCurrentFileName;
//						mCurrentFileName =fileName;
                        }
                    } else {
                        // extracts file name from URL
                        mCurrentFileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                                fileURL.length());
                    }

                    input = httpConn.getInputStream();
                    String saveFilePath = savePath;

                    // opens an output stream to save into file
//				FileOutputStream outputStream =
                    ir.microsign.utility.File.AppendDir(new File(saveFilePath).getParent());
                    output = new FileOutputStream(saveFilePath);

                    int count;
                    byte[] buffer = new byte[bufferSize];
                    int total = mUrls.size();
                    int counter = 0;
                    while ((count = input.read(buffer)) != -1) {
                        output.write(buffer, 0, count);
                        if (checkCancel()) return -1;
                        total += count;
                        if (counter % updatePeriod == 0) {
                            publishProgress(urlIndex + 1, mTotalFiles, total, contentLength > 0 ? contentLength : total);
                            Thread.sleep(10);
                        }
                        counter++;

                    }

                    try {
                        output.close();
                        input.close();
                    } catch (Exception ex) {
                        Log.e("Error:", ex.getMessage());
                    }
                    if (contentLength > 0 && total < contentLength) return -1;
                    publishProgress(urlIndex + 1, mTotalFiles, total, contentLength > 0 ? contentLength : total);
                    Thread.sleep(10);
                } else {
                    return -1;
                }
                httpConn.disconnect();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return -1;
            }
            return 1;
        }

        protected void onProgressUpdate(final Integer... progress) {
            mDoubleProgressDialog.show(mTitle, mP1Title, mP2Title, mFileRecievedFormat, mByteRecievedFormat, progress[0], progress[1], progress[2] / 1024, progress[3] / 1024, true);

        }

        @Override
        protected void onPostExecute(Integer result) {
            if (mDoubleProgressDialog != null && mDoubleProgressDialog.isShowing()) mDoubleProgressDialog.dismiss();
            onFinish();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            mCanceled = true;

        }
    }


}

