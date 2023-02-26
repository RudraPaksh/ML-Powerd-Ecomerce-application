package io.shubh.e_comm_ver1.PaymentFragments.View;

import android.content.Context;

public interface PaymentView {

    void switchActivity(int i);

    Context getContext(boolean getActvityContext);

    void showProgressBar(boolean b);


    void ShowSnackBarWithAction(String msg, String actionName);

    void showToast(String msg);

    void showSuccessScreen(boolean b);

    void updateProgressTv(int i);
}
