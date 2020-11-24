package com.cms.callmanager.background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MJobShedular extends JobService {
 private    MJobExecutor mJobExecutor;
    @Override
    public boolean onStartJob(final JobParameters params) {

        mJobExecutor = new MJobExecutor(){
            @Override
            protected void onPostExecute(String s) {
                //Toast.makeText(MJobShedular.this, s, Toast.LENGTH_SHORT).show();
              //  Toast.makeText(MJobShedular.this, "Hello", Toast.LENGTH_SHORT).show();
                jobFinished(params,false);


                Log.d("data1234456890123", s.toString());
                // dismiss the progress dialog after receiving data from API
                //   progressDialog.dismiss();
                try {
                    // JSON Parsing of data
                    //JSONArray jsonArray = new JSONArray(s);

                    JSONObject oneObject =  new JSONObject(s);
                    // Pulling items from the array
                    String title = oneObject.getString("ErrorMessage");
                   // Toast.makeText(MJobShedular.this, "Hello", Toast.LENGTH_SHORT).show();

                   // Toast.makeText(MJobShedular.this, ""+title, Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }




        };
        mJobExecutor.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobExecutor.cancel(true);
        return false;
    }
}
