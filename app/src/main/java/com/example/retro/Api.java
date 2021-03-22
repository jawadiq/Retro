package com.example.retro;

import androidx.core.app.RemoteInput;

import com.google.gson.annotations.Expose;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

import retrofit2.http.Header;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface Api {
  @GET("forecast/{api}/{lat},{lon}")
  Call<Main> getModels(@Path("api")String key, @Path("lat") double lat,@Path("lon")double lon);
}
