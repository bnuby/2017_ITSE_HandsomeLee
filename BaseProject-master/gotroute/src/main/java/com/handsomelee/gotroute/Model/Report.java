package com.handsomelee.gotroute.Model;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;
import org.json.JSONException;
import org.json.JSONObject;

public class Report {
  private JsonElement no;
  private String dateTime;
  private String reportType;
  private String comment;
  private Location location;
  private String user;
  
  public Report(String dateTime, String reportType, String comment, Location location) {
    this.dateTime = dateTime;
    this.reportType = reportType;
    this.comment = comment;
    this.location = location;
  }
  
  public static Location ConvertLocation(android.location.Location location) {
    return new Location(location.getLatitude(), location.getLongitude());
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
  
  public static class fetchReport {
    public JsonElement no;
    public String time;
    public String comment;
    public String type;
    public String user;
    private String location;
    
    public LatLng getLatLng() {
      try {
        JSONObject location = new JSONObject(this.location);
        return new LatLng(location.getDouble("latitude"), location.getDouble("longitude"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return null;
    }
  }
  
  public static class Location {
    private double latitude;
    private double longitude;
    
    public Location(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
    }
    
    // latitude Getter Method
    public double getLatitude() {
      return latitude;
    }
    
    // longtitude Getter Method
    public double getLongitude() {
      return longitude;
    }
    
    public LatLng getLatLng() {
      return new LatLng(latitude, longitude);
    }
    
    
  }
}
