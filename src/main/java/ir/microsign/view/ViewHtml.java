package ir.microsign.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ir.microsign.utility.Font;
import ir.microsign.utility.Reshape;
import ir.microsign.utility.Text;
import java.util.Locale;


/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 10/15/13
 * Time: 8:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewHtml extends WebView {

	public static String mTypeFace = null;
	public boolean mReplaceLine = false;
	public int mPadding_sides = 5, mPadding_heights = 0;
	public Font mFont = null;
	// Font.getFontByKey(getContext(),Font.TextPos.web);

	//	public void setBackgroundColor(int color) {
//		mBackgroundColor = color;
//	}
	public String mContent = "";
	public Boolean mRTL = true;
	public String mPatternPath = null;
	public String mExtraStyle =null;
	public String mTextAlign = "justify";
	public OnUrlClickListener mOnUrlClickListener = null;
	public String mBaseUrl = null;
	OnLongClickListener mLongClickListener = null;
	OnSlideListener mOnSlideListener = null;
	private GestureDetector gestureDetector = null;

	public ViewHtml(Context context) {
		super(context);
		init();
	}

	public ViewHtml(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewHtml(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	boolean textIsHtml(String txt) {
//		txt.matches("</?[^>]+?>");
		return Text.isContainByRegex(txt, "</?[^>]+?>");
	}

	private void init() {
		if (isInEditMode()) return;
		gestureDetector = new GestureDetector(getContext(), new GestureListener());
		mFont = Font.getFontByKey(getContext(), Font.TextPos.web);
		WebSettings settings = getSettings();
		settings.setDefaultTextEncodingName("utf-8");
//		settings.setUseWideViewPort(true);


	}

	public void setContent(String content) {
		mReplaceLine = !textIsHtml(content);
		mContent = content;
		setContent();
	}

	public void setContent(String content, boolean replaceLine) {
		mReplaceLine = replaceLine;
		mContent = content;
		setContent();
	}

	public void setContent(String content, String textPos, boolean replaceLine) {
		mReplaceLine = replaceLine;
		mContent = content;
		mFont = Font.getFont(getContext(),textPos);

		setContent();
	}

	public void setContent(String content, String textPos) {
		mContent = content;
		mFont = Font.getFont(getContext(),textPos);
		setContent();
	}

	public void setContent(String content, String textPos, String patternPath) {
		mContent = content;
		mFont = Font.getFont(getContext(),textPos);
		mPatternPath = patternPath;
		setContent();
	}

	public void setContent(String content, Font font, String patternPath) {
		mContent = content;
		mFont = font;
		mPatternPath = patternPath;
		setContent();
	}

	public void setContent(String content, String textPos, boolean rtl, boolean replaceLine) {
		mContent = content;
		mFont = Font.getFont(getContext(),textPos);
		mRTL = rtl;
		mReplaceLine = replaceLine;
		setContent();
	}

	public void setContent(String content, Font font, boolean rtl, boolean replaceLine) {
		mContent = content;
		mFont = font;
		mRTL = rtl;
		mReplaceLine = replaceLine;
		setContent();
	}

	public void setContent(String content, Font font, boolean rtl) {
		mContent = content;
		mFont = font;
		mRTL = rtl;
		mReplaceLine = !textIsHtml(content);
		setContent();
	}

	public void setContent() {
		String html = makeHtml(doFinalChanges(mContent), mFont, mRTL, mReplaceLine);
		String mime = "text/html";
		String encoding = "utf-8";
		setWebViewClient(getWebViewClient());
		setOnLongClickListener(mLongClickListener);
		loadDataWithBaseURL(mBaseUrl, html, mime, encoding, null);
	}

	public String doFinalChanges(String html) {
		return html;
	}

	public boolean onLinkClicked(String url) {

		if (mOnUrlClickListener != null) if (!mOnUrlClickListener.onClick(this, url)) return true;
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(intent);

			if (mOnUrlClickListener != null) return mOnUrlClickListener.onClicked(this, url);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (mOnUrlClickListener != null) return mOnUrlClickListener.onClicked(this, url);
			return false;
		}
	}

	public WebViewClient getWebViewClient() {
		return new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.e("url clicked",this.getClass().getName());
				return onLinkClicked(url);
			}
		};
	}

	public void setOnLongClickListener(OnLongClickListener l) {
		mLongClickListener = l;
		if (mLongClickListener == null) mLongClickListener = new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return ViewHtml.this.onLongClick();
			}
		};
		super.setOnLongClickListener(mLongClickListener);
	}

	public boolean onLongClick() {
		return true;
	}

	public String getPattern() {
//		String path="file:///android_asset/"+imageName;
		if (Text.isNullOrEmpty(mPatternPath)) return "none";
		return "url(\"" + mPatternPath + "\")";
	}

	//	String getPaddingSides(){
