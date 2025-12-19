package ir.microsign.api.view;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import ir.microsign.adapter.SpinnerAdapter;
import ir.microsign.api.object.App;
import ir.microsign.api.object.Profile;
import ir.microsign.api.object.Recover;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.dbhelper.view.BaseView;
import ir.microsign.dialog.MessageDialog;
import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;

public class ProfileView extends BaseView {

    int minRecoveryCodeLen;

    public void setMinRecoveryCodeLen(int minRecoveryCodeLen) {
        this.minRecoveryCodeLen = minRecoveryCodeLen;
        etRecoverCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(minRecoveryCodeLen)});
    }

    public enum Mode {
    signUp,
    signIn,
    recover,
    signOut,
    update,
    newPass
}
View llPass,llPass1,llPass2,llEmail,llPhone,llSignOut,llSignIn,llName,llRecover,llUsername,llCode;
EditText etPass1,etPass2,etPhone,etEmail,etFirst,etLast, etRecoverCode,etCode;
TextView txtHello, txtSignIn,txtRecoverPassword,txtCompleteProfile,txtSignUp,txtUpdate,txtTitle,txtRecoverEnterCode, txtPassword,txtCode,txtLicense;
//Switch swUsePhone, chShowPass;
Button btnOk,btnSignOut;
   Spinner spnCode;
   CheckBox chAgree, chShowPass;
   RadioGroup rdbGroupUser;
   RadioButton rdbEmail,rdbCode, rdbTel;
    Mode mode;

    boolean allowEmail,allowCode,allowMobile;
    Activity mActivity;

