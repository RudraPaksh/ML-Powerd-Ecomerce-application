package io.shubh.e_comm_ver1.Notification.View;


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

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.NotifcationObject;
import io.shubh.e_comm_ver1.Utils.InterfaceForClickCallbackFromAnyAdaptr;
import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.Adapters.ReclrAdapterClassForNotificationObjects;
import io.shubh.e_comm_ver1.Notification.Interactor.NotificationInteractorImplt;
import io.shubh.e_comm_ver1.Notification.Presenter.NotificationPresenter;
import io.shubh.e_comm_ver1.Notification.Presenter.NotificationPresenterImplt;
import io.shubh.e_comm_ver1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements NotificationView , InterfaceForClickCallbackFromAnyAdaptr {

    View containerViewGroup;
    LayoutInflater inflater;
    NotificationPresenter mPresenter;

    RecyclerView recyclerView;
    ReclrAdapterClassForNotificationObjects adapter;
    RecyclerView.LayoutManager layoutManager;
    ///   GridLayoutManager gridLayoutManager;
    List<NotifcationObject> notifcationObjects;
    ShimmerFrameLayout mShimmerViewContainer;
    RelativeLayout rlCpntainerFrEmptyListMsg;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        containerViewGroup = inflater.inflate(R.layout.fragment_notification, container, false);
        this.inflater = inflater;

        mPresenter = new NotificationPresenterImplt(this, new NotificationInteractorImplt() {
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

        recyclerView = (RecyclerView) containerViewGroup.findViewById(R.id.id_fr_recycler_view_notif_items_list);
         layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
      //  gridLayoutManager = new GridLayoutManager(getContext(), 2);
     //   gridLayoutManager.setOrientation(RecyclerView.VERTICAL); // set Horizontal Orientation
     //   recyclerView.setLayoutManager(gridLayoutManager);

        notifcationObjects = new ArrayList<>();
        adapter = new ReclrAdapterClassForNotificationObjects((InterfaceForClickCallbackFromAnyAdaptr) this, getContext() ,getActivity().getApplicationContext(), notifcationObjects);
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


        showProgressBar(true);
        rlCpntainerFrEmptyListMsg.setVisibility(View.GONE);
        mPresenter.getNotificationData(false);
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
    public void onGettingCtgrItemsFromPrsntr(ArrayList<NotifcationObject> itemsList, boolean listNotEmpty, boolean isItLoadMoreCall) {

                if (isItLoadMoreCall == false) {
                    emptyTheRecyclerView();
                }

                this.notifcationObjects.addAll(itemsList);
                adapter.notifyDataSetChanged();
                showProgressBar(false);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                           // Log.d("-----","end");

                            if (!recyclerView.canScrollVertically(1)) {
                                callForPresenterToGetCtgrItems(true);

                                showToast("Loading more items");
                            }
                        }
                    }


                });
           // }


    }

    @Override
    public void onClickOnItem(String docId) {

    }



    @Override
    public void onNoItemsFoundResult(boolean isItLoadMoreCall) {
        rlCpntainerFrEmptyListMsg.setVisibility(View.VISIBLE);
        showProgressBar(false);
        notifcationObjects.clear();
        adapter.notifyDataSetChanged();

    }
    private void emptyTheRecyclerView() {

        notifcationObjects.clear();
        adapter.notifyDataSetChanged();
    }

    private void callForPresenterToGetCtgrItems(boolean ifItsALoadMorecall) {
       /* if(ifItsALoadMorecall==true) {
            pageNoForNewDataToFetchOnReclrScrollToBottom++;
        }*/
        if (ifItsALoadMorecall == false) {
            showProgressBar(true);
        } else {
            //ToDO--- add shimmer layout containig two views
        }

        mPresenter.getNotificationData( ifItsALoadMorecall);
    }



    public  void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(NotificationFragment.this)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showToast(String msg) {
        Utils.showCustomToastForFragments(msg,getContext());
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
            //   progressBar.setVisibility(android.view.View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            mShimmerViewContainer.startShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
        } else {
            //  progressBar.setVisibility(android.view.View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);

        }
    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {

    }




    @Override
    public void onClickOnSaveToLikedItemsBt(String docId) {
        //not applicable
    }

    @Override
    public void onClickOnDeleteFromLikedItemsBt(String docId) {
//not applicable
    }


}
