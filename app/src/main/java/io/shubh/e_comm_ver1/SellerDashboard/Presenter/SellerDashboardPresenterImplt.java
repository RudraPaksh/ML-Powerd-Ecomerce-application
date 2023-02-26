package io.shubh.e_comm_ver1.SellerDashboard.Presenter;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.SellerDashboard.Interactor.SellerDashboardInteractor;
import io.shubh.e_comm_ver1.SellerDashboard.View.SellerDashboardView;

public class SellerDashboardPresenterImplt implements SellerDashboardPresenter, SellerDashboardInteractor.CallbacksToPresnter {

    private SellerDashboardView mView;
    private SellerDashboardInteractor mInteractor;

    public SellerDashboardPresenterImplt(SellerDashboardView categoryItemsView , SellerDashboardInteractor mSplashInteractor ) {
       this.mView =categoryItemsView;
       this.mInteractor = mSplashInteractor;


       mInteractor.init(this);



    }

    @Override
    public void getSellerData() {
        mView.showProgressBar(true);
        mInteractor.getSellerDataWithArgAsCallback(new SellerDashboardInteractor.SeparateCallbackToPresnterAfterGettingSellerData() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, ArrayList<Order.SubOrderItem> subOrderItems) {
                if(callbackResultOfTheCheck==true){
                    //system upadte available ..so throw a dialog asking to download update
                    if(subOrderItems.size()!=0){


                       // mView.showItemsInRecyclerView(subOrderItems);
                       processTheSubOrderListData(subOrderItems);
                    }else{
                        mView.showProgressBar(false);
                      //  mView.showEmptyListMessage();
                    }


                }else{
                    //system upadte not available ..so continue
                    mView.showProgressBar(false);
                    mView.showToast("Some Problem Ocuured");
                }

            }
        });
    }



    private void processTheSubOrderListData(ArrayList<Order.SubOrderItem> subOrderItems) {

        //to get the dat like amount of newOrders and processed order .i am calculating it from the list
        //of the orders containing this seller item.The Ideal method of doing this is having field of each
        //in user account document online ..and then retrive from there ..TODO -do that next time
        //if this app is used for real urpose
        /*int newOrders =0;
        int processed =0;
        int returnedOrders =0;*/

        ArrayList<Order.SubOrderItem> newOrdersList = new ArrayList<>();
        ArrayList<Order.SubOrderItem> processedList = new ArrayList<>();
        ArrayList<Order.SubOrderItem> returnedOrdersList = new ArrayList<>();

        for(int i =0 ;i<subOrderItems.size() ;i++){
            if(subOrderItems.get(i).getStatusOfOrder()==2 || subOrderItems.get(i).getStatusOfOrder()==3 ||subOrderItems.get(i).getStatusOfOrder()==4 ){
            //    newOrders++;
                newOrdersList.add(subOrderItems.get(i));
            }else if(subOrderItems.get(i).getStatusOfOrder()==5){
            //    processed++;
                processedList.add(subOrderItems.get(i));
            }else if(subOrderItems.get(i).getStatusOfOrder()==7 || subOrderItems.get(i).getStatusOfOrder()==8){
             //   returnedOrders++;
                returnedOrdersList.add(subOrderItems.get(i));
            }
        }

     //   mView.showProgressBar(false);
        mView.updateTransactionSummaryTvs(subOrderItems ,newOrdersList ,processedList ,returnedOrdersList );
    }


    @Override
    public void getDataForBottomSheet() {

        mInteractor.getSellerUploadedItemsWithArgAsCallback(new SellerDashboardInteractor.SeparateCallbackToPresnterAfterGettingSellerItems() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, ArrayList<ItemsForSale> list) {
                if(callbackResultOfTheCheck==true){
                    //system upadte available ..so throw a dialog asking to download update
                    if(list.size()!=0){

                     //   mView.showProgressBar(false);
                         mView.showItemsInBottomSheet(list);

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
  /*  @Override
    public void LoginRelatedWork() {


       *//* mInteractor.getItemsFromFirebaseWithResultsOnSeparateCallback(new CategoryItemsInteractor.SeparateCallbackToPresnterAfterGettingItemsForRclrView(){
            @Override
            public void onFinishedGettingItems(boolean callbackResultOfTheCheck) {

              if(callbackResultOfTheCheck==true){
                  //system upadte available ..so throw a dialog asking to download update
              }else{
                  //system upadte not available ..so continue
              }
            }
        });*//*
    }*/



    @Override
    public void onFinishedCheckingSomething1() {
//this is call from interactor
    }



}
