package io.shubh.e_comm_ver1.MyOrders.View;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.shubh.e_comm_ver1.MyOrders.Interactor.MyOrdersInteractorImplt;
import io.shubh.e_comm_ver1.MyOrders.Presenter.MyOrdersPresenter;
import io.shubh.e_comm_ver1.MyOrders.Presenter.MyOrdersPresenterImplt;
import io.shubh.e_comm_ver1.Adapters.ReclrAdapterClassForOrderItemsList;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.R;

public class MyOrdersFragment extends Fragment implements MyOrdersView ,InterfaceForSubOrderItemClickcCallback{

    View containerViewGroup;
    LayoutInflater inflater;
    MyOrdersPresenter mPresenter;
    List<Order> orderItemlist;
    RecyclerView recyclerView;
    ReclrAdapterClassForOrderItemsList adapter;
    BottomSheetBehavior behavior;
    View inflatedBttmSheet;
    ShimmerFrameLayout mShimmerViewContainer;
    RelativeLayout rlCpntainerFrEmptyListMsg;
    
    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        containerViewGroup = inflater.inflate(R.layout.fragment_my_orders, container, false);
        this.inflater = inflater;
        
        mPresenter = new MyOrdersPresenterImplt(this, new MyOrdersInteractorImplt() {
        });

        DoUiWork();


        // Inflate the layout for this fragment
        return containerViewGroup;
    }

    private void DoUiWork() {

       // initializations here //todo shimmer below
        mShimmerViewContainer = containerViewGroup.findViewById(R.id.shimmer_view_container);
        rlCpntainerFrEmptyListMsg =(RelativeLayout )containerViewGroup.findViewById(R.id.rlCpntainerFrEmptyListMsg);

        //---setups here

        setUpToolbar();

        doBottomSheetWork();
        //logic work start here
        rlCpntainerFrEmptyListMsg.setVisibility(View.GONE);
        mPresenter.getOrderItemsData();

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
    public void showItemsInRecyclerView(List<Order> orderItemlist) {
        this.orderItemlist = orderItemlist;
        //------------recycler setting up
        recyclerView = (RecyclerView) containerViewGroup.findViewById(R.id.rclrViewFrMyOrdersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ReclrAdapterClassForOrderItemsList(this ,getContext() , getActivity().getApplicationContext(), orderItemlist);
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

    private void doBottomSheetWork() {

        CoordinatorLayout content = (CoordinatorLayout) containerViewGroup.findViewById(R.id.content);

         inflatedBttmSheet = inflater.inflate(R.layout.bottom_sheet_fr_order_timeline,content, false);
        content.addView(inflatedBttmSheet);


        behavior = BottomSheetBehavior.from(inflatedBttmSheet);

       /* DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int heightInPixels = displayMetrics.heightPixels;
       *//* Log.i("***", "layout hieght =" + heightInPixels);
        int topMarginForBottomSheetInPixFromDp = (int) (285 * this.getResources().getDisplayMetrics().density + 0.5f);
        Log.i("***", "bs hieght =" + (heightInPixels - topMarginForBottomSheetInPixFromDp));
*//*
        //this below line is for giving the top margin for toolbar to show when the whole btmsheet is expanded to top
        bottomSheet.getLayoutParams().height = heightInPixels - (int) (65 * this.getResources().getDisplayMetrics().density + 0.5f);
*/
        //     behavior.setPeekHeight(heightInPixels - topMarginForBottomSheetInPixFromDp);


        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN) {

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        ImageButton btCloseBttmSheet =(ImageButton)inflatedBttmSheet.findViewById(R.id.btCloseBttmSheet);
        btCloseBttmSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    @Override
    public void onSubOrderItemClick(Order order, int finalI) {
        displayTimelineAndActionsInBottomSheet(order ,finalI);

    }

    private void displayTimelineAndActionsInBottomSheet( Order order, int finalI) {

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //recycyling the layout and code for bottomsheet at seller dashboard

        TextView tvHeader = (TextView) inflatedBttmSheet.findViewById(R.id.tvHeader);
        tvHeader.setText("(Qt." + order.getSubOrderItems().get(finalI).getItemAmount().toString() + ")," + order.getSubOrderItems().get(finalI).getItemName());

        RelativeLayout rl1 = (RelativeLayout) inflatedBttmSheet.findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) inflatedBttmSheet.findViewById(R.id.rl2);
        rl2.setVisibility(View.GONE);
        RelativeLayout rl3 = (RelativeLayout) inflatedBttmSheet.findViewById(R.id.rl3);
        rl3.setVisibility(View.GONE);
        RelativeLayout rl4 = (RelativeLayout) inflatedBttmSheet.findViewById(R.id.rl4);
        rl4.setVisibility(View.GONE);
        RelativeLayout rl5 = (RelativeLayout) inflatedBttmSheet.findViewById(R.id.rl5);
        rl5.setVisibility(View.GONE);

           TextView tvDescp1 = (TextView) inflatedBttmSheet.findViewById(R.id.tvDescp1);
        tvDescp1.setVisibility(View.GONE);
           TextView tvDescp2 = (TextView) inflatedBttmSheet.findViewById(R.id.tvDescp2);
        tvDescp2.setVisibility(View.GONE);
           TextView tvDescp3 = (TextView) inflatedBttmSheet.findViewById(R.id.tvDescp3);
        tvDescp3.setVisibility(View.GONE);


        CheckBox cb1 = (CheckBox) inflatedBttmSheet.findViewById(R.id.cb1);
                cb1.setVisibility(View.GONE);
        CheckBox cb2  =inflatedBttmSheet.findViewById(R.id.cb2);
        cb2.setVisibility(View.GONE);
        CheckBox cb3 = inflatedBttmSheet.findViewById(R.id.cb3);
        cb3.setVisibility(View.GONE);


        TextView tvTimeOfCreation1 = (TextView) inflatedBttmSheet.findViewById(R.id.tvTimeOfCreation1);
        TextView tvTimeOfCreation2 = (TextView) inflatedBttmSheet.findViewById(R.id.tvTimeOfCreation2);
        TextView tvTimeOfCreation3 = (TextView) inflatedBttmSheet.findViewById(R.id.tvTimeOfCreation3);



        if (order.getSubOrderItems().get(finalI).getStatusOfOrder() == 2) {
            tvTimeOfCreation1.setText(getDateFromUnix(order.getTimeOfCreationOfOrder()));
            tvTimeOfCreation1.setVisibility(View.VISIBLE);

        } else if (order.getSubOrderItems().get(finalI).getStatusOfOrder() == 3) {
            tvTimeOfCreation1.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfCreationOfOrder()));
            tvTimeOfCreation1.setVisibility(View.VISIBLE);
            rl2.setVisibility(View.VISIBLE);
            tvTimeOfCreation2.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfPackagedOfItem()));
            tvTimeOfCreation2.setVisibility(View.VISIBLE);

        } else if (order.getSubOrderItems().get(finalI).getStatusOfOrder() == 4) {
            tvTimeOfCreation1.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfCreationOfOrder()));
            tvTimeOfCreation1.setVisibility(View.VISIBLE);

            rl2.setVisibility(View.VISIBLE);
            tvTimeOfCreation2.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfPackagedOfItem()));
            tvTimeOfCreation2.setVisibility(View.VISIBLE);

            rl3.setVisibility(View.VISIBLE);
            tvTimeOfCreation3.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfShippedOfItem()));
            tvTimeOfCreation3.setVisibility(View.VISIBLE);

        }else if (order.getSubOrderItems().get(finalI).getStatusOfOrder() == 5) {
            tvTimeOfCreation1.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfCreationOfOrder()));
            tvTimeOfCreation1.setVisibility(View.VISIBLE);

            rl2.setVisibility(View.VISIBLE);
            tvTimeOfCreation2.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfPackagedOfItem()));
            tvTimeOfCreation2.setVisibility(View.VISIBLE);

            rl3.setVisibility(View.VISIBLE);
            tvTimeOfCreation3.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfShippedOfItem()));
            tvTimeOfCreation3.setVisibility(View.VISIBLE);

            rl4.setVisibility(View.VISIBLE);
            tvTimeOfCreation3.setText(getDateFromUnix(order.getSubOrderItems().get(finalI).getTimeOfDeliveryOfItem()));
            tvTimeOfCreation3.setVisibility(View.VISIBLE);
        }

