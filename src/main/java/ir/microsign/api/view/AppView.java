package ir.microsign.api.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ir.microsign.api.object.App;
import ir.microsign.api.object.Market;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

public class AppView extends BaseView {

    public AppView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_app,this,true);
        setView();
    }
    void setView() {
//        App app=getObject();

//        ViewHtml viewHtml=findViewById(R.id.html);
//        viewHtml.setContent(app.getHtml(getContext()));
        Text.setText(findViewById(R.id.txt_title),getObject().name, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_desc),getObject().getDescription(getContext()), Font.TextPos.h3);
        App.setImage((ImageView) findViewById(R.id.img_image),findViewById(R.id.prg_wait),getObject().getIconUrl(),getObject().getIconSavePath(getContext()));

        LinearLayout llMarket=findViewById(R.id.ll_markets);
        List<Market> markets=getObject().getMarkets();
        for (Market market : markets) {
            if(Text.isNullOrEmpty(market.url))continue;
            TextView btn=new TextView(getContext());
            btn.setBackgroundResource(R.drawable.bkg_link);
            btn.setTag(market);
            btn.setOnClickListener(onMarketClicked);
            Text.setText(btn,market.name, Font.TextPos.h3);

            llMarket.addView(btn,-2,-2);
        }

    }
static OnClickListener onMarketClicked=new OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.e("j",v.toString());
      Market market= (Market) v.getTag();
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(market.url));
        v.getContext().startActivity(intent);
    }
};

    public App getObject() {
        return (App) super.getDbObject();
    }
}
