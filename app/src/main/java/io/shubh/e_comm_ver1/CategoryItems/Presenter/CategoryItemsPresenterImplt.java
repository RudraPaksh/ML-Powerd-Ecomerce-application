package io.shubh.e_comm_ver1.CategoryItems.Presenter;

import java.util.List;

import io.shubh.e_comm_ver1.CategoryItems.Interactor.CategoryItemsInteractor;
import io.shubh.e_comm_ver1.Models.LikedItem;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.CategoryItems.View.CategoryItemsView;
import io.shubh.e_comm_ver1.Models.ItemsForSale;

public class CategoryItemsPresenterImplt implements CategoryItemsPresenter, CategoryItemsInteractor.CallbacksToPresnter {

    private CategoryItemsView categoryItemsView;
    private CategoryItemsInteractor mInteractor;

    String categoryName;
    String categoryPath;



    public CategoryItemsPresenterImplt(CategoryItemsView categoryItemsView , CategoryItemsInteractor mSplashInteractor , String category , String categoryPath) {
       this.categoryItemsView =categoryItemsView;
       this.mInteractor = mSplashInteractor;
       this.categoryName=category;
        this.categoryPath =categoryPath;

       mInteractor.init(this);



    }

    @Override
    public void getItemsFromFirebase(String param1CategoryName, String param2CategoryPath, String rootCtgr, String subCtgr, String subSubCtgr, boolean ifItsALoadMorecall) {

        mInteractor.getTheFirstItemDocumentAsAReferenceForStartAtFunct(param1CategoryName,param2CategoryPath ,rootCtgr,subCtgr,subSubCtgr,ifItsALoadMorecall );

     //   mInteractor.getItemsFromFirebaseWithResultsOnSeparateCallback(mParam1CategoryName , mParam2CategoryPath);
    }


    @Override
    public void onFinishedGettingItems(List<ItemsForSale> itemList, Boolean listNotEmpty, String ctgrName ,boolean ifItsALoadMoreCallResult) {

        if(listNotEmpty==true){

            categoryItemsView.onGettingCtgrItemsFromPrsntr(itemList,listNotEmpty ,ctgrName ,ifItsALoadMoreCallResult);
        }else{
            //tell the fragment to show no results found
            categoryItemsView.showToast("No items Found");

            //tell the fragment to show no results found
            categoryItemsView.onNoItemsFoundResult(ctgrName , ifItsALoadMoreCallResult) ;
        }
    }

    @Override
    public void showToast(String no_more_items_found) {
        categoryItemsView.showToast("No More Items Found");
    }

    @Override
    public void saveTheItemToLikedItems(String itemID) {

        LikedItem likedItem = new LikedItem();
        likedItem.setItemId(itemID);
        likedItem.setLikedItemDocId(itemID+ StaticClassForGlobalInfo.UId);
        likedItem.setTimeOfsaving(System.currentTimeMillis() /1000l);
        likedItem.setUserId(StaticClassForGlobalInfo.UId);

        mInteractor.saveItemToUserLikedListWithResultsOnSeparateCallback(likedItem, new CategoryItemsInteractor.SeparateCallbackToPresnterAfterSavingItemToBuyerLikedItems() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck) {
                if(callbackResultOfTheCheck==true){

                    categoryItemsView.showToast("Item added to liked list");
                }else{
                    categoryItemsView.showToast("Some problem occured while adding to liked items list");
                }
            }
        });
    }

    @Override
    public void deleteTheItemFromLikedItems(String docId) {

        String fireStoreDocId = docId +StaticClassForGlobalInfo.UId;

        mInteractor.deleteItemFromUserLikedListWithResultsOnSeparateCallback(fireStoreDocId, new CategoryItemsInteractor.SeparateCallbackToPresnterAfterDeletingFromBuyerLikedItems() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck) {
                if(callbackResultOfTheCheck==true){
                    categoryItemsView.showToast("Item deleted from liked list");
                }else{
                    categoryItemsView.showToast("Some problem occured while deleting from liked items list");
                }
            }
        });
    }




}
