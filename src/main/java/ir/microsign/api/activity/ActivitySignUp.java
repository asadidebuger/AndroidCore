package ir.microsign.api.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import ir.microsign.api.Api;
import ir.microsign.api.Utils;
import ir.microsign.api.database.DataSourceApi;
import ir.microsign.api.object.NetErrorMiddleware;
import ir.microsign.api.object.Profile;
import ir.microsign.api.object.Recover;
import ir.microsign.api.object.Response;
import ir.microsign.api.view.ProfileDataView;
import ir.microsign.api.view.ProfileView;
import ir.microsign.context.Preference;
import ir.microsign.dialog.WaitingDialog;
import ir.microsign.R;
import ir.microsign.utility.view;

public class ActivitySignUp extends Activity {
	private  Recover fillVerify;
//    public static void startSignIn(Context context){
//        start(context,"sign_in");
//    }
//    public static void startSignUp(Context context){
//        start(context,"sign_up");
//    }
//    public static void startSignOut(Context context){
//        start(context,"sign_out");
//    }
	void showWaiting(final boolean show){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WaitingDialog.showWaitingDialog(ActivitySignUp.this,show);

			}
		});
	}
    public static void show(Context context,int mode){
        Intent intent=new Intent(context,ActivitySignUp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("action",action);
		intent.putExtra("mode",mode);
        context.startActivity(intent);

    }
    public static void show(Context context) {
		show(context, 0);

	}
    public  void sendBroadcast(String data){
        Intent intent=new Intent(getPackageName()+".broadcast.login_signals");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri u=Uri.parse(data);
        intent.putExtra("action",data);
        sendBroadcast(intent);
    }

    ProfileView profileView = null;
    ProfileDataView profileDataView = null;

    int mode=0;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mode=getIntent().getIntExtra("mode",0);
		setView();


	}

	ProfileView.onConfirmListener onConfirmListener=new ProfileView.onConfirmListener() {
		@Override
		public void signUp(final Profile profile) {showWaiting(true);
			Api.signUp(profile, new Api.onSignUpListener() {
				@Override
				public void registered(Profile p) {
					showWaiting(false);
//                        Utils.setProfileSecret(p.password);
					p.password = profile.password;
					profileView.setProfile(p);
//                        DataSourceApi.getSource(ActivitySignUp.this).insertProfile(p);


//						Utils.setProfileSecret(p.password);
//						p.password = profile.password;
//						profileView.setProfile(p);
//						DataSourceApi.getSource(ActivitySignUp.this).insertProfile(p);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							profileView.setMode(ProfileView.Mode.signIn);
						}
					});
//						sendBroadcast("sign_up");
//						sendBroadcast("sign_in");
//						finish();

				}

				@Override
				public void error(String e) {
					showMessage(R.string.msg_login_error);
				}

				@Override
				public void alreadyExist() {
					switch (profile.getUserNameType()){
						case email:
							showMessage(R.string.msg_login_email_already_exist);
							break;
						case code:
							showMessage(R.string.msg_login_code_already_exist);
							break;
						case tel:
							showMessage(R.string.msg_login_phone_already_exist);
					}

				}
			});
		}

		@Override
		public void signIn(final Profile profile) {showWaiting(true);
			Api.signIn(profile, new Api.onSignInListener() {
				@Override
				public void signed(final Profile p) {showWaiting(false);
					Utils.setProfileSecret(p.password);
					p.password ="";// profile.password;
					profileView.setProfile(p);
					DataSourceApi.getSource(ActivitySignUp.this).insertProfile(p);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							profileView.setMode(ProfileView.Mode.signOut);
						}
					});
					sendBroadcast("sign_in");
					finish();
				}

				@Override
				public void wrongPass() {
					showMessage(R.string.msg_login_pass_wrong);
				}

				@Override
				public void notExist() {

					switch (profile.getUserNameType()){
						case email:
							showMessage(R.string.msg_login_user_not_exist_email);
							break;
						case code:
							showMessage(R.string.msg_login_user_not_exist_code);
							break;
						case tel:
							showMessage(R.string.msg_login_user_not_exist_phone);
					}

				}

				@Override
				public void error(String e) {
					showMessage(R.string.msg_login_error);
				}
			});
		}

		@Override
		public void update(final Profile profile) {showWaiting(true);
			Api.updateProfile(profile, new Api.onUpdateListener() {
				@Override
				public void updated(Profile p) {showWaiting(false);
//                        Utils.setProfileSecret(p.password);
					Utils.removeProfileSecret();
					p.password ="";// profile.password;
					DataSourceApi.getSource(ActivitySignUp.this).insertProfile(p);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							profileView.setMode(ProfileView.Mode.signIn);

						}
					});
				}

				@Override
				public void mustLogin() {
					showMessage(R.string.msg_login_must_login);
				}

				@Override
				public void error(String e) {
					showMessage(R.string.msg_login_error);
				}
			});
		}

		@Override
		public void recover(Profile profile) {showWaiting(true);
			Api.recoverPassword(profile, new Api.onRecoverListener() {
				@Override
				public void verificationSent(final Recover recover) {
					showWaiting(false);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							profileView.getObject().tel = recover.tel;
							profileView.getObject().email = recover.email;
							profileView.getObject().password = "";
							profileView.setMinRecoveryCodeLen(recover.len);
							profileView.setMode(ProfileView.Mode.newPass);
						}
					});
				}

				@Override
				public void limited() {
					showMessage(R.string.msg_login_recovery_limited);
				}

				@Override
				public void error(String e) {
					showMessage(R.string.msg_login_error);
				}
			});
		}

		@Override
		public void signOut(Profile profile1) {
			showWaiting(true);
			Api.post("profile", "signout", new Api.onResponseListener() {
				@Override
				public void onResponse(Response response) {
					showWaiting(false);
					if (!response.succeed())return;
					Utils.removeProfileSecret();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							profileView.setMode(ProfileView.Mode.signIn);
						}
					});
					sendBroadcast("sign_out");
				}
			});



		}

		@Override
		public void newPass(Recover recover) {
			showWaiting(true);
			Api.setNewPassword(recover, new Api.onNewPassListener() {
				@Override
				public void recovered(Profile profile) {
					showWaiting(false);
					DataSourceApi.getSource(ActivitySignUp.this).insertProfile(profile);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							profileView.setMode(ProfileView.Mode.signIn);
						}
					});
				}

				@Override
				public void timeout() {
					showMessage(R.string.msg_login_recovery_timeout);
				}

				@Override
				public void limited() {
					showMessage(R.string.msg_login_recovery_limited);
				}

				@Override
				public void invalid() {
					showMessage(R.string.msg_login_recovery_invalid);
				}

				@Override
				public void error(String e) {
					showMessage(R.string.msg_login_error);
				}
			});
		}

		@Override
		public void recoverCodeInvalid(int len) {
			showMessage(getString(R.string.msg_login_recover_code_not_valid, len));
		}

		@Override
		public void emptyPass() {
			showMessage(R.string.msg_login_pass_empty);

		}

		@Override
		public void lowPass(int minLen) {
			showMessage(getString(R.string.msg_login_pass_low, 4));

		}

		@Override
		public void passNotSame() {
			showMessage(R.string.msg_login_pass_not_same);
		}

		@Override
		public void invalidEmail(String email) {
			showMessage(R.string.msg_login_invalid_email);

		}

		@Override
		public void invalidCode(String code) {
			showMessage(R.string.msg_login_invalid_code);
		}

		@Override
		public void invalidPhone(String phone) {
			showMessage(R.string.msg_login_invalid_phone);

		}

		@Override
		public void notAgree() {
			showMessage(R.string.msg_login_not_agree);

		}

		@Override
		public void fillMode() {
			mode=1;
			setView();
		}

		@Override
		public void needLogin() {
			showWaiting(false);
			profileView.setMode(ProfileView.Mode.signIn);
			showMessage(R.string.msg_need_login);

		}

		@Override
		public void filled(Profile profile) {
			showWaiting(true);
			Api.sendRequest(Api.METHOD.POST, "profile", "sync", profile, new Api.onResponseListener() {
				@Override
				public void onResponse(Response response) {
					showWaiting(false);
					if (response.succeed()){
						fillVerify=response.getObject(Recover.class,"sync");

						DataSourceApi.getSource(ActivitySignUp.this).insertRecover(fillVerify);
						Preference.set(ActivitySignUp.this,"validation_code_timeout",System.currentTimeMillis()+fillVerify.timeout*1000);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								profileDataView.setMinRecoveryCodeLen(fillVerify.len);
								profileDataView.setMode(ProfileDataView.Mode.verify);

							}
						});
					return;
					}
					String err=response.getString();
					switch (err){
						case "code_not_valid":
							showMessage(R.string.msg_login_invalid_code);
							return;
						case "tel_not_valid":
							showMessage(R.string.msg_login_invalid_phone);
							return;
						case "email_not_valid":
							showMessage(R.string.msg_login_invalid_email);
							return;
						case Api.STATE.NOT_FOUND:
						showMessage(R.string.msg_login_email_or_tel_not_entered);
							return;
							case Api.STATE.LIMITED:
						showMessage(R.string.msg_login_verification_limited);
//							return;
					}
				}
			});

		}

		@Override
		public void codeEntered( Profile profile, String code) {
			fillVerify=DataSourceApi.getSource(ActivitySignUp.this).getRecover();
			fillVerify.recover=code;
			showWaiting(true);
			Api.sendRequest(Api.METHOD.POST, "profile", "syncverify", fillVerify, new Api.onResponseListener() {
				@Override
				public void onResponse(Response response) {
					showWaiting(false);
					if (!response.succeed()) {
						String err=response.getString();
						switch (err){
							case "code_not_valid":
								showMessage(R.string.msg_login_invalid_code);
								return;
							case "tel_not_valid":
								showMessage(R.string.msg_login_invalid_phone);
								return;
							case "email_not_valid":
								showMessage(R.string.msg_login_invalid_email);
								return;
							case Api.STATE.NOT_FOUND:
								showMessage(R.string.msg_login_email_or_tel_not_entered);
								return;
							case Api.STATE.LIMITED:
								showMessage(R.string.msg_login_verification_limited);
							case Api.STATE.WRONG_PASS:
								showMessage(R.string.msg_login_verification_invalid);
//							return;
						}
						return;
					}
					final Profile profile = response.getObject(Profile.class, "syncverify");
					Profile saved=Profile.getSavedProfile(ActivitySignUp.this);

					profile.password = saved!=null?saved.password:"";
					DataSourceApi.getSource(ActivitySignUp.this).insertProfile(profile);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Preference.set(ActivitySignUp.this,"validation_code_timeout",System.currentTimeMillis());

									mode=0;
									setView();
									signOut(profile);

								}
							});
//							ActivitySignUp.show(ActivitySignUp.this);

//							Utils.removeProfileSecret();
//							sendBroadcast("sign_out");
//							finish();
						}
					});

				}
			});

		}
	};

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Api.addOnExceptionListener(this.getClass().getName(),new NetErrorMiddleware(this));

	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Api.removeOnExceptionListener(this.getClass().getName());

	}

	protected void setView() {


        Profile profile =Profile.getSavedProfile(this);
        if (profile == null) {
        	if (mode==1){
//        		onConfirmListener.needLogin();
        		finish();
        		return;
			}
            profile = new Profile();
        }

		if (mode==0) {
			profileView = new ProfileView(this, profile);
			profileView.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			setContentView(profileView);
			profileView.setOnConfirmListener(onConfirmListener);
			return;
		}
		profileDataView=new ProfileDataView(this,profile);
		profileDataView.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setContentView(profileDataView);
		profileDataView.setOnConfirmListener(onConfirmListener);
    }

    void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.showToast(ActivitySignUp.this, msg);
            }
        });


    }

    void showMessage(int msg) {
		showWaiting(false);
        showMessage(getString(msg));
    }

}
