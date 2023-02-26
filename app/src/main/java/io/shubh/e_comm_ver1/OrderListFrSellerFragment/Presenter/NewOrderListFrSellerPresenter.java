package io.shubh.e_comm_ver1.OrderListFrSellerFragment.Presenter;

import io.shubh.e_comm_ver1.Models.Order;

public interface NewOrderListFrSellerPresenter {

  //  void checkIfAlreadyLoggedIn();

    //void checkForSystemUpdates();

   // void onGettingThegoogleSignInResult(int code, int requestCode, Intent data);

     //void LoginRelatedWork();

    void changeStatusOfOrderAndSendNotification(Order.SubOrderItem subOrderItemOrder, long l);
}
