package ir.microsign.api.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.Ticket;
import ir.microsign.dbhelper.adapter.BaseAdapter;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

public class TicketsView extends LinearLayout {
//    Ticket ticket;
//    Message message;
    ListView listView;
    View
            btnAdd; //,btnCancel;
//    TextView txtTitle;
//    EditText etMessage;
    public TicketsView(Context context) {
        super(context);
//        this.ticket=ticket;
        view.getLayoutInflater(getContext()).inflate(R.layout.layout_tickets,this,true);

        setView();
        refill();
    }
//    public void initFromBaseObject(BaseObject baseObject) {
//        super.initFromBaseObject(baseObject);
//        getLayoutInflater().inflate(R.layout.layout_tickets,this,true);
//        setView();

//    }
    void setView(){
        listView=findViewById(R.id.lst_tickets);
        listView.setDivider(null);

        btnAdd =findViewById(R.id.btn_add);
        Text.setText(btnAdd,R.string.txt_new_ticket, Font.TextPos.h1);
//        btnCancel =findViewById(R.id.btn_exit);
//        etMessage=findViewById(R.id.et_message);
//        txtTitle=findViewById(R.id.txt_title);
//        txtTitle.setText(R.string.txt_tickets_title);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
//        btnCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ticketListener ==null)return;
//                ticketListener.onExit();
//            }
//        });

    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        setView();
//        refill();
//    }

    void add(){
//       Ticket ticket =getTicket();
        if (ticketListener ==null)return;
        ticketListener.onAdd();//(ticket);
    }
Ticket getTicket(){
    Ticket  ticket=new Ticket();
//     ticket.title=etMessage.getText().toString();
//      message.ticket=ticket._id;
        return ticket;
}
    List<BaseObject> list;
    public void refill(){
        list=(List<BaseObject>) DataSourceApi.getSource(getContext()).getTickets();
       BaseAdapter baseAdapter=new BaseAdapter(getContext(),list );
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ticketListener==null)return;
                ticketListener.onClicked(((TicketView)view).getObject());
            }
        });
    }
public List<BaseObject> getTickets(){
    return list;
}
//    public Ticket getObject() {
//        return (Ticket) super.getDbObject();
//    }

    ticketChatListener ticketListener;

    public void setTicketListener(ticketChatListener ticketListener) {
        this.ticketListener = ticketListener;
    }

   public interface ticketChatListener{
//        void onSend(Ticket ticket);
        void onClicked(Ticket ticket);
        void onAdd();
        void onExit();

    }
}
