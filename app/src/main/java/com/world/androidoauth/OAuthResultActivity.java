package com.world.androidoauth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class OAuthResultActivity extends Activity {

    private SharedPreferences prefs;
    private TextView txtApiResponse;
    private OAuthHelper oAuthHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        txtApiResponse = (TextView) findViewById(R.id.response_code);
        oAuthHelper = new OAuthHelper(prefs, Constants.OAUTHPARAMS);

        performApiCall();
    }

    private void performApiCall() {
        new ApiCallExecutor().execute();
    }

    private class ApiCallExecutor extends AsyncTask<Uri, Void, Void> {

        String apiResponse = null;

        @Override
        protected Void doInBackground(Uri... params) {

            try {
                apiResponse = oAuthHelper.executeApiCall();
                Log.i(Constants.TAG, "Received response from API : " + apiResponse);
            } catch (Exception ex) {
                ex.printStackTrace();
                apiResponse = ex.getMessage();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            txtApiResponse.setText(apiResponse);

        }

    }

}
