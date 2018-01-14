package com.handsomelee.gotroute.Controller;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.google.gson.Gson;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.Model.*;
import com.handsomelee.gotroute.R;
import com.handsomelee.gotroute.Services.DatabaseConnect;
import com.handsomelee.gotroute.Services.GoogleMapSystem;
import com.handsomelee.gotroute.Services.RouteDetailAdapter;

import java.text.DateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;


/**
 * MapsActivity Class.
 *
 * Attributes:
 *  public:
 *    ListView listView
 *    RouteInfo routeInfo
 *  private:
 *    String destinationString = ""
 *
 * Method:
 *  public:
 *    MapsActivity(int, int, int)
 *    requestDirection(String, String, DirectionType): void
 *    processReport(): void
 *    processParking(): void
 *    getDestination(): String
 *    placeMarker(Place): void
 *
 *  Dependency:
 *    PlaceSearch
 *    DatabaseConnect
 *    Location
 *    Report
 *
 *  Association:
 *    DeviceInfo
 *
 *  Aggregation:
 *    RouteInfo
 *
 *  Composite:
 *    CarParking
 *    ParkingWindow
 *    MainActivity
 *
 *  Composition:
 *    MainActivity
 *
 *  Inner Enum
 *    public:
 *      ProgressType:
 *        Navigation, Free
 *
 *      DirectionType:
 *        Driving, Walking, Transit, Cycling
 *
 */
