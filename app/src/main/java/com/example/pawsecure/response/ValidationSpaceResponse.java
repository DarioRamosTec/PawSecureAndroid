package com.example.pawsecure.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValidationSpaceResponse {
    @SerializedName("name")
    public List<String> name;

    @SerializedName("description")
    public List<String> description;

    @SerializedName("pets")
    public List<String> pets;

    @SerializedName("mac")
    public List<String> mac;
}
