package com.handsomelee.gotroute.Model;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Report.
 *
 * Attributes:
 *  private:
 *    JsonElement no
 *    String dateTime
 *    String reportType
 *    String comment
 *    Location location
 *    String user
 *
 * Methods:
 *  public:
 *    Report(String, String, String, Location)
 *    ConvertLocation(Location): Location
 *    getDateTime(): String
 *    getReportType(): String
 *    getComment(): String
 *    getLocation(): Location
 *
 * Inner Class:
 *  Attributes:
 *    public:
 *      JsonElement no
 *      String time
 *      String comment
 *      String type
 *
 *    private:
 *      String location
 *
 *  Methods:
 *    public getLatLng(): LatLng
 *
 *  Associate:
 *    android
 *
 */

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
  
  
}
