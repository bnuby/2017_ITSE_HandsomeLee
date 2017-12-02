package com.handsomelee.gotroute;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class LocationSystem extends Activity {
  
  private LocationManager locationManager;
  
  private boolean isLocationEnabled() {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  }
  
  private void showAlert() {
    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Enable Location").setMessage("Your Locations Setting is set to 'Off'.\nPlease Enable Location to" +
            "use this app").setPositiveButton("Location Setting", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(myIntent);
      }
    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
      
      }
    });
    dialog.show();
  }
}
