package com.handsomelee.gotroute.Model;

public class SnapToRoad {
  public SnapPoint[] snappedPoints;
  
  
  public static class SnapPoint {
    public Report.Location location;
    public int originalIndex;
    public String placeId;
  }
}
