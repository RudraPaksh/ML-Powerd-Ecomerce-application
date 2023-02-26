package io.shubh.e_comm_ver1.Main.Interactor;


import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.Category;

public interface MainInteractor {

    interface CallbacksToPresnter {
        void onFinishedCheckingSomething1();

        //void onFinishedGettingItems();

    }
    interface SeparateCallbackToPresnterAfterGettingcategories {

        void onFinishedGettingCategories(ArrayList<Category> categoriesList);
    }

    interface SeparateCallbackToPresnterAftercheckingIfUserASellerOrNot {

        void onFinishedChecking(Boolean result);
    }

    void init(CallbacksToPresnter mPresenter);




    void getAllCatgrFromFirestoreWithArgAsCallbackFunction(MainInteractor.SeparateCallbackToPresnterAfterGettingcategories l);

    void checkIfUserisASellerOrNot(MainInteractor.SeparateCallbackToPresnterAftercheckingIfUserASellerOrNot l);

}

