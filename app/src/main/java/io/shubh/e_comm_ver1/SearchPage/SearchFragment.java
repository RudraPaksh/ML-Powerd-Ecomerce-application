package io.shubh.e_comm_ver1.SearchPage;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.ml.common.FirebaseMLException;


import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseModelOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.google.firebase.ml.custom.model.FirebaseCloudModelSource;
import com.google.firebase.ml.custom.model.FirebaseLocalModelSource;
import com.google.firebase.ml.custom.model.FirebaseModelDownloadConditions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import com.google.firebase.ml.vision.label.FirebaseVisionLabel;

import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import io.shubh.e_comm_ver1.Adapters.CustomPagerAdapterForSearchFragment;
import io.shubh.e_comm_ver1.SearchPage.View.SearchResultsFragment;
import io.shubh.e_comm_ver1.Utils.Utils;
import io.shubh.e_comm_ver1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    String TAG = "SearchFragment";
    View containerViewGroup;
    LayoutInflater inflater;
    int pageNoForMlFeature;
    private Bitmap mSelectedImage;
    BottomSheetBehavior behaviorBotttomSheet;

    //this below array gets values inside gettoplabel code
    List<Float> probsForLabelsFromtenserflowModel = new ArrayList<>();
    //====================================================

    private FirebaseModelInterpreter mInterpreter;

    // Name of the model file hosted with Firebase.
    private static final String HOSTED_MODEL_NAME = "cloud_model_1";
    private static final String LOCAL_MODEL_ASSET = "mobilenet_v1_1.0_224_quant.tflite";

    // Number of results to show in the UI.
    private static final int RESULTS_TO_SHOW = 3;

    //Dimensions of inputs.
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;

    // Preallocated buffers for storing image data. *//*
    private final int[] intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

    // Data configuration of input & output data of model.
    private FirebaseModelInputOutputOptions mDataOptions;
    // Labels corresponding to the output of the vision model.
    private List<String> mLabelList;
    // Name of the label file stored in Assets.
    private static final String LABEL_PATH = "labels.txt";

    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float>
                                o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerViewGroup = inflater.inflate(R.layout.fragment_search, container, false);
        this.inflater = inflater;

        /*mPresenter = new NotificationPresenterImplt(this, new NotificationInteractorImplt() {
        });*/


        DoUiWork();


        // Inflate the layout for this fragment
        return containerViewGroup;
    }

    private void DoUiWork() {

        //initializations here
        SearchViewInit();
        doPagerWork();
        initViews();
        setupBottomSheet();
        initCustomModel();
        //initCustomModel();

        //---setups here
        //attachOnBackBtPressedlistener();


        //  setUpToolbar();
    }

    private void setupBottomSheet() {


        CoordinatorLayout rootView = (CoordinatorLayout) containerViewGroup.findViewById(R.id.cl_root);
        View inflatedBottomSheetdialog = inflater.inflate(R.layout.bottom_sheet_fr_ml_object_detect_results_fr_search_frag, rootView, false);
        rootView.addView(inflatedBottomSheetdialog);

        behaviorBotttomSheet = BottomSheetBehavior.from(inflatedBottomSheetdialog);

        behaviorBotttomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

        View dim_background_of_bottom_sheet = (View) containerViewGroup.findViewById(R.id.touch_to_dismiss_bottom_sheet_dim_background);
        dim_background_of_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                behaviorBotttomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        behaviorBotttomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
    }

    private void SearchViewInit() {
        SearchView searchView = (SearchView) containerViewGroup.findViewById(R.id.searchview);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();

                SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                searchResultsFragment.setLocalVariables(getListOfNameKeywordsFromSentence(query));
                searchResultsFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                searchResultsFragment.setExitTransition(new Slide(Gravity.RIGHT));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.drawerLayout, searchResultsFragment, "SearchResultsFragment")
                        .addToBackStack(null)
                        .commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }


        });
