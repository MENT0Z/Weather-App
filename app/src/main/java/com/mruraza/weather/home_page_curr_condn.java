package com.mruraza.weather;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home_page_curr_condn extends AppCompatActivity {
    Button btntonextpage;
    String ansslat, anslongit, anstoshow, url;
    double latt, longg;
    TextView txtt,tempid;
    ArrayList<String>newarr = new ArrayList<>(1);
    FusedLocationProviderClient fusedLocationProviderClient;
    private final String apkid = "95d14b9dc65d9a290ed3d27f2a430672";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page_curr_condn);
        btntonextpage = findViewById(R.id.globalweatherbtn);
        getSupportActionBar().hide();

        tempid = findViewById(R.id.tempid);
        txtt = findViewById(R.id.resulttvcurr);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(home_page_curr_condn.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();
        } else {
            ActivityCompat.requestPermissions(home_page_curr_condn.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(home_page_curr_condn.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        latt = addresses.get(0).getLatitude();
                        longg = addresses.get(0).getLongitude();
                        ansslat = String.valueOf(latt);
                        anslongit = String.valueOf(longg);

                        // Build the URL and make the API request inside onComplete
                        anstoshow = "?lat=" + ansslat + "&lon=" + anslongit;
                        url = "https://api.openweathermap.org/data/2.5/weather" + anstoshow + "&appid=" + apkid;



                        makeWeatherAPIRequest();
//
//                        if(newarr.get(0).equals("raining")){
//                            View activityview = getWindow().getDecorView();
//                            activityview.setBackgroundResource(R.drawable.rasinny);
//                        }else {
//                            View activityview = getWindow().getDecorView();
//                            activityview.setBackgroundResource(R.drawable.background_home);
//                        }




                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        btntonextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page_curr_condn.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void makeWeatherAPIRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectweather = jsonArray.getJSONObject(0);
                    String description = jsonObjectweather.getString("description");

                    JSONObject jsonobjmain = jsonObject.getJSONObject("main");
                    double temperature = jsonobjmain.getDouble("temp") - 273.15;
                    double feels_like = jsonobjmain.getDouble("feels_like") - 273.15;
                    float pressure = jsonobjmain.getInt("pressure");
                    int humidity = jsonobjmain.getInt("humidity");

                    JSONObject jsonobjwind = jsonObject.getJSONObject("wind");
                    String wind_speed = jsonobjwind.getString("speed");

                    JSONObject JSONOBJcloud = jsonObject.getJSONObject("clouds");
                    String clouds = JSONOBJcloud.getString("all");

                    JSONObject jsonobjsys = jsonObject.getJSONObject("sys");
                    String country = jsonobjsys.getString("country");
                    String citynaem = jsonObject.getString("name");

                    String formattedTemperature = String.format("%.2f", temperature)+"°C";
                    String formattedFeelsLike = String.format("%.2f", feels_like);

                    String output = "Country = " + country + "\nCity = " + citynaem
                            + "\nfeels_like = " + formattedFeelsLike +
                            "\npressure = " + pressure + "\nhumidity = " + humidity +
                            "\nwind speed = " + wind_speed + "\nclouds = " + clouds
                            +"\ncondition =" +description;

                    txtt.setText(output);
                    newarr.add(description);
                    tempid.setText(formattedTemperature);

                    if (description.contains("cloud")
                    ) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.cloudy);
                        
                    } else if (description.contains("raining")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.rasinny);
                    } else if (description.contains("mist")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.mist);
                        
                    } else if (description.contains("sunny")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.sunny);
                    }
                    else if (description.contains("clear sky")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.sunny);
                    }
                    else if (description.contains("haze")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.haze);

                    }

                    else if (description.contains("snow")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.snow);

                    }

                    else if (description.equals("haze")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.haze);

                    } else if (description.contains("thunderstorm")) {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.thubder);
                    }else {
                        View activityview = getWindow().getDecorView();
                        activityview.setBackgroundResource(R.drawable.background_home);
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home_page_curr_condn.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}
