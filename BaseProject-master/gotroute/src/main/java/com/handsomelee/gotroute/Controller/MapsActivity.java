package com.handsomelee.gotroute.Controller;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.R;
import com.handsomelee.gotroute.Services.GoogleMapSystem;

import static android.content.ContentValues.TAG;

public class MapsActivity extends GoogleMapSystem {
  
  private EditText editText;
  private static PlaceAutocompleteFragment autocompleteFragment;
  public static PlaceAutocompleteFragment origin;
  public static PlaceAutocompleteFragment destination;
  private Button getLocationBtn;
  private Button navigationBtn;
  private static String destinationString = "";
  
  public MapsActivity(int mapViewId, int layoutActivityId, int googleMapType) {
    super(mapViewId, layoutActivityId, googleMapType);
  }
  
  @Override
  public void addOn() {
    super.addOn();
    navigationBtn = (Button) rootView.findViewById(R.id.navigationBtn);
    autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.placeSearch);
    origin = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.Origin);
    destination = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.Destination);
    getLocationBtn = (Button) rootView.findViewById(R.id.getMyLocation);
    autocompleteFragment.getView().setBackgroundColor(Color.argb(255 / 100 * 95, 255, 255, 255));
    configureOriginAndDestination();
    
    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        Log.i(TAG, "Place: " + place.getName());
        destinationString = place.getName().toString();
        Log.v("Place Address", place.getName().toString());
        placeMarker(place);
        showNavigationBtn();
        showOriginAndDestination();
      }
      
      @Override
      public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(TAG, "An error occurred: " + status);
      }
    });
    
    autocompleteFragment.getView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        view.setVisibility(View.GONE);
      }
    });
    
    autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        autocompleteFragment.setText("");
        removeMarker();
        hideNavigationBtn();
      }
    });
    
    getLocationBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng(MainActivity.getLocation().getLatitude(), MainActivity.getLocation().getLongitude()))
                        .zoom(15)
                        .build()
        ));
      }
    });
  }
  
  public void showNavigationBtn() {
    navigationBtn.setClickable(true);
    navigationBtn.setVisibility(View.VISIBLE);
    navigationBtn.animate().setDuration(1000).y(1902).alpha(1).start();
  }
  
  public void hideNavigationBtn() {
    navigationBtn.setClickable(false);
    navigationBtn.animate().setDuration(1000).y(2102).alpha(0).withEndAction(new Runnable() {
      @Override
      public void run() {
        navigationBtn.setVisibility(View.INVISIBLE);
      }
    }).start();
  }
  
  public void configureAutoComplete(PlaceAutocompleteFragment autocompleteFragment) {
    autocompleteFragment.getView().setBackgroundColor(Color.rgb( 255, 255, 255));
    autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.INVISIBLE);
    autocompleteFragment.getView().setAlpha(0);
    autocompleteFragment.getView().setClickable(false);
    autocompleteFragment.getView().setVisibility(View.INVISIBLE);
    
  }
  
  public void configureOriginAndDestination() {
    configureAutoComplete(origin);
    configureAutoComplete(destination);
    origin.getView().setY(-300);
    destination.getView().animate().y(-100).start();
  }
  
  public static void hideOriginAndDestination() {
    origin.getView().setClickable(false);
    destination.getView().setClickable(false);
    origin.getView().animate().setDuration(600).y(-300).alpha(0).start();
    destination.getView().animate().setDuration(600).y(-100).alpha(0).withEndAction(new Runnable() {
      @Override
      public void run() {
        origin.getView().setVisibility(View.INVISIBLE);
        destination.getView().setVisibility(View.INVISIBLE);
        Log.v("Origin", origin.getView().getY() + "," + destination.getView().getY());
      }
    }).start();
    
    autocompleteFragment.getView().animate().setDuration(600).alpha(1).y(30).start();
  }
  
  public static void showOriginAndDestination() {
    origin.getView().setClickable(true);
    destination.getView().setClickable(true);
    origin.getView().animate().setDuration(600).alpha(1).y(30).start();
    destination.getView().animate().setDuration(600).alpha(1).y(230).start();
    origin.getView().setVisibility(View.VISIBLE);
    destination.getView().setVisibility(View.VISIBLE);
    autocompleteFragment.getView().animate().setDuration(600).alpha(0).y(-200).start();
  }
  
  public void placeMarker(Place place) {
    if (place == null) {
      return;
    }
    removeMarker();
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(place.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker()).title(place.getName().toString());
    marker = mMap.addMarker(markerOptions);
    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(place.getLatLng())      // Sets the center of the map to Mountain View
            .zoom(15)                   // Sets the zoom
            .bearing(0)                // Sets the orientation of the camera to east
            .build();                   // Creates a CameraPosition from the builder
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    markerType = MARKERTYPE.Name;
  }
  
  public void removeMarker() {
    if (marker != null) {
      marker.remove();
    }
  }
  
  @Override
  public void onMapLongClick(LatLng latLng) {
    super.onMapLongClick(latLng);
    hideOriginAndDestination();
    removePolyline();
    navigationBtn.setText("navigation");
    autocompleteFragment.setText(String.format("%.4f,\t%.4f", latLng.latitude, latLng.longitude));
    showNavigationBtn();
  }
  
  public static String getDestinationString() {
    return destinationString;
  }
  
  // navigationBtn Getter Method
  public Button getNavigationBtn() {
    return navigationBtn;
  }
  
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    MainActivity.removeFragment(R.id.placeSearch);
  }
}
