package com.cms.callmanager;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.Foc_Chargeble.APIListner;
import com.cms.callmanager.activities.ChangePasswordActivity;
import com.cms.callmanager.activities.InvTrasActivity;
import com.cms.callmanager.adapter.CallListAdapter;
import com.cms.callmanager.constants.Constant;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.CallDTO;
import com.cms.callmanager.fcm_notification.MessageEvent;
import com.cms.callmanager.fcm_notification.Notification_list;
import com.cms.callmanager.fcm_notification.firebase.MyFirebaseMessagingService;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener,Constant {

    private TextView userName;
    public static String UserName;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SharedPreferences preferences;
    private TextView errorTxt;
    private Toolbar toolbar;
    CallDTO callDTO = new CallDTO();
    String date;
    LocationManager locationManager;
    String Latitude, Longitude;
    String Lati, Longi;

    MenuItem menuItem;
    // badge text view
    TextView badgeCounter;
    FrameLayout framelayout_id ;
    // change the number to see badge in action
    int pendingNotifications = 10;
    SharedPreferences prf;
    int badge_count;
    String Atm_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        prf = getSharedPreferences("counterValues",MODE_PRIVATE);





     //   prf = getSharedPreferences("counterValues",MODE_PRIVATE);


        SharedPreferences.Editor editor = prf.edit();
        badge_count = prf.getInt("badgecout", 0);
        editor.commit();
       // Toast.makeText(this, "" + badge_count, Toast.LENGTH_SHORT).show();

      //  badge_count++;






        getLocationData();

        //  GetCurrentTimeDate();


        initUI();
        if ((getIntent() != null && getIntent().hasExtra("callList") && getIntent().getParcelableArrayListExtra("callList") != null)) {
            ArrayList<CallDTO> calls = (ArrayList<CallDTO>) getIntent().getSerializableExtra("callList");

            if (calls.size() > 0) {
                Utils.Log("In callList");
                //calls.get(0).getResponseTime();


                mAdapter = new CallListAdapter(calls, HomeActivity.this, "Pending");
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                errorTxt.setVisibility(View.VISIBLE);
            }
        } else {
            getPendingCallList();








        }


    }










    // notification Code

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);

        menuItem = menu.findItem(R.id.nav_notification);


        if (badge_count == 0) {

            // if no pending notification remove badge

            menuItem.setActionView(null);
        } else {

            // if notification than set the badge icon layout
            menuItem.setActionView(R.layout.notification_badge);
            // get the view from the nav item
            View view = menuItem.getActionView();
            // get the text view of the action view for the nav item
            badgeCounter = view.findViewById(R.id.badge_counter);
            ImageView bell_icon = view.findViewById(R.id.bell_icon);
            framelayout_id = view.findViewById(R.id.framelayout_id);


            framelayout_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  i1 = new Intent(HomeActivity.this, Notification_list.class);
                    startActivity(i1);
                    MyFirebaseMessagingService.counter = 0;
                    SharedPreferences.Editor editor = prf.edit();
                    editor.remove("badgecout");
                    editor.clear();
                    editor.commit();




                }
            });

            badgeCounter.setText(String.valueOf(badge_count));
        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_notification:
                Intent  i1 = new Intent(HomeActivity.this, Notification_list.class);
                startActivity(i1);
                MyFirebaseMessagingService.counter = 0;
                SharedPreferences.Editor editor = prf.edit();
                editor.remove("badgecout");
                editor.clear();
                editor.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        SharedPreferences.Editor editor = prf.edit();
        badge_count = prf.getInt("badgecout", 0);
        editor.commit();
      //  Toast.makeText(this, "" + badge_count, Toast.LENGTH_SHORT).show();
        Log.d("", "OnMessageEvent: "+badge_count);

      //  badgeCounter.setText(String.valueOf(badge_count));


        if (badge_count == 0) {

            // if no pending notification remove badge

            menuItem.setActionView(null);
        } else {

            // if notification than set the badge icon layout
            menuItem.setActionView(R.layout.notification_badge);
            // get the view from the nav item
            View view = menuItem.getActionView();
            // get the text view of the action view for the nav item
            badgeCounter = view.findViewById(R.id.badge_counter);
            ImageView bell_icon = view.findViewById(R.id.bell_icon);
            framelayout_id = view.findViewById(R.id.framelayout_id);


            framelayout_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  i1 = new Intent(HomeActivity.this, Notification_list.class);
                    startActivity(i1);
                    MyFirebaseMessagingService.counter = 0;
                    SharedPreferences.Editor editor = prf.edit();
                    editor.remove("badgecout");
                    editor.clear();
                    editor.commit();




                }
            });

            badgeCounter.setText(String.valueOf(badge_count));
        }


        /* Do something */};
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



    private void getLocationData() {











        /*
------------------------get device registered mail id ----------------------------*/

        String device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        //Toast.makeText(this, "android_id= " + android_id, Toast.LENGTH_LONG).show();

        Prefs.with(HomeActivity.this).save(Constant.DEVICE_ID, device_id);


        // --------------------------get location --------------------


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        } else {

            getLocation();

        }
    }

    private void getLocation() {
        try {


            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean networkProviderExists = false;
            if (locationManager.getAllProviders().contains("network")) {
                networkProviderExists = true;
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    private void initUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.calllist);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        errorTxt = (TextView) findViewById(R.id.errorTxt);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", null);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            errorTxt.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            getPendingCallList();
                        } catch (Exception e) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.userName);
        UserName = preferences.getString("UserName", null);
        userName.setText(UserName);
        navigationView.setNavigationItemSelectedListener(this);

        int isTLFlag = preferences.getInt("IsTLFlag", 0);

        if (isTLFlag == 1) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.rejectedCalls).setVisible(true);
        } else {
            Utils.Log("in menu-------2--");
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.rejectedCalls).setVisible(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.Log("on resume");

        if ((getIntent() != null && getIntent().hasExtra("callList") && getIntent().getParcelableArrayListExtra("callList") != null)) {
            ArrayList<CallDTO> calls = (ArrayList<CallDTO>) getIntent().getSerializableExtra("callList");

            if (calls.size() > 0) {
                Utils.Log("In callList");


                mAdapter = new CallListAdapter(calls, HomeActivity.this, "Pending");
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else {
                mRecyclerView.setVisibility(View.GONE);
                errorTxt.setVisibility(View.VISIBLE);
            }
        } else {
            getPendingCallList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.Log("on Pause");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Utils.Log("On post REsume");
        if ((getIntent() != null && getIntent().hasExtra("callList") && getIntent().getParcelableArrayListExtra("callList") != null)) {
            ArrayList<CallDTO> calls = (ArrayList<CallDTO>) getIntent().getSerializableExtra("callList");

            if (calls.size() > 0) {
                Utils.Log("In callList");


                mAdapter = new CallListAdapter(calls, HomeActivity.this, "Pending");
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else {
                mRecyclerView.setVisibility(View.GONE);
                errorTxt.setVisibility(View.VISIBLE);
            }
        } else {
            getPendingCallList();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {
            case R.id.inventoryTransfer:
                startActivity(new Intent(HomeActivity.this, InvTrasActivity.class));
                finish();
                break;
            case R.id.searchCall:
                Intent searchIntent = new Intent(HomeActivity.this, searchCallActivity.class);
                startActivity(searchIntent);
                finish();
                break;
            case R.id.nearestATM:
                //Toast.makeText()
                Intent intent = new Intent(HomeActivity.this, NearestATMActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:


                SharedPreferences preferences =getSharedPreferences("mypref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();


                Intent logoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
                logoutIntent.putExtra("finish", true);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
                break;
            case R.id.pendingCallList:
                Intent i = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.rejectedCalls:



                Intent rejectedIntent = new Intent(HomeActivity.this, RejectedCallList.class);
                startActivity(rejectedIntent);
                finish();
                break;


           /* case R.id.ChangePassword:



                Intent ii = new Intent(HomeActivity.this, ChangePasswordActivity.class);
                startActivity(ii);
                finish();
                break;*/
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPendingCallList() {

        if (Utils.isInternetOn(this)) {

            String userId = preferences.getString("userId", null);

            PendingCallAsyncTask pendingCallAsyncTask = new PendingCallAsyncTask(Constants.PENDINGCALLS + "?userid=" + userId, "GET",
                    HomeActivity.this);
            pendingCallAsyncTask.execute();


            //Prefs.with(RepairDetailsActivity.this).getString(UserId, "")
            // S5NE000021621
            //9611776
            // Atm_ID






        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerView.setVisibility(View.GONE);
            errorTxt.setVisibility(View.VISIBLE);
            errorTxt.setText(getString(R.string.network_error));

        }


    }

    @Override
    public void onLocationChanged(Location location) {

        Lati = String.valueOf(location.getLatitude());
        Longi = String.valueOf(location.getLongitude());




/*

         LocationModel call = new LocationModel();

        call.setLatitude(Lati.toString());
        call.setLongitude(Longi.toString());
*/

        // String  lati = callDTO.getLatitude();

      //  Toast.makeText(this, lati, Toast.LENGTH_SHORT).show();





        Prefs.with(HomeActivity.this).save(LATI, Lati.toString());
        Prefs.with(HomeActivity.this).save(LONGI, Longi.toString());


        Log.d("", "onLocationChanged: ");

        //  Toast.makeText(this, Latitude , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }




    public class PendingCallAsyncTask extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public PendingCallAsyncTask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
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
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            mSwipeRefreshLayout.setRefreshing(false);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        // Utils.Log("response====" + json.toString() + "----");

                        //Utils.Log(json.getJSONArray("PayLoad").get(0).toString()+"====");
                        JSONArray respCallList = json.getJSONArray("PayLoad");
                        ArrayList<CallDTO> callDTOs = new ArrayList<>();
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {

                        } else if (respCallList.length() > 0) {
                            for (int i = 0; i < respCallList.length(); i++) {
                                CallDTO callDTO = new CallDTO();

                                callDTO.setLatitude(Lati);
                                callDTO.setLongitude(Longi);



                                JSONObject jsonData = (JSONObject) respCallList.get(i);
                                if (jsonData.getString("Docket_No") != null && jsonData.getString("Docket_No") != "") {
                                    callDTO.setDocketNo(jsonData.getString("Docket_No"));
                                }
                                if (jsonData.getString("ATM_Id") != null && jsonData.getString("ATM_Id") != "") {
                                    callDTO.setAtmID(jsonData.getString("ATM_Id"));

                                    Atm_ID = jsonData.getString("ATM_Id");


                                    getCallDtail();


                                }

                                if (jsonData.getString("Diagnosis") != null && jsonData.getString("Diagnosis") != "") {
                                    callDTO.setDiagnosis(jsonData.getString("Diagnosis"));
                                }

                                if (jsonData.getString("Call_Type") != null && jsonData.getString("Call_Type") != "") {
                                    callDTO.setCallType(jsonData.getString("Call_Type"));
                                }
                                if (jsonData.getString("Dispatch_Date") != null && jsonData.getString("Dispatch_Date") != "") {
                                    callDTO.setDispatchDate(jsonData.getString("Dispatch_Date"));
                                }

                                if (jsonData.getString("Call_Date") != null && jsonData.getString("Call_Date") != "") {
                                    String callDate = Utils.convertToCurrentTimeZone(jsonData.getString("Call_Date"));
                                    callDTO.setCallDate(callDate.toString());
                                }
                                if (jsonData.getString("Bank") != null && jsonData.getString("Bank") != "") {
                                    callDTO.setBankName(jsonData.getString("Bank"));
                                }
                                if (jsonData.getString("Target_Response_Time") != null && jsonData.getString("Target_Response_Time") != "") {
                                    String respTime = Utils.convertToCurrentTimeZone(jsonData.getString("Target_Response_Time"));
                                    callDTO.setResponseTime(respTime.toString());

                                    Log.d("", "getResponseTime---: " + callDTO.getResponseTime());

                                    /*if(date.equalsIgnoreCase(callDTO.getResponseTime())){

                                        Toast.makeText(HomeActivity.this, "notification", Toast.LENGTH_SHORT).show();

                                    }
                                    */

                                }
                                if (jsonData.getString("Target_Resolution_Time") != null && jsonData.getString("Target_Resolution_Time") != "") {
                                    String resolTIme = Utils.convertToCurrentTimeZone(jsonData.getString("Target_Resolution_Time"));
                                    callDTO.setResolutionTime(resolTIme.toString());
                                    //callDTO.getResolutionTime();

                                }

                                if (jsonData.getString("Status").equalsIgnoreCase("Open")) {
                                    Utils.Log("to titlecase====" + Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setMobileActivity(Utils.toTitleCase(jsonData.getString("Mobile_Activity")));
                                    callDTO.setMainStatus("Open");


                                } else if (jsonData.getString("Status").equalsIgnoreCase("Hold")) {
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setMobileActivity(Utils.toTitleCase(jsonData.getString("Mobile_Activity")));
                                    callDTO.setMainStatus("Hold");

                                } else {
                                    callDTO.setMobileActivity("Close");
                                    callDTO.setMainStatus("Close");

                                }

                                if (jsonData.getString("Mobile_Active") != null &&
                                        jsonData.getString("Mobile_Active").equalsIgnoreCase("0")) {
                                    callDTO.setActive(false);
                                } else {
                                    callDTO.setActive(true);
                                }

                                callDTOs.add(callDTO);
                                /*callDTOs.get(0).setActive(true);
                                callDTOs.get(0).setMainStatus("Open");
                                callDTOs.get(0).setStatus("Engineer Reached");
                                callDTOs.get(0).setMobileActivity("Engineer Reached");*/
                            }

                        } else {
                            Utils.showAlertBox("No records to display", HomeActivity.this);
                        }

                        if (callDTOs.size() > 0) {
                            // callDTOs.get(0).setStatus("DISPATCHED ENGINEER");









/*
                            //  Toast.makeText(this,  callDTOs.get(0).getResponseTime(), Toast.LENGTH_SHORT).show();



                            for (int i=0 ; i<callDTOs.size();i++)
                            {
                              String  res_times = callDTOs.get(i).getResponseTime();

                                Toast.makeText(HomeActivity.this, res_times, Toast.LENGTH_SHORT).show();

                                Log.d("", "onPostExecuteresdate: "+res_times);


                                //   String  res_times = "2019-06-04 14:48:00";




                                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                //final String received_date ="2019/06/03 12:05";
                                long timeInMilliseconds = 0;

                                try {
                                    Date date = sdf.parse(res_times);
                                    timeInMilliseconds = date.getTime();
                                    //System.out.println(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();


                                }

                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.SECOND, 0);
                                System.out.println("Current time => "+c.getTime());

                                String current_date = sdf.format(c.getTime());
                                Log.i("current_date",current_date+"");




                             *//*   if (current_date.equalsIgnoreCase(res_times))
                                {*//*
                                  //  String result  = callDTOs.get(i).getResponseTime();
                                    // notification

                                    Toast.makeText(HomeActivity.this, "Matched", Toast.LENGTH_SHORT).show();

                                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); //Please note that context is "this" if you are inside an Activity

                                    Intent intent = new Intent(HomeActivity.this, BroadcastManager.class);
                                 //   intent.putExtra("date",callDTOs.get(i).getResponseTime());
                                  //  intent.putExtra("atmid",callDTOs.get(i).getAtmID());
                                    PendingIntent event = PendingIntent.getBroadcast(HomeActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                    //alarmManager.setInexactRepeating (AlarmManager.RTC_WAKEUP, timeInMilliseconds, event);
                                    //alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, event);
                                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,timeInMilliseconds, AlarmManager.INTERVAL_DAY, event);

                               *//* }else {
                                    Toast.makeText(HomeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                }*//*
                            }*/
                            mAdapter = new CallListAdapter(callDTOs, HomeActivity.this, "Pending");
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                            errorTxt.setVisibility(View.VISIBLE);
                        }

                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), HomeActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), HomeActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", HomeActivity.this);
            }
        }
    }


 /*   public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);
            add = add + "," + obj.getAdminArea();
            add = add + "," + obj.getCountryName();

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }*/

 public void getCallDtail(){

     //Toast.makeText(this, ""+Atm_ID, Toast.LENGTH_SHORT).show();

    /* if(Atm_ID != null){

         CallListAdapter.GetOpenCall getOpenCall = new CallListAdapter.GetOpenCall(Constants.GetOpenCallOfaEng + "?ATMID="+Atm_ID+"&EngineerID="+Prefs.with(HomeActivity.this).getString(UserId, ""), "GET", this) ;
         getOpenCall.execute();

     }*/
 }




}
