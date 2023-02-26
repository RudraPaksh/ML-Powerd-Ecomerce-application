package io.shubh.e_comm_ver1.Welcome.View;

import android.content.Context;
import android.content.Intent;

public interface WelcomeView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void startActivityForResultt(Intent signInIntent, int RC_SIGN_IN);

    String getStringFromRes(int resId);
}
