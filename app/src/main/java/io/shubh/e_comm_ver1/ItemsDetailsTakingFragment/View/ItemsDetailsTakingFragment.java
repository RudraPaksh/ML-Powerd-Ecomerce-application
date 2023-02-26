package io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.View;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.Presenter.ItemsDetailsTakingPresenter;
import io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.Presenter.ItemsDetailsTakingPresenterImplt;
import io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.Interactor.ItemsDetailsTakingInteractorImplt;
import io.shubh.e_comm_ver1.Models.Category;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Utils.Utils;


// ItemsDetailsTaking
public class ItemsDetailsTakingFragment extends Fragment implements ItemsDetailsTakingView {

    ItemsDetailsTakingPresenter mPresenter;
    View containerViewGroup;
    BottomSheetBehavior behavior_bttm_sheet_which_select_img;

    LayoutInflater inflater;

    //----------------------------------------------------
    Bitmap singleImageBitmap;
    List<Bitmap> multipleImageBitmap;
    int allowedImagesAmountForPickup = 5;

    int PICKUP_MULTIPLE_IMAGE_FROM_STORAGE = 1;
    int CAPTURE_IMAGE_FROM_CAMERA = 2;
    int PICKUP_SINGLE_IMAGE_FROM_STORAGE = 3;

    boolean isEditBtOnAnyImageViewIscalled = false;
    int indexOfIvOfWhichEditBtWasClicked = -1;
    //------------------------------------------------------
    String mCategoryPath = "null initially";
    String mCategoryName = "null initially";
    String rootCtgr;
    String subCtgr;
    String subSubCtgr;
    //below level var is used for info on where in the multilevelctgr we are ..when we wnat to go backwards..by default we are always showing the lsit of ctgr..ie lvl1
    int levelInMultiLvlCtgrSys = 1;
    boolean isCtgrWithNosubCtgrIsSelected = false;
    //--------------------------------------------------
    String TAG = "!!!!!";
    //below is false by default because most items doesnt need Variety,Only items like clothes ,shoes have variety have of size
    boolean isAddVarietyFeatureAlreadyEnabled = false;
    boolean stateOfVisibility = true;
    boolean isItemUploading = false;



    public ItemsDetailsTakingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerViewGroup = inflater.inflate(R.layout.fragment_items_details_taking, container, false);
        this.inflater = inflater;


        DoUiWork();
        //always do presenter related work at last in Oncreate
        mPresenter = new ItemsDetailsTakingPresenterImplt(this, new ItemsDetailsTakingInteractorImplt() {
        });


