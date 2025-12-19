package ir.microsign.content.fragment;


import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ir.microsign.apphelper.interfaces.Listener;
import ir.microsign.R;
import ir.microsign.content.object.Content;
import ir.microsign.content.object.ContentAlert;
import ir.microsign.content.object.ContentLogo;
import ir.microsign.contentcore.object.Category;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

/**
 * Created by Mohammad on 6/25/14.
 */
public class FragmentContent extends FragmentBase {
    public String mPayment = null;
    LinearLayout ll_featured = null, ll_root = null;

    //	String mSpliterText=null;
//	public void setSpliterText(String spliterText){
//		mSpliterText=spliterText;
//	}
    ContentAlert getContentAlert(final boolean shouldBuy, final String payment, final String... args1) {
        ContentAlert contentAlert = new ContentAlert(getString(shouldBuy ? R.string.content_alert_buy_title : R.string.content_alert_down_title),
                getString(shouldBuy ? R.string.content_alert_buy_desc1 : R.string.content_alert_down_desc1),
                shouldBuy ? String.format(getString(R.string.content_alert_buy_desc2), ((payment == null) ? "" : String.valueOf(0 / 10))) : null,
                getString(R.string.content_alert_back),
                getString(shouldBuy ? R.string.content_alert_buy : R.string.content_alert_download),
                getResources().getDrawable(shouldBuy ? R.drawable.icon_access_denied : R.drawable.icon_download_content)

        );
        contentAlert.setEventRequestListener(new Listener.EventRequestListener() {
            @Override
            public void onEventRequest(int code, Object... args) {
                int code1 = 0;
                if (code == 2) {
                    code1 = shouldBuy ? 2 : 1;
                }
                if (code1 == 2)
                    OnEventRequestListener(code1, payment, args1[0]);
                else OnEventRequestListener(code1);
            }
        });
        return contentAlert;
    }

    ContentLogo getContentLogo() {
        ContentLogo contentAlert = new ContentLogo(getString(R.string.content_logo_desc)
                , Font.getFont(getContext(),Font.TextPos.h1)
                , new Font(getContext(),getContext().getResources().getInteger(R.integer.content_logo_font_index), getResources().getDimension(R.dimen.content_logo_description_font_size))
                , getResources().getDrawable(R.drawable.icon_content_logo));
        return contentAlert;
    }

    void OnEventRequestListener(int id, Object... args) {
        if (mEventRequestListener != null) mEventRequestListener.onEventRequest(id, args);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.layout_content_fragment_list;
    }

    public void setItems(List<BaseObject> baseObjects, Category parent, boolean hasSubCategory, boolean showLogo) {
        super.setItems(getAccessibleItems(baseObjects, parent, hasSubCategory, showLogo));
        setFeaturedItems(baseObjects);
    }

    List<BaseObject> getAccessibleItems(List<BaseObject> baseObjects, Category parent, boolean hasSubCategory, boolean showLogo) {
        if (baseObjects == null) baseObjects = new ArrayList<BaseObject>();
        if (baseObjects.size() < 1) {
            if (hasSubCategory && showLogo) baseObjects.add(getContentLogo());
            else if (!hasSubCategory)
                baseObjects.add(getContentAlert(false, null));
            return baseObjects;
        }

        List<BaseObject> list = new ArrayList<BaseObject>();
        String accessId = "";
        for (BaseObject object : baseObjects) {
            Content content = (Content) object;
            if (content.isAvailable())
                list.add(content);
            else if (!content.isUserAccess()) {
                accessId = content.access;
            }

        }
        if (list.size() != baseObjects.size()) {
            String payment = null;
            if (Text.notEmpty(accessId))
//                payment = ir.microsign.payment.network.utility.getSavedPayment("useraccesslevels=" + String.format(Locale.ENGLISH, "%d", accessId));
            if (Text.notEmpty(accessId)) mPayment = payment;
            else mPayment = null;
            list.add(getContentAlert(Text.notEmpty(accessId), payment, accessId));
        } else mPayment = null;
        return list;
    }

    @Override
    public void OnItemSelected(BaseObject selectedItem) {
        if (!(selectedItem instanceof ContentAlert))
            super.OnItemSelected(selectedItem);
    }

    void setFeaturedItems(List<BaseObject> list) {
        boolean haveFeaturedItem = false;
        List<Content> featured = new ArrayList<Content>();
        for (BaseObject content : list)
            if (content instanceof Content && ((Content) content).isFeatured()) {
                ((Content) content).mSplitText = getActivity().getIntent().getStringExtra("splittext");
                haveFeaturedItem = true;
                featured.add((Content) content);
            }

        boolean listVisibility = (haveFeaturedItem && list.size() > 1) || (!haveFeaturedItem);
        view.setVisibility(findViewById(R.id.list_view), listVisibility);
        setFeatureList(featured, !listVisibility);


    }

    void initFeaturedGroup() {
        if (ll_featured == null) {
            ll_featured = (LinearLayout) findViewById(R.id.ll_featured);
            ll_root = (LinearLayout) findViewById(R.id.ll_root);

        }
        ll_featured.removeAllViews();
    }

    void setFeatureList(List<Content> items, boolean fillParent) {
        initFeaturedGroup();
        view.setVisibility(ll_featured, items.size() > 0);
        ll_root.setOrientation(isLandscape() ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, fillParent ? -1 : -2);
        layoutParams.weight = fillParent ? 0 : 1;
        ll_featured.setLayoutParams(layoutParams);
        for (Content item : items)
            ll_featured.addView(item.getFeatureView(getContext()), new ViewGroup.LayoutParams(-1, -1));
    }

    public void setContents(Category parentCategory, Category parent, boolean hasSubCategory, boolean showLogo) {

        setItems((List<BaseObject>) getDataSource().getContents(parentCategory), parent, hasSubCategory, showLogo);
    }

//    @Override
//    public void search(String txt) {
//        super.search(txt);
//        setItems((List<BaseObject>) getDataSource().search(txt, -1, false, true, true, true));
//    }
}
