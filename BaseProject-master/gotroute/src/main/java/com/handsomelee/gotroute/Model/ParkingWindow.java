package com.handsomelee.gotroute.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.R;
import com.handsomelee.gotroute.Services.DatabaseConnect;

public class ParkingWindow implements GoogleMap.InfoWindowAdapter {
  private Activity context;
  
  public ParkingWindow(Activity context) {
    this.context = context;
  }
  
  @Override
  public View getInfoWindow(Marker marker) {
    return null;
  }
  
  @Override
  public View getInfoContents(Marker marker) {
    View view = context.getLayoutInflater().inflate(R.layout.parking_window, null);
    final TextView windowTitle = view.findViewById(R.id.carParkName);
    TextView isOpen = view.findViewById(R.id.isOpen);
    final TextView available = view.findViewById(R.id.available);
    String[] array = marker.getSnippet().split(",");
    switch (Integer.valueOf(array[0])) {
      case 1:
        isOpen.setText("YES");
        isOpen.setTextColor(Color.GREEN);
        break;
      case 0:
        isOpen.setText("-");
        isOpen.setTextColor(Color.RED);
        break;
      case -1:
        isOpen.setText("NO");
        isOpen.setTextColor(Color.RED);
        break;
    }
    windowTitle.setText(marker.getTitle());
    available.setText(array[1]);
    return view;
  }
  
  public static void updateButton(final CharSequence title, CharSequence numberOfAvailable, final Marker marker) {
    final AlertDialog updateDialog;
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.mActivity);
    final View mView = MainActivity.mActivity.getLayoutInflater().inflate(R.layout.update_carparking_dialog, null);
    TextView titleTv = mView.findViewById(R.id.updateTitle);
    final EditText availableEt = mView.findViewById(R.id.availableNumberET);
    Button updateBtn = mView.findViewById(R.id.updateCarParkBtn);
    Button cancelBtn = mView.findViewById(R.id.cancelUpdateBtn);
    titleTv.setText(title);
    availableEt.setText(numberOfAvailable);
    
    
    mBuilder.setView(mView);
    updateDialog = mBuilder.create();
    updateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!availableEt.getText().toString().equals("")) {
          try {
            int number = Integer.parseInt(availableEt.getText().toString());
            Log.v("number", number + "");
            String snippet = marker.getSnippet();
            Log.v("snippet", snippet);
            marker.setSnippet(snippet.split(",")[0].concat("," + number));
            Log.v("snippet", marker.getSnippet());
            DatabaseConnect.updateCarParking(title.toString(), String.valueOf(number));
            updateDialog.dismiss();
          } catch (Exception parser) {
            Toast.makeText(MainActivity.mActivity, "Invalid Number", Toast.LENGTH_SHORT).show();
          }
        } else {
          Toast.makeText(MainActivity.mActivity, "Cannot Be Null", Toast.LENGTH_SHORT).show();
        }
      }
    });
    cancelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        updateDialog.dismiss();
      }
    });
    
    updateDialog.show();
  }
  
}
