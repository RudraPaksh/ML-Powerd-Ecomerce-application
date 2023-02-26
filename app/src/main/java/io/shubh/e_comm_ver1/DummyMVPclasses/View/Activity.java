package io.shubh.e_comm_ver1.DummyMVPclasses.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import io.shubh.e_comm_ver1.DummyMVPclasses.Presenter.Presenter;
import io.shubh.e_comm_ver1.DummyMVPclasses.Presenter.PresenterImplt;
import io.shubh.e_comm_ver1.Welcome.View.WelcomeActivity;
import io.shubh.e_comm_ver1.Main.View.MainActivity;
import io.shubh.e_comm_ver1.DummyMVPclasses.Interactor.InteractorImplt;
import io.shubh.e_comm_ver1.R;


public class Activity extends AppCompatActivity implements View {

    ImageView splashimageicon;
    ProgressBar progressBar;
    Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        DoUiWork();

        //always do presenter related work at last in Oncreate
        mPresenter = new PresenterImplt(this, new InteractorImplt() {
        });


    }

    private void DoUiWork() {
        splashimageicon=(ImageView)findViewById(R.id.imagesplash);

        splashimageicon.setVisibility(android.view.View.VISIBLE);

         progressBar =(ProgressBar)findViewById(R.id.id_fr_prggrs_bar_splash_screen);


    }



    @Override
    public void switchActivity(int i) {
        progressBar.setVisibility(android.view.View.GONE);

        if(i==1) {
            Intent in = new Intent(Activity.this, MainActivity.class);
            startActivity(in);
        } else if(i==2){
            //  showToast("not logged in");
            Intent in = new Intent(Activity.this, WelcomeActivity.class);
            startActivity(in);
        }
    }

    @Override
    public Context getContext(boolean getActvityContext) {

        if(getActvityContext==true){
            return this;
        }
        return this.getApplicationContext();
    }

    @Override
    public void showProgressBar(boolean b) {
        if(b==true) {
            progressBar.setVisibility(android.view.View.VISIBLE);
        }else {
            progressBar.setVisibility(android.view.View.INVISIBLE);

        }
    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {
        LinearLayout ll_Root = (LinearLayout)findViewById(R.id.layoutsplash);
        Snackbar snackbar = Snackbar
                .make(ll_Root , msg, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(actionName, new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {
                        mPresenter.LoginRelatedWork();

                    }
                });

        snackbar.show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(Activity.this, msg, Toast.LENGTH_LONG).show    ();

    }
}