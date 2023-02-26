package io.shubh.e_comm_ver1.ItemDetailPage.Presenter;

import io.shubh.e_comm_ver1.ItemDetailPage.Interactor.ItemDetailInteractor;
import io.shubh.e_comm_ver1.Models.LikedItem;
import io.shubh.e_comm_ver1.ItemDetailPage.View.ItemDetailView;
import io.shubh.e_comm_ver1.Models.BagItem;
import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;

public class ItemDetailPresenterImplt implements ItemDetailPresenter, ItemDetailInteractor.CallbacksToPresnter {

    private ItemDetailView mview;
    private ItemDetailInteractor mInteractor;


    public ItemDetailPresenterImplt(ItemDetailView mview, ItemDetailInteractor mInteractor) {
        this.mview = mview;
        this.mInteractor = mInteractor;

        mInteractor.init(this);


    }


    @Override
    public void onBagItBtClicked(ItemsForSale item, int itemAmount , int chosenVarietyIndex) {

        BagItem bagItem = new BagItem();
        bagItem.setItemAmount(String.valueOf(itemAmount));
        bagItem.setItemId(String.valueOf(item.getItem_id()));
        bagItem.setTime_of_upload(System.currentTimeMillis() / 1000L);
        bagItem.setUserId(StaticClassForGlobalInfo.UId);
      if(item.getVarieies().size()!=0){
        bagItem.setSelectedVarietyIndexInList(String.valueOf(chosenVarietyIndex));
      }

mview.showProgressBarAtBagItBt(true);
        mInteractor.uploadBagItemWithArgAsCallbackFunction(bagItem, new ItemDetailInteractor.SeparateCallbackToPresnterAfterBagItemUpload() {
            @Override
            public void onFinishedUploading(boolean callbackResultOfTheUpload) {
                if(callbackResultOfTheUpload){
                   // mview.showToast("added to bag");
                    mview.showKsnackBarWithAction();
                    mview.showProgressBarAtBagItBt(false);
                    //TODO-make  a snackbar here or a button in a toast wich reads 'view bag items' or just 'view'
                }else{
                    mview.showToast("some error occured");
                    mview.showProgressBarAtBagItBt(false);
                }

            }
        });
    }


    @Override
    public void saveTheItemToLikedItems(String itemID) {

        LikedItem likedItem = new LikedItem();
        likedItem.setItemId(itemID);
        likedItem.setLikedItemDocId(itemID+ StaticClassForGlobalInfo.UId);
        likedItem.setTimeOfsaving(System.currentTimeMillis() /1000l);
        likedItem.setUserId(StaticClassForGlobalInfo.UId);

        mInteractor.saveItemToUserLikedListWithResultsOnSeparateCallback(likedItem, new ItemDetailInteractor.SeparateCallbackToPresnterAfterSavingItemToBuyerLikedItems() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck) {
                if(callbackResultOfTheCheck==true){

                    mview.showToast("Item added to liked list");
                }else{
                    mview.showToast("Some problem occured while adding to liked items list");
                }
            }
        });
    }

    @Override
    public void deleteTheItemFromLikedItems(String docId) {

        String fireStoreDocId = docId +StaticClassForGlobalInfo.UId;

        mInteractor.deleteItemFromUserLikedListWithResultsOnSeparateCallback(fireStoreDocId, new ItemDetailInteractor.SeparateCallbackToPresnterAfterDeletingFromBuyerLikedItems() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck) {
                if(callbackResultOfTheCheck==true){
                    mview.showToast("Item deleted from liked list");
                }else{
                    mview.showToast("Some problem occured while deleting from liked items list");
                }
            }
        });
    }
}
