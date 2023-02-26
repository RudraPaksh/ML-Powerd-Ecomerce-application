package io.shubh.e_comm_ver1.SearchPage.Presenter;

import java.util.List;

import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.SearchPage.View.SearchResultsView;
import io.shubh.e_comm_ver1.SearchPage.Interactor.SearchResultsInteractor;

public class SearchResultsPresenterImplt implements SearchResultsPresenter, SearchResultsInteractor.CallbacksToPresnter {

    private SearchResultsView view;
    private SearchResultsInteractor mInteractor;

    String categoryName;
    String categoryPath;



    public SearchResultsPresenterImplt(SearchResultsView view , SearchResultsInteractor mSplashInteractor ) {
       this.view =view;
       this.mInteractor = mSplashInteractor;


       mInteractor.init(this);
    }


    @Override
    public void getSearchQuery(List<String> listOfKeywords) {

        view.showProgressBar(true);

        mInteractor.getTheItemsHavingSearchKeywordsInthierName(listOfKeywords , new SearchResultsInteractor.SeparateCallbackToPresnterAfterGettingItems() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, List<ItemsForSale> list) {

                if(list.size()!=0){

                    view.onGettingCtgrItemsFromPrsntr(list);
                    view.showProgressBar(false);
                }else{
                    view.showProgressBar(false);
                    //tell the fragment to show no results found
                    view.showToast("No items Found");

                    //tell the fragment to show no results found
                    view.onNoItemsFoundResult( ) ;
                }

            }
        });
    }
}
