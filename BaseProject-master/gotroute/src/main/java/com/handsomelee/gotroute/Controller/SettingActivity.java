package com.handsomelee.gotroute.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    final Button applyButton = rootView.findViewById(R.id.ApplyButton);
    final EditText refreshEt = rootView.findViewById(R.id.refreshET);
    
    refreshEt.setText(DeviceInfo.getInstance().getRefreshTime().toString());
    
    long second = Long.parseLong(refreshEt.getText().toString());
    applyButton.setOnClickListener(new applyAction(second));
    
    return rootView;
  }
  
  class applyAction implements View.OnClickListener {
    long second;
    
    public applyAction(long second) {
      this.second = second;
    }
  
    @Override
    public void onClick(View view) {
      if (second < 30) {
            Toast.makeText(MainActivity.mActivity, "Please insert not smaller than 30.", Toast.LENGTH_LONG).show();
          } else {
            LocalDatabase database = new LocalDatabase(MainActivity.mActivity);
            DeviceInfo.getInstance().setRefreshTime(second);
            MapsActivity.updateRefreshSecond();
            database.updateRefreshTime(second);
            Toast.makeText(MainActivity.mActivity, "Apply Success", Toast.LENGTH_LONG).show();
          }
    }
  }
  
  
}
