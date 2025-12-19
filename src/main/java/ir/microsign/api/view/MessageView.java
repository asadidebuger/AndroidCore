package ir.microsign.api.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.Message;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

public class MessageView extends BaseView {
    TextView txtTitle,txtDate,txtMessage,txtUser;
    LinearLayout imgFile;
    public MessageView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }
View child;
    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
        child= getLayoutInflater().inflate(R.layout.layout_message,this,false);
      addView(child,-1,-2);
      setPadding(fromUser()?60:10,20,fromUser()?10:60,5);
        setView();
    }
    void setView() {
        try {


            txtDate = findViewById(R.id.txt_date);
            txtUser = findViewById(R.id.txt_user);
//        txtFileDesc = findViewById(R.id.txt_file_desc);
            imgFile = findViewById(R.id.ll_file);
            txtMessage = findViewById(R.id.txt_message);
            txtTitle = findViewById(R.id.txt_title);
            Text.setText(txtDate, getObject().getDate(), Font.TextPos.small);
            Text.setText(txtMessage, getObject().message, Font.TextPos.p);
//        txtDate.setText(getObject().getDate());
//        txtDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
//        txtMessage.setText(getObject().message);
            child.setBackgroundResource(fromUser() ? R.drawable.bkg_message : R.drawable.bkg_message1);
            getObject().setImage(imgFile);
//        txtFileDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//        txtFileDesc.setText(getObject().getFileDescription());
            if (getObject().fromUser) {
//            txtUser.setText();
                Text.setText(txtUser, DataSourceApi.getSource(getContext()).getProfile().getFullName(), Font.TextPos.small);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        imgFile.setOnClickListener(onClickListener);
//        txtFileDesc.setOnClickListener(onClickListener);
//        txtType.setText(getObject().title);
    }
//OnClickListener onClickListener=new OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        File.openFile(getContext(),getObject().getFileLocalPath(getContext()),getString(R.string.txt_open_file_chooser_title));
//    }
//};
    boolean fromUser(){
        return getObject().fromUser==null||getObject().fromUser;
}
    public Message getObject() {
        return (Message) super.getDbObject();
    }
}
