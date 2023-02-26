package io.shubh.e_comm_ver1.LikedItems.Presenter;

import java.util.List;

import io.shubh.e_comm_ver1.LikedItems.Interactor.LikedItemsInteractor;
import io.shubh.e_comm_ver1.LikedItems.View.LikedItemsView;
import io.shubh.e_comm_ver1.Models.LikedItem;

public class LikedItemsPresenterImplt implements LikedItemsPresenter, LikedItemsInteractor.CallbacksToPresnter {

    private LikedItemsView mView;
    private LikedItemsInteractor mInteractor;


    public LikedItemsPresenterImplt(LikedItemsView mView, LikedItemsInteractor mSplashInteractor) {
        this.mView = mView;
        this.mInteractor = mSplashInteractor;

        mInteractor.init(this);


    }

    @Override
    public void getLikedItemsData() {


        mView.showProgressBar(true);

        mInteractor.getLikedItemsDataWithArgAsCallbackFunction(new LikedItemsInteractor.SeparateCallbackToPresnterAfterGettingLikedItemList() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, List<LikedItem> likedItemList) {

                if (callbackResultOfTheCheck == true) {
                    //system upadte available ..so throw a dialog asking to download update
                    if (likedItemList.size() != 0) {

                        mView.showProgressBar(false);
                        mView.showItemsInRecyclerView(likedItemList);
                    } else {
                        mView.showProgressBar(false);
                        mView.showEmptyListMessage();
                    }


                } else {
                    //system upadte not available ..so continue
                    mView.showProgressBar(false);
                    mView.showToast("Some Problem Ocuured");
                }
            }
        });

    }


    @Override
    public void deleteLikedItem(String docId) {

        mInteractor.deleteLikedItemWithArgAsCallbackFunction(docId, new LikedItemsInteractor.SeparateCallbackToPresnterAfterDeletingLikedItem() {
                    @Override
                    public void onFinished(boolean callbackResultOfTheCheck) {
                        if(callbackResultOfTheCheck==true){
                            mView.updateReclrViewListAfterDeletionOfItem();
                        }else{
                            mView.showToast("Some Problem Ocuured");
                        }
                    }
                }

        );

    }


}
