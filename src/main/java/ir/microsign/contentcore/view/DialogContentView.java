package ir.microsign.contentcore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import ir.microsign.R;
import ir.microsign.contentcore.dialog.DialogContent;
import ir.microsign.contentcore.object.Content;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
/**
 * Created by Mohammad on 6/24/14.
 */
public class DialogContentView extends BaseView
{

DialogContent.OnDialogResultListener mOnDialogResultListener=null;
    public DialogContentView(Context context) {
        super(context);
    }

    public DialogContentView(Context context, ir.microsign.contentcore.object.BaseObject baseObject, DialogContent.OnDialogResultListener l) {
        super(context, baseObject);
        mOnDialogResultListener=l;
    }

    public DialogContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_item_full_screen_content, this);
        setView();
    }

    public void setView() {
        findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDialogResultListener!=null)mOnDialogResultListener.OnDialogResult(false,null);
            }
        });
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

        final RichWebView webView = (RichWebView) findViewById(R.id.web_content);
        webView.mShowBeforeImage = getContext().getResources().getBoolean(R.bool.content_full_show_images_before);
        webView.mShowFirstImage = getContext().getResources().getBoolean(R.bool.content_full_show_images_first);
        webView.mShowBeforeImage = getContext().getResources().getBoolean(R.bool.content_full_show_images_before);
        webView.mExtraStyle = Text.ReadResTxt(getContext(),R.raw.extrastyle);
        webView.mSplitter = "\\{BREAKMORE\\}";

        webView.setContent(getContent().getFull());

        setImage(getContext().getResources().getBoolean(R.bool.content_full_show_images_navigator) && getContent().haveImageIntro());

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
