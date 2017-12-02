package com.handsomelee.gotroute;

import android.util.Log;
import android.widget.EditText;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.ContentValues.TAG;

public class MapsActivity extends GoogleMapSystem {
  
  private EditText editText;
  private PlaceAutocompleteFragment autocompleteFragment;
  
  public MapsActivity(int mapViewId, int layoutActivityId, int googleMapType) {
    super(mapViewId, layoutActivityId, googleMapType);
    
  }
  
  @Override
  public void addOn() {
    super.addOn();
    autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.placeSearch);
    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        Log.i(TAG, "Place: " + place.getName());
        placeMarker(place);
        
      }
      
      @Override
      public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(TAG, "An error occurred: " + status);
      }
    });
  }
  
  public void placeMarker(Place place) {
    if (place == null) {
      return;
    }
    if (marker != null) {
      marker.remove();
    }
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(place.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker()).title(place.getName().toString());
    marker = mMap.addMarker(markerOptions);
    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(place.getLatLng())      // Sets the center of the map to Mountain View
        .zoom(15)                   // Sets the zoom
        .bearing(0)                // Sets the orientation of the camera to east
        .build();                   // Creates a CameraPosition from the builder
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }
  
  
  //  private static final String LOGTAG = "MapFragment";
//  private GoogleMap mMap;
//  private Marker marker;
//  private GoogleApiClient mGoogleApiClient;
//  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
//  private static final float DEFAULT_ZOOM = 15f;
//  private LocationRequest mLocationRequest;
//  private Location mLastKnownLocation;
//
//  @Override
//  public void onCreate(Bundle bundle) {
//    super.onCreate(bundle);
//
////    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//
//  }
//
//  @Override
//  public void onMapReady(GoogleMap googleMap) {
//    mMap = googleMap;
//
//    Log.v(LOGTAG, "map is ready");
//
//    LatLng sydney = new LatLng(-34, 151);
//    mMap.setOnMyLocationButtonClickListener(this);
//    mMap.setOnMyLocationClickListener(this);
//    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
////    getPermission();
//    EnableMyLocationSetting();
//    Log.v(LOGTAG, "Added Sydney");
//
//  }
//
//  public MapsActivity() {
//  }
//
//  public void onSearch(View view) {
//    EditText editText = (EditText) getActivity().findViewById(R.id.editText);
//    String location = editText.getText().toString();
//    List<Address> addressList = null;
//    if(location != null && !location.equals("") ) {
//      Geocoder geocoder = new Geocoder(getActivity());
//      try {
//        addressList = geocoder.getFromLocationName(location, 1);
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//
//      Address address = addressList.get(0);
//      LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//      mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//      mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//    }
//  }
//
//
//  public void EnableMyLocationSetting() {
//    while(!mMap.isMyLocationEnabled()) {
//      if(MainActivity.checkGPSPermission(getActivity())){
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setZoomGesturesEnabled(true);
//        mMap.setOnMapLongClickListener(this);
//      }
//    }
//  }
//
//
//  @Override
//  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//    super.onActivityCreated(savedInstanceState);
//    getMapAsync(this);
//  }
//
//  @Override
//  public boolean onMyLocationButtonClick() {
//    Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
//    // Return false so that we don't consume the event and the default behavior still occurs
//    // (the camera animates to the user's current position).
//    return false;
//  }
//
//  @Override
//  public void onMyLocationClick(@NonNull Location location) {
//
//    Toast.makeText(getActivity(), "Current location:\nLatitude : " + location.getLatitude() + "\nLongtitude : " + location.getLongitude(), Toast.LENGTH_LONG).show();
//  }
//
//  @Override
//  public void onMapLongClick(LatLng latLng) {
//    if(marker != null) {
//      marker.remove();
//    }
//    MarkerOptions markerOptions = new MarkerOptions();
//    markerOptions.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//    marker = mMap.addMarker(markerOptions);
//  }
  
  /**
   * bestLocation Getter Method
   */
//  public Location getLastBestLocation() {
//    Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//    Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//    long GPSLocationTime = 0;
//    if(locationGPS != null) {
//      GPSLocationTime = locationGPS.getTime();
//    }
//    long NetLocationTime = 0;
//    if(locationNet != null) {
//      NetLocationTime = locationNet.getTime();
//    }
//
//    if(GPSLocationTime - NetLocationTime > 0){
//      return locationGPS;
//    } else {
//      return locationNet;
//    }
//
//  }

//  public Location getNewLocation(){
//    boolean isNetworkEnabled, isGPSEnabled;
//    try {
//      isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//      isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//      if(!isNetworkEnabled & !isGPSEnabled){
//
//      } else {
//        if(isNetworkEnabled){
//          mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 20, 10, this);
//          if()
//        }
//      }
//
//    } catch (Exception e){
//      e.printStackTrace();
//    }
//  }
}
