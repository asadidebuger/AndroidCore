package ir.microsign.api.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import ir.microsign.api.Api;
import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.dialog.DialogEditTicket;
import ir.microsign.api.object.Message;
import ir.microsign.api.object.NetErrorMiddleware;
import ir.microsign.api.object.Response;
import ir.microsign.api.object.Ticket;
import ir.microsign.api.view.MessagesView;
import ir.microsign.api.view.TicketsView;
import ir.microsign.calendar.Calendar;
import ir.microsign.context.Application;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dialog.BaseAlterDialog;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

public class ActivityTicket extends Activity {
    public static void show(Context context){
        Intent intent=new Intent(context,ActivityTicket.class);
        context.startActivity(intent);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Api.addOnExceptionListener(this.getClass().getName(),new NetErrorMiddleware(this));
        updateTickets(false);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Api.removeOnExceptionListener(this.getClass().getName());

    }
//    ListView listView=null;
    TicketsView ticketsView =null;
    MessagesView messagesView =null;
LinearLayout llRoot=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar.setContext(Application.getContext());
		setContentView(R.layout.layout_activity_ticket);
		llRoot = findViewById(R.id.ll_root);
		findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		findViewById(ir.microsign.R.id.img_header_icon1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (messagesView != null) updateMessages(messagesView.getMessage(), true);
				else updateTickets(true);

			}
		});
//        File.copyDatabases(this);
//        listView=findViewById(R.id.lst_tickets);

		initView();
	}



    @Override
    public void setTitle(int titleId) {
        Text.setText(findViewById(R.id.txt_title),titleId, Font.TextPos.h1,false);    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        Text.setText(findViewById(R.id.txt_title),title.toString(), Font.TextPos.h1,false);
    }


    public void setContent(View view) {

        llRoot.removeAllViews();
        llRoot.addView(view,-1,-1);
//        super.setContentView(view);

    }

    void initView(){
setTitle(R.string.txt_tickets_title);
        ticketsView =new TicketsView(this);
        ticketsView.setTicketListener(new TicketsView.ticketChatListener() {
//            @Override
//            public void onSend(Ticket ticket) {
//                send(ticket);
//            }

            @Override
            public void onClicked(Ticket ticket) {
                showMessages(ticket);
            }

            @Override
            public void onAdd() {
                addTicket();
            }

            @Override
            public void onExit() {
                finish();
            }

        });
        setContent(ticketsView);
//        ticketsView.refill();
        messagesView=null;

    }
    void addTicket(){
        DialogEditTicket editTicket=new DialogEditTicket(this,new Ticket());
        editTicket.show();
        editTicket.setOnActionListener(new BaseAlterDialog.onActionListener() {
            @Override
            public void onAction(String action, Object data) {
                send((Ticket)data);
            }
        });
    }
//    void SetAdapter(){
//       BaseAdapter baseAdapter=new BaseAdapter(this, (List<BaseObject>) DataSourceApi.getSource(this).select(new Ticket()));
//       listView.setAdapter(baseAdapter);
//    }

    void showMessages(Ticket ticket){
        setTitle(ticket.title);
//        if (messagesView!=null)
        messagesView=new MessagesView(this,ticket);
        messagesView.setMessageListener(new MessagesView.messageListener() {
            @Override
            public void onSend(Message message) {
                send(message);
            }
        });
        setContent(messagesView);

        updateMessages(messagesView.getMessage(),false);
//        messagesView.refill();
    }

    @Override
    public void onBackPressed() {

        if (messagesView==null)
        super.onBackPressed();
        else {
            initView();
        }
    }

    void send(Ticket ticket){
        if (Text.isNullOrEmpty(ticket.title))return;
        Api.postTicket(ticket, new Api.onResponseListener() {
            @Override
            public void onResponse(final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        updateTickets();
                        if (!response.succeed())return;
                      Ticket ticket1=  response.getObject(Ticket.class,Api.FUNC.Ticket.toString());
                        getDataSource().insert(ticket1);
                        showMessages(ticket1);
                    }
                });
            }
        });
    }
    void send(final Message message){
        Api.postMessage(message, new Api.onResponseListener() {
            @Override
            public void onResponse(final Response response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.succeed()){
                            messagesView.emptyText();
                        }
                        updateMessages(message,true);

                    }
                });
            }
        });
    }
    void updateTickets(boolean force){
//        Long now=java.util.Calendar.getInstance().getTimeInMillis();
        if (!force
                &&ticketsView.getTickets().size()>0
                &&java.util.Calendar.getInstance().getTimeInMillis()-((Ticket)ticketsView.getTickets().get(0)).last_pdate<1000*60*3)
            return;
        Api.getTickets(new Api.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (!response.succeed())return;
//               DataSourceApi.getSource(ActivityTicket.this).autoInsertUpdateByColumn(response.getObjectArray(Ticket.class,Api.FUNC.Ticket.toString()),"_id");
               List<BaseObject> ticketList= response.getObjectArray(Ticket.class,Api.FUNC.Ticket.toString());
                Long now=java.util.Calendar.getInstance().getTimeInMillis();
                for (BaseObject baseObject : ticketList) {
                    ((Ticket)baseObject).last_pdate= now;
                }
                getDataSource().cleanAndInsertObjects(new Ticket().getTableName(),ticketList);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if (ticketsView!=null)
                       ticketsView.refill();
//                       SetAdapter();
                   }
               });
            }
        });
    }
    public DataSourceApi getDataSource(){
        return DataSourceApi.getSource(this);
    }
    void updateMessages(final Message message, boolean force){
        if (!force
                &&messagesView.getMessages().size()>0
                &&java.util.Calendar.getInstance().getTimeInMillis()-((Message)messagesView.getMessages().get(0)).last_pdate<1000*60*3)
            return;
//        Message message=new Message();
//        message.ticket=ticket._id;
        if (messagesView==null)return;
        Api.getMessages(message,new Api.onResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (!response.succeed())return;
//               DataSourceApi.getSource(ActivityTicket.this).autoInsertUpdateByColumn(response.getObjectArray(Message.class,Api.FUNC.Message.toString()),"_id");
                List<BaseObject> messageList= response.getObjectArray(Message.class,Api.FUNC.Message.toString());
                Long now=java.util.Calendar.getInstance().getTimeInMillis();
                for (BaseObject baseObject : messageList) {
                    ((Message)baseObject).last_pdate= now;
                }
               DataSourceApi.getSource(ActivityTicket.this).insertMessages(messageList,message.ticket);

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if (messagesView==null)return;
                       messagesView.refill();
//                       SetAdapter();
                   }
               });
            }
        });
    }
    void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.showToast(ActivityTicket.this, msg);
            }
        });


    }

    void showMessage(int msg) {
        showMessage(getString(msg));
    }
}