//	public void setActivity(Activity mActivity) {
//		this.mActivity = mActivity;
//	}

	public ProfileView(Context context, BaseObject baseObject) {
        super(context, baseObject);
		if (context instanceof Activity) mActivity= (Activity) context;

    }

    @Override
    public void initFromBaseObject(BaseObject baseObject) {
        super.initFromBaseObject(baseObject);
		allowCode=getResources().getBoolean(R.bool.login_allow_code);
		allowEmail=getResources().getBoolean(R.bool.login_allow_email);
		allowMobile=getResources().getBoolean(R.bool.login_allow_mobile);
        getLayoutInflater().inflate(R.layout.layout_login,this,true);
        llSignOut=findViewById(R.id.ll_sign_out);
        llSignIn=findViewById(R.id.ll_sign_in);
        llPass=findViewById(R.id.ll_pass);
        llPass1=findViewById(R.id.ll_pass1);
        llPass2=findViewById(R.id.ll_pass2);
        llEmail=findViewById(R.id.ll_use_email);
        llPhone=findViewById(R.id.ll_use_phone);
        llCode=findViewById(R.id.ll_use_code);
        llName=findViewById(R.id.ll_name);
        llRecover=findViewById(R.id.ll_recover);
        llUsername=findViewById(R.id.ll_username);


        etPass1=findViewById(R.id.et_password);
        etPass2=findViewById(R.id.et_password2);
        etPhone=findViewById(R.id.et_phone);
        etEmail=findViewById(R.id.et_email);
        etCode=findViewById(R.id.et_code);
        etFirst=findViewById(R.id.et_first);
        etLast=findViewById(R.id.et_last);
        etRecoverCode =findViewById(R.id.et_recover_code);

        chAgree=findViewById(R.id.chbx_aggree);
        spnCode = findViewById(R.id.spinner_code);
//        swUsePhone = findViewById(R.id.switch_use_phone);
        rdbCode = findViewById(R.id.rdb_use_code);
        rdbEmail = findViewById(R.id.rdb_use_email);
        rdbTel = findViewById(R.id.rdb_use_tel);




        rdbGroupUser = findViewById(R.id.rdb_group_user);
        chShowPass = findViewById(R.id.switch_show_pass);
        btnOk= findViewById(R.id.btn_ok);
        btnSignOut= findViewById(R.id.btn_sign_out);
        txtHello=findViewById(R.id.txt_hello);
        txtTitle=findViewById(R.id.txt_title);
        txtPassword =findViewById(R.id.txt_password);
        txtSignIn =findViewById(R.id.txt_sign_in);
        txtSignUp=findViewById(R.id.txt_sign_up);
        txtLicense=findViewById(R.id.txt_license);
        txtRecoverPassword=findViewById(R.id.txt_recover_pass);
        txtCompleteProfile=findViewById(R.id.txt_complete_profile);
        txtUpdate=findViewById(R.id.txt_update);
        txtRecoverEnterCode=findViewById(R.id.txt_recover_enter_code);
        spnCode.setAdapter(new SpinnerAdapter(getContext(),getContext().getResources().getStringArray(R.array.array_country_code),Font.getFont(getContext(),Font.TextPos.h3)));
//        spnCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TExv.setcolo
//            }
//        });
        Text.setHint(etPass1,R.string.caption_password, Font.TextPos.h2);
        Text.setHint(etPass2,R.string.caption_password_repeat, Font.TextPos.h2);
        Text.setHint(etPhone,R.string.caption_phone, Font.TextPos.h2);
        Text.setHint(etEmail,R.string.caption_email, Font.TextPos.h2);
        Text.setHint(etCode,R.string.caption_code, Font.TextPos.h2);
        Text.setHint(etFirst,R.string.caption_name, Font.TextPos.h2);
        Text.setHint(etLast,R.string.caption_lastname, Font.TextPos.h2);
        Text.setHint(etRecoverCode,R.string.caption_recover_code, Font.TextPos.h2);

//        Text.setText(swUsePhone,R.string.txt_use_phone, Font.TextPos.h2);
        Text.setText(rdbCode,R.string.rdb_use_code, Font.TextPos.h2);
        Text.setText(rdbEmail,R.string.rdb_use_email, Font.TextPos.h2);
        Text.setText(rdbTel,R.string.rdb_use_tel, Font.TextPos.h2);
//        Text.setText(chAgree,R.string.txt_agree, Font.TextPos.h2);
        Text.setText(chAgree,R.string.txt_agree, Font.TextPos.h2);
        Text.setText(chShowPass,R.string.txt_show_password, Font.TextPos.h2);
        Text.setText(btnSignOut,R.string.txt_sign_out, Font.TextPos.h1);
        Text.setText(txtSignIn,R.string.txt_sign_in, Font.TextPos.h3);
        Text.setText(txtLicense,R.string.txt_license_title, Font.TextPos.h3);
        Text.setText(txtSignUp,R.string.txt_sign_up, Font.TextPos.h1);
//		txtSignUp.setTextColor(Color.GREEN);
        Text.setText(txtRecoverPassword,R.string.txt_forgot_pass, Font.TextPos.h3);
        Text.setText(txtCompleteProfile,R.string.txt_complete_profile, Font.TextPos.h3);
        Text.setText(txtUpdate,R.string.txt_sign_update, Font.TextPos.h3);
        Text.setText(txtRecoverEnterCode,R.string.txt_recover_enter_code, Font.TextPos.h3);



        Text.setText(findViewById(R.id.txt_name),R.string.txt_name, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_mobile_num),R.string.txt_mobile_num, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_email),R.string.txt_email, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_code),R.string.txt_code, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_recover_code),R.string.txt_recover_code, Font.TextPos.h2);
        Text.setText(findViewById(R.id.txt_password2),R.string.txt_password_repeat, Font.TextPos.h2);

        view.setVisibility(rdbEmail,allowEmail);
		view.setVisibility(rdbCode,allowCode);
		view.setVisibility(rdbTel,allowMobile);

		OnClickListener onClickListener=new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.equals(txtSignIn)) {
                    setMode(Mode.signIn);
                    return;
                }
                if (view.equals(txtRecoverPassword)) {
                    setMode(Mode.recover);
                    return;
                }
                if (view.equals(txtSignUp)) {
                    setMode(Mode.signUp);
                    return;
                }
                if (view.equals(txtUpdate)) {
                    setMode(Mode.update);
                    return;
                }
                if (view.equals(txtLicense)) {
                    App.updateApps(mActivity, false, new App.updateListener() {
						@Override
						public void ok() {
							MessageDialog.showMessage(getContext(),getString(R.string.txt_license_title),App.getApp(getContext()).license);
						}

						@Override
						public void failed() {

						}
					});
                    return;
                }
                if (view.equals(txtRecoverEnterCode)) {
                    setMode(Mode.newPass);
                    return;
                }
                if (view.equals(txtCompleteProfile)) {
                	if (onConfirmListener!=null)onConfirmListener.fillMode();
//                    setMode(Mode.signIn);
                }

            }
        };
        txtSignIn.setOnClickListener(onClickListener);
        txtSignUp.setOnClickListener(onClickListener);
        txtLicense.setOnClickListener(onClickListener);
        txtRecoverPassword.setOnClickListener(onClickListener);
        txtCompleteProfile.setOnClickListener(onClickListener);
        txtUpdate.setOnClickListener(onClickListener);
        txtRecoverEnterCode.setOnClickListener(onClickListener);

