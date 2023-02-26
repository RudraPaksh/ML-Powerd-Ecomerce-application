package io.shubh.e_comm_ver1.OrderListFrSellerFragment.Interactor;


import io.shubh.e_comm_ver1.Models.Order;

public interface NewOrderListFrSellerInteractor {

    interface CallbacksToPresnter {
      //  void onFinishedCheckingSomething1();

     //   void onFinishedCheckingSomething2();

    }
    interface SeparateCallbackToPresnterAfterOrderStatusUpdate {

        void onFinished(boolean callbackResultOfTheCheck, int nextStatusNoToUpdate);
    }

    void init(CallbacksToPresnter mPresenter);




    void UpdateStatusInDatabaseWithArgAsCallbackFunction(Order.SubOrderItem subOrderItem, int nextStatusNoToUpdate, NewOrderListFrSellerInteractor.SeparateCallbackToPresnterAfterOrderStatusUpdate l);
}

