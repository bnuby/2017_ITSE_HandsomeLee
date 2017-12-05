package com.handsomelee.gotroute.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.handsomelee.gotroute.R;


public class CarParkingActivity extends Fragment {
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_car_parking, container, false);
        
        return rootView;
      }
  

}
