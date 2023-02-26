package io.shubh.e_comm_ver1.CategoryItems.Interactor;


import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.LikedItem;
import io.shubh.e_comm_ver1.Models.ItemsForSale;

public interface CategoryItemsInteractor {

    interface CallbacksToPresnter {


        void onFinishedGettingItems(List<ItemsForSale> itemList, Boolean listNotEmpty, String ctgrName, boolean ifItsALoadMoreCallResult);

        void showToast(String no_more_items_found);
    }

    interface SeparateCallbackToPresnterAfterSavingItemToBuyerLikedItems {

        void onFinished(boolean callbackResultOfTheCheck);
    }

    interface SeparateCallbackToPresnterAfterDeletingFromBuyerLikedItems {

        void onFinished(boolean callbackResultOfTheCheck);
    }

    void init(CallbacksToPresnter mPresenter);


    void getTheFirstItemDocumentAsAReferenceForStartAtFunct(String ctgr, String ctgrPath, String rootCtgr, String subCtgr, String subSubCtgr, boolean ifItsALoadMorecall);

    void getItemsFromFirebaseWithResultsOnSeparateCallback(String ctgr, String ctgrPath, String rootCtgr, String subCtgr, String subSubCtgr, boolean ifItsALoadMorecall, ArrayList<ItemsForSale> itemsList);

    void saveItemToUserLikedListWithResultsOnSeparateCallback(LikedItem itemId, SeparateCallbackToPresnterAfterSavingItemToBuyerLikedItems l);

    void deleteItemFromUserLikedListWithResultsOnSeparateCallback(String docId, SeparateCallbackToPresnterAfterDeletingFromBuyerLikedItems l);

}

