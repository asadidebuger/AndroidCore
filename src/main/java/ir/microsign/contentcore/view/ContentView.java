package ir.microsign.contentcore.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import ir.microsign.R;
import ir.microsign.contentcore.activity.ActivityMain;
import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.dialog.DialogContent;
import ir.microsign.contentcore.object.BaseObject;
import ir.microsign.contentcore.object.BookIndexItem;
import ir.microsign.contentcore.object.Content;
import ir.microsign.contentcore.object.Helper;
import ir.microsign.contentcore.object.MusicList;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;
import ir.microsign.view.ViewHtml;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ContentView extends BaseView implements ViewHtml.OnSlideListener,View.OnClickListener {

//	net.tarnian.content.object.Content mContent = null;
    ViewHtml.OnSlideListener mOnSlideListener=null;

    public void setOnSlideListener(ViewHtml.OnSlideListener l) {
        this.mOnSlideListener = l;
    }

    RichWebView richWebView = null;
    ActivityMain mActivity=null;
//    public ContentView(Context activity) {
//        super(activity);
//    }
    public ContentView(ActivityMain activity) {
        super(activity);
        mActivity=activity;
    }

    public ContentView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initFromBaseObject(ir.microsign.dbhelper.object.BaseObject baseObject) {
        super.initFromBaseObject(baseObject);

    }

    public int setContent(DataSource dataSource, BookIndexItem parent,String lastSelectedId) {
        showNextPage(false);
        List<Content> childs = (List<Content>) dataSource.getSubContentItemChilds(parent);
        final StringBuilder sb = new StringBuilder(childs.size()*200);
        if (childs.size() == 1) {

         mBaseObject=childs.get(0);
            sb.append(getItem().getHtmlFullText(getContext()));
        }else {
            for (Content content : childs) {
                content.getHtmlItem(sb, true);
            }
            mBaseObject = null;
        }
        boolean hasStartTag=Text.isEmpty(lastSelectedId)&&childs.size()>1;
        getRichWebView().mStartTag=hasStartTag?"cnt"+String.valueOf(lastSelectedId):null;
        getRichWebView().setOnSlideListener(childs.size()==1?this:null);
        setContent(sb.toString(), hasStartTag, childs.size() == 1 ? 5 : 0);
        return childs.size();
    }
    View imgNext,imgPrev;
public void showNextPage(boolean show){
    if (imgNext==null){
        imgNext=findViewById(R.id.img_slide_next);
        if (imgNext==null)return;
        imgPrev=findViewById(R.id.img_slide_prev);

    imgNext.setOnClickListener(this);
    imgPrev.setOnClickListener(this);
    }
    view.setVisibility(imgNext,show);
    view.setVisibility(imgPrev,show);
}
    public void setContent(DataSource dataSource, String contentId, String linkId, int scrollPosition) {
        showNextPage(dataSource.getParalellContentsCount(contentId)>1);
        Font font = Font.getFont(getContext(),Font.TextPos.web);
        font.setBackColor(getContext().getResources().getColor(R.color.color_headlines_right_solid));
        getRichWebView().mStartPosition = scrollPosition > 0 ? scrollPosition : 0;
        getRichWebView().mSoftScroll = scrollPosition <= 0;
        getRichWebView().mStartTag = linkId;
        mBaseObject=dataSource.getContent(contentId);
        getRichWebView().setOnSlideListener(this);
        setContent(getItem().getHtmlFullText(getContext()), true, 5);

    }

    public void setContent(String html, boolean useScript, int sidesPadding) {
        getRichWebView().setContent(html, useScript, sidesPadding);
        LinearLayout extra= (LinearLayout) findViewById(R.id.ll_extra);
        MusicListView.release();
        if (extra!=null)
            extra.removeAllViews();
        if (getItem()!=null&&!Text.isNullOrEmpty(getItem().mExtra))
        {

            MusicList musicList=new MusicList(mActivity,getItem().title,getItem().mExtra);

            extra.addView(musicList.getView(getContext()),-1,-2);
        }
    }
//public void removePlayer(){
//
//}
    void setView() {
        if (getChildCount() < 1) {
            getLayoutInflater().inflate(R.layout.layout_content_web, this, true);
            richWebView = (RichWebView) findViewById(R.id.rich_web_view);
            richWebView.mTextAlign = "unset";
            richWebView.mBaseUrl="http://localhost";richWebView.mOnOpenContentListener=new RichWebView.OnOpenContentListener() {
				@Override
				public boolean openContent(String id, boolean popUp) {
					DialogContent.showMessage(getContext(), Helper.getHelper().getDataSource().getContent(id));
					return true;
				}
			};
            this.setBackgroundColor(getContext().getResources().getColor(R.color.color_headlines_right_solid));
            richWebView.getSettings().setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT > 10)
                richWebView.getSettings().setDisplayZoomControls(false);
            richWebView.mPadding_sides = 0;
        }
    }

    public RichWebView getRichWebView() {
        if (richWebView == null) {
            removeAllViews();
            setView();
        }

        return richWebView;

    }

    public Content getItem() {
        return (Content) getDbObject();
    }

    @Override
    public void onSlide(boolean next) {
        if (mOnSlideListener!=null)mOnSlideListener.onSlide(next);
    }

    @Override
    public void onClick(View v) {
        onSlide(v.getId()!=R.id.img_slide_next);
    }


//    @Override
//    public void onSlide(boolean next) {
//
//    }
}
