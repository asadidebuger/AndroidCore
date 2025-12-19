package ir.microsign.api.view;

import android.content.Context;
import android.widget.TextView;

import java.util.Locale;

import ir.microsign.api.object.Ticket;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

public class TicketView extends BaseView {
    TextView txtType,txtDate, txtTitle,txtCount;
    public TicketView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.layout_ticket,this,true);
        setView();
    }
    void setView() {
//        setBackgroundResource(R.drawable.bkg_ticket);
        txtDate = findViewById(R.id.txt_date);
        txtTitle = findViewById(R.id.txt_title);
        txtType = findViewById(R.id.txt_type);
        txtCount = findViewById(R.id.txt_counter);

//        txtDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
//        txtType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        Text.setText(txtDate,getObject().getDate(), Font.TextPos.small);
        Text.setText(txtType,getObject().type, Font.TextPos.small);
        Text.setText(txtTitle,getObject().title, Font.TextPos.h2);
//        txtDate.setText(getObject().getDate());
//        txtTitle.setText(getObject().title);
//        txtType.setText(getObject().type);
        txtCount.setText(String.format(Locale.getDefault(),"%d",getObject().countMessages(getContext())));

//        txtType.setText(getObject().title);
    }


    public Ticket getObject() {
        return (Ticket) super.getDbObject();
    }
}
