package io.shubh.e_comm_ver1.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import io.shubh.e_comm_ver1.R;

public class CustomPagerAdapterForSearchFragment extends PagerAdapter {

    public Object instantiateItem(ViewGroup collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.page_one;
                break;
            case 1:
                resId = R.id.page_two;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        // No super
    }
}