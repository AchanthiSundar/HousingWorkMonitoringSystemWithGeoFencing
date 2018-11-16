package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Upgradeapk extends Activity {
	 String dwnload_file_path = "http://www.tnrd.gov.in/project/downloads/TNWorkMonitoringSystem.apk";
	 int downloadedSize = 0;
	 int totalSize = 0;
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.firmware_upgrade_prgress);
		
		AlertDialog.Builder ab = new AlertDialog.Builder(Upgradeapk.this);
		ab.setMessage("Updates are available\nDo you want update?");
		ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				DownloadFiles();
			}
		});
		ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		ab.show();
		
	}

	private void DownloadFiles() {

        new Thread(new Runnable() {
            public void run() {
            	 try {
					downloadFile();
				} catch (FileNotFoundException e) {
					showError("File Not Found " + e);
				} catch (final MalformedURLException e) {
		    		showError("Error : MalformedURLException " + e);  		
		    		e.printStackTrace();
		    	} catch (final IOException e) {
		    		showError("Error : IOException " + e);  		
		    		e.printStackTrace();
		    	}catch (final Exception e) {
		    		showError("Error : Please check your internet connection " + e);
		    	}  
            }
          }).start(); 
	
		
	}
	void downloadFile() throws Exception{
		URL url = new URL(dwnload_file_path);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(true);

		//connect
		urlConnection.connect();

		//set the path where we want to save the file    		
		File SDCardRoot = Environment.getExternalStorageDirectory(); 
		//create a new file, to save the downloaded file 
		File file = new File(SDCardRoot,"TNWorkMonitoringSystem.apk");

		FileOutputStream fileOutput = new FileOutputStream(file);

		//Stream used for reading the data from the internet
		InputStream inputStream = urlConnection.getInputStream();

		//this is the total size of the file which we are downloading
		totalSize = urlConnection.getContentLength();

		runOnUiThread(new Runnable() {
		    public void run() {
		    	//pb.setMax(totalSize);
		    }			    
		});
		
		//create a buffer...
		byte[] buffer = new byte[1024];
		int bufferLength = 0;

		while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
			fileOutput.write(buffer, 0, bufferLength);
			downloadedSize += bufferLength;
			// update the progressbar //
			runOnUiThread(new Runnable() {
			    public void run() {
			    	float per = ((float)downloadedSize/totalSize) * 100;
			    }
			});
		}
		//close the output stream when complete //
		fileOutput.close();
		runOnUiThread(new Runnable() {
		    public void run() {
		    	Intent intent = new Intent(Intent.ACTION_VIEW);
			    intent.setDataAndType(Uri.fromFile(new File("/sdcard/TNWorkMonitoringSystem.apk")), "application/vnd.android.package-archive");
			    startActivity(intent);
			    System.exit(0);
		    }
		});    	
	}
	void showError(final String err){
    	runOnUiThread(new Runnable() {
		    public void run() {
		    	Toast.makeText(Upgradeapk.this, err, Toast.LENGTH_LONG).show();
		    	 System.exit(0);
		 }
	});
 }
}
