package io.shubh.e_comm_ver1.MyProfile;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.shubh.e_comm_ver1.AddressSelectionPage.View.AddressSelectionFragment;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    View containerViewGroup;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerViewGroup = inflater.inflate(R.layout.fragment_my_profile, container, false);

        doUiWork();
        
        return containerViewGroup;
    }

    private void doUiWork() {
        ImageButton btCloseFrag = (ImageButton) containerViewGroup.findViewById(R.id.btCloseFrag);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeFragment();

            }
        });

        //-----------setting Up tvs
        TextView tv_for_nav_dr_name = (TextView) containerViewGroup.findViewById(R.id.tv_for_nav_dr_name);
        TextView tv_for_nav_dr_email = (TextView) containerViewGroup.findViewById(R.id.tv_for_nav_dr_email);
        tv_for_nav_dr_name.setText(StaticClassForGlobalInfo.UserName);
        tv_for_nav_dr_email.setText(StaticClassForGlobalInfo.UserEmail);

        LinearLayout llMyAdresses = (LinearLayout) containerViewGroup.findViewById(R.id.llMyAdresses);
        llMyAdresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressSelectionFragment addressSelectionFragment = new AddressSelectionFragment();
                addressSelectionFragment.setLocalVariables(false, new Order());
                addressSelectionFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                addressSelectionFragment.setExitTransition(new Slide(Gravity.RIGHT));

                getActivity().getSupportFragmentManager().beginTransaction()
                        //both parameters for instantiating the fragment will be same as at rootl level of ctgr tree ,the name of ctgr and path is same
                        .add(R.id.drawerLayout,addressSelectionFragment,"AddressSelectionFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    public void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(MyProfileFragment.this)
                .addToBackStack(null)
                .commit();
    }
}
