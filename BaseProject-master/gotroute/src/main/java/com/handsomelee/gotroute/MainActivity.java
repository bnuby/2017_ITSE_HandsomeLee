package com.handsomelee.gotroute;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class MainActivity extends AppCompatActivity {
  
  private ViewPager viewPager;
  
  private PagerAdapter pageAdapter;

  private TabLayout.OnTabSelectedListener tabLayoutOnTabListener = new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      viewPager.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) { }
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
    
    
  }
  
  public class PagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
      super(fm);
    }
  
    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          MapsActivity tab1 = new MapsActivity();
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
  
  
}
