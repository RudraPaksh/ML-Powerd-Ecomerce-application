package io.shubh.e_comm_ver1.Main.View;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;
import com.razorpay.PaymentResultListener;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.View.ItemsDetailsTakingFragment;
import io.shubh.e_comm_ver1.Main.Interactor.MainInteractorImplt;
import io.shubh.e_comm_ver1.Main.Presenter.MainPresenter;
import io.shubh.e_comm_ver1.Main.Presenter.MainPresenterImplt;
import io.shubh.e_comm_ver1.SearchPage.View.SearchResultsFragment;
import io.shubh.e_comm_ver1.SellerDashboard.View.SellerDashboardFragment;
import io.shubh.e_comm_ver1.Welcome.View.WelcomeActivity;
import io.shubh.e_comm_ver1.AddressSelectionPage.View.AddressSelectionFragment;
import io.shubh.e_comm_ver1.BagItems.View.BagItemsFragment;
import io.shubh.e_comm_ver1.CategoryItems.View.CategoryItemsFragment;
import io.shubh.e_comm_ver1.ItemDetailPage.View.ItemDetailFragment;
import io.shubh.e_comm_ver1.LikedItems.View.LikedItemsFragment;
import io.shubh.e_comm_ver1.MyOrders.View.MyOrdersFragment;
import io.shubh.e_comm_ver1.Models.ClassForMainActvityItemReclrDATAObject;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.MyProfile.MyProfileFragment;
import io.shubh.e_comm_ver1.Notification.View.NotificationFragment;
import io.shubh.e_comm_ver1.OrderListFrSellerFragment.View.NewOrderListFrSellerFragment;
import io.shubh.e_comm_ver1.PaymentFragments.View.PaymentFragment;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.SearchPage.SearchFragment;
import io.shubh.e_comm_ver1.SellerDashboard.SellerConfirmationFragment;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.Adapters.ReclrAdapterClassForMainActivity;


public class MainActivity extends AppCompatActivity implements MainView, PaymentResultListener, FragmentManager.OnBackStackChangedListener {

    MainPresenter mPresenter;

    //below 2 var for on back pressed
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;


    RecyclerView recyclerView;

    //below is the loading placeholder view
    ShimmerFrameLayout mShimmerViewContainer;
    DrawerLayout drawerLayout;

    String currentFragment;
    //this below code listens to the fragment manager and tells which fragment is currently visible
    FragmentManager fragmentManager;
    int activeFragmnetCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MUST do this before super call or setContentView(...)
        // pick which theme DAY or NIGHT from settings
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
       if (settings.getInt("theme",1)==1){
           StaticClassForGlobalInfo.theme=1;
       }else {
           StaticClassForGlobalInfo.theme=2;
       }


        if(StaticClassForGlobalInfo.theme==1){
            setTheme( R.style.AppThemeLight);
        }else{            setTheme( R.style.AppThemeDark); }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this below code listens to the fragment manager and tells which fragment is currently visible
        //TIP- always use getSupportFragmentManager in each activity and fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        DoUiWork();

