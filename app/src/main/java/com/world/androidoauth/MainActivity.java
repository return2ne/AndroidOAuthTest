package com.world.androidoauth;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private WebView webView;
    private TextView txtGoogleOAuth;
    private Button btnGoogleOAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGoogleOAuth = (TextView) findViewById(R.id.txtGoogleOAuth);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        btnGoogleOAuth = (Button) findViewById(R.id.btnGoogleOAuth);
        btnGoogleOAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}