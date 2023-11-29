package com.example.pawsecure.request;
import com.example.pawsecure.model.Movement;

import retrofit2.Call;
import retrofit2.http.GET;
public interface MovementRequest {
    @GET("endpoint") // Reemplaza "endpoint" con el endpoint específico de tu API
    Call<Movement> getData(); // Reemplaza YourResponseModel con el modelo de datos de tu respuesta

}
