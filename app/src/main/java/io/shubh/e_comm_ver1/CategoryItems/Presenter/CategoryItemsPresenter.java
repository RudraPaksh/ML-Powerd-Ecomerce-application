package io.shubh.e_comm_ver1.CategoryItems.Presenter;

public interface CategoryItemsPresenter {

    void getItemsFromFirebase(String param1CategoryName, String param2CategoryPath, String rootCtgr, String mParam1CategoryName, String mParam2CategoryPath, boolean ifItsALoadMorecall);

    void saveTheItemToLikedItems(String docId);

    void deleteTheItemFromLikedItems(String docId);

}
