package ir.microsign.api.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import ir.microsign.adapter.SpinnerAdapter;
import ir.microsign.api.Utils;
import ir.microsign.api.object.Profile;
import ir.microsign.context.Preference;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

public class ProfileDataView extends BaseView {

    int minRecoveryCodeLen;

    public void setMinRecoveryCodeLen(int minRecoveryCodeLen) {
        this.minRecoveryCodeLen = minRecoveryCodeLen;
        etVerifyCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(minRecoveryCodeLen)});
    }


    public enum Mode {
    fill,verify,ok
}
View llEmail,llPhone,llName, llVerify,llUsername,llCode,llData;
EditText etPhone,etEmail,etFirst,etLast, etVerifyCode,etCode;
TextView txtTitle, txtVerifyCode, txtPassword,txtCode,txtTimer;
//Switch swUsePhone, chShowPass;
Button btnOk;
   Spinner spnCode,spnSex;
      Mode mode;
    public ProfileDataView(Context context, BaseObject baseObject) {
        super(context, baseObject);
    }

    long getTimeOut(){
		return Preference.getLong(getContext(),"validation_code_timeout", 0L);
	}
    @Override
    public void initFromBaseObject(BaseObject baseObject) {
		super.initFromBaseObject(baseObject);

		long timeout = getTimeOut();

		getLayoutInflater().inflate(R.layout.layout_profile_data, this, true);
		llEmail = findViewById(R.id.ll_use_email);
		llPhone = findViewById(R.id.ll_use_phone);
		llCode = findViewById(R.id.ll_use_code);
		llName = findViewById(R.id.ll_name);
		llVerify = findViewById(R.id.ll_verify);
		llData = findViewById(R.id.ll_data);
		llUsername = findViewById(R.id.ll_username);

		etPhone = findViewById(R.id.et_phone);
		etEmail = findViewById(R.id.et_email);
		etCode = findViewById(R.id.et_code);
		etFirst = findViewById(R.id.et_first);
		etLast = findViewById(R.id.et_last);
		etVerifyCode = findViewById(R.id.et_verify_code);

		spnCode = findViewById(R.id.spinner_code);
		spnSex = findViewById(R.id.spinner_sex);

		btnOk = findViewById(R.id.btn_ok);

		txtTitle = findViewById(R.id.txt_title);
		txtTimer = findViewById(R.id.txt_timer);

		txtVerifyCode = findViewById(R.id.txt_verify_code);
		spnCode.setAdapter(new SpinnerAdapter(getContext(), getContext().getResources().getStringArray(R.array.array_country_code), Font.getFont(getContext(), Font.TextPos.h3)));
		spnSex.setAdapter(new SpinnerAdapter(getContext(), getContext().getResources().getStringArray(R.array.array_sex), Font.getFont(getContext(), Font.TextPos.h3)));


		Text.setHint(etPhone, R.string.caption_phone, Font.TextPos.h2);
		Text.setHint(etEmail, R.string.caption_email, Font.TextPos.h2);
		Text.setHint(etCode, R.string.caption_code, Font.TextPos.h2);
		Text.setHint(etFirst, R.string.caption_name, Font.TextPos.h2);
		Text.setHint(etLast, R.string.caption_lastname, Font.TextPos.h2);
		Text.setHint(etVerifyCode, R.string.caption_recover_code, Font.TextPos.h2);

		Text.setText(txtVerifyCode, R.string.txt_verify_code, Font.TextPos.h2);

		Text.setText(findViewById(R.id.txt_name), R.string.txt_name, Font.TextPos.h2);
		Text.setText(findViewById(R.id.txt_mobile_num), R.string.txt_mobile_num, Font.TextPos.h2);
		Text.setText(findViewById(R.id.txt_email), R.string.txt_email, Font.TextPos.h2);
		Text.setText(findViewById(R.id.txt_code), R.string.txt_code, Font.TextPos.h2);
		Text.setText(findViewById(R.id.txt_timer_title), R.string.txt_timer_title, Font.TextPos.h2);

		if (Profile.isSignIn())
			setMode(timeout < System.currentTimeMillis() ? Mode.fill : Mode.verify);
		else {
			if (onConfirmListener != null) onConfirmListener.needLogin();
		}
//        else  setMode(Mode.signIn);
	}
