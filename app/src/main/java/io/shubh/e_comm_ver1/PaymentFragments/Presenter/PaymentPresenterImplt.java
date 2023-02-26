package io.shubh.e_comm_ver1.PaymentFragments.Presenter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.ArrayList;

import io.shubh.e_comm_ver1.PaymentFragments.Interactor.PaymentInteractor;
import io.shubh.e_comm_ver1.PaymentFragments.View.PaymentView;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;
import io.shubh.e_comm_ver1.Models.Order;
import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.Splash.View.SplashActivity;

public class PaymentPresenterImplt implements PaymentPresenter, PaymentInteractor.CallbacksToPresnter {

    private PaymentView mView;
    private PaymentInteractor mInteractor;
    private Context context;
    String TAG = "####";
    int totalPaymentAmount;

    public PaymentPresenterImplt(PaymentView mView, PaymentInteractor mSplashInteractor,Context context) {
        this.mView = mView;
        this.mInteractor = mSplashInteractor;
        this.context = context;

        mInteractor.init(this);


    }


    @Override
    public void startPayment(int totalPaymentAmount, Context context) {
        this.totalPaymentAmount =totalPaymentAmount;
/**
 * You need to pass current activity in order to let Razorpay create CheckoutActivity
 */
        //final Fragment fragment = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "ML Powered E commerce Application");
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            //TODO - use your app icon here
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = String.valueOf(totalPaymentAmount);
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            //TODO- if login is by using phone number then ...give it below
            //   preFill.put("contact", "9958584589");
            preFill.put("email", StaticClassForGlobalInfo.UserEmail);

            options.put("prefill", preFill);
            co.open((Activity) context, options);
        } catch (Exception e) {
            mView.showToast("Error in payment: " + e.getMessage());
            e.printStackTrace();
            Log.e(TAG, "startPayment: " + e.getMessage());
        }
    }

    @Override
    public void onSucessOfPayment(Order order, String s) {

        order.setStatusOfOrder(2);
        order.setBuyerId(StaticClassForGlobalInfo.UId);
        order.setTimeOfPaymentSuccessOfOrder(System.currentTimeMillis() / 1000L);
        order.setTransactionId(s);
        order.setTotalPrice(String.valueOf(totalPaymentAmount));
        //setting seller in order attribute of order object
        ArrayList<String> sellersInOrder = new ArrayList<>();
        for(int i=0;i<order.getBagItems().size();i++){
            sellersInOrder.add(order.getBagItems().get(i).getItemObject().getSeller_id());
        }
        order.setSellersInOrder(sellersInOrder);

        //   mView.showSuccessScreen(true);
        mView.showProgressBar(true);
        //  Log.i(TAG, "onSucessOfPayment: value of string:" + s);

        mInteractor.getLastOrderIdWithArgAsCallbackFunction(new PaymentInteractor.SeparateCallbackToPresnterAfterGettingLastOrderId() {
            @Override
            public void onFinished(boolean callbackResultOfTheCheck, String orderId) {

                if (callbackResultOfTheCheck == true) {
                    mView.updateProgressTv(10);
                    orderId = String.valueOf(Integer.valueOf(orderId) + 1);
                    order.setOrderId(orderId);

                    String finalOrderId = orderId;
                    mInteractor.uploadOrderInDatabaseWithArgAsCallbackFunction(order, new PaymentInteractor.SeparateCallbackToPresnterAfterOrderUpload() {
                        @Override
                        public void onFinished(boolean callbackResultOfTheCheck) {
                            if (callbackResultOfTheCheck == true) {
                                mView.updateProgressTv(40);
                                //making subOrder objects from bag items
                                ArrayList<Order.SubOrderItem> subOrderItems = new ArrayList<>();
                                for (int i = 0; i < order.getBagItems().size(); i++) {

                                    Order.SubOrderItem subOrderItem = new Order.SubOrderItem();
                                    subOrderItem.setItemAmount(order.getBagItems().get(i).getItemAmount());
                                    subOrderItem.setItemId(order.getBagItems().get(i).getItemId());
                                    if (order.getBagItems().get(i).getItemObject().getVarieies().size() != 0) {
                                        subOrderItem.setVarietyName(order.getBagItems().get(i).getItemObject().getVarietyName());
                                        subOrderItem.setSelectedVarietyName(order.getBagItems().get(i).getItemObject().getVarieies().get(Integer.valueOf(order.getBagItems().get(i).getSelectedVarietyIndexInList())));
                                    }
                                    subOrderItem.setStatusOfOrder(2);
                                    subOrderItem.setImageUrl(order.getBagItems().get(i).getItemObject().getListOfImageURLs().get(0));
                                    subOrderItem.setSellerId(order.getBagItems().get(i).getItemObject().getSeller_id());
                                    subOrderItem.setItemPrice(order.getBagItems().get(i).getItemObject().getItem_price());
                                    subOrderItem.setBuyerId(StaticClassForGlobalInfo.UId);
                                    subOrderItem.setItemName(order.getBagItems().get(i).getItemObject().getName());


                                    subOrderItems.add(subOrderItem);
                                }

                                mInteractor.setSubItemsInsubCollectionWithArgAsCallbackFunction(0, order, subOrderItems, new PaymentInteractor.SeparateCallbackToPresnterAfterUploadingSubCollections() {
                                    @Override
                                    public void onFinished(boolean callbackResultOfTheCheck) {
                                        if (callbackResultOfTheCheck == true) {
                                            mView.updateProgressTv(70);
                                            mInteractor.updateLastOrderIdWithArgAsCallbackFunction(finalOrderId, new PaymentInteractor.SeparateCallbackToPresnterAfterUpdatingLastOrdreId() {
                                                @Override
                                                public void onFinished(boolean callbackResultOfTheCheck) {
                                                    if (callbackResultOfTheCheck == true) {
                                                        mView.updateProgressTv(90);
                                                        mInteractor.deleteAllBagItemsWithArgAsCallbackFunction(order, new PaymentInteractor.SeparateCallbackToPresnterAfterDeletingBagItems() {
                                                            @Override
                                                            public void onFinished(boolean callbackResultOfTheCheck) {

                                                                if (callbackResultOfTheCheck == true) {

                                                                    //since order plced successfully I will just send an notif to seller.
                                                                    //I aint not using a on finished callback ..because it is not required and it will happen in background tho in thread
                                                                    mInteractor.sendNotifToSellerWithArgAsCallbackFunction(order, new PaymentInteractor.SeparateCallbackToPresnterAfterSendNotifToSeller() {
                                                                    //    @RequiresApi(api = Build.VERSION_CODES.M)
                                                                        @Override
                                                                        public void onFinished(boolean callbackResultOfTheCheck) {

                                                                          if(callbackResultOfTheCheck==true) {
                                                                              mView.showProgressBar(false);
                                                                              sendNotifToShowTheMsg();
                                                                              mView.showSuccessScreen(true);
                                                                          }else {
                                                                              mView.showToast("Some problem Occured");
                                                                              mView.switchActivity(1);
                                                                          }
                                                                        }
                                                                    });


                                                                } else {
                                                                    mView.showToast("Some problem Occured");
                                                                    mView.switchActivity(1);
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        mView.showToast("Some problem Occured");
                                                        mView.switchActivity(1);
                                                    }
                                                }
                                            });
                                        } else {
                                            mView.showToast("Some problem Occured");
                                            mView.switchActivity(1);
                                        }
                                    }
                                });
                            } else {
                                mView.showToast("Some problem Occured");
                                mView.switchActivity(1);
                            }
                        }
                    });


                } else {
                    mView.showToast("Some problem Occured");
                    mView.switchActivity(1);
                }
            }
        });
    }


    //@RequiresApi(api = Build.VERSION_CODES.M)
    private void sendNotifToShowTheMsg() {
        int NotificationID = 2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Purchase Successful";
            String description = "Seller has been notified of the order";
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
        /*Bundle extras = new Bundle();
        extras.putString("type", "3");
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
                .setContentTitle("Purchase Successful")
                .setContentText("Seller has been notified of the order")
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(Viewaction)
                .addAction(Dissmissaction);





//    mBuilder.setContentIntent(launchIntent);


        Notification buildNotification = mBuilder.build();
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(002, buildNotification);

    }

    @Override
    public void onFailiureOfPayment(Order order, String s) {

        mView.showSuccessScreen(false);

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
