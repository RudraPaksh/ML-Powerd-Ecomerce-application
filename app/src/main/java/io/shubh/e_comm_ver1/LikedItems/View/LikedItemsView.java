package io.shubh.e_comm_ver1.LikedItems.View;

import android.content.Context;

import java.util.List;

import io.shubh.e_comm_ver1.Models.LikedItem;

public interface LikedItemsView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void updateReclrViewListAfterDeletionOfItem();

    void showItemsInRecyclerView(List<LikedItem> likedItemList);

    void showEmptyListMessage();
}
