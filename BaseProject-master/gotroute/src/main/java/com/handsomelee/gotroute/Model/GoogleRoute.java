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

/**
 * GoogleRoute.
 * Attributes:
 *  public:
 *    JsonObject distance
 *    JsonObject duration
 *    String end_address
 *    JsonObject end_location;
 *    String start_address;
 *    JsonObject start_location;
 *    Route[] steps
 *
 * Methods:
 *  public:
 *    fetchLatLng(List<LatLng>, GoogleRoute.Route[]): void
 *    processRouteInfo(JSONArray): void
 *
 * Inner class:
 *  Route.
 *
 *  Attributes
 *    public:
 *      JsonObject distance
 *      JsonObject duration
 *      JsonObject end_location
 *      JsonObject start_location;
 *
 *  Inner class:
 *    Path:
 *      Attributes:
 *        private:
 *          JsonElement lat
 *          JsonElement lng
 *
 *      Methods:
 *        public:
 *          getLatitude(): double
 *          getLongtitude(): double
 *
 */

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
//      if (step.steps != null && step.steps.length > 0) {
//        fetchLatLng(latLngList, step.steps);
//      }
      JsonObject end_location = step.end_location;
      latLngList.add(new LatLng(end_location.get("lat").getAsDouble(),
              end_location.get("lng").getAsDouble()));
    }
  }
  
  public static void processRouteInfo(JSONArray jsonArray) throws JSONException {

    Gson gson = new Gson();
    RouteInfo routeInfo = gson.fromJson(jsonArray.getJSONObject(0).toString(), RouteInfo.class);
    MapsActivity.routeInfo = routeInfo;
    Log.v("routeinfo",routeInfo.steps.length+"");
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
    private JsonElement lat;
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

}
