package com.example.pawsecure.response;

import com.example.pawsecure.implementation.PawSecureResponse;
import com.google.gson.annotations.SerializedName;

public class TokenResponse extends PawSecureResponse {

    @SerializedName("access_token")
    public String access_token;

    @SerializedName("token_type")
    public String token_type;

    @SerializedName("expires_in")
    public long expires_in;
}
