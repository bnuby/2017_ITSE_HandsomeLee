package com.handsomelee.gotroute.Services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.handsomelee.gotroute.Model.RouteInfo;

import java.util.List;

/**
 * RouteDetailAdapter.
 * Attributes:
 *  public:
 *    RouteInfo.RouteDetail[] routeDetails
 *
 * Methods:
 *  public:
 *    getView(int i, View view, ViewGroup viewGroup): View
 *
 * Associate:
 *  RouteInfo
 */

public class RouteDetailAdapter extends BaseAdapter {
  
  int layoutId;
  int[] viewId;
  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private RouteInfo.RouteDetail[] routeDetails;
  
  public RouteDetailAdapter(Context mContext, RouteInfo.RouteDetail[] routeDetails, int layoutId, int[] viewId) {
    this.mContext = mContext;
    this.routeDetails = routeDetails;
    this.layoutId = layoutId;
    this.viewId = viewId;
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    Log.v("routeinfo","count Inside" + routeDetails.length);
  }
  
  @Override
  public int getCount() {
    return routeDetails.length;
  }
  
  @Override
  public Object getItem(int i) {
    return routeDetails[i];
  }
  
  @Override
  public long getItemId(int i) {
    return i;
  }
  
  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    View rowView = mLayoutInflater.inflate(layoutId, viewGroup, false);
    ((TextView) rowView.findViewById(viewId[0])).setText(routeDetails[i].getDistance());
    ((TextView) rowView.findViewById(viewId[1])).setText(routeDetails[i].getDuration());
    ((TextView) rowView.findViewById(viewId[2])).setText(routeDetails[i].getInstructions());
    ((TextView) rowView.findViewById(viewId[3])).setText(routeDetails[i].getTravelMode());
    if (routeDetails[i].getTransit() != null)
      ((TextView) rowView.findViewById(viewId[4])).setText(routeDetails[i].getTransit().getBusNo());
    else {
      ((TextView) rowView.findViewById(viewId[4])).setVisibility(View.INVISIBLE);
      ((TextView) rowView.findViewById(viewId[4])).setHeight(0);
    }
    return rowView;
  }
}