//		return String.format(Locale.ENGLISH,"%d", mPadding_sides);
//	}
//	String getPaddingHeights(){
//		return String.format(Locale.ENGLISH,"%d", mPadding_heights);
//	}
	public ViewHtml setPadding(int padding) {
		mPadding_sides = padding;
		return this;
	}

	public StringBuilder getHead(Font font, boolean rtl) {
		StringBuilder sb = new StringBuilder(1024);
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><style type=\"text/css\">")
				.append(getTypeFaces())
				.append("html {font-family:")
				.append(font.getFontName())
				.append(";line-height:")
				.append(font.getLineHeight() > 0 ? Math.round(font.getLineHeight()) + "%" : "100%")
				.append(";font-size:")
				.append(font.getSize() > 0 ? Math.round(font.getSize()) + "px" : "normal")
				.append(";direction:")
				.append(rtl ? "rtl" : "ltr")
				.append(";color:")
				.append(font.getColorString())
				.append(";font-weight:normal")
				.append(";background-color:")
				.append(getBackgroundColorString(font))
				.append(";background-image:")
				.append(getPattern())
				.append(";text-align:")
				.append(mTextAlign).append(";margin:0;padding:0;max-width: 100%;}")
				.append(getExtraStyle())
				.append("</style>").append(getScript()).append("</head>");
		return sb;


	}


	public String getBackgroundColorString(Font font) {
		return  font.getBackColorString();
	}

	public String getExtraStyle() {
		return mExtraStyle==null?"": mExtraStyle;
	}

	public String getScript() {
		return "";
	}

	public StringBuilder getBody(String content, boolean replaceLines) {
//		content=content.replace("href=\"index.php?","href=\"http://www.l.l/index.php?");
		StringBuilder sb = new StringBuilder();
		sb.append("<body ").
				append(getBodyExtra()).
				append("><div style=\"margin:").
				append(String.format(Locale.ENGLISH, "%dpx %dpx %dpx %dpx", getPaddingTop() + mPadding_heights, getPaddingRight() + mPadding_sides, getPaddingBottom() + mPadding_heights, getPaddingLeft() + mPadding_sides))
				.append(";\">")
				.append(
						(replaceLines ?
								Reshape.reshapeWeb(content).replaceAll("\r?\n", "<br>") :
								Reshape.reshapeWeb(content)))
				.append("</div></body>");
		return sb;


	}

	public String getBodyExtra() {
		return "";
	}

	public String makeHtml(String input, Font font, boolean rtl, boolean replaceLines) {
		return getHead(font, rtl).append(getBody(input == null ? "" : input, replaceLines)).append("</html>").toString();
	}

	private String getTypeFaces() {
		if (mTypeFace != null) return mTypeFace;
		String typeFace = "";
		String[] fonts = Font.getFonts(getContext());
		for (int i = 0; i < fonts.length; i++) {
			boolean isBold=Font.getFontName(getContext(),i, false).endsWith("bold");
			if (isBold) {
				typeFace += "@font-face{font-family:" + Font.getFontNameForWeb(getContext(), i, false) + ";\nsrc:url(" + Font.getFontPath(getContext(), i) + ") ;\n}\n";
			}
			typeFace += "@font-face{font-family:" + Font.getFontNameForWeb(getContext(),i, true) + ";\nsrc:url(" + Font.getFontPath(getContext(),i) + ") ;\n";

			if (isBold) typeFace += "font-weight:bold;\n";
			 else typeFace +="font-weight:normal;\n";
			typeFace += "}\n";
		}
		mTypeFace = typeFace;
		return typeFace;
	}

	public void setOnUrlClickListener(OnUrlClickListener l) {
		mOnUrlClickListener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//        if(
		gestureDetector.onTouchEvent(event);
//                )return true;

		return super.onTouchEvent(event);
	}


	boolean onSlide(int delta) {
		if (mOnSlideListener == null) return false;
		mOnSlideListener.onSlide(delta > 0);
		return true;
	}

	public void setOnSlideListener(OnSlideListener l) {
		this.mOnSlideListener = l;
	}

	public interface OnUrlClickListener {
		boolean onClick(WebView view, String url);

		boolean onClicked(WebView view, String url);
	}

	public interface OnSlideListener {
		void onSlide(boolean next);
	}

	class GestureListener extends GestureDetector.SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						return onSlide((int) diffX);
//                        if (diffX > 0) {
//                            onSwipeRight();
//                        } else {
//                            onSwipeLeft();
//                        }
					}
//                    result = true;
				}
//                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffY > 0) {
//                        onSwipeBottom();
//                    } else {
//                        onSwipeTop();
//                    }
//                }
//                result = true;

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return false;
		}
	}

}
