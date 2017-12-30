package com.handsomelee.gotroute.Services;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.handsomelee.gotroute.Controller.MapsActivity;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.DeviceInfo;
import com.handsomelee.gotroute.Model.GoogleRoute;
import com.handsomelee.gotroute.Model.Location;
import com.handsomelee.gotroute.Model.Report;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class DatabaseConnect {
  
  public static boolean directionStatus = true;
  public static boolean parkingStatus;
  static String secretKey = "12345";
  static List<LatLng> latLngList;
  private static Boolean navigationStatus = false;
  
  public static String[] fetchData(String db, String collection) {
    String url = "https://stitch.mongodb.com/api/client/v2.0/app/handsomelee-bxznj" +
            "/service/findCollection/incoming_webhook/FindCollection" +
            "?secret=" + secretKey + "&db=" + db + "&collection=" + collection;
    final String[] response = new String[2];
    StringRequest request = new StringRequest(url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        response[0] = s;
        Log.v("Success", "Fetch Data From MongoDB");
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.e("Failed", "Fetch Data From MongoDB");
      }
    });
    MainActivity.queue.add(request);
    return response;
  }
  
  public static void updateCarParking(final String name, final String available) {
    String url = "https://stitch.mongodb.com/api/client/v2.0/app/handsomelee-bxznj/" +
            "service/findCollection/incoming_webhook/Insert_And_Update_Car_Parking" +
            "?secret=" + secretKey;
    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        if (s.equals("true")) {
          Log.v("Success", "Insert Into Car Parking -> MongoDB\n" + s);
        } else {
          Log.v("Failed", "Insert Into Car Parking -> MongoDB\n" + s);
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.e("Failed to Connect", "Insert Into Car Parking -> MongoDB\n" + volleyError.toString());
      }
    }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.put("name", Base64.encodeToString(name.getBytes(), 0));
        map.put("available", available);
        return map;
      }
    };
    MainActivity.queue.add(request);
  }
  
  public static void insertReportData(final Report report) {
    String url = "https://stitch.mongodb.com/api/client/v2.0/app/handsomelee-bxznj" +
            "/service/findCollection/incoming_webhook/reportInsert?secret=" + secretKey;
    StringRequest objectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        Log.v("Success", s.toString());
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.v("Failed", volleyError.toString());
        
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        String location = String.format("{latitude:%s, longitude:%s}", report.getLocation().getLatitude(), report.getLocation().getLongitude());
        Map<String, String> map = new HashMap<>();
        map.put("location", location);
        map.put("time", report.getDateTime());
        map.put("type", report.getReportType());
        map.put("comment", report.getComment());
        map.put("user", DeviceInfo.getInstance().getId());
        return map;
      }
    };
    MainActivity.queue.add(objectRequest);
  }
  
  public static void fetchDirection(String s) {
    try {
      latLngList = new ArrayList<>();
      JSONObject jsonObject = new JSONObject(s);
      if (jsonObject.get("status").equals("ZERO_RESULTS")) {
        directionStatus = false;
        Toast.makeText(MainActivity.mActivity, "Can't Navigate", Toast.LENGTH_SHORT).show();
        return;
      }
      JSONArray legs = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
      Log.v("JSON", legs.toString());
      Gson gson = new Gson();
      GoogleRoute route[] = gson.fromJson(legs.toString(), GoogleRoute[].class);
      GoogleRoute.fetchLatLng(latLngList, route[0].steps);
      MainActivity.latLngs = latLngList;
      navigationStatus = true;
      GoogleRoute.processRouteInfo(legs);
      MapsActivity.configureRouteDetail();
    } catch (JSONException e) {
      e.printStackTrace();
      directionStatus = false;
    }
  }
  
  public static void updateUserLocation(final Long duration, final Location location) {
    String secret = "12345";
    String url = "https://stitch.mongodb.com/api/client/v2.0/app/handsomelee-bxznj/service" +
            "/findCollection/incoming_webhook/uploadLocation?secret=" + secret;
    final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        Log.v("Update User Location", "Sucess");
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Log.e("Update User Location", "Failed");
        
      }
    }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Date date = new Date();
        Map<String, String> map = new HashMap<>();
        map.put("id", DeviceInfo.getInstance().getId());
        map.put("location", location.toString());
        map.put("time", date.toString());
        map.put("duration", duration.toString());
        return map;
      }
    };
    MainActivity.queue.add(request);
  }
  
  // navigationStatus Getter Method
  public static Boolean getNavigationStatus() {
    Boolean a = navigationStatus;
    return a;
  }
  
  // NavigationStatus Setter Method
  public static void setNavigationStatus(Boolean navigationStatus) {
    DatabaseConnect.navigationStatus = navigationStatus;
  }
  
  
}
