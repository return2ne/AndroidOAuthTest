package com.world.androidoauth;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;

public enum OAuthParams {

    NAVER_BLOG_OAUTH2(
            "",
            "",
            "https://nid.naver.com/oauth2.0/token",
            "https://nid.naver.com/oauth2.0/authorize",
            BearerToken.queryParameterAccessMethod(),
            "",
            "http://127.0.0.1",
            "naver",
            "https://openapi.naver.com/v1/nid/me"),

    GOOGLE_TASKS_OAUTH2(
            "",
            "",
            "https://accounts.google.com/o/oauth2/token",
            "https://accounts.google.com/o/oauth2/auth",
            BearerToken.authorizationHeaderAccessMethod(),
            "https://www.googleapis.com/auth/tasks",
            "http://localhost",
            "tasks",
            "https://www.googleapis.com/tasks/v1/users/@me/lists");

    private String clientId;
    private String clientSecret;
    private String tokenServerUrl;
    private String authorizationServerEncodedUrl;
    private AccessMethod accessMethod;
    private String scope;
    private String rederictUri;
    private String userId;
    private String apiUrl;

    OAuthParams(
            String clientId,
            String clientSecret,
            String tokenServerUrl,
            String authorizationServerEncodedUrl,
            AccessMethod accessMethod,
            String scope,
            String rederictUri,
            String userId,
            String apiUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenServerUrl = tokenServerUrl;
        this.authorizationServerEncodedUrl = authorizationServerEncodedUrl;
        this.accessMethod = accessMethod;
        this.scope = scope;
        this.rederictUri = rederictUri;
        this.userId = userId;
        this.apiUrl = apiUrl;
    }

    public String getClientId() {
        if (clientId == null || clientId.length() == 0) {
            throw new IllegalArgumentException("clientId is null");
        }
        return clientId;
    }

    public String getClientSecret() {
        /**
        if (clientSecret == null || clientSecret.length() == 0) {
            throw new IllegalArgumentException("clientSecret is null");
        }
         **/
        return clientSecret;
    }

    public String getTokenServerUrl() {
        return tokenServerUrl;
    }

    public String getAuthorizationServerEncodedUrl() {
        return authorizationServerEncodedUrl;
    }

    public AccessMethod getAccessMethod() {
        return accessMethod;
    }

    public String getScope() {
        return scope;
    }

    public String getRederictUri() {
        return rederictUri;
    }

    public String getUserId() {
        return userId;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}
