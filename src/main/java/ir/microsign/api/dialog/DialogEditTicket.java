package ir.microsign.api.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import ir.microsign.api.object.Ticket;
import ir.microsign.api.view.EditTicketView;
import ir.microsign.dialog.BaseDialog;
import ir.microsign.R;
import ir.microsign.utility.Display;

public class DialogEditTicket extends BaseDialog {
    EditTicketView editTicketView;
    Ticket ticket;
    public DialogEditTicket(Activity activity, Ticket ticket) {
        super(activity, null);
    }

    @Override
    public void inIt() {
//        getWindow().getAttributes().width= (int) (Display.getWidth(getContext())*.8);
        editTicketView=new EditTicketView(getContext(),ticket);
        editTicketView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emitAction("add",editTicketView.getEdited());
                hide();
            }
        });
        editTicketView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cancel();
            }
        });
        setContentView(editTicketView);
    }
    public void setLayout() {

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout((int) (Display.getWidth(getContext())*.8), Display.getHeightWithoutBar(getContext()));
    }

}