        return containerViewGroup;
    }

    private void DoUiWork() {

        themeNavAndStatusBar(getActivity());
        ImageButton btCloseFrag = (ImageButton)containerViewGroup.findViewById(R.id.id_fr_menu_bt);
        btCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        setUpImagePickingDialogueBottomSheetAndImgBttnForIt();
        setUpCtgrSelectionBox();
        setUpVarietyGivingOPtionAndVisibilityOption();
        setUpUploadBtUIAndLogicWork();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void themeNavAndStatusBar(Activity activity)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

        Window w = activity.getWindow();
        w.setNavigationBarColor(Color.parseColor("#70EE03"));
        w.setStatusBarColor(Color.parseColor("#5C8F30"));
    }


    //================================below functions are for Image selection related=======================================

    private void setUpImagePickingDialogueBottomSheetAndImgBttnForIt() {

        ImageButton btAddImages = (ImageButton) containerViewGroup.findViewById(R.id.id_fr_bt_choose_images);
        btAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior_bttm_sheet_which_select_img.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
//---------------------------------------Bottom Sheet setup------------------

        CoordinatorLayout rootView = (CoordinatorLayout) containerViewGroup.findViewById(R.id.cl_root);
        View inflatedBottomSheetdialog = inflater.inflate(R.layout.bottom_sheet_select_image_dialog, rootView, false);
        rootView.addView(inflatedBottomSheetdialog);

        behavior_bttm_sheet_which_select_img = BottomSheetBehavior.from(inflatedBottomSheetdialog);

        behavior_bttm_sheet_which_select_img.setState(BottomSheetBehavior.STATE_COLLAPSED);

        View dim_background_of_bottom_sheet = (View) containerViewGroup.findViewById(R.id.touch_to_dismiss_bottom_sheet_dim_background);
        dim_background_of_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                behavior_bttm_sheet_which_select_img.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        behavior_bttm_sheet_which_select_img.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dim_background_of_bottom_sheet.setVisibility(View.GONE);
                    // is_bottom_sheet_expanded = false;
                } else {
                    dim_background_of_bottom_sheet.setVisibility(View.VISIBLE);
                    // is_bottom_sheet_expanded = true;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        Button btGallery = (Button) inflatedBottomSheetdialog.findViewById(R.id.bt_gallery);
        btGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior_bttm_sheet_which_select_img.setState(BottomSheetBehavior.STATE_COLLAPSED);

                //when edit bt is clicked ,that means only one image needs to be picked up and replaced
                if (isEditBtOnAnyImageViewIscalled == false) {
                    //throwing an intent which opens gallery to select multiple images
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    if (allowedImagesAmountForPickup != 0) {
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICKUP_MULTIPLE_IMAGE_FROM_STORAGE);
                    } else {
                        showToast("Images Limit of 5 crossed");
                    }
                } else {
                    //throwing an intent which opens gallery to select single images
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickIntent, PICKUP_SINGLE_IMAGE_FROM_STORAGE);
                    pickIntent.setType("image/*");
                    isEditBtOnAnyImageViewIscalled = false;
                }
            }
        });

        Button btCamera = (Button) inflatedBottomSheetdialog.findViewById(R.id.bt_camera);
        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checking if we have camera permission
                if (TedPermission.isGranted(getContext(), Manifest.permission.CAMERA)) {
                    behavior_bttm_sheet_which_select_img.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (allowedImagesAmountForPickup != 0) {
                        startActivityForResult(cameraIntent, CAPTURE_IMAGE_FROM_CAMERA);
                    } else {
                        showToast("Images Limit of 5 crossed");
                    }
                } else {
                    //call for getting permission
                    getCameraPermissions();
                }

            }
        });

    }

    private void getCameraPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showToast("Permission Granted");
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                showToast("Permission Denied\n" + deniedPermissions.toString());
            }


        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }


    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
//===================//now check if image is selected from storage/gallery amd the single and multiple img picking was allowed
            if (requestCode == PICKUP_MULTIPLE_IMAGE_FROM_STORAGE && resultCode == Activity.RESULT_OK && data != null) {
                // Get the Image from data

                //below id detetcs when single image is detetcted
                if (data.getData() != null) {

                    Bitmap bitmap = null;
                    Uri imageUri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                    singleImageBitmap = bitmap;

                    mPresenter.onSingleImageSelectedOrCaptured(singleImageBitmap);

                } else {
                    //below id detetcs when single image is detetcted
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Bitmap> mArrayBitmap = new ArrayList<Bitmap>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            Bitmap bitmap = null;
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            singleImageBitmap = bitmap;
                            mArrayBitmap.add(singleImageBitmap);
                        }
                        multipleImageBitmap = mArrayBitmap;
                        Log.i("LOG_TAG", "Selected Images" + multipleImageBitmap.size());
                        if (multipleImageBitmap.size() <= allowedImagesAmountForPickup) {
                            mPresenter.onMultipleImagesSelectedFromGallery(multipleImageBitmap);
                        } else {
                            showToast("images selected are more than allowed ie..5");
                            Log.i(TAG, "images selected are more than allowed ");
                        }
                    }
                }
                //===================//now check if image is captured from camera
            } else if (requestCode == CAPTURE_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK && data != null) {

                Bitmap bitmap = null;
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    singleImageBitmap = bitmap;
                    mPresenter.onSingleImageSelectedOrCaptured(singleImageBitmap);
                } catch (Exception e) {
                    showToast("Some problem occured");
                    Log.e("Camera", e.toString());
                }
