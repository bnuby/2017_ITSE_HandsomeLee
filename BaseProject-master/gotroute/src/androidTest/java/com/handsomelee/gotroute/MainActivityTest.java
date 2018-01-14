package com.handsomelee.gotroute;

import android.inputmethodservice.Keyboard;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import com.handsomelee.gotroute.Services.DatabaseConnect;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  final long seconds = 1000L;
  private UiDevice mDevice = null;
  
  @Rule
  public ActivityTestRule<MainActivity> mActivityRule =
          new ActivityTestRule<>(MainActivity.class);
    
  
  @Before
  public void startMainActivity() {
    mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
  }
  
  
  @Test
  public void ensureSettingPageWork() {
    
    onView(withChild(withText("Setting"))).perform(click());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.refreshET)).perform(replaceText("20"));
    onView(withId(R.id.ApplyButton)).perform(click());
    onView(isRoot()).perform(waitFor(3 * seconds));
    onView(withId(R.id.refreshET)).perform(replaceText("50"));
    onView(withId(R.id.ApplyButton)).perform(click());
    onView(isRoot()).perform(waitFor(3 * seconds));
    onView(withChild(withText("Map"))).perform(click());
    onView(isRoot()).perform(waitFor(3 * seconds));
    
  }
  
  @Test
  public void ensureNavigationWork() {

    onView(withId(R.id.mapView)).perform(longClick());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.navigationBtn)).perform(click());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView((withId(R.id.WalkingRadio))).perform(click());
    onView(isRoot()).perform(waitFor(4 * seconds));
    onView((withId(R.id.CyclingRadio))).perform(click());
    onView(isRoot()).perform(waitFor(4 * seconds));
    onView(withId(R.id.ListViewBtn)).perform(click());
    onView(withId(R.id.navigationBtn)).perform(click());
    onView(isRoot()).perform(waitFor(3 * seconds));
    
  }
  
  
  @Test
  public void ensureReportBtnWork() throws InterruptedException, UiObjectNotFoundException {
    
    onView(withId(R.id.ReportBtn)).perform(click());
    onView(withId(R.id.getMyLocation)).perform(click());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.mapView)).perform(swipeLeft());
    onView(isRoot()).perform(waitFor(500));
    onView(withId(R.id.mapView)).perform(longClick());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.ReportBtn)).perform(click());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.cancel_window)).perform(click());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.ReportBtn)).perform(click());
    onView(withId(R.id.send_window)).perform(click());
    onView(withId(R.id.comment_window)).perform(replaceText("Fcu"));
    onView(withText("Please Select One")).perform(click());
    sleep(2 * seconds);
    mDevice.findObject(new UiSelector().text("Temporary Inspection")).click();
    sleep(2 * seconds);
    onView(withId(R.id.send_window)).perform(click());
    
  }
  
  @Test
  public void ensureCarParkingBtnWork() throws UiObjectNotFoundException {

    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.CarParkingBtn)).perform(click());
    mDevice.findObject(new UiSelector().index(4)
                      .className("android.view.View")).click();
    onView(isRoot()).perform(waitFor(5 * seconds));
    onView(withId(R.id.CarParkingBtn)).perform(click());
    
  }
  
  @Test
  public void ensureMarkerWork() throws UiObjectNotFoundException {
    mDevice.findObject(new UiSelector()
            .descriptionMatches("Temporary Inspection. "))
            .click();
    onView(withId(R.id.getMyLocation)).perform(click());
  }
  
  @Test
  public void ensureParkingReportWork() {
    DatabaseConnect.updateCarParking("testing only", "0");
  }
  
  @Test
    public void ensureSearchBarWork() throws InterruptedException {
  
    try {
      mDevice.findObject(new UiSelector().resourceId("com.handsomelee.gotroute:id/place_autocomplete_search_input")
              .className("android.widget.EditText").index(1)).setText("yext");
      mDevice.findObject(new UiSelector().className("android.widget.ImageButton")
              .resourceId("com.handsomelee.gotroute:id/place_autocomplete_search_button"))
              .click();
      sleep(3 * seconds);
      mDevice.click(      mDevice.getDisplayWidth() / 2, (int)(mDevice.getDisplayHeight() * 0.2));
      sleep(2 * seconds);
//      onView(withChild(withText("New York, NY, United States"))).perform(click());
      onView(withId(R.id.navigationBtn)).perform(click());
      onView(isRoot()).perform(waitFor(3 * seconds));
      onView(withId(R.id.mapView)).perform(longClick());
      onView(withId(R.id.mapView)).perform(swipeLeft());
      onView(isRoot()).perform(waitFor(1 * seconds));
      onView(withId(R.id.mapView)).perform(click());
    } catch (UiObjectNotFoundException e) {
      e.printStackTrace();
    }
  
  }
  
  @Test
  public void ensureNotifyMessageWork() throws InterruptedException {
    sleep(1 * 60 * seconds + 30 * seconds);
  }
  
  
  public static ViewAction waitFor(final long millis) {
      return new ViewAction() {
          @Override
          public Matcher<View> getConstraints() {
              return isRoot();
          }
  
          @Override
          public String getDescription() {
              return "Wait for " + millis + " milliseconds.";
          }
  
          @Override
          public void perform(UiController uiController, final View view) {
              uiController.loopMainThreadForAtLeast(millis);
          }
      };
  }
  
  
}