public class MapsActivity extends GoogleMapSystem implements PlaceSelectionListener
        , GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
  private static final Handler refreshHandler = new Handler();
  public static PlaceAutocompleteFragment origin;
  public static PlaceAutocompleteFragment destination;
  public static RadioGroup navigationRadioGroup;
  public static ListView listView;
  public static LinearLayout listLinearLayoutView;
  public static RouteDetailAdapter mAdapter;
  public static RouteInfo routeInfo;
  public static Button listViewBtn;
  public static Marker[] carParkingMarkers;
  public static Marker[] reportMarkers;
  private static PlaceAutocompleteFragment autocompleteFragment;
  private static String destinationString = "";
  private static ProgressType progressType = ProgressType.Free;
  private static long refreshSecond;
  private Button getLocationBtn;
  private Button navigationBtn;
  
  public MapsActivity(int mapViewId, int layoutActivityId, int googleMapType) {
    super(mapViewId, layoutActivityId, googleMapType);
  }
  
  public static void requestDirection(String origin, String destination, DirectionType type) {
    String url = "http://www.kebohan.com/gibson.php";
//    String url = "http://192.168.31.38/index.php";
    String mode = "";
    switch (type) {
      case Walking:
        mode = "walking";
        break;
      case Cycling:
        mode = "bicycling";
        break;
      case Transit:
        mode = "transit";
        break;
      case Driving:
      default:
        mode = "driving";
    }
    WebView webView = MainActivity.mActivity.findViewById(R.id.webView);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(new WebViewClient() {
      
      @Override
      public void onPageFinished(final WebView view, String url) {
        Log.v("Finished", url);
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            view.evaluateJavascript("(function() { return document.querySelector(\"#json\").innerHTML })()",
                    new ValueCallback<String>() {
                      @Override
                      public void onReceiveValue(String s) {
                        String string = convertStandardJSONString(s);
                        DatabaseConnect.fetchDirection(string);
                      }
                    });
          }
        }, 2000);
        super.onPageFinished(view, url);
      }
    });
    webView.loadUrl(String.format("%s?origin=%s&destination=%s&travelMode=%s&id=%s", url, origin, destination, mode, DeviceInfo.getInstance().getId()));
  }
  
  public static String convertStandardJSONString(String data_json) {
    data_json = data_json.substring(1, data_json.length() - 1);
    data_json = data_json.replace("\\", "");
    data_json = data_json.replace("&gt;", "");
    data_json = data_json.replace("div style=\"font-size:0.9em\"", "");
    data_json = data_json.replace("&lt;", "");
    return data_json;
  }
  
  public static void closeListView() {
    listViewBtn.setText("^");
    listLinearLayoutView.animate().setDuration(600).y(MainActivity.getHeight()).start();
  }
  
  public static void showListView() {
    listViewBtn.setText("v");
    listLinearLayoutView.setVisibility(View.VISIBLE);
    listLinearLayoutView.animate().setDuration(600).y(MainActivity.getHeight() - listLinearLayoutView.getHeight()).start();
  }
  
  public static void hideListView() {
    listViewBtn.setText("^");
    listLinearLayoutView.animate().setDuration(600).y(MainActivity.getHeight() - 40).start();
    
  }
  
  public static void hideOriginAndDestination() {
    origin.getView().setClickable(false);
    destination.getView().setClickable(false);
    origin.getView().animate().setDuration(600).y(-300).alpha(0).start();
    navigationRadioGroup.animate().setDuration(600).y(-70).alpha(0).start();
    destination.getView().animate().setDuration(600).y(-100).alpha(0).withEndAction(new Runnable() {
      @Override
      public void run() {
        origin.getView().setVisibility(View.INVISIBLE);
        destination.getView().setVisibility(View.INVISIBLE);
      }
    }).start();
    
    autocompleteFragment.getView().animate().setDuration(600).alpha(1).y(MainActivity.calculateHeight(85.33)).start();
  }
  
  public static void showOriginAndDestination() {
    origin.getView().setClickable(true);
    destination.getView().setClickable(true);
    origin.getView().animate().setDuration(600).alpha(1).y(MainActivity.calculateHeight(85.34)).start();
    destination.getView().animate().setDuration(600).alpha(1).y(MainActivity.calculateHeight(10.8) + MainActivity.calculateHeight(85.34) + 20).start();
    origin.getView().setVisibility(View.VISIBLE);
    destination.getView().setVisibility(View.VISIBLE);
    navigationRadioGroup.animate().y(2 * (MainActivity.calculateHeight(10.8)) + MainActivity.calculateHeight(85.34) + 40).alpha(1).start();
    autocompleteFragment.getView().animate().setDuration(600).alpha(0).y(-200).start();
  }
  
  public static void removeParkingMarker() {
    if (carParkingMarkers != null && carParkingMarkers.length > 0) {
      for (Marker i : carParkingMarkers) {
        i.remove();
      }
    }
  }
  
  // RefreshSecond Setter Method
  public static void updateRefreshSecond() {
    MapsActivity.refreshSecond = DeviceInfo.getInstance().getRefreshTime() * 1000;
  }
  
  public static void processReport() {
    final String[] fetchData = DatabaseConnect.fetchData("handsomelee", "reports");
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        while (mMap == null) ;
        while (fetchData[0] == null) {
          System.out.print("buffer");
        }
        MainActivity.mActivity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Gson gson = new Gson();
            Report.fetchReport[] reports = gson.fromJson(fetchData[0], Report.fetchReport[].class);
            if (reportMarkers != null && reportMarkers.length > 0) {
              for (Marker marker : reportMarkers) {
                marker.remove();
                marker = null;
              }
            }
            reportMarkers = new Marker[reports.length];
            for (int i = 0; i < reports.length; i++) {
              MarkerOptions options = new MarkerOptions()
                      .title(reports[i].type)
                      .position(reports[i].getLatLng())
                      .snippet(reports[i].comment);
              switch (reports[i].type) {
                case "Temporary Inspection":
                  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.temporary_inspection));
                  break;
                case "Road Block":
                  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.road_block));
                  break;
                case "Mobile Speed Track":
                  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.speed_track));
                  break;
              }
              reportMarkers[i] = mMap.addMarker(options);
            }
            fetchData[0] = null;
          }
        });
        return null;
      }
    }.execute();
  }
  
  public static void processParking() {
    removeParkingMarker();
    final PlaceSearch placeSearch = MainActivity.placeSearch;
    if (MainActivity.placeSearch != null && placeSearch.status.equals("OK")) {
      MainActivity.mActivity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (placeSearch.results != null) {
            carParkingMarkers = new Marker[placeSearch.results.length];
            
            for (int i = 0; i < placeSearch.results.length; i++) {
              int available = 0;
              String title = "";
              int open_now = 0;
              if (placeSearch.results[i].name != null) {
                title = placeSearch.results[i].name;
              }
              if (placeSearch.results[i].opening_hours != null && placeSearch.results[i].opening_hours.has("open_now")) {
                if (placeSearch.results[i].opening_hours.get("open_now").getAsBoolean()) {
                  open_now = 1;
                } else {
                  open_now = -1;
                }
                Log.v("open", "" + open_now);
              }
              
              MarkerOptions markerOptions = new MarkerOptions()
                      .position(placeSearch.results[i].getLatLng())
                      .title(title)
                      .snippet(open_now + "," + available)
                      .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking));
              final String finalTitle = title;
              final int finalI = i;
              mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                  Log.v("title", finalTitle);
                  Log.v("index", finalI + "");
                  ParkingWindow.updateButton(marker.getTitle(), marker.getSnippet().split(",")[1], marker);
                }
              });
              carParkingMarkers[i] = mMap.addMarker(markerOptions);
              carParkingMarkers[i].setTag(String.format("parking"));
            }
            new AsyncTask<Void, Void, Void>() {
              @Override
              protected Void doInBackground(Void... voids) {
                final String fetchData[] = DatabaseConnect.fetchData("handsomelee", "carParking");
                while (fetchData[0] == null) {
                  System.out.print("buffer");
                };
                MainActivity.mActivity.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    Gson gson = new Gson();
                    final CarParking[] carParkings = gson.fromJson(fetchData[0], CarParking[].class);
                    for (Marker marker : carParkingMarkers) {
                      for (CarParking carParking : carParkings) {
                        Log.v("Parking", carParkings.length+""+carParkings[0].getAvailable());
                        Log.v("Parking", fetchData[0]);
                        Log.v("Parking1", "asda");
                        Log.v("Parking2", marker.getTitle());
                        if (carParking.getName().equals(Base64.encodeToString(marker.getTitle().getBytes(), 0))) {
                          Log.v("Parking", "Found");
                          String snippet = marker.getSnippet().split(",")[0];
                          marker.setSnippet(snippet + "," + carParking.getAvailable());
                        }
                      }
                    }
                  }
                });
                
                return null;
              }
            }.execute();
          } else {
            Toast.makeText(MainActivity.mActivity, "Parking Not Found.", Toast.LENGTH_LONG);
          }
        }
      });
    } else {
      Toast.makeText(MainActivity.mActivity, "Parking Request Failed.", Toast.LENGTH_LONG);
    }
  }
  
  public static String getDestinationString() {
    return destinationString;
  }
  
  // progressType Getter Method
  public static ProgressType getProgressType() {
    return progressType;
  }
  
  // ProgressType Setter Method
  public static void setProgressType(ProgressType progressType) {
    MapsActivity.progressType = progressType;
  }
  
  public static void configureRouteDetail() {
    int viewId[] = {R.id.Distance, R.id.duration, R.id.html_instructions, R.id.travel_mode, R.id.maneuver};
    mAdapter = new RouteDetailAdapter(MainActivity.mActivity, routeInfo.steps, R.layout.simple_list, viewId);
    listView.setAdapter(mAdapter);
  }
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    autoRefresh();
    
  }
  
  private void autoRefresh() {
    refreshHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        processReport();
        autoRefresh();
      }
    }, refreshSecond);
  }
  
  @Override
  public void addOn() {
    super.addOn();
    listViewBtn = rootView.findViewById(R.id.ListViewBtn);
    listLinearLayoutView = rootView.findViewById(R.id.ListViewParent);
    final Button reportBtn = (Button) rootView.findViewById(R.id.ReportBtn);
    navigationBtn = (Button) rootView.findViewById(R.id.navigationBtn);
    autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.placeSearch);
    origin = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.Origin);
    destination = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.Destination);
    navigationRadioGroup = (RadioGroup) rootView.findViewById(R.id.navigationRadioGroup);
    getLocationBtn = (Button) rootView.findViewById(R.id.getMyLocation);
    ProgressBar progressBar = new ProgressBar(getActivity());
    progressBar.setLayoutParams(new AbsListView.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
    progressBar.setIndeterminate(true);
    listView = (ListView) rootView.findViewById(R.id.ListView);
    listView.setEmptyView(progressBar);
    autocompleteFragment.getView().setBackgroundColor(Color.argb(255 / 100 * 95, 255, 255, 255));
    configureRadioGroup();
    configureOriginAndDestination();
    afterScreenLoaded();
    autocompleteFragment.setOnPlaceSelectedListener(this);
    destination.setOnPlaceSelectedListener(this);
    processReport();
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
                        .target(MainActivity.getLocationSystem().getLatLng())
                        .zoom(15)
                        .build()
        ));
      }
    });
    reportBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        reportProcessBtn();
      }
    });
  }
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
    super.onMapReady(googleMap);
    mMap.setOnMarkerClickListener(this);
    mMap.setOnMapClickListener(this);
    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(24.178855,120.645879)));
    
  }
  
  private void afterScreenLoaded() {
    rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        configureListView();
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
      }
    });
  }
  
  public void showNavigationBtn() {
    navigationBtn.setClickable(true);
    navigationBtn.setVisibility(View.VISIBLE);
    navigationBtn.animate().setDuration(1000).y(MainActivity.getHeight() - navigationBtn.getHeight() - 30).alpha(1).start();
  }
  
  public void hideNavigationBtn() {
    navigationBtn.setClickable(false);
    navigationBtn.animate().setDuration(1000).y(MainActivity.getHeight()).alpha(0).withEndAction(new Runnable() {
      @Override
      public void run() {
        navigationBtn.setVisibility(View.INVISIBLE);
      }
    }).start();
  }
  
  public void configureListView() {
    listLinearLayoutView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MainActivity.calculateHeight(100.0 / 50)));
    listLinearLayoutView.animate().y(MainActivity.getHeight() + 100).start();
  }
  
  public void configureRadioGroup() {
    navigationRadioGroup.setY(-570);
    navigationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
          case R.id.CyclingRadio:
            MainActivity.directionType = DirectionType.Cycling;
            MainActivity.requestNavigation(new Button(getContext()));
            break;
          case R.id.DrivingRadio:
            MainActivity.directionType = DirectionType.Driving;
            MainActivity.requestNavigation(new Button(getContext()));
            break;
          case R.id.TransitRadio:
            MainActivity.directionType = DirectionType.Transit;
            MainActivity.requestNavigation(new Button(getContext()));
            break;
          case R.id.WalkingRadio:
            MainActivity.directionType = DirectionType.Walking;
            MainActivity.requestNavigation(new Button(getContext()));
            break;
        }
      }
    });
  }
  
  public void configureAutoComplete(PlaceAutocompleteFragment autocompleteFragment) {
    autocompleteFragment.getView().setBackgroundColor(Color.rgb(255, 255, 255));
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
  
  public void reportProcessBtn() {
    if (marker != null) {
      final String[] reportType = new String[1];
      final String dateTime = DateFormat.getDateTimeInstance().format(new Date());
      final Report[] report = new Report[1];
      final AlertDialog alertDialog;
      AlertDialog.Builder mBuilder = new AlertDialog.Builder(rootView.getContext());
      final View mView = MainActivity.mActivity.getLayoutInflater().inflate(R.layout.report_window, null);
      String[] array_spinner = getResources().getStringArray(R.array.reports_array);
      ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.support_simple_spinner_dropdown_item, array_spinner);
      Spinner spinner = mView.findViewById(R.id.spinner_window);
      spinner.setAdapter(adapter);
      spinner.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.calculateWidth(100 / 80), ViewGroup.LayoutParams.WRAP_CONTENT));
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
          reportType[0] = adapterView.getSelectedItem().toString();
        }
        
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
      });
      Log.v("view", "Start");
      final EditText comment = mView.findViewById(R.id.comment_window);
      comment.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.calculateWidth(100 / 80), MainActivity.calculateHeight(8)));
      Button send = mView.findViewById(R.id.send_window);
      Button cancel = mView.findViewById(R.id.cancel_window);
      mBuilder.setView(mView);
      alertDialog = mBuilder.create();
      cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          alertDialog.dismiss();
        }
      });
      
      send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (!reportType[0].equals("Please Select One")) {
            report[0] = new Report(dateTime, reportType[0], comment.getText().toString(), new Location(marker.getPosition().latitude, marker.getPosition().longitude));
            DatabaseConnect.insertReportData(report[0]);
            processReport();
            alertDialog.dismiss();
          } else {
            Toast.makeText(MainActivity.mActivity, "Please select report type.", Toast.LENGTH_SHORT).show();
          }
        }
      });
      alertDialog.show();
    } else {
      Toast.makeText(MainActivity.mActivity, "Please put marker", Toast.LENGTH_LONG).show();
    }
  }
  
  @Override
  public boolean onMarkerClick(Marker marker) {
    if (marker.getTag() != null) {
      if (((String) marker.getTag()).equals("parking")) {
        mMap.setInfoWindowAdapter(new ParkingWindow(MainActivity.mActivity));
      }
    } else {
      mMap.setInfoWindowAdapter(null);
    }
    marker.showInfoWindow();
    return true;
  }
  
  @Override
  public void onMapClick(LatLng latLng) {
    if (progressType == ProgressType.Free) {
      autocompleteFragment.setText("");
      removeMarker();
      hideNavigationBtn();
    }
  }
  
  @Override
  public void onMapLongClick(LatLng latLng) {
    super.onMapLongClick(latLng);
    hideOriginAndDestination();
    removePolyline();
    closeListView();
    navigationBtn.setText("navigation");
    MapsActivity.setProgressType(ProgressType.Free);
    autocompleteFragment.setText(String.format("%.4f,\t%.4f", latLng.latitude, latLng.longitude));
    if (mMap.getCameraPosition().zoom <= 15 || mMap.getCameraPosition().zoom >= 15) {
      CameraPosition cameraPosition = new CameraPosition.Builder()
              .target(latLng)
              .zoom(15)
              .build();
      MapsActivity.getmMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    showNavigationBtn();
  }
  
  // navigationBtn Getter Method
  public Button getNavigationBtn() {
    return navigationBtn;
  }
  
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    MainActivity.removeFragment(R.id.placeSearch);
    MainActivity.removeFragment(R.id.Origin);
    MainActivity.removeFragment(R.id.Destination);
  }
  
  @Override
  public void onPlaceSelected(Place place) {
    // TODO: Get info about the selected place.
    destinationString = place.getName().toString();
    placeMarker(place);
    showNavigationBtn();
  }
  
  @Override
  public void onError(Status status) {
    // TODO: Handle the error.
    Log.i(TAG, "An error occurred: " + status);
  }
  
  public enum ProgressType {
    Navigation, Free
  }
  
  public enum DirectionType {
    Driving, Walking, Transit, Cycling
  }
  
}
