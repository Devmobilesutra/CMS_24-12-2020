package com.cms.callmanager.background;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cms.callmanager.Prefs;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.cms.callmanager.constants.Constant.UserId;

public class MJobExecutor extends AsyncTask<Void, Void, String> {

    MJobExecutor getApplicationContext = null;
    private Context ctx;


    String USER_ID = Prefs.with(ctx).getString(UserId, "");


    //String apiUrl ="https://slmuat.cms.com:8085/UATMB/cms/MovePartRequest/GetOpenCallNotAcceptedByEng?EngineerID="+USER_ID;
    String apiUrl ="https://naverp.cms.com:8085/webapi/cms/MovePartRequest/GetOpenCallNotAcceptedByEng?EngineerID="+USER_ID;

    private static final String AuthorizationKey = "Basic V0lOU1BJUkVBRE1JTjp3aW5zcGlyZUAxMjM=";
  //  https://naverp.cms.com:8085/webapi/

    @Override
    protected String doInBackground(Void... voids) {
        // implement API in background and store the response in current variable
//        Toast.makeText(ctx, ""+USER_ID, Toast.LENGTH_SHORT).show();
        String current = "";
        try {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(apiUrl);

                urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.setRequestProperty("Authorization", AuthorizationKey);
                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    current += (char) data;
                    data = isw.read();
                    System.out.print(current);

                }
                // return the data to onPostExecute method
                return current;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
        return current;
    }

}