//        if (mode==null)mode=Mode.signUp;
//        setView();
        if (getObject().isSignIn())
        setMode(Mode.signOut);
        else  setMode(Mode.signIn);
    }
//    static  class spnAdapter extends SpinnerAdapter
void setTitle(int id){
    Text.setText(txtTitle,getContext().getString(id), Font.TextPos.h1,1.2f,true);

}void setBtnTitle(int id){
    Text.setText(btnOk,getContext().getString(id), Font.TextPos.h1);

}
    void setView() {
        LinearLayout llFace= findViewById(R.id.ll_face);
        llFace.removeAllViews();

        if (mode == Mode.signOut) {
           llFace.addView(getObject().getProfileImageView(getContext()),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            llName.setVisibility(VISIBLE);
            llSignOut.setVisibility(VISIBLE);
            llSignIn.setVisibility(GONE);
            Text.setText(txtHello,getContext().getString(R.string.txt_hello, getObject().getFullName()), Font.TextPos.h1,1.2f,true);
            setTitle(R.string.txt_login_title_sign_out);
            txtCompleteProfile.setVisibility(VISIBLE);
            txtSignUp.setVisibility(GONE);
            txtRecoverPassword.setVisibility(GONE);
            txtUpdate.setVisibility(VISIBLE);
            txtSignIn.setVisibility(GONE);
            txtRecoverEnterCode.setVisibility(GONE);
            btnSignOut.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onConfirmListener != null) onConfirmListener.signOut(getObject());
                }
            });
        } else {
            llSignOut.setVisibility(GONE);
            llSignIn.setVisibility(VISIBLE);
            switch (mode) {
                case signIn:
                    llName.setVisibility(GONE);
                    llPass.setVisibility(VISIBLE);
                    txtCompleteProfile.setVisibility(GONE);
                    txtSignUp.setVisibility(VISIBLE);
                    txtRecoverPassword.setVisibility(VISIBLE);
                    txtUpdate.setVisibility(GONE);
                    txtSignIn.setVisibility(GONE);
                    setBtnTitle(R.string.txt_sign_in);
                    setTitle(R.string.txt_login_title_sign_in);
                    break;
                case recover:
                    llName.setVisibility(GONE);
                    llPass.setVisibility(GONE);
                    txtCompleteProfile.setVisibility(GONE);
                    txtSignUp.setVisibility(VISIBLE);
                    txtRecoverPassword.setVisibility(GONE);
                    txtUpdate.setVisibility(GONE);
                    txtSignIn.setVisibility(VISIBLE);
                    setTitle(R.string.txt_login_title_recover);
                    setBtnTitle(R.string.txt_sign_recover_receive_code);
                    break;
                case newPass:
                    llName.setVisibility(GONE);
                    llPass.setVisibility(VISIBLE);
                    txtCompleteProfile.setVisibility(GONE);
                    txtSignUp.setVisibility(VISIBLE);
                    txtRecoverPassword.setVisibility(GONE);
                    txtUpdate.setVisibility(GONE);
                    txtSignIn.setVisibility(VISIBLE);
                    setBtnTitle(R.string.txt_sign_recover);
                    setTitle(R.string.txt_login_title_recover);
                    break;
                case signUp:
                    llName.setVisibility(VISIBLE);
                    llPass.setVisibility(VISIBLE);
                    txtCompleteProfile.setVisibility(GONE);
                    txtSignUp.setVisibility(GONE);
                    txtRecoverPassword.setVisibility(VISIBLE);
                    txtUpdate.setVisibility(GONE);
                    txtSignIn.setVisibility(VISIBLE);
//                    btnOk.setText(R.string.txt_sign_up);
//                    txtTitle.setText(R.string.txt_login_title_sign_up);
                    setBtnTitle(R.string.btn_sign_up);
                    setTitle(R.string.txt_login_title_sign_up);
                    break;
                case update:
//                    llEmail.setVisibility(INVISIBLE);
                    llName.setVisibility(VISIBLE);
                    llPass.setVisibility(VISIBLE);
                    txtCompleteProfile.setVisibility(VISIBLE);
                    txtSignUp.setVisibility(GONE);
                    txtRecoverPassword.setVisibility(GONE);
                    txtUpdate.setVisibility(GONE);
                    txtSignIn.setVisibility(GONE);
//                    txtTitle.setText(R.string.txt_login_title_update);
//                    btnOk.setText(R.string.txt_sign_update);
                    setBtnTitle(R.string.txt_sign_update);
                    setTitle(R.string.txt_login_title_update);
                    break;
//                case signOut:btnOk.setText(R.string.txt_sign_);
//                    break;
            }
            llPass2.setVisibility(isDoublePass() ? VISIBLE : GONE);

            Text.setHint(etPass1,mode==Mode.newPass||mode==Mode.update ?R.string.caption_password_new:R.string.caption_password, Font.TextPos.h2);
            Text.setText(txtPassword,mode==Mode.newPass ?R.string.txt_password_new:R.string.txt_password, Font.TextPos.h2);
            llRecover.setVisibility(mode==Mode.newPass ? VISIBLE : GONE);
//            llSignOut.setVisibility(GONE);
//            llSignIn.setVisibility(INVISIBLE);
            if (mode == Mode.recover) {
                llPass.setVisibility(mode == Mode.recover ? GONE : VISIBLE);

            }
            llUsername.setVisibility(mode == Mode.update?GONE:VISIBLE);
            txtRecoverEnterCode.setVisibility(mode==Mode.recover?VISIBLE:GONE);
            chAgree.setVisibility(mode==Mode.signUp?VISIBLE:GONE);
            rdbGroupUser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Profile.UserNameType userNameType =Profile.UserNameType.email;
                    if (checkedId==R.id.rdb_use_code) userNameType =Profile.UserNameType.code;
                    else if (checkedId==R.id.rdb_use_tel) userNameType =Profile.UserNameType.tel;
                    setUserNameType(userNameType);
                }
            });
