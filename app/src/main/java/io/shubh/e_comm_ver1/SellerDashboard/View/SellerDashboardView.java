package io.shubh.e_comm_ver1.SellerDashboard.View;

import android.content.Context;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Models.Order;

public interface SellerDashboardView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void updateTransactionSummaryTvs(ArrayList<Order.SubOrderItem> subOrderItems, ArrayList<Order.SubOrderItem> newOrders, ArrayList<Order.SubOrderItem> processed, ArrayList<Order.SubOrderItem> returnedOrders);

    void showEmptyListMessage();

    void showItemsInBottomSheet(ArrayList<ItemsForSale> list);


    // void onCategoryButtonsClicked(int levelOfCategory , String name);

}
