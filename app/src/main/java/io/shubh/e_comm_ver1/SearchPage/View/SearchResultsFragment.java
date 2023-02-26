package io.shubh.e_comm_ver1.SearchPage.View;


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
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Adapters.ReclrAdapterClassForCtgrItems;
import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.SearchPage.Interactor.SearchResultsInteractorImplt;
import io.shubh.e_comm_ver1.SearchPage.Presenter.SearchResultsPresenter;
import io.shubh.e_comm_ver1.SearchPage.Presenter.SearchResultsPresenterImplt;
import io.shubh.e_comm_ver1.SearchPage.SearchFragment;
import io.shubh.e_comm_ver1.Utils.InterfaceForClickCallbackFromAnyAdaptr;
import io.shubh.e_comm_ver1.Utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment implements SearchResultsView, InterfaceForClickCallbackFromAnyAdaptr {

    View containerViewGroup;
    LayoutInflater inflater;
    SearchResultsPresenter mPresenter;
    ShimmerFrameLayout mShimmerViewContainer;

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    ReclrAdapterClassForCtgrItems adapter;

    List<String> listOfKeywords;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    public void setLocalVariables(List<String> listOfKeywords) {
        this.listOfKeywords = listOfKeywords;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerViewGroup = inflater.inflate(R.layout.fragment_search_results, container, false);
        this.inflater = inflater;


        mPresenter = new SearchResultsPresenterImplt(this, new SearchResultsInteractorImplt());

        DoUiWork();
        mPresenter.getSearchQuery(listOfKeywords);


        // Inflate the layout for this fragment
        return containerViewGroup;
    }

    private void DoUiWork() {
        SearchViewInit();

        mShimmerViewContainer = containerViewGroup.findViewById(R.id.shimmer_view_container);
        recyclerView = (RecyclerView) containerViewGroup.findViewById(R.id.id_fr_recycler_view_ctgr_items_list);

        TextView tvDescp = (TextView) containerViewGroup.findViewById(R.id.tvDescp);
        for (int i = 0; i < listOfKeywords.size(); i++) {
            if (listOfKeywords.get(i) != null) {
                tvDescp.append(" " +listOfKeywords.get(i));
            }
        }

    }


    private void SearchViewInit() {
        CardView cv_search = (CardView) containerViewGroup.findViewById(R.id.cv_search);
        cv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(MainActivity.this, SearchActivity.class));
                finish();*/
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                searchFragment.setExitTransition(new Slide(Gravity.RIGHT));

                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.drawerLayout,searchFragment ,"SearchFragment")
                        .commit();

            }
        });

    }

    @Override
    public void onGettingCtgrItemsFromPrsntr(List<ItemsForSale> itemList) {

        //------------recycler setting up

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ReclrAdapterClassForCtgrItems((InterfaceForClickCallbackFromAnyAdaptr) this, getContext(), getActivity().getApplicationContext(), itemList, false,getActivity());
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

    private List<String> getListOfNameKeywordsFromSentence(String name) {


        String[] words = name.split("\\s+");
        List<String> wordsList = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i].replaceAll("[^\\w]", "");//removes any puctuation like ?,!

            wordsList.add(words[i]);
        }


        return wordsList;
    }

    public void closeFragment() {
        //clears the whole fragment stack
        getActivity().getSupportFragmentManager()
                .popBackStack(null, getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {

    }

    @Override
    public void showToast(String msg) {
        Utils.showCustomToastForFragments(msg, getContext());
    }


    @Override
    public void onNoItemsFoundResult() {
//todo
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


