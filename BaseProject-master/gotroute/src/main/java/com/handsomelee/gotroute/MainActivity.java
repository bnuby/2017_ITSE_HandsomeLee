package com.handsomelee.gotroute;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.handsomelee.gotroute.Controller.CarParkingActivity;
import com.handsomelee.gotroute.Controller.MapsActivity;
import com.handsomelee.gotroute.Controller.ReportActivity;
import com.handsomelee.gotroute.Model.Report;
import com.handsomelee.gotroute.Services.LocationSystem;
import com.handsomelee.gotroute.Services.RequestDirection;
import org.json.JSONException;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
  
  private ViewPager viewPager;
  
  private PagerAdapter pageAdapter;
  
  private static FusedLocationProviderClient mFusedLocationClient;
  
  private static Location mLastLocation;
  
  private static int GPS_REQUEST_CODE = 1;
  
  private static LocationSystem locationSystem;
  
  public static RequestQueue queue;
  
  public static android.app.FragmentManager mFragmentManager;
  
  public static List<LatLng> latLngs;
  
  private TabLayout.OnTabSelectedListener tabLayoutOnTabListener = new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      viewPager.setCurrentItem(tab.getPosition(), true);
    }
    
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }
    
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
  };
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (mFragmentManager == null) {
      mFragmentManager = getFragmentManager();
    }
    setContentView(R.layout.activity_main);
    queue = Volley.newRequestQueue(this);
    pageAdapter = new PagerAdapter(getSupportFragmentManager());
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    TabLayout tab = (TabLayout) findViewById(R.id.tabLayout);
    tab.addOnTabSelectedListener(tabLayoutOnTabListener);
    viewPager.setAdapter(pageAdapter);
    viewPager.setCurrentItem(0);
    checkGPSEnabled();
