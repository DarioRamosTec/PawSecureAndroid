package com.example.pawsecure.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValidationPetResponse {

    @SerializedName("name")
    public List<String> name;

    @SerializedName("race")
    public List<String> race;

    @SerializedName("sex")
    public List<String> sex;

    @SerializedName("icon")
    public List<String> icon;

    @SerializedName("image")
    public List<String> image;

    @SerializedName("animal")
    public List<String> animal;

    @SerializedName("birthday")
    public List<String> birthday;

    @SerializedName("description")
    public List<String> description;
}
