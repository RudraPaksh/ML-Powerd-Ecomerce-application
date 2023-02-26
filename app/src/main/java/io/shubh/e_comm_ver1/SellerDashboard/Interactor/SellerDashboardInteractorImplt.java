package io.shubh.e_comm_ver1.SellerDashboard.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;

public class SellerDashboardInteractorImplt implements SellerDashboardInteractor {

    FirebaseFirestore db;
    CallbacksToPresnter mPresenter;

    String TAG = "#####";

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter = mPresenter;


    }

    @Override
    public void getSellerDataWithArgAsCallback(SeparateCallbackToPresnterAfterGettingSellerData l) {

        CollectionReference collectionReference = db.collection("orders");
        collectionReference.whereArrayContains("sellers in order", StaticClassForGlobalInfo.UId)
                .orderBy("creation of order", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Order> list = null;
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                if (task.getResult().size() != 0) {
                                    list = task.getResult().toObjects(Order.class);

                                    // l.onFinished(true, list);
                                    ArrayList<Order.SubOrderItem> subOrderlist = new ArrayList<>();
                                    getTheSubOrderItemObjectList(0, list, subOrderlist, l);
                                } else {
                                    //TODO-show toast of no items found
                                    //no items found ..so just sending an empty bagitemlist back to presenter ..it will check it itself
                                    List<Order> orderItemslist = new ArrayList<>();
                                    l.onFinished(true, new ArrayList<Order.SubOrderItem>());

                                }
                            }

                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            l.onFinished(false, new ArrayList<Order.SubOrderItem>());
                        }
                    }
                });

    }

    @Override
    public void getSellerUploadedItemsWithArgAsCallback(SeparateCallbackToPresnterAfterGettingSellerItems l) {


        Query query = db.collection("items for sale")
                .whereEqualTo("seller id", StaticClassForGlobalInfo.UId)
                .orderBy("item id", Query.Direction.ASCENDING);

        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {


                                if (task.getResult().size() != 0) {
                                    List<ItemsForSale> list = task.getResult().toObjects(ItemsForSale.class);

                               l.onFinished(true, (ArrayList<ItemsForSale>) list);
                                  } else {

                                 //   Log.i("***", "first call result has 0 size ");
                                    l.onFinished(true, new ArrayList<>());

                                }


                            }

                        } else {
                            Log.e("CategoryItemsInteractor", "Error getting documents: ", task.getException());
                            l.onFinished(false, new ArrayList<>());

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CategoryItemsInteractor", "Error getting documents: ", e);

                        l.onFinished(false, new ArrayList<>());
                    }
                });
    }


    //recrsive fnction
    private void getTheSubOrderItemObjectList(int index, List<Order> list, ArrayList<Order.SubOrderItem> subOrderlist, SeparateCallbackToPresnterAfterGettingSellerData l) {

        if (index < list.size()) {
        //    Log.i(TAG, "getTheSubOrderItemObjectList: "+ list.get(index).getParentOrderId());
            CollectionReference collectionReference = db.collection("orders").document(list.get(index).getOrderId()).collection("sub order items");
            collectionReference  .whereEqualTo("seller id", StaticClassForGlobalInfo.UId)
                 //   .orderBy("creation of order", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Order.SubOrderItem> subOrderlistLocal = null;
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {
                                  //  if (task.getResult().size() != 0) {
                                        subOrderlistLocal = task.getResult().toObjects(Order.SubOrderItem.class);
                                        // for(int i=0;i<task.getResult().size();i++) {

                                    for(int i=0;i<subOrderlistLocal.size();i++){
                                        subOrderlistLocal.get(i).setParentOrderId(list.get(index).getOrderId());
                                        subOrderlistLocal.get(i).setTimeOfCreationOfOrder(list.get(index).getTimeOfCreationOfOrder());
                                        subOrderlistLocal.get(i).setAdressItem(list.get(index).getAdressItem());

                                    }
                                        subOrderlist.addAll(subOrderlistLocal);
                                        //}
                                        // l.onFinished(true, list);

                                        getTheSubOrderItemObjectList(index + 1, list, subOrderlist, l);
                                   /* } else {
                                        //TODO-show toast of no items found
                                        //no items found ..so just sending an empty bagitemlist back to presenter ..it will check it itself
                                        List<Order> orderItemslist = new ArrayList<>();
                                        l.onFinished(true, subOrderlist);

                                    }*/
                                }

                            } else {
                                Log.e(TAG, "Error getting documents: ", task.getException());
                                l.onFinished(false, subOrderlist);
                            }
                        }
                    });
        } else {
            l.onFinished(true, subOrderlist);
        }

    }


}
