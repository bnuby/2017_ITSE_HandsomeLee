package com.handsomelee.gotroute.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.Location;

/**
 * CheckLocationService.
 * Attributes:
 *  Default (~):
 *    LocationSystem locationSystem
 *    Location location
 *    Long duration
 *    Long UPDATE_DURATION
 * Methods:
 *  public:
 *    initialize(): void
 *    checkLocation(): boolean
 *
 * Dependency:
 *  DatabaseConnect
 *
 * Composite:
 *  LocationSystem
 *  Location
 *
 */

public class CheckLocationService extends Service {

  static LocationSystem locationSystem;
  static Location location;
  static Long duration;
  final Long UPDATED_DURATION = 2 * 60 * 1000L;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  public static void start(Context context) {
    context.startService(new Intent(context, CheckLocationService.class));
  }

  public static void stop(Context context) {
    context.stopService(new Intent(context, CheckLocationService.class));
  }

  @Override
  public void onCreate() {
    super.onCreate();
    duration = 0L;
    runHandler();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    return START_STICKY;
  }

  public void runHandler() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        initialize();
        checkLocation();
        runHandler();
      }
    }, UPDATED_DURATION);
  }

  private void initialize() {
    duration = 0L;
    if (locationSystem == null || location == null) {
      locationSystem = MainActivity.getLocationSystem();
      if(locationSystem != null)
        location = new Location(locationSystem.getLatitude(), locationSystem.getLongitude());
    }
  }

  public boolean checkLocation() {

    if (location != null) {
      if (location.equals(locationSystem.getCurrentLocation())) {
        duration += UPDATED_DURATION;
        DatabaseConnect.updateUserLocation(duration, location);
        MainActivity.notificationMessage("You Aren't Moving In 2 Min", "Maybe the road is traffic jam");
        Log.v("Location", "Same");
      } else {
        duration = 0L;
        location.setLatLng(locationSystem.getCurrentLocation());
        DatabaseConnect.updateUserLocation(duration, location);
        Log.v("Location", "Not Same");
      }
      return true;
    }
    return false;
  }


}
