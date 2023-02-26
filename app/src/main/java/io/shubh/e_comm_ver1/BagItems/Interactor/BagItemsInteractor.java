package io.shubh.e_comm_ver1.BagItems.Interactor;


import java.util.List;

import io.shubh.e_comm_ver1.Models.BagItem;

public interface BagItemsInteractor {

    interface CallbacksToPresnter {
       /* void onFinishedCheckingSomething1();

        void onFinishedCheckingSomething2();*/

    }

    interface SeparateCallbackToPresnterAfterGettingTheObjectList {

        void onFinished(boolean callbackResultOfTheCheck, List<BagItem> bagItemlist);
    }
    interface SeparateCallbackToPresnterAfterDeletingBagItem {

        void onFinished(boolean callbackResultOfTheCheck);
    }

    void init(CallbacksToPresnter mPresenter);

    void getbagItemsDataWithArgAsCallbackFunction(BagItemsInteractor.SeparateCallbackToPresnterAfterGettingTheObjectList l);

    void deletebagItemsWithArgAsCallbackFunction(String docId, SeparateCallbackToPresnterAfterDeletingBagItem l);
}

