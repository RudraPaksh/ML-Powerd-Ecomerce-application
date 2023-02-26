package io.shubh.e_comm_ver1.MyOrders.View;

import android.content.Context;

import java.util.List;

import io.shubh.e_comm_ver1.Models.Order;

public interface MyOrdersView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void showItemsInRecyclerView(List<Order> orderItemlist);

    void showEmptyListMessage();
}
