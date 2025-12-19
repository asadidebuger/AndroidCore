package ir.microsign.api.object;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import androidx.core.app.ActivityCompat;

import ir.microsign.api.Api;
import ir.microsign.api.activity.ActivitySignUp;
import ir.microsign.R;
import ir.microsign.utility.view;

public class NetErrorMiddleware implements   Api.onExceptionListener {
    public NetErrorMiddleware(Activity activity){
        this.activity=activity;
    }
    Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void nullDeviceId() {
        showMessage(R.string.msg_api_no_unique_address);
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                10000);
    }
},1500);

    }

    @Override
    public void noNetwork() {
        showMessage(R.string.msg_login_no_network);
    }

    @Override
    public void mustLogin() {
        showMessage(R.string.msg_login_must_login);
        activity.runOnUiThread(new Runnable() {
            @Override
//            public void run() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
                    public void run() {
                        try {

                            ActivitySignUp.show(activity);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
//                },1000);
//            }
        });

    }

    @Override
    public void missingArgs() {
        showMessage(R.string.msg_api_missing_args);
    }

    @Override
    public void timeout() {
        showMessage(R.string.msg_api_timeout);

    }

    void showMessage(final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.showToast(activity, msg);
            }
        });


    }

    void showMessage(int msg) {
        showMessage(activity.getString(msg));
    }
}
