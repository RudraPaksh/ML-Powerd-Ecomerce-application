package io.shubh.e_comm_ver1.OrderListFrSellerFragment.View;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.OrderListFrSellerFragment.Interactor.NewOrderListFrSellerInteractorImplt;
import io.shubh.e_comm_ver1.OrderListFrSellerFragment.Presenter.NewOrderListFrSellerPresenter;
import io.shubh.e_comm_ver1.OrderListFrSellerFragment.Presenter.NewOrderListFrSellerPresenterImplt;
import io.shubh.e_comm_ver1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderListFrSellerFragment extends Fragment implements NewOrderListFrSellerView {

    View containerViewGroup;
    LayoutInflater inflater;
    NewOrderListFrSellerPresenter mPresenter;
    ArrayList<Order.SubOrderItem> subOrderItems;
    int index ; //if index=1 then display new orders else display processed oreders
    ShimmerFrameLayout mShimmerViewContainer;
    BottomSheetBehavior behavior;

    public NewOrderListFrSellerFragment() {
        // Required empty public constructor

    }

    public void setLocalvariables(ArrayList<Order.SubOrderItem> subOrderItems ,   int index ) {
        // Required empty public constructor
        this.subOrderItems = subOrderItems;
        this.index =index; //if index=1 then display new orders else display processed oreders
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerViewGroup = inflater.inflate(R.layout.fragment_new_order_list_fr_seller, container, false);
        this.inflater = inflater;

        mPresenter = new NewOrderListFrSellerPresenterImplt(this, new NewOrderListFrSellerInteractorImplt() {
        },getContext());

        DoUiWork();
        // Inflate the layout for this fragment
        return containerViewGroup;
    }

    private void DoUiWork() {

        setUpToolbar();

        // not using a recl view here for now...Todo- maybe do that later when used for real purpose
        showItemsInNonReclrViewList();

        doBottomSheetWork();

        //if index=1 then display new orders else display processed oreders
        if(index==1 ){
          TextView  id_fr_tv_header =(TextView)containerViewGroup.findViewById(R.id.id_fr_tv_header);
          id_fr_tv_header.setText("New Orders");
        }else {
            TextView  id_fr_tv_header =(TextView)containerViewGroup.findViewById(R.id.id_fr_tv_header);
            id_fr_tv_header.setText("Processed Orders");
        }

    }


    private void showItemsInNonReclrViewList() {

        LinearLayout llContainerFrItems = (LinearLayout) containerViewGroup.findViewById(R.id.llContainerFrItems);
        //llContainerFrAddressItems.removeAllViews();

        for (int i = 0; i < subOrderItems.size(); i++) {

            View inflatedVarietyBox = inflater.inflate(R.layout.inflate_sub_order_item, llContainerFrItems, false);
            llContainerFrItems.addView(inflatedVarietyBox);

            TextView tvOrdreNo = (TextView) inflatedVarietyBox.findViewById(R.id.tvOrdreNo);
            TextView tvTimeOfCreation = (TextView) inflatedVarietyBox.findViewById(R.id.tvTimeOfCreation);
            TextView tvOrdreAmount = (TextView) inflatedVarietyBox.findViewById(R.id.tvOrdreAmount);
            TextView tvItemName = (TextView) inflatedVarietyBox.findViewById(R.id.tvItemName);
            TextView tvAddress = (TextView) inflatedVarietyBox.findViewById(R.id.tvAddress);
            TextView tvVarietyCtgrName = (TextView) inflatedVarietyBox.findViewById(R.id.tvVarietyCtgrName);
            TextView tvVarietyName = (TextView) inflatedVarietyBox.findViewById(R.id.tvVarietyName);
            TextView tvStatus = (TextView) inflatedVarietyBox.findViewById(R.id.tvStatus);

            ImageView ivItemImage = (ImageView) inflatedVarietyBox.findViewById(R.id.ivItemImage);
            // iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_svg));

            Glide.with(getActivity().getApplicationContext()).load(subOrderItems.get(i).getImageUrl()).centerCrop().into(ivItemImage);

            tvOrdreNo.setText("Item Id " + subOrderItems.get(i).getItemId() +
                    " from order no " + subOrderItems.get(i).getParentOrderId());
            tvTimeOfCreation.setText(getDateFromUnix(subOrderItems.get(i).getTimeOfCreationOfOrder()));
            tvOrdreAmount.setText("Qt." + subOrderItems.get(i).getItemAmount());
            tvItemName.setText(subOrderItems.get(i).getItemName());
            String sourceString = "Ordered for " + "<b>" + subOrderItems.get(i).getAdressItem().getRecieverName() + "</b> "
                    + " for to be delivered at " + "<b>" + subOrderItems.get(i).getAdressItem().getArea() + ", " +
                    subOrderItems.get(i).getAdressItem().getCity() + ", " + subOrderItems.get(i).getAdressItem().getState() + "</b>" +
                    ", House no : " + "<b>" + subOrderItems.get(i).getAdressItem().getHouseNo() + "</b>";
            tvAddress.setText(Html.fromHtml(sourceString));
            if (subOrderItems.get(i).getVarietyName() != null) {
                tvVarietyCtgrName.setVisibility(View.VISIBLE);
                tvVarietyName.setVisibility(View.VISIBLE);

                tvVarietyCtgrName.setText(subOrderItems.get(i).getVarietyName() + " : ");
                tvVarietyName.setText(subOrderItems.get(i).getSelectedVarietyName());
            }

            if (subOrderItems.get(i).getStatusOfOrder() == 2) {
                tvStatus.setText("Packaging Pending");
            } else if (subOrderItems.get(i).getStatusOfOrder() == 3) {
                tvStatus.setText("Shipping Pending");
            } else if (subOrderItems.get(i).getStatusOfOrder() == 4) {
                tvStatus.setText("Delivery Pending");
            }else if (subOrderItems.get(i).getStatusOfOrder() == 5) {
                tvStatus.setText("Delivered");
                tvStatus.setTextColor(getResources().getColor(R.color.colorGreen));
            }


            int finalI = i;
            inflatedVarietyBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    displayTimelineAndActionsInBottomSheet(finalI,subOrderItems.get(finalI));
                }
            });
        }
    }

    private void displayTimelineAndActionsInBottomSheet(int finalI, Order.SubOrderItem subOrderItem) {

        TextView tvHeader = (TextView) containerViewGroup.findViewById(R.id.tvHeader);
        tvHeader.setText("(Qt." + subOrderItem.getItemAmount().toString() + ")," + subOrderItem.getItemName());

        RelativeLayout rl1 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl2);
        rl2.setVisibility(View.GONE);
        RelativeLayout rl3 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl3);
        rl3.setVisibility(View.GONE);
        RelativeLayout rl4 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl4);
        rl4.setVisibility(View.GONE);
        RelativeLayout rl5 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl5);
        rl5.setVisibility(View.GONE);

        CheckBox cb1 = (CheckBox) containerViewGroup.findViewById(R.id.cb1);
        cb1.setChecked(false);
        cb1.setEnabled(true);
        CheckBox cb2 = (CheckBox) containerViewGroup.findViewById(R.id.cb2);
        cb2.setChecked(false);
        cb2.setEnabled(true);
        CheckBox cb3 = (CheckBox) containerViewGroup.findViewById(R.id.cb3);
        cb3.setChecked(false);
        cb3.setEnabled(true);

        TextView tvTimeOfCreation1 = (TextView) containerViewGroup.findViewById(R.id.tvTimeOfCreation1);
        TextView tvTimeOfCreation2 = (TextView) containerViewGroup.findViewById(R.id.tvTimeOfCreation2);
        TextView tvTimeOfCreation3 = (TextView) containerViewGroup.findViewById(R.id.tvTimeOfCreation3);
        TextView tvTimeOfCreation4 = (TextView) containerViewGroup.findViewById(R.id.tvTimeOfCreation4);
        TextView tvTimeOfCreation5 = (TextView) containerViewGroup.findViewById(R.id.tvTimeOfCreation5);

        ProgressBar pb1 = (ProgressBar) containerViewGroup.findViewById(R.id.pb1);
        pb1.setVisibility(View.GONE);
        ProgressBar pb2 = (ProgressBar) containerViewGroup.findViewById(R.id.pb2);
        pb2.setVisibility(View.GONE);
        ProgressBar pb3 = (ProgressBar) containerViewGroup.findViewById(R.id.pb3);
        pb3.setVisibility(View.GONE);


        if (subOrderItem.getStatusOfOrder() == 2) {
            tvTimeOfCreation1.setText(getDateFromUnix(subOrderItem.getTimeOfCreationOfOrder()));
            cb1.setEnabled(true);
        } else if (subOrderItem.getStatusOfOrder() == 3) {
            tvTimeOfCreation1.setText(getDateFromUnix(subOrderItem.getTimeOfCreationOfOrder()));
            cb1.setChecked(true);
            cb1.setEnabled(false);
            cb2.setEnabled(true);

            rl2.setVisibility(View.VISIBLE);
            tvTimeOfCreation2.setText(getDateFromUnix(subOrderItem.getTimeOfPackagedOfItem()));

        } else if (subOrderItem.getStatusOfOrder() == 4) {
            tvTimeOfCreation1.setText(getDateFromUnix(subOrderItem.getTimeOfCreationOfOrder()));
            cb1.setChecked(true);
            cb1.setEnabled(false);


            rl2.setVisibility(View.VISIBLE);
            tvTimeOfCreation2.setText(getDateFromUnix(subOrderItem.getTimeOfPackagedOfItem()));
            cb2.setChecked(true);
            cb2.setEnabled(false);

            rl3.setVisibility(View.VISIBLE);
            tvTimeOfCreation3.setText(getDateFromUnix(subOrderItem.getTimeOfShippedOfItem()));
            //cb3.setChecked(true);
            cb3.setEnabled(true);
        }else if (subOrderItem.getStatusOfOrder() == 5) {
            tvTimeOfCreation1.setText(getDateFromUnix(subOrderItem.getTimeOfCreationOfOrder()));
            cb1.setChecked(true);
            cb1.setEnabled(false);


            rl2.setVisibility(View.VISIBLE);
            tvTimeOfCreation2.setText(getDateFromUnix(subOrderItem.getTimeOfPackagedOfItem()));
            cb2.setChecked(true);
            cb2.setEnabled(false);

            rl3.setVisibility(View.VISIBLE);
            tvTimeOfCreation3.setText(getDateFromUnix(subOrderItem.getTimeOfShippedOfItem()));
            cb3.setChecked(true);
            cb3.setEnabled(false);

            rl4.setVisibility(View.VISIBLE);
            tvTimeOfCreation3.setText(getDateFromUnix(subOrderItem.getTimeOfDeliveryOfItem()));
        }

        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.changeStatusOfOrderAndSendNotification(subOrderItem ,System.currentTimeMillis()/1000L);
                subOrderItem.setStatusOfOrder(3);
                subOrderItem.setTimeOfPackagedOfItem(System.currentTimeMillis()/1000L);
                subOrderItems.set(finalI,subOrderItem);
                cb1.setEnabled(false);
