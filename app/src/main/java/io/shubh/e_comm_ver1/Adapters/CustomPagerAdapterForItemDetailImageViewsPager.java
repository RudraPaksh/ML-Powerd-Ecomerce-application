package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import io.shubh.e_comm_ver1.R;

public class CustomPagerAdapterForItemDetailImageViewsPager extends PagerAdapter {
    private Context context;
    List<String> listOfImageURLs;
    Context applicationContext;

    public CustomPagerAdapterForItemDetailImageViewsPager(Context context,Context applicationContext,List<String> listOfImageURLs) {
        this.context = context;
        this.applicationContext = applicationContext;
        this.listOfImageURLs=listOfImageURLs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflate_imageview_pager_item, null);
        ImageView imageView = view.findViewById(R.id.iv);

        Glide.with(applicationContext).load(listOfImageURLs.get(position)).centerCrop().into(imageView);
        //  imageView.setImageDrawable(context.getResources().getDrawable(getImageAt(position)));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return listOfImageURLs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return object == view;
    }

 /*   private int getImageAt(int position) {
        switch (position) {
            case 0:
                return R.drawable.india_taj_mahal;
            case 1:
                return R.drawable.colosseum;
            case 2:
                return R.drawable.eiffel_tower;
            case 3:
                return R.drawable.statue_of_liberty;
            default:
                return R.drawable.india_taj_mahal;
        }
    }*/
}