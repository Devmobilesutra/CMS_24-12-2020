package com.cms.callmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.LoginActivity;
import com.cms.callmanager.R;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView username;
    Button btn_sub;
    EditText new_pswd, confirm_pswd;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);

        new_pswd =(EditText)findViewById(R.id.confirm_pswd);
        confirm_pswd =(EditText)findViewById(R.id.confirm_pswd);
        username =(TextView)findViewById(R.id.cms_username);
        btn_sub =(Button)findViewById(R.id.btn_sub);
        String name = preferences.getString("UserName", null);
        username.setText(name);


        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String new_pswd1 = new_pswd.getText().toString();
                String confirm_pswd1 = confirm_pswd.getText().toString();
             //   if(new_pswd1 == confirm_pswd1) {



                    SharedPreferences preferences =getSharedPreferences("mypref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    preferences.edit().remove("password").commit();
                    finish();



                    Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(i);
               // }else{
                 //   Toast.makeText(ChangePasswordActivity.this, "Enter Wrong password", Toast.LENGTH_SHORT).show();
               // }
            }
        });

    }
}
