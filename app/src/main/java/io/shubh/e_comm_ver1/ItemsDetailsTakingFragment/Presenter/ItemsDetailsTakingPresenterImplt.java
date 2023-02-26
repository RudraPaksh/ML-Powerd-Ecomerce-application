package io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.Presenter;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;


import io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.Interactor.ItemsDetailsTakingInteractor;
import io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.View.ItemsDetailsTakingView;
import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;

public class ItemsDetailsTakingPresenterImplt implements ItemsDetailsTakingPresenter, ItemsDetailsTakingInteractor.CallbacksToPresnter {

    private ItemsDetailsTakingView mView;
    private ItemsDetailsTakingInteractor mInteractor;

    ArrayList<Bitmap> imagesBitmap = new ArrayList<Bitmap>();
    String TAG = "!!!!!";

    public ItemsDetailsTakingPresenterImplt(ItemsDetailsTakingView splashview, ItemsDetailsTakingInteractor mSplashInteractor) {
        this.mView = splashview;
        this.mInteractor = mSplashInteractor;


        mInteractor.init(this);
    }

    @Override
    public void onSingleImageSelectedOrCaptured(Bitmap singleImageBitmap) {
        mView.makeNewImageViewAndSetImageToIt(singleImageBitmap);
        mView.decrementAllowedImagesPickUpAmount(1);
    }


    @Override
    public void onMultipleImagesSelectedFromGallery(List<Bitmap> multipleImageBitmaps) {

        for (int i = 0; i < multipleImageBitmaps.size(); i++) {
            mView.makeNewImageViewAndSetImageToIt(multipleImageBitmaps.get(i));
            mView.decrementAllowedImagesPickUpAmount(1);

        }
    }

    @Override
    public void removeIvAndItsDataAtThisIndex(int index) {

        mView.removeIvAtThisIndex(index);
        mView.incrementAllowedImagesPickUpAmount(1);

    }

    @Override
    public void onEditBtOfAnyImageViewIsClickedAndBitmapHasReturned(Bitmap singleImageBitmap, int indexOfIvOfWhichEditBtWasClicked) {
        mView.replaceBitmapOnThisPosition(indexOfIvOfWhichEditBtWasClicked, singleImageBitmap);
    }

    @Override
    public void makeItemObjectAndUpload(ArrayList<Bitmap> bitmaps, String itemName, String itemPrice, String itemDescrp, String ctgr, String rootctgr, String subctgr, String subsubctgr, String nameOfVariety, ArrayList<String> varieties, boolean visible) {

        //=========First -Get The Id os the last Item uploaded
        mInteractor.getLastItemUploadedItemNo(new ItemsDetailsTakingInteractor.SeparateSecondCallbackToPresnterForSystemUpdate() {
            @Override
            public void onFinishedGettingTheIdNo(String idOfLastItemUploaded) {

                String idForThisItem = String.valueOf(Integer.valueOf(idOfLastItemUploaded) + 1);

                //=========second -save images online and get the urls..below is recuresive function so give it a starting point that is 1
                //also i am passing empty string list ,which it will fill it with download url and return
                ArrayList<String> dwnldImageUrls = new ArrayList<>();
                mInteractor.uploadImagesInStorageWithArgAsCallbackFunction(1, idForThisItem, dwnldImageUrls, bitmaps, new ItemsDetailsTakingInteractor.SeparateCallbackToPresnterForSystemUpdate() {
                    @Override
                    public void onFinishedUploadingImages(ArrayList<String> callbackResultUrlList) {

                        //=========third  -Now make the item object and give it to interactor to upload
                        ItemsForSale item = new ItemsForSale();
                        item.setName(itemName);
                        item.setItem_price(itemPrice);
                        item.setDescription(itemDescrp);
                        item.setListOfImageURLs(dwnldImageUrls);
                        item.setCategory(ctgr);
                        item.setRoot_category(rootctgr);
                        item.setSub_category(subctgr);
                        item.setSub_sub_category(subsubctgr);
                        item.setItem_id(Integer.parseInt(idForThisItem));
                        item.setSeller_id(StaticClassForGlobalInfo.UId);
                        item.setTime_of_upload(System.currentTimeMillis() / 1000L);
                        item.setArrayOfNameKeywords(getArrayOfNameKeywords(itemName ,item));
                        item.setVisibility(visible);
                        if (varieties.size() != 0) {
                            item.setVarietyName(nameOfVariety);
                        } else {
                            item.setVarietyName("null");
                        }
                        item.setVarieies(varieties);

                        mInteractor.uploadItemFunctionWithArgAsCallbackFunction(item, new ItemsDetailsTakingInteractor.SeparateThirdCallbackToPresnterAfterItemUploaded() {
                            @Override
                            public void onFinishedUploadingItem() {

                                //================forth just Update the last oitem uploaded id to current one
                                mInteractor.updateLastItemUploadedIdVar(idForThisItem, new ItemsDetailsTakingInteractor.SeparateForthCallbackToPresnterAfterIIdUpdation() {
                                    @Override
                                    public void onFinishedUpdatingId() {
                                  
                                        mView.onFinishedUploadingItem();
                                    }
                                });

                            }
                        });

                    }



                });


            }
        });


    }

    private List<String> getArrayOfNameKeywords(String itemName, ItemsForSale item) {


        String[] words = itemName.split("\\s+");
        List<String> wordsList = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i].replaceAll("[^\\w]", "");//removes any puctuation like ?,!

            wordsList.add(words[i].toLowerCase());
        }

        //also adding the categories
        wordsList.add(item.getSub_category().toLowerCase());
        wordsList.add(item.getRoot_category().toLowerCase());
        wordsList.add(item.getSub_sub_category().toLowerCase());

        return wordsList;
    }
    //--------------------------------------------------------------------------
    @Override
    public void LoginRelatedWork() {


       /* mInteractor.checkSomethingInDatabaseWithArgAsCallbackFunction(new ItemsDetailsTakingInteractor.SeparateCallbackToPresnterForSystemUpdate() {
            @Override
            public void onFinishedCheckingSystemUpdate(boolean callbackResultOfTheCheck) {

                if (callbackResultOfTheCheck == true) {
                    //system upadte available ..so throw a dialog asking to download update
                } else {
                    //system upadte not available ..so continue
                }
            }
        });*/
    }


    @Override
    public void onFinishedCheckingSomething1() {
//this is call from interactor
    }

    @Override
    public void onFinishedCheckingSomething2() {

        //this is call from interactor
    }
}
