package com.example.pawsecure.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValidationEmailResponse {

    @SerializedName("email")
    public List<String> email;

    @SerializedName("password")
    public List<String> password;

    @SerializedName("password_again")
    public List<String> password_again;

    @SerializedName("name")
    public List<String> name;

    @SerializedName("lang")
    public List<String> lang;
}
