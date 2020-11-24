package com.cms.callmanager.fcm_notification.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();


    Context context;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        // storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

       /* // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer1234: " + token);


        //   Prefs.with(context).save(FCM_ID,token);

         Log.d(TAG, "sendRegistrationToServer12345: "+token);

        // MyApp.set_session(MyApp.PREFS_PROPERTY_REG_ID,token);
    }
}