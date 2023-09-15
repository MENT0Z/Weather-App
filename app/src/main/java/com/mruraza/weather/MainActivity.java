package com.mruraza.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText cityname;
    Button donebutton;
    TextView TVforres,temperatureshpw;
    private final String apkid = "95d14b9dc65d9a290ed3d27f2a430672";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        cityname = findViewById(R.id.cityname);
        donebutton = findViewById(R.id.donebotton);
        TVforres = findViewById(R.id.resulttv);
        temperatureshpw = findViewById(R.id.tempidinmain);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    String city = cityname.getText().toString().trim();
                    if (city.equals("")) {
                        cityname.setError("This Field Can't be empty");
                    } else {

                        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apkid;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                              //  String outputt = "";
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                                    JSONObject jsonObjectweather = jsonArray.getJSONObject(0);
                                    String description = jsonObjectweather.getString("description");

                                    JSONObject jsonobjmain = jsonObject.getJSONObject("main");
                                    double temperature = jsonobjmain.getDouble("temp") - 273.15;
                                    double feels_like = jsonobjmain.getDouble("feels_like")-273.15;
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
                                            +"\ncondition = "+description;


                                    TVforres.setText(output);

                                    temperatureshpw.setText(formattedTemperature);

                                    if (description.contains("cloud")
                                    ) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.cloudy);

                                    } else if (description.contains("raining")|| description.contains("rain")|| description.contains("drizzle") ) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.rasinny);
                                    } else if (description.contains("mist")) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.mist);

                                    }
                                    else if (description.contains("snow")) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.snow);

                                    }

                                    else if (description.contains("sunny")) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.sunny);
                                    } else if (description.contains("thunderstorm")) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.thubder);
                                    }

                                    else if (description.contains("clear sky")) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.sunny);
                                    }
                                    else if (description.contains("haze")) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.haze);

                                    } else if (description.equals("haze")) {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.haze);

                                    }

                                    else {
                                        View activityview = getWindow().getDecorView();
                                        activityview.setBackgroundResource(R.drawable.background1);
                                    }


                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(request);


                }
            }
        });

    }



}