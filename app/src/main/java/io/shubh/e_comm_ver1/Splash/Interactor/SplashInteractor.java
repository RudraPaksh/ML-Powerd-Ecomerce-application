package io.shubh.e_comm_ver1.Splash.Interactor;

public interface SplashInteractor {

   interface CallbacksToPresnter {
        void onFinishedCheckingUserAlreadyExists();


    }
/*
    interface SeparateCallbackToPresnterForSystemUpdate {

        void onFinishedGettingItems(boolean callbackResultOfTheCheck);
    }

    void init(CallbacksToPresnter mPresenter);


    void checkIfUserAlreadyExistsInDatabase();

    void checkForSystemUpdate(SplashInteractor.SeparateCallbackToPresnterForSystemUpdate l);*/
}