//----------------------------------------------------------------
/*
        CustomPagerAdapterForSearchFragment adapter = new CustomPagerAdapterForSearchFragment();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);*/


    }


    private void doPagerWork() {


        ViewPager viewPager = (ViewPager) containerViewGroup.findViewById(R.id.pager3);
        CustomPagerAdapterForSearchFragment adapter = new CustomPagerAdapterForSearchFragment();
        viewPager.setAdapter(adapter);


        TabLayout tabLayout = (TabLayout) containerViewGroup.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        //rlVpContainer = (RelativeLayout) containerViewGroup.findViewById(R.id.rl_viewpager_container);


    }

    private void initViews() {

        LinearLayout bt_cmaera = (LinearLayout) containerViewGroup.findViewById(R.id.bt_camera1);
        LinearLayout bt_gallery = (LinearLayout) containerViewGroup.findViewById(R.id.bt_gallery1);


        bt_cmaera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checking if we have camera permission
                if (TedPermission.isGranted(getContext(), Manifest.permission.CAMERA)) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 0);

                    pageNoForMlFeature = 1;


                } else {
                    //call for getting permission
                    getCameraPermissions();
                }
            }
        });

        bt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickIntent.setType("image/* video/*");
                pickIntent.setType("image/*");

                startActivityForResult(pickIntent, 1);

                pageNoForMlFeature = 1;
            }
        });

        //======================================================================================================
        //page 2 views initialisation
        LinearLayout bt_cmaera2 = (LinearLayout) containerViewGroup.findViewById(R.id.bt_camera22);
        LinearLayout bt_gallery2 = (LinearLayout) containerViewGroup.findViewById(R.id.bt_gallery22);


        bt_cmaera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);

                pageNoForMlFeature = 2;
            }
        });

        bt_gallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickIntent.setType("image/* video/*");
                pickIntent.setType("image/*");

                startActivityForResult(pickIntent, 3);

                pageNoForMlFeature = 2;
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
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {


                    Bitmap bitmap = null;
                    try {
                        bitmap = (Bitmap) data.getExtras().get("data");

                        mSelectedImage = bitmap;

                        //   runModelInference();
                        runImageLabeling(bitmap);


                        //      imageView.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        // showToast("Failed to load");
                        Log.e("Camera", e.toString());
                    }
                }
            case 1:
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap bitmap = null;
                    try {
                        Uri imageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

                        mSelectedImage = bitmap;

                        //  runModelInference();
                        runImageLabeling(bitmap);
                        //     imageView.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        //  showToast("Failed to load");
                        Log.e("Camera", e.toString());
                    }
                }

                //--==================================================

                //Page 2 views initialisation
            case 2:
                if (resultCode == Activity.RESULT_OK) {


                    Bitmap bitmap = null;
                    try {
                        bitmap = (Bitmap) data.getExtras().get("data");

                        mSelectedImage = bitmap;

                        runImgTextDetection(bitmap);

                        //      imageView.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        //  showToast("Failed to load");
                        Log.e("Camera", e.toString());
                    }
                }
            case 3:
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap bitmap = null;
                    try {
                        Uri imageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

                        mSelectedImage = bitmap;

                        runImgTextDetection(bitmap);

                        //     imageView.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        //  showToast("Failed to load");
                        Log.e("Camera", e.toString());
                    }
                }
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

            wordsList.add(words[i].toLowerCase());
        }


        return wordsList;
    }


    public void closeFragment() {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null).remove(SearchFragment.this).commit();


    }

    private void showToast(String message) {
        Utils.showCustomToastForFragments(message,getContext());
    }

    private void runImgTextDetection(Bitmap bitmap) {



        //the below if is because both the function of mlkit get called at the same time no matter called for what
        if (pageNoForMlFeature == 2) {



            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextRecognizer textRecognizer =
                    FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {

                    SearchResultsFragment searchResultsFragment= new SearchResultsFragment();
                    searchResultsFragment.setLocalVariables(getListOfNameKeywordsFromSentence(firebaseVisionText.getText()));
                    searchResultsFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                    searchResultsFragment.setExitTransition(new Slide(Gravity.RIGHT));
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.drawerLayout, searchResultsFragment,"SearchResultsFragment" )
                            .addToBackStack(null)
                            .commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast("Some prob occured in identifying the text");
                }
            });
        }

    }

    private void runImageLabeling(Bitmap bitmap) {

        if (pageNoForMlFeature == 1) {
         /*   TensorflowMlkitObjectDetection tensorflowMlkitObjectDetection = new TensorflowMlkitObjectDetection((AppCompatActivity) getContext(), (InterfaceForCallbackOnGettingLabelsAfterObjectDetection) getContext());
            tensorflowMlkitObjectDetection.runModelInference(bitmap);*/

            FirebaseVisionLabelDetectorOptions options = new FirebaseVisionLabelDetectorOptions.Builder()

//Set the confidence threshold//

                    .setConfidenceThreshold(0.7f)
                    .build();

//Create a FirebaseVisionImage object//

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

//Create an instance of FirebaseVisionLabelDetector//

            FirebaseVisionLabelDetector detector =
                    FirebaseVision.getInstance().getVisionLabelDetector(options);

            detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionLabel>>() {
                @Override

//Implement the onSuccess callback//

                public void onSuccess(List<FirebaseVisionLabel> labels) {

                    showTheResultsInBottomSheet(labels, new ArrayList<String>());


                    runModelInference();
                    /* for (FirebaseVisionLabel label : labels) {

//Display the label and confidence score in our TextView//

                    Log.i("!!!!",  label.getLabel() + "\n");
                    Log.i("!!!!",  label.getConfidence() + "\n\n");
                }*/
                }

//Register an OnFailureListener//

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //      mTextView.setText(e.getMessage());
                    Log.i("!!!!", "error");
                }
            });


