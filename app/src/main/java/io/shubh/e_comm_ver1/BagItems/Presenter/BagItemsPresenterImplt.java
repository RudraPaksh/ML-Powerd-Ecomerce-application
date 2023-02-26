package io.shubh.e_comm_ver1.BagItems.Presenter;

import java.util.List;

import io.shubh.e_comm_ver1.BagItems.Interactor.BagItemsInteractor;
import io.shubh.e_comm_ver1.Models.BagItem;
import io.shubh.e_comm_ver1.BagItems.View.BagItemsView;

public class BagItemsPresenterImplt implements BagItemsPresenter, BagItemsInteractor.CallbacksToPresnter {

    private BagItemsView mView;
    private BagItemsInteractor mInteractor;


    public BagItemsPresenterImplt(BagItemsView mView , BagItemsInteractor mSplashInteractor) {
       this.mView=mView;
       this.mInteractor = mSplashInteractor;

       mInteractor.init(this);



    }

    @Override
    public void getBagItemsData() {

        mView.showProgressBar(true);

        mInteractor.getbagItemsDataWithArgAsCallbackFunction( new BagItemsInteractor.SeparateCallbackToPresnterAfterGettingTheObjectList(){
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, List<BagItem> bagItemlist) {

                if(callbackResultOfTheCheck==true){
                    //system upadte available ..so throw a dialog asking to download update
                    if(bagItemlist.size()!=0){

                        mView.showProgressBar(false);
                        mView.showItemsInRecyclerView(bagItemlist);
                    }else{
                        mView.showProgressBar(false);
                        mView.showEmptyListMessage();
                    }


                }else{
                    //system upadte not available ..so continue
                    mView.showProgressBar(false);
                     mView.showToast("Some Problem Ocuured");
                }
            }
        });

    }

    @Override
    public void deleteBagItem(String docId) {
        mInteractor.deletebagItemsWithArgAsCallbackFunction(docId,new BagItemsInteractor.SeparateCallbackToPresnterAfterDeletingBagItem() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck) {

                if(callbackResultOfTheCheck==true){
                    mView.updateReclrViewListAfterDeletionOfItem();
                }else{
                    mView.showToast("Some Problem Ocuured");
                }
            }
        });
    }


}
