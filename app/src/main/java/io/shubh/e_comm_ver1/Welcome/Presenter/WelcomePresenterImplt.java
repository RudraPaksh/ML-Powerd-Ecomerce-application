package io.shubh.e_comm_ver1.Welcome.Presenter;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Welcome.View.WelcomeView;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.Welcome.Interactor.WelcomeInteractor;

public class WelcomePresenterImplt implements WelcomePresenter, WelcomeInteractor.CallbacksToPresnter {

    private WelcomeView welcomeView;
    private WelcomeInteractor mInteractor;

    private FirebaseAuth mAuth;
    private int RC_SIGN_IN=7;
    GoogleSignInClient mGoogleSignInClient;

    String TAG = "WelcomeActivty";

    public WelcomePresenterImplt(WelcomeView welcomeView , WelcomeInteractor mSplashInteractor) {
       this.welcomeView=welcomeView;
       this.mInteractor = mSplashInteractor;

       mInteractor.init(this);

        mAuth = FirebaseAuth.getInstance();
        setUpGoogleSignIn();

    }
    @Override
    public void onSignInButtonClicked() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        welcomeView.startActivityForResultt(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onGettingActivityResultOfGoogleSignIn(int requestCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                welcomeView.showProgressBar(true);

                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("WelcomeActivty", "Google sign in failed", e);
                welcomeView.showToast("Google sign in failed");
                welcomeView.showProgressBar(false);
                // ...
            }
        }
    }

    private void setUpGoogleSignIn() {


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(welcomeView.getStringFromRes(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(welcomeView.getContext(false), gso);




    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((AppCompatActivity) welcomeView.getContext(true), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                          //  Log.d(TAG, "signInWithCredential:success");
                           // FirebaseUser user = mAuth.getCurrentUser();

                            // StaticClassForGlobalInfo.isLoggedIn =true;

                            mInteractor.push_the_uid_to_users_node(welcomeView.getContext(true));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
welcomeView.showToast("Not Able To login to Google");                            // updateUI(null);

welcomeView.showProgressBar(false);
                        }

                    }
                });
    }




/*
    @Override
    public void LoginRelatedWork() {


        mInteractor.getItemsFromFirebaseWithResultsOnSeparateCallback( new WelcomeInteractor.SeparateCallbackToPresnterForSystemUpdate(){
            @Override
            public void onFinishedGettingItems(boolean callbackResultOfTheCheck) {

              if(callbackResultOfTheCheck==true){
                  //system upadte available ..so throw a dialog asking to download update
              }else{
                  //system upadte not available ..so continue
              }
            }
        });
    }*/




    @Override
    public void onFinishedPushingTheUidToUsersNode() {


        googleSignInExtractInformationAndStoreGlobally();
       welcomeView.showProgressBar(false);

       welcomeView.switchActivity(1);


    }
    private void googleSignInExtractInformationAndStoreGlobally() {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(welcomeView.getContext(false));
        if (acct != null) {
            // String personName = acct.getDisplayName();
           /* String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();*/
            //     String personId = acct.getId();
            //   Uri personPhoto = acct.getPhotoUrl();

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            //     currentFirebaseUser.getUid();


            Log.d("&&&&&&&&&&&&&&&&&&&", "name -"+acct.getDisplayName() +" email - " +acct.getId());


            StaticClassForGlobalInfo.UId  = currentFirebaseUser.getUid();
            StaticClassForGlobalInfo.UserEmail  = acct.getEmail();
            StaticClassForGlobalInfo.UserName  =  acct.getDisplayName();
           // StaticClassForGlobalInfo.isLoggedIn  =  true;

            welcomeView.showProgressBar(false);
        }

    }



    @Override
    public void onClickingNotNowButton() {
       // StaticClassForGlobalInfo.isLoggedIn =false;

        welcomeView.switchActivity(1);
    }


}