//            swUsePhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    setUserNameType(b);
//                }
//            });
            chShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    setShowPassword(b);
                }
            });
            chShowPass.setChecked(false);
            btnOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOk();
                }
            });
            initProfile();
        }
    }
    void setShowPassword(boolean showPassword){
        if (showPassword){
            llPass2.setVisibility(GONE);
            etPass1.setTransformationMethod(null);
            return;
        }
            etPass1.setTransformationMethod(new PasswordTransformationMethod());

        if(isDoublePass()){
            llPass2.setVisibility(VISIBLE);

        }
    }
    public void initProfile() {
        Profile profile = getObject();
        etFirst.setText(profile.first);
        etLast.setText(profile.last);
        etEmail.setText(profile.email);
        etCode.setText(profile.code);
        etPhone.setText(profile.getPurePhone());
        etPass1.setText(profile.password);
        etPass2.setText(profile.password);
        setUserNameType(profile.getUserNameType());
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
            String countryCode = spnCode.getSelectedItem().toString();
            String tel = etPhone.getText().toString();
            switch (userName())
            {
                case email:
                    if (!profile.emailIsValid()) {
                        onConfirmListener.invalidEmail(profile.email);
                        return;
                    }
                    break;
                case code:
                    if (!profile.codeIsValid()) {
                        onConfirmListener.invalidCode(profile.code);
                        return;
                    }
                    break;
                case tel:
                    if (!profile.phoneIsValid(tel)) {
                        onConfirmListener.invalidPhone(profile.tel);
                        return;
                    }
                    profile.setPhone(countryCode, tel);
                    break;
            }

//        }
            if (mode != Mode.recover) {
                String pass1 = etPass1.getText().toString();
                if (Text.isNullOrEmpty(pass1)) {
                    onConfirmListener.emptyPass();
                    return;
                }
                if (pass1.length() < 4) {
                    onConfirmListener.lowPass(4);
                    return;
                }
                String pass2 = etPass2.getText().toString();
                if (isDoublePass() && !pass1.equals(pass2) && !chShowPass.isChecked()) {
                    onConfirmListener.passNotSame();
                    return;
                }

                profile.password = pass1;
            }
            if (mode == Mode.signUp && !chAgree.isChecked()) {
                onConfirmListener.notAgree();
                return;
            }
            switch (userName()) {
                case code:
                    profile.email = null;
                    profile.tel = null;
                    break;
                case tel:
                    profile.email = null;
                    profile.code = null;
                    break;
                case email:
                    profile.tel = null;
                    profile.code = null;

            }



        switch (mode) {
            case update:
                onConfirmListener.update(profile);
                break;
            case signIn:
                onConfirmListener.signIn(profile);
                break;
            case signUp:
                onConfirmListener.signUp(profile);
                break;
            case recover:
                onConfirmListener.recover(profile);
                break;
            case newPass:

                Recover recover=new Recover();
                recover.email=profile.email;
                recover.tel=profile.tel;
                recover.code=profile.code;
                recover.password=profile.password;
                recover.recover=etRecoverCode.getText().toString();
                if (recover.recover.length()<4){
                    onConfirmListener.recoverCodeInvalid(minRecoveryCodeLen);
                    return;
                }
                onConfirmListener.newPass(recover);
                break;


        }
//        onConfirmListener.signUp(profile);
    }
    Profile.UserNameType userName(){

        int i = rdbGroupUser.getCheckedRadioButtonId();
        if (i == R.id.rdb_use_code) {
            return Profile.UserNameType.code;
        }

        if (i == R.id.rdb_use_tel) {
            return Profile.UserNameType.tel;
        }
//        if (i == R.id.rdb_use_email) {
            return Profile.UserNameType.email;
//        }
//        return  swUsePhone.isChecked();
}

    void setUserNameType(Profile.UserNameType userNameType){
        switch (userNameType){
            case tel:
                llPhone.setVisibility(VISIBLE);
                llEmail.setVisibility(GONE);
                llCode.setVisibility(GONE);
                rdbTel.setChecked(true);
                break;
            case code:
                llPhone.setVisibility(GONE);
                llEmail.setVisibility(GONE);
                llCode.setVisibility(VISIBLE);
                rdbCode.setChecked(true);
                break;
            case email:
                llPhone.setVisibility(GONE);
                llEmail.setVisibility(VISIBLE);
                llCode.setVisibility(GONE);
                rdbEmail.setChecked(true);
                break;
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
onConfirmListener onConfirmListener;

    public void setOnConfirmListener(onConfirmListener l) {
        this.onConfirmListener = l;
    }

    public boolean isDoublePass() {
        return mode==Mode.signUp||mode==Mode.newPass||mode==Mode.update;
    }

//    public void setDoublePass(boolean doublePass) {
//        this.doublePass = doublePass;
//        llPass2.setVisibility(doublePass?VISIBLE:GONE);
//    }


//    void setView(){
//        if (getObject()==null)return;
//    }
    public Profile getObject(){
        return (Profile) getDbObject();
    }

    public interface onConfirmListener{
        void signUp(Profile profile);
        void signIn(Profile profile);
        void update(Profile profile);
        void recover(Profile profile);
        void signOut(Profile profile);
        void newPass(Recover recover);
        void recoverCodeInvalid(int len);
        void emptyPass();
        void lowPass(int minLen);
        void passNotSame();
        void invalidEmail(String email);
        void invalidCode(String code);
        void invalidPhone(String phone);
        void notAgree();
        void fillMode();
		void needLogin();
		void filled(Profile profile);
		void codeEntered(Profile profile,String code);
    }
}
