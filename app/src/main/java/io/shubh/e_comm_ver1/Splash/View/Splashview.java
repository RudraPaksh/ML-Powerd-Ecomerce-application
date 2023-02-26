package io.shubh.e_comm_ver1.Splash.View;

import android.content.Context;

public interface Splashview {

    void SwitchActivity(int i);

    Context getContext();

    void showProgreeBar(boolean b);

    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String you_are_logged_in_already);
}
