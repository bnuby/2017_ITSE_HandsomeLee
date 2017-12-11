package com.handsomelee.gotroute.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.DeviceInfo;
import com.handsomelee.gotroute.R;
import com.handsomelee.gotroute.Services.LocalDatabase;


public class SettingActivity extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.activity_setting, container, false);
  
    
    final EditText refreshEt = rootView.findViewById(R.id.refreshET);
    refreshEt.setText(DeviceInfo.getInstance().getRefreshTime().toString());
    refreshEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean b) {
        if(!b) {
          long temp = Long.parseLong(((EditText)view).getText().toString());
                  if(temp < 30) {
                    Toast.makeText(MainActivity.mActivity, "Please insert not smaller than 30.", Toast.LENGTH_LONG).show();
                  } else {
                    LocalDatabase database = new LocalDatabase(MainActivity.mActivity);
                    DeviceInfo.getInstance().setRefreshTime(temp);
                    MapsActivity.updateRefreshSecond();
                    database.updateRefreshTime(temp);
                    Toast.makeText(MainActivity.mActivity, "Apply Success", Toast.LENGTH_LONG).show();
                  }
        }
      }
    });
    
    rootView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        refreshEt.clearFocus();
      }
    });

    
    return rootView;
  }
  
  
}
