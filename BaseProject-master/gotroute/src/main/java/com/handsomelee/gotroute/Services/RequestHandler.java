package com.handsomelee.gotroute.Services;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.handsomelee.gotroute.Controller.MapsActivity;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.GoogleRoute;
import com.handsomelee.gotroute.Model.PlaceSearch;
import com.handsomelee.gotroute.Model.RouteInfo;
import com.handsomelee.gotroute.Model.SnapToRoad;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestHandler {
  //  static List<LatLng> latLngList;
//  public static boolean directionStatus = true;
  public static boolean parkingStatus;
  static String api = "AIzaSyCMY3fnCku6kSTiZ3AxnlCUC84YWQgUYqE";
//  private static Boolean navigationStatus = false;


//  public static boolean requestDirection(final Activity activity, DirectionType type, String orgn, String dest) {
//    directionStatus = true;
//    String origin = orgn;
//    String destination = dest;
//
//    String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&mode=" + mode + "&key=" + api;
//    Log.v("url", url);
//    final String finalMode = mode;
//    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//      @Override
//      public void onResponse(String s) {
//        Log.v(finalMode, s);
//        try {
//          latLngList = new ArrayList<>();
//          JSONObject jsonObject = new JSONObject(s);
//          Log.v("jsonObject", jsonObject.toString());
//          if (jsonObject.get("status").equals("ZERO_RESULTS")) {
//            directionStatus = false;
//            Toast.makeText(activity, "Can't Navigate", Toast.LENGTH_SHORT).show();
//            return;
//          }
//          JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
//          Gson gson = new Gson();
//          GoogleRoute route = gson.fromJson(jsonArray.getString(0), GoogleRoute.class);
//          fetchLatLng(route.steps);
//          latLngList.add(new LatLng(route.end_location.get("lat").getAsDouble(), route.end_location.get("lng").getAsDouble()));
//
//          MainActivity.latLngs = latLngList;
//          navigationStatus = true;
//          processRouteInfo(jsonArray);
//          MapsActivity.configureRouteDetail();
//        } catch (JSONException e) {
//          Log.e("Error", "Exceoption on request data\n" + e.toString());
//          directionStatus = false;
//        }
//      }
//    }, new Response.ErrorListener() {
//      @Override
//      public void onErrorResponse(VolleyError volleyError) {
//        Log.v("volleyError", volleyError.toString());
//      }
//    });
//    MainActivity.queue.add(stringRequest);
//    return true;
//  }

//  public static void requestSnapToRoad(String url) {
//    Log.v("url", url);
//    StringRequest stringRequest1 = new StringRequest(url, new Response.Listener<String>() {
//      @Override
//      public void onResponse(String s) {
//        try {
//          JSONObject jsonObject = new JSONObject(s);
//          Gson gson = new Gson();
//          SnapToRoad snapToRoad = gson.fromJson(jsonObject.toString(), SnapToRoad.class);
//          latLngList = new ArrayList<>();
//
//          Log.v("SnapToRoad", snapToRoad.snappedPoints[0].placeId);
//          for (SnapToRoad.SnapPoint snapPoint : snapToRoad.snappedPoints) {
//            latLngList.add(new LatLng(snapPoint.location.getLatitude(), snapPoint.location.getLongitude()));
//          }
//          MainActivity.latLngs = latLngList;
//          navigationStatus = true;
//        } catch (JSONException e) {
//          e.printStackTrace();
//          directionStatus = false;
//        }
//      }
//    }, new Response.ErrorListener() {
//      @Override
//      public void onErrorResponse(VolleyError volleyError) {
//        Log.e("Error", "SnapToRoad Error " + volleyError.toString());
//      }
//    });
//    MainActivity.queue.add(stringRequest1);
//  }
  
  public static JSONObject convertLatLngToAddress(LatLng latLng) throws JSONException {
    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&key=" + api;
    final String[] data = {""};
    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        data[0] = s;
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.e("Exception", "Lat Long Request Fail!\n" + volleyError.toString());
      }
    });
    
    MainActivity.queue.add(request);
    Log.v("JsonObject", data[0]);
    
    if (data[0].equals("")) {
      return null;
    }
    
    JSONObject jsonObject = new JSONObject(data[0]);
    Log.v("JsonObject", jsonObject.toString());
    return jsonObject;
  }
  
  // NavigationStatus Setter Method
//  public static void setNavigationStatus(Boolean navigationStatus) {
//    RequestHandler.navigationStatus = navigationStatus;
//  }
//
//  // navigationStatus Getter Method
//  public static Boolean getNavigationStatus() {
//    Boolean a = navigationStatus;
//    return a;
//  }
//
//  private static void fetchLatLng(GoogleRoute.Route[] steps) {
//    for (GoogleRoute.Route step : steps) {
//      JsonObject location = step.start_location;
//      latLngList.add(new LatLng(location.get("lat").getAsDouble(), location.get("lng").getAsDouble()));
//      if (step.steps != null && step.steps.length > 0) {
//        fetchLatLng(step.steps);
//      }
//    }
//  }

//  public static void processRouteInfo(JSONArray jsonArray) throws JSONException {
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
//  }
//
//  public static void processRouteDetail(List<RouteInfo.RouteDetail> routeDetails, JSONArray steps) {
//    try {
//      Log.v("steps", steps.getJSONObject(0).length() + "");
//      for (int j = 0; j < steps.getJSONObject(0).length(); j++) {
//        JSONObject object = steps.getJSONObject(j);
//        String temp = "", travelMode = "", distance = "", duration = "", html_instructions = "";
//        if (object.has("travel_mode"))
//          travelMode = object.getString("travel_mode");
//        if (object.has("html_instructions"))
//          html_instructions = object.getString("html_instructions");
//        if (object.has("distance"))
//          distance = object.getJSONObject("distance").getString("text");
//        if (object.has("duration"))
//          duration = object.getJSONObject("duration").getString("text");
//        routeDetails.add(new RouteInfo.RouteDetail(duration,
//                distance,
//                html_instructions,
//                travelMode,
//                temp));
//        Log.v("distance", distance);
//        if (object.has("steps") && object.getJSONArray("steps").length() > 0) {
//          processRouteDetail(routeDetails, object.getJSONArray("steps"));
//        }
//      }
//    } catch (JSONException e) {
//      Log.e("ProcessRouteDetail", e.toString());
//    }
//  }
  
  public static void requestPlaceSearch(LatLng latLng, String keyword, String type) {
    String latLngString = latLng.latitude + "," + latLng.longitude;
    final String url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
            "?location=%s&radius=1000&type=%s&keyword=%s&key=%s", latLngString, type, keyword, api);
    StringRequest request = new StringRequest(url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        Log.v("PlaceSearch", url);
        Gson gson = new Gson();
        PlaceSearch placeSearch = gson.fromJson(s, PlaceSearch.class);
        MainActivity.placeSearch = placeSearch;
        RequestHandler.parkingStatus = true;
        MapsActivity.processParking();
        
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.e("", "Error in PlaceSearch " + volleyError.toString());
      }
    });
    MainActivity.queue.add(request);
  }
  
  
}
