package io.shubh.e_comm_ver1.SellerDashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import io.shubh.e_comm_ver1.SellerDashboard.View.SellerDashboardFragment;
import io.shubh.e_comm_ver1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SellerConfirmationFragment extends Fragment {
    //this fragment doesnt have factory methods like ''newinstance' method ..and bundle args passed
    View containerViewGroup;

    public SellerConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerViewGroup = inflater.inflate(R.layout.fragment_seller_confirmation, container, false);

        Button btLater = (Button) containerViewGroup.findViewById(R.id.bt_later);
        btLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SellerDashboardFragment sellerDashboardFragment = new SellerDashboardFragment();
             sellerDashboardFragment.setEnterTransition(new Slide(Gravity.RIGHT));
             sellerDashboardFragment.setExitTransition(new Slide(Gravity.RIGHT));

                getActivity().getSupportFragmentManager().beginTransaction()
                        //both parameters for instantiating the fragment will be same as at rootl level of ctgr tree ,the name of ctgr and path is same
                        .add(R.id.drawerLayout,sellerDashboardFragment,"SellerDashboardFragment")
                        .addToBackStack(null)
                        .commit();

                //I have commented the below code because I m using tokens instead of topics for now
                //for the purpose of notifs but ..Tokens are also neccessary in case I nedd to give a
                //announcement kind of notif to every seller..but since this app is not have real purpose tus commented for now
                
               /* //Todo- this subscription below is invoked each time seller confirmation fragment is open
                //todo -so make the user become a seller for one time opnly...in the future
                //since the user has chose to become seller ..we need it to be subscribed to the notification meant for sellers
                FirebaseMessaging.getInstance().subscribeToTopic("notificationsForSellers").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "some thing went wrong", Toast.LENGTH_LONG).show();

                    }
                });*/
            }
        });

        ImageButton btCloseFrag = (ImageButton) containerViewGroup.findViewById(R.id.btCloseFrag);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              closeFragment();

            }
        });


        return containerViewGroup;
    }

    public  void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(SellerConfirmationFragment.this)
                .addToBackStack(null)
                .commit();
    }
}
