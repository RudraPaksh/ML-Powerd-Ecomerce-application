package io.shubh.e_comm_ver1.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onurkagan.ksnack_lib.Animations.Slide;
import com.onurkagan.ksnack_lib.KSnack.KSnack;

import io.shubh.e_comm_ver1.Welcome.View.WelcomeActivity;
import io.shubh.e_comm_ver1.R;

public final class Utils {
    //  public static boolean isLoggedIn =false;
    public static boolean isUserLoggedIn() {
        boolean isLoggedIn = false;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //user is signed in
            isLoggedIn = true;
        } else {
            //user is not signed in
            isLoggedIn = false;
        }
        return isLoggedIn;
    }

    public static void showToast(String msg, Activity activity) {


        KSnack kSnack = new KSnack(activity);


        kSnack.setMessage(msg)
                // message
                .setTextColor(R.color.colorPrimary) // message text color
                .setBackColor(R.color.colorSecondary) // background color
                .setButtonTextColor(R.color.colorPrimaryDark) // action button text color
                // .setBackgrounDrawable(R.drawable.background_ex_one) // background drawable
                .setAnimation(Slide.Up.getAnimation(kSnack.getSnackView()), Slide.Down.getAnimation(kSnack.getSnackView()))
                .setDuration(2000) // you can use for auto close.
                .show();
    }


    public static void showKsnackForLogin(Activity activity) {

        //  if(StaticClassForGlobalInfo.theme==1) {
        KSnack kSnack = new KSnack(activity);
        kSnack
                .setAction("Login", new View.OnClickListener() { // name and clicklistener
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(activity, WelcomeActivity.class);
                        activity.startActivity(in);
                        kSnack.dismiss();
                    }
                })
                .setMessage("Login Required") // message
                .setTextColor(R.color.colorPrimaryLight) // message text color
                .setBackColor(R.color.colorPrimaryDark) // background color
                .setButtonTextColor(R.color.colorSecondary) // action button text color
                //  .setBackgrounDrawable(R.drawable.background_ex_one) // background drawable
                .setAnimation(Slide.Up.getAnimation(kSnack.getSnackView()), Slide.Down.getAnimation(kSnack.getSnackView()))
                .setDuration(2500); // you can use for auto close.


        if (StaticClassForGlobalInfo.theme == 1) {
            kSnack.setTextColor(R.color.colorPrimaryLight) // message text color
                    .setBackColor(R.color.colorPrimaryDark) // background color
                    .setButtonTextColor(R.color.colorSecondary); // action button text color
        } else {
            kSnack.setTextColor(R.color.colorPrimaryDark) // message text color
                    .setBackColor(R.color.colorPrimaryLight) // background color
                    .setButtonTextColor(R.color.colorSecondary);
        }
        kSnack.show();
        //     }else {

        //  }
    }

    public static void showCustomToastForFragments(String msg, Context context) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.inflate_custom_toast, null);
        toast.setView(view);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(msg);

        toast.show();

    }


}