pb1.setVisibility(View.VISIBLE);
              //  rl2.setVisibility(View.VISIBLE);
            }
        });

        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.changeStatusOfOrderAndSendNotification(subOrderItem ,System.currentTimeMillis()/1000L);
                cb2.setEnabled(false);
                subOrderItem.setStatusOfOrder(4);
                subOrderItem.setTimeOfPackagedOfItem(System.currentTimeMillis()/1000L);
                subOrderItems.set(finalI,subOrderItem);
                pb2.setVisibility(View.VISIBLE);
            }
        });
        cb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.changeStatusOfOrderAndSendNotification(subOrderItem ,System.currentTimeMillis()/1000L);
                cb3.setEnabled(false);
                subOrderItem.setStatusOfOrder(5);
                subOrderItem.setTimeOfPackagedOfItem(System.currentTimeMillis()/1000L);
                subOrderItems.set(finalI,subOrderItem);
                pb3.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void OnStatusUpdateDOne(int nextStatusNoToUpdate) {
        if (nextStatusNoToUpdate == 3) {
            RelativeLayout rl2 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl2);
            rl2.setVisibility(View.VISIBLE);

            ProgressBar pb1 = (ProgressBar) containerViewGroup.findViewById(R.id.pb1);
            pb1.setVisibility(View.GONE);

        } else if (nextStatusNoToUpdate == 4) {
            RelativeLayout rl3 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl3);
            rl3.setVisibility(View.VISIBLE);

            ProgressBar pb2 = (ProgressBar) containerViewGroup.findViewById(R.id.pb2);
            pb2.setVisibility(View.GONE);

        } else if (nextStatusNoToUpdate == 5) {
            RelativeLayout rl4 = (RelativeLayout) containerViewGroup.findViewById(R.id.rl4);
            rl4.setVisibility(View.VISIBLE);

            ProgressBar pb3 = (ProgressBar) containerViewGroup.findViewById(R.id.pb3);
            pb3.setVisibility(View.GONE);

            //since the order is processed remove it from new oreder list

        }
    }



    private void doBottomSheetWork() {

        RelativeLayout bottomSheet = (RelativeLayout) containerViewGroup.findViewById(R.id.rl_container_of_bt_sheet_fr_order_timeline);
        behavior = BottomSheetBehavior.from(bottomSheet);

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

        ImageButton btCloseBttmSheet =(ImageButton)containerViewGroup.findViewById(R.id.btCloseBttmSheet);
        btCloseBttmSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

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


    private void setUpToolbar() {
        ImageButton btCloseFrag = (ImageButton) containerViewGroup.findViewById(R.id.btCloseFrag);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });


    }



    public void closeFragment() {

        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).remove(NewOrderListFrSellerFragment.this).commit();


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

    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {

    }

    @Override
    public void showToast(String msg) {
        Utils.showCustomToastForFragments(msg,getContext());
    }


}
