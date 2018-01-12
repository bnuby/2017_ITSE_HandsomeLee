package com.handsomelee.gotroute.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * PlaceSearch.
 * Attributes:
 *  public:
 *    JsonArray htmlattributions
 *    searchDetail[] results
 *    String status
 *
 * Inner Class:
 *  searchDetail:
 *    Attributes:
 *      public:
 *        String name
 *        JsonObject opening_hours
 *        double rating
 *
 *    Methods:
 *      public:
 *        isOpen(): boolean
 *        getLatLng(): LatLng
 *        getLatitude(): double
 *        getLongitude(): double
 *
 *  Associate:
 *    JsonArray
 *
 */

public class PlaceSearch {
  public JsonArray html_attributions;
  public searchDetail[] results;
  public String status;
  
  public static class searchDetail {

    public JsonObject geometry;
    public String name;
    public JsonObject opening_hours;
    public JsonArray photos;
    public double rating;
    public String vicinity;
    
    public boolean isOpen() {
      return opening_hours.get("open_now").getAsBoolean();
    }
    
    public boolean hasPhotoReference() {
      return photos != null;
    }
    
    public String getPhotoReference() {
      return photos.get(0).getAsJsonObject().get("photo_reference").getAsString();
    }
    
    public LatLng getLatLng() {
      return new LatLng(getLatitude(), getLongitude());
    }
    
    public double getLatitude() {
      return geometry.getAsJsonObject("location").get("lat").getAsDouble();
    }
    
    public double getLongitude() {
      return geometry.getAsJsonObject("location").get("lng").getAsDouble();
    }
  }
  
}
