package com.handsomelee.gotroute.Services;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.DeviceInfo;
import com.handsomelee.gotroute.Model.Report;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConnect {
  
  static String secretKey = "12345";
  
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
        map.put("name", name);
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
  
  
}
