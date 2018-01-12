package com.handsomelee.gotroute;

import com.handsomelee.gotroute.Model.DeviceInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


public class MainActivityTest {
  
  @Before
  public void init() {
    
    DeviceInfo.getInstance();
    DeviceInfo.getInstance().setScreenWidth(200);
    DeviceInfo.getInstance().setScreenHeight(200);
  }
  
  @Test
  public void calculateWidth() {
    
    assertEquals(4, MainActivity.calculateWidth(50));
  }
  
  @Test
  public void calculateHeight() {
    assertEquals(4, MainActivity.calculateWidth(50));
  }
  
  @Test
  public void getWidth() {
    assertEquals(200, MainActivity.getWidth());
  }
  
  @Test
  public void getHeight() {
    assertEquals(200, MainActivity.getHeight());
  }
  
  @Test
  public void getLocationSystem() {
    MainActivity.getLocationSystem()
  }
  
  @Test
  public void hasLocationSystem() {
  }
  
  @Test
  public void notificationMessage() {
  }
}