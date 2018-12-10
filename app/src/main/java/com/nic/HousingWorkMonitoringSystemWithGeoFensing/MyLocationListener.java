package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

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
		Log.d("gpsenabled",""+provider);
	}
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
}
