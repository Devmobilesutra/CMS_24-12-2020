package com.cms.callmanager.fcm_notification;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cms.callmanager.Foc_Chargeble.APIListner;
import com.cms.callmanager.HomeActivity;
import com.cms.callmanager.Prefs;
import com.cms.callmanager.R;
import com.cms.callmanager.RepairDetailsActivity;
import com.cms.callmanager.adapter.Open_Call_Details_Adapter;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.fcm_notification.firebase.MyFirebaseMessagingService;
import com.cms.callmanager.services.CallManagerAsyncTaskArray;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static com.cms.callmanager.constants.Constant.UserId;

public class Notification_list extends AppCompatActivity {

    ArrayList<Notification_Model> notification_list ;
    private ProgressDialog progDailog;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

         recyclerView = (RecyclerView)findViewById(R.id.rv_notification_list);

      //  MyFirebaseMessagingService.counter = 0;


        new GetNotification_list(Constants.ManageGetCallresolutionTimebeforeFifteen + "?EngineerID="+Prefs.with(Notification_list.this).getString(UserId, ""), "GET", this, new APIListner() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onErrors() {
            }
        }).execute();



    }


    class GetNotification_list extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;

        public GetNotification_list(String action, String reqType, Context context, APIListner apiListner) {
            super(action, reqType, context);
            this.apiListner = apiListner;
        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {

                return doWorkJSONArray(postParamData);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(Notification_list.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            notification_list  = new ArrayList<>();

            if (json != null) {
                Utils.Log("JSON GetOpenCall1234=========" + json.toString());
                progDailog.dismiss();

                try {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject dataobj = json.getJSONObject(i);


                        Notification_Model model = new Notification_Model();

                        model.setDocketNo(dataobj.getString("DocketNo"));
                        model.setATMId(dataobj.getString("ATMId"));
                        model.setAddress(dataobj.getString("Address"));
                        model.setCalllogDateTime(dataobj.getString("CalllogDateTime"));
                        model.setTargetResponseTime(dataobj.getString("TargetResponseTime"));
                        model.setTargetResolutionTime(dataobj.getString("TargetResolutionTime"));
                        model.setRequestType(dataobj.getString("RequestType"));




                        notification_list.add(model);
                        Notification_Adapter adapter = new Notification_Adapter(notification_list);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Notification_list.this));
                        recyclerView.setAdapter(adapter);


                        Log.d("", "onPostExecute_notification_list: "+ notification_list);

                    }

                    apiListner.onSuccess();
                      progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertBox("Record Not Found!!", Notification_list.this);
                progDailog.dismiss();

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(Notification_list.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


}
