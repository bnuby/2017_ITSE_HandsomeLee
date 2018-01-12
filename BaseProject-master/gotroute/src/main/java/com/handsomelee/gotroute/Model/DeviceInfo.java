package com.handsomelee.gotroute.Model;

import java.util.Date;

public class DeviceInfo {
  
  private static DeviceInfo _instance;
  private int screenWidth;
  private int screenHeight;
  private String id;
  private Date date;
  private Long refreshTime;

  public static DeviceInfo getInstance() {
    if (_instance == null) {
      _instance = new DeviceInfo();
    }
    return _instance;
  }
  
  public boolean hasScreenWidth() {
    return (screenWidth != 0);
  }
  
  public boolean hasScreenHeight() {
    return (screenHeight != 0);
  }
  
  
  // screenWidth Getter Method
  public int getScreenWidth() {
    return screenWidth;
  }

  // ScreenWidth Setter Method
  public void setScreenWidth(int screenWidth) {
    this.screenWidth = screenWidth;
  }

  // screenHeight Getter Method
  public int getScreenHeight() {
    return screenHeight;
  }

  // ScreenHeight Setter Method
  public void setScreenHeight(int screenHeight) {
    this.screenHeight = screenHeight;
  }
  
  // id Getter Method
  public String getId() {
    return id;
  }
  
  // Id Setter Method
  public void setId(String id) {
    this.id = id;
  }
  
  // date Getter Method
  public Date getDate() {
    return date;
  }
  
  // Date Setter Method
  public void setDate(Date date) {
    this.date = date;
  }
  
  // refreshTime Getter Method
  public Long getRefreshTime() {
    return refreshTime;
  }
  
  // RefreshTime Setter Method
  public void setRefreshTime(long refreshTime) {
    this.refreshTime = refreshTime;
  }
}