//    AccessLastKnownLocation();
    
    requestLocationPermissions();
    locationSystem = new LocationSystem(this);
    Log.v("latitude", locationSystem.getLatitude() + "");
    
  }
  
  MapsActivity tab1;
  CarParkingActivity tab2;
  ReportActivity tab3;
  
  
  public class PagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
      super(fm);
    }
    
    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          if (tab1 == null) {
            tab1 = new MapsActivity(R.id.mapView, R.layout.activity_maps, GoogleMap.MAP_TYPE_NORMAL);
          }
          return tab1;
        
        case 1:
          if (tab2 == null)
            tab2 = new CarParkingActivity();
          return tab2;
        
        case 2:
          if (tab3 == null)
            tab3 = new ReportActivity();
          return tab3;
        
        default:
          return null;
      }
      
    }
    
    @Override
    public int getCount() {
      return 3;
    }
  }
  
  public static Boolean checkGPSPermission(Context context) {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
  }
  
  private void checkGPSEnabled() {
    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      Log.v("GPS Provider", "Is Disabled");
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
      alertBuilder.setMessage("Your Gps is Disabled!\nPlease turn it on\n").setTitle("GPS Error")
              .setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                  startActivity(myIntent);
                }
              })
              .setNegativeButton(R.string.cast_tracks_chooser_dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  finish();
                }
              });
      AlertDialog alert = alertBuilder.create();
      alert.show();
    }
  }
  
  private void requestLocationPermissions() {
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    if (!checkGPSPermission(this)) {
      ActivityCompat.requestPermissions(this, permissions, GPS_REQUEST_CODE);
      return;
    }
    Log.v("Logger", "Logger");
    MapsActivity.EnableMyLocationSetting(1000);
    
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == GPS_REQUEST_CODE && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
            && permissions[1].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      MapsActivity.EnableMyLocationSetting(100);
      Log.v("GPS", "GPS is Activated");
//      return;
    } else {
      Log.v("GPS", "Failed to Activated");
      requestLocationPermissions();
    }
  }
  
  public void sendReport(View v) throws JSONException {
    String dateTime = DateFormat.getDateTimeInstance().format(new Date());
    final Report report = new Report(dateTime, ReportActivity.reportType, ReportActivity.comment, new Report.Location(locationSystem.getLatitude(), locationSystem.getLongtitude()));
    Toast.makeText(this, "location : " + locationSystem.getLatitude() + ", " + locationSystem.getLongtitude()
            + "\nDate : " + dateTime + "\nReport type : " + ReportActivity.reportType + "\ncomment : " + ReportActivity.comment, Toast.LENGTH_LONG).show();
    
    int secret = 12345;
    String url = "https://stitch.mongodb.com/api/client/v2.0/app/handsomelee-bxznj/service/findCollection/incoming_webhook/reportInsert?secret=" + secret;
    StringRequest objectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        Log.v("Success", s.toString());
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.v("Failed", volleyError.toString());
        
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, Double> location = new HashMap<>();
        location.put("latitude", locationSystem.getLatitude());
        location.put("longtitude", locationSystem.getLongtitude());
        Map<String, String> map = new HashMap<>();
        map.put("location", location.toString());
        map.put("time", report.getDateTime());
        map.put("type", report.getReportType());
        map.put("comment", report.getComment());
        return map;
      }
    };
    queue.add(objectRequest);
  }
  
  public static Boolean removeFragment(int id) {
    try {
      android.app.FragmentManager fm = mFragmentManager;
      android.app.Fragment fragment = fm.findFragmentById(id);
      android.app.FragmentTransaction ft = fm.beginTransaction();
      ft.remove(fragment);
      ft.commit();
    } catch (Exception e) {
      return false;
    }
    
    return true;
  }
  
  public boolean requestNavigation(final View v) {
    if (((Button) v).getText().equals("navigation")) {
      try {
        final String locString = locationSystem.getLatitude() + "," + locationSystem.getLongtitude();
        String destination = "";
        Log.v("mode", MapsActivity.getMarkerType().toString());
        switch (MapsActivity.getMarkerType()) {
          case Name:
            MapsActivity.destination.setText(MapsActivity.getDestinationString());
            destination = MapsActivity.getDestinationString().replace(' ', '+');
            break;
          case LatLng:
            destination = MapsActivity.getLatLng().latitude + "," + MapsActivity.getLatLng().longitude;
            MapsActivity.destination.setText(String.format("%.4f, %.4f", Double.parseDouble(destination.split(",")[0]), Double.parseDouble(destination.split(",")[1])));
            break;
          case Address:
            break;
        }
        Log.v("origin", locString);
        Log.v("LatLng", destination);
        
        final FragmentActivity activity = this;
        
        
        // Your Position
        if ((locationSystem.getLatitude() + "," + locationSystem.getLongtitude()).equals(locString)) {
          MapsActivity.origin.setText("Your Position");
        } else {
          MapsActivity.origin.setText(locString);
        }
        MapsActivity.showOriginAndDestination();
        
        RequestDirection.requestDirection(this, RequestDirection.DirectionType.Walking, locString, destination);
        
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... voids) {
            while (latLngs == null || latLngs.size() == 0) {
              if (!RequestDirection.status) {
                Log.v("error", "d");
                return null;
              }
            }
            ;
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Log.v("PollyLatLong", "lat=" + latLngs);
                
                Log.v("RUN", "lat");
                MapsActivity.drawPolyLines(activity, latLngs);
                ((Button) v).setText("cancel");
              }
            });
            return null;
          }
        }.execute();
      } catch (Exception e) {
        Log.e("Exception", "Request Navigation Failed!\n" + e.toString());
        return false;
      }
    } else {
      MapsActivity.hideOriginAndDestination();
      MapsActivity.removePolyline();
      ((Button) v).setText("navigation");
    }
    
    return true;
  }
  
  public static Location getLocation() {
    return locationSystem.getLocation();
  }
  
}
