package io.shubh.e_comm_ver1.OrderListFrSellerFragment.Presenter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import io.shubh.e_comm_ver1.OrderListFrSellerFragment.Interactor.NewOrderListFrSellerInteractor;
import io.shubh.e_comm_ver1.OrderListFrSellerFragment.View.NewOrderListFrSellerView;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.Splash.View.SplashActivity;

public class NewOrderListFrSellerPresenterImplt implements NewOrderListFrSellerPresenter, NewOrderListFrSellerInteractor.CallbacksToPresnter {

    private NewOrderListFrSellerView mView;
    private NewOrderListFrSellerInteractor mInteractor;
    private Context context;



    public NewOrderListFrSellerPresenterImplt(NewOrderListFrSellerView mView , NewOrderListFrSellerInteractor mSplashInteractor ,Context context) {
       this.mView=mView;
       this.mInteractor = mSplashInteractor;
       this.context = context;


       mInteractor.init(this);



    }

    @Override
    public void changeStatusOfOrderAndSendNotification(Order.SubOrderItem subOrderItem, long l) {

        if (subOrderItem.getStatusOfOrder() == 2) {

            subOrderItem.setTimeOfPackagedOfItem(l);
            mInteractor.UpdateStatusInDatabaseWithArgAsCallbackFunction( subOrderItem , 3 ,new NewOrderListFrSellerInteractor.SeparateCallbackToPresnterAfterOrderStatusUpdate() {
              //  @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onFinished(boolean callbackResultOfTheCheck, int nextStatusNoToUpdate) {

                    sendNotifToShowTheMsg();
                    mView.OnStatusUpdateDOne(nextStatusNoToUpdate);
                }
            });
        } else if (subOrderItem.getStatusOfOrder() == 3) {

            subOrderItem.setTimeOfShippedOfItem(l);
            mInteractor.UpdateStatusInDatabaseWithArgAsCallbackFunction( subOrderItem , 4 ,new NewOrderListFrSellerInteractor.SeparateCallbackToPresnterAfterOrderStatusUpdate() {
             //   @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onFinished(boolean callbackResultOfTheCheck, int nextStatusNoToUpdate) {
                    sendNotifToShowTheMsg();
                    mView.OnStatusUpdateDOne(nextStatusNoToUpdate);
                }
            });

        } else if (subOrderItem.getStatusOfOrder() == 4) {

            subOrderItem.setTimeOfDeliveryOfItem(l);
            mInteractor.UpdateStatusInDatabaseWithArgAsCallbackFunction( subOrderItem , 5 ,new NewOrderListFrSellerInteractor.SeparateCallbackToPresnterAfterOrderStatusUpdate() {
           //     @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onFinished(boolean callbackResultOfTheCheck, int nextStatusNoToUpdate) {
                    sendNotifToShowTheMsg();
                    mView.OnStatusUpdateDOne(nextStatusNoToUpdate);
                }
            });

        }
    }

   // @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendNotifToShowTheMsg() {
        int NotificationID = 2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Order Status Change Successful";
            String description = "Buyer has been notified of the Status change of the order";
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //below intent for when click on notification
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
       /* Bundle extras = new Bundle();
        extras.putString("type", "2");
        notificationIntent.putExtras(extras);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(context, NotificationID, notificationIntent, 0);

        Intent cancel = new Intent("io.shubh.e_comm_ver1");
        PendingIntent cancelP = PendingIntent.getBroadcast(context, NotificationID, cancel, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Action Viewaction = new NotificationCompat.Action(android.R.drawable.ic_menu_view, "VIEW", pendingIntent);
        NotificationCompat.Action Dissmissaction = new NotificationCompat.Action(android.R.drawable.ic_delete, "DISSMISS", cancelP);

//todo- dissmiss bt aint working ..make it work using this link https://stackoverflow.com/questions/19739371/dismiss-ongoing-android-notification-via-action-button-without-opening-app

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.shopping_bag_app_icon)
              //  .setLargeIcon(bitmap)
                .setContentTitle("Order Status Change Successful")
                .setContentText("Buyer has been notified of the Status change of the order")
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(Viewaction)
                .addAction(Dissmissaction);





//    mBuilder.setContentIntent(launchIntent);


        Notification buildNotification = mBuilder.build();
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(002, buildNotification);

    }


}
