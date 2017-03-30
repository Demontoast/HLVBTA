package com.example.damentomassi.astraeademo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private static TextView adTitleTextView;
    private static TextView adContentTextView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("This is the main method, hello!");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        adTitleTextView = (TextView) findViewById(R.id.adTitle);
        adContentTextView = (TextView) findViewById(R.id.adDetail);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                textView.setText("\n LAT: " + location.getLatitude() + " LONG: " + location.getLongitude());
                buildLocationPackage(location);
                System.out.println("Hello2");
                String buildAddressForServer = "http://ec2-34-204-100-174.compute-1.amazonaws.com/locations/";
                buildAddressForServer += location.getLatitude();
                buildAddressForServer += "/" + location.getLongitude();
                //buildAddressForServer += "/" + location.getSpeed();
                buildAddressForServer += "/30";
                System.out.println("Query address: " + buildAddressForServer);
                getAdFromServer(buildAddressForServer);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);

            return;
        } else {
            configureButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureButton();
                }
                return;
        }
    }

    private void configureButton() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });
    }

    private void buildLocationPackage(Location location)
    {
        try {
            String jsonString = new JSONObject()
                    .put("Lat", location.getLatitude())
                    .put("Long", location.getLongitude())
                    .put("Speed", location.getSpeed())
                    .toString();
            System.out.println(jsonString);
        }
        catch(JSONException e)
        {
            System.out.println("CRAP");
        }
    }

    public static void getAdFromServer(String address)
    {
        System.out.println("I got called!");
        System.out.println(address);
        try {
            String str = address;
          //  str += URLEncoder.encode(str, "UTF-8");
            URL url = new URL(str);

            //read from the url
            Scanner scan = new Scanner(url.openStream());
            String serverResponse = new String();

            while (scan.hasNext()) {
                serverResponse += scan.nextLine();
            }
            scan.close();
            System.out.println(serverResponse);

            String adTitle = "";
            String adContent = "";

            JSONArray jsonarray = new JSONArray(serverResponse);
            for (int i = 0; i < jsonarray.length(); i++)
            {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                 adTitle = jsonobject.getString("title");
                 adContent = jsonobject.getString("description");
            }

            adTitleTextView.setText(adTitle);
            adContentTextView.setText(adContent);
           // JsonObject jsonObject = new JsonParser().parse(serverResponse).getAsJsonObject();
           // System.out.println("Title: " + jsonObject.get("title").getAsString());

        }
        catch (Exception ex)
        {
            System.out.println("It broke!");
            System.out.println(ex);
            adTitleTextView.setText("Waiting for an ad...");
            adContentTextView.setText("Please wait for an ad!");
        }
    }
}
