package io.shubh.e_comm_ver1.OrderListFrSellerFragment.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.shubh.e_comm_ver1.Models.Order;

public class NewOrderListFrSellerInteractorImplt implements NewOrderListFrSellerInteractor {

    FirebaseFirestore db ;
    CallbacksToPresnter mPresenter;
    String TAG ="NewOrderListFrSellerInteractorImplt";

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter =mPresenter;
    }

    @Override
    public void UpdateStatusInDatabaseWithArgAsCallbackFunction(Order.SubOrderItem subOrderItem, int nextStatusNoToUpdate, SeparateCallbackToPresnterAfterOrderStatusUpdate l) {

        Map<String, Object> data = new HashMap<>();
        data.put("StatusOfOrder", nextStatusNoToUpdate);
        if (nextStatusNoToUpdate == 3) {
            data.put("TimeOfPackagedOfItem", subOrderItem.getTimeOfPackagedOfItem());
        } else if (nextStatusNoToUpdate == 4) {
            data.put("TimeOfShippedOfItem", subOrderItem.getTimeOfShippedOfItem());
        } else if (nextStatusNoToUpdate == 5) {
            data.put("TimeOfDeliveryOfItem", subOrderItem.getTimeOfDeliveryOfItem());
        }

        db.collection("orders").document(subOrderItem.getParentOrderId()).collection("sub order items").document(subOrderItem.getItemId())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("####", "ityem uploadwd ");

updateBuyer(subOrderItem,nextStatusNoToUpdate,l);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error uploading image", e);
                        l.onFinished(false, nextStatusNoToUpdate);
                    }
                });
    }

    private void updateBuyer(Order.SubOrderItem subOrderItem, int nextStatusNoToUpdate, SeparateCallbackToPresnterAfterOrderStatusUpdate l) {

        DocumentReference pg =db.collection("users").document(subOrderItem.getBuyerId());
        pg.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String token = (String) doc.get("token");

                    //2nd step ..making and putting notif inside the collection called notifuactions under
                    // the user document

                    Map<String, Object> notif = new HashMap<>();
                    notif.put("title", "order Status Updated");
                    notif.put("content", "Seller Has updated order status for item "+ subOrderItem.getItemName());
                    notif.put("image url",subOrderItem.getImageUrl());
                    notif.put("token", token);
                    notif.put("has it been read", false);
                    Long time =System.currentTimeMillis() / 1000L;
                    notif.put("time of upload", time);
                    notif.put("type", "2"); //type - 1 means its for seller ..so open that fragment and type - 3 means its for buyer

                    //     notif.put("topic", "notificationsForSellers");

                    db.collection("users").document(subOrderItem.getBuyerId())
                            .collection("notifications").document(String.valueOf(time))
                            .set(notif)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    l.onFinished(true ,nextStatusNoToUpdate);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Error writing document of subcollection notification", e);
                                    l.onFinished(false ,nextStatusNoToUpdate);
                                }
                            });


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
                l.onFinished(false ,nextStatusNoToUpdate);
            }
        });

    }


}
