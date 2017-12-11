package com.handsomelee.gotroute.Model;

import java.util.List;

public class RouteInfo {
  public String distance;
  public String duration;
  public List<RouteDetail> routeDetails;
  
  public RouteInfo(String distance, String duration, List<RouteDetail> routeDetails) {
    this.distance = distance;
    this.duration = duration;
    this.routeDetails = routeDetails;
  }
  
  public static class RouteDetail {
    public String duration;
    public String distance;
    public String html_instructions;
    public String travel_mode;
    public String maneuver;
  
    public RouteDetail(String duration, String distance, String html_instructions, String travel_mode, String maneuver) {
      this.duration = duration;
      this.distance = distance;
      this.html_instructions = html_instructions;
      this.travel_mode = travel_mode;
      this.maneuver = maneuver;
    }
  }
  
}
