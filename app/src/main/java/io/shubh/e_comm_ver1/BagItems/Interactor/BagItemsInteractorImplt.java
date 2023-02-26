package io.shubh.e_comm_ver1.BagItems.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Models.BagItem;

public class BagItemsInteractorImplt implements BagItemsInteractor {

    FirebaseFirestore db;
    CallbacksToPresnter mPresenter;
    String TAG = "#####";

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter = mPresenter;
    }

    @Override
    public void getbagItemsDataWithArgAsCallbackFunction(SeparateCallbackToPresnterAfterGettingTheObjectList l) {

        Query query = db.collection("bag or cart items")
                .whereEqualTo("user id", StaticClassForGlobalInfo.UId)
                .orderBy("time of upload", Query.Direction.DESCENDING);


        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<BagItem> list = null;
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                if (task.getResult().size() != 0) {
                                    list = task.getResult().toObjects(BagItem.class);

                                    getTheItemObjectIndivisually(0, list, l);
                                } else {
                                    //TODO-show toast of no items found
                                    //no items found ..so just sending an empty bagitemlist back to presenter ..it will check it itself
                                    List<BagItem> BagItemlist = new ArrayList<>();
                                    l.onFinished(true, BagItemlist);

                                }
                            }

                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            l.onFinished(false, list);
                        }
                    }
                });


    }


    //below is a recursive function
    private void getTheItemObjectIndivisually(int index, List<BagItem> BagItemlist, SeparateCallbackToPresnterAfterGettingTheObjectList l) {


        if (index < BagItemlist.size()) {

            Query query = db.collection("items for sale")
                    .whereEqualTo("item id", Integer.valueOf(BagItemlist.get(index).getItemId()));

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {
                                    //below list contain only one object
                                    List<ItemsForSale> itemsForSaleObject = task.getResult().toObjects(ItemsForSale.class);

                                    if (itemsForSaleObject.size() != 0) {
                                        BagItemlist.get(index).setItemObject(itemsForSaleObject.get(0));

                                        getTheItemObjectIndivisually(index + 1, BagItemlist, l);


                                    } else {
                                        BagItemlist.get(index).setTheOriginalItemDeleted(true);

                                        getTheItemObjectIndivisually(index + 1, BagItemlist, l);
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error getting documents: ", e);

                            l.onFinished(false, BagItemlist);
                        }
                    });
        } else {

            //   Log.i(TAG, "finished all work");


            l.onFinished(true, BagItemlist);
            //calling back to presenter
        }

    }


    @Override
    public void deletebagItemsWithArgAsCallbackFunction(String docId, SeparateCallbackToPresnterAfterDeletingBagItem l) {

        Task<Void> query = db.collection("bag or cart items")
                .document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        l.onFinished(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        l.onFinished(false);
                    }
                });
    }


}






