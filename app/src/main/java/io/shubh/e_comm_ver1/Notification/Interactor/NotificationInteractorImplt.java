package io.shubh.e_comm_ver1.Notification.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.NotifcationObject;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;

public class NotificationInteractorImplt implements NotificationInteractor {

    FirebaseFirestore db;
    CallbacksToPresnter mPresenter;
    int idodLastItemRetrived = 0;
    String TAG = "NotificationInteractorImplt";


    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter = mPresenter;
    }

    @Override
    public void getTheFirstItemDocumentAsAReferenceForStartAtFunct(boolean isItLoadMoreCall, SeparateCallbackToPresnterAfterGettingNotifData l) {

        ArrayList<NotifcationObject> itemsList = new ArrayList<>();

        if (isItLoadMoreCall == false) {

            Query query = null;

            query = db.collection("users").document(StaticClassForGlobalInfo.UId)
                    .collection("notifications")
                    .orderBy("time of upload", Query.Direction.ASCENDING)
                    .limit(1);


            query
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {
                                    if (task.getResult().size() != 0) {
                                        List<NotifcationObject> list = task.getResult().toObjects(NotifcationObject.class);

                                        itemsList.add(list.get(0));

                                        idodLastItemRetrived = (int) itemsList.get(0).getTime();
                                        getItemsFromFirebaseWithResultsOnSeparateCallback(false, itemsList ,l);
                                    } else {
                                        Log.i("***", "first call result has 0 size ");
                                        l.onFinished(itemsList, false, isItLoadMoreCall);
                                    }
                                }

                            } else {
                                Log.e(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error getting documents: ", e);

                        }
                    });

        } else {
            getItemsFromFirebaseWithResultsOnSeparateCallback(true, itemsList, l);
        }
    }


    public void getItemsFromFirebaseWithResultsOnSeparateCallback(boolean ifItsALoadMorecall, ArrayList<NotifcationObject> itemsList, SeparateCallbackToPresnterAfterGettingNotifData l) {


        int pageSize = 0;
        if (ifItsALoadMorecall == true) {
            pageSize = 12;
        } else {
            pageSize = 11;
        }


        Query query = db.collection("users").document(StaticClassForGlobalInfo.UId)
                .collection("notifications")
                .orderBy("time of upload", Query.Direction.ASCENDING)
                .startAfter(idodLastItemRetrived)//<------This below function decides after which document ,all other documents need to be fetched
                //for first time I will pass it the very first function,after that the last document i have
                //Also the field of orderBy and startafter should be same
                .limit(pageSize);


        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                //  Log.i("******", String.valueOf(task.getResult().size()));

                                if (task.getResult().size() != 0) {

                                    List<NotifcationObject> list = task.getResult().toObjects(NotifcationObject.class);
                                    itemsList.addAll(list);


                                    idodLastItemRetrived = (int) itemsList.get(itemsList.size() - 1).getTime();

                                     l.onFinished(itemsList, true, ifItsALoadMorecall);


                                } else {

                                    if (itemsList.isEmpty()) {
                                        if (ifItsALoadMorecall == true) {
                                            //TODO-showToast..of No more items found  ..after scrolling to bottom
                                            mPresenter.showToast("No more Items found");
                                        }
                                    } else {

                                          l.onFinished(itemsList, true, ifItsALoadMorecall);
                                    }
                                }


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
