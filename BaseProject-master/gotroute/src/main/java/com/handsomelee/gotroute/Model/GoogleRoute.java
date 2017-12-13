package com.handsomelee.gotroute.Model;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handsomelee.gotroute.Controller.MapsActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// legs
public class GoogleRoute {
  public JsonObject distance;
  public JsonObject duration;
  public String end_address;
  public JsonObject end_location;
  public String start_address;
  public JsonObject start_location;
  public Route[] steps;
  
  public static void fetchLatLng(List<LatLng> latLngList, GoogleRoute.Route[] steps) {
    for (GoogleRoute.Route step : steps) {
      JsonObject start_location = step.start_location;
      latLngList.add(new LatLng(start_location.get("lat").getAsDouble(),
              start_location.get("lng").getAsDouble()));
      if (step.path != null && step.path.length > 0) {
        for (Path path : step.path) {
          latLngList.add(path.getLatLng());
        }
      }
      if (step.steps != null && step.steps.length > 0) {
        fetchLatLng(latLngList, step.steps);
      }
      JsonObject end_location = step.end_location;
      latLngList.add(new LatLng(end_location.get("lat").getAsDouble(),
              end_location.get("lng").getAsDouble()));
    }
  }
  
  public static void processRouteInfo(JSONArray jsonArray) throws JSONException {

    Gson gson = new Gson();
    RouteInfo routeInfo = gson.fromJson(jsonArray.getJSONObject(0).toString(), RouteInfo.class);
    MapsActivity.routeInfo = routeInfo;
//    List<RouteInfo.RouteDetail> routeDetails = new ArrayList<>();
//    String distance = null;
//    String duration = null;
//    for (int i = 0; i < jsonArray.length(); i++) {
//      JSONArray steps = jsonArray.getJSONObject(i).getJSONArray("steps");
//      processRouteDetail(routeDetails, steps);
//      distance = jsonArray.getJSONObject(i).getJSONObject("distance").getString("text");
//      duration = jsonArray.getJSONObject(i).getJSONObject("duration").getString("text");
//    }
//    MapsActivity.routeInfo = new RouteInfo(distance, duration, routeDetails);
  }
  
  // steps
  public static class Route {
    public JsonObject distance;
    public JsonObject duration;
    public JsonObject end_location;
    public String html_instructions;
    public String maneuver;
    public JsonObject polyline;
    public JsonObject start_location;
    public String travel_mode;
    public Route[] steps;
    public Path[] path;
  }
  
  public static class Path {
    public JsonElement lat;
    private JsonElement lng;

    public double getLatitude() {
      return lat.getAsDouble();
    }

    public double getLongitude() {
      return lng.getAsDouble();
    }

    public LatLng getLatLng() {
      return new LatLng(getLatitude(), getLongitude());
    }
  }

//  public static void processRouteDetail(List<RouteInfo.RouteDetail> routeDetails, JSONArray steps) {
//    try {
//      for (int i = 0; i < steps.length(); i++) {
//        Log.v("steps", steps.getJSONObject(i).length() + "");
//          JSONObject object = steps.getJSONObject(i);
//          String temp = "", travelMode = "", distance = "", duration = "", html_instructions = "";
//          if (object.has("travel_mode"))
//            travelMode = object.getString("travel_mode");
//          if (object.has("html_instructions"))
//            html_instructions = object.getString("html_instructions");
//          if (object.has("distance"))
//            distance = object.getJSONObject("distance").getString("text");
//          if (object.has("duration"))
//            duration = object.getJSONObject("duration").getString("text");
//          routeDetails.add(new RouteInfo.RouteDetail(duration,
//                  distance,
//                  html_instructions,
//                  travelMode,
//                  temp));
//          Log.v("distance", distance);
//          if(object.has("transit") && object.getJSONArray("transit").length() > 0) {
//
//          }
//          if (object.has("steps") && object.getJSONArray("steps").length() > 0) {
//            processRouteDetail(routeDetails, object.getJSONArray("steps"));
//          }
//      }
//
//    } catch (JSONException e) {
//      Log.e("ProcessRouteDetail", e.toString());
//    }
//  }
}
