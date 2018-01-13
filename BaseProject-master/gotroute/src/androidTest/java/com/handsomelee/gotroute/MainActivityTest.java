package com.handsomelee.gotroute;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

  final long seconds = 1000L;
  
  @Rule
  public ActivityTestRule<MainActivity> mActivityRule =
          new ActivityTestRule<>(MainActivity.class);
  
  @Test
  public void ensureNavigationWork() {                                                            

    onView(withId(R.id.mapView)).perform(longClick());
    onView(isRoot()).perform(waitFor(TimeUnit.SECONDS.toMillis(1)));
    onView(withId(R.id.navigationBtn)).perform(click());
    onView(isRoot()).perform(waitFor(TimeUnit.SECONDS.toMillis(1)));
    onView(withId(R.id.navigationBtn)).perform(click());
    
  }
  
  @Test
  public void ensureCarParking() {
    onView(withId(R.id.getMyLocation)).perform(click());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.CarParkingBtn)).perform(click());
    onView(isRoot()).perform(waitFor(1 * seconds));
    onView(withId(R.id.CarParkingBtn)).perform(click());
    
  }
  
  @Test
  public void ensureReportBtn() {
    
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