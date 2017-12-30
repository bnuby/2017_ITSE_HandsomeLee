package com.handsomelee.gotroute.Model;

import com.google.gson.JsonElement;

public class CarParking {
  private String name;
  private JsonElement available;
  
  // name Getter Method
  public String getName() {
    return name;
  }
  
  // available Getter Method
  public int getAvailable() {
    return available.getAsInt();
  }
  
}
