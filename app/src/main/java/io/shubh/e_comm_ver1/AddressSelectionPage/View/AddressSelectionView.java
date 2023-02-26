package io.shubh.e_comm_ver1.AddressSelectionPage.View;

import android.content.Context;

import java.util.List;

import io.shubh.e_comm_ver1.Models.AdressItem;

public interface AddressSelectionView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void showItemsInRecyclerView(List<AdressItem> bagItemlist);

    void updateReclrViewListAfterDeletionOfItem();

    void showEmptyListMessage();
}
