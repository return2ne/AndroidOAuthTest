package com.world.androidoauth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLDecoder;

@SuppressLint("SetJavaScriptEnabled")
public class OAuthAccessTokenActivity extends Activity {

    private WebView webview;
    boolean handled = false;
    private boolean hasLoggedIn;
    private SharedPreferences prefs;
    private OAuthHelper oAuthHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(Constants.TAG, "Starting task to retrieve request token.");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        oAuthHelper = new OAuthHelper(prefs, Constants.OAUTHPARAMS);
        webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVisibility(View.VISIBLE);
        setContentView(webview);

        String authorizationUrl = oAuthHelper.getAuthorizationUrl();
        Log.i(Constants.TAG, "Using authorizationUrl = " + authorizationUrl);

        handled = false;
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap bitmap) {
                Log.d(Constants.TAG, "onPageStarted : " + url + " handled = " + handled);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                Log.d(Constants.TAG, "onPageFinished : " + url + " handled = " + handled);

                if (url.startsWith(Constants.OAUTHPARAMS.getRederictUri())) {
                    webview.setVisibility(View.INVISIBLE);

                    if (!handled) {
                        new ProcessToken(url).execute();
                    }
                } else {
                    webview.setVisibility(View.VISIBLE);
                }
            }

        });

        webview.loadUrl(authorizationUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Constants.TAG, "onResume called with " + hasLoggedIn);
        if (hasLoggedIn) {
            finish();
        }
    }

    private class ProcessToken extends AsyncTask<Uri, Void, Void> {

        String url;
        boolean startActivity = false;

        public ProcessToken(String url) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(Uri... params) {


            if (url.startsWith(Constants.OAUTHPARAMS.getRederictUri())) {
                Log.i(Constants.TAG, "Redirect URL found" + url);
                handled = true;
                try {
                    if (url.indexOf("code=") != -1) {
                        String authorizationCode = extractCodeFromUrl(url);

                        Log.i(Constants.TAG, "Found code = " + authorizationCode);

                        oAuthHelper.retrieveAndStoreAccessToken(authorizationCode);
                        startActivity = true;
                        hasLoggedIn = true;

                    } else if (url.indexOf("error=") != -1) {
                        startActivity = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.i(Constants.TAG, "Not doing anything for url " + url);
            }
            return null;
        }

        private String extractCodeFromUrl(String url) throws Exception {
            String code = "";
            try {
                Uri uri = Uri.parse(url);
                code = URLDecoder.decode(uri.getQueryParameter("code"), "UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return code;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
            if (startActivity) {
                startActivity(new Intent(OAuthAccessTokenActivity.this, OAuthResultActivity.class));
                finish();
            }

        }

    }

}
