package io.shubh.e_comm_ver1.BagItems.View;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Adapters.ReclrAdapterClassForBagItemsList;
import io.shubh.e_comm_ver1.AddressSelectionPage.View.AddressSelectionFragment;
import io.shubh.e_comm_ver1.BagItems.Interactor.BagItemsInteractorImplt;
import io.shubh.e_comm_ver1.BagItems.Presenter.BagItemsPresenter;
import io.shubh.e_comm_ver1.BagItems.Presenter.BagItemsPresenterImplt;
import io.shubh.e_comm_ver1.Models.BagItem;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BagItemsFragment extends Fragment implements BagItemsView, InterfaceForClickcCallback {

    View containerViewGroup;
    LayoutInflater inflater;

    ImageView splashimageicon;
    ProgressBar progressBar;
    BagItemsPresenter mPresenter;

    RecyclerView recyclerView;
    ReclrAdapterClassForBagItemsList adapter;
    List<BagItem> bagItemlist;
    int postionFromItemtoDelete;
    ShimmerFrameLayout mShimmerViewContainer;
    RelativeLayout rlCpntainerFrEmptyListMsg;

    public BagItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        containerViewGroup = inflater.inflate(R.layout.fragment_bag_items, container, false);
        this.inflater = inflater;

        mPresenter = new BagItemsPresenterImplt(this, new BagItemsInteractorImplt() {
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
        setUpToolbar();

        //logic work start here
callForPresntrTogetData();

        Button btContinue = (Button) containerViewGroup.findViewById(R.id.btContinue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bagItemlist != null && bagItemlist.size()!=0 ){

                    if(noDeactivatedItemIsInBag()) {

                        Order order =makeOrderObject();
                        AddressSelectionFragment addressSelectionFragment = new AddressSelectionFragment();
                        addressSelectionFragment.setLocalVariables(true, order);
                        addressSelectionFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                        addressSelectionFragment.setExitTransition(new Slide(Gravity.RIGHT));

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.drawerLayout, addressSelectionFragment,"AddressSelectionFragment")
                                .addToBackStack(null)
                                .commit();
                    }else{
                        showToast("Delete deactivated items first");
                    }

                }else{
                    showToast("bag is empty");
                }
            }
        });
    }

    private void callForPresntrTogetData() {
        rlCpntainerFrEmptyListMsg.setVisibility(View.GONE);
        mPresenter.getBagItemsData();
    }

    private Order makeOrderObject() {
        Order order =new Order() ;
        ArrayList<BagItem> bagItems = new ArrayList<>();

        for(int i =0 ;i<bagItemlist.size() ;i++){
        if (bagItemlist.get(i).isTheOriginalItemDeleted() == false && bagItemlist.get(i).getItemObject().isVisibility() == true) {
        bagItems.add(bagItemlist.get(i));
        }
        }
        order.setBagItems(bagItems);
        return order;
    }

    private boolean noDeactivatedItemIsInBag() {
        boolean b = true;
        for(int i =0 ;i<bagItemlist.size() ;i++){
            if(bagItemlist.get(i).isTheOriginalItemDeleted()==true ||bagItemlist.get(i).getItemObject().isVisibility() ==false){
                b= false;
            }
        }
        return b ;
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
    public void showItemsInRecyclerView(List<BagItem> bagItemlist) {
        this.bagItemlist = bagItemlist;
        //------------recycler setting up
        recyclerView = (RecyclerView) containerViewGroup.findViewById(R.id.rclrViewFrBagItemsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ReclrAdapterClassForBagItemsList(getContext(),getActivity().getApplicationContext(), this, bagItemlist);
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
        bagItemlist.remove(postionFromItemtoDelete);

        adapter.notifyItemRemoved(postionFromItemtoDelete);
        adapter.notifyItemRangeChanged(postionFromItemtoDelete, bagItemlist.size());

    }

    @Override
    public void showEmptyListMessage() {

      //  showToast("No Items Found");

        rlCpntainerFrEmptyListMsg.setVisibility(View.VISIBLE);
    }




    public void closeFragment() {

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .remove(BagItemsFragment.this).commit();
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
    public void onrecyclrItemClick(BagItem bagItem) {

    }

    @Override
    public void onDeleteItemClick(String docId, int position) {
        mPresenter.deleteBagItem(docId);
        this.postionFromItemtoDelete = position;
    }
}


interface InterfaceForClickcCallback {
    void onrecyclrItemClick(BagItem bagItem);

    void onDeleteItemClick(String docId, int position);
}
