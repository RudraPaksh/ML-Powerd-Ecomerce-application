package io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.Interactor;


import android.graphics.Bitmap;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.ItemsForSale;

public interface ItemsDetailsTakingInteractor {

    interface CallbacksToPresnter {
        void onFinishedCheckingSomething1();

        void onFinishedCheckingSomething2();

    }
    interface SeparateCallbackToPresnterForSystemUpdate {
        void onFinishedUploadingImages(ArrayList<String> callbackResultUrlList);
    }

    interface SeparateSecondCallbackToPresnterForSystemUpdate {
        void onFinishedGettingTheIdNo(String idOfLastItemUploaded);
    }
    interface SeparateThirdCallbackToPresnterAfterItemUploaded {
        void onFinishedUploadingItem();
    }
    interface SeparateForthCallbackToPresnterAfterIIdUpdation {
        void onFinishedUpdatingId();
    }

    void init(CallbacksToPresnter mPresenter);


    void getLastItemUploadedItemNo(SeparateSecondCallbackToPresnterForSystemUpdate l);

    void uploadImagesInStorageWithArgAsCallbackFunction(int i, String idForThisItem, ArrayList<String> dwnldImageUrls, ArrayList<Bitmap> bitmaps, SeparateCallbackToPresnterForSystemUpdate l);

    void uploadItemFunctionWithArgAsCallbackFunction(ItemsForSale item, SeparateThirdCallbackToPresnterAfterItemUploaded l);

    void updateLastItemUploadedIdVar(String idForThisItem, SeparateForthCallbackToPresnterAfterIIdUpdation l);

}