//===================//now check if image is selected from storage amd the single img picking was allowed due to edit button click
            } else if (requestCode == PICKUP_SINGLE_IMAGE_FROM_STORAGE && resultCode == Activity.RESULT_OK && data != null) {

                Bitmap bitmap = null;
                try {
                    Uri imageUri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    singleImageBitmap = bitmap;
                    mPresenter.onEditBtOfAnyImageViewIsClickedAndBitmapHasReturned(singleImageBitmap, indexOfIvOfWhichEditBtWasClicked);
                } catch (Exception e) {
                    showToast("Some problem occured");
                    Log.e("Camera", e.toString());
                }
            } else {
                showToast("No Images Picked");
            }
        } catch (Exception e) {
            showToast("Something went wrong");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void decrementAllowedImagesPickUpAmount(int decrmntThisAmount) {
        allowedImagesAmountForPickup = allowedImagesAmountForPickup - decrmntThisAmount;
    }

    @Override
    public void makeNewImageViewAndSetImageToIt(Bitmap imgBitmap) {
        LinearLayout llContainerFrIvs = (LinearLayout) containerViewGroup.findViewById(R.id.ll_container_fr_ivs);


        CardView inflatedIvRlContainer = (CardView) inflater.inflate(R.layout.inflate_relative_layouts_with_image_views, llContainerFrIvs, false);
        llContainerFrIvs.addView(inflatedIvRlContainer);

        ImageView iv = (ImageView) inflatedIvRlContainer.findViewById(R.id.id_fr_slected_image);
        ImageButton btRemove = (ImageButton) inflatedIvRlContainer.findViewById(R.id.id_fr_bt_remove);
        ImageButton btEdit = (ImageButton) inflatedIvRlContainer.findViewById(R.id.id_fr_bt_edit);

        iv.setImageBitmap(imgBitmap);

        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int indexOfThisRelativeView = llContainerFrIvs.indexOfChild(inflatedIvRlContainer);
                mPresenter.removeIvAndItsDataAtThisIndex(indexOfThisRelativeView);
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int indexOfThisRelativeView = llContainerFrIvs.indexOfChild(inflatedIvRlContainer);
                indexOfIvOfWhichEditBtWasClicked = indexOfThisRelativeView;
                isEditBtOnAnyImageViewIscalled = true;
                behavior_bttm_sheet_which_select_img.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

    }

    @Override
    public void removeIvAtThisIndex(int index) {
        LinearLayout llContainerFrIvs = (LinearLayout) containerViewGroup.findViewById(R.id.ll_container_fr_ivs);
        llContainerFrIvs.removeViewAt(index);
    }

    @Override
    public void incrementAllowedImagesPickUpAmount(int i) {
        allowedImagesAmountForPickup++;
    }

    @Override
    public void replaceBitmapOnThisPosition(int indexOfIvOfWhichEditBtWasClicked, Bitmap singleImageBitmap) {
        LinearLayout llContainerFrIvs = (LinearLayout) containerViewGroup.findViewById(R.id.ll_container_fr_ivs);
        ImageView iv = (ImageView) llContainerFrIvs.getChildAt(indexOfIvOfWhichEditBtWasClicked).findViewById(R.id.id_fr_slected_image);
        iv.setVisibility(View.VISIBLE);
        iv.setImageBitmap(singleImageBitmap);
    }

    @Override
    public void onFinishedUploadingItem() {

        isItemUploading=false;
        Utils.showCustomToastForFragments("Item Uploaded Sucessfully", getContext());
        closeFragment();
    }


//=====================================below functions are ctgr related functions=========================================


    private void setUpCtgrSelectionBox() {
        //  loadCategorylayoutsInTheSidebarWithAnimation();
        loadCategorylayoutsInTheSidebar();
    }

    private void loadCategorylayoutsInTheSidebarWithAnimation() {
        LinearLayout ll_container_side_bar = (LinearLayout) containerViewGroup.findViewById(R.id.container_fr_ctgr);
        TranslateAnimation animate = new TranslateAnimation(0, -ll_container_side_bar.getWidth(), 0, 0);
        animate.setDuration(300);
        ll_container_side_bar.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadCategorylayoutsInTheSidebar();

                TranslateAnimation animate = new TranslateAnimation(ll_container_side_bar.getWidth(), 0, 0, 0);
                animate.setDuration(300);
                ll_container_side_bar.startAnimation(animate);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    private void loadCategorylayoutsInTheSidebar() {


        LinearLayout llContainerFrCtgr = (LinearLayout) containerViewGroup.findViewById(R.id.container_fr_ctgr);

        if (levelInMultiLvlCtgrSys == 1) {

            ArrayList<Category> categoriesList = StaticClassForGlobalInfo.categoriesList;
            llContainerFrCtgr.removeAllViews();
            setUpTheHeaderBackbutton(true);
            updateCtgrBoxHeader("Select a Category");

            for (int i = 0; i < categoriesList.size(); i++) {

                View inflatedRow = inflater.inflate(R.layout.infalte_rows_fr_ctgr_box_of_item_details_taking_frag, llContainerFrCtgr, false);
                llContainerFrCtgr.addView(inflatedRow);

                TextView tv = (TextView) inflatedRow.findViewById(R.id.tv_ctgr_name);
                ImageView iv = (ImageView) inflatedRow.findViewById(R.id.iv_ctgr_indicator);

                tv.setText(categoriesList.get(i).getName());
                //making the iv with arrows if the ctgr have subctgrs
                if (categoriesList.get(i).isHaveSubCatgr()) {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_right_svg));
                } else {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_svg));
                }


                int finalI = i;
                inflatedRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mCategoryName = categoriesList.get(finalI).getName();
                        rootCtgr = mCategoryName;
                        mCategoryPath = rootCtgr;


                        /*if (categoriesList.get(i).isHaveSubCatgr()) {
                            // above if is to check if the same ctgr is clicked twice or not...because if it is...then the ctgrpath will already have the string i am adding below
                            mCategoryPath = rootCtgr + "/" + subCtgr;
                            mParam1CategoryName = subCategoriesList.get(finalJ).getName();
                        }
*/
                        if (categoriesList.get(finalI).isHaveSubCatgr()) {
                            levelInMultiLvlCtgrSys = 2;
                            loadCategorylayoutsInTheSidebarWithAnimation();

                            isCtgrWithNosubCtgrIsSelected = false;
                        } else {
                            isCtgrWithNosubCtgrIsSelected = true;
                            subCtgr = "null";
                            subSubCtgr = "null";

                            levelInMultiLvlCtgrSys = 1;
                            updateCtgrBoxHeader("Ctgr Selected: " + mCategoryName);
                            setRadioBtToAllOtherRowsWhichHadThemOriginally(inflatedRow);
                            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_svg));
                            iv.setTag("ic_check_svg");

                        }
                    }
                });
            }

        }


        if (levelInMultiLvlCtgrSys == 2) {
            //  tv_header.setText(mParam1CategoryName);
            //I have only have the name of the category and where it is on the ctgr level
            //since its at one ,I will just iterate through each ctgr at one level to find it
            //after finding it i will hust load the subctgr under it

            for (int i = 0; i < StaticClassForGlobalInfo.categoriesList.size(); i++) {
                if (StaticClassForGlobalInfo.categoriesList.get(i).getName() == rootCtgr) {

                    ArrayList<Category.SubCategory> subCategoriesList = StaticClassForGlobalInfo.categoriesList.get(i).getSubCategoriesList();
                    llContainerFrCtgr.removeAllViews();
                    setUpTheHeaderBackbutton(false);
                    updateCtgrBoxHeader(rootCtgr);

                    for (int j = 0; j < subCategoriesList.size(); j++) {

                        View inflatedRow = inflater.inflate(R.layout.infalte_rows_fr_ctgr_box_of_item_details_taking_frag, llContainerFrCtgr, false);
                        llContainerFrCtgr.addView(inflatedRow);

                        TextView tv = (TextView) inflatedRow.findViewById(R.id.tv_ctgr_name);
                        ImageView iv = (ImageView) inflatedRow.findViewById(R.id.iv_ctgr_indicator);

                        tv.setText(subCategoriesList.get(j).getName());
                        //making the iv with arrows if the subctgr have subsubctgrs
                        if (subCategoriesList.get(j).isHaveSubSubCatgr()) {
                            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_right_svg));
                        } else {
                            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_svg));
                        }


                        int finalJ = j;
                        inflatedRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mCategoryName = subCategoriesList.get(finalJ).getName();
                                subCtgr = mCategoryName;
                                mCategoryPath = rootCtgr + "/" + subCtgr;

                                updateCtgrBoxHeader(mCategoryName);

                              /*  if (mCategoryPath.indexOf(subCategoriesList.get(finalJ).getName()) == -1) {
                                    // above if is to check if the same ctgr is clicked twice or not...because if it is...then the ctgrpath will already have the string i am adding below
                                    mCategoryPath = rootCtgr + "/" + subCtgr;
                                    mParam1CategoryName = subCategoriesList.get(finalJ).getName();
                                }
*/
                                if (subCategoriesList.get(finalJ).isHaveSubSubCatgr()) {
                                    levelInMultiLvlCtgrSys = 3;
                                    loadCategorylayoutsInTheSidebarWithAnimation();
                                    isCtgrWithNosubCtgrIsSelected = false;
                                    updateCtgrBoxHeader(mCategoryName);

                                } else {
                                    levelInMultiLvlCtgrSys = 2;
                                    subSubCtgr = "null";
                                    updateCtgrBoxHeader("Ctgr Selected: " + mCategoryName);

                                    setRadioBtToAllOtherRowsWhichHadThemOriginally(inflatedRow);
                                    iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_svg));
                                    iv.setTag("ic_check_svg");
                                    isCtgrWithNosubCtgrIsSelected = true;

                                }
                            }
                        });
                    }

                }
            }
        }
        //below if checks if we are at level 3 that is of subsubctgr..so it will show the subsubctgrlist
        if (levelInMultiLvlCtgrSys == 3) {

            //I have only have the name of the subcategory and where it is on the ctgr level
            //since its at two,first  ,I will just iterate through each ctgr at one level to find it whome it is subctgr of
            //after finding it i will hust load the subsubctgr under it
            for (int i = 0; i < StaticClassForGlobalInfo.categoriesList.size(); i++) {
                if (StaticClassForGlobalInfo.categoriesList.get(i).isHaveSubCatgr() == true) {

                    for (int j = 0; j < StaticClassForGlobalInfo.categoriesList.get(i).getSubCategoriesList().size(); j++) {
                        if (subCtgr == StaticClassForGlobalInfo.categoriesList.get(i).getSubCategoriesList().get(j).getName()) {
                            //we have got the subctgr in the ctgrpath
                            ArrayList<Category.SubCategory.SubSubCategory> subsubCategoriesList = StaticClassForGlobalInfo.categoriesList.get(i).getSubCategoriesList().get(j).getSubSubCategoryList();
                            llContainerFrCtgr.removeAllViews();
                            setUpTheHeaderBackbutton(false);
                            updateCtgrBoxHeader(subCtgr);

                            for (int k = 0; k < subsubCategoriesList.size(); k++) {
                                View inflatedRow = inflater.inflate(R.layout.infalte_rows_fr_ctgr_box_of_item_details_taking_frag, llContainerFrCtgr, false);
                                llContainerFrCtgr.addView(inflatedRow);

                                TextView tv = (TextView) inflatedRow.findViewById(R.id.tv_ctgr_name);
                                ImageView iv = (ImageView) inflatedRow.findViewById(R.id.iv_ctgr_indicator);

                                tv.setText(subsubCategoriesList.get(k).getName());
                                iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_svg));


                                int finalK = k;
                                inflatedRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mCategoryName = subsubCategoriesList.get(finalK).getName();
                                        subSubCtgr = mCategoryName;
                                        mCategoryPath = rootCtgr + "/" + subCtgr + "//" + subSubCtgr;

                                        updateCtgrBoxHeader("Ctgr Selected: " + mCategoryName);
                                        setRadioBtToAllOtherRowsWhichHadThemOriginally(inflatedRow);
                                        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_svg));
                                        iv.setTag("ic_check_svg");
                                        isCtgrWithNosubCtgrIsSelected = true;
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

    }

    private void setUpTheHeaderBackbutton(boolean makeThehdrBtVisiblityGone) {
//        makeThehdrBtVisiblityGone is true when the list of ctgr is shown ..so logically we cant go back than that

        ImageButton btGoBackwards = (ImageButton) containerViewGroup.findViewById(R.id.id_fr_bt_bottom_sheet_back);
        if (makeThehdrBtVisiblityGone == true) {
            btGoBackwards.setVisibility(View.GONE);
        } else {
            btGoBackwards.setVisibility(View.VISIBLE);

            btGoBackwards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //below if means if the ctgr box is showing the subctgrlist
                    if (levelInMultiLvlCtgrSys == 2) {

                        isCtgrWithNosubCtgrIsSelected = false;
                        levelInMultiLvlCtgrSys = 1;
                        //showing rotation in baackward direction
                        LinearLayout ll_container_side_bar = (LinearLayout) containerViewGroup.findViewById(R.id.container_fr_ctgr);
                        TranslateAnimation animate = new TranslateAnimation(0, ll_container_side_bar.getWidth(), 0, 0);
                        animate.setDuration(300);
                        ll_container_side_bar.startAnimation(animate);
                        animate.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                loadCategorylayoutsInTheSidebar();

                                TranslateAnimation animate = new TranslateAnimation(-ll_container_side_bar.getWidth(), 0, 0, 0);
                                animate.setDuration(300);
                                ll_container_side_bar.startAnimation(animate);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });


                    } else if (levelInMultiLvlCtgrSys == 3) {

                        isCtgrWithNosubCtgrIsSelected = false;
                        levelInMultiLvlCtgrSys = 2;
                        // mCategoryPath = rootCtgr ;
                        //showing rotation in baackward direction
                        LinearLayout ll_container_side_bar = (LinearLayout) containerViewGroup.findViewById(R.id.container_fr_ctgr);
                        TranslateAnimation animate = new TranslateAnimation(0, ll_container_side_bar.getWidth(), 0, 0);
                        animate.setDuration(300);
                        ll_container_side_bar.startAnimation(animate);
                        animate.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                loadCategorylayoutsInTheSidebar();

                                TranslateAnimation animate = new TranslateAnimation(-ll_container_side_bar.getWidth(), 0, 0, 0);
                                animate.setDuration(300);
                                ll_container_side_bar.startAnimation(animate);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });


                    }
                }
            });

        }
    }

    private void updateCtgrBoxHeader(String mCategoryNamee) {
        TextView tvHeaderOfCtgrBox = (TextView) containerViewGroup.findViewById(R.id.id_br_bottom_sheet_tv_header);
        tvHeaderOfCtgrBox.setText(mCategoryNamee);


    }

    private void setRadioBtToAllOtherRowsWhichHadThemOriginally(View inflatedRow) {

        LinearLayout llContainerForCtgrBox = (LinearLayout) containerViewGroup.findViewById(R.id.container_fr_ctgr);
        final int childCount = llContainerForCtgrBox.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RelativeLayout rlContainingOtherTv = (RelativeLayout) llContainerForCtgrBox.getChildAt(i);
            ImageView iv = (ImageView) rlContainingOtherTv.findViewById(R.id.iv_ctgr_indicator);
            String imageName2 = (String) iv.getTag();
            if (imageName2 == "ic_check_svg") {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_svg));
            }
        }
    }

    //-----------------------------------Variety giving facuility------------------------------------------
    private void setUpVarietyGivingOPtionAndVisibilityOption() {

        Switch mAddOrRemoveSwitch = (Switch) containerViewGroup.findViewById(R.id.switch1);
        LinearLayout llContainerFrVarities = (LinearLayout) containerViewGroup.findViewById(R.id.container_for_varieties);
        EditText edNameOfVariety = (EditText) containerViewGroup.findViewById(R.id.ed_name_of_variety);
        Button btAdd = (Button) containerViewGroup.findViewById(R.id.btAdd_indivisual_variety);

        mAddOrRemoveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isAddVarietyFeatureAlreadyEnabled == false) {
                    mAddOrRemoveSwitch.setText("Remove");
                    isAddVarietyFeatureAlreadyEnabled = true;
                    edNameOfVariety.setVisibility(View.VISIBLE);
                    llContainerFrVarities.setVisibility(View.VISIBLE);
                    btAdd.setVisibility(View.VISIBLE);
                    btAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            View inflatedRow = inflater.inflate(R.layout.infalte_rows_fr_add_variety_box, llContainerFrVarities, false);
                            llContainerFrVarities.addView(inflatedRow);

                            EditText ed = (EditText) inflatedRow.findViewById(R.id.ed_name);
                            ImageButton ibt_del = (ImageButton) inflatedRow.findViewById(R.id.bt_del);

                            ibt_del.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    llContainerFrVarities.removeView(inflatedRow);

                                }
                            });
                        }
                    });

                    btAdd.performClick();
                    btAdd.performClick();

                } else {
                    mAddOrRemoveSwitch.setText("Add");
                    isAddVarietyFeatureAlreadyEnabled = false;
                    llContainerFrVarities.setVisibility(View.GONE);
                    edNameOfVariety.setVisibility(View.GONE);
                    btAdd.setVisibility(View.GONE);

                    llContainerFrVarities.removeAllViews();
                }
            }
        });

