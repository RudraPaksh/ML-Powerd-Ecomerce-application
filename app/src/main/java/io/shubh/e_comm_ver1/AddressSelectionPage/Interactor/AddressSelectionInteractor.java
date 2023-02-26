package io.shubh.e_comm_ver1.AddressSelectionPage.Interactor;


import java.util.List;

import io.shubh.e_comm_ver1.Models.AdressItem;

public interface AddressSelectionInteractor {

    interface CallbacksToPresnter {
       /* void onFinishedCheckingSomething1();

        void onFinishedCheckingSomething2();*/

    }

    interface SeparateCallbackToPresnterAfterGettingTheObjectList {

        void onFinished(boolean callbackResultOfTheCheck, List<AdressItem> addressItemlist);
    }
    interface SeparateCallbackToPresnterAfterAddingAddressObject {

        void onFinished(boolean callbackResultOfTheCheck);
    }
    interface SeparateCallbackToPresnterAfterDeletingBagItem {

        void onFinished(boolean callbackResultOfTheCheck);
    }

    void init(CallbacksToPresnter mPresenter);

    void getAddressDataWithArgAsCallbackFunction(AddressSelectionInteractor.SeparateCallbackToPresnterAfterGettingTheObjectList l);

    void addAddressObjectWithArgAsCallbackFunction(AdressItem adressItem, SeparateCallbackToPresnterAfterAddingAddressObject l);

    void deletebagItemsWithArgAsCallbackFunction(String docId, SeparateCallbackToPresnterAfterDeletingBagItem l);
}

