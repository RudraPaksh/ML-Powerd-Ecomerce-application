package io.shubh.e_comm_ver1.AddressSelectionPage.View;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import io.shubh.e_comm_ver1.AddressSelectionPage.Interactor.AddressSelectionInteractorImplt;
import io.shubh.e_comm_ver1.AddressSelectionPage.Presenter.AddressSelectionPresenter;
import io.shubh.e_comm_ver1.AddressSelectionPage.Presenter.AddressSelectionPresenterImplt;
import io.shubh.e_comm_ver1.PaymentFragments.View.PaymentFragment;
import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.Models.AdressItem;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.R;


public class AddressSelectionFragment extends Fragment implements AddressSelectionView {

    private boolean isThisFragCalledFromBagItemsFrag = false;
    private Order order;

    View containerViewGroup;
    LayoutInflater inflater;

    AddressSelectionPresenter mPresenter;
    List<AdressItem> bagItemlist;
    int postionFromItemtoDelete;
    ShimmerFrameLayout mShimmerViewContainer;

    BottomSheetBehavior behavior_bttm_sheet_address_taking;

    Button btContinue;
    boolean ifDataIsBeingFetched = true;
    boolean ifAnyItemIsBeingDeleted = false;
    int sizeOfAddresList = 0;

    AdressItem selectedAdressItem ;
    RelativeLayout rlCpntainerFrEmptyListMsg;

    public AddressSelectionFragment() {
        // Required empty public constructor
    }

    //TODO-id the address list is empty then on clicking the continue button ..doesnt show any toast
    //TODO- also add a  toast of

