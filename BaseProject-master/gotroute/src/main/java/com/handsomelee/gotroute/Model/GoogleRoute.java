package com.handsomelee.gotroute.Model;

import com.google.gson.JsonObject;

public class GoogleRoute {
  public JsonObject distance;
  public JsonObject duration;
  public String end_address;
  public JsonObject end_location;
  public String start_address;
  public JsonObject start_location;
  public Route[] steps;

  
  public static class Route{
    public JsonObject distance;
    public JsonObject duration;
    public JsonObject end_location;
    public String html_instructions;
    public String maneuver;
    public JsonObject polyline;
    public JsonObject start_location;
    public String travel_mode;
    public Route[] steps;
  }
}