        mPresenter = new MainPresenterImplt(this, new MainInteractorImplt());


    }


    private void DoUiWork() {


        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        //this below code listens to the fragment manager and tells which fragment is currently visible
        //RULE- always use getSupportFragmentManager in each activity and fragment
        //RULE - always use getApplicationContext in Glide.with() ..because else glide puts its fragment in backstack and it messess active fragment count
        //RULE - always use  .addToBackStack(null) on any transaction whether rmove or add..doing this invokes this onBackStackChanged listener and helps the main activity know about fragmnet transactions (not sub fragments thgo)
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        navigationDrawerSetUp();
        setSearchViewWork();
        setThemeButton();
    }



    //RULE - always use  .addToBackStack(null) on any transaction whether rmove or add..doing this invokes this onBackStackChanged listener and helps the main activity know about fragmnet transactions (not sub fragments thgo)
    @Override
    public void onBackStackChanged() {
        //this below code listens to the fragment manager and tells which fragment is currently visible
        List<Fragment> f = fragmentManager.getFragments();
        if (f.size() != 0) {
            Fragment frag = f.get(f.size() - 1);
            currentFragment = frag.getClass().getSimpleName();
        }
        activeFragmnetCount = f.size();

        //todo - below code
        if(activeFragmnetCount==0){
            //means we are on main activty ..
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else {
            //means a fragment is open ..so disable the drawer open on swipe from left edge of the screen
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }


    @Override
    public void loadtheUI() {


        set_nav_dr_button_setup();
        setUpToolBar();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_fr_recycler_view_main_activity_items_list);
        recyclerView.setVisibility(View.VISIBLE);


    }

    private void setUpToolBar() {

        ImageButton btBagItems = (ImageButton) findViewById(R.id.btBagItems);
        btBagItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isUserLoggedIn()) {
                    switchActivity(8);
                } else {
                    Utils.showKsnackForLogin(MainActivity.this);
                }

            }
        });

        ImageButton btNotification = (ImageButton) findViewById(R.id.btNotification);
        btNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isUserLoggedIn()) {
                    switchActivity(7);
                } else {
                    Utils.showKsnackForLogin(MainActivity.this);
                }
            }
        });
    }


    private void setSearchViewWork() {
        CardView cv_search = (CardView) findViewById(R.id.cv_search);
        cv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchActivity(3);
            }
        });

    }

    private void set_nav_dr_button_setup() {

        LinearLayout myProfileButton = (LinearLayout) findViewById(R.id.id_fr_nav_bt_profile);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isUserLoggedIn()) {
                    switchActivity(9);
                } else {
                    Utils.showKsnackForLogin(MainActivity.this);
                }
            }
        });
//=----------------------------------------

        LinearLayout switchToSellerbutton = (LinearLayout) findViewById(R.id.id_fr_nav_bt_switch);
        switchToSellerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isUserLoggedIn()) {
                    mPresenter.checkIfUserIsASellerOrNot();
                } else {
                    Utils.showKsnackForLogin(MainActivity.this);
                }
            }
        });


        LinearLayout myOrderBt = (LinearLayout) findViewById(R.id.id_fr_nav_bt_my_orders);
        myOrderBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isUserLoggedIn()) {
                    switchActivity(5);
                } else {
                    Utils.showKsnackForLogin(MainActivity.this);
                }
            }
        });


        LinearLayout btDrwrLiked = (LinearLayout) findViewById(R.id.btDrwrLiked);
        btDrwrLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isUserLoggedIn()) {
                    switchActivity(6);
                } else {
                    Utils.showKsnackForLogin(MainActivity.this);

                }
            }
        });

        LinearLayout btDrwrBag = (LinearLayout) findViewById(R.id.id_fr_nav_bt_cart);
        btDrwrBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isUserLoggedIn()) {
                    switchActivity(8);
                } else {
                    Utils.showKsnackForLogin(MainActivity.this);

                }
            }
        });


//------------------------------------
//doinf a bit changes in ui first ..setting logou icon if user is logged in and vice versa

    /*    TextView tv_fr_button_signin_in_navdr = (TextView) findViewById(R.id.id_fr_tv_nav_login_);
        ImageView iv_fr_icon_button_signin_in_navdr = (ImageView) findViewById(R.id.id_fr_iv_nav_login_icon);
        LinearLayout Login_or_logout_button = (LinearLayout) findViewById(R.id.id_fr_nav_bt_login_or_logout);
*/

        LinearLayout Login_or_logout_button = (LinearLayout) findViewById(R.id.id_fr_ll_as_bt__Login);
        TextView tvLoginBt = (TextView) findViewById(R.id.tvLoginBt);

        if (Utils.isUserLoggedIn() == true) {
            tvLoginBt.setText("Logout");
            // iv_fr_icon_button_signin_in_navdr.setImageResource(R.drawable.logout_icon_);
        } else {
            tvLoginBt.setText("Login");
        }
        //doing some ui work now....the default height for this button linearlayout is different from others button because ll here is depending on child's heighth  and its icon aint android studio rather imported from web so just copying the height of prev button nad setting to it
        //  iv_fr_icon_button_signin_in_navdr.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,switchToSellerbutton.getHeight()));

        //doing actual backend work
        Login_or_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isUserLoggedIn() == true) {
                    //make user sign out and redirect to welcome screen in case he wants to switch id

                    FirebaseAuth.getInstance().signOut();

                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();

                }

            }
        });

    }

    private void navigationDrawerSetUp() {


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        final CoordinatorLayout content = (CoordinatorLayout) findViewById(R.id.content);


        //removing the feature where the main content of activity gets dark as the drawer opoens
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        //this beolw ondrawer slide lets us push the main content when the drawer opens ...
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

//-----------------------------
        ImageButton menu = (ImageButton) findViewById(R.id.id_fr_menu_bt);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.LEFT);

            }
        });


