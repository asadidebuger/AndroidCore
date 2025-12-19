package ir.microsign.api.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.Message;
import ir.microsign.api.object.Ticket;
import ir.microsign.dbhelper.adapter.BaseAdapter;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

public class MessagesView extends LinearLayout {
    Ticket ticket;
//    TextView txtTitle;
    //    Message message;
    ListView listView;
    View btnSend;
    EditText etMessage;

    public MessagesView(Context context, Ticket ticket) {
        super(context);
        this.ticket = ticket;
        view.getLayoutInflater(getContext()).inflate(R.layout.layout_messages, this, true);
    }

    //    public void initFromBaseObject(BaseObject baseObject) {
//        super.initFromBaseObject(baseObject);
//        getLayoutInflater().inflate(R.layout.layout_tickets,this,true);
//        setView();
//    }
    void setView() {
        listView = findViewById(R.id.lst_messages);
//        listView.setDividerHeight(20);
        listView.setDivider(null);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                File.openFile(getContext(),((MessageView)view).getObject().getFileLocalPath(getContext(),),getContext().getString(R.string.txt_open_file_chooser_title));
//
//            }
//        });
        btnSend = findViewById(R.id.btn_send);


        etMessage = findViewById(R.id.et_message);Text.setText(btnSend,R.string.txt_send, Font.TextPos.h1);
        Text.setHint(etMessage,R.string.caption_message, Font.TextPos.p);
//        txtTitle = findViewById(R.id.txt_title);
//        txtTitle.setText(ticket.title);
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

    }

    //boolean fromUser(){
//        return getMessage().fromUser==null||getMessage().fromUser;
//}
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setView();
        refill();
    }

    void send() {
        Message message = getMessage();
        if (Text.isNullOrEmpty(message.message) || messageListener == null) return;
        messageListener.onSend(message);
    }

    public Message getMessage() {
        Message message = new Message();
        message.message = etMessage.getText().toString();
        message.ticket = ticket._id;
        return message;
    }
public void emptyText(){
        etMessage.setText(null);
}
List<BaseObject> list;

    public List<BaseObject> getMessages() {
        return list;
    }

    public void refill() {//DataSourceApi.getSource(getContext()).select(new Message());
        list=(List<BaseObject>) DataSourceApi.getSource(getContext()).getMessages(ticket._id);
        BaseAdapter baseAdapter = new BaseAdapter(getContext(), list);
        listView.setAdapter(baseAdapter);
    }

//    public Ticket getObject() {
//        return (Ticket) super.getDbObject();
//    }

    messageListener messageListener;

    public void setMessageListener(messageListener messageListener) {
        this.messageListener = messageListener;
    }

    public interface messageListener {
        void onSend(Message message);

    }
}
