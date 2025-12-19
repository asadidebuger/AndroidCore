package ir.microsign.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;

import ir.microsign.context.Application;

/**
 * Created by Mohammad on 9/25/14.
 */
public class Graphics {
    static final int small = 426 * 320, normal = 470 * 320, large = 640 * 480, xlarge = 960 * 720;
    static int dpi = -1;

    public static void setImage(Resources resources, ImageView imageView, int resId, int displayRate) {
        Bitmap b = BitmapFactory.decodeResource(resources, resId);
        int dW = Display.getWidth(imageView.getContext());
        int w = (dW * displayRate) / 100;
        float rate = (float) w / (float) b.getWidth();
        int h = (int) (b.getHeight() * rate);
        setImage(resources, imageView, b, w, h);
    }

    public static String getColorString(int color) {
        String color1 = color > 0 ? "#333333" : String.format(Locale.ENGLISH, "#%06X", 0xFFFFFF & color);
        return color1;
    }

    public static Drawable getIconFromXdpi(Resources rs, int id, float extraScale) {

        return resizeDrawable(rs, id, getScale(rs, extraScale));
    }
    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }
    public static void setImage(Resources resources, ImageView imageView, Bitmap b, int displayRate) {

        int dW = Display.getWidth(imageView.getContext());
        int w = (dW * displayRate) / 100;
        float rate = (float) w / (float) b.getWidth();
        int h = (int) (b.getHeight() * rate);
        setImage(resources, imageView, b, w, h);
    }

    public static void setImageMax(Resources resources, ImageView imageView, int resId, int maxW, int maxH) {
        Bitmap b = BitmapFactory.decodeResource(resources, resId);
        int bW = b.getWidth();
        int bH = b.getHeight();
        float rateH = (float) maxH / (float) bH;
        float rateW = (float) maxW / (float) bW;


        float h = rateW * bH;
        float w = rateH * bW;
        float rate = (h <= maxH) ? rateW : rateH;
        setImage(resources, imageView, b, (int) (rate * bW), (int) (rate * bH));
    }

    public static void setImage(Resources resources, ImageView imageView, int resId, int w, int h) {

        setImage(resources, imageView, BitmapFactory.decodeResource(resources, resId), w, h);
    }

    public static void setImage(Resources resources, ImageView imageView, Bitmap bitmap, int w, int h) {
        if (imageView == null) return;
        Bitmap b = resizeBitmap(bitmap, w, h);
        if (b != null && ((w <= 0 || h <= 0) || (b.getWidth() < w || b.getHeight() < h))) {
            w = b.getWidth();
            h = b.getHeight();
        }


        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

        if (layoutParams == null) layoutParams = new ViewGroup.LayoutParams(w, h);
        layoutParams.height = h;
        layoutParams.width = w;
        imageView.setImageBitmap(b);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setAdjustViewBounds(false);
        imageView.setMinimumHeight(h);
        imageView.setMinimumWidth(w);
    }

    public static Drawable getIconFromXdpi(Resources rs, Bitmap bm, Boolean beHalf) {

        return getDrawable(bm, getScale(rs, beHalf));

    }

    public static Drawable getIconFromXdpi(Resources rs, int id, Boolean beHalf) {

        return resizeDrawable(rs, id, getScale(rs, beHalf));
    }

    public static float getScale(Resources rs, Boolean beHalf) {
        return getScale(rs, beHalf ? .5f : 1f);
    }

    public static float getScale(Resources rs, Float extraScale) {
        //todo:Application.getContext()!
        if (dpi < 0) dpi = Display.pxToDp(rs, Display.getHeight(Application.getContext()) * Display.getWidth(Application.getContext()));
        Float scale = 1f;
        if (dpi >= xlarge) scale = 1f;
        else if (dpi >= large) scale = .75f;
        else if (dpi >= normal) scale = .5f;
        else if (dpi >= small) scale = .35f;
        else scale = .3f;
        scale *= extraScale;
        return scale;
    }

    //	public static float getScale(Resources rs,ScreenType srcType, Float extraScale) {
