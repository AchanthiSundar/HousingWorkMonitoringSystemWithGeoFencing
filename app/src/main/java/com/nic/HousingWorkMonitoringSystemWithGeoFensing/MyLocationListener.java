package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

    public static double latitude;
    public static double longitude;

    public void onLocationChanged(Location loc)
	{
		latitude=loc.getLatitude();
		longitude=loc.getLongitude();
	}

	public void onProviderDisabled(String provider)
	{
//		print "Currently GPS is Disabled";
	}
	public void onProviderEnabled(String provider)
	{
		//print "GPS got Enabled";
	}
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
}
