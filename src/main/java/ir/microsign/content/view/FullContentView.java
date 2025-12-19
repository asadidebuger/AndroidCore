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
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 6/24/14.
 */
public class FullContentView extends BaseView //implements ScrollView.onScrollChangedListener
{
    static Font mCustomFont = null;
    public FullContentView(Context context) {
        super(context);
    }

    public FullContentView(Context context, ir.microsign.contentcore.object.BaseObject baseObject) {
        super(context, baseObject);
    }

    public FullContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void setCustomFont(Font font) {
        mCustomFont = font;
    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_item_full_content, this);
        setView();
    }

    public void setView() {
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
        webView.mBaseUrl="http://localhost";
        webView.mOnOpenContentListener=new RichWebView.OnOpenContentListener() {
            @Override
            public boolean openContent(String id, boolean popUp) {
                openLink(id,popUp);
                return true;
            }
        };
        webView.setContent(getContent().getFull(), mCustomFont == null ? Font.getFont(getContext(),Font.TextPos.web) : mCustomFont,pattern);
//        webView.setContent(getContent().getFull(),Font.getFont(Font.TextPos.web),pattern);
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


        setImage(getContext().getResources().getBoolean(R.bool.content_full_show_images_navigator) && getContent().haveImageIntro());

    }
    void openLink(String id,boolean popUp){
    Content content= (Content) DataSource.getDataSource().getContent(id);
    if (popUp){
        DialogContent.showMessage(getContext(),content);
        return;
    }
    Category category= DataSource.getDataSource().getCategory(content.cat);
    ActivityContentMain.mActivityContentMain.onItemSelected(category);
    ActivityContentMain.mActivityContentMain.onItemSelected(content);
}

    void setImage(boolean mShowImageNavigator) {
        if (!mShowImageNavigator) return;
        ViewGroup llImage = (ViewGroup) findViewById(R.id.ll_image);
        if (llImage.getChildCount() > 0 || getContent().getImagesAll().size() < 1) return;
        ImageNavigator imageNavigator = new ImageNavigator(getContext());
        llImage.addView(imageNavigator, new ViewGroup.LayoutParams(-1, -1));
        imageNavigator.setImageList(getContent().getImagesAll());
    }

    public Content getContent() {
        return (Content) getDbObject();
    }
}
