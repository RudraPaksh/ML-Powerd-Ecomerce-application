package io.shubh.e_comm_ver1.MyOrders.Interactor;


import java.util.List;

import io.shubh.e_comm_ver1.Models.Order;

public interface MyOrdersInteractor {

    interface CallbacksToPresnter {
       /* void onFinishedCheckingSomething1();

        void onFinishedCheckingSomething2();*/

    }
    interface SeparateCallbackToPresnterAfterGettingOrderItems {

        void onFinished(boolean callbackResultOfTheCheck, List<Order> orderItemlist);
    }

    void init(CallbacksToPresnter mPresenter);


    void getOrderItemsDataWithArgAsCallbackFunction(MyOrdersInteractor.SeparateCallbackToPresnterAfterGettingOrderItems l);


}


