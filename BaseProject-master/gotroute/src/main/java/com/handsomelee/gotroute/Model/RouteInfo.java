package com.handsomelee.gotroute.Model;

import android.util.Log;
import com.google.gson.JsonObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * RouteInfo.
 *
 * Attributes:
 *  public:
 *    RouteDetail[] steps
 *
 *  private:
 *    JsonObject distance
 *    JsonObject duration
 *    JsonObject arrival_time
 *    JsonObject departure_time
 *
 * Methods:
 *  public getDistance(): String
 *  public getDuration() String
 *
 * Inner Class:
 *  RouteDetail:
 *    private:
 *      JsonObject duration
 *      JsonObject distance
 *      String instruction
 *      Transit transit
 *
 *  Methods:
 *    public:
 *      getDistance(): String
 *      getDuration(): String
 *      getInsturctions(): String
 *      getTransit(): Transit
 *
 *
 *  Transit:
 *    Attributes:
 *      public:
 *        JsonObject arrival_stop;
 *        JsonObject arrival_time;
 *        JsonObject departure_stop;
 *        JsonObject departure_time;
 *        JsonObject line;
 *        JsonObject num_stops;
 *
 *   Methods:
 *    public:
 *      getArrivalStop(): String
 *      getArrivalTime(): Date
 *      getDepartureStop(): String
 *      getDepartureTime(): Date
 *      getBusNo(): String
 *      getBusName(): String
 *
 */

public class RouteInfo {
  public RouteDetail[] steps;
  private JsonObject distance;
  private JsonObject duration;
  private JsonObject arrival_time;
  private JsonObject departure_time;
  
  public String getDistance() {
    return distance.get("text").getAsString();
  }
  
  public String getDuration() {
    return duration.get("text").getAsString();
  }
  
  public Date getArrivalTime() {
    SimpleDateFormat df = new SimpleDateFormat("HH:mmZZ");
    try {
      return df.parse(arrival_time.get("text").getAsString());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public Date getDepartureTime() {
    SimpleDateFormat df = new SimpleDateFormat("HH:mmZZ");
    try {
      return df.parse(departure_time.get("text").getAsString());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  
  public static class RouteDetail {
    private JsonObject duration;
    private JsonObject distance;
    private String instructions;
    private String travel_mode;
    private Transit transit;
    public RouteDetail[] steps;
    
    
    public String getDistance() {
      return distance.get("text").getAsString();
    }
    
    public String getDuration() {
      return duration.get("text").getAsString();
    }
    
    // instructions Getter Method
    public String getInstructions() {
      return instructions;
    }
    
    public String getTravelMode() {
      return travel_mode;
    }
    
    // transit Getter Method
    public Transit getTransit() {
      return transit;
//      return null;
    }
  }
  
  public static class Transit {
    private JsonObject arrival_stop;
    private JsonObject arrival_time;
    private JsonObject departure_stop;
    private JsonObject departure_time;
    private JsonObject line;
    private int num_stops;
    
    
    public String getArrivalStop() {
      return arrival_stop.get("name").getAsString();
    }
    
    public String getDepartureStop() {
      return departure_stop.get("name").getAsString();
    }
    
    public Date getArrivalTime() {
      SimpleDateFormat df = new SimpleDateFormat("HH:mmZZ");
      try {
        return df.parse(arrival_time.get("text").getAsString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }
    
    public Date getDepartureTime() {
      SimpleDateFormat df = new SimpleDateFormat("HH:mmZZ");
      try {
        return df.parse(departure_time.get("text").getAsString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }
    
    public String getBusName() {
      return line.get("name").getAsString();
    }
    
    public String getBusNo() {
      Log.v("shortname", line.get("short_name").toString());
      return line.get("short_name").getAsString();
    }
    
    public String getIconUrl() {
      return line.getAsJsonObject("vehicle").get("icon").getAsString();
    }
    
    public int getNumberOfStop() {
      return num_stops;
    }
    
    
  }
  
}
