package io.shubh.e_comm_ver1.SellerDashboard.View;


import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.View.ItemsDetailsTakingFragment;
import io.shubh.e_comm_ver1.SellerDashboard.Interactor.SellerDashboardInteractorImplt;
import io.shubh.e_comm_ver1.SellerDashboard.Presenter.SellerDashboardPresenterImplt;
import io.shubh.e_comm_ver1.Utils.InterfaceForClickCallbackFromAnyAdaptr;
import io.shubh.e_comm_ver1.Adapters.ReclrAdapterClassForCtgrItems;
import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.OrderListFrSellerFragment.View.NewOrderListFrSellerFragment;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.SellerDashboard.Presenter.SellerDashboardPresenter;
import io.shubh.e_comm_ver1.Utils.Utils;


public class SellerDashboardFragment extends Fragment  implements SellerDashboardView , InterfaceForClickCallbackFromAnyAdaptr {

    View containerViewGroup;
    LayoutInflater inflater;
    SellerDashboardPresenter mPresenter;

    public SellerDashboardFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater=inflater;
         containerViewGroup= inflater.inflate(R.layout.fragment_seller_dashboard, container, false);
        mPresenter = new SellerDashboardPresenterImplt(this, new SellerDashboardInteractorImplt() {
        });
 //------------------------------------
   doUiWork();
mPresenter.getSellerData();

        return containerViewGroup;
    }

    private void doUiWork() {

        Button btAddNewItem = (Button) containerViewGroup.findViewById(R.id.btAddNewItem);
        btAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ItemsDetailsTakingFragment itemsDetailsTakingFragment = new ItemsDetailsTakingFragment();
                itemsDetailsTakingFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                itemsDetailsTakingFragment.setExitTransition(new Slide(Gravity.RIGHT));

                getActivity().getSupportFragmentManager().beginTransaction()
                        //both parameters for instantiating the fragment will be same as at rootl level of ctgr tree ,the name of ctgr and path is same
                        .add(R.id.drawerLayout, itemsDetailsTakingFragment,"ItemsDetailsTakingFragment")
                        .addToBackStack(null)
                        .commit();



            }
        });

        ImageButton btCloseFrag = (ImageButton) containerViewGroup.findViewById(R.id.btCloseFrag);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeFragment();
            }
        });


        mPresenter.getDataForBottomSheet();
        ShimmerFrameLayout mShimmerViewContainer = containerViewGroup.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
    }


    @Override
    public void updateTransactionSummaryTvs(ArrayList<Order.SubOrderItem> subOrderItems, ArrayList<Order.SubOrderItem> newOrdersList, ArrayList<Order.SubOrderItem> processedList, ArrayList<Order.SubOrderItem> returnedOrdersList) {

       TextView newOrderTv =(TextView)containerViewGroup.findViewById(R.id.newOrderTv);
       TextView processedOrederTv =(TextView)containerViewGroup.findViewById(R.id.processedOrederTv);
       TextView returnedOrderTv =(TextView)containerViewGroup.findViewById(R.id.returnedOrderTv);

        newOrderTv.setText(String.valueOf(newOrdersList.size()));
        processedOrederTv.setText(String.valueOf(processedList.size()));
        returnedOrderTv.setText(String.valueOf(returnedOrdersList.size()));


        //since the data has been retrived ..now we can open the new order list
        CardView cvNewOrderBt =(CardView)containerViewGroup.findViewById(R.id.cvNewOrderBt);
        CardView cvProcessedBt =(CardView)containerViewGroup.findViewById(R.id.cvProcessedBt);
        CardView cvReturnedBt =(CardView)containerViewGroup.findViewById(R.id.cvReturnedBt);

        cvNewOrderBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewOrderListFrSellerFragment newOrderListFrSellerFragment = new NewOrderListFrSellerFragment();
                newOrderListFrSellerFragment.setLocalvariables(newOrdersList ,1);
                newOrderListFrSellerFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                newOrderListFrSellerFragment.setExitTransition(new Slide(Gravity.RIGHT));

                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.drawerLayout, newOrderListFrSellerFragment,"NewOrderListFrSellerFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

        cvProcessedBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewOrderListFrSellerFragment newOrderListFrSellerFragment = new NewOrderListFrSellerFragment();
                newOrderListFrSellerFragment.setLocalvariables(processedList ,2);
                newOrderListFrSellerFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                newOrderListFrSellerFragment.setExitTransition(new Slide(Gravity.RIGHT));

                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.drawerLayout, newOrderListFrSellerFragment,"NewOrderListFrSellerFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        cvReturnedBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        showProgressBar(false);
    }

    @Override
    public void showEmptyListMessage() {

    }

    @Override
    public void showItemsInBottomSheet(ArrayList<ItemsForSale> list) {
        ProgressBar progressBarMyOrder = (ProgressBar)containerViewGroup.findViewById(R.id.progressBarMyOrder);
        TextView tvMyItemsAmount = (TextView)containerViewGroup.findViewById(R.id.tvMyItemsAmount);

        ShimmerFrameLayout mShimmerViewContainer = containerViewGroup.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);

        progressBarMyOrder.setVisibility(View.GONE);
        tvMyItemsAmount.setVisibility(View.VISIBLE);
        tvMyItemsAmount.setText(String.valueOf(list.size()));


        RecyclerView recyclerView = (RecyclerView) containerViewGroup.findViewById(R.id.id_fr_recycler_view_ctgr_items_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager);

        ReclrAdapterClassForCtgrItems adapter = new ReclrAdapterClassForCtgrItems(this,getContext(), getActivity().getApplicationContext(), list ,true,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }


    public  void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(SellerDashboardFragment.this)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showProgressBar(boolean b) {

        ProgressBar progressBarFrTrnsctSummryTvs =(ProgressBar)containerViewGroup.findViewById(R.id.progressBarFrTrnsctSummryTvs);
        if(b==true) {
            progressBarFrTrnsctSummryTvs.setVisibility(View.VISIBLE);
        }else {
            progressBarFrTrnsctSummryTvs.setVisibility(View.GONE);
        }
    }



    @Override
    public void switchActivity(int i) {

    }

    @Override
    public Context getContext(boolean getActvityContext) {
        return null;
    }



    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {

    }

    @Override
    public void showToast(String msg) {
        Utils.showCustomToastForFragments(msg,getContext());
    }


    @Override
    public void onClickOnSaveToLikedItemsBt(String docId) {

    }

    @Override
    public void onClickOnDeleteFromLikedItemsBt(String docId) {

    }

    @Override
    public void onClickOnItem(String docId) {

    }
}
