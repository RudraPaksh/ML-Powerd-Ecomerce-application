package io.shubh.e_comm_ver1.Main.View;

import android.content.Context;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.Models.ClassForMainActvityItemReclrDATAObject;

public interface MainView {

    void switchActivity(int i );

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void setReclrViewToshowCtgrs(ArrayList<ClassForMainActvityItemReclrDATAObject> listOfDataObjectsForAdapter);

    void showProgressBarOfDrwrBtSwitchToSeller(boolean b);

     void loadtheUI();
}
