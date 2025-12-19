package ir.microsign.content.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import ir.microsign.R;
import ir.microsign.content.activity.ActivityContentMain;
import ir.microsign.content.database.DataSource;
import ir.microsign.content.object.Content;
import ir.microsign.contentcore.dialog.DialogContent;
import ir.microsign.contentcore.object.Category;
import ir.microsign.contentcore.view.ImageNavigator;
import ir.microsign.contentcore.view.RichWebView;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/24/14.
 */
public class FeaturedContentView extends BaseView //implements ScrollView.onScrollChangedListener
{

	public FeaturedContentView(Context context) {
		super(context);
	}

	public FeaturedContentView(Context context, ir.microsign.contentcore.object.BaseObject baseObject) {
		super(context, baseObject);
	}

	public FeaturedContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
		super.initFromBaseObject(baseObject);
		getLayoutInflater().inflate(R.layout.layout_item_full_content, this);
		try {
			Text.setText(findViewById(R.id.txt_titr), getContent().title, Font.TextPos.h1);
		} catch (NoSuchFieldError error) {
			error.printStackTrace();
		}

		try {
			Text.setText(findViewById(R.id.txt_date), getResources().getBoolean(R.bool.content_full_show_date) ? getContent().getDate(getContext()) : null, Font.TextPos.small);
		} catch (NoSuchFieldError error) {
			error.printStackTrace();
		}

		RichWebView webView = (RichWebView) findViewById(R.id.web_content);
		String pattern = getContext().getString(R.string.content_pattern_name);
		if (!Text.isNullOrEmpty(pattern)) pattern = "file:///android_asset/pattern/" + pattern;
		webView.mShowBeforeImage = getContext().getResources().getBoolean(R.bool.content_full_show_images_before);
		webView.mShowFirstImage = getContext().getResources().getBoolean(R.bool.content_full_show_images_first);
		webView.mShowBeforeImage = getContext().getResources().getBoolean(R.bool.content_full_show_images_before);
		webView.mExtraStyle = Text.ReadResTxt(getContext(),R.raw.extrastyle);
		webView.mSplitter = "\\{BREAKMORE\\}";
//		view.setVisibility(findViewById(R._id.txt_spliter), showSpliter());
		webView.setContent(getContent().getFull().replace("href=\"index.php?", "href=\"http://www.l.l/index.php?"), Font.getFont(getContext(),Font.TextPos.web), pattern);
		webView.mBaseUrl = "http://localhost";
		webView.mOnOpenContentListener = new RichWebView.OnOpenContentListener() {
			@Override
			public boolean openContent(String id, boolean popUp) {
				openLink(id, popUp);
				return true;
			}
		};
//        webView.setOnUrlClickListener(new ViewHtml.OnUrlClickListener() {
//            @Override
//            public boolean onClick(WebView view, String url) {
//                String local="http://www.l.l";
////                if (url.startsWith(local))url=url.substring(local.length());
//                if (url.startsWith(local)||
//                        url.startsWith("about:index.php?option=com_content&view=article&_id=")||
//                        url.startsWith("index.php?option=com_content&view=article&_id=")){
//                    String _id=Text.tryGetMatch(url,"&_id=\\d+",0);
//                    if (_id==null)return false;
//                    int _id=Integer.parseInt(_id.substring(4));
////                    String txt=
//                    boolean newWindow=Text.hasMatches(getContent().getFull(),"<a[^>]*com_content[^>]*;_id="+String.valueOf(_id)+":[^>]*target=\"_blank\"[^>]*>");
//                    openLink(_id,newWindow);
//                    return false;
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onClicked(WebView view, String url) {
//                return false;
//            }
//        });
		if (!Display.isLandscape(getContext()))
			Text.setText(findViewById(R.id.txt_spliter), getContent().mSplitText, Font.TextPos.h1);
		if (getContext().getResources().getBoolean(R.bool.content_feature_show_images_navigator))
			setImage();

	}

	void openLink(String id, boolean popUp) {
		Content content = (Content) DataSource.getDataSource().getContent(id);
		if (popUp) {
			DialogContent.showMessage(ActivityContentMain.mActivityContentMain, content);
			return;
		}
		Category category = DataSource.getDataSource().getCategory(content.cat);
		ActivityContentMain.mActivityContentMain.onItemSelected(category);
		ActivityContentMain.mActivityContentMain.onItemSelected(content);
	}

	void setImage() {

		ViewGroup llImage = (ViewGroup) findViewById(R.id.ll_image);
		if (llImage.getChildCount() > 0 || getContent().getImagesIntro().size() < 1) return;
		ImageNavigator imageNavigator = new ImageNavigator(getContext());
		llImage.addView(imageNavigator, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, (int) (Display.getHeight(getContext()) / 3f)));

		imageNavigator.setImageList(getContent().getImagesIntro());
	}

	public Content getContent() {
		return (Content) getDbObject();
	}
}
