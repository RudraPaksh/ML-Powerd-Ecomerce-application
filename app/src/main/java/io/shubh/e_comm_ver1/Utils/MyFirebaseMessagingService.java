package io.shubh.e_comm_ver1.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import io.shubh.e_comm_ver1.R;
import io.shubh.e_comm_ver1.Splash.View.SplashActivity;


//this below class runs on background...without this class ,we can recieve the notification as
// they are designed in the firebase cloud function
//this class runs on background...even when it gets detroyed its revived by os to customize the incming notification

//todo- on button click it opens the application regardless if the app is alraedy open in foreground or not..so fix taht

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    FirebaseFirestore db;
    String TAG = "MyFirebaseMessagingService";

    //below token is neccessary in the case ..to send notifications to specific persons
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);

//storing in local storage
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("token", s).apply();


        //I have using the below code of storing the token in database plus the storing it local because ..token is generated on
        // install..and at that time user wont be logged in so..token cant be stored inside under his document online
        // so I m storing it locally and then storing it ebery time user logs in or register
        //but i m also trying to storing it right now..because token get refresh if os feels account scuritycompromise
        //and at that time ..user might be logged in ..so the token will not get refresh online

        //updating the new token in database //Though I manually can put the fresh token in the field of notification by using FirebaseInstanceId.getInstance() ..but it aint considered the cleanest approach
        //..but below is neccessary in case I need to send the notif from the firebase dashboard ..then it will have latest token by here

        //I have commentized the below code becuase it was crashing the app ..when app was not logged in
       /* db = FirebaseFirestore.getInstance();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("token", s);
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update(userMap);*/


    }

   /*notification messages are delivered to your onMessageReceived callback only when your app is in the foreground. If your app is in the background or closed then a notification message is shown in the notification center,*/
   /*Don't forget to include "priority": "high" field in your notification request. According to the documentation: data messages are sent with a normal priority, thus they will not arrive instantly; it could also be the problem.*/
    //thus I have removed the notif field and all the data is in adata field
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i(TAG, "onMessageReceived: message recieved");

        Map<String, String> data = remoteMessage.getData();
        /*String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String itemUrl = remoteMessage.getNotification().getIcon();*/
        String title = (String) data.get("title");
        String message = (String) data.get("body");
        String itemUrl = (String) data.get("icon");

         String type = (String) data.get("type");
        // String imageUrl = (String) data.get("image");
        // String action = (String) data.get("action");

        //1st step loading Imaf=g url
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(itemUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        Bitmap bitmap = resource;

                        makeNotifAndDisplay( title ,message ,bitmap ,type );
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });








    }

    private void makeNotifAndDisplay(String title, String message, Bitmap bitmap, String type) {

//todo -make it unique for each notif using counter from utility later
        int NotificationID = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = title;
            String description = message;
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //below intent for when click on notification
        Intent notificationIntent = new Intent(getBaseContext(), SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle extras = new Bundle();
        extras.putString("type", type);
        notificationIntent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, NotificationID, notificationIntent, 0);

        Intent cancel = new Intent("io.shubh.e_comm_ver1");
        PendingIntent cancelP = PendingIntent.getBroadcast(getBaseContext(), NotificationID, cancel, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Action Viewaction = new NotificationCompat.Action(android.R.drawable.ic_menu_view, "VIEW", pendingIntent);
        NotificationCompat.Action Dissmissaction = new NotificationCompat.Action(android.R.drawable.ic_delete, "DISSMISS", cancelP);

//todo- dissmiss bt aint working ..make it work using this link https://stackoverflow.com/questions/19739371/dismiss-ongoing-android-notification-via-action-button-without-opening-app

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.shopping_bag_app_icon)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(Viewaction)
                .addAction(Dissmissaction);





//    mBuilder.setContentIntent(launchIntent);


        Notification buildNotification = mBuilder.build();
        NotificationManager mNotifyMgr = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(001, buildNotification);

    }


    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("token", "empty");
    }

}