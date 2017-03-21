package com.world.androidoauth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity {

    private SharedPreferences prefs;

    private Button btnStartGoogleOAuth;
    private Button btnClearGoogleOAuth;
    private Button btnStartNaverOAuth;
    private Button btnClearNaverOAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btnStartGoogleOAuth = (Button) findViewById(R.id.btnStartGoogleOAuth);
        btnStartGoogleOAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOAuthFlow(OAuthParams.GOOGLE_TASKS_OAUTH2);
            }
        });

        btnClearGoogleOAuth = (Button) findViewById(R.id.btnClearGoogleOAuth);
        btnClearGoogleOAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCredentials(OAuthParams.GOOGLE_TASKS_OAUTH2);
            }
        });

        btnStartNaverOAuth = (Button) findViewById(R.id.btnStartNaverOAuth);
        btnStartNaverOAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOAuthFlow(OAuthParams.NAVER_LOGIN_OAUTH2);
            }
        });

        btnClearNaverOAuth = (Button) findViewById(R.id.btnClearNaverOAuth);
        btnClearNaverOAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCredentials(OAuthParams.NAVER_LOGIN_OAUTH2);
            }
        });
    }

    private void startOAuthFlow(OAuthParams oAuthParams) {
        Constants.OAUTHPARAMS = oAuthParams;
        startActivity(new Intent().setClass(this, OAuthAccessTokenActivity.class));
    }

    private void clearCredentials(OAuthParams oAuthParams) {
        try {
            new OAuthHelper(prefs, oAuthParams).clearCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}