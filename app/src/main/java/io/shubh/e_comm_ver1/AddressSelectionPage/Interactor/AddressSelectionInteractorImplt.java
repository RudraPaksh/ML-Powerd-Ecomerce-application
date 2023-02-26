package io.shubh.e_comm_ver1.AddressSelectionPage.Interactor;

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

import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Models.AdressItem;

public class AddressSelectionInteractorImplt implements AddressSelectionInteractor {

    FirebaseFirestore db;
    CallbacksToPresnter mPresenter;
    String TAG = "#####";

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter = mPresenter;
    }

    @Override
    public void getAddressDataWithArgAsCallbackFunction(SeparateCallbackToPresnterAfterGettingTheObjectList l) {

//the data is in the subcollection indise  a user document inside user collection
        //the address item is kept seperately ,unlike keeping it inside  a single global collection like items
        //because items for sale documents were for to show to everyone..but address are for the to show to one who put them


        db.collection("users").document(StaticClassForGlobalInfo.UId).collection("addresses")
             .orderBy("time of upload",Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<AdressItem> list = task.getResult().toObjects(AdressItem.class);
                    l.onFinished(true, list);

                } else {

                    List<AdressItem> list = new ArrayList<>();
                    l.onFinished(false,list );
                    Log.d("TAG", "Errorgettingdocuments:", task.getException());
                }
            }
        });

    }

    @Override
    public void addAddressObjectWithArgAsCallbackFunction(AdressItem adressItem, SeparateCallbackToPresnterAfterAddingAddressObject l) {

        Log.i("####", "call for craete reached to interactor");

        db.collection("users").document(StaticClassForGlobalInfo.UId).collection("addresses").document(String.valueOf(adressItem.getTimeOfUpload()))
                .set(adressItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("####", "ityem uploadwd ");

                        l.onFinished(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error uploading image", e);
                        l.onFinished(false);
                    }
                });
    }

    @Override
    public void deletebagItemsWithArgAsCallbackFunction(String docId, SeparateCallbackToPresnterAfterDeletingBagItem l) {

      db.collection("users").document(StaticClassForGlobalInfo.UId).collection("addresses")
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






