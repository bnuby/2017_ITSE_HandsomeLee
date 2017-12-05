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
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.GoogleRoute;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestDirection {
  
  public enum DirectionType{
    Driving, Walking, Transit, Cycling
  }
  static List<LatLng> latLngList;
  public static boolean status;
  
  public static boolean requestDirection(final Activity activity, DirectionType type, String orgn, String dest) {
    status = true;
    String api = "AIzaSyCMY3fnCku6kSTiZ3AxnlCUC84YWQgUYqE";
    String origin = orgn;
    String destination = dest;
    String mode = "";
    switch(type) {
      case Walking:
        mode = "walking";
        break;
      case Cycling:
        mode = "cycling";
        break;
      case Transit:
        mode = "transit";
        break;
      case Driving:
      default:
        mode = "driving";
    }
    String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination+ "&mode=" + mode + "&key=" + api;
    Log.v("url",url);
    final String finalMode = mode;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        Log.v(finalMode, s);
        try {
          latLngList = new ArrayList<>();
          JSONObject jsonObject = new JSONObject(s);
          if(jsonObject.get("status").equals("ZERO_RESULTS")) {
            status = false;
            Toast.makeText(activity, "Can't Navigate", Toast.LENGTH_SHORT).show();
            return;
          }
          JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
          Gson gson = new Gson();
          GoogleRoute route = gson.fromJson(jsonArray.getString(0), GoogleRoute.class);
          latLngList.add(new LatLng(route.start_location.get("lat").getAsDouble(), route.start_location.get("lng").getAsDouble()));
          for(GoogleRoute.Route step : route.steps) {
            JsonObject location = step.start_location;
            latLngList.add(new LatLng(location.get("lat").getAsDouble(), location.get("lng").getAsDouble()));
          }
          latLngList.add(new LatLng(route.end_location.get("lat").getAsDouble(), route.end_location.get("lng").getAsDouble()));
          MainActivity.latLngs = latLngList;
        } catch (JSONException e) {
          status = false;
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.v("volleyError", volleyError.toString());
      }
    });
    MainActivity.queue.add(stringRequest);
    return true;
  }
  
  public static JSONObject convertLatLngToAddress(LatLng latLng) throws JSONException {
    String api = "AIzaSyCMY3fnCku6kSTiZ3AxnlCUC84YWQgUYqE";
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
        Log.e("Exception", "Lat Long Request Fail!");
      }
    });
    
    MainActivity.queue.add(request);
    Log.v("JsonObject", data[0]);
    
    if(data[0].equals("")){
      return null;
    }
    
    JSONObject jsonObject = new JSONObject(data[0]);
    Log.v("JsonObject", jsonObject.toString());
    return jsonObject;
  }
}
