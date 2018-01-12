package com.handsomelee.gotroute.Services;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.handsomelee.gotroute.Controller.MapsActivity;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.PlaceSearch;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * RequestHandler.
 * Attributes:
 *  public:
 *    boolean parkingStatus
 *    String api
 * Methods:
 *  public requestPlaceSearch(LatLng): void
 *
 * Dependency:
 *  MainActivity.
 */

public class RequestHandler {

  public static boolean parkingStatus;
  static String api = "AIzaSyCMY3fnCku6kSTiZ3AxnlCUC84YWQgUYqE";

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
