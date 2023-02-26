package io.shubh.e_comm_ver1.MyOrders.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Models.Order;

public class MyOrdersInteractorImplt implements MyOrdersInteractor {

    FirebaseFirestore db ;
    CallbacksToPresnter mPresenter;
    String TAG ="MyOrdersInteractorImplt";

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter =mPresenter;
    }

    @Override
    public void getOrderItemsDataWithArgAsCallbackFunction(SeparateCallbackToPresnterAfterGettingOrderItems l) {


        Query query = db.collection("orders")
                .whereEqualTo("buyer id", StaticClassForGlobalInfo.UId)
                .orderBy("creation of order", Query.Direction.DESCENDING);


        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Order> list = null;
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                if (task.getResult().size() != 0) {
                                    list = task.getResult().toObjects(Order.class);

                                    Log.i(TAG, "getbagitem retrived ..size of list" + list.size());

                                   // l.onFinished(true, list);

                                      getTheSubOrderItemObjectList(0,list, l);
                                } else {
                                    //TODO-show toast of no items found
                                    //no items found ..so just sending an empty bagitemlist back to presenter ..it will check it itself
                                    List<Order> orderItemslist = new ArrayList<>();
                                    l.onFinished(true, orderItemslist);

                                }
                            }

                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            l.onFinished(false, list);
                        }
                    }
                });

    }

    private void getTheSubOrderItemObjectList( int index ,List<Order> list, SeparateCallbackToPresnterAfterGettingOrderItems l) {


        if (index < list.size()) {
            db.collection("orders").document(list.get(index).getOrderId()).collection("sub order items")
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<Order.SubOrderItem> subOrderItems=new ArrayList<>();
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            Order.SubOrderItem  subOrderItem = document.toObject(Order.SubOrderItem.class);
                            subOrderItems.add(subOrderItem);
                        }
                       list.get(index).setSubOrderItems( subOrderItems);

                        getTheSubOrderItemObjectList(index+1 ,list ,l);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    l.onFinished(false ,list);
                    Log.e(TAG, "onFailure: "+e );
                }
            });
        } else{

            l.onFinished(true ,list);
        }





    }



}
