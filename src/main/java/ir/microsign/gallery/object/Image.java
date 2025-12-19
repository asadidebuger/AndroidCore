package ir.microsign.gallery.object;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ir.microsign.utility.File;
import ir.microsign.utility.Graphics;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.context.Application;
import ir.microsign.dialog.ConnectDialog;
import ir.microsign.R;
import ir.microsign.gallery.network.utility;
import ir.microsign.gallery.view.LargeImageView;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.microsign.net.DownloadFiles;
import ir.microsign.net.DownloadSilentFile;
import ir.microsign.net.Utility;

/**
 * Created by Mohammad on 6/15/14.
 */
public class Image extends BaseObject {
    public static final String THUMB = "thumb", NORMAL = "img", ORIGINAL = "orig";
    static String mPackage = null;
    public Integer autoid, id, catid, asset_id, downloads, approved, checked_out, ordering, published, imgvotes, access, hits, useruploaded, hidden, owner, imgvotesum;
    public String alias, params, imgtext, metakey, imgtitle, imgthumbname, imgfilename, metadesc, imageurl, imgdate, imgauthor, mType = "n";
    public String mImgTag = null;
    public boolean mIsJoomGallery;
    int limit = 499;
    View mLargeView = null;
    public Image() {

    }

    public Image(int id) {
        this.id = id;
    }

    public Image(String src) {
        decodeUrl(src, NORMAL);
    }

    public Image(String src, String type) {
        decodeUrl(src, type);
    }

    public static String getPackage() {
        if (mPackage != null) return mPackage;
        mPackage = "gallery";
        return mPackage;
//		return "";
    }

    public static String getPathFromTag(String tag) {
        if (tag.startsWith("/storage/sdcard"))
        {
            return tag;
        }
        String path = getSrcContent(tag);
        String path0 = path.trim().toLowerCase();

        if (path0.startsWith("http://") || path0.startsWith("https://"))
            path = path.substring(path.indexOf("//") + 2);
        return File.GetRoot(Application.getContext()) + "/images/" + path;
    }

    public static String getSrcContent(String tag) {
        String src = Text.tryGetMatch(tag, "src[ =]*\".*\"", 0);
        return Text.tryGetMatch(src, "[^\"]*", 2);
    }

