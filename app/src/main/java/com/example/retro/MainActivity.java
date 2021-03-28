package com.example.retro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.retro.apis.Api;
import com.example.retro.models.Main;
import com.google.android.material.timepicker.TimeFormat;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    String URL = "https://api.darksky.net/";
    TextView textView, locationView, city;
    private String latitude;
    private String longitude;
    private String API_KEY = "2bb07c3bece89caf533ac9a5d23d8417";
    double lat;
    double longi;
    LocationManager locationManager;
    Location location;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.myTextView);
        locationView = findViewById(R.id.myLocation);
        city = findViewById(R.id.cityView);

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);


        } else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                lat = location.getLatitude();
                longi = location.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                locationView.append(String.valueOf(lat) + "" + String.valueOf(longi));

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                }
            };

            }
            Geocoder geocoder;

            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    locationView.append(System.getProperty("line.separator"));
                    locationView.append(address);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String unitTimeToDate(double unitTime){
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long)unitTime*1000);
        return dateFormat.format(date);
    }

    @Override
    protected void onStart() {
        super.onStart();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
            Api api = retrofit.create(Api.class);
            Call<Main> call = api.getModels(API_KEY, lat, longi);
            call.enqueue(new Callback<Main>() {
                @Override
                public void onResponse(Call<Main> call, Response<Main> response) {
                    if (response.code() == 200) {
                        Main main = response.body();
                        city.setText(main.getTimezone());


                            textView.append("Time: " + unitTimeToDate(main.getCurrently().getTime()));
                            textView.append(System.getProperty("line.separator"));

                        textView.append("Summary " + main.getCurrently().getSummary());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Percip Type " + main.getCurrently().getPrecipIntensity().toString());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Temprature " + main.getCurrently().getTemperature().toString());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Icon " + main.getCurrently().getIcon());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Dew Point " + main.getCurrently().getDewPoint().toString());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Pressure " + main.getCurrently().getPressure().toString());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Wind speed " + main.getCurrently().getWindSpeed().toString());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Cloud Cover " + main.getCurrently().getCloudCover().toString());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Visibility " + main.getCurrently().getVisibility());
                        textView.append(System.getProperty("line.separator"));
                        textView.append("Ozone " + main.getCurrently().getOzone().toString());
                    }
//
                    else if (response.code() == 401) {
                        Toast.makeText(MainActivity.this, "an Error Occurred", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Cannot load weather at the moment", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Main> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "this" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


