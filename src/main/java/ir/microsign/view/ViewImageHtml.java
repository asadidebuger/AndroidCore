package ir.microsign.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.utility.Image;
import ir.microsign.utility.Text;
//import ir.microsign.gallery.activity.FullScreenImagesPathActivity;
import ir.microsign.net.DownloadSilentFile;

/**
 * Created by Mohammad on 07/01/2015.
 */
public class ViewImageHtml extends ViewHtml {
    static String mLastImage = null;
    public String mSplitter = null;
    public boolean mShowBeforeImage = true, mShowAfterImages = true, mShowFirstImage = true,mShowFirstImageNextSection;
    boolean converted = false;
    List<Image> _images = new ArrayList<Image>();

    public ViewImageHtml(Context context) {
        super(context);
    }

    public ViewImageHtml(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ViewImageHtml(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    String prepareContent(String src, String splitter, boolean showBeforeImage, boolean showAfterImages, boolean showFirstImage) {

        if (Text.isNullOrEmpty(src)) {
            converted = true;
            return src;
        }
        _images = new ArrayList<Image>();
        boolean firstOk = false;
        String[] arr = splitter == null ? new String[]{src} : src.split(splitter);
        String result = "";
        for (int blockIndex = 0, arrLength = arr.length; blockIndex < arrLength; blockIndex++) {
            String txt = arr[blockIndex];
            List<String> matches = Text.getMatches(txt, "<img.*?>");

            if (matches.size() < 1) {
                if (arr.length == 1) {
                    return src;
                }

                if (blockIndex == 0) {
                    result = txt;
                    continue;
                }
                return result + txt;
            }

            for (int i = 0; i < matches.size(); i++) {
                String match = matches.get(i);
                final Image img = new Image(getContext(),"",match);
                String imgHtml = img.getImageHtmlTag();
                if (blockIndex == 0 && i == 0) {
                    txt = txt.replace(match, showFirstImage ? imgHtml : "");
                    firstOk = true;
                } else if (blockIndex == 0)
                    txt = txt.replace(match, showBeforeImage ? imgHtml : "");
                else if (blockIndex > 0 && i == 0 && (!firstOk))
                { txt = txt.replace(match, showFirstImage ? imgHtml : ""); firstOk = true;}
                else txt = txt.replace(match, showAfterImages ? imgHtml : "");
                _images.add(img);

//                if (!img.Exist()) {
                    img.download(this,new DownloadSilentFile.DownloadCompleted(){
                        @Override
                        public void OnDownloadCompleted(boolean succeed, String path) {
                            if (!succeed) return;
                            for (Image img0 : _images) {
                                if (!img0.Exist()&&!img.equals(img0)) return;
                            }
                            ViewImageHtml.this.setContent();
                        }
                    });
//                }
            }
            result += txt;
        }

        converted = true;
        return result;
    }

    public boolean onLinkClicked(String url) {

        if (mOnUrlClickListener != null) if (!mOnUrlClickListener.onClick(this, url))return true;
        String uri = url;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            if (url.contains("joomplu:")) {
//                Image img = new Image().fromUrl(url);
//                uri = img.getExistPath();
//                File file = new File(uri);
//                if (!file.exists()) return true;
//
//                String folder = file.getParent();
//                String filename = file.getName();
//                FullScreenImagesPathActivity.show(getContext(), folder, filename, false);
//                return true;
//            }


            intent.setData(Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            if (mOnUrlClickListener != null) return mOnUrlClickListener.onClicked(this, url);
            return true;
//					}
        } catch (Exception e) {
            Log.e("ViewImageHtml2", e.getMessage() + ":" + uri);
            e.printStackTrace();
            if (mOnUrlClickListener != null)return mOnUrlClickListener.onClicked(this, url);
            return false;
        }
    }

    @Override
    public String doFinalChanges(String html) {
        return prepareContent(html, mSplitter, mShowBeforeImage, mShowAfterImages, mShowFirstImage);
    }

}
