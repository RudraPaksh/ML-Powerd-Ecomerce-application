package io.shubh.e_comm_ver1.Notification.Presenter;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.NotifcationObject;
import io.shubh.e_comm_ver1.Notification.Interactor.NotificationInteractor;
import io.shubh.e_comm_ver1.Notification.View.NotificationView;

public class NotificationPresenterImplt implements NotificationPresenter, NotificationInteractor.CallbacksToPresnter {

    private NotificationView mView;
    private NotificationInteractor mInteractor;


    public NotificationPresenterImplt(NotificationView mView , NotificationInteractor mSplashInteractor) {
       this.mView=mView;
       this.mInteractor = mSplashInteractor;

       mInteractor.init(this);



    }


    @Override
    public void showToast(String no_more_items_found) {
        mView.showToast("No More Items Found");
    }



    @Override
    public void getNotificationData(boolean isItLoadMoreCall) {

        mInteractor.getTheFirstItemDocumentAsAReferenceForStartAtFunct(isItLoadMoreCall, new NotificationInteractor.SeparateCallbackToPresnterAfterGettingNotifData() {
            @Override
            public void onFinished(ArrayList<NotifcationObject> itemsList, boolean listNotEmpty, boolean callbackResultOfTheCheck) {

                if(listNotEmpty==true){

                    mView.onGettingCtgrItemsFromPrsntr(itemsList,listNotEmpty  ,isItLoadMoreCall);
                }else{
                    //tell the fragment to show no results found
                    mView.showToast("No items Found");

                    //tell the fragment to show no results found
                    mView.onNoItemsFoundResult(isItLoadMoreCall) ;
                }

            }
        });

    }
}
