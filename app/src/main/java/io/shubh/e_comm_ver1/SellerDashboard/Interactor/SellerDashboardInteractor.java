package io.shubh.e_comm_ver1.SellerDashboard.Interactor;


import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Models.Order;

public interface SellerDashboardInteractor {



    interface CallbacksToPresnter {
        void onFinishedCheckingSomething1();

    }

    interface SeparateCallbackToPresnterAfterGettingSellerData {

        void onFinished(boolean callbackResultOfTheCheck, ArrayList<Order.SubOrderItem> subOrderItems);
    }
    interface SeparateCallbackToPresnterAfterGettingSellerItems{

        void onFinished(boolean callbackResultOfTheCheck, ArrayList<ItemsForSale> subOrderItems);
    }

    void init(CallbacksToPresnter mPresenter);


    //void checkSomethingInDatabase();

    void getSellerDataWithArgAsCallback(SeparateCallbackToPresnterAfterGettingSellerData l);
    void getSellerUploadedItemsWithArgAsCallback(SeparateCallbackToPresnterAfterGettingSellerItems l);

}

