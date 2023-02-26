package io.shubh.e_comm_ver1.SearchPage.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;

public class SearchResultsInteractorImplt implements SearchResultsInteractor {

    FirebaseFirestore db;
    CallbacksToPresnter mPresenter;
    //int noOfItemsRetrivedTillNo=0;

    int idodLastItemRetrived = 0;
    String ctgrPath = null;

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter = mPresenter;


    }

    @Override
    public void getTheItemsHavingSearchKeywordsInthierName(List<String> listOfKeywords, SeparateCallbackToPresnterAfterGettingItems l) {

        List<ItemsForSale> list = new ArrayList<>();


        //since query doesnt support multiplw wherearraycontains ..so I have to make multiple queries
        for (int i = 0; i < listOfKeywords.size(); i++) {
            Query query = db.collection("items for sale")
                    .whereEqualTo("visibility", true)
                    .whereArrayContains("array of item name keywords", listOfKeywords.get(i));


//      for(int i=0 ;i<listOfKeywords.size();i++){
//            query.whereArrayContains("array of item name keywords",listOfKeywords.get(i));
//       }
      /*  query.orderBy("item id", Query.Direction.ASCENDING)
        ;*/

            int finalI = i;
            query
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {
                                    //    if (task.getResult().size() != 0) {
                                      List<ItemsForSale> sublist = task.getResult().toObjects(ItemsForSale.class);
                                    list.addAll(sublist);

                                    if (finalI == listOfKeywords.size() - 1) {
                                        if (list.size() != 0) {
                                            checkIfTheseItemsExistInSavedItemsList((ArrayList<ItemsForSale>) list, l);
                                        } else {
                                            l.onFinished(true, list);
                                        }
                                    }
                                    // }
                                }

                            } else {
                                Log.e("CategoryItemsInteractor", "Error getting documents: ", task.getException());

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("CategoryItemsInteractor", "Error getting documents: ", e);

                        }
                    });
        }
    }


    private void checkIfTheseItemsExistInSavedItemsList(ArrayList<ItemsForSale> itemsList, SeparateCallbackToPresnterAfterGettingItems l) {
        //   checkIfTheseItemsExistInSavedItemsListRecrsiveFunction(0, itemsList, lsitNotEmpty, ctgrName, ifItsALoadMorecall);

//before I have implemented the below code using recursive function(fnction is below)..but it was slower due to serial read so.. below is iterative version
        for (int i = 0; i < itemsList.size(); i++) {

            int finalI = i;
            db.collection("liked items").document(String.valueOf(itemsList.get(i).getItem_id()) + StaticClassForGlobalInfo.UId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //  Log.d(TAG, "Document exists!");
                            itemsList.get(finalI).setItemLiked(true);

                            if (finalI == itemsList.size() - 1) {
                                l.onFinished(true, itemsList);
                            }

                        } else {
                            //  Log.d(TAG, "Document does not exist!");
                            itemsList.get(finalI).setItemLiked(false);


                            if (finalI == itemsList.size() - 1) {
                                l.onFinished(true, itemsList);
                            }

                        }
                    } else {
                        Log.d("ctgr item interactor", "Failed on getting liked items: ", task.getException());
                        //even tho this read opr is failing I will send result as success because it can show the item list we have already retrived
                        l.onFinished(true, itemsList);
                    }
                }
            });

        }
    }

}
