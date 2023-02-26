package io.shubh.e_comm_ver1.Notification.Interactor;


import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.NotifcationObject;

public interface NotificationInteractor {



    interface CallbacksToPresnter {
        void showToast(String no_more_items_found);
        //  void onFinishedCheckingSomething1();

        // void onFinishedCheckingSomething2();

    }
    interface SeparateCallbackToPresnterAfterGettingNotifData {

        void onFinished(ArrayList<NotifcationObject> itemsList, boolean listNotEmpty, boolean callbackResultOfTheCheck);
    }

    void init(CallbacksToPresnter mPresenter);


    // checkSomethingInDatabase();

    void getTheFirstItemDocumentAsAReferenceForStartAtFunct(boolean isItLoadMoreCall ,SeparateCallbackToPresnterAfterGettingNotifData l);

}

