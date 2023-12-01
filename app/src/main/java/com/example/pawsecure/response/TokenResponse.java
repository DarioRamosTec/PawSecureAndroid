package com.example.pawsecure.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class TokenResponse {
    @SerializedName("error")
    public String error;

    @SerializedName("access_token")
    public String access_token;

    @SerializedName("token_type")
    public String token_type;

    @SerializedName("expires_in")
    public long expires_in;
}
