package io.shubh.e_comm_ver1.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.sak.ultilviewlib.adapter.BaseHeaderAdapter;
import com.sak.ultilviewlib.util.MeasureTools;

import io.shubh.e_comm_ver1.R;

public class PullToRefreshHeaderAdapter extends BaseHeaderAdapter  {
    Context context;
    Context applicationContext;
    View mView;
    public PullToRefreshHeaderAdapter(Context context, Context applicationContext) {
        super(context);
        this.context=context;
        this.applicationContext=applicationContext;
    }

    @Override
    public View getHeaderView() {
         mView = mInflater.inflate(R.layout.inflate_custom_loading_gif_for_pull_to_refresh, null, false);
        ImageView iv_loading_gif = (ImageView) mView.findViewById(R.id.iv_loading_gif);
        Glide.with(applicationContext).load(R.drawable.shopping_loader).into(iv_loading_gif);

        mView.setVisibility(View.GONE);
        return mView;
    }

    @Override
    public void pullViewToRefresh(int deltaY) {

        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void releaseViewToRefresh(int deltaY) {

    }

    @Override
    public void headerRefreshing() {

    }

    @Override
    public void headerRefreshComplete() {
        mView.setVisibility(View.GONE);
    }
}
