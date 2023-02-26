package io.shubh.e_comm_ver1.Splash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.shubh.e_comm_ver1.Adapters.OnboardingAdapter;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.Utils.OnboardingPageTransformer;

public class OnboadingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private OnboardingAdapter onboardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboading);
        makeStatusbarTransparent();

        viewPager = findViewById(R.id.onboarding_view_pager);

        onboardingAdapter = new OnboardingAdapter(this);
        viewPager.setAdapter(onboardingAdapter);
        viewPager.setPageTransformer(false, new OnboardingPageTransformer());


    }


    // Listener for next button press
    public void nextPage(View view) {
        if (view.getId() == R.id.button2) {
            if (viewPager.getCurrentItem() < onboardingAdapter.getCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        }
    }

  //  @RequiresApi(api = Build.VERSION_CODES.P)
    private void makeStatusbarTransparent() {

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);


           window.setNavigationBarColor(Color.parseColor("#26228d"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.setNavigationBarDividerColor(Color.parseColor("#26228d"));
            }
        }
    }


    @Override
    public void onBackPressed() {

                finishAffinity();
                System.exit(0);

    }

}