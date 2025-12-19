package ir.microsign.api.view;

import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;

import ir.microsign.adapter.SpinnerAdapter;
import ir.microsign.api.object.Ticket;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

public class EditTicketView extends BaseView {
//    TextView txtType,txtDate,txtTitle;
    EditText EtTitle;
    Spinner SpnType;


    public EditTicketView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        getLayoutInflater().inflate(R.layout.ll_dialog_edit_ticket,this,true);
        setView();
    }
    void setView(){
        EtTitle=findViewById(R.id.et_title);
        SpnType=findViewById(R.id.spn_type);
        Text.setHint(EtTitle,R.string.caption_ticket_title, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_ticket_edit_title),R.string.txt_edit_ticket_title, Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_type),R.string.txt_ticket_type, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_title),R.string.txt_title, Font.TextPos.h2);
        Text.setText(findViewById(R.id.btn_ok),R.string.txt_create, Font.TextPos.h1);
        Text.setText(findViewById(R.id.btn_cancel),R.string.txt_cancel, Font.TextPos.h1);
        SpnType.setAdapter(new SpinnerAdapter(getContext(),getContext().getResources().getStringArray(R.array.array_ticket_cats),Font.getFont(getContext(),"h2")));
//Spinner spinner=findViewById(R.id.spn_type);
//        txtTitle=findViewById(R.id.txt_message);
//        txtType=findViewById(R.id.txt_title);
//
//       txtDate.setText(getObject().getDate());
//        txtTitle.setText(getObject().title);
//        txtType.setText(getObject().title);
    }

    public Ticket getEdited(){
        Ticket ticket=new Ticket();//getObject();
        ticket.title=EtTitle.getText().toString();
        ticket.type=SpnType.getSelectedItem()==null?null:SpnType.getSelectedItem().toString();
        return ticket;
    }

    public Ticket getObject() {
        return (Ticket) super.getDbObject();
    }
}
