package io.shubh.e_comm_ver1.LikedItems.View;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import io.shubh.e_comm_ver1.Models.LikedItem;
import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.Adapters.ReclrAdapterClassForLikedItemsList;
import io.shubh.e_comm_ver1.LikedItems.Interactor.LikedItemsInteractorImplt;
import io.shubh.e_comm_ver1.LikedItems.Presenter.LikedItemsPresenter;
import io.shubh.e_comm_ver1.LikedItems.Presenter.LikedItemsPresenterImplt;
import io.shubh.e_comm_ver1.R;

public class LikedItemsFragment extends Fragment implements LikedItemsView ,InterfaceForClickCallbackFromLikedItemsAdapter  {

    View containerViewGroup;
    LayoutInflater inflater;
    LikedItemsPresenter mPresenter;


    RecyclerView recyclerView;
    ReclrAdapterClassForLikedItemsList adapter;
    List<LikedItem> likedItemList;
    int postionFromItemtoDelete;
    ShimmerFrameLayout mShimmerViewContainer;
    RelativeLayout rlCpntainerFrEmptyListMsg;

    public LikedItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerViewGroup = inflater.inflate(R.layout.fragment_liked_items, container, false);
        this.inflater = inflater;

        mPresenter = new LikedItemsPresenterImplt(this, new LikedItemsInteractorImplt() {
        });

        DoUiWork();


        // Inflate the layout for this fragment
        return containerViewGroup;
    }


    private void DoUiWork() {

        //initializations here
        mShimmerViewContainer = containerViewGroup.findViewById(R.id.shimmer_view_container);
        rlCpntainerFrEmptyListMsg =(RelativeLayout )containerViewGroup.findViewById(R.id.rlCpntainerFrEmptyListMsg);

        //---setups here
     //   attachOnBackBtPressedlistener();
        setUpToolbar();

        //logic work start here
      //
        rlCpntainerFrEmptyListMsg.setVisibility(View.GONE);
          mPresenter.getLikedItemsData();
    }


    private void setUpToolbar() {
        ImageButton btCloseFrag = (ImageButton) containerViewGroup.findViewById(R.id.btCloseFrag);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
    }

    @Override
    public void showItemsInRecyclerView(List<LikedItem> likedItemList) {


        this.likedItemList = likedItemList;
        //------------recycler setting up
        recyclerView = (RecyclerView) containerViewGroup.findViewById(R.id.rclrViewFrBagItemsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ReclrAdapterClassForLikedItemsList(getContext(),getActivity().getApplicationContext(),this, likedItemList);
        recyclerView.setAdapter(adapter);

        AppBarLayout appBarLayout = (AppBarLayout) containerViewGroup.findViewById(R.id.appBarLayout);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.canScrollVertically(-1)) {
                    appBarLayout.setElevation(50f);
                } else {
                    appBarLayout.setElevation(0f);
                }
            }
        });

    }

    @Override
    public void updateReclrViewListAfterDeletionOfItem() {

        likedItemList.remove(postionFromItemtoDelete);

        adapter.notifyItemRemoved(postionFromItemtoDelete);
        adapter.notifyItemRangeChanged(postionFromItemtoDelete, likedItemList.size());

    }



    @Override
    public void showEmptyListMessage() {
        rlCpntainerFrEmptyListMsg.setVisibility(View.VISIBLE);
    }




    public void closeFragment() {

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).remove(LikedItemsFragment.this).commit();


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
        if (b == true) {

            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }
            mShimmerViewContainer.startShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
        } else {
            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);

        }
    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {

    }

    @Override
    public void showToast(String msg) {
        Utils.showCustomToastForFragments(msg,getContext());
    }


    @Override
    public void onDeleteItemClick(String docId, int position) {
        mPresenter.deleteLikedItem(docId);
        this.postionFromItemtoDelete = position;
    }
}
interface InterfaceForClickCallbackFromLikedItemsAdapter {
   // void onrecyclrItemClick(BagItem bagItem);

    void onDeleteItemClick(String docId , int position);
}
