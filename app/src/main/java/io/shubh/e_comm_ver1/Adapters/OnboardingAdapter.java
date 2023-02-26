package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import io.shubh.e_comm_ver1.Welcome.View.WelcomeActivity;
import io.shubh.e_comm_ver1.R;

public class  OnboardingAdapter extends PagerAdapter {

    private Context context;
    private int[] layouts = {
            R.layout.onboarding_1,
            R.layout.onboarding_2,
            R.layout.onboarding_3,
            R.layout.onboarding_4
    };

    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return false;
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layouts[position], container, false);
        if(position==3) {
            Button btNextFin = (Button)view.findViewById(R.id.button2);
            btNextFin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //marking as onboarding has been seen ..so do not show next time
                    final String PREFS_NAME = "MyPrefsFile";
                    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
                    settings.edit().putBoolean("my_first_time", false).commit();

                    //marking the theme as light by default

                    settings.edit().putInt("theme", 1).commit();


                    Intent in = new Intent(context, WelcomeActivity.class);
                    context.startActivity(in);

                }
            });
          //  iv.setImageDrawable(context.getDrawable(R.drawable.object_recognition));
        }

        view.setTag(position);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((ConstraintLayout) object);
    }
}