//delivered to
        LinearLayout llAddressContainer =(LinearLayout)inflatedBttmSheet.findViewById(R.id.llAddressContainer);
        llAddressContainer.setVisibility(View.VISIBLE);


        TextView tvRecieverName = (TextView) inflatedBttmSheet.findViewById(R.id.tvRecieverName);
        TextView tvAreaStateCity = (TextView) inflatedBttmSheet.findViewById(R.id.tvAreaStateCity);
        TextView tvHouseNo = (TextView) inflatedBttmSheet.findViewById(R.id.tvHouseNo);

        tvRecieverName.setText("Delivery to "+ order.getAdressItem().getRecieverName());
        tvHouseNo.setText("House No: " + order.getAdressItem().getHouseNo());
        tvAreaStateCity.setText(order.getAdressItem().getArea() + ", " + order.getAdressItem().getCity() + ", " + order.getAdressItem().getState());


        LinearLayout llChoices =(LinearLayout)inflatedBttmSheet.findViewById(R.id.llChoices);
        llChoices.setVisibility(View.VISIBLE);

    }


    public String getDateFromUnix(Long unix) {
        long unixSeconds = unix;
// convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds * 1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }



    public void closeFragment() {

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).remove(MyOrdersFragment.this).commit();


    }

    @Override
    public void showProgressBar(boolean b) {
        if (b == true) {
            //  recyclerView.setVisibility(View.GONE);
            mShimmerViewContainer.startShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
        } else {

            //     recyclerView.setVisibility(View.VISIBLE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
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



    }



    @Override
    public void showEmptyListMessage() {
        rlCpntainerFrEmptyListMsg.setVisibility(View.VISIBLE);
    }


}

interface InterfaceForSubOrderItemClickcCallback {
    void onSubOrderItemClick(Order order, int finalI);

}
