package ir.microsign.contentcore.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebSettings;

import java.util.Locale;

import ir.microsign.R;
import ir.microsign.contentcore.object.Content;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.view.ViewImageHtml;

/**
 * Created by Mohammad on 29/08/2015.
 */
public class RichWebView extends ViewImageHtml {

//	static String extraStyle = null;
	//    public OnLoadListener mOnLoadListener = null;
	public int mStartPosition = -1;
	public String mStartTag = "";
	public boolean mSoftScroll = false;
	boolean mUseJavaScript = false;
	Font mRtlFont = null;

	public RichWebView(Context context) {
		super(context);
	}

	public RichWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RichWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setContent(String html, Font rtlFont, Font font, boolean rtl, boolean useScript, int sidesPadding) {
		setUseJavaScript(useScript);
		mPadding_sides = sidesPadding;
		mRtlFont = rtlFont == null ? Font.getFont(getContext(),Font.TextPos.webrtl) : rtlFont;
		if (mRtlFont.getIndex() < 0) mRtlFont = font;
		setContent(html, font == null ? Font.getFont(getContext(),Font.TextPos.web) : font, rtl, false);

	}

//    public void setOnLoadListener(OnLoadListener l) {
//        this.mOnLoadListener = l;
//    }

	public void setContent(String html, boolean useScript, int sidesPadding) {
		setContent(html, null, null, getContext().getResources().getBoolean(R.bool.content_is_rtl), useScript, sidesPadding);

	}

	public void setUseJavaScript(boolean use) {
		mUseJavaScript = use;
	}

	@Override
	public String getScript() {

		WebSettings settings = getSettings();
		settings.setJavaScriptEnabled(mUseJavaScript);
		if (!mUseJavaScript) return "";
		StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">").append(Text.ReadResTxt(getContext(),R.raw.scroll)).append(Text.ReadResTxt(getContext(),R.raw.jquery)).append("</script>");
		//	sb.append("<script type=\"text/javascript\">").append(Text.ReadResTxt(R.raw.responsive_tablesjs)).append("</script>");

		return sb.toString();
	}

	@Override
	public String getBodyExtra() {
		return getScrollFunction();
	}

//    @Override
//    public WebViewClient getWebViewClient() {
////		setOnLongClickListener(mLongClickListener);
//        return super.getWebViewClient();
//    }

	String getScrollFunction() {
		if (mStartPosition < 0 && Text.isNullOrEmpty(mStartTag)) return "";
		if (Text.isNullOrEmpty(mStartTag))
			return String.format(Locale.ENGLISH, "onload=\"%s(%d)\"", mSoftScroll ? "softScrollByPosition" : "hardScrollByPosition", mStartPosition);
		return String.format(Locale.ENGLISH, "onload=\"softScrollById(%s)\"", mStartTag);
	}

	@Override
	public boolean onLinkClicked(String url) {
		if (mOnUrlClickListener != null) if (!mOnUrlClickListener.onClick(this, url)) return true;
		if (Text.isNullOrEmpty(url)) return true;
		if (!Text.isNullOrEmpty(mBaseUrl) && url.startsWith(mBaseUrl)) {

			String _id = Text.tryGetMatch(url, "&_id=\\d+", 0);
			if (_id == null) return false;
			String id =_id.substring(4);
			boolean newWindow = Text.hasMatches(mContent, "<a[^>]*com_content[^>]*;_id=" + String.valueOf(id) + ":[^>]*target=\"_blank\"[^>]*>");
			return openLink(id, newWindow);

		}

		boolean toDic = url.startsWith(Content.getHelper().getPrefix() + ":");
		if (!toDic) return super.onLinkClicked(url);
		try {
			Intent intent = new Intent(toDic ? Content.getHelper().getAction() : Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			intent.setPackage(getContext().getPackageName());
			getContext().sendBroadcast(intent);
			if (mOnUrlClickListener != null) return mOnUrlClickListener.onClicked(this, url);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (mOnUrlClickListener != null) return mOnUrlClickListener.onClicked(this, url);
			return false;
		}
	}
public interface OnOpenContentListener{
	boolean openContent(String id, boolean popUp);
}
	public OnOpenContentListener mOnOpenContentListener=null;
	public boolean openLink(String id, boolean popUp) {
		if (mOnOpenContentListener!=null)return mOnOpenContentListener.openContent(id,popUp);
return true;
	}

	public void share() {
		Text.Share(getContext(), Text.CleanHtml(mContent),getContext().getPackageName().substring(getContext().getPackageName().lastIndexOf(".") + 1));
	}

	@Override
	public String getExtraStyle() {
		if (mExtraStyle == null) mExtraStyle = Text.ReadResTxt(getContext(),R.raw.extrastyle);
		return mExtraStyle;
	}


}
