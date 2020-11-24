package com.cms.callmanager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cms.callmanager.Foc_Chargeble.APIListner;
import com.cms.callmanager.background.MJobShedular;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.UserDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;
import com.cms.callmanager.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.cms.callmanager.constants.Constant.Pass;
import static com.cms.callmanager.constants.Constant.UserId;

public class LoginActivity extends AppCompatActivity {

    EditText name;
    EditText passwordTxt;

    TextInputLayout txtUserNameLayout;
    TextInputLayout txtPwdLayout;

    Button loginBtn;
    String[] perms = {"android.permission.FINE_LOCATION"};
    SharedPreferences preferences;
    int permsRequestCode = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;

    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    String token_id ;
    private  static final int JOB_ID =101;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


     // Start Background Service Code
        ComponentName componentName = new ComponentName(this, MJobShedular.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,componentName);
        builder.setPeriodic(5000);
    /*    builder.setRequiresCharging(true);
        builder.setRequiredNetworkType(jobInfo.NETWORK_TYPE_UNMETERED);
        builder.setPersisted(true);*/
        jobInfo= builder.build();
        jobScheduler =(JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);



        //
      /*  Intent  i = new Intent(this, BackgroundService.class);
        startService(i);*/

       /* if (Utils.isInternetOn(LoginActivity.this)) {


        }else{
            Utils.showAlertBox(getString(R.string.network_error), LoginActivity.this);


        }*/


        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        if (!checkPermission())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, permsRequestCode);
            }
        actionBar.hide();

        initUI();
        /*name.setText("9606300");
        passwordTxt.setText("1234");*/
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.isInternetOn(LoginActivity.this)) {
                    if (!Validation.validateField(name, txtUserNameLayout, LoginActivity.this,
                            getString(R.string.InvalidName))) {
                        return;
                    } else if (!Validation.validatePassword(passwordTxt, txtPwdLayout, LoginActivity.this)) {
                        return;
                    } else {
                        ProgressUtil.showProgressBar(LoginActivity.this, findViewById(R.id.root), R.id.progressBar);
                        login();


                        // Start Background Service
                        jobScheduler.schedule(jobInfo);
                     //   Toast.makeText(LoginActivity.this, "StartJob", Toast.LENGTH_SHORT).show();

                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("", "getInstanceId failed", task.getException());
                                            return;
                                        }

                                        // Get new Instance ID token
                                        token_id = task.getResult().getToken();

                                        // Log and toast
                                        Log.d("", token_id);
                                        Log.d("", "sendRegistrationToServer12345: " + token_id);

                                       // Toast.makeText(LoginActivity.this, token_id, Toast.LENGTH_SHORT).show();
                                    }
                                });



                    }
                } else {
                    Utils.showAlertBox(getString(R.string.network_error), LoginActivity.this);
                }
            }
        });


    }

    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        //  int result1 = ContextCompat.checkSelfPermission(getApplicationContext());

        return result == PackageManager.PERMISSION_GRANTED;


    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    //        boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    //   if (locationAccepted && cameraAccepted)
                    //       Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    //   else {

                    //    Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
//
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void initUI() {

        loginBtn = (Button) findViewById(R.id.signIn);
        name = (EditText) findViewById(R.id.edtTxtUserName);
        passwordTxt = (EditText) findViewById(R.id.edTxtPassword);


        String UserName = sharedPreferences.getString("username", "");
        String Password = sharedPreferences.getString("password", "");

        name.setText(UserName);
        passwordTxt.setText(Password);

        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);

        txtUserNameLayout = (TextInputLayout) findViewById(R.id.txtUserNameLayout);
        txtPwdLayout = (TextInputLayout) findViewById(R.id.txtPwdLayout);


        name.addTextChangedListener(new MyTextWatcher(name));
        passwordTxt.addTextChangedListener(new MyTextWatcher(passwordTxt));


    }

    public void login() {


        String userName = name.getText().toString();
        String password = passwordTxt.getText().toString();

        Prefs.with(this).getString(UserId, "");

        UserAsyncTask userAsyncTask = null;
        userAsyncTask = new UserAsyncTask(Constants.LOGIN, "POST", userName, password, LoginActivity.this);
        userAsyncTask.execute();

    }

    public class UserAsyncTask extends CallManagerAsyncTask {
        InputStream responseXML = null;
        String userName;
        String password;


        public UserAsyncTask(String action, String reqType, String userName, String password, Context context) {
            super(action, reqType, context);
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("User_ID", userName);
                postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        Utils.Log("response====" + json.toString());


                        new SendFCM_Id(Constants.SendFCM + "?EngneerId=" + userName +"&GICNo="+token_id, "POST", LoginActivity.this, new APIListner() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onErrors() {
                            }
                        }).execute();

                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONObject user = (JSONObject) json.getJSONArray("PayLoad").get(0);
                        UserDTO userDTO = new UserDTO();
                        if (user.getString("UserId") != null && user.getString("UserId") != "") {
                            userDTO.setUserId(user.getString("UserId"));
                        }
                        if (user.getString("UserName") != null && user.getString("UserName") != "") {
                            userDTO.setFirstName(user.getString("UserName"));
                        }
                        if (user.getString("IsTl") != null) {
                            userDTO.setTLFlag(Boolean.parseBoolean(user.getString("IsTl")));
                        }
                        preferences.edit().putInt("IsTLFlag", Integer.parseInt(user.getString("IsTl"))).commit();
                        preferences.edit().putString("UserName", user.getString("FullName").toString()).commit();
                        preferences.edit().putString("userId", user.getString("UserId").toString()).commit();


                        Prefs.with(LoginActivity.this).save(UserId, userName);
                        Prefs.with(LoginActivity.this).save(Pass, password);


                        // store shared preference
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", userName);
                        editor.putString("password", password);
                        editor.commit();







                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage"), LoginActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage"), LoginActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Utils.showAlertBox(json.getString("ErrorMessage"), LoginActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.edtTxtUserName:
                    Validation.validateField(name, txtUserNameLayout, LoginActivity.this, getString(R.string.InvalidName));
                    break;
                case R.id.edTxtPassword:
                    Validation.validateField(passwordTxt, txtPwdLayout, LoginActivity.this, getString(R.string.InvalidPassword));
                    break;
            }
        }
    }


    public class SendFCM_Id extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();
        APIListner apiListner;

        public SendFCM_Id(String action, String reqType, Context context, APIListner apiListner) {
            super(action, reqType, context);
            this.apiListner = apiListner;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(postParamData);
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

            if (json != null) {

                try {


                    Log.d("", "onPostExecuteSendFCMErrorMessage: "+json);

                    String ErrorMessage =  json.getString("ErrorMessage");
                    Log.d("", "onPostExecuteFCMResponse: "+ErrorMessage);
                   // Toast.makeText(LoginActivity.this, ""+ErrorMessage, Toast.LENGTH_SHORT).show();


                    apiListner.onSuccess();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                // Utils.showAlertBox("Record Not Found!!", Notification_list.this);

            }
        }
    }
}





