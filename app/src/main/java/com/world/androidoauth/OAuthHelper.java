package com.world.androidoauth;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class OAuthHelper {

    //http://www.programcreek.com/java-api-examples/index.php?api=com.google.api.client.auth.oauth2.StoredCredential
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private final CredentialStore credentialStore;
    private AuthorizationCodeFlow flow;
    private OAuthParams oAuthParams;

    public OAuthHelper(SharedPreferences sharedPreferences, OAuthParams oAuthParams) {

        credentialStore = new SharedPreferencesCredentialStore(sharedPreferences);
        this.oAuthParams = oAuthParams;
        flow = new AuthorizationCodeFlow.Builder(
                this.oAuthParams.getAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl(this.oAuthParams.getTokenServerUrl()),
                new ClientParametersAuthentication(
                        this.oAuthParams.getClientId(),
                        this.oAuthParams.getClientSecret()),
                this.oAuthParams.getClientId(),
                this.oAuthParams.getAuthorizationServerEncodedUrl())
                .setCredentialStore(credentialStore).build();

    }

    public String getAuthorizationUrl() {

        String authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(oAuthParams.getRederictUri())
                .setScopes(convertScopesToString(oAuthParams.getScope()))
                .build();
        return authorizationUrl;

    }

    public void retrieveAndStoreAccessToken(String authorizationCode) throws IOException {

        Log.i(Constants.TAG, "retrieveAndStoreAccessToken for code " + authorizationCode);

        TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                .setScopes(convertScopesToString(oAuthParams.getScope()))
                .setRedirectUri(oAuthParams.getRederictUri())
                .execute();
        Log.i(Constants.TAG, "Found tokenResponse :");
        Log.i(Constants.TAG, "Access Token : " + tokenResponse.getAccessToken());
        Log.i(Constants.TAG, "Refresh Token : " + tokenResponse.getRefreshToken());
        flow.createAndStoreCredential(tokenResponse, oAuthParams.getUserId());

    }

    public String executeApiCall() throws IOException {

        Log.i(Constants.TAG, "Executing API call at url " + this.oAuthParams.getApiUrl());
        return HTTP_TRANSPORT.createRequestFactory(loadCredential())
                .buildGetRequest(new GenericUrl(this.oAuthParams.getApiUrl()))
                .execute()
                .parseAsString();

    }

    public Credential loadCredential() throws IOException {

        return flow.loadCredential(oAuthParams.getUserId());

    }

    public void clearCredentials() throws IOException {

        flow.getCredentialStore().delete(oAuthParams.getUserId(), null);

    }

    private Collection<String> convertScopesToString(String scopesConcat) {

        Collection<String> collection = null;
        if (scopesConcat != null && !scopesConcat.equals("")) {
            String[] scopes = scopesConcat.split(",");
            collection = new ArrayList<>();
            Collections.addAll(collection, scopes);
        }

        return collection;
    }

}