//-------------------------------------setting the bottomSheet which gives explaination about this features




        ImageButton btShowBttmSheetFrInfrOnVariety = (ImageButton) containerViewGroup.findViewById(R.id.bt_show_btm_sheet_fr_info);
        btShowBttmSheetFrInfrOnVariety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
showToast("Add Variety to Item like Color green and red for a Shirt ,Or different sizes for a Shoe");
            }
        });

//---------------Visibility option work from here
        Switch switchShowOrHideOfVisibiltyOption = (Switch) containerViewGroup.findViewById(R.id.switch2);
        switchShowOrHideOfVisibiltyOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (stateOfVisibility == true) {
                    switchShowOrHideOfVisibiltyOption.setText("Hidden");
                    stateOfVisibility = false;
                } else {
                    stateOfVisibility = true;
                    switchShowOrHideOfVisibiltyOption.setText("Visible");
                }

            }
        });

        ImageButton btShowBttmSheetFrInfrOnVisibilty = (ImageButton) containerViewGroup.findViewById(R.id.ibt_show_bttm_sheet_info);
        btShowBttmSheetFrInfrOnVisibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
showToast("This toggle makes the item show/hidden for users to see in app.This is useful when item is out of stock ,or You dont want incoming orders due to some reasons");

            }
        });
    }


    //------------------------------------Upload data related function-------------------------------
    private void setUpUploadBtUIAndLogicWork() {

        Button btUploadData = (Button) containerViewGroup.findViewById(R.id.bt_upload_data);
        btUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //first check  if images have been selected
                if (allowedImagesAmountForPickup != 5) {

                    //second check  if inputs are given to edittexts
                    EditText edName = (EditText) containerViewGroup.findViewById(R.id.ed_name_of_item);
                    EditText edPrice = (EditText) containerViewGroup.findViewById(R.id.ed_price_of_item);
                    EditText edDescrpt = (EditText) containerViewGroup.findViewById(R.id.ed_descrp_of_item);

                    String itemName = edName.getText().toString();
                    String itemPrice = edPrice.getText().toString();
                    String itemDescrp = edDescrpt.getText().toString();

                    if (itemName.length() > 2 && itemName != null && itemPrice != null && itemDescrp.length() > 2 && itemDescrp != null) {

                        //third - check  if Category have been selected
                        if (isCtgrWithNosubCtgrIsSelected == true) {

                            //fourth check-if addVariety is selected and their edittexts are not empty
                            if (checkIfTheAddVarietyOptionIsReadyToUpload()) {
                                //fifth check-of  visibilty ..but no matter its value things are gonna upload anyways ..but below if is for showing warning
                                if (stateOfVisibility == false) {
                                    showToast("Visiblity is turned off ,It wont appear in search,can be turned on later");
                                }

                                getAllDataFromAllFieldsAndSendToPresenter();

                            } else {
                                //this doesnt need to show toast here because the function in if statementwill do itself
                            }

                        } else {
                            showToast("Select a Category for the Item");
                        }

                    } else {
                        showToast("Any of the Field is empty OR text length is not much");
                    }
                } else {
                    showToast("Select atleast 1 item image");
                }
            }
        });
    }

    private boolean checkIfTheAddVarietyOptionIsReadyToUpload() {
        //  Button btAddOrRemoveVariety = (Button) containerViewGroup.findViewById(R.id.btAddVarity);
        LinearLayout llContainerFrVarities = (LinearLayout) containerViewGroup.findViewById(R.id.container_for_varieties);
        EditText edNameOfVariety = (EditText) containerViewGroup.findViewById(R.id.ed_name_of_variety);
        Button btAdd = (Button) containerViewGroup.findViewById(R.id.btAdd_indivisual_variety);

        if (isAddVarietyFeatureAlreadyEnabled == true) {
            if (edNameOfVariety.getText().toString().length() > 2 && edNameOfVariety.getText().toString() != null) {
                if (llContainerFrVarities.getChildCount() != 0) {
                    boolean everythingIsFine = true;
                    for (int i = 0; i < llContainerFrVarities.getChildCount(); i++) {
                        RelativeLayout container = (RelativeLayout) llContainerFrVarities.getChildAt(i);
                        EditText ed = (EditText) container.findViewById(R.id.ed_name);
                        if (ed.getText().toString().length() == 0) {
                            everythingIsFine = false;
                        }
                    }
                    if (everythingIsFine == true) {
                        return true;
                    } else {
                        showToast("one of the Variety option is empty");
                        return false;
                    }
                } else {
                    showToast("Add varieties");
                    return false;
                }
            } else {
                showToast("Variety name Field is empty or text count is low.");
                return false;
            }
        } else {
            return true;
        }

    }

    private void getAllDataFromAllFieldsAndSendToPresenter() {


        showProgressBar(true);

        //------------getting Images in a list
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        LinearLayout llContainerFrIvs = (LinearLayout) containerViewGroup.findViewById(R.id.ll_container_fr_ivs);
        for (int i = 0; i < llContainerFrIvs.getChildCount(); i++) {
            //  RelativeLayout inflatedIvRlContainer = (RelativeLayout) llContainerFrIvs.getChildAt(i);
            CardView cv = (CardView) llContainerFrIvs.getChildAt(i);
            ImageView iv = (ImageView) cv.findViewById(R.id.id_fr_slected_image);
            bitmaps.add(((BitmapDrawable) iv.getDrawable()).getBitmap());
        }
        //------------getting names from text fields
        EditText edName = (EditText) containerViewGroup.findViewById(R.id.ed_name_of_item);
        EditText edPrice = (EditText) containerViewGroup.findViewById(R.id.ed_price_of_item);
        EditText edDescrpt = (EditText) containerViewGroup.findViewById(R.id.ed_descrp_of_item);

        String itemName = edName.getText().toString();
        String itemPrice = edPrice.getText().toString();
        String itemDescrp = edDescrpt.getText().toString();
        //------------getting Category data
        String ctgr = mCategoryPath;
        String rootctgr = rootCtgr;
        String subctgr = subCtgr;
        String subsubctgr = subSubCtgr;
        //------------getting Variety optioins data
        EditText edNameOfVariety = (EditText) containerViewGroup.findViewById(R.id.ed_name_of_variety);
        ArrayList<String> varieties = getVarieties();
        String nameOfVariety = edNameOfVariety.getText().toString();
        //------------getting Visibility option
        boolean visible = stateOfVisibility;

        //=============================now pass all this data to presenter
        isItemUploading = true;
        mPresenter.makeItemObjectAndUpload(bitmaps, itemName, itemPrice, itemDescrp, ctgr, rootctgr, subctgr, subsubctgr, nameOfVariety, varieties, visible);
    }

    private ArrayList<String> getVarieties() {
        LinearLayout llContainerFrVarities = (LinearLayout) containerViewGroup.findViewById(R.id.container_for_varieties);
        ArrayList<String> varieties = new ArrayList<>();
        for (int i = 0; i < llContainerFrVarities.getChildCount(); i++) {
            RelativeLayout container = (RelativeLayout) llContainerFrVarities.getChildAt(i);
            EditText ed = (EditText) container.findViewById(R.id.ed_name);
            varieties.add(ed.getText().toString());
        }
        return varieties;
    }


    //-------------------------------------------------------------------

    public void closeFragment() {

        if (isItemUploading != true) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null).remove(ItemsDetailsTakingFragment.this).commit();
            Window w = getActivity().getWindow();
            if(StaticClassForGlobalInfo.theme==1) {
                w.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryLight));
                w.setStatusBarColor(getResources().getColor(R.color.colorPrimaryLight));
            }else{
                w.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
                w.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
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
    public void showProgressBar(boolean b) {


        View dim_background_of_bottom_sheet = (View) containerViewGroup.findViewById(R.id.touch_to_dismiss_bottom_sheet_dim_background);
        dim_background_of_bottom_sheet.setVisibility(View.VISIBLE);
        dim_background_of_bottom_sheet.setClickable(true);

        ImageView iv_loading_gif = (ImageView) containerViewGroup.findViewById(R.id.iv_loading_gif);
        CardView container_iv_loading_gif = (CardView) containerViewGroup.findViewById(R.id.cv_container_loaading_iv);
        container_iv_loading_gif.setVisibility(View.VISIBLE);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.shopping_loader).into(iv_loading_gif);

        /*    Picasso.get()
                .load(R.drawable.shopping_loader).into(iv_loading_gif);*/
    }

    @Override
    public void ShowSnackBarWithAction(String msg, String actionName) {

    }

    @Override
    public void showToast(String msg) {
        Utils.showCustomToastForFragments(msg, getContext());
    }

}
