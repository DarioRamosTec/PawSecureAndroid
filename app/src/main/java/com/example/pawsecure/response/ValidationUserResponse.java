package com.example.pawsecure.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValidationUserResponse {

    @SerializedName("email")
    public List<String> email;

    @SerializedName("password")
    public List<String> password;

    @SerializedName("password_again")
    public List<String> password_again;

    @SerializedName("name")
    public List<String> name;

    @SerializedName("middle_name")
    public List<String> middle_name;

    @SerializedName("last_name")
    public List<String> last_name;

    @SerializedName("lang")
    public List<String> lang;
}
