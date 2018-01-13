package com.handsomelee.gotroute;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.handsomelee.gotroute.Controller.MapsActivity;
import com.handsomelee.gotroute.Controller.SettingActivity;
import com.handsomelee.gotroute.Model.DeviceInfo;
import com.handsomelee.gotroute.Model.PlaceSearch;
import com.handsomelee.gotroute.Services.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * MainActivity.
 *
 * Attributes:
 *  public:
 *    RequestQueue queue
 *    ViewPager viewPager
 *    PagerAdapter pageAdapter
 *    LocationSystem locationSystem
 *
 * Methods:
 *  public:
 *    requestNavigation(View): boolean
 *    getLocationSystem(): LocationSystem
 *    hasLocationSystem(): boolean
 *    hasLocationSystem(): boolean
 *    createUserId(): void
 *
 *  private:
 *    requestLocationPermission(): void
 *
 * Dependency:
 *  MapsActivity
 *  RequestQueue
 *  LocationSystem
 *
 *  Inner Class:
 *    PagerAdapter
 *      Methods:
 *        public:
 *          getItem(int): Fragment
 *          getCount(): int
 */

public class MainActivity extends AppCompatActivity implements LocationSource.OnLocationChangedListener {
  

  public static RequestQueue queue;
  public static MapsActivity.DirectionType directionType = MapsActivity.DirectionType.Driving;
  public static android.app.FragmentManager mFragmentManager;
  public static List<LatLng> latLngs;
  public static PlaceSearch placeSearch;
  public static Activity mActivity;
  private static FusedLocationProviderClient mFusedLocationClient;
  private static Location mLastLocation;
  private static int GPS_REQUEST_CODE = 1;
  private static LocationSystem locationSystem;
  MapsActivity tab1;
  SettingActivity tab2;
  private ViewPager viewPager;
  private PagerAdapter pageAdapter;
  
