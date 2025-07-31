package vn.chuot96.auth3rdapi.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {
    GOOGLE("google", "https://www.googleapis.com/oauth2/v3/certs"),
    FACEBOOK("facebook", "https://graph.facebook.com/me?fields=id,name,email,picture&access_token="),
    FIREBASE("eyJ", "https://securetoken.google.com/PROJECT_ID");

//    AUTH0("auth0", "https://AUTH0_DOMAIN/"),
//    COGNITO("cognito", "https://cognito-idp.REGION.amazonaws.com/USER_POOL_ID"),
//    AZURE("azure", "https://login.microsoftonline.com/TENANT_ID/v2.0");

    private final String key;
    private final String endpoint;

}
