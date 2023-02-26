package io.shubh.e_comm_ver1.PaymentFragments.View;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.PaymentFragments.Interactor.PaymentInteractorImplt;
import io.shubh.e_comm_ver1.PaymentFragments.Presenter.PaymentPresenter;
import io.shubh.e_comm_ver1.PaymentFragments.Presenter.PaymentPresenterImplt;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.Utils.Utils;


public class PaymentFragment extends Fragment implements PaymentView {

    View containerViewGroup;
    LayoutInflater inflater;
    PaymentPresenter mPresenter;

    boolean isThisFragCalledFromAddressFrag;
    Order order;
    int totalPaymentAmount;
    Button btStartPayment;

    boolean isPaymnetProcessing = false;
    String TAG = "####";


    public void setLocalVariables(boolean isThisFragCalledFromAddressFrag, Order order) {
        this.isThisFragCalledFromAddressFrag = isThisFragCalledFromAddressFrag;
        //if above value is false then a null bag item onject is given to the below one
        this.order = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerViewGroup = inflater.inflate(R.layout.fragment_payment, container, false);
        this.inflater = inflater;

        //always do presenter related work at last in Oncreate
        mPresenter = new PaymentPresenterImplt(this, new PaymentInteractorImplt() {
        },getContext());

        doUIWork();

        return containerViewGroup;
    }


