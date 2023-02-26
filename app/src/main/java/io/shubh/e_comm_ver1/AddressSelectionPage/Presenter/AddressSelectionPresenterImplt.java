package io.shubh.e_comm_ver1.AddressSelectionPage.Presenter;

import java.util.List;

import io.shubh.e_comm_ver1.AddressSelectionPage.Interactor.AddressSelectionInteractor;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.AddressSelectionPage.View.AddressSelectionView;
import io.shubh.e_comm_ver1.Models.AdressItem;

public class AddressSelectionPresenterImplt implements AddressSelectionPresenter, AddressSelectionInteractor.CallbacksToPresnter {

    private AddressSelectionView mView;
    private AddressSelectionInteractor mInteractor;


    public AddressSelectionPresenterImplt(AddressSelectionView mView , AddressSelectionInteractor mSplashInteractor) {
       this.mView=mView;
       this.mInteractor = mSplashInteractor;

       mInteractor.init(this);



    }

    @Override
    public void getAddressData() {

        mView.showProgressBar(true);

        mInteractor.getAddressDataWithArgAsCallbackFunction(new AddressSelectionInteractor.SeparateCallbackToPresnterAfterGettingTheObjectList() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, List<AdressItem> addressItemlist) {

                if(callbackResultOfTheCheck==true){
                    //system upadte available ..so throw a dialog asking to download update
                    if(addressItemlist.size()!=0){

                        mView.showProgressBar(false);
                        mView.showItemsInRecyclerView(addressItemlist);
                    }else{
                        mView.showProgressBar(false);
                        mView.showEmptyListMessage();
                    }


                }else{
                    mView.showProgressBar(false);
                    mView.showToast("Some Problem Ocuured");
                }

            }
        });


    }

    @Override
    public void addAdressObject(AdressItem adressItem) {
        adressItem.setUid(StaticClassForGlobalInfo.UId);

        mInteractor.addAddressObjectWithArgAsCallbackFunction(adressItem,new AddressSelectionInteractor.SeparateCallbackToPresnterAfterAddingAddressObject() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck) {

                getAddressData();
              mView.showToast("added");
            }
        });
    }




    @Override
    public void deleteBagItem(String docId) {
        mInteractor.deletebagItemsWithArgAsCallbackFunction(docId,new AddressSelectionInteractor.SeparateCallbackToPresnterAfterDeletingBagItem() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck) {

                if(callbackResultOfTheCheck==true){
                    mView.updateReclrViewListAfterDeletionOfItem();
                }else{
                    getAddressData();
                    mView.showToast("Some Problem Ocuured");
                }
            }
        });
    }




}
