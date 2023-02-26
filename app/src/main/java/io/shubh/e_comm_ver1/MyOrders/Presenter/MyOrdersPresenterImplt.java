package io.shubh.e_comm_ver1.MyOrders.Presenter;

import java.util.List;

import io.shubh.e_comm_ver1.MyOrders.Interactor.MyOrdersInteractor;
import io.shubh.e_comm_ver1.Models.Order;

import io.shubh.e_comm_ver1.MyOrders.View.MyOrdersView;

public class MyOrdersPresenterImplt implements MyOrdersPresenter, MyOrdersInteractor.CallbacksToPresnter {

    private MyOrdersView mView;
    private MyOrdersInteractor mInteractor;


    public MyOrdersPresenterImplt(MyOrdersView mView , MyOrdersInteractor mSplashInteractor) {
       this.mView=mView;
       this.mInteractor = mSplashInteractor;

       mInteractor.init(this);



    }

    @Override
    public void getOrderItemsData() {
        mView.showProgressBar(true);


        mInteractor.getOrderItemsDataWithArgAsCallbackFunction( new MyOrdersInteractor.SeparateCallbackToPresnterAfterGettingOrderItems(){
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, List<Order> orderItemlist) {
                if(callbackResultOfTheCheck==true){
                    //system upadte available ..so throw a dialog asking to download update
                    if(orderItemlist.size()!=0){

                        mView.showProgressBar(false);
                        mView.showItemsInRecyclerView(orderItemlist);
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



}
