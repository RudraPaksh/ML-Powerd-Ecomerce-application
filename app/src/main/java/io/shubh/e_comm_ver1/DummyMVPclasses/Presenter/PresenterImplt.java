package io.shubh.e_comm_ver1.DummyMVPclasses.Presenter;

import io.shubh.e_comm_ver1.DummyMVPclasses.Interactor.Interactor;
import io.shubh.e_comm_ver1.DummyMVPclasses.View.View;

public class PresenterImplt implements Presenter, Interactor.CallbacksToPresnter {

    private View mView;
    private Interactor mInteractor;


    public PresenterImplt(View mView , Interactor mSplashInteractor) {
       this.mView=mView;
       this.mInteractor = mSplashInteractor;

       mInteractor.init(this);



    }


    @Override
    public void LoginRelatedWork() {


        mInteractor.checkSomethingInDatabaseWithArgAsCallbackFunction( new Interactor.SeparateCallbackToPresnterForSystemUpdate(){
            @Override
            public void onFinishedCheckingSystemUpdate(boolean callbackResultOfTheCheck) {

              if(callbackResultOfTheCheck==true){
                  //system upadte available ..so throw a dialog asking to download update
              }else{
                  //system upadte not available ..so continue
              }
            }
        });
    }


    @Override
    public void onFinishedCheckingSomething1() {
//this is call from interactor
    }

    @Override
    public void onFinishedCheckingSomething2() {

        //this is call from interactor
    }
}