//    static  class spnAdapter extends SpinnerAdapter
void setTitle(int id){
    Text.setText(txtTitle,getContext().getString(id), Font.TextPos.h1,1.2f,true);

}
void setBtnTitle(int id){
    Text.setText(btnOk,getContext().getString(id), Font.TextPos.h1);

}

    void setView() {
    	if (timer!=null){
    		try {
				timer.cancel();
				timer=null;
			}
			catch (Exception ex){
    			timer=null;
			}

		}
        LinearLayout llFace= findViewById(R.id.ll_face);
        llFace.removeAllViews();

        if (mode == Mode.fill) {
           llFace.addView(getObject().getProfileImageView(getContext()),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            setTitle(R.string.txt_title_profile_data);
            setBtnTitle(R.string.btn_reg_data);

            llVerify.setVisibility(GONE);
            llData.setVisibility(VISIBLE);

        } else {
			setTitle(R.string.txt_title_profile_data_verify);
			setBtnTitle(ir.microsign.R.string.ok);
			llVerify.setVisibility(VISIBLE);
			llData.setVisibility(GONE);

			timer=	new CountDownTimer(getTimeOut()-System.currentTimeMillis(),1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					try {
						millisUntilFinished=Long.valueOf(millisUntilFinished/1000).intValue();
						txtTimer.setText(String.format(Locale.US,"%d:%d",millisUntilFinished/60,millisUntilFinished%60));
					}catch (Exception ex){
						try {
							timer.cancel();
							timer=null;
						}catch (Exception e){
							timer=null;
						}

					}

				}

				@Override
				public void onFinish() {
					setMode(Mode.fill);
				}
			};
			timer.start();


            }

            btnOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOk();
                }
            });
            initProfile();




    }

    static CountDownTimer timer=null;

    public void initProfile() {
        Profile profile = getObject();
        etFirst.setText(profile.first);
        etLast.setText(profile.last);
        etEmail.setText(profile.email);
        etCode.setText(profile.code);
        etPhone.setText(profile.getPurePhone());
        spnSex.setSelection(profile.getSexIndex());
    }
	void submit(){
		initProfile();

	}
    void onOk() {
        if (onConfirmListener == null) return;
        Profile profile=new Profile();
//        if (mode != Mode.newPass) {
//            profile= new Profile();
            profile.first = etFirst.getText().toString();
            profile.last = etLast.getText().toString();
            profile.email = etEmail.getText().toString();
            profile.code = etCode.getText().toString();
            profile.setSexFromIndex(spnCode.getSelectedItemPosition());

            String countryCode = spnCode.getSelectedItem().toString();
            String tel = etPhone.getText().toString();


		if (onConfirmListener != null) {
			if (mode==Mode.fill)
			{
				if (Text.notEmpty(profile.email)&&!profile.emailIsValid()) {
					onConfirmListener.invalidEmail(profile.email);
					return;
				}
				if (Text.notEmpty(profile.code)&&!profile.codeIsValid()) {
					onConfirmListener.invalidCode(profile.code);
					return;
				}
				if (Text.notEmpty(profile.tel)&&!profile.phoneIsValid(tel)) {
					onConfirmListener.invalidPhone(tel);
					return;
				}
				profile.setPhone(countryCode, tel);
				onConfirmListener.filled(profile);
			}

			else {
//				NumberFormat nf=NumberFormat.getInstance(Locale.US);
//				nf.format(i);
//				etVerifyCode.setTextLocale(Locale.US);
				onConfirmListener.codeEntered(getObject(),Utils.CurrectDigits(etVerifyCode.getText().toString()));
			}
		}
    }




    public void setMode(Mode mode) {
        this.mode = mode;
        setView();
    }
public void setProfile(Profile profile)
{
    mBaseObject=profile;
}
    public Mode getMode() {
        return mode;
    }

    //    boolean doublePass=true;
ProfileView.onConfirmListener onConfirmListener;

    public void setOnConfirmListener(ProfileView.onConfirmListener l) {
        this.onConfirmListener = l;
    }

    public Profile getObject(){
        return (Profile) getDbObject();
    }


}