//keeping these below statements before the above ml relted code ..hinders the ml code getting executed
            behaviorBotttomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
    }

    //=========================================================

    private void initCustomModel() {
        mLabelList = loadLabelList();

        int[] inputDims = {DIM_BATCH_SIZE, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y, DIM_PIXEL_SIZE};
        int[] outputDims = {DIM_BATCH_SIZE, mLabelList.size()};
        try {
            mDataOptions =
                    new FirebaseModelInputOutputOptions.Builder()
                            .setInputFormat(0, FirebaseModelDataType.BYTE, inputDims)
                            .setOutputFormat(0, FirebaseModelDataType.BYTE, outputDims)
                            .build();
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions
                    .Builder()
                    .requireWifi()
                    .build();
            FirebaseCloudModelSource cloudSource = new FirebaseCloudModelSource.Builder
                    (HOSTED_MODEL_NAME)
                    .enableModelUpdates(true)
                    .setInitialDownloadConditions(conditions)
                    .setUpdatesDownloadConditions(conditions)  // You could also specify
                    // different conditions
                    // for updates
                    .build();
            FirebaseLocalModelSource localSource =
                    new FirebaseLocalModelSource.Builder("asset")
                            .setAssetFilePath(LOCAL_MODEL_ASSET).build();
            FirebaseModelManager manager = FirebaseModelManager.getInstance();
            manager.registerCloudModelSource(cloudSource);
            manager.registerLocalModelSource(localSource);
            FirebaseModelOptions modelOptions =
                    new FirebaseModelOptions.Builder()
                            .setCloudModelName(HOSTED_MODEL_NAME)
                            .setLocalModelName("asset")
                            .build();
            mInterpreter = FirebaseModelInterpreter.getInstance(modelOptions);
        } catch (FirebaseMLException e) {
            showToast("Error while setting up the model");
            e.printStackTrace();
        }
    }

    //Reads label list from Assets.
    private List<String> loadLabelList() {
        List<String> labelList = new ArrayList<>();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(getActivity().getAssets().open
                             (LABEL_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                labelList.add(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to read label list.", e);
        }
        return labelList;
    }


    private void runModelInference() {
        if (mInterpreter == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.");
            return;
        }
        // Create input data.
        ByteBuffer imgData = convertBitmapToByteBuffer(mSelectedImage, mSelectedImage.getWidth(),
                mSelectedImage.getHeight());

        try {
            FirebaseModelInputs inputs = new FirebaseModelInputs.Builder().add(imgData).build();
            // Here's where the magic happens!!
            mInterpreter
                    .run(inputs, mDataOptions)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, e.getMessage());
                            showToast("Error running model inference");
                        }
                    })
                    .continueWith(
                            new Continuation<FirebaseModelOutputs, List<String>>() {
                                @Override
                                public List<String> then(Task<FirebaseModelOutputs> task) {


                                    byte[][] labelProbArray = task.getResult()
                                            .<byte[][]>getOutput(0);
                                    List<String> topLabels = getTopLabels(labelProbArray);

                                    List<String> names = extractTheNamesWithoutProbabilityScoreFromTheList(topLabels);
                                   showTheResultsInBottomSheet(null ,names);

                                    return topLabels;

                                }
                            });
        } catch (FirebaseMLException e) {
            e.printStackTrace();
            showToast("Error running model inference");
        }

    }

    private List<String>  extractTheNamesWithoutProbabilityScoreFromTheList(List<String> labels) {

       // probs.clear();
        List<String> names = new ArrayList<>();

        for (int i = 0; i < labels.size(); i++) {
            Log.i("!!!", "label "+labels.get(i));
            int iend = labels.get(i).indexOf(":"); //this finds the first occurrence of ":"

            names.add(labels.get(i).substring(0, iend));
          //  probs.add(labels.get(i).substring( iend ,3));
            Log.i("!!!", labels.get(i).substring(0, iend));

        }
        return names;

    }


    //Gets the top labels in the results

    private synchronized List<String> getTopLabels(byte[][] labelProbArray) {

        probsForLabelsFromtenserflowModel.clear();

        for (int i = 0; i < mLabelList.size(); ++i) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<>(mLabelList.get(i), (labelProbArray[0][i] &
                            0xff) / 255.0f));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        List<String> result = new ArrayList<>();
        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            probsForLabelsFromtenserflowModel.add(label.getValue());
            result.add(label.getKey() + ":" + label.getValue());
        }
        Log.d(TAG, "labels: " + result.toString());

        return result;
    }

    //Writes Image data into a {@code ByteBuffer}.
    private synchronized ByteBuffer convertBitmapToByteBuffer(
            Bitmap bitmap, int width, int height) {
        ByteBuffer imgData =
                ByteBuffer.allocateDirect(
                        DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y,
                true);
        imgData.rewind();
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.getWidth(), 0, 0,
                scaledBitmap.getWidth(), scaledBitmap.getHeight());
        // Convert the image to int points.
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                imgData.put((byte) ((val >> 16) & 0xFF));
                imgData.put((byte) ((val >> 8) & 0xFF));
                imgData.put((byte) (val & 0xFF));
            }
        }
        return imgData;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();


        InputStream is;
        Bitmap bitmap = null;
        try {
            is = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void showTheResultsInBottomSheet(List<FirebaseVisionLabel> firebaseVisionLabels, List<String> names) {

        if (firebaseVisionLabels != null) {
            LinearLayout containerForDetectedItemsThroughMl = containerViewGroup.findViewById(R.id.containerForDetectedItemsThroughMl);
            containerForDetectedItemsThroughMl.removeAllViews();

            if (firebaseVisionLabels.size() == 0) {
                showToast("No Object Identified from firebase image labeling mlkit");
            }

            for (FirebaseVisionLabel label : firebaseVisionLabels) {
                View row = inflater.inflate(R.layout.infalte_rows_fr_ml_results_search_frag, containerForDetectedItemsThroughMl, false);
                containerForDetectedItemsThroughMl.addView(row);

                TextView tvItemName = (TextView) row.findViewById(R.id.tvItemName);
                TextView tvItemProbability = (TextView) row.findViewById(R.id.tvItemProbability);

                tvItemName.setText(label.getLabel());
                tvItemProbability.setText(String.valueOf(label.getConfidence() * 100.0 + "%"));

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                        searchResultsFragment.setLocalVariables(getListOfNameKeywordsFromSentence(label.getLabel()));
                        searchResultsFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                        searchResultsFragment.setExitTransition(new Slide(Gravity.RIGHT));
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.drawerLayout, searchResultsFragment, "SearchResultsFragment")
                                .addToBackStack(null)
                                .commit();


                    }
                });

            }
        }else  if (names != null) {
            //call is from tenserflow lite model
            LinearLayout containerForDetectedItemsThroughTenserflow = containerViewGroup.findViewById(R.id.containerForDetectedItemsThroughTenserflow);
            containerForDetectedItemsThroughTenserflow.removeAllViews();

            if (names.size() == 0) {
                showToast("No Object Identified from firebase image labeling mlkit");
            }

            for (int i=0 ;i<names.size() ;i++) {
                View row = inflater.inflate(R.layout.infalte_rows_fr_ml_results_search_frag, containerForDetectedItemsThroughTenserflow, false);
                containerForDetectedItemsThroughTenserflow.addView(row);

                TextView tvItemName = (TextView) row.findViewById(R.id.tvItemName);
                TextView tvItemProbability = (TextView) row.findViewById(R.id.tvItemProbability);
               // tvItemProbability.setVisibility(View.GONE);



                tvItemName.setText(names.get(i));
                tvItemProbability.setText(probsForLabelsFromtenserflowModel.get(i)*100.0+"%");


                int finalI = i;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                        searchResultsFragment.setLocalVariables(getListOfNameKeywordsFromSentence(names.get(finalI)));
                        searchResultsFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                        searchResultsFragment.setExitTransition(new Slide(Gravity.RIGHT));
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.drawerLayout, searchResultsFragment, "SearchResultsFragment")
                                .addToBackStack(null)
                                .commit();


                    }
                });

            }

        }
    }


}


