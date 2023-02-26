package io.shubh.e_comm_ver1.LikedItems.Interactor;

import java.util.List;

import io.shubh.e_comm_ver1.Models.LikedItem;

public interface LikedItemsInteractor {

    interface CallbacksToPresnter {
       // void onFinishedCheckingSomething1();

       // void onFinishedCheckingSomething2();

    }
    interface SeparateCallbackToPresnterAfterGettingLikedItemList {

        void onFinished(boolean callbackResultOfTheCheck, List<LikedItem> likedItemList);
    }
    interface SeparateCallbackToPresnterAfterDeletingLikedItem {

        void onFinished(boolean callbackResultOfTheCheck);
    }


    void init(CallbacksToPresnter mPresenter);


    void getLikedItemsDataWithArgAsCallbackFunction(SeparateCallbackToPresnterAfterGettingLikedItemList l);

    void deleteLikedItemWithArgAsCallbackFunction(String docId, SeparateCallbackToPresnterAfterDeletingLikedItem l);

}