//		if (dpi<0)dpi=Display.pxToDp(rs,Display.getHeight() * Display.getWidth());
//		Float scale = 1f;
//		if (dpi >= xlarge) scale = 1f;
//		else if (dpi >= large) scale = .75f;
//		else if (dpi >= normal) scale = .5f;
//		else if (dpi >= small) scale = .35f;
//		else scale=.3f;
//		scale *= extraScale;
//		return scale;
//	}
//	public enum ScreenType{
//	nodpi,ldpi,mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi;
//	}
    public static Bitmap getBitmapIconFromXdpi(Resources rs, Bitmap bm, Boolean beHalf) {
        return resizeBitmap(bm, getScale(rs, beHalf));

    }

    public static Bitmap getBitmapIconFromXdpi(Resources rs, int id, Boolean beHalf) {
        return resizeBitmap(rs, id, getScale(rs, beHalf));

    }

    public static Bitmap getBitmapIconFromXdpi(Resources rs, int id, float extra) {
        return resizeBitmap(rs, id, getScale(rs, extra));

    }

    public static Drawable resizeDrawable(Resources rs, int id, float scale) {
        Bitmap bm = BitmapFactory.decodeResource(rs, id);
        return getDrawable(bm, scale);
    }

    public static Drawable getDrawable(Bitmap bm, float scale) {

        return new BitmapDrawable(resizeBitmap(bm, scale));
    }

    public static Drawable resizeDrawable(Drawable bm, float scale) {

        return new BitmapDrawable(resizeBitmap(((BitmapDrawable) bm).getBitmap(), scale));
    }

    public static Bitmap resizeBitmap(Resources rs, int id, float scale) {
        Bitmap bm = BitmapFactory.decodeResource(rs, id);
        return resizeBitmap(bm, scale);
    }
    public static Bitmap resizeBitmap(String path, float scale) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        return resizeBitmap(bm, scale);
    }

    public static Bitmap resizeBitmap(Bitmap bm, float scale, boolean recycle) {
        if (scale == 1) return bm;
        if (bm == null) return null;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        try {
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            if (recycle && bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
            return resizedBitmap;
        }
        catch (Exception ex){return null;}
    }

    public static Bitmap resizeBitmap(Bitmap bm, float scale) {
        return resizeBitmap(bm, scale, true);
    }

    public static Bitmap resizeBitmap(Resources rs, int id, int w, int h) {

        return resizeBitmap(BitmapFactory.decodeResource(rs, id), w, h);
    }

    public static Bitmap getResizedBitmap(String patch, int max_height) {
        if (patch == null || patch.length() < 1) return null;
        return getResizedBitmap(BitmapFactory.decodeFile(patch), max_height);
    }

    public static Bitmap resizeBitmap(Bitmap bm, int w, int h) {
        return resizeBitmap(bm, w, h, true);
    }

    public static Bitmap resizeBitmap(Bitmap bm, int w, int h, boolean recycle) {
        if (bm == null) return null;

        int height = bm.getHeight();
        int width = bm.getWidth();

        float scaleX = 1;
        float scaleY = 1;

        if (w <= 0 && h > 0) {
            w = (int) (((float) h / (float) height) * width);
        } else if (w > 0 && h <= 0) {
            h = (int) (((float) w / (float) width) * height);
        } else if (w <= 0 && h <= 0) {
            w = width;
            h = height;
        }
//		if (h < height)
//		{
        scaleY = (float) h / (float) height;
//			height = h;
//		}
//		if (w < width) {
        scaleX = (float) w / (float) width;
//			width = w;
//		}
        if (scaleX == 1 && scaleY == 1) return bm;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (recycle && bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return resizedBitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int max_height) {
        return getResizedBitmap(bm, max_height, true);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int max_height, boolean recycle) {
        if (bm == null) return null;
        int height = bm.getHeight();
        if (max_height >= height) return bm;
        int width = bm.getWidth();
        // float aspect = (float)width / height;
        float scale = (float) max_height / (float) height;
//            float scaleWidth = width*scale;
//            float scaleHeight = height*scale;        // yeah!
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scale, scale);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (recycle && bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return resizedBitmap;
    }

    public static boolean saveBitmapToFile(Bitmap bmp, String savePath, int quality) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(savePath));

            String sp = savePath.toLowerCase();
            Bitmap.CompressFormat format = sp.contains(".png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
            bmp.compress(format, quality, fOut);
            fOut.flush();
            fOut.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap applyWatermark(Bitmap src, Bitmap watermark) {
        try {


            Bitmap bmOverlay = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
            int left = src.getWidth() - watermark.getWidth();
            int top = src.getHeight() - watermark.getHeight();
            Canvas canvas = new Canvas(bmOverlay);
            canvas.drawBitmap(src, new Matrix(), null);
            canvas.drawBitmap(watermark, left, top, null);
            return bmOverlay;
        } catch (Exception ex) {
            Log.e("applyWatermark", ex.getMessage());
            return src;
        }
    }

    public static Bitmap decodeFile(String filePath, int width, int height) {
        try {
            if (width == 0 && height == 0) return BitmapFactory.decodeFile(filePath);
            File f = new File(filePath);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            if (width <= 0 && height <= 0) return bitmap;

            final int REQUIRED_WIDTH = width;
            final int REQUIRED_HEIGHT = height;
            int scale = 1;

            while ((width > 0 && height > 0 && o.outWidth / scale / 2f >= REQUIRED_WIDTH && o.outHeight / scale / 2f >= REQUIRED_HEIGHT) || (width > 0 && o.outWidth / scale / 2f >= REQUIRED_WIDTH) || (height > 0 && o.outHeight / scale / 2f >= REQUIRED_HEIGHT))
//			(width>0&&o.outWidth / scale / 2f >= REQUIRED_WIDTH)||&& o.outHeight / scale / 2f >= REQUIRED_HEIGHT)
                scale *= 2;
//			bitmap.recycle();
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (Exception e) {
            Log.e("decodeFile", e.getMessage());
        }
        return null;
    }
}
