package io.shubh.e_comm_ver1.ItemsDetailsTakingFragment.View;

import android.content.Context;
import android.graphics.Bitmap;

public interface ItemsDetailsTakingView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void decrementAllowedImagesPickUpAmount(int decrmntThisAmount);

    void makeNewImageViewAndSetImageToIt(Bitmap imgBitmap );

    void removeIvAtThisIndex(int index);

    void incrementAllowedImagesPickUpAmount(int i);

    void replaceBitmapOnThisPosition(int indexOfIvOfWhichEditBtWasClicked, Bitmap singleImageBitmap);

    void onFinishedUploadingItem();
}
