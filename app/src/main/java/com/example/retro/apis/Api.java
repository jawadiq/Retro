package com.example.retro.apis;

import com.example.retro.models.Main;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Path;


public interface Api {
  @GET("forecast/{api}/{lat},{lon}")
  Call<Main> getModels(@Path("api")String key, @Path("lat") double lat, @Path("lon")double lon);
}
