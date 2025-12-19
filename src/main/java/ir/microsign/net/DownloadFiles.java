package ir.microsign.net;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import ir.microsign.utility.Text;
import ir.microsign.utility.File;
import ir.microsign.view.DoubleProgressDialog;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class DownloadFiles {

    int mBufferSize, mPeriodProgressUpdate;
    Context mContext = null;
    DownloadCompletedListener mDownloadCompletedListener = null;
    //	 List<String> mUrls = null;
    String mSavePath = null;
    Boolean mCanceled = false;
    String mTitle = "", mP1Title = "", mP2Title = "", mFileRecievedFormat = "%d/%d", mByteRecievedFormat = "%d kb/%d kb";
    DoubleProgressDialog mDoubleProgressDialog;
    int mTotalFiles = 0;

    public void GetFiles(Activity activity, int bufferSize, int periodProgressUpdate, String title, String P1Title, String savePath, List<String> urls, DownloadCompletedListener l) {
//        ir.microsign.context.Application.setActivity(activity);
        mBufferSize = bufferSize;
        mPeriodProgressUpdate = periodProgressUpdate;
        mP1Title = P1Title;
        mContext = activity;
        mTitle = title;
        mSavePath = savePath;
//		mUrls = urls;

        mCanceled = false;
        mDownloadCompletedListener = l;
//		String[] strarray = urls.toArray(new String[0]);
        new DownloadFileFromURL().execute(urls.toArray(new String[0]));
    }

    public void GetFiles(Activity activity, String title, String P1Title, String savePath, List<String> urls, DownloadCompletedListener l) {
        GetFiles(activity, 10 * 1024, 20, title, P1Title, savePath, urls, l);
    }

    public interface DownloadCompletedListener {
        public void OnFileDownloaded(boolean succeed, String path);

        public void OnFinish(boolean allSucceed, String root);
    }

    public class DownloadFileFromURL extends AsyncTask<String, Integer, Integer> implements DialogInterface.OnCancelListener {
        InputStream input = null;
        OutputStream output = null;
        String mCurrentFileName = null;
        boolean allSucceed = true;

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
            return File.pathIsDirectory(mSavePath) ? mSavePath + "/" + mCurrentFileName : mSavePath;

        }

        void cleanFile() {

            File.Delete(getCurrentFilePath());
        }

        void onFinish() {
            if (mDownloadCompletedListener != null)
                mDownloadCompletedListener.OnFinish(allSucceed, mSavePath);
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
        protected Integer doInBackground(String... urls) {
            mP2Title = "";
            mTotalFiles = urls.length;
            publishProgress(1, urls.length, 0, 0);
            File.AppendDir(mSavePath);
            try {
                for (int index = 0; index < urls.length; index++) {
                    if (checkCancel()) return -1;
                    String url = urls[index];
                    int result = 0;
                    if (Text.isNullOrEmpty(url) || url.startsWith("null/")) result = -1;
                    result = downloadFile(urls[index], mSavePath, mBufferSize, mPeriodProgressUpdate, index);
                    onFileDownloaded(result > 0);
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return -1;
            }

            return 1;
        }

        Integer downloadFile(String fileURL, String saveDir, int bufferSize, int updatePeriod, int currentIndex) {
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
                    String saveFilePath = File.pathIsDirectory(saveDir) ? saveDir + java.io.File.separator + mCurrentFileName : saveDir;

                    // opens an output stream to save into file
//				FileOutputStream outputStream =
                    output = new FileOutputStream(saveFilePath);

                    int count;
                    byte[] buffer = new byte[bufferSize];
                    int total = 0;
                    int counter = 0;
                    while ((count = input.read(buffer)) != -1) {
                        output.write(buffer, 0, count);
                        if (checkCancel()) return -1;
                        total += count;
                        if (counter % updatePeriod == 0) {
                            publishProgress(currentIndex + 1, mTotalFiles, total, contentLength > 0 ? contentLength : total);
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
                    publishProgress(currentIndex + 1, mTotalFiles, total, contentLength > 0 ? contentLength : total);
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

