package io.shubh.e_comm_ver1.DummyMVPclasses.View;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.shubh.e_comm_ver1.DummyMVPclasses.Presenter.Presenter;
import io.shubh.e_comm_ver1.DummyMVPclasses.Presenter.PresenterImplt;
import io.shubh.e_comm_ver1.DummyMVPclasses.Interactor.InteractorImplt;
import io.shubh.e_comm_ver1.R;


public class Fragment extends androidx.fragment.app.Fragment implements io.shubh.e_comm_ver1.DummyMVPclasses.View.View {

    View containerViewGroup;
    LayoutInflater inflater;
    Presenter mPresenter;

    public Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        containerViewGroup = inflater.inflate(R.layout.fragment_my_orders, container, false);
        this.inflater = inflater;
        
        mPresenter = new PresenterImplt(this, new InteractorImplt() {
        });

        DoUiWork();


        // Inflate the layout for this fragment
        return containerViewGroup;
    }

    private void DoUiWork() {

        setUpToolbar();
    }


    private void setUpToolbar() {
        ImageButton btCloseFrag = (ImageButton) containerViewGroup.findViewById(R.id.btCloseFrag);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackButtonPressed();
            }
        });
    }


    //below function is for catching back button pressed
    private void attachOnBackBtPressedlistener() {
        containerViewGroup.setFocusableInTouchMode(true);
        containerViewGroup.requestFocus();
        containerViewGroup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackButtonPressed();
                    return true;
                }
                return false;
            }
        });
    }

    private void onBackButtonPressed() {

    //    getFragmentManager().beginTransaction().remove(Fragment.this).commit();


    }


    @Override
    public void switchActivity(int i) {
        
    }

    @Override
    public Context getContext(boolean getActvityContext) {
        return null;
    }

    @Override
    public void showProgressBar(boolean b) {

    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {

    }

    @Override
    public void showToast(String msg) {

    }
}