//Setting up the name ,email in drawer if user is not guest user.......do profile pic later

        if (Utils.isUserLoggedIn() != false) {
            TextView tv_name = (TextView) findViewById(R.id.tv_for_nav_dr_name);
            TextView tv_email = (TextView) findViewById(R.id.tv_for_nav_dr_email);

            tv_name.setText(StaticClassForGlobalInfo.UserName);
            tv_email.setText(StaticClassForGlobalInfo.UserEmail);
        }
    }


    @Override
    public void onBackPressed() {

        if (activeFragmnetCount == 0) { //if no fragment is opened then ..just exut the application ..else clode the fragment

            //below code is for "click two time to exit the application"
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                finishAffinity();
                System.exit(0);
            } else {
                //   Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
                showToast("Tap back button once more to exit");
            }
            mBackPressed = System.currentTimeMillis();

        /*//just adding an animatiion here whic makes it go with animation sliding to right
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
*/
        } else {
            switch (currentFragment) {
                case "SellerConfirmationFragment":
                    SellerConfirmationFragment sellerConfirmationFragment = (SellerConfirmationFragment) fragmentManager.findFragmentByTag("SellerConfirmationFragment");
                    sellerConfirmationFragment.closeFragment();
                    return;
                case "NotificationFragment":
                    NotificationFragment notificationFragment = (NotificationFragment) fragmentManager.findFragmentByTag("NotificationFragment");
                    notificationFragment.closeFragment();
                    return;
                case "SellerDashboardFragment":
                    SellerDashboardFragment sellerDashboardFragment = (SellerDashboardFragment) fragmentManager.findFragmentByTag("SellerDashboardFragment");
                    sellerDashboardFragment.closeFragment();
                    return;
                case "PaymentFragment":
                    PaymentFragment paymentFragment = (PaymentFragment) fragmentManager.findFragmentByTag("PaymentFragment");
                    paymentFragment.closeFragment();
                    return;
                case "AddressSelectionFragment":
                    AddressSelectionFragment addressSelectionFragment = (AddressSelectionFragment) fragmentManager.findFragmentByTag("AddressSelectionFragment");
                    addressSelectionFragment.closeFragment();
                    return;
                case "BagItemsFragment":
                    BagItemsFragment bagItemsFragment = (BagItemsFragment) fragmentManager.findFragmentByTag("BagItemsFragment");
                    bagItemsFragment.closeFragment();
                    return;
                case "CategoryItemsFragment":
                    CategoryItemsFragment categoryItemsFragment = (CategoryItemsFragment) fragmentManager.findFragmentByTag("CategoryItemsFragment");
                    categoryItemsFragment.closeFragment();
                    return;
                case "ItemDetailFragment":
                    ItemDetailFragment itemDetailFragment = (ItemDetailFragment) fragmentManager.findFragmentByTag("ItemDetailFragment");
                    itemDetailFragment.closeFragment();
                    return;
                case "ItemsDetailsTakingFragment":
                    ItemsDetailsTakingFragment itemsDetailsTakingFragment = (ItemsDetailsTakingFragment) fragmentManager.findFragmentByTag("ItemsDetailsTakingFragment");
                    itemsDetailsTakingFragment.closeFragment();
                    return;
                case "LikedItemsFragment":
                    LikedItemsFragment likedItemsFragment = (LikedItemsFragment) fragmentManager.findFragmentByTag("LikedItemsFragment");
                    likedItemsFragment.closeFragment();
                    return;
                case "MyOrdersFragment":
                    MyOrdersFragment myOrdersFragment = (MyOrdersFragment) fragmentManager.findFragmentByTag("MyOrdersFragment");
                    myOrdersFragment.closeFragment();
                    return;
                case "NewOrderListFrSellerFragment":
                    NewOrderListFrSellerFragment newOrderListFrSellerFragment = (NewOrderListFrSellerFragment) fragmentManager.findFragmentByTag("NewOrderListFrSellerFragment");
                    newOrderListFrSellerFragment.closeFragment();
                    return;
                case "SearchFragment":
                    SearchFragment searchFragment = (SearchFragment) fragmentManager.findFragmentByTag("SearchFragment");
                    searchFragment.closeFragment();
                    return;
                case "SearchResultsFragment":
                    SearchResultsFragment searchResultsFragment = (SearchResultsFragment) fragmentManager.findFragmentByTag("SearchResultsFragment");
                    searchResultsFragment.closeFragment();
                    return;
                case "MyProfileFragment":
                    MyProfileFragment myProfileFragment = (MyProfileFragment) fragmentManager.findFragmentByTag("MyProfileFragment");
                    myProfileFragment.closeFragment();
                    return;
                default://use deafult to close fragments
                    // fragmentManager.popBackStackImmediate(); //cant use this it messes the animation because  .addToBackStack(null)
                    //to an fargment wile adding it means that while poopping it from stack it will reverse all that happened wile adding it ..
                    //so my animation was enter from left and close from right..while popping it ..it reverse the animation of opening one rather than going fro closing one
                    //so I m removing each one of them than having a default
            }
        }

    }


    @Override
    public void switchActivity(int i) {
        // progressBar.setVisibility(android.view.View.GONE);
        drawerLayout.closeDrawer(Gravity.LEFT);

        switch (i) {
            case 1:
                Intent in = new Intent(MainActivity.this, MainActivity.class);
                startActivity(in);
                return;
            case 2:
                SellerConfirmationFragment sellerConfirmationFragment = new SellerConfirmationFragment();
                sellerConfirmationFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                sellerConfirmationFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        .add(R.id.drawerLayout, sellerConfirmationFragment, "SellerConfirmationFragment")
                        .addToBackStack(null)
                        .commit();
                return;
            case 3:
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                searchFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        .add(R.id.drawerLayout, searchFragment, "SearchFragment")
                        .addToBackStack(null)
                        .commit();

                return;
            case 4:
                AddressSelectionFragment addressSelectionFragment = new AddressSelectionFragment();
                addressSelectionFragment.setLocalVariables(false, new Order());
                addressSelectionFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                addressSelectionFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        .add(R.id.drawerLayout, addressSelectionFragment, "AddressSelectionFragment")
                        .addToBackStack(null)
                        .commit();
                return;
            case 5:
                MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                myOrdersFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                myOrdersFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        .add(R.id.drawerLayout, myOrdersFragment, "MyOrdersFragment")
                        .addToBackStack(null)
                        .commit();
                return;
            case 6:
                LikedItemsFragment likedItemsFragment = new LikedItemsFragment();
                likedItemsFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                likedItemsFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        //both parameters for instantiating the fragment will be same as at rootl level of ctgr tree ,the name of ctgr and path is same
                        .add(R.id.drawerLayout, likedItemsFragment, "ItemsDetailsTakingFragment")
                        .addToBackStack(null)
                        .commit();
                return;
            case 7:
                NotificationFragment notificationFragment = new NotificationFragment();
                notificationFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                notificationFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        .add(R.id.drawerLayout, notificationFragment, "NotificationFragment")
                        .addToBackStack(null)
                        .commit();

                return;
            case 8:
                BagItemsFragment bagItemsFragment = new BagItemsFragment();
                bagItemsFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                bagItemsFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        .add(R.id.drawerLayout, bagItemsFragment, "BagItemsFragment")
                        .addToBackStack(null)
                        .commit();

                return;
            case 9:
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                myProfileFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                myProfileFragment.setExitTransition(new Slide(Gravity.RIGHT));

                fragmentManager.beginTransaction()
                        .add(R.id.drawerLayout, myProfileFragment, "MyProfileFragment")
                        .addToBackStack(null)
                        .commit();

                return;

        }

    }

    private void setThemeButton() {
        DayNightSwitch dayNightSwitch;
        View backgroundView;

        dayNightSwitch = (DayNightSwitch)findViewById(R.id.day_night_switch);
     //   backgroundView = (View)findViewById(R.id.background_view);
        if(StaticClassForGlobalInfo.theme==1) {
            dayNightSwitch.setIsNight(false);
        }else {
            dayNightSwitch.setIsNight(true);
        }
        dayNightSwitch.setDuration(500);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean isNight) {
                if(isNight)
                {
                    final String PREFS_NAME = "MyPrefsFile";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    settings.edit().putInt("theme", 2).commit();


                    StaticClassForGlobalInfo.theme=2;
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // decide which theme to use DAY or NIGHT and save it


                            MainActivity.this.recreate();
                        }
                    }, 400);//800=.8sec = 8millisec

                }
                else
                {

                    final String PREFS_NAME = "MyPrefsFile";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    settings.edit().putInt("theme", 1).commit();

                    StaticClassForGlobalInfo.theme=1;
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            MainActivity.this.recreate();
                        }
                    }, 400);//800=.8sec = 8millisec

                }
            }
        });
    }



    @Override
    public Context getContext(boolean getActvityContext) {
        if (getActvityContext == true) {
            return this;
        }
        return this.getApplicationContext();
    }

    @Override
    public void showProgressBar(boolean b) {
        if (b == true) {
            //   progressBar.setVisibility(android.view.View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
        } else {
            //  progressBar.setVisibility(android.view.View.INVISIBLE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);

        }
    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {
    /*  //  LinearLayout ll_Root = (LinearLayout) findViewById(R.id.drawerLayout);
        Snackbar snackbar = Snackbar
                .make(drawerLayout, msg, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(actionName, new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {
                        //    mPresenter.LoginRelatedWork();
                        Intent in = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(in);
                    }
                });

        snackbar.show();*/
    }

    @Override
    public void showToast(String msg) {

        Utils.showToast(msg, MainActivity.this);
    }

    @Override
    public void setReclrViewToshowCtgrs(ArrayList<ClassForMainActvityItemReclrDATAObject> listOfDataObjectsForAdapter) {
        //now executing the UI part
        recyclerView = (RecyclerView) findViewById(R.id.id_fr_recycler_view_main_activity_items_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.removeAllViews();

      /*  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CategoryItemsActivity.this);
        recyclerView.setLayoutManager(layoutManager);*/

        // set a GridLayoutManager with 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView


        //data_list_for_adapter = list_of_data_objects__for_adapter;
        ReclrAdapterClassForMainActivity adapter = new ReclrAdapterClassForMainActivity(MainActivity.this, getApplicationContext(), listOfDataObjectsForAdapter);
        recyclerView.setAdapter(adapter);


        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
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


        if (listOfDataObjectsForAdapter.size() == 0) {
            showToast("No Categories found");
        }
    }


    @Override
    public void showProgressBarOfDrwrBtSwitchToSeller(boolean b) {

        ImageView iv_icon_bt_drwr_switch_to_seller = (ImageView) findViewById(R.id.iv_icon_bt_drwr_switch_to_seller);
        ProgressBar progressBar_fr_bt_switch_to_seller = (ProgressBar) findViewById(R.id.progressBar_fr_bt_switch_to_seller);
        progressBar_fr_bt_switch_to_seller.setLayoutParams(new LinearLayout.LayoutParams(iv_icon_bt_drwr_switch_to_seller.getWidth(), iv_icon_bt_drwr_switch_to_seller.getHeight()));

        if (b == true) {
            progressBar_fr_bt_switch_to_seller.setVisibility(View.VISIBLE);

            iv_icon_bt_drwr_switch_to_seller.setVisibility(View.GONE);
        } else {

            progressBar_fr_bt_switch_to_seller.setVisibility(View.GONE);

            iv_icon_bt_drwr_switch_to_seller.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            PaymentFragment fragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag("PaymentFragment");
            fragment.onPaymentSuccessCallbackFromMainActivty(s);
        } catch (Error e) {
            Log.e("MainActivty", "onPaymentSuccess: " + e.getMessage());
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            PaymentFragment fragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag("PaymentFragment");
            fragment.onPaymentFailiureCallbackFromMainActivty(s);
        } catch (Error e) {
            Log.e("MainActivty", "onPaymentSuccess: " + e.getMessage());

        }

    }


}