    public void setLocalVariables(boolean isThisFragCalledFromBagItemsFrag, Order order) {
        this.isThisFragCalledFromBagItemsFrag = isThisFragCalledFromBagItemsFrag;
        //if above value is false then a null bag item onject is given to the below one
        this.order = order;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerViewGroup = inflater.inflate(R.layout.fragment_address_selection, container, false);
        this.inflater = inflater;

        mPresenter = new AddressSelectionPresenterImplt(this, new AddressSelectionInteractorImplt() {
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
        setUpAddressTakingBttmSheet();

        setUpBTContinue();

        //logic work start here
        rlCpntainerFrEmptyListMsg.setVisibility(View.GONE);
        mPresenter.getAddressData();

    }

    private void setUpBTContinue() {

        btContinue = (Button) containerViewGroup.findViewById(R.id.btContinue);
        btContinue.setEnabled(false);

        if(isThisFragCalledFromBagItemsFrag==false){

            btContinue.setVisibility(View.GONE);
        }

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                order.setAdressItem(selectedAdressItem);
                PaymentFragment paymentFragment =new PaymentFragment();
                paymentFragment.setLocalVariables(true,order);
                paymentFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                paymentFragment.setExitTransition(new Slide(Gravity.RIGHT));

                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.drawerLayout, paymentFragment ,"PaymentFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

    }


    private void setUpToolbar() {
        ImageButton btCloseFrag = (ImageButton) containerViewGroup.findViewById(R.id.btCloseFrag);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });


        ImageButton btAddAddress = (ImageButton) containerViewGroup.findViewById(R.id.btAddAddress);
        btAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior_bttm_sheet_address_taking.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });
    }

    private void setUpAddressTakingBttmSheet() {

//---------------------------------------Bottom Sheet setup------------------

        CoordinatorLayout rootView = (CoordinatorLayout) containerViewGroup.findViewById(R.id.cl_root);
        View inflatedBottomSheetdialog = inflater.inflate(R.layout.bottom_sheet_fr_address_detail_taking, rootView, false);
        rootView.addView(inflatedBottomSheetdialog);

        behavior_bttm_sheet_address_taking = BottomSheetBehavior.from(inflatedBottomSheetdialog);
        behavior_bttm_sheet_address_taking.setState(BottomSheetBehavior.STATE_COLLAPSED);

        View dim_background_of_bottom_sheet = (View) containerViewGroup.findViewById(R.id.touch_to_dismiss_bottom_sheet_dim_background);
        dim_background_of_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior_bttm_sheet_address_taking.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        behavior_bttm_sheet_address_taking.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dim_background_of_bottom_sheet.setVisibility(View.GONE);

                    //btcontinue overlaps the bttm sheet ..so hiding it for the time
                    Button btContinue = (Button) containerViewGroup.findViewById(R.id.btContinue);
                    btContinue.setVisibility(View.VISIBLE);

                    // is_bottom_sheet_expanded = false;
                } else {
                    dim_background_of_bottom_sheet.setVisibility(View.VISIBLE);

                    Button btContinue = (Button) containerViewGroup.findViewById(R.id.btContinue);
                    btContinue.setVisibility(View.GONE);
                    // is_bottom_sheet_expanded = true;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        LinearLayout llAsBtUseGps = (LinearLayout) inflatedBottomSheetdialog.findViewById(R.id.llAsBtUseGps);
        llAsBtUseGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("API key missing");

            }
        });


        EditText edRecieverName = (EditText) inflatedBottomSheetdialog.findViewById(R.id.edRecieverName);
        EditText edPinCode = (EditText) inflatedBottomSheetdialog.findViewById(R.id.edPinCode);
        EditText edHouseNo = (EditText) inflatedBottomSheetdialog.findViewById(R.id.edHouseNo);
        EditText edArea = (EditText) inflatedBottomSheetdialog.findViewById(R.id.edArea);
        EditText edCity = (EditText) inflatedBottomSheetdialog.findViewById(R.id.edCity);
        EditText edState = (EditText) inflatedBottomSheetdialog.findViewById(R.id.edState);


        Button btCreateAddress = (Button) inflatedBottomSheetdialog.findViewById(R.id.btContinue2);
        btCreateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edRecieverName.getText().toString().length() != 0 &&
                        edPinCode.getText().toString().length() != 0 &&
                        edHouseNo.getText().toString().length() != 0 &&
                        edArea.getText().toString().length() != 0 &&
                        edCity.getText().toString().length() != 0 &&
                        edState.getText().toString().length() != 0
                ) {

                    AdressItem adressItem = new AdressItem();
                    adressItem.setRecieverName(edRecieverName.getText().toString());
                    adressItem.setPinCode(edPinCode.getText().toString());
                    adressItem.setHouseNo(edHouseNo.getText().toString());
                    adressItem.setArea(edArea.getText().toString());
                    adressItem.setCity(edCity.getText().toString());
                    adressItem.setState(edState.getText().toString());
                    adressItem.setTimeOfUpload(System.currentTimeMillis() / 1000L);

                    //below if is for the purpose ,that order no for address item be correct ,by first retriving already address amount
                    if (ifDataIsBeingFetched == false) {
                        Log.i("####", "createbt clicked ");
                        mPresenter.addAdressObject(adressItem);
                    } else {

                    }
                } else {
                    showToast("Any Field Is Empty");
                }
            }
        });

    }

    @Override
    public void showItemsInRecyclerView(List<AdressItem> addressItemlist) {
        rlCpntainerFrEmptyListMsg.setVisibility(View.GONE);

        sizeOfAddresList = addressItemlist.size();
        LinearLayout llContainerFrAddressItems = (LinearLayout) containerViewGroup.findViewById(R.id.llContainerFrAddressItems);
        llContainerFrAddressItems.removeAllViews();
        behavior_bttm_sheet_address_taking.setState(BottomSheetBehavior.STATE_HIDDEN);

        for (int i = 0; i < addressItemlist.size(); i++) {

            View inflatedVarietyBox = inflater.inflate(R.layout.infalte_addresses_items, llContainerFrAddressItems, false);
            llContainerFrAddressItems.addView(inflatedVarietyBox);
            TextView tvRecieverName = (TextView) inflatedVarietyBox.findViewById(R.id.tvRecieverName);
            TextView tvAreaStateCity = (TextView) inflatedVarietyBox.findViewById(R.id.tvAreaStateCity);
            TextView tvHouseNo = (TextView) inflatedVarietyBox.findViewById(R.id.tvHouseNo);
            ImageView iv = (ImageView) inflatedVarietyBox.findViewById(R.id.iv_ctgr_indicator);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_svg));

            tvRecieverName.setText(addressItemlist.get(i).getRecieverName());
            tvAreaStateCity.setText(addressItemlist.get(i).getArea() + ", " + addressItemlist.get(i).getCity() + ", " + addressItemlist.get(i).getState());
            tvHouseNo.setText("House No: " + addressItemlist.get(i).getRecieverName());

            if(isThisFragCalledFromBagItemsFrag==true) {
              //  iv.setImageDrawable(getResources().getDrawable(R.drawable.radio_bt));

                int finalI = i;
                inflatedVarietyBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //removing cj=hecked status from every else address item
                        setRadioBtToAllOtherRowsWhichHadThemOriginally();
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_svg));


                        selectedAdressItem= addressItemlist.get(finalI);
                        btContinue.setEnabled(true);
                       iv.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorSecondary), android.graphics.PorterDuff.Mode.SRC_IN);
                   //     iv.setTag("ic_check_svg");

                    }
                });
            }else{
                btContinue.setVisibility(View.GONE);

                iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_svg));

                int finalI1 = i;
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(ifAnyItemIsBeingDeleted ==false) {
                            mPresenter.deleteBagItem(String.valueOf(addressItemlist.get(finalI1).getTimeOfUpload()));

                            addressItemlist.remove(finalI1);
                            llContainerFrAddressItems.removeViewAt(finalI1);
                            ifAnyItemIsBeingDeleted =true;
                        }
                    }
                });
            }
        /*tvIndivVarietyNmae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
*/
        }

    }

    private void setRadioBtToAllOtherRowsWhichHadThemOriginally() {

        LinearLayout llContainerFrAddressItems = (LinearLayout) containerViewGroup.findViewById(R.id.llContainerFrAddressItems);
        final int childCount = llContainerFrAddressItems.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RelativeLayout rlContainingOtherTv = (RelativeLayout) llContainerFrAddressItems.getChildAt(i);
            ImageView iv = (ImageView) rlContainingOtherTv.findViewById(R.id.iv_ctgr_indicator);
                iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_svg));
            iv.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorLightGrayForSubHeaderTvs), android.graphics.PorterDuff.Mode.SRC_IN);

        }
    }

    @Override
    public void showProgressBar(boolean b) {

        if (b == true) {
            ifDataIsBeingFetched = true;
            //  recyclerView.setVisibility(View.GONE);
            mShimmerViewContainer.startShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
        } else {
            ifDataIsBeingFetched = false;
            //     recyclerView.setVisibility(View.VISIBLE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
    }




    public void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(AddressSelectionFragment.this)
                .addToBackStack(null).commit();
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
    //    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        Utils.showCustomToastForFragments(msg,getContext());
    }


    @Override
    public void updateReclrViewListAfterDeletionOfItem() {

        ifAnyItemIsBeingDeleted =false;
    }

    @Override
    public void showEmptyListMessage() {
        rlCpntainerFrEmptyListMsg.setVisibility(View.VISIBLE);
    }
}
