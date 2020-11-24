package com.cms.callmanager.fcm_notification.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.cms.callmanager.LoginActivity;
import com.cms.callmanager.Prefs;
import com.cms.callmanager.R;
import com.cms.callmanager.fcm_notification.MessageEvent;
import com.cms.callmanager.fcm_notification.Notification_list;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.cms.callmanager.constants.Constant.UserId;
import static com.cms.callmanager.constants.Constant.badgecount;

/**
 * Created by Bhavesh on 10/10/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static   int counter = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences pref;
    String title, body;



    public static final String mypreference = "FCMclearCount";

    public static final String debugTag = "MyFirebaseApp";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);




        if(remoteMessage.getData().toString()!= null) {
            Log.e("DATA", remoteMessage.getData().toString());
            JSONObject object = null;
            try {
                object = new JSONObject(remoteMessage.getData().toString());
                JSONArray arr = object.getJSONArray("body");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    title = obj.getString("Title");
                    Log.d(debugTag, "Notification Received Title!" + title);

                    body = obj.getString("body");
                    Log.d(debugTag, "Notification Received body!" + body);

                }

                sendNotification(title,body);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {

        }


        counter= 0;

        counter++;

        Log.d("", "onMessageReceivedCounter: "+ counter);
        pref = getSharedPreferences("counterValues",MODE_PRIVATE);
        int badge_count = counter;

        int c = pref.getInt("badgecout",0);
        badge_count = counter+c;

        Log.d("", "VariablesendNotificationcounter: "+counter);
        Log.d("", "sendNotificationcounter: "+badge_count);

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("badgecout",badge_count);
        editor.commit();

        EventBus.getDefault().post(new MessageEvent(badge_count));



    }


    private void sendNotification(String title,String messageBody) {


        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setLargeIcon(largeIcon)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        int notificationCode = (int) (Math.random() * 1000);
        notificationManager.notify(notificationCode /* ID of notification */, notificationBuilder.build());




        /*if(counter> 0){

            int badge_count = counter;
            Log.d("", "sendNotificationcounter: "+badge_count);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("badgecout",badge_count);
            editor.commit();
        }*/
    }







/*




    //This method is only generating push notification
    private void sendNotification(String title, String messageBody) {

        Intent intent = new Intent(this, Notification_list.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent);
//        Toast.makeText(this, ""+title, Toast.LENGTH_SHORT).show();
        Log.d("", "title: "+title);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

        if(counter> 0){

             int badge_count = counter;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("badgecout",badge_count);
            editor.commit();
        }



    }

*/


}

