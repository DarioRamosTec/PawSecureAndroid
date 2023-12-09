package com.example.pawsecure.response;

import com.example.pawsecure.implementation.PawSecureResponse;
import com.example.pawsecure.model.Pet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PetResponse extends PawSecureResponse
{
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public List<Pet> data;

    @SerializedName("errors")
    public ValidationPetResponse errors;
}
