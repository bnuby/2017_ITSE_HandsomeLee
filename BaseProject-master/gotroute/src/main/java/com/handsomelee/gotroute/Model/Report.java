package com.handsomelee.gotroute.Model;


public class Report {
  private String dateTime;
  private String reportType;
  private String comment;
  private Location location;
  
  public Report(String dateTime, String reportType, String comment, Location location) {
    this.dateTime = dateTime;
    this.reportType = reportType;
    this.comment = comment;
    this.location = location;
  }
  
  // dateTime Getter Method
  public String getDateTime() {
    return dateTime;
  }
  
  // reportType Getter Method
  public String getReportType() {
    return reportType;
  }
  
  // comment Getter Method
  public String getComment() {
    return comment;
  }
  
  // location Getter Method
  public Location getLocation() {
    return location;
  }
  
  public static class Location {
    private double latitude;
    private double longtitude;
    
    public Location(double latitude, double longtitude) {
      this.latitude = latitude;
      this.longtitude = longtitude;
    }
    
    // latitude Getter Method
    public double getLatitude() {
      return latitude;
    }
    
    // longtitude Getter Method
    public double getLongtitude() {
      return longtitude;
    }
  }
}
