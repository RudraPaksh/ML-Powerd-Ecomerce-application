package io.shubh.e_comm_ver1.Utils;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.recyclerview.widget.RecyclerView;

import io.shubh.e_comm_ver1.R;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private int mToolbarOffset = 0;
    private int mToolbarHeight;

    public HidingScrollListener(Context context ) {

        //below code is for calculating toolbar hieght
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        mToolbarHeight =  toolbarHeight;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        clipToolbarOffset();
        onMoved(mToolbarOffset);

        if((mToolbarOffset <mToolbarHeight && dy>0) || (mToolbarOffset >0 && dy<0)) {
            mToolbarOffset += dy;
        }
    }

    private void clipToolbarOffset() {
        if(mToolbarOffset > mToolbarHeight) {
            mToolbarOffset = mToolbarHeight;
        } else if(mToolbarOffset < 0) {
            mToolbarOffset = 0;
        }
    }

    public abstract void onMoved(int distance);

}
