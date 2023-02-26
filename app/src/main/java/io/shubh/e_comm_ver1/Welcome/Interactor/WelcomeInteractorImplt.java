package io.shubh.e_comm_ver1.Welcome.Interactor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class WelcomeInteractorImplt implements WelcomeInteractor {

    FirebaseFirestore db ;
    CallbacksToPresnter mPresenter;
    String TAG ="WelcomeInteractorImplt";


    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter =mPresenter;
    }

    @Override
    public void push_the_uid_to_users_node(Context context) {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        //below code checks if the uid is alraedy in database or not to know whether its the first time login otr not

        db.collection("users").document(currentFirebaseUser.getUid())
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //user has alraedy used logged to this app in past
                        Log.d(TAG, "Document exists!");

                        //storing the token (on every login) of user from local storage in case if it would have refresged
                        db = FirebaseFirestore.getInstance();
                        Map<String, Object> userMap = new HashMap<>();
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context,  new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();
                                Log.e("newToken",newToken);
                                userMap.put("token", newToken);
                                db.collection("users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update(userMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                mPresenter.onFinishedPushingTheUidToUsersNode();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: "+"of storing the token on login ");
                                    }
                                });

                            }
                        });




                    } else {

                        //user is first time login
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);

                        Log.d(TAG, "user does not exist!");

                        Map<String, Object> user_node_data = new HashMap<>();
                        user_node_data.put("uid", currentFirebaseUser.getUid());
                        user_node_data.put("name", acct.getDisplayName());
                        user_node_data.put("email", acct.getEmail());
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context,  new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();
                                Log.e("newToken",newToken);

                                user_node_data.put("token", newToken);
                                //below value is false by default
                                user_node_data.put("is a seller also ?", false);
                                //below i m giving sellerbusiness a default name ..even when now user is not a seller now..I m giving here now just in case user become  seller then a default name for him ..//todo ..ask for seller name at sellelr confiramtion ..if this app is ever used for actual business
                                user_node_data.put("seller or business name",acct.getDisplayName() );

                                db.collection("users").document(currentFirebaseUser.getUid())
                                        .set(user_node_data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i(TAG, "DocumentSnapshot successfully written!");

                                                mPresenter.onFinishedPushingTheUidToUsersNode();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });
                            }
                        });

                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });

    }



 /*   @Override
    public void checkSomethingInDatabase() {



        //after checking something ..communicating the results back to presenter ..we have a call back
       // mPresenter.onFinishedCheckingSomething1();
    }

    @Override
    public void getItemsFromFirebaseWithResultsOnSeparateCallback(SeparateCallbackToPresnterForSystemUpdate l) {

        //after checking something ..communicating the results back to presenter ..we have a call back
        l.onFinishedGettingItems(true);
    }
*/


}
