package io.shubh.e_comm_ver1.ItemDetailPage.Presenter;

import io.shubh.e_comm_ver1.Models.ItemsForSale;

public interface ItemDetailPresenter {

  //  void checkIfAlreadyLoggedIn();

    //void checkForSystemUpdates();

   // void onGettingThegoogleSignInResult(int code, int requestCode, Intent data);

    void onBagItBtClicked(ItemsForSale item, int itemAmount , int chosenVarietyIndex);

    void saveTheItemToLikedItems(String docId);

    void deleteTheItemFromLikedItems(String docId);

}
