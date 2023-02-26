package io.shubh.e_comm_ver1.Notification.View;

import android.content.Context;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.NotifcationObject;

public interface NotificationView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void onGettingCtgrItemsFromPrsntr(ArrayList<NotifcationObject> itemsList, boolean listNotEmpty, boolean isItLoadMoreCall);

    void onNoItemsFoundResult(boolean isItLoadMoreCall);
}