    private void doUIWork() {
        setUpTextViews();
        setUpToolbar();

        //Todo-disable all back clicks in this fragment
        // attachOnBackBtPressedlistener();
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

    private void setUpTextViews() {

        int totalAmount = 0;
        int shippingCharge = 100;
        int total = 0;

        for (int i = 0; i < order.getBagItems().size(); i++) {
            totalAmount = totalAmount + (Integer.valueOf(order.getBagItems().get(i).getItemObject().getItem_price())
                    * (Integer.valueOf(order.getBagItems().get(i).getItemAmount())));

        }

        total = totalAmount + shippingCharge;
        totalPaymentAmount = total;

        TextView tvSubtotal = (TextView) containerViewGroup.findViewById(R.id.tvSubtotal);
        tvSubtotal.setText("₹" + String.valueOf(totalAmount));
        TextView tvDiscount = (TextView) containerViewGroup.findViewById(R.id.tvDiscount);
        tvDiscount.setText(String.valueOf(0) + "%");
        TextView tvShipping = (TextView) containerViewGroup.findViewById(R.id.tvShipping);
        tvShipping.setText("₹" + String.valueOf(shippingCharge));
        TextView tvTotal = (TextView) containerViewGroup.findViewById(R.id.tvTotal);
        tvTotal.setText("₹" + String.valueOf(total));


        btStartPayment = (Button) containerViewGroup.findViewById(R.id.btCheckout);
        btStartPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setTimeOfCreationOfOrder(System.currentTimeMillis() / 1000L);
                mPresenter.startPayment(totalPaymentAmount, getContext());


            }
        });
    }

    //The success and failiure listeners for the payment are in the main activity ...
    //so main activty will call below functions on the event

    public void onPaymentSuccessCallbackFromMainActivty(String s) {
        Log.i(TAG, s);

        mPresenter.onSucessOfPayment(order, s);


    }

    public void onPaymentFailiureCallbackFromMainActivty(String s) {
        Log.i(TAG, s);

        mPresenter.onFailiureOfPayment(order, s);
    }


    @Override
    public void showSuccessScreen(boolean isPaymentSuccessful) {

        btStartPayment.setVisibility(View.GONE);

        if (isPaymentSuccessful == true) {
            RelativeLayout rlPaymentStatusOverlay = (RelativeLayout) containerViewGroup.findViewById(R.id.rlPaymentStatusOverlay);
            rlPaymentStatusOverlay.setVisibility(View.VISIBLE);

            //doing animation on image view
            ImageView ivThumbs = (ImageView) containerViewGroup.findViewById(R.id.ivThumbs);
            ;
            ivThumbs.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumbs_up_svg));
            ObjectAnimator rotate = ObjectAnimator.ofFloat(ivThumbs, "rotation", 0f, 20f, 0f, -20f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
// animateView (View object)
            rotate.setRepeatCount(13); // repeat the loop 20 times
            rotate.setDuration(400); // animation play time 100 ms
            rotate.start();

            //setting tint to imageview
            ivThumbs.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorGreen), android.graphics.PorterDuff.Mode.SRC_IN);

            Button btOfMessageScreen = (Button) containerViewGroup.findViewById(R.id.btOfMessageScreen);
            btOfMessageScreen.setText("Back To Home");
            btOfMessageScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //clears the whole fragment stack
                    getActivity().getSupportFragmentManager()
                            .popBackStack(null, getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                }
            });

            TextView tvStatus = (TextView) containerViewGroup.findViewById(R.id.tvStatus);
            tvStatus.setText("You have successfully \ncompleted your payment procedure");
            tvStatus.setTextColor(getResources().getColor(R.color.colorGreen));
        } else {

            RelativeLayout rlPaymentStatusOverlay = (RelativeLayout) containerViewGroup.findViewById(R.id.rlPaymentStatusOverlay);
            rlPaymentStatusOverlay.setVisibility(View.VISIBLE);

            //doing animation on image view
            ImageView ivThumbs = (ImageView) containerViewGroup.findViewById(R.id.ivThumbs);
            ;
            ivThumbs.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumbs_down_svg));
            ObjectAnimator rotate = ObjectAnimator.ofFloat(ivThumbs, "rotation", 0f, 20f, 0f, -20f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
// animateView (View object)
            rotate.setRepeatCount(13); // repeat the loop 20 times
            rotate.setDuration(400); // animation play time 100 ms
            rotate.start();

            //setting tint to imageview
            ivThumbs.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorRed), android.graphics.PorterDuff.Mode.SRC_IN);


            Button btOfMessageScreen = (Button) containerViewGroup.findViewById(R.id.btOfMessageScreen);
            btOfMessageScreen.setText("Retry");

            btOfMessageScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlPaymentStatusOverlay.setVisibility(View.GONE);
                    btStartPayment.setVisibility(View.VISIBLE);
                }
            });

            TextView tvStatus = (TextView) containerViewGroup.findViewById(R.id.tvStatus);
            tvStatus.setText("Payment procedure failed");
            tvStatus.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }


    @Override
    public void updateProgressTv(int i) {
        TextView tvProgressPercent = (TextView) containerViewGroup.findViewById(R.id.tvProgressPercent);
        tvProgressPercent.setText(String.valueOf(i) + "%");
    }


    public void closeFragment() {

        if (isPaymnetProcessing != true) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null).remove(PaymentFragment.this).commit();
        }

    }

    @Override
    public void showProgressBar(boolean b) {

        if (b == true) {
            isPaymnetProcessing = true;

            btStartPayment.setVisibility(View.GONE);
            RelativeLayout rlDimBg = (RelativeLayout) containerViewGroup.findViewById(R.id.rlDimBg);
            rlDimBg.setVisibility(View.VISIBLE);
            rlDimBg.setClickable(true);

            ImageView iv_loading_gif = (ImageView) containerViewGroup.findViewById(R.id.iv_loading_gif);
            CardView container_iv_loading_gif = (CardView) containerViewGroup.findViewById(R.id.cv_container_loaading_iv);
            container_iv_loading_gif.setVisibility(View.VISIBLE);
            Glide.with(getActivity().getApplicationContext()).load(R.drawable.shopping_loader).into(iv_loading_gif);

        } else {
            btStartPayment.setVisibility(View.VISIBLE);
            RelativeLayout rlDimBg = (RelativeLayout) containerViewGroup.findViewById(R.id.rlDimBg);
            rlDimBg.setVisibility(View.GONE);
            CardView container_iv_loading_gif = (CardView) containerViewGroup.findViewById(R.id.cv_container_loaading_iv);
            container_iv_loading_gif.setVisibility(View.GONE);

        }
    }

    @Override
    public void switchActivity(int i) {

        //todo --give code nbelow    if i == 1 switch to main activity
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
        Utils.showCustomToastForFragments(msg, getContext());
    }
}








