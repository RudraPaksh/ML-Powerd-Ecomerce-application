package io.shubh.e_comm_ver1.Splash.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


import io.shubh.e_comm_ver1.Splash.Interactor.SplashInteractor;
import io.shubh.e_comm_ver1.Splash.View.Splashview;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;

public class SplashPresenterImplt implements SplashPresenter, SplashInteractor.CallbacksToPresnter {

    private Splashview splashview;
    private SplashInteractor mInteractor;
    private FirebaseAuth mAuth;

    public SplashPresenterImplt(Splashview splashview , SplashInteractor mSplashInteractor) {
       this.splashview=splashview;
       this.mInteractor = mSplashInteractor;

       //mInteractor.init(this);


//checkForSystemUpdates();

     //   LoginRelatedWork();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;



        ConnectivityManager cm = (ConnectivityManager)  splashview.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void LoginRelatedWork() {

        if (haveNetworkConnection()){

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //checking if user already is logged in
        if (user != null) {
            // User is signed in.
            splashview.showToast("Logged in already,welcome again");


            boolean googleSignIn = false;

            //now to find out whther the method of login is using google sign in or email with filrebase
            for (UserInfo userinfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (userinfo.getProviderId().equals("google.com")) {
                    googleSignIn = true;
                }
            }

            if (googleSignIn == true) {
                //means user had logged in using google sign in

                // so use google code and method to extract user data
                googleSignInExtractInformationAndStoreGlobally();
                splashview.SwitchActivity(1);

                //animation for sliding activity
                // overridePendingTransition(R.anim.right_in, R.anim.left_out);


            } else {
                //user had logged in using other signin option ..so use other methods to extract data...or maybe we can extraxt data from user table in firebase database using the Uid provided by firebase ...for that we need to ask for esssential info at the time of sign up using other method ...if the firebase email authentication is nt able to get the infromation himself

            }


        } else {
            // No user is signed in.
            splashview.showToast("Not logged In already");
            //redirecting him to welcome activity
            splashview.SwitchActivity(2);

            //animation for sliding activity
            //   overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

    }else{
            splashview.ShowSnackBarWithAction("Network not found", "Try again");
        }
    }

    private void googleSignInExtractInformationAndStoreGlobally() {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(splashview.getContext());
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
          //  StaticClassForGlobalInfo.isLoggedIn  =  true;
        }

    }




    @Override
    public void onFinishedCheckingUserAlreadyExists() {

    }




}
