package com.handsomelee.gotroute;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {
  
  private ViewPager viewPager;
  
  private PagerAdapter pageAdapter;
  
  private static FusedLocationProviderClient mFusedLocationClient;
  
  private static Location mLastLocation;
  
  private static int GPS_REQUEST_CODE = 1;

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
    setContentView(R.layout.activity_main);

    pageAdapter = new PagerAdapter(getSupportFragmentManager());
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    TabLayout tab = (TabLayout) findViewById(R.id.tabLayout);
    tab.addOnTabSelectedListener(tabLayoutOnTabListener);
    viewPager.setAdapter(pageAdapter);
    viewPager.setCurrentItem(0);
    checkGPSEnabled();
//    AccessLastKnownLocation();
    
    requestLocationPermissions();
    
  }
  
  public class PagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
      super(fm);
    }
    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          MapsActivity tab1 = new MapsActivity(R.id.mapView, R.layout.activity_maps, GoogleMap.MAP_TYPE_NORMAL);
          return tab1;
        
        case 1:
          CarParkingActivity tab2 = new CarParkingActivity();
          return tab2;
        
        case 2:
          ReportActivity tab3 = new ReportActivity();
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
    if (checkGPSPermission(this)) {
      ActivityCompat.requestPermissions(this, permissions, GPS_REQUEST_CODE);
      return;
    }
//    MapsActivity.EnableMyLocationSetting();
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == GPS_REQUEST_CODE && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
            && permissions[1].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//      MapsActivity.EnableMyLocationSetting();
      Log.v("GPS", "GPS is Activated");
//      return;
    } else {
      Log.v("GPS", "Failed to Activated");
      requestLocationPermissions();
    }
  }
  
  private void AccessLastKnownLocation() {
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
      @Override
      public void onSuccess(Location location) {
        if (location != null) {
          mLastLocation = location;
          Log.v("Gps", location.getLatitude() + "," + location.getLatitude());
        }
      }
    });
  }
  
  public static Location getLastKnownLocation() {
    if (mLastLocation != null) {
      return mLastLocation;
    }
    return null;
  }
  
  
}
