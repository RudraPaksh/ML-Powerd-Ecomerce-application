package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sak.ultilviewlib.adapter.BaseFooterAdapter;

import io.shubh.e_comm_ver1.R;

public class BottomListProgressBarAdapter extends BaseFooterAdapter {

    //this base footer class adapter is not working

    Context context;
    Context applicationContext;
    View mView;
    public BottomListProgressBarAdapter(Context context, Context applicationContext) {
        super(context);
        this.context=context;
        this.applicationContext=applicationContext;
    }

    @Override
    public View getFooterView() {
        mView = mInflater.inflate(R.layout.inflate_custom_toast, null, false);
      //  ImageView iv_loading_gif = (ImageView) mView.findViewById(R.id.iv_loading_gif);
       // Glide.with(applicationContext).load(R.drawable.shopping_loader).into(iv_loading_gif);

        mView.setVisibility(View.VISIBLE);
        return mView;
    }

    @Override
    public void pullViewToRefresh(int deltaY) {
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void releaseViewToRefresh(int deltaY) {
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void footerRefreshing() {
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void footerRefreshComplete() {
        mView.setVisibility(View.GONE);
    }
}
