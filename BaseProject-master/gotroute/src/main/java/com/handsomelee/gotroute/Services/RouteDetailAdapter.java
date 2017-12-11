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

public class RouteDetailAdapter extends BaseAdapter {
  
  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private List<RouteInfo.RouteDetail> routeDetails;
  int layoutId;
  int[] viewId;
  
  public RouteDetailAdapter(Context mContext, List<RouteInfo.RouteDetail> routeDetails, int layoutId, int[] viewId) {
    this.mContext = mContext;
    this.routeDetails = routeDetails;
    this.layoutId = layoutId;
    this.viewId = viewId;
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }
  
  @Override
  public int getCount() {
    
    Log.v("routeDetails", "" + routeDetails.size());
    return routeDetails.size();
  }
  
  @Override
  public Object getItem(int i) {
    return routeDetails.get(i);
  }
  
  @Override
  public long getItemId(int i) {
    return i;
  }
  
  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    View rowView = mLayoutInflater.inflate(layoutId, viewGroup, false);
    ((TextView) rowView.findViewById(viewId[0])).setText(routeDetails.get(i).distance);
    ((TextView) rowView.findViewById(viewId[1])).setText(routeDetails.get(i).duration);
    ((TextView) rowView.findViewById(viewId[2])).setText(routeDetails.get(i).html_instructions);
    ((TextView) rowView.findViewById(viewId[3])).setText(routeDetails.get(i).travel_mode);
    ((TextView) rowView.findViewById(viewId[4])).setText(routeDetails.get(i).maneuver);
    return rowView;
  }
}
