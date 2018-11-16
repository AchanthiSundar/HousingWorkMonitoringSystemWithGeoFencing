package Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil 
{
	public static boolean isNetworkAvailable(Context context)
	{
		Context mContext = context;
		try 
		{
			ConnectivityManager connectivity = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) 
			{
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) 
				{
					for (int i = 0; i < info.length; i++) 
					{
						if (info[i].getState() == NetworkInfo.State.CONNECTED) 
						{
							return true;
						}
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}
