package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import Util.NetworkUtil;
import Util.PlaceDataSQL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PreviewPage extends Activity implements OnClickListener  {
	
	private static PlaceDataSQL placeData;
	SQLiteDatabase db;
	
	ImageView taken_photo,home, back;
	String fin_year,schemeId,stage_id,worktypeid;
	
	TextView stage,remarks,workId,service_provider,latitude,longitude,title;
	RelativeLayout remark_RL;
	Button save,upload;
	ArrayList<String> al_param = new ArrayList<String>();
	ArrayList<String> al_param_value = new ArrayList<String>();
	
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
	LocationManager mlocManager=null;
	LocationListener mlocListener;
	AlertDialog.Builder alert;
	
	String user_name;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.preview_page);
		
		placeData = new PlaceDataSQL(this);
		db = placeData.getWritableDatabase();
		
		al_param.clear();
		al_param_value.clear();
		
		initialize();
		previewValues();
		setServiceProvider();
	}

	private void setServiceProvider() {
		
		Cursor cursors = getRawEvents("select * from details", null);
		while (cursors.moveToNext()) {
			String ser = cursors.getString(6);
			// d = cursors.getString(3);
			// b = cursors.getString(4);
			service_provider.setText(ser);
		}
		//cursors.close();
	}

	private void previewValues() {
		
		fin_year = PendingWorkDetailSpinner.fin_year;
		schemeId = PendingWorkDetailSpinner.schemeId;
		stage_id = PendingWorkDetailSpinner.selectedStageCode;
		worktypeid = PendingWorkDetailSpinner.workTypeId;
		
		workId.setText(PendingWorkDetailSpinner.workId);
		stage.setText(PendingWorkDetailSpinner.selectedStageName);
		if(PendingWorkDetailSpinner.remarks.equals("")||PendingWorkDetailSpinner.remarks.equals(null)){
			remark_RL.setVisibility(View.GONE);
		}
		remarks.setText(PendingWorkDetailSpinner.remarks);
		
//		Log.i("fin_year", fin_year);
		Bitmap bm = BitmapFactory.decodeByteArray(PendingWorkDetailSpinner.getCameraImage(), 0, PendingWorkDetailSpinner.getCameraImage().length);
		taken_photo.setImageBitmap(bm);
		
		alert  = new AlertDialog.Builder(PreviewPage.this);
		alert.setCancelable(true);
		
//		latitude.setText("12.7364876985");
//		longitude.setText("80.298364986");
		
		if (MyLocationListener.latitude > 0) {
			String latTextValue = "" + MyLocationListener.latitude;
			String lanTextValue = "" + MyLocationListener.longitude;
			
			latitude.setText(latTextValue);
			longitude.setText(lanTextValue);
		}  else {
			alert.setTitle("GPS");
			alert.setMessage("GPS activation in progress,\n Please press back button to\n retake the photo");
			alert.setPositiveButton("OK", null);
			alert.show();
		}
		
	}

	private void initialize() {
		
		workId = (TextView)findViewById(R.id.tv_workidspin_workId);
		taken_photo = (ImageView)findViewById(R.id.taken_photo);
		stage = (TextView)findViewById(R.id.tv_workidspin_stage);
		remarks = (TextView)findViewById(R.id.tv_workidspin_ramark);
		latitude = (TextView)findViewById(R.id.tv_latitude);
		longitude = (TextView)findViewById(R.id.tv_longitude);
		remark_RL = (RelativeLayout)findViewById(R.id.Linear_Layout_remarks);
		save = (Button)findViewById(R.id.but_workidspin_save);
		title = (TextView)findViewById(R.id.title_tv);
		title.setText("CaptureImage");
		save.setOnClickListener(this);
		
		upload = (Button) findViewById(R.id.but_workidspin_upload);
		upload.setOnClickListener(this);
		
		home = (ImageView) findViewById(R.id.homeimg);
		home.setOnClickListener(this); 
		
		back = (ImageView) findViewById(R.id.backimg);
		back.setOnClickListener(this);
		
		service_provider = (TextView) findViewById(R.id.footertxt);
	}

	public void onClick(View v) {
		if(v.equals(upload)){
			if(NetworkUtil.isNetworkAvailable(getApplicationContext())){		
				uploadPhoto();			
			} else {
				Toast.makeText(getApplicationContext(), "Network Connection Not Available...", Toast.LENGTH_LONG).show();
			}
		} if(v.equals(home)) {
			Intent Pendingwork = new Intent(PreviewPage.this,Dashboard.class);
			Pendingwork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(Pendingwork);		
			finish();
			super.onBackPressed();
			overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
		} if(v.equals(back)) {
			onBackPress();
		} if(v.equals(save)) {
			LoginScreen.preferences = PreferenceManager.getDefaultSharedPreferences(PreviewPage.this);
			user_name = LoginScreen.preferences.getString("user_name","");
			try {
				placeData.AddPendingUploads(PendingWorkDetailSpinner.workId,
						PendingWorkDetailSpinner.selectedStageName,
						PendingWorkDetailSpinner.remarks, 
						latitude.getText().toString(),
						longitude.getText().toString(),
						PendingWorkDetailSpinner.getCameraImage(),
						schemeId,fin_year,stage_id,worktypeid,user_name);
			
				Toast.makeText(PreviewPage.this, "Data has been Saved Successfully..", Toast.LENGTH_LONG).show();
				Dashboard.tv_pendingUploadCount.setText(""+placeData.getTotalCount());
			
				Intent Pendingwork = new Intent(PreviewPage.this,Dashboard.class);
				Pendingwork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(Pendingwork);
				finish();
				super.onBackPressed();
				overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		} 
	}
	public void onBackPress(){
		 super.onBackPressed();
		setResult(Activity.RESULT_CANCELED);
		overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
	}
	private void uploadPhoto() {
		 
		Bitmap bitmap = PendingWorkDetailSpinner.getCameraBitmap();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeBytes(byte_arr);
		
		LoginScreen.preferences = PreferenceManager.getDefaultSharedPreferences(PreviewPage.this);
		user_name = LoginScreen.preferences.getString("user_name","");
		
		al_param.add("txtwkid");
		al_param.add("txtremarks");
		al_param.add("image");
		al_param.add("lat");
		al_param.add("lan");
		
		al_param.add("schemeid");
		al_param.add("fin_year");
		al_param.add("stage_id");
		al_param.add("mworkid");
		al_param.add("username");
		
		al_param.add("mode");
		
		
		al_param_value.add(PendingWorkDetailSpinner.workId);
		al_param_value.add(PendingWorkDetailSpinner.remarks);
		al_param_value.add(image_str);
		al_param_value.add(latitude.getText().toString());
		al_param_value.add(longitude.getText().toString());
		
		al_param_value.add(schemeId);
		al_param_value.add(fin_year);
		al_param_value.add(stage_id);
		al_param_value.add(worktypeid);
		al_param_value.add(user_name);
		
		al_param_value.add("online");
		
		PostMethod.param = new String[al_param.size()];
		PostMethod.paramValue = new String[al_param.size()];
		
		for(int i=0;i<al_param.size();i++){
			
			PostMethod.param[i] = al_param.get(i).toString();
			PostMethod.paramValue[i] = al_param_value.get(i).toString();
		}
		
		try {
			Upload task = new Upload();
			task.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class Upload extends AsyncTask<String, String, String> {
		String response ;
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		protected String doInBackground(String... urls) {
			PostMethod po = new PostMethod(getApplicationContext(), "https://www.tnrd.gov.in/project/webservices_forms/sample_photo.php");
			//PostMethod po = new PostMethod(getApplicationContext(), "http://10.163.14.137/rdwebtraining/project_new/webservices_forms/sample_photo.php");
			response = po.post();
			//Log.d("Response", response);
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d("LOG_TAG", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
//			Log.d("LOG_TAG", response);
			try {
				if (response.equals("Saved")) {
					Toast.makeText(PreviewPage.this,
							"Data Uploaded Successfully", Toast.LENGTH_LONG)
							.show();

					Intent Pendingwork = new Intent(PreviewPage.this,Dashboard.class);
					Pendingwork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(Pendingwork);		
					finish();
					overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
				}
				if (response.equals("Failed")) {
					Toast.makeText(PreviewPage.this, "Error In Data Upload",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{
		case DIALOG_DOWNLOAD_PROGRESS: //we set this to 0
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please Wait..Your Data Is Getting Uploaded..");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	private Cursor getRawEvents(String sql, String string) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		startManagingCursor(cursor);
		return cursor;
	}
	@Override
	public void onBackPressed(){
		onBackPress();
	}
}