    public static void setImageViewSize(ImageView imageView, Bitmap bitmap, int breakId) {
        if (bitmap == null) bitmap = BitmapFactory.decodeResource(imageView.getResources(), breakId);
        if (bitmap == null) return;
        int w0 = imageView.getMeasuredWidth();
        double wd = bitmap.getWidth();
        if (w0 > 0) wd = Math.min(wd, w0);
        double rate = wd / bitmap.getWidth();
        int h = (int) (bitmap.getHeight() * rate);
        int w = (int) wd;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params == null)
            params = new ViewGroup.LayoutParams(imageView.getMeasuredWidth(), -1);
        else {
            params.height = h;
            params.width = -1;// w;
        }
        imageView.setLayoutParams(params);
    }

    public void decodeUrl(String src, String type) {
        mImgTag = src;
        if (src.contains("com_joomgallery")) {
            mIsJoomGallery = true;
            if (Text.isNullOrEmpty(src)) return;
            try {
                id = Integer.parseInt(Text.getMatches(src, "id=\\d+").get(0).split("\\=")[1]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                mType = Text.getMatches(src, "type=[^ \"]*").get(0).split("\\=")[1];
            } catch (Exception ex) {
                Log.e("Image", ex.getMessage());
            }
            if (mType == null && type != null) mType = type;
        }

        if (id == null) mIsJoomGallery = false;
    }

    public Image fromUrl(String url) {
        if (url.startsWith("/storage/sdcard"))mImgTag=url;
        else
        if (url.contains("joomplu:")) {
            try {
                id = Integer.parseInt(Text.getMatches(url, "joomplu:\\d+").get(0).split("\\:")[1]);
                mType = Text.tryGetMatch(url, "type=[^ \"]*", 0).split("\\=")[1];

            } catch (Exception ex) {
                Log.e("Image.fromUrl()", ex.getMessage());
            }
        }
        if (id != null) mIsJoomGallery = true;
        return this;
    }

    @Override
    public List<String> getExceptionFields() {
        List<String> list=super.getExceptionFields();
        list.addAll(Arrays.asList("THUMB", "NORMAL", "ORIGINAL", "mLargeView", "mPackage", "limit", "mType", "mImgTag", "mIsJoomGallery"));
        return list;
    }

    String getSubFolder() {
        String sub = "";
        int id = this.id;
        int limPow = limit * limit;
        int level = id / limPow;
        int root = (id % limPow) / limit;
        sub = String.valueOf(root) + "/";
        for (int i = 0; i < level; i++)
            sub += "_/";

        return sub;
    }

    public String getNormalPath() {
        return getNormalPathRoot() + getNormalName();
    }

    public String getNormalPathRoot() {
        return File.GetRoot(Application.getContext()) + "/images" + "/img/" + getSubFolder();
    }

    public String getOriginalPath() {
        return getOriginalPathRoot() + getOriginalName();
    }

    public String getOriginalPathRoot() {
        return File.GetRoot(Application.getContext()) + "/images" + "/orig/" + getSubFolder();
    }

    public String getThumbPath() {
        return getThumbPathRoot() + getThumbName();
    }

    public String getThumbPathRoot() {
        return File.GetRoot(Application.getContext()) + "/images" + "/thumb/" + getSubFolder();
    }

    public boolean originalExist() {
        return File.Exist(getOriginalPath());
    }

    public boolean normalExist() {
        return File.Exist(getNormalPath());
    }

    public boolean Exist() {
        return File.Exist(getPath(mType));
    }

    public boolean thumbExist() {
        return File.Exist(getThumbPath());
    }

    public void downloadThumb() {

    }

    public String getThumbName() {
        return getPackage() + "thumb" + id + ".jpg";
    }

    public String getNormalName() {
        return getPackage() + "normal" + id + ".jpg";
    }

    public String getOriginalName() {
        return getPackage() + "orig" + id + ".jpg";
    }

    public String getImageHtmlTag(String type) {
        String src = "src=\"file:///" + getPath(type) + "\"";
        if (mImgTag == null) return "<img " + src + "/>";
        return mImgTag.replaceAll("src[ ]*=\"[^\"]*\"", src);
    }

    public String getImageHtmlTag() {
        return getImageHtmlTag(mType);
    }

    public String getPath(String type) {
        if (!mIsJoomGallery) return getPathFromTag(mImgTag);
        if (type.equals(ORIGINAL))
            return getOriginalPath();
        if (type.equals(NORMAL))
            return getNormalPath();
//		 if (type.equals(THUMB))
        return getThumbPath();

    }

    public String getPath() {
        return getPath(mType);
    }

    public String getExistPath() {
        String path = getPath(mType);
        if (File.Exist(path)) return path;
        path = getPath(NORMAL.endsWith(mType) ? ORIGINAL : NORMAL);
        if (File.Exist(path)) return path;
        return getPath(THUMB);
    }
public String getUrl(){
   return   mIsJoomGallery ? utility.getImageFileUrl(id, mType) : utility.getImageFileUrl(getSrcContent(mImgTag));
}
    public void downloadOriginalVisual(final Activity activity, final String savePath, final DownloadFiles.DownloadCompletedListener l) {

        if (File.Exist(savePath + "/" + getOriginalName())) {
            l.OnFileDownloaded(true, savePath + "/" + getOriginalName());
            return;
        }
        String _savePath = "";
        if (Text.isNullOrEmpty(_savePath)) _savePath = getPath(ORIGINAL);
        final String tempPath = _savePath;
        Utility.CheckInternet(activity, new ConnectDialog.OnDialogResultListener() {
            @Override
            public void OnDialogResult(boolean ok, boolean isConnect) {
                if (!isConnect) return;
                String url = utility.getImageFileUrl(id, ORIGINAL);
                List<String> list = new ArrayList<String>();
                list.add(url);
                new DownloadFiles().GetFiles(activity, activity.getString(R.string.gallery_get_original_title), activity.getString(R.string.gallery_download_p1_title), tempPath, list, new DownloadFiles.DownloadCompletedListener() {
                    @Override
                    public void OnFileDownloaded(boolean succeed, String path) {
                        String path0 = savePath + "/" + getOriginalName();
                        if (succeed) {
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            Bitmap watermarked = applyWatermark(activity, bitmap);
                            boolean result = Graphics.saveBitmapToFile(watermarked, path0, 80);
                            if (result) File.Delete(path);
                            try {
                                bitmap.recycle();
                                watermarked.recycle();
                            } catch (Exception ex) {
                            }
                            l.OnFileDownloaded(result, path0);
                        } else l.OnFileDownloaded(false, path0);
                    }

                    @Override
                    public void OnFinish(boolean allSucceed, String root) {

                    }
                });
            }
        });


    }

    //	void applyWatermark(Context context,String path){
//
//	}
    public Bitmap applyWatermark(Context context, Bitmap bmp1) {
        Bitmap watermark = Graphics.resizeBitmap(context.getResources(), R.drawable.watermark, bmp1.getWidth(), -1);
        return Graphics.applyWatermark(bmp1, watermark);
    }

    public int downloadDirect(String type) {
        if (mImgTag!=null&&mImgTag.startsWith("/storage/sdcard"))return 1;
        if (id == null) return -1;
        String savePath = getPath(type);
        if (File.Exist(savePath)) {
            return 1;
        }
        String url = utility.getImageFileUrl(id, type);
        if (Text.isNullOrEmpty(url) || url.startsWith("null/")) return -1;
        if (Utility.isConnect(Application.getContext())) return new DownloadSilentFile().downloadFile(1024 * 10, savePath, url);
        return -1;

    }

    public void download(String type, final DownloadSilentFile.DownloadCompleted l) {

        String savePath = getPath(type);
        if (File.Exist(savePath)) {
            return;
        }
        String url =getUrl();
        if (Utility.isConnect(Application.getContext())) new DownloadSilentFile().GetFile(1024 * 10, savePath, url, l);
//		return -1;

    }

    public void download(final DownloadSilentFile.DownloadCompleted l) {

        download(mType, l);
    }

    public View getLargeView(Context context) {
        if (mLargeView != null) return mLargeView;
        mLargeView = new LargeImageView(context, this);
        return mLargeView;
    }

    public void setImageToImageView(final View root, final int imgViewId, final int prgrsId, final String type, final int width, final int height) {
        setImageToImageView(root, imgViewId, prgrsId, type, width, height, -1, false);
    }

    public void setImageToImageView(final View root, final int imgViewId, final int prgrsId, final String type, final int width, final int height, final int emptyImage, final boolean scaleImageView) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params)  {
                return downloadDirect(type);
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                setView(result > 0, root, imgViewId, prgrsId, type, width, height, emptyImage, scaleImageView);
            }
        }.execute();
    }

    public void setView(final boolean ok, final View root, final int imgViewId, final int prgrsId, final String type, final int width, final int height, final int emptyImage, final boolean scaleImageView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prgrsId > 0)
                    view.setVisibility(root.findViewById(prgrsId), false);
                ImageView imageView = (ImageView) root.findViewById(imgViewId);
                int breakId = emptyImage < 0 ? R.drawable.image_break : emptyImage;
                Bitmap bitmap = null;
                if (ok) {

                    String path = getPath(type);
                    if (!File.Exist(path)) {
                        download(type, new DownloadSilentFile.DownloadCompleted() {
                            @Override
                            public void OnDownloadCompleted(boolean succeed, String path) {
                                if (!succeed) return;
                                setView(true, root, imgViewId, prgrsId, type, width, height, emptyImage, scaleImageView);
                            }
                        });
                        return;
                    }
                    bitmap = Graphics.decodeFile(path, width, height);
                    if (bitmap == null) {
                        File.Delete(path);
                        imageView.setImageResource(breakId);

//						setImageViewSize(imageView,);
                    } else {
                        if (width > 0 && height > 0) imageView.setImageBitmap(bitmap);
                        else {
                            bitmap = BitmapFactory.decodeFile(getPath(type));
                            imageView.setImageBitmap(bitmap);

                        }

                    }
                } else imageView.setImageResource(breakId);
                if (scaleImageView) {
                    setImageViewSize(imageView, bitmap, breakId);
                }
            }
        }, id==null?0:id);
    }

    @Override
    public boolean equals(Object o) {
        if (o==null)return false;
        return o.toString().equals(toString());
    }

    @Override
    public String toString() {
        return getPath();
    }
}
