package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends Activity implements OnClickListener {

	//To Show Progressbar While Logging in
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	SitesList sitelist;
	static SharedPreferences preferences;
	public static ArrayList<byte[]> xml_emp_photo = new ArrayList<byte[]>();
	public static ArrayList<String> xml_emp_name = new ArrayList<String>();
	public static ArrayList<String> xml_emp_designation = new ArrayList<String>();
	public static ArrayList<String> xml_districtcode = new ArrayList<String>();
	public static ArrayList<String> xml_blockcode = new ArrayList<String>();
	public static ArrayList<String> xml_emp_last_visit_date = new ArrayList<String>();
	public static ArrayList<String> xml_serviceProvider = new ArrayList<String>();
	public static ArrayList<String> xml_version = new ArrayList<String>();

	TextView emp_name, emp_designation, emp_last_visit_date, service_provider;
	ImageView employee_photo;
	int pendingWorkCount;

	TextView bt_selectVillage, tv_pendingUpload, tv_getHousingWorks;
	public static TextView tv_pendingUploadCount;


	static final String KEY_ID = "id";
	static final String KEY_WORK_ID = "workid";
	static final String KEY_SCHEME_GROUP_NAME = "schemegrouppname";
	static final String KEY_SCHEME = "scheme";
	static final String KEY_SCHEME_ID = "schemeid";
	static final String KEY_FINANCIAL_YEAR = "financialyear";
	static final String KEY_AGENCY_NAME = "agencyname";
	static final String KEY_WORK_GROUP_NAME = "workgroupname";
	static final String KEY_WORK_GROUP_ID = "workgroupid";
	static final String KEY_WORK_NAME = "workname";
	static final String KEY_WORK_TYPE_ID = "worktypeid";
	static final String KEY_BLOCK = "block";
	static final String KEY_VILLAGE = "village";
	static final String KEY_VILLAGE_CODE = "villagecode";
	static final String KEY_STAGE_NAME = "stagename";
	static final String KEY_CURRENT_STAGE_OF_WORK = "currentstageofwork";
	static final String KEY_BENEFICIARY_NAME = "beneficiaryname";
	static final String KEY_BENEFICIARY_FATHER_NAME = "beneficiaryfhname";
	static final String KEY_WORK_TYPE_NAME = "worktypname";

	static final String KEY_BENEFICIARY_GENDER = "beneficiarygender";
	static final String KEY_BENEFICIARY_COMMUNITY = "beneficiarycommunity";
	static final String KEY_INITIAL_AMOUNT = "initalamount";
	static final String KEY_AMOUNT_SPENT_SO_FAR = "amountspentsofar";

	static final String KEY_LAST_VISITED_DATE = "upddate";

	SitesListPendingWorkList sitesListPendingWorkList;
	LocationManager mlocManager = null;
	LocationListener mlocListener;
	AlertDialog.Builder alert;
	double lat, longi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.dash_board);

		initializeViews();
		viewDetails();
	}

	public void onResume() {
		super.onResume();
		try {
			if (LoginScreen.placeData.getTotalCount() == 0) {
				RelativeLayout ll = (RelativeLayout) findViewById(R.id.ll_pendingUploads);
				ll.setVisibility(View.INVISIBLE);
			} else {
				RelativeLayout ll = (RelativeLayout) findViewById(R.id.ll_pendingUploads);
				ll.setVisibility(View.VISIBLE);
				tv_pendingUploadCount.setText("" + LoginScreen.placeData.getTotalCount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void viewDetails() {

		sitelist = MyXMLHandler.sitesList;
		try {
			int c = preferences.getInt("count", 1);
			if (c == 1) {
				ViewEmployeeDetailsTask task = new ViewEmployeeDetailsTask();
				task.execute(sitelist.getPhoto().get(0));
				//Log.i("URL", sitelist.getPhoto().get(i));

				c++;
				SharedPreferences.Editor editor = preferences.edit();
				editor.putInt("count", c);
				editor.commit();
			} else {
				viewEmployeeDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void viewEmployeeDetails() {
		Cursor cursors = getRawEvents("select * from details", null);
		while (cursors.moveToNext()) {

			LinearLayout ll = (LinearLayout) findViewById(R.id.ll_pendingWorks);
			ll.setVisibility(View.VISIBLE);

			LinearLayout ll1 = (LinearLayout) findViewById(R.id.LinearLayout04);
			ll1.setVisibility(View.VISIBLE);

			byte[] emp_image = cursors.getBlob(0);
			String emp_name1 = cursors.getString(1);
			String emp_designation1 = cursors.getString(2);
			String date = cursors.getString(5);
			String version = cursors.getString(6);
			String ser = cursors.getString(7);

			try {
				employee_photo.setImageBitmap(BitmapFactory.decodeByteArray(emp_image, 0, emp_image.length));
			} catch (NullPointerException e) {
				//Toast.makeText(getApplicationContext(), "Photo Url is Not Available..", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				//Toast.makeText(getApplicationContext(), "Photo Url is Not Available..", Toast.LENGTH_LONG).show();
			}

			emp_name.setText(emp_name1);
			emp_designation.setText(emp_designation1);
			emp_last_visit_date.setText(date);
			service_provider.setText(ser);


			//get app version
			PackageInfo pInfo = null;
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			String mobileVersion = pInfo.versionName;
//			Log.i("Version", version);

			float verNew = Float.parseFloat(version);
			float verOld = Float.parseFloat(mobileVersion);
			if (verNew > verOld) {
				Intent in5 = new Intent(Dashboard.this, Upgradeapk.class);
				startActivity(in5);
			}
		}
		//cursors.close();

	}

	private Cursor getRawEvents(String sql, String string) {
		String[] id = {string};
		Cursor cursor = LoginScreen.db.rawQuery(sql, null);

		startManagingCursor(cursor);
		return cursor;
	}

	private class ViewEmployeeDetailsTask extends AsyncTask<String, String, String> {
		byte[] b;

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		protected String doInBackground(String... urls) {
			try {
				String url = urls[0];
				//System.out.println("URL=" + url);
				b = LoadImageFromWebOperations(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

			xml_emp_photo.add(b);
			xml_emp_name.add(sitelist.getEmpName().get(0));
			xml_emp_designation.add(sitelist.getEmpDesignation().get(0));
			xml_districtcode.add(sitelist.getDistrictCode().get(0));
			xml_blockcode.add(sitelist.getBlockCode().get(0));
			xml_emp_last_visit_date.add(sitelist.getLastVisitDate().get(0));
			xml_serviceProvider.add(sitelist.getServiceProvider().get(0));
			xml_version.add(sitelist.getVersion().get(0));

			byte[] photo = xml_emp_photo.get(0);
			String emp_name = xml_emp_name.get(0).toString();
			String designation = xml_emp_designation.get(0).toString();
			String districtcode = xml_districtcode.get(0).toString();
			String blockcode = xml_blockcode.get(0).toString();
			String last_visit_date = xml_emp_last_visit_date.get(0)
					.toString();
			String version = xml_version.get(0).toString();
			String serviceProvider = xml_serviceProvider.get(0).toString();

			insertDataIntoEmployeeDetails(photo, emp_name, designation, districtcode, blockcode,
					last_visit_date, version, serviceProvider);

			try {
				viewEmployeeDetails();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (LoginScreen.placeData.getTotalCount() == 0) {
					RelativeLayout ll = (RelativeLayout) findViewById(R.id.ll_pendingUploads);
					ll.setVisibility(View.INVISIBLE);
				} else {
					RelativeLayout ll = (RelativeLayout) findViewById(R.id.ll_pendingUploads);
					ll.setVisibility(View.VISIBLE);
					tv_pendingUploadCount.setText("" + LoginScreen.placeData.getTotalCount());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] LoadImageFromWebOperations(String url)
			throws ClientProtocolException, IOException {
		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpGet mHttpGet = new HttpGet(url);
			HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);

			if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = mHttpResponse.getEntity();
				if (entity != null) {
					return EntityUtils.toByteArray(entity);
				}
			}
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
		return null;

	}

	private void insertDataIntoEmployeeDetails(byte[] emp_photo, String name,
											   String designation, String districtcode, String blockcode, String last_visit_date, String version, String serviceProvider) {

		ContentValues values;
		values = new ContentValues();

		values.put("employee_photo", emp_photo);
		values.put("emp_name", name);
		values.put("emp_designation", designation);
		values.put("districtcode", districtcode);
		values.put("blockcode", blockcode);
		values.put("emp_last_visit_date", last_visit_date);
		values.put("version", version);
		values.put("service_provider", serviceProvider);

		LoginScreen.db.insert("details", null, values);
	}

	public void onClick(View v) {
		if (v == tv_getHousingWorks) {
			if (MyLocationListener.latitude > 0) {
				lat = MyLocationListener.latitude;
				longi = MyLocationListener.longitude;
				(new GetHousingWorks()).execute();

			} else {
				alert = new AlertDialog.Builder(Dashboard.this);
				alert.setCancelable(true);
				alert.setTitle("GPS");
				alert.setMessage("GPS activation in progress,\n Please press back button to\n retake the photo");
				alert.setPositiveButton("OK", null);
				alert.show();
			}
		}
		if (v.equals(bt_selectVillage)) {
			startActivity(new Intent(Dashboard.this, VillageList.class));
		}
		if (v.equals(tv_pendingUpload)) {
			workid.clear();
			stage.clear();
			remarks.clear();
			latitude.clear();
			longitude.clear();
			image.clear();
			worktypeid.clear();
			getDataAndPopulate();
		}
	}

	private class GetHousingWorks extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);

			PostMethod.param = new String[4];
			PostMethod.paramValue = new String[4];

			PostMethod.param[0] = "schmgroup";
			PostMethod.paramValue[0] = "Housing";

			PostMethod.param[1] = "longitude";
			PostMethod.paramValue[1] = "" + longi;
			//	PostMethod.paramValue[1] = "77.35871784"; //77.23711276

			PostMethod.param[2] = "latitude";
			PostMethod.paramValue[2] = "" + lat;
			//	PostMethod.paramValue[2] = "11.20138061"; //	77.35871784 11.22741753

			PostMethod.param[3] = "buffered_size";
			PostMethod.paramValue[3] = "20";
		}

		protected String doInBackground(String... urls) {
			try {
				PostMethod po = new PostMethod(getApplicationContext(), "https://www.tnrd.gov.in/rd_service/find_workids.php");
				String sb = po.post1();
				Log.d("LOG_TAG", sb);

				if (sb.contains("<rd>")) {
					InputSource is = new InputSource();
					is.setCharacterStream(new StringReader(sb.toString()));
					try {
						/** Handling XML */
						SAXParserFactory spf = SAXParserFactory.newInstance();
						SAXParser sp = spf.newSAXParser();
						XMLReader xr = sp.getXMLReader();
						SitesListPendingWorkList.Clear();
						MyXMLHandlerPendingWorkList myMarkMyXmlHandler = new MyXMLHandlerPendingWorkList();
						xr.setContentHandler(myMarkMyXmlHandler);
						xr.parse(is);

					} catch (SAXException e) {
						System.out.println("XML = " + e);
					} catch (IOException e) {
						System.out.println("IOn = " + e);
					}
				} else {
					sb = "N";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onProgressUpdate(String... progress) {
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

			String id = "",
					workid = "",
					schemeid = "",
					schemegrouppname = "",
					scheme = "",
					financialyear = "",
					agencyname = "",
					workgroupname = "",
					workgroupid = "",
					workname = "",
					worktypeid = "",
					block = "",
					village = "",
					villagecode = "",
					stagename = "",
					currentstageofwork = "",
					beneficiaryname = "",
					beneficiaryfhname = "",
					worktypname = "",
					beneficiarygender = "",
					beneficiarycommunity = "",
					initalamount = "",
					amountspentsofar = "",
					updated_date = "";

			try {
				sitesListPendingWorkList = MyXMLHandlerPendingWorkList.sitesListPendingWorkList;
				Log.v("Size:", "" + sitesListPendingWorkList.getWorkId().size());
				for (int i = 0; i < sitesListPendingWorkList.getWorkId().size(); i++) {

					//id = sitesListPendingWorkList.getId().get(i).toString();
					id = "" + i;
					workid = sitesListPendingWorkList.getWorkId().get(i).toString();
					schemeid = sitesListPendingWorkList.getSchemeId().get(i).toString();
					schemegrouppname = sitesListPendingWorkList.getSchemeGroupName().get(i)
							.toString();
					scheme = sitesListPendingWorkList.getScheme().get(i).toString();
					financialyear = sitesListPendingWorkList.getFinancialYear().get(i)
							.toString();
					agencyname = sitesListPendingWorkList.getAgencyName().get(i).toString();
					workgroupname = sitesListPendingWorkList.getWorkGroupName().get(i)
							.toString();
					workgroupid = sitesListPendingWorkList.getWorkGroupId().get(i).toString();
					workname = sitesListPendingWorkList.getWorkName().get(i).toString();
					worktypeid = sitesListPendingWorkList.getWorkTypeId().get(i).toString();
					block = sitesListPendingWorkList.getBlock().get(i).toString();
					village = sitesListPendingWorkList.getVillage().get(i).toString();
					villagecode = sitesListPendingWorkList.getVillageCode().get(i).toString();
					stagename = sitesListPendingWorkList.getStageName().get(i).toString();
					currentstageofwork = sitesListPendingWorkList.getCurrentStageOfWork()
							.get(i).toString();
					beneficiaryname = sitesListPendingWorkList.getBeneficiaryName().get(i).toString();
					beneficiaryfhname = sitesListPendingWorkList.getBeneficiaryFatherName().get(i).toString();

					worktypname = sitesListPendingWorkList.getWorkTypeName().get(i).toString();


					beneficiarygender = sitesListPendingWorkList.getBeneficiarygender().get(i).toString();
					beneficiarycommunity = sitesListPendingWorkList.getBeneficiarycommunity().get(i).toString();
					initalamount = sitesListPendingWorkList.getInitialAmount().get(i).toString();
					amountspentsofar = sitesListPendingWorkList.getAmountSpentSoFar().get(i).toString();

					insertDataValue2(id, workid, schemegrouppname, schemeid, scheme,
							financialyear, agencyname, workgroupname,
							workgroupid, workname, worktypeid, block, village,
							villagecode, stagename, currentstageofwork, beneficiaryname,
							beneficiaryfhname, worktypname, beneficiarygender, beneficiarycommunity,
							initalamount, amountspentsofar,
							updated_date);
					String msg = "";
					if (beneficiarygender.equalsIgnoreCase("male")) {
						msg = "Mr." + beneficiaryname;
					} else if (beneficiarygender.equalsIgnoreCase("female")) {
						msg = "Mrs." + beneficiaryname;
					}
					showAlert(msg, villagecode);
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "No Record Found\nSelect Work Manualy", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}

	private void insertDataValue2(String id, String workid,
								  String schemegrouppname, String schemeid, String scheme, String financialyear,
								  String agencyname, String workgroupname, String workgroupid,
								  String workname, String worktypeid, String block, String village,
								  String villagecode, String stagename, String currentstageofwork,
								  String beneficiaryname, String beneficiaryfhname, String worktypname,
								  String beneficiarygender, String beneficiarycommunity,
								  String initalamount, String amountspentsofar, String updated_date) {

		//LoginScreen.db = LoginScreen.placeData.getWritableDatabase();
		ContentValues values;
		values = new ContentValues();

		values.put(KEY_ID, id);
		values.put(KEY_WORK_ID, workid);
		values.put(KEY_SCHEME_GROUP_NAME, schemegrouppname);
		values.put(KEY_SCHEME_ID, schemeid);
		values.put(KEY_SCHEME, scheme);
		values.put(KEY_FINANCIAL_YEAR, financialyear);
		values.put(KEY_AGENCY_NAME, agencyname);
		values.put(KEY_WORK_GROUP_NAME, workgroupname);
		values.put(KEY_WORK_GROUP_ID, workgroupid);
		values.put(KEY_WORK_NAME, workname);
		values.put(KEY_WORK_TYPE_ID, worktypeid);
		values.put(KEY_BLOCK, block);
		values.put(KEY_VILLAGE, village);
		values.put(KEY_VILLAGE_CODE, villagecode);
		values.put(KEY_STAGE_NAME, stagename);
		values.put(KEY_CURRENT_STAGE_OF_WORK, currentstageofwork);
		values.put(KEY_BENEFICIARY_NAME, beneficiaryname);
		values.put(KEY_BENEFICIARY_FATHER_NAME, beneficiaryfhname);
		values.put(KEY_WORK_TYPE_NAME, worktypname);

		values.put(KEY_BENEFICIARY_GENDER, beneficiarygender);
		values.put(KEY_BENEFICIARY_COMMUNITY, beneficiarycommunity);
		values.put(KEY_INITIAL_AMOUNT, initalamount);
		values.put(KEY_AMOUNT_SPENT_SO_FAR, amountspentsofar);

		values.put(KEY_LAST_VISITED_DATE, "");
		LoginScreen.db.delete("pendingworks", null, null);
		LoginScreen.db.insert("pendingworks", null, values);
	}

	static ArrayList<Integer> id = new ArrayList<Integer>();
	static ArrayList<String> workid = new ArrayList<String>();
	static ArrayList<String> stage = new ArrayList<String>();
	static ArrayList<String> remarks = new ArrayList<String>();
	static ArrayList<String> latitude = new ArrayList<String>();
	static ArrayList<String> longitude = new ArrayList<String>();
	static ArrayList<byte[]> image = new ArrayList<byte[]>();

	static ArrayList<String> schemeid = new ArrayList<String>();
	static ArrayList<String> fin_year = new ArrayList<String>();
	static ArrayList<String> stage_id = new ArrayList<String>();
	static ArrayList<String> worktypeid = new ArrayList<String>();
	static ArrayList<String> username = new ArrayList<String>();
	static String[] spinArray;

	private void getDataAndPopulate() {
		Cursor cursor = getEvents(LoginScreen.placeData.pendingTable);
		while (cursor.moveToNext()) {
			int ID = cursor.getInt(0);
			String WORK_ID = cursor.getString(1);
			String STAGE = cursor.getString(2);
			String REMARKS = cursor.getString(3);
			byte[] IMAGE = cursor.getBlob(4);
			String LATITUDE = cursor.getString(5);
			String LONGITUDE = cursor.getString(6);
			String schemeId = cursor.getString(7);
			String finYear = cursor.getString(8);
			String stageId = cursor.getString(9);
			String mworkId = cursor.getString(10);
			String userName = cursor.getString(11);

			id.add(ID);
			workid.add(WORK_ID);
			stage.add(STAGE);
			remarks.add(REMARKS);
			image.add(IMAGE);
			latitude.add(LATITUDE);
			longitude.add(LONGITUDE);
			schemeid.add(schemeId);
			fin_year.add(finYear);
			stage_id.add(stageId);
			worktypeid.add(mworkId);
			username.add(userName);

		}
		spinArray = (String[]) stage.toArray(new String[stage.size()]);
		if (workid.size() > 0) {
			try {
				startActivity(new Intent(Dashboard.this, PendingUpload.class));
				//finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(Dashboard.this, "There is no data to Upload",
					Toast.LENGTH_LONG).show();
		}
	}

	private Cursor getEvents(String table) {
		Cursor cursor = LoginScreen.db.query(table, null, null, null, null, null, null);
		startManagingCursor(cursor);
		return cursor;
	}

	private void initializeViews() {
		employee_photo = (ImageView) findViewById(R.id.emp_photo);
		emp_name = (TextView) findViewById(R.id.emp_name);
		emp_designation = (TextView) findViewById(R.id.designation);
		bt_selectVillage = (TextView) findViewById(R.id.pendingWorks);
		tv_pendingUpload = (TextView) findViewById(R.id.pendingUploads);
		tv_pendingUploadCount = (TextView) findViewById(R.id.pendingUploadCount);
		tv_getHousingWorks = (TextView) findViewById(R.id.tv_getHousingWorks);

		emp_last_visit_date = (TextView) findViewById(R.id.emp_last_visit_date);
		service_provider = (TextView) findViewById(R.id.footertxt);

		bt_selectVillage.setOnClickListener(this);
		tv_pendingUpload.setOnClickListener(this);
		tv_getHousingWorks.setOnClickListener(this);
		preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);


		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	}
	@Override
	public void onBackPressed(){
		AlertDialog.Builder ab = new AlertDialog.Builder(Dashboard.this);
		ab.setMessage("Do you want to exit?");
		ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				System.exit(0);
			}
		});
		ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		ab.show();
	}
	public void showAlert(String msg,final String villagecode){
		AlertDialog.Builder ab = new AlertDialog.Builder(Dashboard.this);
		ab.setTitle("House Detail's:");
		ab.setMessage("You standing on "+msg+" house.\nDo you wish to continue?");
		ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent in = new Intent(Dashboard.this, PendingWorkList.class);
				in.putExtra("vcode", villagecode);
				in.putExtra("mode", "geofence");
				startActivity(in);
			}
		});
		ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
             dialog.cancel();
			}
		});
		ab.show();
	}
	/*public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder ab = new AlertDialog.Builder(Dashboard.this);
			ab.setMessage("Do you want to exit?");
			ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					System.exit(0);
				}
			});
			ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

				}
			});
			ab.show();
		}
		return super.onKeyDown(keyCode, event);
	}*/
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS: // we set this to 0
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please Wait...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
}