  private TabLayout.OnTabSelectedListener tabLayoutOnTabListener = new TabLayout.OnTabSelectedListener() {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
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

  public static Boolean checkGPSPermission(Context context) {
    return ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context
            , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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

  public static boolean requestNavigation(final View v) {
    (v).setClickable(false);
    if (MapsActivity.getProgressType() == MapsActivity.ProgressType.Free || ((Button) v).getText().equals("")) {
      MapsActivity.setProgressType(MapsActivity.ProgressType.Navigation);
      try {
        final String locString = locationSystem.getLatitude() + "," + locationSystem.getLongitude();
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

        final FragmentActivity activity = (FragmentActivity) mActivity;


        // Your Position
        if ((locationSystem.getLatitude() + "," + locationSystem.getLongitude()).equals(locString)) {
          MapsActivity.origin.setText("Your Position");
        } else {
          MapsActivity.origin.setText(locString);
        }

        MapsActivity.requestDirection(locString, destination, directionType);

        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... voids) {
            while (!DatabaseConnect.getNavigationStatus()) {
              if (!DatabaseConnect.directionStatus) {
                ((Button) v).setClickable(true);
                Log.v("error", "d");
                return null;
              }
            }
            
            DatabaseConnect.setNavigationStatus(false);
            mActivity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Log.v("RUN", "lat");
                MapsActivity.drawPolyLines(activity, latLngs);
                MapsActivity.showOriginAndDestination();
                ((Button) v).setClickable(true);
                ((Button) v).setText("cancel");
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(locationSystem.getLatLng())
                        .zoom(20f)
                        .tilt(80f)
                        .build();
                MapsActivity.getmMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                MapsActivity.showListView();
              }
            });
            return null;
          }
        }.execute();
      } catch (Exception e) {
        Log.e("Exception", "Request Navigation Failed!\n" + e.toString());
        MapsActivity.setProgressType(MapsActivity.ProgressType.Free);
        return false;
      }
    } else {
      MapsActivity.hideOriginAndDestination();
      MapsActivity.removePolyline();
      ((Button) v).setText("navigation");
      MapsActivity.closeListView();
      MapsActivity.setProgressType(MapsActivity.ProgressType.Free);
      CameraPosition cameraPosition = new CameraPosition.Builder()
              .target(locationSystem.getLatLng())
              .zoom(15f)
              .build();
      MapsActivity.getmMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    return true;
  }

  public static LocationSystem getLocationSystem() {
    if(locationSystem == null) {
      locationSystem = new LocationSystem(MainActivity.mActivity);
    }
    return locationSystem;
  }

  public static boolean hasLocationSystem() {
    return locationSystem != null;
  }

  public static int getWidth() {
    return DeviceInfo.getInstance().getScreenWidth();
  }

  public static int calculateWidth(double divide) {
    return (int) (Math.ceil(DeviceInfo.getInstance().getScreenWidth() / divide));
  }

  public static int calculateHeight(double divide) {
    return (int) (Math.ceil(DeviceInfo.getInstance().getScreenHeight() / divide));
  }

  public static int getHeight() {
    return DeviceInfo.getInstance().getScreenHeight();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = this;
    if (mFragmentManager == null) {
      mFragmentManager = getFragmentManager();
    }
    createUserId();

    setContentView(R.layout.activity_main);
    queue = Volley.newRequestQueue(this);
    pageAdapter = new PagerAdapter(getSupportFragmentManager());
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    TabLayout tab = (TabLayout) findViewById(R.id.tabLayout);
    tab.addOnTabSelectedListener(tabLayoutOnTabListener);
    viewPager.setAdapter(pageAdapter);
    viewPager.setCurrentItem(0);
    ViewTreeObserver treeObserver = viewPager.getViewTreeObserver();
    if (treeObserver.isAlive()) {
      treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          DeviceInfo.getInstance().setScreenHeight(viewPager.getHeight());
          DeviceInfo.getInstance().setScreenWidth(viewPager.getWidth());
          viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      });
    }
    checkGPSEnabled();
    requestLocationPermissions();
    CheckLocationService.start(this);
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.v("location", location.getLatitude() + "");
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

  public void createUserId() {
    final LocalDatabase database = new LocalDatabase(this);
    Cursor cursor = database.getData();
    Log.v("here", "asd");

    if (cursor.moveToNext()) {
      Log.v("here", "asd1");
      DateFormat df = new SimpleDateFormat("E MMM DD HH:mm:ss ZZZZZZZZZ yyyy");
      DeviceInfo.getInstance().setId(cursor.getString(0));
      DeviceInfo.getInstance().setRefreshTime(Long.parseLong(cursor.getString(2)));
      try {
        DeviceInfo.getInstance().setDate(df.parse(cursor.getString(1)));
        Log.v("device", DeviceInfo.getInstance().getDate().toString());

      } catch (ParseException e) {
        Log.e("Parse Exception", e.toString());
      } catch (Exception e) {
        Log.e("Exception", e.toString());
      }
      MapsActivity.updateRefreshSecond();
      Log.v("device", DeviceInfo.getInstance().getId());
      Log.v("device", DeviceInfo.getInstance().getRefreshTime() + "");

    }
  }

  private void requestLocationPermissions() {
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    if (!checkGPSPermission(this)) {
      ActivityCompat.requestPermissions(this, permissions, GPS_REQUEST_CODE);
      return;
    }
    Log.v("Logger", "Logger");
    MapsActivity.EnableMyLocationSetting(this);
    locationSystem = new LocationSystem(this);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == GPS_REQUEST_CODE && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
            && permissions[1].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      MapsActivity.EnableMyLocationSetting(this);
      locationSystem = new LocationSystem(this);
      Log.v("GPS", "GPS is Activated");
//      return;
    } else {
      Log.v("GPS", "Failed to Activated");
      requestLocationPermissions();
    }
  }

  public void listViewBtn(View v) {
    if (((Button) v).getText().equals("^")) {
      MapsActivity.showListView();
    } else {
      MapsActivity.hideListView();
    }
  }

  public void parkingBtn(View v) {
    if (!RequestHandler.parkingStatus) {
      RequestHandler.requestPlaceSearch(MapsActivity.getmMap().getCameraPosition().target, "", "parking");

      Log.v("true", "");
    } else {
      Log.v("false", "");
      MapsActivity.removeParkingMarker();
      RequestHandler.parkingStatus = false;
    }
  }

  public static void notificationMessage(String title, String contentText) {
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.mActivity, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher_round))
            .setContentTitle(title)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setContentText(contentText);
    final NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.mActivity);
    managerCompat.notify(20, notificationBuilder.build());
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        managerCompat.cancel(20);
      }
    }, 5000L);
  }

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
            tab2 = new SettingActivity();
          return tab2;

        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}


