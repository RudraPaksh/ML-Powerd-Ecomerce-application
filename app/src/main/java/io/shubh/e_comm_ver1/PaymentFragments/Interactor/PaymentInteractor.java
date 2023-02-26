package io.shubh.e_comm_ver1.PaymentFragments.Interactor;


import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.Order;

public interface PaymentInteractor {



    interface CallbacksToPresnter {
        void onFinishedCheckingSomething1();

        void onFinishedCheckingSomething2();

    }
    interface SeparateCallbackToPresnterAfterOrderUpload {

        void onFinished(boolean callbackResultOfTheCheck);
    }
    interface SeparateCallbackToPresnterAfterGettingLastOrderId {

        void onFinished(boolean callbackResultOfTheCheck, String orderId);
    }
    interface SeparateCallbackToPresnterAfterUploadingSubCollections {

        void onFinished(boolean callbackResultOfTheCheck);
    }
    interface SeparateCallbackToPresnterAfterUpdatingLastOrdreId{

        void onFinished(boolean callbackResultOfTheCheck);
    }
    interface SeparateCallbackToPresnterAfterDeletingBagItems{

        void onFinished(boolean callbackResultOfTheCheck);
    }
    interface SeparateCallbackToPresnterAfterSendNotifToSeller{

        void onFinished(boolean callbackResultOfTheCheck);
    }


    void init(CallbacksToPresnter mPresenter);

    void uploadOrderInDatabaseWithArgAsCallbackFunction(Order orderId, SeparateCallbackToPresnterAfterOrderUpload l);
    void getLastOrderIdWithArgAsCallbackFunction(PaymentInteractor.SeparateCallbackToPresnterAfterGettingLastOrderId l);
    void setSubItemsInsubCollectionWithArgAsCallbackFunction(int orderOfCall, Order order, ArrayList<Order.SubOrderItem> subOrderItems, SeparateCallbackToPresnterAfterUploadingSubCollections l);
    void updateLastOrderIdWithArgAsCallbackFunction(String finalOrderId, SeparateCallbackToPresnterAfterUpdatingLastOrdreId l);
    void deleteAllBagItemsWithArgAsCallbackFunction(Order order, SeparateCallbackToPresnterAfterDeletingBagItems l);
    void sendNotifToSellerWithArgAsCallbackFunction(Order order, SeparateCallbackToPresnterAfterSendNotifToSeller l);

}

