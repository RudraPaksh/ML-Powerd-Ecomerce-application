package io.shubh.e_comm_ver1.SearchPage.Interactor;


import java.util.List;

import io.shubh.e_comm_ver1.Models.ItemsForSale;

public interface SearchResultsInteractor {

    interface CallbacksToPresnter {

    }

    interface SeparateCallbackToPresnterAfterGettingItems {

        void onFinished(boolean callbackResultOfTheCheck, List<ItemsForSale> list);
    }


    void init(CallbacksToPresnter mPresenter);


    void getTheItemsHavingSearchKeywordsInthierName(List<String> listOfKeywords, SeparateCallbackToPresnterAfterGettingItems l);


}

